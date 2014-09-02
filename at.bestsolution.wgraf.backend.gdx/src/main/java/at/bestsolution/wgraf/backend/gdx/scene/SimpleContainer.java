package at.bestsolution.wgraf.backend.gdx.scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import at.bestsolution.wgraf.backend.gdx.GdxConverter;
import at.bestsolution.wgraf.style.Background;
import at.bestsolution.wgraf.style.Backgrounds;
import at.bestsolution.wgraf.style.BaseBackground;
import at.bestsolution.wgraf.style.Border;
import at.bestsolution.wgraf.style.BorderStroke;
import at.bestsolution.wgraf.style.FillBackground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Group;

public class SimpleContainer extends Group {

	private Background background;
	private Border border;
	
	private ShapeRenderer renderer = new ShapeRenderer();
	
	public void setBackground(Background background) {
		this.background = background;
	}
	
	public void setBorder(Border border) {
		this.border = border;
	}
	private static ShaderProgram shader;
	private ShaderProgram getShader() {
		if (shader == null) {
			shader =  new ShaderProgram(
					Gdx.files.classpath("at/bestsolution/wgraf/backend/gdx/scene/SimpleContainer.vert.glsl").readString(), 
					Gdx.files.classpath("at/bestsolution/wgraf/backend/gdx/scene/SimpleContainer.frag.glsl").readString());
			shader.enableVertexAttribute("a_position");
			if (shader.isCompiled() == false) {
				Gdx.app.log("ShaderTest", shader.getLog());
				Gdx.app.exit();
			}
			System.err.println("my attribs: " + Arrays.toString(shader.getAttributes()));
			System.err.println("my uniforms: " + Arrays.toString(shader.getUniforms()));
		}
		return shader;
	}
	
	Matrix4 combinedMatrix = new Matrix4();
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.end();
		
		Matrix4 trans = new Matrix4(batch.getTransformMatrix());
		trans.translate(getX(), getY(), 0);
	    combinedMatrix.set(batch.getProjectionMatrix()).mul(trans);
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glDepthMask(true);
		
	    // render background
	 	drawBackground(renderer);
	 	// renderBorder
	 	drawBorder(renderer);
		
	 	batch.begin();
		// render children
		super.draw(batch, parentAlpha);
	}
	
	
	
	
	private void drawBackground(ShapeRenderer renderer) {
		if (background != null) {

			if (background instanceof Backgrounds) {
				List<BaseBackground> bg = new ArrayList<BaseBackground>(
						((Backgrounds) background).backgrounds);
				Collections.reverse(bg);
				for (BaseBackground b : bg) {
					drawBackground(renderer, b);
				}
			} else if (background instanceof BaseBackground) {
				drawBackground(renderer, (BaseBackground) background);
			}
		}
	}
	
	private void drawBorder(ShapeRenderer renderer) {
		if (border != null) {
			for (BorderStroke s : border.strokes) {
				drawBorderStroke(renderer, s);
			}
		}
	}
	
	private void drawBorderStroke(ShapeRenderer renderer, BorderStroke stroke) {
		renderer.setColor(GdxConverter.convert(stroke.paint));
	
		// TODO width = ??
//		pen.setWidthF(stroke.widths.top);
		
		float width = getWidth() -1;
		float height = getHeight() -1;
		
		float left = (float)stroke.insets.left;
		float right = width - (float)stroke.insets.right;
		
		float top = (float)stroke.insets.top;
		float bottom = height - (float)stroke.insets.bottom;
		
		if (
		Double.isNaN(width) ||
		Double.isNaN(height) ||
		Double.isNaN(left) ||
		Double.isNaN(right) ||
		Double.isNaN(top) ||
		Double.isNaN(bottom)
		) {
			System.err.println("nan: width=" + width + ", height="+height + ", left=" + left + ", right=" + right + ", top="+top + ", bottom="+ bottom);
			throw new RuntimeException("NaN value!");
		}
		
		if (
		Double.isNaN(stroke.cornerRadii.bottomLeftHorizontalRadius) ||
		Double.isNaN(stroke.cornerRadii.bottomRightHorizontalRadius) ||
		Double.isNaN(stroke.cornerRadii.topLeftHorizontalRadius) ||
		Double.isNaN(stroke.cornerRadii.topRightHorizontalRadius) ||
		Double.isNaN(stroke.cornerRadii.bottomLeftVerticalRadius) ||
		Double.isNaN(stroke.cornerRadii.bottomRightVerticalRadius) ||
		Double.isNaN(stroke.cornerRadii.topLeftVerticalRadius) ||
		Double.isNaN(stroke.cornerRadii.topRightVerticalRadius)
		) {
			System.err.println("nan: " + stroke.cornerRadii);
			throw new RuntimeException("NaN value!");
		}
		
		renderer.begin(ShapeType.Line);
		float rTopLeft = (float)stroke.cornerRadii.topLeftHorizontalRadius;
		float rTopRight = (float)stroke.cornerRadii.topRightHorizontalRadius;
		float rBottomRight = (float)stroke.cornerRadii.bottomRightHorizontalRadius;
		float rBottomLeft = (float)stroke.cornerRadii.bottomLeftHorizontalRadius;
		// top line
		renderer.line(left + rTopLeft, top, right - rTopRight, top);
		// top right arc
		renderer.arc(right - rTopRight * 2, top, rTopRight * 2, -90, 90);
		// right line
		renderer.line(right, top + rTopRight, right, bottom - rBottomRight);
		// bottom right arc
		renderer.arc(right - rBottomRight * 2, bottom - rBottomRight * 2 , rBottomRight, 0, -90);
		// bottom line
		renderer.line(right - rBottomRight, bottom, left + rBottomLeft, bottom);
		// bottom left arc
		renderer.arc(left, bottom - rBottomLeft * 2, rBottomLeft, -90, -90);
		// left line
		renderer.line(left, bottom - rBottomLeft, left, top - rTopLeft);
		// top left arc
		renderer.arc(left, top, rTopLeft, -180, 90);
		
		renderer.end();
		
	}

	private void drawBackground(ShapeRenderer renderer, BaseBackground bg) {
		if (bg instanceof FillBackground) {
			drawBackground(renderer, (FillBackground)bg);
		}
	}
	
	private void drawBackground(ShapeRenderer renderer, FillBackground bg) {
		float width = getWidth();
		float height = getHeight();
		
		float left = (float)bg.insets.left;
		float right =(float) (width - bg.insets.right);
		
		float top = (float) bg.insets.top;
		float bottom = (float) (height - bg.insets.bottom);
		
		
		if (
		Double.isNaN(width) ||
		Double.isNaN(height) ||
		Double.isNaN(left) ||
		Double.isNaN(right) ||
		Double.isNaN(top) ||
		Double.isNaN(bottom)
		) {
			System.err.println("nan: width=" + width + ", height="+height + ", left=" + left + ", right=" + right + ", top="+top + ", bottom="+ bottom);
			throw new RuntimeException("NaN value!");
		}
		
		if (
		Double.isNaN(bg.cornerRadii.bottomLeftHorizontalRadius) ||
		Double.isNaN(bg.cornerRadii.bottomRightHorizontalRadius) ||
		Double.isNaN(bg.cornerRadii.topLeftHorizontalRadius) ||
		Double.isNaN(bg.cornerRadii.topRightHorizontalRadius) ||
		Double.isNaN(bg.cornerRadii.bottomLeftVerticalRadius) ||
		Double.isNaN(bg.cornerRadii.bottomRightVerticalRadius) ||
		Double.isNaN(bg.cornerRadii.topLeftVerticalRadius) ||
		Double.isNaN(bg.cornerRadii.topRightVerticalRadius)
		) {
			System.err.println("nan: " + bg.cornerRadii);
			throw new RuntimeException("NaN value!");
		}
		//List<Vec2d> points = new ArrayList<Vec2d>();
		
		
		Mesh mesh = new RoundRectRenderer(left, top, right, bottom, bg.cornerRadii, bg.fill).createRoundRectGradientMesh();
		
		getShader().begin();
		getShader().setUniformMatrix("u_worldView", combinedMatrix);
		mesh.render(getShader(), GL20.GL_TRIANGLES);
		getShader().end();
		
//		renderer.begin(ShapeType.Filled);
//		RoundRectRenderer.drawFilled(renderer, left, top, right, bottom, bg.cornerRadii);
//		renderer.end();
		// filled rect for now - no radii
//		renderer.rect(left, top, right, bottom);
		
//		
//		float rTopLeft = (float)bg.cornerRadii.topLeftHorizontalRadius;
//		float rTopRight = (float)bg.cornerRadii.topRightHorizontalRadius;
//		float rBottomRight = (float)bg.cornerRadii.bottomRightHorizontalRadius;
//		float rBottomLeft = (float)bg.cornerRadii.bottomLeftHorizontalRadius;
////		// top line
////		renderer.line(left + rTopLeft, top, right - rTopRight, top);
////		points.add(new Vec2d(left + rTopLeft, top));
////		points.add(new Vec2d(right - rTopRight, top));
////		
////		
////		
////		// top right arc
////		renderer.arc(right - rTopRight * 2, top, rTopRight * 2, -90, 90);
////		// right line
////		renderer.line(right, top + rTopRight, right, bottom - rBottomRight);
////		// bottom right arc
////		renderer.arc(right - rBottomRight * 2, bottom - rBottomRight * 2 , rBottomRight, 0, -90);
////		// bottom line
////		renderer.line(right - rBottomRight, bottom, left + rBottomLeft, bottom);
////		// bottom left arc
////		renderer.arc(left, bottom - rBottomLeft * 2, rBottomLeft, -90, -90);
////		// left line
////		renderer.line(left, bottom - rBottomLeft, left, top - rTopLeft);
////		// top left arc
////		renderer.arc(left, top, rTopLeft, -180, 90);
//		
//		renderer.end();
	}
}
