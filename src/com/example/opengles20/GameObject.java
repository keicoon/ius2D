package com.example.opengles20;

import ius.Util.ius_Rect;
import ius.Vector3f;
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
	private String spr_name;
	private float width, height;
	private int spr_total_num;			// �ִϸ��̼��� ���� �� Sprite ������ ������
	private float animation_timer;		// �ִϸ��̼��� ���� framecount�� ������
	
	public GameObject(){
		super(myGLRenderer.mObjectProgramHandle);
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
		
		GL20 = mGL20;
		x = px; y = py;
		angle = pangle;
		scale = pscale;
		
		Vector3f v3f = GL20.atlasManager.loadTextureSize(spr_name, ani_num);
		mTextureDataHandle = GL20.atlasManager.loadBitmap(spr_name);
		width = v3f.x; height = v3f.y; spr_total_num = v3f.iZ;
	}
	/* ���� Sprite�� Texture ũ��� ����� */
	public void Draw(){
		// �� �����Ӹ��� Sprite ������ ��� �׸�
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);
		float nW = pos.width()/2*myGLRenderer.ssx, nH = pos.height()/2*myGLRenderer.ssy;

		iusDraw(0f,
				nW, nH, 
				pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
				1.0f, 1.0f, 1.0f,
				myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, myGLRenderer.mLightPosInEyeSpace);
	
	}
	/* �������̵� �Լ��� Sprite�� Texture�� �Ķ���� ������ ����� */
	public void Draw(float pWidth, float pHeight){
		//load texture pos
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);

		iusDraw(0f,
				pWidth, pHeight, 
				pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
				1.0f, 1.0f, 1.0f,
				myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, myGLRenderer.mLightPosInEyeSpace);
	
	}
	/* �浹 ������ �˻��ϱ����� ���� Sprite�� �簢������ ���� */
	public ius_Rect Get_Rect(){
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);
		float nW = pos.width()*0.5f*scale*myGLRenderer.ssx, nH = pos.height()*0.5f*scale*myGLRenderer.ssy;
		
		return new ius_Rect(x-nW,y-nH,x+nW,y+nH);
	}
	/* translate�� ���� �Լ� */
	public void translate(float deltaX, float deltaY){
		x += deltaX*myGLRenderer.ssx;
		y += deltaY*myGLRenderer.ssy;
	}
	/* rotation�� ���� �Լ� */
	public void addAngle(float delta) {
		angle += delta;
		if (angle > 360f || angle < -360f) angle = 0f; 
	}
	/* animation�� ���� �Լ� */
	public void animation(float time, float delta){
		// �ִϸ��̼� Ÿ�̸Ӹ� ������Ŵ
		animation_timer += delta;
		// ���� �ִϸ��̼� Ÿ�̸Ӹ� �˻��Ͽ� �˸��� Sprite Number�� ����
		int spr_current_num = (int)(animation_timer*(float)spr_total_num/time);
		// ����  Sprite Number�� �� Sprite Number���� ũ�ٸ� 0���� ������
		if(spr_current_num >= spr_total_num){
			animation_timer = 0f; spr_current_num = 0;
		}
		// ���� Sprite Number�� update��
		spr_num = spr_current_num;
	}
}
