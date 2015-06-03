package com.example.opengles20;

import android.content.Context;
import android.graphics.RectF;
import ius.Util;
import ius.iusObject;
/* ButtonObject Class
 * Function : 버튼오브젝트를 정의하는 클래스 < iusObject를 상속받음 >
 */
public class ButtonObject extends iusObject {
	// 아래 변수들은 Object의 공통적인 변수
	protected myGLSurfaceView GL20;
	public int ani_num;
	public int spr_num;
	private String spr_name;
	private float width, height;
	private RectF pos;
	private float fontScale;
	// 아래 변수들은 Button과 관련된 변수
	public int id;					// 버튼의 고유 Id를 나타냄
	String text;					// 버튼의 text를 저장함
	int Color;					// 버튼의 색상을 표현함
	boolean option;					// 버튼에 효과를 넣기 위함 < 계속 눌리는 함수 또는 한번만 눌리는 함수 >
	public boolean active;			// 버튼이 눌렸을 때를 표현하기 위함

	public ButtonObject(Context mContext, myGLSurfaceView mGL20,
			String textureName, int p_AN, int p_SN, float px, float py,
			float pangle, float pscale,
			int pid, String ptext, int pColor, float pfscale) {
		super(myGLRenderer.mObjectProgramHandle);

		spr_name = textureName;
		ani_num = p_AN;
		spr_num = p_SN;

		GL20 = mGL20;
		x = px;
		y = py;
		angle = pangle;
		scale = pscale;
		text = ptext;
		Color = pColor;
		id = pid;
		
		Util.Vector3f v3f = GL20.atlasManager.loadTextureSize(spr_name, ani_num);
		mTextureDataHandle = GL20.atlasManager.loadBitmap(spr_name);
		width = v3f.x;
		height = v3f.y;
		
		// pos를 저장하는 이유는 일반적인 버튼인 경우 변화되는 버튼 Sprite의 크기가 고정이기 떄문임
		// 상황에 따라 pos를 draw()에서 받아 매 프레임 다른 상황을 적용할 수 있음
		pos = GL20.atlasManager.loadTexturePOS(spr_name, ani_num, spr_num);
		fontScale = pfscale;
		
		option = false;			// option은 false가 default임
		active = false;
	}
	/* 버튼이 눌렸는지 학인하는 함수로 tx, ty로 검사함 */
	public void OnEvent(boolean type, float tx, float ty) {
		float nW = pos.width()*0.5f*scale*myGLRenderer.ssx, nH = pos.height()*0.5f*scale*myGLRenderer.ssy;
		if (	x - nW <= tx 
				&& x + nW >= tx 
				&& y - nH <= ty
				&& y + nH >= ty )
			active = type;
	}
	@Override
	public void Draw() {
		float nW = pos.width()*0.5f*myGLRenderer.ssx, nH = pos.height()*0.5f*myGLRenderer.ssy;
		
		if(active)	// 눌린 경우 색상을 초록색으로 출력
			iusDraw(0f,
					nW, nH, 
					pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
					0.0f, 1.0f, 0.0f,
					myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, null);
		else		// 눌리지 않은 경우 색상을 하얀색으로 출력
			iusDraw(0f,
					nW, nH, 
					pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
					1.0f, 1.0f, 1.0f,
					myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, null);
	}
	public void DrawFont(){
		GL20.fontManager.draw(
				text,
				x,y,true,
				Color,
				fontScale);
	}
	public void reset(){
		active = false;
	}
}
