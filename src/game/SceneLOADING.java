package game;

import com.example.opengles20.myGLSurfaceView;

import ius.GameObject;
import ius.Scene;

public class SceneLOADING extends Scene{

	int fadeState;
	GameObject BG;
	
	public SceneLOADING(myGLSurfaceView gl20) {
		super( gl20);
	}

	@Override
	public void Init() {
		/* TODO 시간이 걸리는 texture를 미리 읽음 */
		AM.setAtlas("spr_bg");
		AM.setAtlas("spr_cheetahmen_1");
		AM.setAtlas("spr_map_object");
		AM.setAtlas("spr_util");
		/* TODO 시간이 걸리는 sound를 미리 읽음 */
		SM.LoadSound("title.mp3",  true);
		SM.LoadSound("cheetahmen.mp3",  true);
		SM.LoadSound("deadtrag.mp3", true);
		SM.LoadSound("menu_select.mp3", false);
		SM.LoadSound("hit.wav", false);
		SM.LoadSound("hurt.wav", false);
		
		BG = OM.newItem(
				"spr_util", 1, 2,
				screenWidth*0.5f, screenHeight*0.5f,
				0f, 3.0f);
		fadeState = 0;
		setShader(0);	//default Shader
	}

	@Override
	protected void Draw() {
		// TODO Auto-generated method stub
		BG.Draw();
	}

	@Override
	protected void Update(float INTERVAL) {
		switch(fadeState)
		{
		case 0:
			setFadeState(1.0f);
			fadeState = 1;
			break;
		case 1:
			if(doFadeIn(INTERVAL, 0.3f))
				fadeState = 2;
			break;
		case 2:
			setFadeState(0.0f);
			fadeState = 3;
			break;
		case 3:
			if(doFadeOut(INTERVAL, 0.3f))
				//TODO change nextScene
				mGL20.ChangeScene(this, new SceneINGAME(mGL20));
			break;
		}
	}

	@Override
	protected void Input() {
		// TODO Auto-generated method stub
		
	}

}
