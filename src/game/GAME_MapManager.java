package game;

import java.util.ArrayList;

import ius.Util;
import ius.Util.ius_Rect;
import android.content.Context;

import com.example.opengles20.GameObject;
import com.example.opengles20.myGLSurfaceView;
/* GAME_MapManager Class
 * Function : 'ġŸ��'������ �� ���� ����� �����ϴ� Ŭ����
 */
public class GAME_MapManager {

	int	 	width = 16;
	int		WIDTH = width+1;
	int 	height = 6;
	float	mapTile_width = 80f;
	float 	mapTile_height = 60f;

	float	mapTile_startXP;
	int 	current_left_bound;
	
	// �ܺ� ����
	ArrayList<GameObject>	mMonster;
	Context 				mContext;
	myGLSurfaceView			mGL20;
	/* mapTile ����
	 * -1 -> default (sky)
	 *
	 * stage_1 ��
	 * 6 x N ũ��� ������
	 * ������ Ÿ���� Spirte_num���� ������
	 * */
	int[]	stage1_mon = {0,2,5,4,0,4,0,5,0,5,2,5,2,4,2,4,0,5,4,4,5,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	int[][] stage1_tile= {
			{-1,-1,6,7,10,10},
			{-1,-1,-1,8,10,10},
			{-1,-1,6,7,10,10},
			{-1,-1,-1,8,10,10},
			{-1,-1,6,7,10,10},
			{-1,-1,-1,8,10,10},
			{-1,-1,6,7,10,10},
			{-1,-1,-1,8,10,10},
			{-1,-1,6,7,10,10},
			{-1,-1,-1,-1,10,10},
			{-1,-1,-1,-1,10,10},
			{-1,-1,-1,10,10,10},
			{-1,-1,10,10,10,10},
			{-1,-1,-1,10,10,10},
			{-1,-1,-1,10,10,10},
			{-1,-1,-1,-1,10,10},
			{-1,-1,-1,-1,10,10},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,5,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,5,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,5,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,8,10,10},
			{-1,-1,-1,8,10,10},
			{-1,-1,-1,8,10,10},
			{-1,-1,-1,8,10,10},
			{-1,-1,-1,8,10,10},
			{-1,-1,5,-1,10,10},
			{-1,-1,-1,-1,10,10},
			{-1,-1,5,-1,10,10},
			{-1,-1,-1,-1,10,10},
			{-1,-1,5,-1,10,10},
			{-1,-1,-1,-1,10,10},
			{-1,-1,-1,-1,10,10},
			{-1,-1,-1,-1,10,10},
			{-1,-1,-1,-1,10,10},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1}};
	
	GameObject[][] mapTile;
	public GAME_MapManager(Context pContext, myGLSurfaceView pGL20, ArrayList<GameObject> pMonster){
		mContext = pContext;
		mGL20 = pGL20;
		mMonster = pMonster;
		// TODO ���� < 2���� �迭 ������ �ٽ� Ȯ���ؾ��� >
		mapTile = new GameObject[WIDTH][height];
		mapTile_startXP = 0f;
		current_left_bound = 0;
		for(int i=0;i<WIDTH;i++)
			for(int j=0;j<height;j++){
				mapTile[i][j] = mGL20.objectmanager.newItem(
						mContext, mGL20,
						"spr_map_object", 0, 0,
						40f+80f*i, 30f+60f*(height-1-j),
						0f, 1.0f);
			}
		/* Tile Update*/
		Update();
	}
	/* Ÿ���� �׸��� �Լ� */
	public void Draw(){
		//call draw mapTile
		for(int i=0;i<WIDTH;i++)
			for(int j=0;j<height;j++)
				if(mapTile[i][j].spr_num != -1)	
					mapTile[i][j].Draw();
	}
	private void Update(){
		for(int i=current_left_bound;i<current_left_bound+WIDTH;i++)
			for(int j=0;j<height;j++){
				int u = i-current_left_bound;
				mapTile[u][j].bDeath = true;
				mapTile[u][j].spr_num = stage1_tile[i][j];
				if(mapTile[u][j].spr_num != -1){
					/* �浹�� �ʿ��� Ÿ�ϵ��� ���� */
					if(			mapTile[u][j].spr_num == 5
							|| 	mapTile[u][j].spr_num == 10
							|| 	mapTile[u][j].spr_num == 11)
						mapTile[u][j].bDeath = false;
				}
			}
		
		/* ���� ���� */
		int monsterN =stage1_mon[current_left_bound];
		switch(monsterN)
		{
			case 2:// �ذ�
				GameObject tmp_monster = mGL20.objectmanager.newItem(
						mContext, mGL20,
						"spr_cheetahmen_1", 2, 0,
						1300f, 195f,
						0f, 3.0f);
				tmp_monster.fSpeed = -100f;
				tmp_monster.bDeath = false;
				mMonster.add(tmp_monster);
				break;
			case 4:// ��
				tmp_monster = mGL20.objectmanager.newItem(
						mContext, mGL20,
						"spr_cheetahmen_1", 4, 0,
						1300f, 310f,
						0f, 3.0f);
				tmp_monster.fSpeed = -200f;
				tmp_monster.bDeath = false;
				mMonster.add(tmp_monster);
				break;
			case 5:// ������
				tmp_monster = mGL20.objectmanager.newItem(
						mContext, mGL20,
						"spr_cheetahmen_1", 5, 0,
						1300f, 155f,
						0f, 3.0f);
				tmp_monster.fSpeed = -150f;
				tmp_monster.bDeath = false;
				mMonster.add(tmp_monster);
				break;
			default:
			// Number 0 is default
				break;
		}
	}
	/* �÷��̾�� ����Ÿ���� �浹�� �˻��ϴ� �Լ� < �� �����Ӹ��� 1���� �Ҹ� > */
	public boolean IsCrash(GameObject player)
	{
		/* left, top, right, bottom*/
		boolean []CrashDirection = {false,false,false,false};
		/* detection CrashDirection*/		
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				if(!mapTile[i][j].bDeath){
					 float bWIndex = 2f, bHIndex = 0f, tWIndex = 0f, tHIndex = 0f;
					ius_Rect rect = Util.RectCrashObject2Object_B(player,bWIndex,bHIndex,
																	mapTile[i][j],tWIndex,tHIndex);
					if(rect != null){
						// CRASH!
						ius_Rect pRect = player.Get_Rect();
						float nInterW = rect.getWidth();
						float nInterH = rect.getHeight();
						if(nInterW > nInterH){
							if(rect.top == pRect.top + bHIndex){
								// �Ʒ��ʿ��� CRASH!
								player.translate(0f, rect.getHeight());
								CrashDirection[3] = true;
							}
							else if(rect.bottom == pRect.bottom - bHIndex){
								// ���ʿ��� CRASH!
								player.translate(0f, -rect.getHeight());
								CrashDirection[1] = true;
							}
						}
						else{
							if(rect.left == pRect.left + bWIndex){
								// ���ʿ��� CRASH!
								player.translate(rect.getWidth(), 0f);
								CrashDirection[0] = true;
							}
							else if(rect.right == pRect.right - bWIndex){
								// �����ʿ��� CRASH!
								player.translate(-rect.getWidth(), 0f);
								CrashDirection[2] = true;
							}
						}
					}
			}	
		return CrashDirection[3];
	}
	/* ���� ��ũ�Ѹ� �� ��, ���� �������� deltaX��ŭ �̵���Ű�� �Լ� */
	public void translateMap(float deltaX, float deltaY){
		float value;
		if(mapTile_startXP < -mapTile_width ){
			value = -mapTile_startXP;
			mapTile_startXP = 0f;
			++current_left_bound;
			/* Tile Update*/
			Update();
		}
		else{
			value = deltaX;
			mapTile_startXP += deltaX;
		}
		for(int i=0;i<WIDTH;i++)
			for(int j=0;j<height;j++)
				mapTile[i][j].translate(value, deltaY);
		for(int i=0; i<mMonster.size();i++){
			GameObject tmp_monster = mMonster.get(i);
			tmp_monster.translate(deltaX, deltaY);
		}
	}
}
