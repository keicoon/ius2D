package game;

import java.util.ArrayList;

import com.example.opengles20.myGLRenderer;
import com.example.opengles20.myGLSurfaceView;

import common.FrameCounter;
import ius.GameObject;
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
	GameObject life[];
	GAME_MapManager GMM;
	
	long playtime;
	
	public SceneINGAME(myGLSurfaceView gl20){
		super(gl20);
	}
	public void Init(){
		IM.SetInput(true);
		TM.AddTimer("TotalTimer", 1.0f);
		
		IM.setButtonObject(
				"spr_util", 0, 0, 
				screenWidth-50f, screenHeight-40f,
				0f, 0.5f,
				2, "����",
				0x00FF0000,
				1f);
		
		playtime =0;
		SCORE = 0;
		LIFE = 5;
		bullet = new ArrayList<GameObject>();
		monster = new ArrayList<GameObject>();
		particle = new ArrayList<GameObject>();
		GMM = new GAME_MapManager(mGL20 ,monster);
		
		life = new GameObject[5];
		for(int i=0; i<LIFE;i++)
			life[i] = OM.newItem(
					"spr_cheetahmen_1", 3, 1,
					145f+24f*i,screenHeight-52f,
					0f, 3.0f);
		
		bg = OM.newItem(
				"spr_util", 1, 1,
				myGLRenderer.mScreenWidth*0.5f, myGLRenderer.mScreenHeight*0.5f,
				0f, 1.0f);
		
		player = OM.newItem(
				"spr_cheetahmen_1", 0, 0,
				100f, 195f,
				0f, 3.0f);
		player.fPower = 0f;
		player.fSpeed = 200f;
		player.bDeath = false;
		player.time = TM.AddTimer(1.0f,false);
		
		SM.PlayBGMSound("cheetahmen.mp3");
		setShader(0);	//default Shader
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
		for(int i=0;i<LIFE;i++)
			life[i].Draw();
		
		if(!player.bDeath)
			player.Draw();
		
		IM.draw();
		
		FM.FontBegin();
		FM.draw(
				"PLAY TIME : " + playtime,
				screenWidth/2,screenHeight-25f,true,
				0x00000000,
				1.0f);
		FM.draw(
				"FPS : " + FrameCounter.getFrameCount(),
				screenWidth/2,screenHeight-55f,true,
				0x00000000,
				1.0f);
		FM.draw(
				"SCORE : " + int2String(SCORE),
				10f,screenHeight-40f,false,
				0x00000000,
				1.0f);
		FM.draw(
				"LIFE ",
				10f,screenHeight-70f,false,
				0x00000000,
				1.0f);
		FM.FontEnd();
	}
	protected void Update(float INTERVAL){
		TM.time(INTERVAL);
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
		
		if(IM.LEFT){
			if(!player.fleep) player.fleep = true;
			player.iState = 1; // moveMotion
			player.ani_num = 0;
			// ���� ��ũ�� ��� �˻�
			if(player.x-player.Get_HalfWidth()>0)
				player.translate(-player.fSpeed*INTERVAL, 0f);
			else
				player.x = player.Get_HalfWidth();
		}
		else if(IM.RIGHT){
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

		if(IM.A)
			/* �Ѿ� ���� �ӵ� ó�� */
			if(player.time.isOn()){
				player.iState = 2; // attackMotion
				player.ani_num = 1;
				// �Ѿ� ����
				GameObject tmp_bullet = OM.newItem(
						"spr_cheetahmen_1", 3, 0,
						player.x+((player.fleep == false)?40f:-40f), player.y+40f,
						0f, 3.0f);
				tmp_bullet.iDirection = (player.fleep == false)?1:-1;
				tmp_bullet.bDeath = false;
				tmp_bullet.fSpeed = 480f;
				bullet.add(tmp_bullet);
			}
		if(IM.B && isLand){
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
		
		if(IM.getActive(2)){
			mGL20.ChangeScene(this, new SceneTITLE(mGL20));
		}
		/* ���� ������ ó�� */
		for(int i=0; i<monster.size();i++){
			GameObject tmp_monster = monster.get(i);
			// crash < ���Ͱ� ȭ�� >
			for(int j=0; j<bullet.size();j++){
				GameObject tmp_bullet = bullet.get(j);
				if(Util.RectCrashObject2Object_A(tmp_monster, 0f, tmp_bullet, 0f)){
					SM.PlaySound("hit.wav", false);
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
					SM.PlaySound("hurt.wav", false);
					--LIFE;
					if(LIFE<1)
						// ���� ����
						DeadTrag();
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
				OM.RemoveItem(tmp_monster);
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
				OM.RemoveItem(tmp_bullet);
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
				mGL20.ChangeScene(this, new SceneINGAME(mGL20));
		}
		//SetQuakeSize(20f, 2.0f);
		//setFadeState(1.0f);
		//doFadeIn(INTERVAL, 0.2f);
		//doFadeOut(INTERVAL, 0.2f);
		//doQuakeEffect(INTERVAL);
	}
	private void processTime(){
		/* ������ ���� �ð� ó��*/
		if(TM.EvnetOnTimer("TotalTimer"))
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
		GameObject tmp_particle = OM.newItem(
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
		SM.PlayBGMSound("deadtrag.mp3");
		useParticle(player.x, player.y, 3);
		player.bDeath = true;
		// ��� ���̴� ������
//		setShader(myGLRenderer.mBWObjectProgramHandle);
		setFadeState(0.0f);
	}
}
