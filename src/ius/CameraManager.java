package ius;

import android.opengl.Matrix;

import com.example.opengles20.myGLRenderer;

/* CameraManager Class
 * Function : 카메라 이동에 관함 부분을 처리함(단, 2d이므로 주로 카메라 효과를 표현하는데 사용됨) 
 */
public class CameraManager {
	//시점 표현에 필요한 변수 카메라 X, Y, Z 좌표 변수
	private float eyeX = 0.0f;
	private float eyeY = 0.0f;

	public CameraManager(){ }
	/* 카메라 매트릭스를 Renderer Class에 설정하는 함수 */
	private void setCamera(){
		Matrix.setLookAtM(myGLRenderer.mViewMatrix, 0, eyeX, eyeY, 1.0f, eyeX, eyeY, 0f, 0f, 1.0f, 0.0f);
		Matrix.multiplyMM(myGLRenderer.mMVPMatrix, 0, myGLRenderer.mProjectionMatrix, 0, myGLRenderer.mViewMatrix, 0);
	}
	/* camera를 delta값 만큼 이동하는 함수 */
	public void translateCamera(float deltaX, float deltaY){	
		eyeX += deltaX; eyeY += deltaY;
		setCamera();
	}
	/* camera 고정하는 함수 */
	public void fixedCamera(float ex, float ey){
		eyeX =ex; eyeY = ey;
		setCamera();
	}
}