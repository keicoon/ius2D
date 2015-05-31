package game;

import android.content.Context;

import com.example.opengles20.GameObject;
import com.example.opengles20.PrintTextObject;
import com.example.opengles20.R;
import com.example.opengles20.myGLRenderer;
import com.example.opengles20.myGLSurfaceView;

import ius.Scene;

public class SceneINGAME extends Scene{

	float screenWidth;
	float screenHeight;
	GameObject bg;
	GameObject goTest;
	GameObject goTest2;
	PrintTextObject textLine;
	
	long playtime;
	int currentFPS, prevFPS;
	public SceneINGAME(Context context, myGLSurfaceView gl20){
		super(context, gl20);
		screenWidth = myGLRenderer.mScreenWidth;
		screenHeight = myGLRenderer.mScreenHeight;
	}
	public void Init(){
		
		mGL20.atlasManager.setAtlas(mContext, "spr_bg_fantasy");
		mGL20.atlasManager.setAtlas(mContext, "spr_cheetahmen_1");
		mGL20.atlasManager.setAtlas(mContext, "spr_cheetahmen_2");
		mGL20.atlasManager.setAtlas(mContext, "spr_util");
		mGL20.inputManager.SetInput(mContext, mGL20, true);
		mGL20.timeManager.AddTimer("1second", 1.0f);
		
		mGL20.soundManager.LoadSound("cheetahmen", R.raw.cheetahmen,  true);
		mGL20.soundManager.LoadSound("menu_select", R.raw.menu_select, false);
		
		bg = mGL20.objectmanager.newItem(
				mContext, mGL20,
				"spr_bg_fantasy", 0, 0,
				screenWidth/2, screenHeight/2,
				0f, 1.0f);
		goTest = mGL20.objectmanager.newItem(
				mContext, mGL20,
				"spr_cheetahmen_1", 0, 0,
				screenWidth/2, screenHeight/2,
				0f, 3.0f);
		
		goTest2 = mGL20.objectmanager.newItem(
				mContext, mGL20,
				"spr_cheetahmen_2", 0, 0,
				screenWidth/2+140, screenHeight/2+140,
				0f, 3.0f);
		mGL20.inputManager.setButtonObject(
				"spr_util", 0, 0, 
				screenWidth-50f, screenHeight-40f,
				0f, 0.5f,
				3, "종료",
				0x00FF0000,
				1f);
		
		playtime =0;
		currentFPS =0;
		prevFPS =0;
		
		textLine = new PrintTextObject(mGL20.fontManager,
				"안 녕 하 세 요 \n테스트입니다...\n이렇게 해도? 되는거겠죠??",
				screenWidth/2, screenHeight-40f,
				0x00AFEEEE);
		
		mGL20.soundManager.PlayBGMSound();
		setShader(0);	//default Shader
	}
	public void Destroy(){
		mGL20.soundManager.ReleaseBGMSound();
		mGL20.objectmanager.Dispose();
		mGL20.inputManager.deleteButtonObject();
	}
	protected void Draw(){
		
		bg.Draw();
		goTest.Draw();
		goTest2.Draw();

		mGL20.inputManager.draw();
		
		mGL20.fontManager.FontBegin();
		mGL20.fontManager.draw(
				"FPS : " + prevFPS,
				0f,screenHeight-40f,false,
				0x00FFFFFF,
				1.0f);
		mGL20.fontManager.draw(
				"PLAY TIME : " + playtime,
				0f,screenHeight-80f,false,
				0x00FFFFFF,
				1.0f);
		
		textLine.Draw();
		
		mGL20.fontManager.FontEnd();
	}
	protected void Update(long elapsed){
		float INTERVAL = (float)elapsed/1000f;
		goTest.animation(1.0f, INTERVAL);
		if(mGL20.timeManager.EvnetOnTimer("1second")){
			++playtime;
			prevFPS = currentFPS;
			currentFPS = 0;
		}
		currentFPS++;
		
		if(mGL20.inputManager.LEFT)goTest.translate(-30f*INTERVAL, 0f);
		else if(mGL20.inputManager.RIGHT)goTest.translate(30f*INTERVAL, 0f);
		if(mGL20.inputManager.UP)goTest.translate(0f, 30f*INTERVAL);
		else if(mGL20.inputManager.DOWN)goTest.translate(0f, -30f*INTERVAL);
		if(mGL20.inputManager.A){
			mGL20.soundManager.PlaySound("menu_select", false);
			//SetQuakeSize(20f, 2.0f);
			//setFadeState(1.0f);
			//setShader(myGLRenderer.mBWObjectProgramHandle);
		}
		mGL20.timeManager.time(INTERVAL);
		
		textLine.Update(INTERVAL, mGL20.inputManager.B);
		
		//doFadeIn(INTERVAL, 0.2f);
		//doFadeOut(INTERVAL, 0.2f);
		//doQuakeEffect(INTERVAL);
	}
	@Override
	protected void Input() {
		// TODO Auto-generated method stub
		
		//Log.d("TOUCH",TOUCH_X+","+TOUCH_Y);
	}
}
