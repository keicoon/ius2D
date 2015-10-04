package ius;

import com.example.opengles20.myGLRenderer;
import com.example.opengles20.myGLSurfaceView;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.SparseArray;
import android.view.MotionEvent;

/* Scene Class
 * Function : ���� ���� �⺻ Ŭ������ ���� �⺻���� ���� ������
 * 
 */
public abstract class Scene {			
	public myGLSurfaceView mGL20;			
	public boolean gameLoop = true;			// ���� ���� Logic�� �����ϰų� �����ϴ� ����
	private int currentShader;				// ���� ���� Shader�� �����ϴ� ����
	protected float screenWidth;						// ȭ�� ���� ũ��
	protected float screenHeight;						// ȭ�� ���� ũ��
	
	protected int TOUCH_EVENT;				// ��ġ �̺�Ʈ ������ �����ϴ� ����
	protected float TOUCH_X, TOUCH_Y;		// ��ġ ��ǥ X, Y�� �����ϴ� ����
	public SparseArray<PointF> mActivePointers;
	
	protected InputManager IM;
	protected TimeManager TM;
	protected ObjectManager OM;
	protected SoundManager SM;
	protected FontManager FM;
	protected AtlasManager AM;
	
	public abstract void Init();			// ���� �ʱ�ȭ�� ����ϴ� ��� �Լ�
	public void Destroy(){			// ���� �ı�(�޸�����)�� ����ϴ� ��� �Լ�
		/* Before Destroy, must call Manager's clearFuction */
		TM.ClearTime();
		IM.deleteButtonObject();
		OM.ClearItem();
	}
	
	protected abstract void Draw();			// ���� �׸��� ������ ���� �Լ�
	protected abstract void Update(float INTERVAL);//���� Logic ������ ���� �Լ�
	protected abstract void Input();		// ���� �Է�ó��(��ġ) ������ ���� �Լ�
	
	public Scene(myGLSurfaceView gl20){
		mGL20 = gl20;
		mActivePointers = new SparseArray<PointF>();
		screenWidth = myGLRenderer.mScreenWidth;
		screenHeight = myGLRenderer.mScreenHeight;
		IM = InputManager.getInstance();
		TM = TimeManager.getInstance();
		OM = ObjectManager.getInstance();
		SM = SoundManager.getInstance();
		FM = FontManager.getInstance();
		AM = AtlasManager.getInstance();
	}
	public void run(float elapsed)
	{
		synchronized(mGL20){
			GLES20.glUseProgram(currentShader);
			Draw();
			if(gameLoop)
				Update(elapsed);
		}
	}
	/*���̴��� �����ϴ� �Լ� */
	public void setShader(int shader){
		if(shader == 0)
			currentShader = myGLRenderer.mObjectProgramHandle;
		else
			currentShader = shader;
	}
	/* ��ġ ó���� ���� �Լ�(���� Renderer Class�� ���� ��ӹ���) */
	public boolean onTouchEvent(MotionEvent e) {
		//���� ������ �Ϲ����� Logic�̹Ƿ� �ּ��� ���� ����
		TOUCH_EVENT = e.getActionMasked();

		int pointerIndex = e.getActionIndex();
		int pointerId = e.getPointerId(pointerIndex);
		switch(TOUCH_EVENT)// ��ġ �̺�Ʈ ������ ���� CASE���� ó����
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				PointF f = new PointF();
			      f.x = e.getX(pointerIndex)/myGLRenderer.ssx;
			      f.y = (myGLRenderer.pScreenHeight-e.getY(pointerIndex))/myGLRenderer.ssy;
			      mActivePointers.put(pointerId, f);
			      // InputManager�� �Լ��� ó���� (��ġ�е�� ��ư�� ������ �ִ� �κ�)
			      IM.updateStatus(0, true, f.x, f.y, pointerId);
			      // ��ġó���� ���� ��� �Լ�
			break;
			case MotionEvent.ACTION_MOVE:
				for (int size = e.getPointerCount(), i = 0; i < size; i++) {
					PointF point = mActivePointers.get(e.getPointerId(i));
					float lx = e.getX(i)/myGLRenderer.ssx;
					float ly = (myGLRenderer.pScreenHeight-e.getY(i))/myGLRenderer.ssy;
					if (point != null
							&& point.x != lx
							&& point.y != ly) {
						IM.updateStatus(1,false, point.x, point.y, e.getPointerId(i));
						point.x = lx;
						point.y = ly;
						IM.updateStatus(1,true, point.x, point.y, e.getPointerId(i));
					}
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				PointF t = mActivePointers.get(pointerId);
				/* TODO ���� ����ó�� */
				if(t != null){
					IM.updateStatus(2,false, t.x, t.y, pointerId);
					mActivePointers.remove(pointerId);
				}
			break;
		}
		Input();
		return true;
	}
	
	/* ��ȿ��
	 * 1. ī�޶� �̵��� ���� ����ȿ��
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
		// ī�޶� ����, ��� �� �̵��Ѵ�.
		if(gQuakeState){// �� �ð����� �����Ѵ�.
			gQuakeTempTime += deltaTime;
			if(gQuakeTempTime > 0.1f)
			{
				gQuakeTime -= gQuakeTempTime;
				gQuakeTempTime = 0f;
				gQuakeValue *= -1.0f;	
				
				float camera_x = gQuakeSize * gQuakeValue, camera_y =  gQuakeSize * gQuakeValue;
				CameraManager.getInstance().fixedCamera(camera_x, camera_y);
			}
			if(gQuakeTime < 0f){
				gQuakeState = false;
				CameraManager.getInstance().fixedCamera(0f, 0f);
			}
		}
	}
	/*
	 * Fade Effect�� ������ return true �� �ܴ̿� return false
	 */
	public void setFadeState(float FadeType){
		gFadeState = true;
		gFadeValue = FadeType;
		BG_Black_Screen = OM.newItem(
				//���� ��� �׸����� �����ؾ���
				"spr_util", 1, 0,
				myGLRenderer.pScreenWidth*0.5f, myGLRenderer.pScreenHeight*0.5f,
				0f, 1.0f);
	}
	public boolean doFadeIn(float deltaTime, float speed){
		if(gFadeState){
			if(gFadeValue <= 0f){
				//end ����
				OM.RemoveItem(BG_Black_Screen);
				return true;
			}
			else{
				//process ����
				gFadeValue -= speed*deltaTime;
				BG_Black_Screen.alpha = gFadeValue;
				BG_Black_Screen.Draw(myGLRenderer.pScreenWidth*1.5f, myGLRenderer.pScreenHeight*1.5f);
			}
		}
		return false;
	}
	public boolean doFadeOut(float deltaTime, float speed){
		if(gFadeState){
			if(gFadeValue >= 1f){
				//end ����
				OM.RemoveItem(BG_Black_Screen);
				return true;
			}
			else{
				//process ����
				gFadeValue += speed*deltaTime;
				BG_Black_Screen.alpha = gFadeValue;
				BG_Black_Screen.Draw(myGLRenderer.pScreenWidth*1.5f, myGLRenderer.pScreenHeight*1.5f);
			}
		}
		return false;
	}
}
