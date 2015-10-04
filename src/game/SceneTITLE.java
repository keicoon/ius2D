package game;

import android.view.MotionEvent;

import com.example.opengles20.myGLSurfaceView;

import ius.GameObject;
import ius.Scene;

public class SceneTITLE extends Scene {
	
	GameObject BG;
	boolean flag;
	public SceneTITLE(myGLSurfaceView gl20) {
		super(gl20);
	}

	@Override
	public void Init() {
		flag = false;
		
		BG = OM.newItem(
				"spr_bg", 0, 0,
				screenWidth*0.5f, screenHeight*0.5f,
				0f, 1.0f);
		SM.PlayBGMSound("title.mp3");
		setShader(0);	//default Shader
	}
	@Override
	protected void Draw() {
		// TODO Auto-generated method stub
		BG.Draw(screenWidth, screenHeight);
	}

	@Override
	protected void Update(float INTERVAL) {
		if(doFadeOut(INTERVAL, 0.3f))
			//TODO change nextScene
			mGL20.ChangeScene(this, new ScenePrologue(mGL20));
	}

	@Override
	protected void Input() {
		// TODO Auto-generated method stub
		if(TOUCH_EVENT == MotionEvent.ACTION_DOWN){
			flag = true;
		}
		else if(flag && TOUCH_EVENT == MotionEvent.ACTION_UP){
			SM.PlaySound("menu_select.mp3", false);
			setFadeState(1.0f);
		}

	}
}
