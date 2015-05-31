package ius;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.opengles20.myGLRenderer;
/* iusObjecet Class
 * Function : Object Class의 상속 클래스로 Triangle Draw 기능을 함
 */
public abstract class iusObject {
	// Object 출력 정보
	public float x, y;
	public float angle;
	public float scale;
	public float alpha;

	private final ShortBuffer drawListBuffer;	// IndexBuffer는 고정이므로 변수로 생성함
	private final FloatBuffer mNormals;			// 법선Buffer는 고정이므로 변수로 생성함

	private int mPositionHandle;				// 위치 핸들
	private int mColorHandle;					// 색상 핸들
	private int mNormalHandle;					// 법선 핸들
	private int mLightPosHandle;				// 광원 핸들
	private int mTextureUniformHandle;			// 텍스쳐 핸들
	private int mTextureCoordinateHandle;		// 텍스쳐 좌표 핸들
	public int mTextureDataHandle;				// 텍스쳐 핸들
	
	public int mFontTypeHandle;					// 폰트타입 핸들
	public int mType;
	
	private int mMVPMatrixHandle;				// model + view + projection
	private int mMVMatrixHandle;				// model + view
	
	private float[] mModelMatrix = new float[16];	// 모델 
	private float[] mMVPMatrix = new float[16];		// MVP
	
	final float[] NormalData = {				// 법선 정보는 2d에서 고정임
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f };
	private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw
					
	protected iusObject(int mProgramHandle){
		x = 0f; y = 0f; angle = 0f; scale = 1f; alpha = 1f;
		
		drawListBuffer = ByteBuffer.allocateDirect(drawOrder.length * 2)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		drawListBuffer.put(drawOrder).position(0);

		mNormals = ByteBuffer.allocateDirect(NormalData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mNormals.put(NormalData).position(0);
		// 우선순위 1
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
	    mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
	    
	    mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
	    mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color"); 
	    mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
	    // 우선순위 2
	    mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
	    mLightPosHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_LightPos");
	    mNormalHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Normal");
	    mFontTypeHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Type");
	}
	public void iusDraw(float type,
			float nW, float nH,
			float l, float t, float r, float b,
			float CR, float CG, float CB,
			float[] mViewMatrix, float[] mProjectionMatrix, float[] mLightPosInEyeSpace){
	// vertex 정보
	float[] squareCoords = new float[]{ 
				-nW+type*nW, nH+type*nH , 0.0f, // top left
				-nW+type*nW, -nH+type*nH, 0.0f, // bottom left
				nW+type*nW , -nH+type*nH, 0.0f, // bottom right
				nW+type*nW , nH+type*nH , 0.0f }; // top right
	// texture coordinate 정보
	float[] TextureCoordinateData = new float[]{ 
				l, t,
				l, b, 
				r, b,
				r, t };
	// color 정보
	float[] ColorData = { 
			CR, CG, CB, alpha,			
			CR, CG, CB, alpha,
			CR, CG, CB, alpha,
			CR, CG, CB, alpha };
	
	FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(squareCoords.length * 4)
			.order(ByteOrder.nativeOrder()).asFloatBuffer();
	vertexBuffer.put(squareCoords).position(0);
	FloatBuffer textureBuffer = ByteBuffer.allocateDirect(TextureCoordinateData.length * 4)
			.order(ByteOrder.nativeOrder()).asFloatBuffer();
	textureBuffer.put(TextureCoordinateData).position(0);
	FloatBuffer mColors = ByteBuffer.allocateDirect(ColorData.length * 4)
	        .order(ByteOrder.nativeOrder()).asFloatBuffer();							
	mColors.put(ColorData).position(0);
	
	// 텍스쳐 세팅
	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
	GLES20.glUniform1i(mTextureUniformHandle, 0);
	GLES20.glUniform1i(mFontTypeHandle, mType);
	
	// Buffer에 정보 넣음
	GLES20.glVertexAttribPointer(mPositionHandle, 3,
			GLES20.GL_FLOAT, false, 12, vertexBuffer);
	GLES20.glEnableVertexAttribArray(mPositionHandle);
	
    GLES20.glVertexAttribPointer(mColorHandle, 4,
    		GLES20.GL_FLOAT, false, 0, mColors);        
    GLES20.glEnableVertexAttribArray(mColorHandle);
    
    GLES20.glVertexAttribPointer(mNormalHandle, 3,
    		GLES20.GL_FLOAT, false, 0, mNormals);
    GLES20.glEnableVertexAttribArray(mNormalHandle);
    
   	GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2,
   			GLES20.GL_FLOAT, false, 0, textureBuffer);   
   	GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
   	
   	
   	Matrix.setIdentityM(mModelMatrix, 0);
   	
   	Matrix.translateM(mModelMatrix, 0, x *myGLRenderer.ssx, y *myGLRenderer.ssy, 0f);
	Matrix.rotateM(mModelMatrix, 0, angle, 0.0f, 0.0f, 1.0f);
	Matrix.scaleM(mModelMatrix, 0, scale , scale, 1.0f);   
   	
   	Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
   	GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
   	
   	Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
   	GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
    
   	GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
    
	GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
			GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
	
	GLES20.glDisableVertexAttribArray(mPositionHandle);
	GLES20.glDisableVertexAttribArray(mColorHandle);
	GLES20.glDisableVertexAttribArray(mNormalHandle);
	GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
	}
	public abstract void Draw();
}
