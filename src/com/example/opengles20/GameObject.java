package com.example.opengles20;

import ius.TimeManager.Time;
import ius.Util;
import ius.Util.ius_Rect;
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
	public Time time;					// GameObject의 시간처리를 하기 위한 변수
	private String spr_name;
	private float width, height;
	private int spr_total_num;			// 애니메이션을 위해 총 Sprite 갯수를 저장함
	private float animation_timer;		// 애니메이션을 위해 framecount를 저장함
	
	/* 게임에 사용할 수 있는 변수 */
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
	/* 현재 Sprite의 Texture 크기로 출력함 */
	public void Draw(){
		// 매 프레임마다 Sprite 정보를 얻어 그림
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);
		float nW = pos.width()*0.5f*myGLRenderer.ssx, nH = pos.height()*0.5f*myGLRenderer.ssy;

		iusDraw(0f,
				nW, nH, 
				pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
				1.0f, 1.0f, 1.0f,
				myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, null);
	
	}
	/* 오버라이드 함수로 Sprite의 Texture를 파라미터 값으로 출력함 */
	public void Draw(float pWidth, float pHeight){
		//load texture pos
		RectF pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);

		iusDraw(0f,
				pWidth*0.5f*myGLRenderer.ssx, pHeight*0.5f*myGLRenderer.ssy, 
				pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
				1.0f, 1.0f, 1.0f,
				myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, null);
	
	}
	/* 충돌 영역을 검사하기위해 현재 Sprite의 사각영역을 얻음 */
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
	/* setPosition을 위한 함수 */
	public void setPosition(float X, float Y){
		x = X;
		y = Y;
	}
	/* translate를 위한 함수 */
	public void translate(float deltaX, float deltaY){
		x += deltaX;//*myGLRenderer.ssx;
		y += deltaY;//*myGLRenderer.ssy;
	}
	/* rotation을 위한 함수 */
	public void addAngle(float delta) {
		angle += delta;
		if (angle > 360f || angle < -360f) angle = 0f; 
	}
	/* animation을 위한 함수 */
	public boolean animation(float time, float delta){
		boolean IsFrameEnd = false;
		// 애니메이션 타이머를 증가시킴
		animation_timer += delta;
		// 현재 애니메이션 타이머를 검사하여 알맞은 Sprite Number를 얻음
		int spr_current_num = (int)(animation_timer*(float)spr_total_num/time);
		// 현재  Sprite Number가 총 Sprite Number보다 크다면 0으로 세팅함
		if(spr_current_num >= spr_total_num){
			animation_timer = 0f; spr_current_num = 0; IsFrameEnd = true;
		}
		// 현재 Sprite Number를 update함
		spr_num = spr_current_num;
		
		return IsFrameEnd;
	}
}
