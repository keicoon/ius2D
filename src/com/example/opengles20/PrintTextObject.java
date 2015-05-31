package com.example.opengles20;

import ius.FontManager;
/* PrintTextObject Class
 * Function : ��ȭ�ϴ� ȿ���� Text�� ����ϴ� Ŭ���� 
 * */
public class PrintTextObject {
	private FontManager FM;
	
	private int text_INDEX = 0;		// ������� ��µ� text�� ��ġ
	private String myText;			// ����ؾ� �ϴ� Text�� ��
	private float x,y,cY;			// ����ؾ� �ϴ� Text�� ��ġ
	private int Color;				// ����ؾ� �ϴ� Text�� ����
	
	private boolean STOP = false;	// Text�� ���� ��»���
	private float stopTIME;			// ����ؾ� �ϴ� �ð�
	private float TIME;				// ���� �ð�
	
	public PrintTextObject(FontManager pFM, String pText,
			float pX, float pY,
			int pColor){
		FM = pFM;
		x = pX; y = pY; cY = y; Color = pColor;
		myText = pText;
		stopTIME = 0f; TIME = 0f;
	}
	/* Ư�� Char������ Text�� �����Ǿ� �ϰ� �׷� ȿ���� �ֱ� ���� ���� text�� ��ġ�� ����ִ� �Լ� */
	public boolean Update(float deltaTime, boolean KEY){
		TIME += deltaTime;
		// Ư�� ���ڸ� �����ϰ� �����ð��� ������
		switch (myText.charAt(text_INDEX)) {
		case ' ':
			stopTIME = 0.5f;
			break;
		case '\n':
			stopTIME = 1f;
			STOP = true;
			break;
		case '!':
		case '?':
		case '.':
			stopTIME = 0.5f;
			break;
		default:
			stopTIME = 0.2f;
			break;
		}
		// ���� ���¿� �ð��� ���Ͽ� ���¸� ������Ʈ��
		if (TIME > stopTIME && !STOP) {
			if(text_INDEX >= myText.length() - 1){
				STOP = true;
			}
			else{
				++text_INDEX;
				TIME = 0f;
			}
		}
		// ��� Text�� ����� �Ϸ���� �� �б⸦ ������Ʈ��
		if (KEY && STOP) {
			STOP = false;
			if(text_INDEX < myText.length() - 1){
				++text_INDEX;
				STOP = false;
			}
			else{
				STOP = true;
				return true;
			}
		}
		return false;
	}
	/* FontManager Class�� ����� Text�� ����� */
	public void Draw(){
		String currentText = myText.substring(0, text_INDEX);
		String Text[] = currentText.split("\n");

		cY = y;
		for(int i=0;i<Text.length;i++){
			FM.draw(Text[i], x, cY, false, Color, 1.0f);
			cY -= 40f;
		}
	}
}
