package ius;

import ius.Util.FFF;

import java.util.HashMap;

import com.example.opengles20.myGLRenderer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
/* FontManager Class
 * Function : ��Ʈ�� ����ϱ� ���� �����ϴ� Ŭ����
 */
public class FontManager {
	
	int mTextureHandle;			// ASCII�� Unicode ������ �ϳ��� Texture�� �ۼ��� TextureID�� ������ ����
	FontObejct fontObject;
	Context mContext;
	
	/* TableTexture Class
	 * Function : Texture������ ȿ�������� �����ϱ� ���� �ۼ��� Ŭ����
	 * �ϳ��� ���ڸ� �����ϰ� ��
	 */
	public class TableTexture{
		public float top,left,bottom,right ,width;
		public int type;
		public TableTexture(float x, float y, float pwidth, float pheight ,int ptype){
			top = y/(float)textureSize;
			left = x/(float)textureSize;
			bottom = (y+pheight)/(float)textureSize;
			right = (x+pwidth)/(float)textureSize;
			width = pwidth;
			type = ptype;
		}
	}
	
	private HashMap<Integer, TableTexture> FontsizeTable;
	private int textureSize;
	private float charHeight;
	// �ѱ� Unicode�� 11,000���� ����
	// euc-kr�� 2350���� ����
	private int KOREAN_LINE_LENGTH;
	// ASCII�� 90�� ������ ����
	private final int ASCII_START = 32;
	private final int ASCII_END = 126;
	private final int FONTSIZE = 32;
	// �������� �ʴ� ���ڴ� Ư������ '��'�� ǥ����
	private final int DEFAULT = 9633;
	
	Typeface mTF;
	
	@SuppressLint("UseSparseArrays")
	public FontManager(String fontName, Context context){
		// mTF�� ������� *.fft������ �а� ������
		mTF = Typeface.createFromAsset(context.getAssets(), fontName);
		FontsizeTable = new HashMap<Integer, TableTexture>();
		mContext = context;
	}
	public void FontLoad(){
		// �ѱ� 2350���� �����
		String hangle = getHangleFont();
		KOREAN_LINE_LENGTH = (int) Math.sqrt((hangle.length() + ASCII_END - ASCII_START + 2)*0.66) + 1;
		
		float fontHeight;
		float fontDescent;
		
		//�ӵ� ����� ���� RGBA�� 3���� ������ ������ FontMap�� ������
		// �� �κ��� Shader�� ������ �����Ƿ� Shader�� ������ �ʿ���
		Paint textPaint = new Paint();
		textPaint.setTextSize(FONTSIZE);//default size
		textPaint.setAntiAlias(true);
		textPaint.setTypeface(mTF);//font
		
		Paint.FontMetrics fm = textPaint.getFontMetrics();
		fontHeight = (float)Math.ceil(Math.abs(fm.bottom)+Math.abs(fm.top));
		fontDescent = (float)Math.ceil(Math.abs(fm.descent));
		
		float []charWidths = new float[hangle.length() + ASCII_END - ASCII_START + 2];
		float charWidthMax;
		charWidthMax = charHeight = 0;
		char[] s = new char[2];
		float[] w = new float[2];
		int cnt=0;
		
		for(int i=0;i<hangle.length();i++){
			s[0] = hangle.charAt(i);
			textPaint.getTextWidths(s, 0, 1, w);
			charWidths[cnt] = w[0];
			if(charWidths[cnt] > charWidthMax) charWidthMax = charWidths[cnt];
			++cnt;
		}
		for(int i=ASCII_START;i<=ASCII_END;i++){
			s[0] = (char)i;
			textPaint.getTextWidths(s, 0, 1, w);
			//if(w[0] < 5f)w[0] = 5f;//5f is default size
			charWidths[cnt] = w[0];
			//Log.d("FONT","c:"+s[0]+"/w:"+charWidths[cnt]);
			if(charWidths[cnt] > charWidthMax) charWidthMax = charWidths[cnt];
			++cnt;
		}
		s[0] = (char)DEFAULT;
		textPaint.getTextWidths(s, 0, 1, w);
		charWidths[cnt] = w[0];
		if(charWidths[cnt] > charWidthMax) charWidthMax = charWidths[cnt];
		++cnt;
		
		
		charHeight = fontHeight;
		int maxSize = (int)(charWidthMax > charHeight ? charWidthMax : charHeight); Log.d("FONT","maxSize :"+maxSize);
		textureSize = KOREAN_LINE_LENGTH*maxSize; Log.d("FONT","bitmap SIZE :"+textureSize+"/"+textureSize);
		
		Bitmap bitmap = Bitmap.createBitmap(textureSize, textureSize, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		bitmap.eraseColor(0);
		
		float x=0f,y= charHeight - fontDescent;
		final float MARGIN = y;
		int TYPE = 0;
		for(int i=0;i<hangle.length();i++){
			char C = hangle.charAt(i);
			s[0] = C;
			
			SetPaintColor(textPaint, TYPE);
			
			canvas.drawText(s, 0, 1, x, y, textPaint);
			FontsizeTable.put((int)C, new TableTexture(x, y-MARGIN, charWidths[i], charHeight, TYPE));
			
			if(TYPE == 2) x+=(float)maxSize;
			TYPE = ++TYPE % 3;
			
			if((int)x+maxSize > textureSize){
				x = 0f;
				y += (float)maxSize;
			}
		}
		for(int i=ASCII_START;i<=ASCII_END;i++){
			s[0] = (char)i;
			
			SetPaintColor(textPaint, TYPE);
			
			canvas.drawText(s, 0, 1, x, y, textPaint);
			FontsizeTable.put(i, new TableTexture(x, y-MARGIN, charWidths[hangle.length()+i-ASCII_START], charHeight, TYPE));

			if(TYPE == 2) x+=(float)maxSize;
			TYPE = ++TYPE % 3;
			
			if((int)x+maxSize > textureSize){
				x = 0f;
				y += (float)maxSize;
			}
		}
		s[0] = (char)DEFAULT;
		
		SetPaintColor(textPaint, TYPE);
		
		canvas.drawText(s, 0, 1, x, y, textPaint);
		FontsizeTable.put(DEFAULT, new TableTexture(x, y-MARGIN, charWidths[hangle.length()+(ASCII_END-ASCII_START+1)], charHeight, TYPE));
		
		final int[] textureHandle = new int[1];
		GLES20.glGenTextures(1, textureHandle, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		
		mTextureHandle = textureHandle[0];
		fontObject = new FontObejct(myGLRenderer.mFontProgramHandle, mTextureHandle);
	}
	public void FontBegin(){
		GLES20.glUseProgram(myGLRenderer.mFontProgramHandle);
	}
	public void FontEnd(){
		GLES20.glUseProgram(myGLRenderer.mObjectProgramHandle);
	}
	/* ���ڸ� ����ϴ� �Լ�
	 * ��� ������� Text�� �� ���ھ� ����� */
	public void draw(
			String text,
			float x, float y, boolean CenterAlign,
			int color,
			float scale){
		
		float indexXspace;
		if(CenterAlign==true){
			indexXspace = x-getTextSize(text,scale);//center
			y-=FONTSIZE*0.5f*scale;
		}
		else
			indexXspace = x;
		
		int len = text.length();
		for(int i=0;i<len;i++){
			char ch = text.charAt(i);
			TableTexture temp = FontsizeTable.get((int)(ch));
			if(temp == null)//don't have key
				temp = FontsizeTable.get(DEFAULT);
			
			fontObject.SetFontObject(indexXspace,y,color,temp,scale);
			fontObject.Draw();
			
			indexXspace += (temp.width)*scale;//*myGLRenderer.ssu;
		}
	}
	/* Text ������ ���� ��ü Text ũ�⸦ ��� �Լ� */
	public float getTextSize(String text, float scale){
		int len = text.length();
		int textSize = 0;
		for(int i=0;i<len;i++){
			char ch = text.charAt(i);
			TableTexture temp = FontsizeTable.get((int)(ch));
			if(temp == null)//don't have key
				temp = FontsizeTable.get(DEFAULT);
			textSize += temp.width;
		}
		return (float)textSize*scale*0.5f;
	}
	/* FontObject Class
	 * Function : Font ����� ���� Ŭ����
	 */
	public class FontObejct extends iusObject{
		FFF fColor;				// ���� ������ �����ϴ� ����
		TableTexture temp;		// Texture ��ǥ ������ �����ϴ� ����
		
		protected FontObejct(int mProgramHandle, int pTextureHandle) {
			super(mProgramHandle);
			mTextureDataHandle = pTextureHandle;
		}
		
		public void SetFontObject(
				float px, float py,
				int pColor,
				TableTexture ptemp,
				float pscale){
			x =px;y =py;
			fColor = new FFF(pColor);
			temp = ptemp;
			scale = pscale;
			mType = ptemp.type;
		}
		@Override
		public void Draw() {
			float nW = temp.width/2*myGLRenderer.ssx, nH = charHeight/2*myGLRenderer.ssy;
			// Font�� Left, Top �������� �׷��� < iusDraw�� ù��° Parameter�� 1f >
			iusDraw(1f,
					nW, nH,
					temp.left, temp.top, temp.right, temp.bottom,
					fColor.R, fColor.G, fColor.B,
					myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, myGLRenderer.mLightPosInEyeSpace);
		}
	}
	/* RGBA���� TYPE�� �´� ����� �����ϴ� �Լ� */
	private void SetPaintColor(Paint textPaint, int TYPE){
		switch(TYPE)
		{
		case 0:
			textPaint.setARGB(0x7f, 0xff, 0x00, 0x00);	// red
			break;
		case 1:
			textPaint.setARGB(0x7f, 0x00, 0xff, 0x00);	// green
			break;
		case 2:
			textPaint.setARGB(0x7f, 0x00, 0x00, 0xff);	// blue
			break;
		}
	}
	/* �ѱ� ��Ʈ ������ �д� �Լ� */
	private String getHangleFont(){
		String s 
			= "�������������������������������������������������������������������°ðİŰưǰȰɰʰ˰̰ͰΰϰаѰҰӰ԰հְװذٰڰ۰ܰݰް߰������������������������������������������������������������������������������������������������������������±ñıűƱǱȱɱʱ˱�";
		s  += "�ͱαϱбѱұӱԱձֱױرٱڱ۱ܱݱޱ߱������������������������������������������������������������������������������������������������������������²òĲŲƲǲȲɲʲ˲̲ͲβϲвѲҲӲԲղֲײزٲڲ۲ܲݲ޲߲�����������������������������";
		s  += "�������������������������������������������������������������������������������³óĳųƳǳȳɳʳ˳̳ͳγϳгѳҳӳԳճֳ׳سٳڳ۳ܳݳ޳߳������������������������������������������������������������������������������������������������������������´ôĴŴ�";
		s  += "�Ǵȴɴʴ˴̴ʹδϴдѴҴӴԴմִ״شٴڴ۴ܴݴ޴ߴ������������������������������������������������������������������������������������������������������������µõĵŵƵǵȵɵʵ˵̵͵εϵеѵҵӵԵյֵ׵صٵڵ۵ܵݵ޵ߵ�������������������";
		s  += "������������������������������������������������������������������������������������������¶öĶŶƶǶȶɶʶ˶̶Ͷζ϶жѶҶӶԶնֶ׶ضٶڶ۶ܶݶ޶߶���������������������������������������������������������������������������������������������������������";		
		s  += "���·÷ķŷƷǷȷɷʷ˷̷ͷηϷзѷҷӷԷշַ׷طٷڷ۷ܷݷ޷߷������������������������������������������������������������������������������������������������������������¸øĸŸƸǸȸɸʸ˸̸͸θϸиѸҸӸԸոָ׸ظٸڸ۸ܸݸ޸߸�������������";
		s  += "������������������������������������������������������������������������������������������������¹ùĹŹƹǹȹɹʹ˹̹͹ιϹйѹҹӹԹչֹ׹عٹڹ۹ܹݹ޹߹���������������������������������������������������������������������������������������������";
		s  += "���������������ºúĺźƺǺȺɺʺ˺̺ͺκϺкѺҺӺԺպֺ׺غٺںۺܺݺ޺ߺ������������������������������������������������������������������������������������������������������������»ûĻŻƻǻȻɻʻ˻̻ͻλϻлѻһӻԻջֻ׻ػٻڻۻܻݻ޻߻�������";
		s  += "������������������������������������������������������������������������������������������������������¼üļżƼǼȼɼʼ˼̼ͼμϼмѼҼӼԼռּ׼ؼټڼۼܼݼ޼߼���������������������������������������������������������������������������������";
		s  += "���������������������������½ýĽŽƽǽȽɽʽ˽̽ͽνϽнѽҽӽԽսֽ׽ؽٽڽ۽ܽݽ޽߽������������������������������������������������������������������������������������������������������������¾þľžƾǾȾɾʾ˾̾;ξϾоѾҾӾԾվ־׾ؾپھ۾ܾݾ޾߾�";
		s  += "������������������������������������������������������������������������������������������������������������¿ÿĿſƿǿȿɿʿ˿̿ͿοϿпѿҿӿԿտֿ׿ؿٿڿۿܿݿ޿߿���������������������������������������������������������������������";
		s  += "������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������";
		s  += "������������������������������������������������������������������������¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿������������������������������������������������������������������������������������������������������������������������������áâãäåæçè";
		s  += "éêëìíîïðñòóôõö÷øùúûüýþÿ������������������������������������������������������������������������������������������������������������������������������ġĢģĤĥĦħĨĩĪīĬĭĮįİıĲĳĴĵĶķĸĹĺĻļĽľĿ������������������������������������������";
		s  += "������������������������������������������������������������������������������������šŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽžſ������������������������������������������������������������������������������������������������������������������������������ơƢ";
		s  += "ƣƤƥƦƧƨƩƪƫƬƭƮƯưƱƲƳƴƵƶƷƸƹƺƻƼƽƾƿ������������������������������������������������������������������������������������������������������������������������������ǡǢǣǤǥǦǧǨǩǪǫǬǭǮǯǰǱǲǳǴǵǶǷǸǹǺǻǼǽǾǿ������������������������������";
		s  += "������������������������������������������������������������������������������������������������ȡȢȣȤȥȦȧȨȩȪȫȬȭȮȯȰȱȲȳȴȵȶȷȸȹȺȻȼȽȾȿ������������������������������������������������������������������������������������������������������������������������������";
		return s;
	}
}
