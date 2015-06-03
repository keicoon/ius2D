package game;

import android.content.Context;

import com.example.opengles20.GameObject;
import com.example.opengles20.myGLSurfaceView;

import ius.Scene;

public class SceneLOADING extends Scene{

	int fadeState;
	GameObject BG;
	
	public SceneLOADING(Context context, myGLSurfaceView gl20) {
		super(context, gl20);
	}

	@Override
	public void Init() {
		/* TODO 시간이 걸리는 texture를 미리 읽음 */
		mGL20.atlasManager.setAtlas(mContext, "spr_bg");
		mGL20.atlasManager.setAtlas(mContext, "spr_cheetahmen_1");
		mGL20.atlasManager.setAtlas(mContext, "spr_map_object");
		mGL20.atlasManager.setAtlas(mContext, "spr_util");
		
		BG = mGL20.objectmanager.newItem(
				mContext, mGL20,
				"spr_util", 1, 1,
				screenWidth*0.5f, screenHeight*0.5f,
				0f, 1.0f);
		fadeState = 0;
		setShader(0);	//default Shader
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		mGL20.objectmanager.ClearItem();
	}

	@Override
	protected void Draw() {
		// TODO Auto-generated method stub
		BG.Draw(screenWidth, screenHeight);
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
				mGL20.ChangeScene(this, new SceneTITLE(mContext, mGL20));
				//mGL20.ChangeScene(this, new SceneINGAME(mContext, mGL20));
			break;
		}
	}

	@Override
	protected void Input() {
		// TODO Auto-generated method stub
		
	}

}
