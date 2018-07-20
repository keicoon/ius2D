package game;

import android.view.MotionEvent;

import com.example.opengles20.myGLSurfaceView;

import ius.GameObject;
import ius.PrintTextObject;
import ius.Scene;

public class ScenePrologue extends Scene{
	
	GameObject BG;
	int INDEX;
	boolean onTOUCH, flag;
	PrintTextObject textLine1;
	PrintTextObject textLine2;
	PrintTextObject textLine3;
	PrintTextObject textLine4;
	public ScenePrologue(myGLSurfaceView gl20) {
		super(gl20);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Init() {
		BG = OM.newItem(
				"spr_bg", 1, 0,
				screenWidth*0.5f, 550f,
				0f, 1.0f);
		
		INDEX = 0;
		onTOUCH = false;
		
		textLine1 = new PrintTextObject(FM,
				"Mad Scientist가 세계를 위협한다.\n치타맨은 그를 막기위해 출동한다.\n어둠을 가르며 그의 부하를 쫓는다.\n",
				200f, 300f,
				0x00FFFFFF);
		textLine2 = new PrintTextObject(FM,
				"치타맨은 그의 부하를 찾았다!\n긴 싸움끝에 부하를 굴복시키고......!\n",
				200f, 300f,
				0x00FFD700);
		textLine3 = new PrintTextObject(FM,
				"부하로부터 과학자의 위치를 자백받는다.\n치타맨은 그를 찾으러 떠난다.\n",
				200f, 300f,
				0x0098FB98);
		textLine4= new PrintTextObject(FM,
				"치타맨... 드디어!!\n과학자의 위치를 찾았다.\n이제 치타맨에게 세계의 운명이 달렸다.\n",
				200f, 300f,
				0x00FFD700);
		SM.PlayBGMSound("title.mp3");
		setShader(0);	//default Shader
	}

	@Override
	protected void Draw() {
		// TODO Auto-generated method stub
		BG.Draw(800f,300f);
		
		FM.FontBegin();
		switch(INDEX)
		{
			case 0:
				textLine1.Draw(textLine1.getX()+3f,textLine1.getY()-3f,0x00FFD700);
				textLine1.Draw();
				break;
			case 1:
				textLine2.Draw(textLine2.getX()+3f,textLine2.getY()-3f,0x00F4A460);
				textLine2.Draw();
				break;
			case 2:
				textLine3.Draw(textLine3.getX()+3f,textLine3.getY()-3f,0x00FFD700);
				textLine3.Draw();
				break;
			case 3:
				textLine4.Draw(textLine4.getX()+3f,textLine4.getY()-3f,0x00FFFFFF);
				textLine4.Draw();
				break;
		}
		FM.FontEnd();
	}

	@Override
	protected void Update(float INTERVAL) {
		switch(INDEX)
		{
			case 0:
				if(textLine1.Update(INTERVAL, onTOUCH)){
					SM.PlaySound("menu_select.mp3", false);
					++INDEX; BG.spr_num = 1;
				}
				onTOUCH = false;
				break;
			case 1:
				if(textLine2.Update(INTERVAL, onTOUCH)){
					SM.PlaySound("menu_select.mp3", false);
					++INDEX; BG.spr_num = 2;
				}
				onTOUCH = false;
				break;
			case 2:
				if(textLine3.Update(INTERVAL, onTOUCH)){
					SM.PlaySound("menu_select.mp3", false);
					++INDEX; BG.spr_num = 3;
				}
				onTOUCH = false;
				break;
			case 3:
				if(textLine4.Update(INTERVAL, onTOUCH)){
					SM.PlaySound("menu_select.mp3", false);
					mGL20.ChangeScene(this, new SceneINGAME(mGL20));
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
