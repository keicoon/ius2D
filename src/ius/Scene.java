package ius;

import com.example.opengles20.GameObject;
import com.example.opengles20.myGLRenderer;
import com.example.opengles20.myGLSurfaceView;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.SparseArray;
import android.view.MotionEvent;

/* Scene Class
 * Function : 게임 씬의 기본 클래스로 가장 기본적인 씬의 형태임
 * 
 */
public abstract class Scene {
	protected Context mContext;				
	public myGLSurfaceView mGL20;			
	public boolean gameLoop = true;			// 현재 씬의 Logic을 동작하거나 중지하는 변수
	private int currentShader;				// 현재 씬의 Shader를 저장하는 변수
	
	protected int TOUCH_EVENT;				// 터치 이벤트 종류를 구별하는 변수
	protected float TOUCH_X, TOUCH_Y;		// 터치 좌표 X, Y를 저장하는 변수
	public SparseArray<PointF> mActivePointers;
	
	public abstract void Init();			// 씬의 초기화를 담당하는 상속 함수
	public abstract void Destroy();			// 씬의 파괴(메모리정리)를 담당하는 상속 함수
	
	protected abstract void Draw();			// 씬을 그리는 내용이 들어가는 함수
	protected abstract void Update(long elapsed);//씬의 Logic 내용이 들어가는 함수
	protected abstract void Input();		// 씬의 입력처리(터치) 내용이 들어가는 함수
	
	public Scene(Context context, myGLSurfaceView gl20){
		mContext = context;
		mGL20 = gl20;
		mActivePointers = new SparseArray<PointF>();
	}
	public void run(long elapsed)
	{
		synchronized(mGL20){
			GLES20.glUseProgram(currentShader);
			Draw();
			if(gameLoop)
				Update(elapsed);
		}
	}
	/*셰이더를 적용하는 함수 */
	public void setShader(int shader){
		if(shader == 0)
			currentShader = myGLRenderer.mObjectProgramHandle;
		else
			currentShader = shader;
	}
	/* 터치 처리를 위한 함수(상위 Renderer Class로 부터 상속받음) */
	public boolean onTouchEvent(MotionEvent e) {
		//하위 내용은 일반적인 Logic이므로 주석을 달지 않음
		TOUCH_EVENT = e.getActionMasked();

		int pointerIndex = e.getActionIndex();
		int pointerId = e.getPointerId(pointerIndex);
		switch(TOUCH_EVENT)// 터치 이벤트 종류에 따라 CASE별로 처리함
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				PointF f = new PointF();
			      f.x = e.getX(pointerIndex)/myGLRenderer.ssx;
			      f.y = (myGLRenderer.pScreenHeight-e.getY(pointerIndex))/myGLRenderer.ssy;
			      mActivePointers.put(pointerId, f);
			      // InputManager의 함수로 처리함 (터치패드와 버튼에 영향을 주는 부분)
			      mGL20.inputManager.updateStatus(0, true, f.x, f.y, pointerId);
			      // 터치처리를 위한 상속 함수
			      Input();
			break;
			case MotionEvent.ACTION_MOVE:
				for (int size = e.getPointerCount(), i = 0; i < size; i++) {
					PointF point = mActivePointers.get(e.getPointerId(i));
					float lx = e.getX(i)/myGLRenderer.ssx;
					float ly = (myGLRenderer.pScreenHeight-e.getY(i))/myGLRenderer.ssy;
					if (point != null
							&& point.x != lx
							&& point.y != ly) {
						mGL20.inputManager.updateStatus(1,false, point.x, point.y, e.getPointerId(i));
						point.x = lx;
						point.y = ly;
						mGL20.inputManager.updateStatus(1,true, point.x, point.y, e.getPointerId(i));
					}
				}
				Input();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				PointF t = mActivePointers.get(pointerId);
				mGL20.inputManager.updateStatus(2,false, t.x, t.y, pointerId);
				mActivePointers.remove(pointerId);
			break;
		}
		
		return true;
	}
	
	/* 씬효과
	 * 1. 카메라 이동에 의한 지진효과
	 * 2. Fade In, Fade Out
	 * */
	private boolean gQuakeState = false;
	private float gQuakeSize = 0f;
	private float gQuakeTime = 0f;
	private float gQuakeTempTime = 0f;
	private float gQuakeValue = 1f;
	
	private boolean gFadeState = false;
	private float gFadeValue = 0f;
	private GameObject BG_Black_Screen;
	
	public void SetQuakeSize(float pQuakeSize, float pQuakeTime){
		gQuakeState = true;
		gQuakeSize = pQuakeSize; gQuakeTime = pQuakeTime;gQuakeTempTime = 0f;
	}
	public void doQuakeEffect(float deltaTime){//deltaTime(s)
		// 카메라가 좌하, 상우 로 이동한다.
		if(gQuakeState){// 총 시간동안 동작한다.
			gQuakeTempTime += deltaTime;
			if(gQuakeTempTime > 0.1f)
			{
				gQuakeTime -= gQuakeTempTime;
				gQuakeTempTime = 0f;
				gQuakeValue *= -1.0f;	
				
				float camera_x = gQuakeSize * gQuakeValue, camera_y =  gQuakeSize * gQuakeValue;
				mGL20.cameraManager.fixedCamera(camera_x, camera_y);
			}
			if(gQuakeTime < 0f){
				gQuakeState = false;
				mGL20.cameraManager.fixedCamera(0f, 0f);
			}
		}
	}
	/*
	 * Fade Effect는 끝날시 return true 그 이외는 return false
	 */
	public void setFadeState(float FadeType){
		gFadeState = true;
		gFadeValue = FadeType;
		BG_Black_Screen = mGL20.objectmanager.newItem(
				mContext, mGL20,
				//검은 배경 그림으로 변경해야함
				"spr_util", 1, 0,
				myGLRenderer.mScreenWidth/2, myGLRenderer.mScreenHeight/2,
				0f, 1.0f);
	}
	public boolean doFadeIn(float deltaTime, float speed){
		if(gFadeState){
			if(gFadeValue <= 0f){
				//end 조건
				return true;
			}
			else{
				//process 조건
				gFadeValue -= speed*deltaTime;
				BG_Black_Screen.alpha = gFadeValue;
				BG_Black_Screen.Draw(myGLRenderer.mScreenWidth,myGLRenderer.mScreenHeight);
			}
		}
		return false;
	}
	public boolean doFadeOut(float deltaTime, float speed){
		if(gFadeState){
			if(gFadeValue >= 1f){
				//end 조건
				return true;
			}
			else{
				//process 조건
				gFadeValue += speed*deltaTime;
				BG_Black_Screen.alpha = gFadeValue;
				BG_Black_Screen.Draw(myGLRenderer.mScreenWidth,myGLRenderer.mScreenHeight);
			}
		}
		return false;
	}
}
