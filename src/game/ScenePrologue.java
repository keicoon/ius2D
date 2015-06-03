package game;

import android.content.Context;
import android.view.MotionEvent;

import com.example.opengles20.GameObject;
import com.example.opengles20.PrintTextObject;
import com.example.opengles20.myGLSurfaceView;

import ius.Scene;

public class ScenePrologue extends Scene{
	
	GameObject BG;
	int INDEX;
	boolean onTOUCH, flag;
	PrintTextObject textLine1;
	PrintTextObject textLine2;
	PrintTextObject textLine3;
	PrintTextObject textLine4;
	public ScenePrologue(Context context, myGLSurfaceView gl20) {
		super(context, gl20);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Init() {
		mGL20.soundManager.LoadSound("cheetahmen.mp3",  true);
		mGL20.soundManager.LoadSound("menu_select.mp3", false);
		BG = mGL20.objectmanager.newItem(
				mContext, mGL20,
				"spr_bg", 1, 0,
				screenWidth*0.5f, 550f,
				0f, 1.0f);
		
		INDEX = 0;
		onTOUCH = false;
		
		textLine1 = new PrintTextObject(mGL20.fontManager,
				"Mad Scientist�� ���踦 �����Ѵ�.\nġŸ���� �׸� �������� �⵿�Ѵ�.\n����� ������ ���� ���ϸ� �Ѵ´�.\n",
				200f, 300f,
				0x00AFEEEE);
		textLine2 = new PrintTextObject(mGL20.fontManager,
				"ġŸ���� ���� ���ϸ� ã�Ҵ�!\n�� �ο򳡿� ���ϸ� ������Ű��......!\n",
				200f, 300f,
				0x00AFEEEE);
		textLine3 = new PrintTextObject(mGL20.fontManager,
				"���Ϸκ��� �������� ��ġ�� �ڹ�޴´�.\nġŸ���� �׸� ã���� ������.\n",
				200f, 300f,
				0x00AFEEEE);
		textLine4= new PrintTextObject(mGL20.fontManager,
				"ġŸ��... ����!!\n�������� ��ġ�� ã�Ҵ�.\n���� ġŸ�ǿ��� ������ ����� �޷ȴ�.\n",
				200f, 300f,
				0x00AFEEEE);
		mGL20.soundManager.PlayBGMSound();
		setShader(0);	//default Shader
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		mGL20.soundManager.ClearSound();
		mGL20.objectmanager.ClearItem();
	}

	@Override
	protected void Draw() {
		// TODO Auto-generated method stub
		BG.Draw(800f,300f);
		
		mGL20.fontManager.FontBegin();
		switch(INDEX)
		{
			case 0:
				textLine1.Draw();
				break;
			case 1:
				textLine2.Draw();
				break;
			case 2:
				textLine3.Draw();
				break;
			case 3:
				textLine4.Draw();
				break;
		}
		mGL20.fontManager.FontEnd();
	}

	@Override
	protected void Update(float INTERVAL) {
		switch(INDEX)
		{
			case 0:
				if(textLine1.Update(INTERVAL, onTOUCH)){
					mGL20.soundManager.PlaySound("menu_select.mp3", false);
					++INDEX; BG.spr_num = 1;
				}
				onTOUCH = false;
				break;
			case 1:
				if(textLine2.Update(INTERVAL, onTOUCH)){
					mGL20.soundManager.PlaySound("menu_select.mp3", false);
					++INDEX; BG.spr_num = 2;
				}
				onTOUCH = false;
				break;
			case 2:
				if(textLine3.Update(INTERVAL, onTOUCH)){
					mGL20.soundManager.PlaySound("menu_select.mp3", false);
					++INDEX; BG.spr_num = 3;
				}
				onTOUCH = false;
				break;
			case 3:
				if(textLine4.Update(INTERVAL, onTOUCH)){
					mGL20.soundManager.PlaySound("menu_select.mp3", false);
					mGL20.ChangeScene(this, new SceneINGAME(mContext, mGL20));
				}
				onTOUCH = false;
				break;
		}
	}

	@Override
	protected void Input() {
		// TODO Auto-generated method stub
		if(TOUCH_EVENT == MotionEvent.ACTION_DOWN){
			flag = true;
		}
		else if(flag && TOUCH_EVENT == MotionEvent.ACTION_UP){
			flag = false;
			onTOUCH = true;
		}
	}
}
