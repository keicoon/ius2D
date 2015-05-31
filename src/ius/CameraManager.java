package ius;

import android.opengl.Matrix;

import com.example.opengles20.myGLRenderer;

/* CameraManager Class
 * Function : ī�޶� �̵��� ���� �κ��� ó����(��, 2d�̹Ƿ� �ַ� ī�޶� ȿ���� ǥ���ϴµ� ����) 
 */
public class CameraManager {
	//���� ǥ���� �ʿ��� ���� ī�޶� X, Y, Z ��ǥ ����
	private float eyeX = 0.0f;
	private float eyeY = 0.0f;

	public CameraManager(){ }
	/* ī�޶� ��Ʈ������ Renderer Class�� �����ϴ� �Լ� */
	private void setCamera(){
		Matrix.setLookAtM(myGLRenderer.mViewMatrix, 0, eyeX, eyeY, 1.0f, eyeX, eyeY, 0f, 0f, 1.0f, 0.0f);
		Matrix.multiplyMM(myGLRenderer.mMVPMatrix, 0, myGLRenderer.mProjectionMatrix, 0, myGLRenderer.mViewMatrix, 0);
	}
	/* camera�� delta�� ��ŭ �̵��ϴ� �Լ� */
	public void translateCamera(float deltaX, float deltaY){	
		eyeX += deltaX; eyeY += deltaY;
		setCamera();
	}
	/* camera �����ϴ� �Լ� */
	public void fixedCamera(float ex, float ey){
		eyeX =ex; eyeY = ey;
		setCamera();
	}
}