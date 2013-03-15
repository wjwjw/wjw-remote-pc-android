package com.key.gl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * A sphere object representation.
 * 
 */
public class Sphere {
	/* buffer holding vertex data */
	private FloatBuffer mVertexBuffer;
	/* buffer holding index data */
	private ShortBuffer mIndexBuffer;
	/* buffer holding texture coordinate data */
	private FloatBuffer mUVBuffer;
	/* VBO for vertex */
	private GLVBO mVertexVBO;
	/* VBO for index */
	private GLVBO mIdxVBO;
	/* VBO for texture coordinates*/
	private GLVBO mTexVBO;
	/* rows of the sphere grids */
	private int rows;
	/* columns of the sphere grids */
	private int cols;
	/* radius of the sphere */
	private float radius;
	/* total points */
	private int mCount;

	
	public Sphere(float radiu, int row, int col) {
		this.radius = radiu;
		this.rows = row;
		this.cols = col;
		mCount = (row + 1) * (col + 1);
		initBuffer();
		build();
		mVertexBuffer.position(0);
		mIndexBuffer.position(0);
		mUVBuffer.position(0);

		mVertexVBO = new GLVBO(GLVBO.VBO_VERTEX, mVertexBuffer);
		mIdxVBO = new GLVBO(GLVBO.VBO_INDEX, mIndexBuffer);
		mTexVBO = new GLVBO(GLVBO.VBO_TEXTURE, mUVBuffer);
	}

	/* initialize the buffers */
	private void initBuffer() {
		mVertexBuffer = BufferFactory.createFloatBuffer(mCount * 3);
		mIndexBuffer = BufferFactory.createShortBuffer(rows * cols * 6);
		mUVBuffer = BufferFactory.createFloatBuffer(mCount * 2);
	}

	/* build the sphere */
	private void build() {
		int r, c;
		Number3d n = new Number3d();
		Number3d pos = new Number3d();
		for (r = 0; r <= rows; r++) {
			float v = (float) r / (float) rows; // [0,1]
			float theta1 = v * (float) Math.PI; // [0,PI]
			n.setAll(0, 1, 0);
			n.rotateZ(theta1);
			for (c = 0; c <= cols; c++) {
				float u = (float) c / (float) cols; // [0,1]
				float theta2 = u * (float) (Math.PI * 2);// [0,2*PI];
				pos.setAllFrom(n);
				pos.rotateY(theta2);
				pos.multiply(radius);
				mVertexBuffer.put(pos.x);
				mVertexBuffer.put(pos.y);
				mVertexBuffer.put(pos.z);
				mUVBuffer.put(u);
				mUVBuffer.put(v);
			}
		}

		int colLength = cols + 1;
		for (r = 0; r < rows; r++) {
			int offset = r * colLength;
			for (c = 0; c < cols; c++) {
				int ul = offset + c;
				int ur = offset + c + 1;
				int br = offset + c + 1 + colLength;
				int bl = offset + c + colLength;
				mIndexBuffer.put((short) ul);
				mIndexBuffer.put((short) br);
				mIndexBuffer.put((short) ur);
				mIndexBuffer.put((short) ul);
				mIndexBuffer.put((short) bl);
				mIndexBuffer.put((short) br);
			}
		}
	}
	
	public void release(){
		mVertexVBO.release();
		mIdxVBO.release();
		mTexVBO.release();
	}

	public void draw(GL11 gl, GLTexture tex1, GLTexture tex2, float u, float v) {
		
		if (tex1 != null) {
			gl.glActiveTexture(GL10.GL_TEXTURE0);
			gl.glClientActiveTexture(GL10.GL_TEXTURE0);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			tex1.bind(gl);
			mTexVBO.bind(gl);
			gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL10.GL_MODULATE);
		}

		if (tex2 != null) {
			gl.glActiveTexture(GL10.GL_TEXTURE1);
			gl.glClientActiveTexture(GL10.GL_TEXTURE1);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			tex2.bind(gl);
			mTexVBO.bind(gl);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL10.GL_DECAL);
			if (u != 0 || v != 0) {
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(u, v, 0);
				gl.glMatrixMode(GL10.GL_MODELVIEW);
			}
		}
		
		mVertexVBO.bind(gl);
		gl.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		mIdxVBO.bind(gl);
		gl.glDrawElements(GL11.GL_TRIANGLES, mIndexBuffer.capacity(),
				GL10.GL_UNSIGNED_SHORT, 0);
		
		mIdxVBO.unBind();
		mVertexVBO.unBind();
		mTexVBO.unBind();
		
		if (tex2 != null) {
			tex2.unBind();
			gl.glActiveTexture(GL10.GL_TEXTURE1);
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glClientActiveTexture(GL10.GL_TEXTURE1);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}

		if (tex1 != null) {
			tex1.unBind();
			gl.glActiveTexture(GL10.GL_TEXTURE0);
			gl.glClientActiveTexture(GL10.GL_TEXTURE0);
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
	}
}
