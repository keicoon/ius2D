package ius;

import com.example.opengles20.myGLRenderer;

import android.graphics.RectF;
/* ButtonObject Class
 * Function : ��ư������Ʈ�� �����ϴ� Ŭ���� < iusObject�� ��ӹ��� >
 */
public class ButtonObject extends iusObject {
	// �Ʒ� �������� Object�� �������� ����
	public int ani_num;
	public int spr_num;
	private String spr_name;
	private float width, height;
	private RectF pos;
	private float fontScale;
	// �Ʒ� �������� Button�� ���õ� ����
	public int id;					// ��ư�� ���� Id�� ��Ÿ��
	String text;					// ��ư�� text�� ������
	int Color;					// ��ư�� ������ ǥ����
	boolean option;					// ��ư�� ȿ���� �ֱ� ���� < ��� ������ �Լ� �Ǵ� �ѹ��� ������ �Լ� >
	public boolean active;			// ��ư�� ������ ���� ǥ���ϱ� ����
	FontManager FM;
	public ButtonObject(
			String textureName, int p_AN, int p_SN, float px, float py,
			float pangle, float pscale,
			int pid, String ptext, int pColor, float pfscale) {
		super(myGLRenderer.mObjectProgramHandle);

		spr_name = textureName;
		ani_num = p_AN;
		spr_num = p_SN;

		x = px;
		y = py;
		angle = pangle;
		scale = pscale;
		text = ptext;
		Color = pColor;
		id = pid;
		
		AtlasManager AM = AtlasManager.getInstance();
		Util.Vector3f v3f = AM.loadTextureSize(spr_name, ani_num);
		mTextureDataHandle = AM.loadBitmap(spr_name);
		width = v3f.x;
		height = v3f.y;
		
		// pos�� �����ϴ� ������ �Ϲ����� ��ư�� ��� ��ȭ�Ǵ� ��ư Sprite�� ũ�Ⱑ �����̱� ������
		// ��Ȳ�� ���� pos�� draw()���� �޾� �� ������ �ٸ� ��Ȳ�� ������ �� ����
		pos = AM.loadTexturePOS(spr_name, ani_num, spr_num);
		fontScale = pfscale;
		
		option = false;			// option�� false�� default��
		active = false;
		FM = FontManager.getInstance();
	}
	/* ��ư�� ���ȴ��� �����ϴ� �Լ��� tx, ty�� �˻��� */
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
		
		if(active)	// ���� ��� ������ �ʷϻ����� ���
			iusDraw(0f,
					nW, nH, 
					pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
					0.0f, 1.0f, 0.0f);
		else		// ������ ���� ��� ������ �Ͼ������ ���
			iusDraw(0f,
					nW, nH, 
					pos.left/width, pos.top/height, pos.right/width, pos.bottom/height, 
					1.0f, 1.0f, 1.0f);
	}
	public void DrawFont(){
		FM.draw(
				text,
				x,y,true,
				Color,
				fontScale);
	}
	public void reset(){
		active = false;
	}
}
