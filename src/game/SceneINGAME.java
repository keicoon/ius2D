package game;

import java.util.ArrayList;

import android.content.Context;

import com.example.opengles20.GameObject;
import com.example.opengles20.myGLRenderer;
import com.example.opengles20.myGLSurfaceView;
import common.FrameCounter;

import ius.Scene;
import ius.Util;

public class SceneINGAME extends Scene{

	GameObject bg;
	GameObject player;
	
	boolean isFalling;
	int SCORE, LIFE;
	ArrayList<GameObject> bullet;
	ArrayList<GameObject> monster;
	ArrayList<GameObject> particle;
	GAME_MapManager GMM;
	
	long playtime;
	
	public SceneINGAME(Context context, myGLSurfaceView gl20){
		super(context, gl20);
	}
	public void Init(){
		mGL20.inputManager.SetInput(mContext, mGL20, true);
		mGL20.timeManager.AddTimer("TotalTimer", 1.0f);
		mGL20.soundManager.LoadSound("cheetahmen.mp3",  true);
		mGL20.soundManager.LoadSound("menu_select.mp3", false);
		
		mGL20.inputManager.setButtonObject(
				"spr_util", 0, 0, 
				screenWidth-50f, screenHeight-40f,
				0f, 0.5f,
				2, "����",
				0x00FF0000,
				1f);
		
		playtime =0;
		SCORE = 0;
		LIFE = 100;
		bullet = new ArrayList<GameObject>();
		monster = new ArrayList<GameObject>();
		particle = new ArrayList<GameObject>();
		GMM = new GAME_MapManager(mContext, mGL20 ,monster);
		
		bg = mGL20.objectmanager.newItem(
				mContext, mGL20,
				"spr_util", 1, 1,
				myGLRenderer.mScreenWidth*0.5f, myGLRenderer.mScreenHeight*0.5f,
				0f, 1.0f);
		
		player = mGL20.objectmanager.newItem(
				mContext, mGL20,
				"spr_cheetahmen_1", 0, 0,
				100f, 195f,
				0f, 3.0f);
		player.fPower = 0f;
		player.fSpeed = 200f;
		player.bDeath = false;
		player.time = mGL20.timeManager.AddTimer(1.0f,false);
		
		mGL20.soundManager.PlayBGMSound();
		setShader(0);	//default Shader
	}
	public void Destroy(){
		/* Before Destroy, must call Manager's clearFuction */
		mGL20.timeManager.ClearTime();
		mGL20.soundManager.ClearSound();
		mGL20.inputManager.deleteButtonObject();
		mGL20.objectmanager.ClearItem();
	}
	protected void Draw(){
		
		/* formal object must call before font Draw */
		bg.Draw(screenWidth, screenHeight);
		GMM.Draw();
		// draw bullet
		for(int i=0; i<bullet.size();i++){
			GameObject tmp_bullet = bullet.get(i);
			tmp_bullet.Draw();
		}
		// draw monster
		for(int i=0; i<monster.size();i++){
			GameObject tmp_monster = monster.get(i);
			tmp_monster.Draw();
		}
		// draw particle
		for(int i=0; i<particle.size();i++){
			GameObject tmp_particle = particle.get(i);
			if(!tmp_particle.bDeath)
				tmp_particle.Draw();
		}
		if(!player.bDeath)
			player.Draw();
		
		mGL20.inputManager.draw();
		
		mGL20.fontManager.FontBegin();
		mGL20.fontManager.draw(
				"PLAY TIME : " + playtime,
				screenWidth/2,screenHeight-25f,true,
				0x00000000,
				1.0f);
		mGL20.fontManager.draw(
				"FPS : " + FrameCounter.getFrameCount(),
				screenWidth/2,screenHeight-55f,true,
				0x00000000,
				1.0f);
		mGL20.fontManager.draw(
				"SCORE : " + int2String(SCORE),
				10f,screenHeight-40f,false,
				0x000000FF,
				1.0f);
		mGL20.fontManager.draw(
				"LIFE : " + LIFE,
				10f,screenHeight-70f,false,
				0x000000FF,
				1.0f);
		mGL20.fontManager.FontEnd();
	}
	protected void Update(float INTERVAL){
		mGL20.timeManager.time(INTERVAL);
		/* �ð� ���� Update ó�� �Լ� */
		processTime();

		if(!player.bDeath){
		/* �浹 ó�� <�÷��̾�� ��>*/
		boolean isLand = GMM.IsCrash(player);
		/* �÷��̾� �߷� ����*/
		if(!isLand){
			if(player.y < 0)
				// �ٴ� �Ʒ��� ��� < ���� >
				DeadTrag();
			else if(!isFalling){
				isFalling = true;
				// ���� ���� ��Ͻ���
				player.fPower = 0f;
			}else
				// ���� ���̸� ����
				player.fPower += 250f*INTERVAL;
			
			player.translate(0, -250f*INTERVAL);
		}
		else {
			isFalling = false;
			if(player.fPower > 400f)
				// ���� ������ ���� �̻��� ��� < ���� >
				DeadTrag();
		}
			
		// ���� ����� ��� �ְ���̱��� ����
		if(player.bJump){
			if(player.y < player.fLimit)
				player.translate(0, 500f*INTERVAL);
			else
				player.bJump = false;
		}
		// �÷��̾� ��� �ʱ� ����
		if(player.iPreState == 2)
			// ���� ����� ��쿡�� ����
			player.iState = player.iPreState;
		else
			// �̿��� ����� ���� ������� ����
			player.iState = 0; // stayMotion
		
		if(mGL20.inputManager.LEFT){
			if(!player.fleep) player.fleep = true;
			player.iState = 1; // moveMotion
			player.ani_num = 0;
			// ���� ��ũ�� ��� �˻�
			if(player.x-player.Get_HalfWidth()>0)
				player.translate(-player.fSpeed*INTERVAL, 0f);
			else
				player.x = player.Get_HalfWidth();
		}
		else if(mGL20.inputManager.RIGHT){
			if(player.fleep) player.fleep = false;
			player.iState = 1; // moveMotion
			player.ani_num = 0;
			// ������ ��ũ�� ��� �˻�
			if(player.x+player.Get_HalfWidth()<screenWidth/2)
				player.translate(player.fSpeed*INTERVAL, 0f);
			else{
				player.x = screenWidth/2 - player.Get_HalfWidth();
				GMM.translateMap(-player.fSpeed*INTERVAL, 0f);
			}
		}

		if(mGL20.inputManager.A)
			/* �Ѿ� ���� �ӵ� ó�� */
			if(player.time.isOn()){
				player.iState = 2; // attackMotion
				player.ani_num = 1;
				// �Ѿ� ����
				GameObject tmp_bullet = mGL20.objectmanager.newItem(
						mContext, mGL20,
						"spr_cheetahmen_1", 3, 0,
						player.x+((player.fleep == false)?40f:-40f), player.y+40f,
						0f, 3.0f);
				tmp_bullet.iDirection = (player.fleep == false)?1:-1;
				tmp_bullet.bDeath = false;
				tmp_bullet.fSpeed = 480f;
				bullet.add(tmp_bullet);
			}
		if(mGL20.inputManager.B && isLand){
			player.bJump = true; // jumpMotion
			// ���� ���� ����
			player.fLimit = player.y+150f;
		}
		// �÷��̾� ��� ������ ����
				if(player.iState == 1 || player.iState == 2)
					if(player.animation(0.5f, INTERVAL) && player.iState == 2){
						//���ݸ���� ������ �ٽ� �⺻������� ��ȯ
						player.iState = 0;
						player.ani_num = 0;
						player.spr_num = 0;
					}
				player.iPreState = player.iState;		
		}
		
		if(mGL20.inputManager.getActive(2)){
			mGL20.ChangeScene(this, new SceneTITLE(mContext, mGL20));
		}
		/* ���� ������ ó�� */
		for(int i=0; i<monster.size();i++){
			GameObject tmp_monster = monster.get(i);
			// crash < ���Ͱ� ȭ�� >
			for(int j=0; j<bullet.size();j++){
				GameObject tmp_bullet = bullet.get(j);
				if(Util.RectCrashObject2Object_A(tmp_monster, 0f, tmp_bullet, 0f)){
					tmp_bullet.bDeath = true;
					tmp_monster.bDeath = true;
					useParticle(tmp_monster.x, tmp_monster.y, 1);
					//TODO ����
					SCORE += 100f;
					break;
				}
			}
			// crash < ���Ͱ� ĳ���� >
			if(!tmp_monster.bDeath && Util.RectCrashObject2Object_A(tmp_monster, 0f, player, 0f)){
				//TODO ������ ����
				
				if(!player.bDeath){
					if(LIFE<=0)
						// ���� ����
						DeadTrag();
					else
						LIFE -= 20;
				}
				tmp_monster.bDeath = true;
			}
			// move monster
			tmp_monster.animation(1f, INTERVAL);
			tmp_monster.translate(tmp_monster.fSpeed*INTERVAL, 0f);
			if(tmp_monster.bDeath
					|| tmp_monster.x < 0f){
				// delete monster
				monster.remove(i--);
				mGL20.objectmanager.RemoveItem(tmp_monster);
			}
		}
		/* �Ѿ� ������ ó�� */
		for(int i=0; i<bullet.size();i++){
			// move bullet
			GameObject tmp_bullet = bullet.get(i);
			tmp_bullet.translate(tmp_bullet.iDirection*tmp_bullet.fSpeed*INTERVAL, 0f);
			if(tmp_bullet.bDeath
					|| tmp_bullet.x > screenWidth
					|| tmp_bullet.x < 0f){
				// delete bullet
				bullet.remove(i--);
				mGL20.objectmanager.RemoveItem(tmp_bullet);
			}
		}
		/* ��ƼŬ ó�� */
		for(int i=0; i<particle.size();i++){
			GameObject tmp_particle = particle.get(i);
			if(!tmp_particle.bDeath){
				if(tmp_particle.animation(1f, INTERVAL) && ++tmp_particle.iState >= tmp_particle.iPreState)
						tmp_particle.bDeath = true;
			}
		}
		if(player.bDeath){
			if(doFadeOut(INTERVAL, 0.2f))
				// ����ȭ������ ��ȯ��
				mGL20.ChangeScene(this, new SceneINGAME(mContext, mGL20));
		}
		//SetQuakeSize(20f, 2.0f);
		//setFadeState(1.0f);
		//doFadeIn(INTERVAL, 0.2f);
		//doFadeOut(INTERVAL, 0.2f);
		//doQuakeEffect(INTERVAL);
	}
	private void processTime(){
		/* ������ ���� �ð� ó��*/
		if(mGL20.timeManager.EvnetOnTimer("TotalTimer"))
			++playtime;
	}
	@Override
	protected void Input() {
		// TODO Auto-generated method stub
	}
	private void useParticle(float X, float Y, int TIME){
		for (int i=0; i< particle.size();i++){
			GameObject tmp_particle = particle.get(i);
			if(tmp_particle.bDeath){
				tmp_particle.bDeath = false;
				tmp_particle.setPosition(X, Y);
				tmp_particle.iState = 0;
				tmp_particle.iPreState = TIME;
				return ;
			}
		}
		GameObject tmp_particle = mGL20.objectmanager.newItem(
					mContext, mGL20,
					"spr_cheetahmen_1", 6, 0,
					myGLRenderer.mScreenWidth*0.5f, myGLRenderer.mScreenHeight*0.5f,
					0f, 3.0f);
		tmp_particle.bDeath = false;
		tmp_particle.setPosition(X, Y);
		tmp_particle.iState = 0;
		tmp_particle.iPreState = TIME;
		particle.add(tmp_particle);
		return ;
	}
	private String int2String(int INT){
		int temp_int = INT;
		int t_length = 8;
		int length;
		String returnString="";
		
		for(length = 0;temp_int>0;temp_int*=0.1,++length);
		
		for(int i=0;i<t_length;i++){
			if(t_length-i<=length){
				returnString += String.valueOf(INT);
				break;
			}
			else
				returnString += "0";
		}
		//for(int i=0;i<length;i++){
		//	returnString += String.valueOf(INT/10);
		//	INT/=10;
		//}
		return returnString;
	}
	private void DeadTrag(){
		mGL20.soundManager.PauseBGMSound(false);
		useParticle(player.x, player.y, 3);
		player.bDeath = true;
		// ��� ���̴� ������
//		setShader(myGLRenderer.mBWObjectProgramHandle);
		setFadeState(0.0f);
	}
}
