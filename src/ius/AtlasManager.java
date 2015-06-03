package ius;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import common.TextureHelper;
import android.content.Context;
import android.graphics.RectF;

/* AtlasManager Class
 * Function : 2d Sprite Texture를 관리하는 매니저 클래스임
 */
public class AtlasManager {

	public class texture {
		int textureId;
		int mWidth, mHeight;
		RectF[][] mPOS = null;

		public texture(Context mContext, int pTextureId, String spr_filename) {
			// load image Information file
			/*
			 * 하나의 Texture에 들어가있는 여러 개의 Sprite 위치정보를 가지고 있는 *.dat 파일을 읽는 부분임 현재는
			 * resource/raw 에서 읽지만 assets/ 으로 변경할 계획이 있음
			 */
			// String dat =
			// RawResourceReader.readTextFileFromRawResource(mContext, mContext
			// .getResources().
			// getIdentifier(spr_filename, "raw", mContext.getPackageName()));
			textureId = pTextureId;
			byte[] buffer = null;
			try {
				InputStream is = mContext.getAssets().open(spr_filename);
				int size = is.available();
				buffer = new byte[size];
				is.read(buffer);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String dat = new String(buffer);
			String[] array = dat.split("/");
			//if (!spr_filename.equals(array[0]))
			//	Log.d("EXCEPTION", "Sprite Read Fail : " + spr_filename);

			mWidth = Integer.parseInt(array[1]);
			mHeight = Integer.parseInt(array[2]);
			int AN = Integer.parseInt(array[3]);
			mPOS = new RectF[AN][];

			int arrayPOS = 4;
			for (int index = 0; index < AN; index++) {
				int SN = Integer.parseInt(array[arrayPOS]);

				mPOS[index] = new RectF[SN];
				for (int sindex = 0; sindex < SN; sindex++) {
					RectF temp = new RectF(
							Float.parseFloat(array[arrayPOS + 1]), // l
							Float.parseFloat(array[arrayPOS + 2]), // t
							Float.parseFloat(array[arrayPOS + 3]), // r
							Float.parseFloat(array[arrayPOS + 4]));// b
					mPOS[index][sindex] = temp;

					arrayPOS += 4;
				}
				++arrayPOS;
			}
		}
	}

	/* Sprite 정보들은 HashMap으로 관리되고 검색됨 */
	private HashMap<String, texture> arr_TexturePOS;

	public AtlasManager() {
		arr_TexturePOS = new HashMap<String, texture>();
	}

	public void setAtlas(Context mContext, String spr_name) {
		// TODO 중복 예외처리를 추가해야함
		getTexturePOS(mContext, spr_name, getBitmap(mContext, spr_name));
	}

	private int getBitmap(Context mContext, String spr_name) {
		// Read in the resource
		/* 이미지 파일을 읽는 부분임 TextureHelper Class로 부터 정보를 얻고 관리하게 됨 */
		/* 반환으로 textureId를 돌려줌 */
		return TextureHelper.loadTexture(mContext, "texture/"+spr_name+".png");
	}

	private void getTexturePOS(Context mContext, String spr_name, int textureId) {
		arr_TexturePOS.put(spr_name, new texture(mContext, textureId, "texture/"+spr_name+".dat"));
	}

	/* bitmap(Image)ID 값을 읽는 함수 */
	public int loadBitmap(String spr_name) {
		return arr_TexturePOS.get(spr_name).textureId;
	}

	/* ani_num과 spr_num 정보를 가지고 해당되는 Sprite 정보를 읽는 함수 */
	public RectF loadTexturePOS(String spr_name, int ani_num, int spr_num) {
		texture temp = arr_TexturePOS.get(spr_name);
		return temp.mPOS[ani_num][spr_num];
	}

	/* Sprite 크기를 읽는 함수 */
	public Util.Vector3f loadTextureSize(String spr_name, int ani_num) {
		texture temp = arr_TexturePOS.get(spr_name);
		return new Util.Vector3f(temp.mWidth, temp.mHeight,
				temp.mPOS[ani_num].length);
	}
}
