package at.bestsolution.wgraf.launcher.android;

import java.util.Arrays;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import at.bestsolution.wgraf.Application;
import at.bestsolution.wgraf.BackendFactory;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class AndroidLauncher extends AndroidApplication {
	String TAG = "wgraf.launcher";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		Log.d(TAG, "Hallo Welt!");
		// TODO find the correct wgraf application and pass it to initialize
		try {
			
			BackendFactory.setBackendFactory("at.bestsolution.wgraf.backend.gdx.GdxBackendFactory");
			
			ActivityInfo app = getPackageManager().getActivityInfo(this.getComponentName(), PackageManager.GET_ACTIVITIES|PackageManager.GET_META_DATA);
			String appClass = app.metaData.getString("application");
			Log.d(TAG, "using appClass = " + appClass);
			
			Class<?> cls1 = Class.forName(appClass);
			Log.d(TAG, "appClass loaded " + cls1);
			Log.d(TAG, "appClass ifaces " + Arrays.toString(cls1.getInterfaces()));
			Object appInstance = cls1.newInstance();
			Log.d(TAG, "appClass instantiated " + appInstance);
			Log.d(TAG, "appClass instanceof check " + (appInstance instanceof ApplicationListener));
			
			Application a = (Application) appInstance;
			
			initialize((ApplicationListener)a.internal_getBackend(), config);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
