package at.bestsolution.wgraf.backend.gdx.scene;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.bestsolution.wgraf.math.Vec2d;
import at.bestsolution.wgraf.paint.Color;
import at.bestsolution.wgraf.paint.LinearGradient;
import at.bestsolution.wgraf.paint.LinearGradient.Stop;
import at.bestsolution.wgraf.paint.Paint;
import at.bestsolution.wgraf.style.CornerRadii;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class RoundRectRenderer {

	private double left;
	private double top;
	private double right;
	private double bottom;
	private CornerRadii radii;
	private Paint paint;
	
	public RoundRectRenderer(double left, double top, double right, double bottom, CornerRadii radii, Paint paint) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.radii = radii;
		this.paint = paint;
	}
	
	
	
	private static Vector2 conv(Vec2d x) {
		return new Vector2((float)x.x, (float)x.y);
	}
	
	public static Color interpolateColor(Color a, Color b, double val) {
		double ival = 1 - val;
		return new Color((int)Math.round(a.red * val + b.red * ival), (int)Math.round(a.green * val + b.green * ival), (int)Math.round(a.blue * val + b.blue * ival), (int)Math.round(a.alpha * val + b.alpha * ival));
		
	}
	
	public Color computeColor(Vec2d pos) {
		if (paint instanceof Color) {
//			System.err.println("using paint");
			return (Color) paint;
		}
		else if (paint instanceof LinearGradient) {
//			System.err.println("Computing color for : " + pos);
			LinearGradient gradient = (LinearGradient) paint;
			
			Vector2 sta = null, sto = null;
			switch (gradient.coordMode) {
			case LOGICAL:
				Vec2d start = new Vec2d(gradient.startX, gradient.startY);
				Vec2d stop = new Vec2d(gradient.stopX, gradient.stopY);
				sta = conv(start);
				sto = conv(stop);
				break;
			case OBJECT_BOUNDING:
				Vec2d start1 = new Vec2d(left + gradient.startX * (right - left), top + gradient.startY * (top - bottom));
				Vec2d stop1 = new Vec2d(left + gradient.stopX * (right - left), top + gradient.stopY * (top - bottom));
				sta = conv(start1);
				sto = conv(stop1);
				break;
			}
//			System.err.println("Calculated vecs: " + sta + " -> " + sto);
			
			Vector2 s = new Vector2(sto).sub(sta);
//			System.err.println("s= " + s);
			float all = s.len();
			
			Vector2 p = conv(pos);
			
			Vector2 tar = s.scl(p.dot(s) / s.dot(s));
//			System.err.println("tar = " + tar);
			
			float tarLen = tar.len();
			
			float pkt = tarLen / all;
			if (Float.isNaN(pkt)) {
				pkt = 0;
			}
			Stop before = null, after = null;
			
			for (Stop cStop :gradient.stops) {
				if (cStop.at <= pkt) {
					before = cStop;
				}
				if (cStop.at > pkt) {
					after = cStop;
					break;
				}
			}
			if (before == null) {
				before = gradient.stops[0];
			}
			if (after == null) {
				after = gradient.stops[gradient.stops.length-1];
			}
			
			System.err.println("GRADIENT CALCULUS: " + pkt + " before: " + before.color + "@" + before.at + " after: " + after.color + "@" + after.at);
//			System.err.println(Arrays.toString(gradient.stops));
			
			double val = (pkt - before.at)/(after.at - before.at);
			
			Color result = interpolateColor(before.color, after.color, val);
			
//			System.err.println(" => " + result);
			
			return result;
			
		}
		else {
			throw new UnsupportedOperationException("paint not yet supported " + paint);
		}
	}
	
	
	public Mesh createRoundRectGradientMesh() {
		VertexAttribute pos = new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE);
//		VertexAttribute pos = VertexAttribute.Position();
		VertexAttribute color = VertexAttribute.ColorUnpacked();
		
		int numSections = 7;
		int tris = 10 + 4 * numSections;
		
		Mesh mesh = new Mesh(false, tris * 3 * 6, tris * 3, pos, color);
	
		
		FloatBuffer buf = FloatBuffer.allocate(tris * 3 * 6);
		ShortBuffer idxBuf = ShortBuffer.allocate(tris * 3);
		long cur = 0;
		
		final Vec2d innerTopLeft = new Vec2d(left + radii.topLeftHorizontalRadius, top + radii.topLeftVerticalRadius);
		final Vec2d innerTopRight = new Vec2d(right - radii.topRightHorizontalRadius, top + radii.topRightVerticalRadius);
		final Vec2d innerBottomLeft = new Vec2d(left + radii.bottomLeftHorizontalRadius, bottom - radii.bottomLeftVerticalRadius);
		final Vec2d innerBottomRight = new Vec2d(right - radii.bottomRightHorizontalRadius, bottom - radii.bottomRightVerticalRadius);
		
		// edges
		cur = computeRoundSection(innerBottomRight, radii.bottomRightHorizontalRadius, radii.bottomRightVerticalRadius, 0, 90, numSections, buf, idxBuf, cur);
		cur = computeRoundSection(innerBottomLeft, radii.bottomLeftHorizontalRadius, radii.bottomLeftVerticalRadius, 90, 90, numSections, buf, idxBuf, cur);
		cur = computeRoundSection(innerTopLeft, radii.topLeftHorizontalRadius, radii.topLeftVerticalRadius, 180, 90, numSections, buf, idxBuf, cur);
		cur = computeRoundSection(innerTopRight, radii.topRightHorizontalRadius, radii.topRightVerticalRadius, 270, 90, numSections, buf, idxBuf, cur);
		
		// inner
		cur = putTriangle(innerTopLeft, innerTopRight, innerBottomLeft, buf, idxBuf, cur);
		cur = putTriangle(innerTopRight, innerBottomRight, innerBottomLeft, buf, idxBuf, cur);
		
		// outer
		// top
		cur = putTriangle(new Vec2d(innerTopLeft.x, top), innerTopLeft, new Vec2d(innerTopRight.x, top), buf, idxBuf, cur);
		cur = putTriangle(new Vec2d(innerTopRight.x, top), innerTopLeft, innerTopRight, buf, idxBuf, cur);
		// left
		cur = putTriangle(new Vec2d(left, innerTopLeft.y), new Vec2d(left, innerBottomLeft.y), innerTopLeft, buf, idxBuf, cur);
		cur = putTriangle(innerTopLeft, new Vec2d(left, innerBottomLeft.y), innerBottomLeft, buf, idxBuf, cur);
		// bottom
		cur = putTriangle(innerBottomLeft, new Vec2d(innerBottomLeft.x, bottom), innerBottomRight, buf, idxBuf, cur);
		cur = putTriangle(innerBottomRight, new Vec2d(innerBottomLeft.x, bottom), new Vec2d(innerBottomRight.x, bottom), buf, idxBuf, cur);
		// right
		cur = putTriangle(innerTopRight, innerBottomRight, new Vec2d(right, innerTopRight.y), buf, idxBuf, cur);
		cur = putTriangle(innerBottomRight, new Vec2d(right, innerBottomRight.y), new Vec2d(right, innerTopRight.y), buf, idxBuf, cur);
		
		
		mesh.setVertices(buf.array());
		mesh.setIndices(idxBuf.array());
		
//		System.err.println("returning mesh with " + cur + " points");
//		System.err.println("index is " + Arrays.toString(idxBuf.array()));
		
		return mesh;
	}
	
	public long computeRoundSection(Vec2d center, double radiusHoriz, double radiusVert, float begin, float degrees, int numSections, FloatBuffer output, ShortBuffer index, long c) {
		List<Vec2d> s = new ArrayList<Vec2d>();
		float w = begin;
		for (int i = 0; i <= numSections; i++) {
			double wRad = w/180d*Math.PI;
			Vec2d cur = new Vec2d(center.x + radiusHoriz * Math.cos(wRad), center.y + radiusVert * Math.sin(wRad));
			s.add(cur);
			w += degrees / (float)numSections;
		}
		
		for (int i = 0; i < numSections; i++) {
			Vec2d a = s.get(i);
			Vec2d b = s.get(i+1);
			
			c = putTriangle(a, center, b, output, index, c);
		}
		
		return c;
	}
	
	public void putColor(Color color, FloatBuffer output) {
		output.put(color.red / 255f);
		output.put(color.green / 255f);
		output.put(color.blue / 255f);
		output.put(color.alpha / 255f);
	}
	
	public void putVertex(Vec2d vec, FloatBuffer output) {
		output.put((float)vec.x);
		output.put((float)vec.y);
	}
	
	public long putTriangle(Vec2d a, Vec2d b, Vec2d c, FloatBuffer output, ShortBuffer index, long cur) {
		putVertex(a, output);
		
		Color col = computeColor(a);
		putColor(col, output);
		
		index.put((short)cur++);
		
		putVertex(b, output);
		
		col = computeColor(b);
		putColor(col, output);
		
		index.put((short)cur++);
		
		putVertex(c, output);
		
		col = computeColor(c);
		putColor(col, output);
		
		index.put((short)cur++);
		return cur;
	}
	
	public static void drawRoundSection(ShapeRenderer r, Vec2d center, double radiusHoriz, double radiusVert, float begin, float degrees) {
		int sections = 7;
		List<Vec2d> s = new ArrayList<Vec2d>();
		float w = begin;
		for (int i = 0; i <= sections; i++) {
			double wRad = w/180d*Math.PI;
			Vec2d cur = new Vec2d(center.x + radiusHoriz * Math.cos(wRad), center.y + radiusVert * Math.sin(wRad));
			s.add(cur);
			w += degrees / (float)sections;
		}
		
		for (int i = 0; i < sections; i++) {
			Vec2d a = s.get(i);
			Vec2d b = s.get(i+1);
			triangle(r, a, center, b);
		}
		
	}
	
	public static void triangle(ShapeRenderer r, Vec2d a, Vec2d b, Vec2d c) {
		r.triangle((float)a.x, (float)a.y, (float) b.x, (float) b.y, (float) c.x , (float) c.y);
	}
	
	public static void drawFilled(ShapeRenderer r, double left, double top, double right, double bottom, CornerRadii radii) {
		
		final Vec2d innerTopLeft = new Vec2d(left + radii.topLeftHorizontalRadius, top + radii.topLeftVerticalRadius);
		final Vec2d innerTopRight = new Vec2d(right - radii.topRightHorizontalRadius, top + radii.topRightVerticalRadius);
		final Vec2d innerBottomLeft = new Vec2d(left + radii.bottomLeftHorizontalRadius, bottom - radii.bottomLeftVerticalRadius);
		final Vec2d innerBottomRight = new Vec2d(right - radii.bottomRightHorizontalRadius, bottom - radii.bottomRightVerticalRadius);
		
		// edges
		drawRoundSection(r, innerBottomRight, radii.bottomRightHorizontalRadius, radii.bottomRightVerticalRadius, 0, 90);
		drawRoundSection(r, innerBottomLeft, radii.bottomLeftHorizontalRadius, radii.bottomLeftVerticalRadius, 90, 90);
		drawRoundSection(r, innerTopLeft, radii.topLeftHorizontalRadius, radii.topLeftVerticalRadius, 180, 90);
		drawRoundSection(r, innerTopRight, radii.topRightHorizontalRadius, radii.topRightVerticalRadius, 270, 90);
		
		// inner
		triangle(r, innerTopLeft, innerTopRight, innerBottomLeft);
		triangle(r, innerTopRight, innerBottomRight, innerBottomLeft);
		
		// outer
		// top
		triangle(r, new Vec2d(innerTopLeft.x, top), innerTopLeft, new Vec2d(innerTopRight.x, top));
		triangle(r, new Vec2d(innerTopRight.x, top), innerTopLeft, innerTopRight);
		// left
		triangle(r, new Vec2d(left, innerTopLeft.y), new Vec2d(left, innerBottomLeft.y), innerTopLeft);
		triangle(r, innerTopLeft, new Vec2d(left, innerBottomLeft.y), innerBottomLeft);
		// bottom
		triangle(r, innerBottomLeft, new Vec2d(innerBottomLeft.x, bottom), innerBottomRight);
		triangle(r, innerBottomRight, new Vec2d(innerBottomLeft.x, bottom), new Vec2d(innerBottomRight.x, bottom));
		// right
		triangle(r, innerTopRight, innerBottomRight, new Vec2d(right, innerTopRight.y));
		triangle(r, innerBottomRight, new Vec2d(right, innerBottomRight.y), new Vec2d(right, innerTopRight.y));
		
	}
	
}
