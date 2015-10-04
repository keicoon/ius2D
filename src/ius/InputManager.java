package ius;

import ius.Util.ius_Point;

import java.util.ArrayList;

import android.annotation.SuppressLint;

import android.opengl.GLES20;
import android.util.Log;

import com.example.opengles20.myGLRenderer;

@SuppressLint("RtlHardcoded")

/* InputManager Class
 * Function : 일반 버튼, 방향 버튼 및 AB 버튼의 동작을 수행하고 관리함
 * 사용하기전에 꼭 SetInput 함수를 사용하여 초기화를 해야함
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

		if(useingTouchPad)// 터치패드를 사용한다면 수행되어야하는 부분 1 <특징 : 고정된 위치에 버튼을 생성함>
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
	/* 일반적인 버튼을 추가하고 싶다면 아래 함수를 사용하여 초기화를 하고 ButtonList에 추가해야함 */
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
	/* 매 씬이 끝날때마다 버튼들을 삭제해서 관리해야함 <차후 수정이 필요한 부분> */
	public void deleteButtonObject(){
		Log.d("input","버튼이 삭제됨");
		handle = null;handle2 = null;
		ButtonList.clear();
		usingInputManager = false;
	}
	/* 버튼을 실질적으로 그리는 부분으로 씬에서 한번만 Draw()를 하면 됨 */
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
	/* 버튼이 실직적으로 눌렸는지 확인하는 함수 <예를들면 1번 버튼의 입력을 확인하여 눌렸다면 TRUE를 리턴함 */
	public boolean getActive(int id){
		return ButtonList.get(id).active;
	}
	/* 터치가 눌렸는지 확인하는 함수로 호출만 하면 됨 <내부적으로 사용하는 함수이므로 사용자는 쓰지않음> */
	public void updateStatus(int itype, boolean type, float x, float y, int touchID){
		// TouchPad Logic 시작
		if(usingInputManager){
			if(useingTouchPad)
			{
				// AB버튼 Logic
				ButtonObject tmp = ButtonList.get(0);
				tmp.OnEvent(type, x, y);
				A = tmp.active;
				//ButtonList.get(1).OnEvent(type, x, y);
				tmp = ButtonList.get(1);
				tmp.OnEvent(type, x, y);
				B = tmp.active;
				
				if(mtouchID == -1 && Util.CircleCrashObject2Point(handle2, 0f, new ius_Point(x,y))){
					// 버튼에 터치가 입력된 경우 (버튼을 잡음)
					mtouchID = touchID;
				}
				if(itype == 2 && mtouchID==touchID){
					// 방향버튼의 손을 똇을 때 동작하는 부분으로 초기화함
					mtouchID = -1;	
					handle2.x = handle.x;
					handle2.y = handle.y;
					LEFT=false;RIGHT=false;UP=false;DOWN=false;
				}
				else if(type && mtouchID==touchID){
					if(Util.CircleCrashObject2Point(handle, 0f, new ius_Point(x,y))){
						//버튼이 최대 버튼안에 속한 경우
						handle2.x = x;
						handle2.y = y;
					}
					else{
						//버튼이 외각으로 벗어난 경우
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
	/* 버튼의 상태를 초기화함 */
	public void resetStatus(){
		for(int i=0;i<ButtonList.size();i++)
			ButtonList.get(i).reset();
	}
}
