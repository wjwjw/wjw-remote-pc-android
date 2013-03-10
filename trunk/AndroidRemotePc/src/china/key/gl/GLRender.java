/***
 * Excerpted from "Hello, Android!",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband for more book information.
 ***/

package china.key.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.key.R;
import com.key.keyboard.GlobalVariables;

public class GLRender implements Renderer
{
	Sphere mEarthSphere;
	GLTexture mEarthTex;
	Context mContext;
	
	float u, v;
	
	public GLRender(Context context){
		mContext = context;
		
		mEarthSphere = new Sphere(30, 30, 30);
		mEarthTex = new GLTexture(context, R.raw.earth, false);
	}
	
	public void onDrawFrame(GL10 gl)
	{
		
		if (GlobalVariables.glviewtouchdown == false) {
			if (GlobalVariables.Ayimuth > 0.0f)
				GlobalVariables.Ayimuth -= 6.0f;
			if (GlobalVariables.Ayimuth < 0.0f) {
					GlobalVariables.Ayimuth += 6.0f;
				if (GlobalVariables.Ayimuth > 0.0f)
					GlobalVariables.Ayimuth = 0.0f;

			}

			if (GlobalVariables.Aximuth > 0.0f)
				GlobalVariables.Aximuth -= 3.0f;
			if (GlobalVariables.Aximuth < 0.0f) {
					GlobalVariables.Aximuth += 3.0f;
				if (GlobalVariables.Aximuth > 0.0f)
					GlobalVariables.Aximuth = 0.0f;
			}
		}

		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		
		gl.glTranslatef(0.0f, 0.0f, -60);

		gl.glRotatef(GlobalVariables.Aximuth, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(GlobalVariables.Ayimuth, 0.0f, 1.0f, 0.0f);

		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	
		mEarthSphere.draw((GL11)gl, mEarthTex, null, 0, 0);

	    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		float ratio = (float) width / height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);	
		gl.glLoadIdentity();	
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glClearColor(0, 0, 0, 0);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}
}