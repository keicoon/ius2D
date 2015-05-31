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
 * Function : ÆùÆ®¸¦ Ãâ·ÂÇÏ±â À§ÇØ °ü¸®ÇÏ´Â Å¬·¡½º
 */
public class FontManager {
	
	int mTextureHandle;			// ASCII¿Í Unicode Á¤º¸¸¦ ÇÏ³ªÀÇ Texture¿¡ ÀÛ¼ºÇÑ TextureID¸¦ ÀúÀåÇÑ º¯¼ö
	FontObejct fontObject;
	Context mContext;
	
	/* TableTexture Class
	 * Function : TextureÁ¤º¸¸¦ È¿À²ÀûÀ¸·Î °ü¸®ÇÏ±â À§ÇØ ÀÛ¼ºµÈ Å¬·¡½º
	 * ÇÏ³ªÀÇ ±ÛÀÚ¸¦ ÀúÀåÇÏ°Ô µÊ
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
	// ÇÑ±Û Unicode´Â 11,000°³¸¦ °¡Áü
	// euc-krÀº 2350°³¸¦ °¡Áü
	private int KOREAN_LINE_LENGTH;
	// ASCII´Â 90°³ Á¤µµ¸¦ °¡Áü
	private final int ASCII_START = 32;
	private final int ASCII_END = 126;
	private final int FONTSIZE = 32;
	// Áö¿øÇÏÁö ¾Ê´Â ±ÛÀÚ´Â Æ¯¼ö¹®ÀÚ '¤±'·Î Ç¥ÇöÇÔ
	private final int DEFAULT = 9633;
	
	Typeface mTF;
	
	@SuppressLint("UseSparseArrays")
	public FontManager(String fontName, Context context){
		// mTF´Â »ç¿ëÀÚÀÇ *.fftÆÄÀÏÀ» ÀĞ°í Àû¿ëÇÔ
		mTF = Typeface.createFromAsset(context.getAssets(), fontName);
		FontsizeTable = new HashMap<Integer, TableTexture>();
		mContext = context;
	}
	public void FontLoad(){
		// ÇÑ±Û 2350°³¸¸ »ç¿ëÇÔ
		String hangle = getHangleFont();
		KOREAN_LINE_LENGTH = (int) Math.sqrt((hangle.length() + ASCII_END - ASCII_START + 2)*0.66) + 1;
		
		float fontHeight;
		float fontDescent;
		
		//¼Óµµ Çâ»óÀ» À§ÇØ RGBAÀÇ 3°¡Áö »ö»óÀ» Àû¿ëÇÑ FontMapÀ» ¸¸µéÀ½
		// ÀÌ ºÎºĞÀº Shader¿Í °ü·ÃÀÌ ÀÖÀ¸¹Ç·Î Shaderµµ ¼öÁ¤ÀÌ ÇÊ¿äÇÔ
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
	/* ±ÛÀÚ¸¦ Ãâ·ÂÇÏ´Â ÇÔ¼ö
	 * Ãâ·Â ¹æ¹ıÀ¸·Î Text¸¦ ÇÑ ±ÛÀÚ¾¿ Ãâ·ÂÇÔ */
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
	/* Text Á¤·ÄÀ» À§ÇØ ÀüÃ¼ Text Å©±â¸¦ ¾ò´Â ÇÔ¼ö */
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
	 * Function : Font Ãâ·ÂÀ» À§ÇÑ Å¬·¡½º
	 */
	public class FontObejct extends iusObject{
		FFF fColor;				// »ö»ó Á¤º¸¸¦ ÀúÀåÇÏ´Â º¯¼ö
		TableTexture temp;		// Texture ÁÂÇ¥ Á¤º¸¸¦ ÀúÀåÇÏ´Â º¯¼ö
		
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
			// Font´Â Left, Top ±âÁØÀ¸·Î ±×·ÁÁü < iusDrawÀÇ Ã¹¹øÂ° Parameter°¡ 1f >
			iusDraw(1f,
					nW, nH,
					temp.left, temp.top, temp.right, temp.bottom,
					fColor.R, fColor.G, fColor.B,
					myGLRenderer.mViewMatrix, myGLRenderer.mProjectionMatrix, myGLRenderer.mLightPosInEyeSpace);
		}
	}
	/* RGBA¿¡¼­ TYPE¿¡ ¸Â´Â °á°ú¸¦ ¼¼ÆÃÇÏ´Â ÇÔ¼ö */
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
	/* ÇÑ±Û Æ÷Æ® Á¤º¸¸¦ ÀĞ´Â ÇÔ¼ö */
	private String getHangleFont(){
		String s 
			= "°¡°¢°£°¤°¥°¦°§°¨°©°ª°«°¬°­°®°¯°°°±°²°³°´°µ°¶°·°¸°¹°º°»°¼°½°¾°¿°À°Á°Â°Ã°Ä°Å°Æ°Ç°È°É°Ê°Ë°Ì°Í°Î°Ï°Ğ°Ñ°Ò°Ó°Ô°Õ°Ö°×°Ø°Ù°Ú°Û°Ü°İ°Ş°ß°à°á°â°ã°ä°å°æ°ç°è°é°ê°ë°ì°í°î°ï°ğ°ñ°ò°ó°ô°õ°ö°÷°ø°ù°ú°û°ü°ı°ş±¡±¢±£±¤±¥±¦±§±¨±©±ª±«±¬±­±®±¯±°±±±²±³±´±µ±¶±·±¸±¹±º±»±¼±½±¾±¿±À±Á±Â±Ã±Ä±Å±Æ±Ç±È±É±Ê±Ë±Ì";
		s  += "±Í±Î±Ï±Ğ±Ñ±Ò±Ó±Ô±Õ±Ö±×±Ø±Ù±Ú±Û±Ü±İ±Ş±ß±à±á±â±ã±ä±å±æ±ç±è±é±ê±ë±ì±í±î±ï±ğ±ñ±ò±ó±ô±õ±ö±÷±ø±ù±ú±û±ü±ı±ş²¡²¢²£²¤²¥²¦²§²¨²©²ª²«²¬²­²®²¯²°²±²²²³²´²µ²¶²·²¸²¹²º²»²¼²½²¾²¿²À²Á²Â²Ã²Ä²Å²Æ²Ç²È²É²Ê²Ë²Ì²Í²Î²Ï²Ğ²Ñ²Ò²Ó²Ô²Õ²Ö²×²Ø²Ù²Ú²Û²Ü²İ²Ş²ß²à²á²â²ã²ä²å²æ²ç²è²é²ê²ë²ì²í²î²ï²ğ²ñ²ò²ó²ô²õ²ö²÷²ø";
		s  += "²ù²ú²û²ü²ı²ş³¡³¢³£³¤³¥³¦³§³¨³©³ª³«³¬³­³®³¯³°³±³²³³³´³µ³¶³·³¸³¹³º³»³¼³½³¾³¿³À³Á³Â³Ã³Ä³Å³Æ³Ç³È³É³Ê³Ë³Ì³Í³Î³Ï³Ğ³Ñ³Ò³Ó³Ô³Õ³Ö³×³Ø³Ù³Ú³Û³Ü³İ³Ş³ß³à³á³â³ã³ä³å³æ³ç³è³é³ê³ë³ì³í³î³ï³ğ³ñ³ò³ó³ô³õ³ö³÷³ø³ù³ú³û³ü³ı³ş´¡´¢´£´¤´¥´¦´§´¨´©´ª´«´¬´­´®´¯´°´±´²´³´´´µ´¶´·´¸´¹´º´»´¼´½´¾´¿´À´Á´Â´Ã´Ä´Å´Æ";
		s  += "´Ç´È´É´Ê´Ë´Ì´Í´Î´Ï´Ğ´Ñ´Ò´Ó´Ô´Õ´Ö´×´Ø´Ù´Ú´Û´Ü´İ´Ş´ß´à´á´â´ã´ä´å´æ´ç´è´é´ê´ë´ì´í´î´ï´ğ´ñ´ò´ó´ô´õ´ö´÷´ø´ù´ú´û´ü´ı´şµ¡µ¢µ£µ¤µ¥µ¦µ§µ¨µ©µªµ«µ¬µ­µ®µ¯µ°µ±µ²µ³µ´µµµ¶µ·µ¸µ¹µºµ»µ¼µ½µ¾µ¿µÀµÁµÂµÃµÄµÅµÆµÇµÈµÉµÊµËµÌµÍµÎµÏµĞµÑµÒµÓµÔµÕµÖµ×µØµÙµÚµÛµÜµİµŞµßµàµáµâµãµäµåµæµçµèµéµêµëµìµíµîµïµğµñµò";
		s  += "µóµôµõµöµ÷µøµùµúµûµüµıµş¶¡¶¢¶£¶¤¶¥¶¦¶§¶¨¶©¶ª¶«¶¬¶­¶®¶¯¶°¶±¶²¶³¶´¶µ¶¶¶·¶¸¶¹¶º¶»¶¼¶½¶¾¶¿¶À¶Á¶Â¶Ã¶Ä¶Å¶Æ¶Ç¶È¶É¶Ê¶Ë¶Ì¶Í¶Î¶Ï¶Ğ¶Ñ¶Ò¶Ó¶Ô¶Õ¶Ö¶×¶Ø¶Ù¶Ú¶Û¶Ü¶İ¶Ş¶ß¶à¶á¶â¶ã¶ä¶å¶æ¶ç¶è¶é¶ê¶ë¶ì¶í¶î¶ï¶ğ¶ñ¶ò¶ó¶ô¶õ¶ö¶÷¶ø¶ù¶ú¶û¶ü¶ı¶ş·¡·¢·£·¤·¥·¦·§·¨·©·ª·«·¬·­·®·¯·°·±·²·³·´·µ·¶···¸·¹·º·»·¼·½·¾·¿·À";		
		s  += "·Á·Â·Ã·Ä·Å·Æ·Ç·È·É·Ê·Ë·Ì·Í·Î·Ï·Ğ·Ñ·Ò·Ó·Ô·Õ·Ö·×·Ø·Ù·Ú·Û·Ü·İ·Ş·ß·à·á·â·ã·ä·å·æ·ç·è·é·ê·ë·ì·í·î·ï·ğ·ñ·ò·ó·ô·õ·ö·÷·ø·ù·ú·û·ü·ı·ş¸¡¸¢¸£¸¤¸¥¸¦¸§¸¨¸©¸ª¸«¸¬¸­¸®¸¯¸°¸±¸²¸³¸´¸µ¸¶¸·¸¸¸¹¸º¸»¸¼¸½¸¾¸¿¸À¸Á¸Â¸Ã¸Ä¸Å¸Æ¸Ç¸È¸É¸Ê¸Ë¸Ì¸Í¸Î¸Ï¸Ğ¸Ñ¸Ò¸Ó¸Ô¸Õ¸Ö¸×¸Ø¸Ù¸Ú¸Û¸Ü¸İ¸Ş¸ß¸à¸á¸â¸ã¸ä¸å¸æ¸ç¸è¸é¸ê¸ë¸ì";
		s  += "¸í¸î¸ï¸ğ¸ñ¸ò¸ó¸ô¸õ¸ö¸÷¸ø¸ù¸ú¸û¸ü¸ı¸ş¹¡¹¢¹£¹¤¹¥¹¦¹§¹¨¹©¹ª¹«¹¬¹­¹®¹¯¹°¹±¹²¹³¹´¹µ¹¶¹·¹¸¹¹¹º¹»¹¼¹½¹¾¹¿¹À¹Á¹Â¹Ã¹Ä¹Å¹Æ¹Ç¹È¹É¹Ê¹Ë¹Ì¹Í¹Î¹Ï¹Ğ¹Ñ¹Ò¹Ó¹Ô¹Õ¹Ö¹×¹Ø¹Ù¹Ú¹Û¹Ü¹İ¹Ş¹ß¹à¹á¹â¹ã¹ä¹å¹æ¹ç¹è¹é¹ê¹ë¹ì¹í¹î¹ï¹ğ¹ñ¹ò¹ó¹ô¹õ¹ö¹÷¹ø¹ù¹ú¹û¹ü¹ı¹şº¡º¢º£º¤º¥º¦º§º¨º©ºªº«º¬º­º®º¯º°º±º²º³º´ºµº¶º·º¸º¹ºº";
		s  += "º»º¼º½º¾º¿ºÀºÁºÂºÃºÄºÅºÆºÇºÈºÉºÊºËºÌºÍºÎºÏºĞºÑºÒºÓºÔºÕºÖº×ºØºÙºÚºÛºÜºİºŞºßºàºáºâºãºäºåºæºçºèºéºêºëºìºíºîºïºğºñºòºóºôºõºöº÷ºøºùºúºûºüºıºş»¡»¢»£»¤»¥»¦»§»¨»©»ª»«»¬»­»®»¯»°»±»²»³»´»µ»¶»·»¸»¹»º»»»¼»½»¾»¿»À»Á»Â»Ã»Ä»Å»Æ»Ç»È»É»Ê»Ë»Ì»Í»Î»Ï»Ğ»Ñ»Ò»Ó»Ô»Õ»Ö»×»Ø»Ù»Ú»Û»Ü»İ»Ş»ß»à»á»â»ã»ä»å»æ";
		s  += "»ç»è»é»ê»ë»ì»í»î»ï»ğ»ñ»ò»ó»ô»õ»ö»÷»ø»ù»ú»û»ü»ı»ş¼¡¼¢¼£¼¤¼¥¼¦¼§¼¨¼©¼ª¼«¼¬¼­¼®¼¯¼°¼±¼²¼³¼´¼µ¼¶¼·¼¸¼¹¼º¼»¼¼¼½¼¾¼¿¼À¼Á¼Â¼Ã¼Ä¼Å¼Æ¼Ç¼È¼É¼Ê¼Ë¼Ì¼Í¼Î¼Ï¼Ğ¼Ñ¼Ò¼Ó¼Ô¼Õ¼Ö¼×¼Ø¼Ù¼Ú¼Û¼Ü¼İ¼Ş¼ß¼à¼á¼â¼ã¼ä¼å¼æ¼ç¼è¼é¼ê¼ë¼ì¼í¼î¼ï¼ğ¼ñ¼ò¼ó¼ô¼õ¼ö¼÷¼ø¼ù¼ú¼û¼ü¼ı¼ş½¡½¢½£½¤½¥½¦½§½¨½©½ª½«½¬½­½®½¯½°½±½²½³½´";
		s  += "½µ½¶½·½¸½¹½º½»½¼½½½¾½¿½À½Á½Â½Ã½Ä½Å½Æ½Ç½È½É½Ê½Ë½Ì½Í½Î½Ï½Ğ½Ñ½Ò½Ó½Ô½Õ½Ö½×½Ø½Ù½Ú½Û½Ü½İ½Ş½ß½à½á½â½ã½ä½å½æ½ç½è½é½ê½ë½ì½í½î½ï½ğ½ñ½ò½ó½ô½õ½ö½÷½ø½ù½ú½û½ü½ı½ş¾¡¾¢¾£¾¤¾¥¾¦¾§¾¨¾©¾ª¾«¾¬¾­¾®¾¯¾°¾±¾²¾³¾´¾µ¾¶¾·¾¸¾¹¾º¾»¾¼¾½¾¾¾¿¾À¾Á¾Â¾Ã¾Ä¾Å¾Æ¾Ç¾È¾É¾Ê¾Ë¾Ì¾Í¾Î¾Ï¾Ğ¾Ñ¾Ò¾Ó¾Ô¾Õ¾Ö¾×¾Ø¾Ù¾Ú¾Û¾Ü¾İ¾Ş¾ß¾à";
		s  += "¾á¾â¾ã¾ä¾å¾æ¾ç¾è¾é¾ê¾ë¾ì¾í¾î¾ï¾ğ¾ñ¾ò¾ó¾ô¾õ¾ö¾÷¾ø¾ù¾ú¾û¾ü¾ı¾ş¿¡¿¢¿£¿¤¿¥¿¦¿§¿¨¿©¿ª¿«¿¬¿­¿®¿¯¿°¿±¿²¿³¿´¿µ¿¶¿·¿¸¿¹¿º¿»¿¼¿½¿¾¿¿¿À¿Á¿Â¿Ã¿Ä¿Å¿Æ¿Ç¿È¿É¿Ê¿Ë¿Ì¿Í¿Î¿Ï¿Ğ¿Ñ¿Ò¿Ó¿Ô¿Õ¿Ö¿×¿Ø¿Ù¿Ú¿Û¿Ü¿İ¿Ş¿ß¿à¿á¿â¿ã¿ä¿å¿æ¿ç¿è¿é¿ê¿ë¿ì¿í¿î¿ï¿ğ¿ñ¿ò¿ó¿ô¿õ¿ö¿÷¿ø¿ù¿ú¿û¿ü¿ı¿şÀ¡À¢À£À¤À¥À¦À§À¨À©ÀªÀ«À¬À­À®";
		s  += "À¯À°À±À²À³À´ÀµÀ¶À·À¸À¹ÀºÀ»À¼À½À¾À¿ÀÀÀÁÀÂÀÃÀÄÀÅÀÆÀÇÀÈÀÉÀÊÀËÀÌÀÍÀÎÀÏÀĞÀÑÀÒÀÓÀÔÀÕÀÖÀ×ÀØÀÙÀÚÀÛÀÜÀİÀŞÀßÀàÀáÀâÀãÀäÀåÀæÀçÀèÀéÀêÀëÀìÀíÀîÀïÀğÀñÀòÀóÀôÀõÀöÀ÷ÀøÀùÀúÀûÀüÀıÀşÁ¡Á¢Á£Á¤Á¥Á¦Á§Á¨Á©ÁªÁ«Á¬Á­Á®Á¯Á°Á±Á²Á³Á´ÁµÁ¶Á·Á¸Á¹ÁºÁ»Á¼Á½Á¾Á¿ÁÀÁÁÁÂÁÃÁÄÁÅÁÆÁÇÁÈÁÉÁÊÁËÁÌÁÍÁÎÁÏÁĞÁÑÁÒÁÓÁÔÁÕÁÖÁ×ÁØÁÙÁÚ";
		s  += "ÁÛÁÜÁİÁŞÁßÁàÁáÁâÁãÁäÁåÁæÁçÁèÁéÁêÁëÁìÁíÁîÁïÁğÁñÁòÁóÁôÁõÁöÁ÷ÁøÁùÁúÁûÁüÁıÁşÂ¡Â¢Â£Â¤Â¥Â¦Â§Â¨Â©ÂªÂ«Â¬Â­Â®Â¯Â°Â±Â²Â³Â´ÂµÂ¶Â·Â¸Â¹ÂºÂ»Â¼Â½Â¾Â¿ÂÀÂÁÂÂÂÃÂÄÂÅÂÆÂÇÂÈÂÉÂÊÂËÂÌÂÍÂÎÂÏÂĞÂÑÂÒÂÓÂÔÂÕÂÖÂ×ÂØÂÙÂÚÂÛÂÜÂİÂŞÂßÂàÂáÂâÂãÂäÂåÂæÂçÂèÂéÂêÂëÂìÂíÂîÂïÂğÂñÂòÂóÂôÂõÂöÂ÷ÂøÂùÂúÂûÂüÂıÂşÃ¡Ã¢Ã£Ã¤Ã¥Ã¦Ã§Ã¨";
		s  += "Ã©ÃªÃ«Ã¬Ã­Ã®Ã¯Ã°Ã±Ã²Ã³Ã´ÃµÃ¶Ã·Ã¸Ã¹ÃºÃ»Ã¼Ã½Ã¾Ã¿ÃÀÃÁÃÂÃÃÃÄÃÅÃÆÃÇÃÈÃÉÃÊÃËÃÌÃÍÃÎÃÏÃĞÃÑÃÒÃÓÃÔÃÕÃÖÃ×ÃØÃÙÃÚÃÛÃÜÃİÃŞÃßÃàÃáÃâÃãÃäÃåÃæÃçÃèÃéÃêÃëÃìÃíÃîÃïÃğÃñÃòÃóÃôÃõÃöÃ÷ÃøÃùÃúÃûÃüÃıÃşÄ¡Ä¢Ä£Ä¤Ä¥Ä¦Ä§Ä¨Ä©ÄªÄ«Ä¬Ä­Ä®Ä¯Ä°Ä±Ä²Ä³Ä´ÄµÄ¶Ä·Ä¸Ä¹ÄºÄ»Ä¼Ä½Ä¾Ä¿ÄÀÄÁÄÂÄÃÄÄÄÅÄÆÄÇÄÈÄÉÄÊÄËÄÌÄÍÄÎÄÏÄĞÄÑÄÒÄÓÄÔ";
		s  += "ÄÕÄÖÄ×ÄØÄÙÄÚÄÛÄÜÄİÄŞÄßÄàÄáÄâÄãÄäÄåÄæÄçÄèÄéÄêÄëÄìÄíÄîÄïÄğÄñÄòÄóÄôÄõÄöÄ÷ÄøÄùÄúÄûÄüÄıÄşÅ¡Å¢Å£Å¤Å¥Å¦Å§Å¨Å©ÅªÅ«Å¬Å­Å®Å¯Å°Å±Å²Å³Å´ÅµÅ¶Å·Å¸Å¹ÅºÅ»Å¼Å½Å¾Å¿ÅÀÅÁÅÂÅÃÅÄÅÅÅÆÅÇÅÈÅÉÅÊÅËÅÌÅÍÅÎÅÏÅĞÅÑÅÒÅÓÅÔÅÕÅÖÅ×ÅØÅÙÅÚÅÛÅÜÅİÅŞÅßÅàÅáÅâÅãÅäÅåÅæÅçÅèÅéÅêÅëÅìÅíÅîÅïÅğÅñÅòÅóÅôÅõÅöÅ÷ÅøÅùÅúÅûÅüÅıÅşÆ¡Æ¢";
		s  += "Æ£Æ¤Æ¥Æ¦Æ§Æ¨Æ©ÆªÆ«Æ¬Æ­Æ®Æ¯Æ°Æ±Æ²Æ³Æ´ÆµÆ¶Æ·Æ¸Æ¹ÆºÆ»Æ¼Æ½Æ¾Æ¿ÆÀÆÁÆÂÆÃÆÄÆÅÆÆÆÇÆÈÆÉÆÊÆËÆÌÆÍÆÎÆÏÆĞÆÑÆÒÆÓÆÔÆÕÆÖÆ×ÆØÆÙÆÚÆÛÆÜÆİÆŞÆßÆàÆáÆâÆãÆäÆåÆæÆçÆèÆéÆêÆëÆìÆíÆîÆïÆğÆñÆòÆóÆôÆõÆöÆ÷ÆøÆùÆúÆûÆüÆıÆşÇ¡Ç¢Ç£Ç¤Ç¥Ç¦Ç§Ç¨Ç©ÇªÇ«Ç¬Ç­Ç®Ç¯Ç°Ç±Ç²Ç³Ç´ÇµÇ¶Ç·Ç¸Ç¹ÇºÇ»Ç¼Ç½Ç¾Ç¿ÇÀÇÁÇÂÇÃÇÄÇÅÇÆÇÇÇÈÇÉÇÊÇËÇÌÇÍÇÎ";
		s  += "ÇÏÇĞÇÑÇÒÇÓÇÔÇÕÇÖÇ×ÇØÇÙÇÚÇÛÇÜÇİÇŞÇßÇàÇáÇâÇãÇäÇåÇæÇçÇèÇéÇêÇëÇìÇíÇîÇïÇğÇñÇòÇóÇôÇõÇöÇ÷ÇøÇùÇúÇûÇüÇıÇşÈ¡È¢È£È¤È¥È¦È§È¨È©ÈªÈ«È¬È­È®È¯È°È±È²È³È´ÈµÈ¶È·È¸È¹ÈºÈ»È¼È½È¾È¿ÈÀÈÁÈÂÈÃÈÄÈÅÈÆÈÇÈÈÈÉÈÊÈËÈÌÈÍÈÎÈÏÈĞÈÑÈÒÈÓÈÔÈÕÈÖÈ×ÈØÈÙÈÚÈÛÈÜÈİÈŞÈßÈàÈáÈâÈãÈäÈåÈæÈçÈèÈéÈêÈëÈìÈíÈîÈïÈğÈñÈòÈóÈôÈõÈöÈ÷ÈøÈùÈúÈûÈüÈıÈş";
		return s;
	}
}
