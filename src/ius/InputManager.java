package ius;

import ius.Util.ius_Point;

import java.util.ArrayList;

import android.annotation.SuppressLint;

import android.opengl.GLES20;
import android.util.Log;

import com.example.opengles20.myGLRenderer;

@SuppressLint("RtlHardcoded")

/* InputManager Class
 * Function : �Ϲ� ��ư, ���� ��ư �� AB ��ư�� ������ �����ϰ� ������
 * ����ϱ����� �� SetInput �Լ��� ����Ͽ� �ʱ�ȭ�� �ؾ���
 */
public class InputManager {

	ArrayList<ButtonObject> ButtonList;
	GameObject handle,handle2;
	
	public boolean UP,DOWN,LEFT,RIGHT,A,B;
	private boolean usingInputManager;
	private int mtouchID;
	private boolean useingTouchPad;
	
	private static InputManager instance;

	public static InputManager getInstance() {
		if (instance == null) {
			instance = new InputManager();
		}
		return instance;
	}
	private InputManager() {
		UP=false;DOWN=false;LEFT=false;RIGHT=false;
		A=false;B=false;mtouchID = -1;usingInputManager = false;
		ButtonList = new ArrayList<ButtonObject>();
	}
	public void SetInput(boolean puseingTouchPad){
		usingInputManager = true;
		useingTouchPad = puseingTouchPad;

		if(useingTouchPad)// ��ġ�е带 ����Ѵٸ� ����Ǿ���ϴ� �κ� 1 <Ư¡ : ������ ��ġ�� ��ư�� ������>
		{
			handle = new GameObject();
			handle.SetGameObject(
					"spr_util", 0, 1,
					150f, 150f,
					0f, 1.2f);
			handle.alpha = 0.8f;
			handle2 = new GameObject();
			handle2.SetGameObject(
					"spr_util", 0, 2,
					150f, 150f,
					0f, 1.2f);
			handle2.alpha = 0.6f;
			setButtonObject("spr_util", 0, 1,
					myGLRenderer.mScreenWidth-240f, 100f,
					0f, 1.2f,
					0, "A",
					0x00FF0000,
					2f);
			
			setButtonObject("spr_util", 0, 1,
					myGLRenderer.mScreenWidth-100f, 180f,
					0f, 1.2f,
					1, "B",
					0x00FF0000,
					2f);
		}	
	}
	/* �Ϲ����� ��ư�� �߰��ϰ� �ʹٸ� �Ʒ� �Լ��� ����Ͽ� �ʱ�ȭ�� �ϰ� ButtonList�� �߰��ؾ��� */
	public void setButtonObject(
			String textureName, int p_AN, int p_SN, float px, float py,
			float pangle, float pscale,
			int pid, String ptext, int pColor, float pfscale){
		ButtonList.add(
				new ButtonObject(
						textureName, p_AN, p_SN,
						px, py,
						pangle, pscale,
						pid, ptext, pColor, pfscale));
	}
	/* �� ���� ���������� ��ư���� �����ؼ� �����ؾ��� <���� ������ �ʿ��� �κ�> */
	public void deleteButtonObject(){
		Log.d("input","��ư�� ������");
		handle = null;handle2 = null;
		ButtonList.clear();
		usingInputManager = false;
	}
	/* ��ư�� ���������� �׸��� �κ����� ������ �ѹ��� Draw()�� �ϸ� �� */
	public void draw(){
		GLES20.glUseProgram(myGLRenderer.mObjectProgramHandle);
		for(int i=0;i<ButtonList.size();i++)
			ButtonList.get(i).Draw();
		
		handle.Draw();
		handle2.Draw();
		GLES20.glUseProgram(myGLRenderer.mFontProgramHandle);
		for(int i=0;i<ButtonList.size();i++)
			ButtonList.get(i).DrawFont();
		
	}
	/* ��ư�� ���������� ���ȴ��� Ȯ���ϴ� �Լ� <������� 1�� ��ư�� �Է��� Ȯ���Ͽ� ���ȴٸ� TRUE�� ������ */
	public boolean getActive(int id){
		return ButtonList.get(id).active;
	}
	/* ��ġ�� ���ȴ��� Ȯ���ϴ� �Լ��� ȣ�⸸ �ϸ� �� <���������� ����ϴ� �Լ��̹Ƿ� ����ڴ� ��������> */
	public void updateStatus(int itype, boolean type, float x, float y, int touchID){
		// TouchPad Logic ����
		if(usingInputManager){
			if(useingTouchPad)
			{
				// AB��ư Logic
				ButtonObject tmp = ButtonList.get(0);
				tmp.OnEvent(type, x, y);
				A = tmp.active;
				//ButtonList.get(1).OnEvent(type, x, y);
				tmp = ButtonList.get(1);
				tmp.OnEvent(type, x, y);
				B = tmp.active;
				
				if(mtouchID == -1 && Util.CircleCrashObject2Point(handle2, 0f, new ius_Point(x,y))){
					// ��ư�� ��ġ�� �Էµ� ��� (��ư�� ����)
					mtouchID = touchID;
				}
				if(itype == 2 && mtouchID==touchID){
					// �����ư�� ���� �H�� �� �����ϴ� �κ����� �ʱ�ȭ��
					mtouchID = -1;	
					handle2.x = handle.x;
					handle2.y = handle.y;
					LEFT=false;RIGHT=false;UP=false;DOWN=false;
				}
				else if(type && mtouchID==touchID){
					if(Util.CircleCrashObject2Point(handle, 0f, new ius_Point(x,y))){
						//��ư�� �ִ� ��ư�ȿ� ���� ���
						handle2.x = x;
						handle2.y = y;
					}
					else{
						//��ư�� �ܰ����� ��� ���
						float L = (float) Math.sqrt(Util.Multyply(handle.x - x) + Util.Multyply(handle.y - y));
						float K = 100f;
						handle2.x = K/L*(x-handle.x) + handle.x;
						handle2.y = K/L*(y-handle.y) + handle.y;
					}
					final float Index = 10f;
					LEFT = false;RIGHT=false;UP=false;DOWN=false;
					if(handle2.x<handle.x-Index)LEFT= true;
					if(handle2.x>handle.x+Index)RIGHT= true;
					if(handle2.y<handle.y-Index)DOWN= true;
					if(handle2.y>handle.y+Index)UP= true;
				}
			}
			
			for(int i=2;i<ButtonList.size();i++)
			{
				ButtonObject tmp = ButtonList.get(i);
				tmp.OnEvent(type, x, y);
			}
		}
	}
	/* ��ư�� ���¸� �ʱ�ȭ�� */
	public void resetStatus(){
		for(int i=0;i<ButtonList.size();i++)
			ButtonList.get(i).reset();
	}
}
