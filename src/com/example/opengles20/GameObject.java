package com.example.opengles20;

import ius.TimeManager.Time;
import ius.Util;
import ius.Util.ius_Rect;
import ius.iusObject;
import android.content.Context;
import android.graphics.RectF;
/* GameObject Class
 * Function : ������ ������Ʈ�� �����ϰ� �׸��� Ŭ����
 */
public class GameObject extends iusObject{

	protected myGLSurfaceView GL20;
	public int ani_num;					// ���� animation�� Index�� ������
	public int spr_num;					// ���� animation�� sprite Index�� ������
	public Time time;					// GameObject�� �ð�ó���� �ϱ� ���� ����
	private String spr_name;
	private float width, height;
	private int spr_total_num;			// �ִϸ��̼��� ���� �� Sprite ������ ������
	private float animation_timer;		// �ִϸ��̼��� ���� framecount�� ������
	
	/* ���ӿ� ����� �� �ִ� ���� */
	public int iState;
	public int iPreState;
	public int iDirection;
	public float fSpeed;
	public float fLimit;
	public float fPower;
	public boolean bDeath;
	public boolean bJump;
	
	public GameObject(){
		super(myGLRenderer.mOutlineObjectProgramHandle);
	}
	public void SetGameObject(
			Context mContext, myGLSurfaceView mGL20,
			String textureName, int p_AN, int p_SN,
			float px, float py, 
			float pangle, float pscale ){

		spr_name = textureName;
		ani_num = p_AN;
		spr_num = p_SN;
		animation_timer = 0f;
		time = null;
		
		GL20 = mGL20;
		x = px; y = py;
		angle = pangle;
		scale = pscale;
		
		Util.Vector3f v3f = GL20.atlasManager.loadTextureSize(spr_name, ani_num);
		mTextureDataHandle = GL20.atlasManager.loadBitmap(spr_name);
		width = v3f.x; height = v3f.y; spr_total_num = v3f.iZ;
		textrueSize[0] = 1/width; textrueSize[1] = 1/height;
	}
	/* ���� Sprite�� Texture ũ��� ����� */
	public void Draw(){
		// �� �����Ӹ��� Sprite ������ ��� �׸�
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);
		float nW = pos.width()*0.5f*myGLRenderer.ssx, nH = pos.height()*0.5f*myGLRenderer.ssy;

		iusDraw(0f,
				nW, nH, 
				pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
				1.0f, 1.0f, 1.0f,
				myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, null);
	
	}
	/* �������̵� �Լ��� Sprite�� Texture�� �Ķ���� ������ ����� */
	public void Draw(float pWidth, float pHeight){
		//load texture pos
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);

		iusDraw(0f,
				pWidth*0.5f*myGLRenderer.ssx, pHeight*0.5f*myGLRenderer.ssy, 
				pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
				1.0f, 1.0f, 1.0f,
				myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, null);
	
	}
	/* �浹 ������ �˻��ϱ����� ���� Sprite�� �簢������ ���� */
	public ius_Rect Get_Rect(){
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);
		float nW = pos.width()*0.5f*scale, nH = pos.height()*0.5f*scale;
		
		return new ius_Rect(x-nW,y-nH,x+nW,y+nH);
	}
	public float Get_HalfWidth(){
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);
		float nW = pos.width()*0.5f*scale;
		return nW;
	}
	/* setPosition�� ���� �Լ� */
	public void setPosition(float X, float Y){
		x = X;
		y = Y;
	}
	/* translate�� ���� �Լ� */
	public void translate(float deltaX, float deltaY){
		x += deltaX;//*myGLRenderer.ssx;
		y += deltaY;//*myGLRenderer.ssy;
	}
	/* rotation�� ���� �Լ� */
	public void addAngle(float delta) {
		angle += delta;
		if (angle > 360f || angle < -360f) angle = 0f; 
	}
	/* animation�� ���� �Լ� */
	public boolean animation(float time, float delta){
		boolean IsFrameEnd = false;
		// �ִϸ��̼� Ÿ�̸Ӹ� ������Ŵ
		animation_timer += delta;
		// ���� �ִϸ��̼� Ÿ�̸Ӹ� �˻��Ͽ� �˸��� Sprite Number�� ����
		int spr_current_num = (int)(animation_timer*(float)spr_total_num/time);
		// ����  Sprite Number�� �� Sprite Number���� ũ�ٸ� 0���� ������
		if(spr_current_num >= spr_total_num){
			animation_timer = 0f; spr_current_num = 0; IsFrameEnd = true;
		}
		// ���� Sprite Number�� update��
		spr_num = spr_current_num;
		
		return IsFrameEnd;
	}
}
