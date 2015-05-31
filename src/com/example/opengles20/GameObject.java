package com.example.opengles20;

import ius.Util.ius_Rect;
import ius.Vector3f;
import ius.iusObject;
import android.content.Context;
import android.graphics.RectF;
/* GameObject Class
 * Function : 게임의 오브젝트를 생성하고 그리는 클래스
 */
public class GameObject extends iusObject{

	protected myGLSurfaceView GL20;
	public int ani_num;					// 현재 animation의 Index를 저장함
	public int spr_num;					// 현재 animation의 sprite Index를 저장함
	private String spr_name;
	private float width, height;
	private int spr_total_num;			// 애니메이션을 위해 총 Sprite 갯수를 저장함
	private float animation_timer;		// 애니메이션을 위해 framecount를 저장함
	
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
	/* 현재 Sprite의 Texture 크기로 출력함 */
	public void Draw(){
		// 매 프레임마다 Sprite 정보를 얻어 그림
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);
		float nW = pos.width()/2*myGLRenderer.ssx, nH = pos.height()/2*myGLRenderer.ssy;

		iusDraw(0f,
				nW, nH, 
				pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
				1.0f, 1.0f, 1.0f,
				myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, myGLRenderer.mLightPosInEyeSpace);
	
	}
	/* 오버라이드 함수로 Sprite의 Texture를 파라미터 값으로 출력함 */
	public void Draw(float pWidth, float pHeight){
		//load texture pos
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);

		iusDraw(0f,
				pWidth, pHeight, 
				pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
				1.0f, 1.0f, 1.0f,
				myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, myGLRenderer.mLightPosInEyeSpace);
	
	}
	/* 충돌 영역을 검사하기위해 현재 Sprite의 사각영역을 얻음 */
	public ius_Rect Get_Rect(){
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);
		float nW = pos.width()*0.5f*scale*myGLRenderer.ssx, nH = pos.height()*0.5f*scale*myGLRenderer.ssy;
		
		return new ius_Rect(x-nW,y-nH,x+nW,y+nH);
	}
	/* translate를 위한 함수 */
	public void translate(float deltaX, float deltaY){
		x += deltaX*myGLRenderer.ssx;
		y += deltaY*myGLRenderer.ssy;
	}
	/* rotation을 위한 함수 */
	public void addAngle(float delta) {
		angle += delta;
		if (angle > 360f || angle < -360f) angle = 0f; 
	}
	/* animation을 위한 함수 */
	public void animation(float time, float delta){
		// 애니메이션 타이머를 증가시킴
		animation_timer += delta;
		// 현재 애니메이션 타이머를 검사하여 알맞은 Sprite Number를 얻음
		int spr_current_num = (int)(animation_timer*(float)spr_total_num/time);
		// 현재  Sprite Number가 총 Sprite Number보다 크다면 0으로 세팅함
		if(spr_current_num >= spr_total_num){
			animation_timer = 0f; spr_current_num = 0;
		}
		// 현재 Sprite Number를 update함
		spr_num = spr_current_num;
	}
}
