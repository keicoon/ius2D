package com.example.opengles20;

import ius.AtlasManager;
import ius.FontManager;
import ius.ObjectManager;
import ius.Scene;
import ius.SoundManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import common.FrameCounter;
import common.RawResourceReader;
import common.ShaderHelper;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

/* myGLRenderer Class
 * Function : GLSurfaceView.Renderer를 상속받는 클래스로 openGLES로 그림을 그리는 부분을 담당한다.
 */
public class myGLRenderer implements GLSurfaceView.Renderer {

	public static final float[] mMVPMatrix = new float[16]; // model + view +
															// projection
	// private final float[] mLightPosInWorldSpace = new float[4]; // 광원 월드 위치
	// 정보
	// private float[] mLightModelMatrix = new float[16]; // 광원 매트릭스 정보
	// private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f,
	// 0.0f, 1.0f}; // 광원 모델 위치 정보

	public static final float[] mProjectionMatrix = new float[16]; // 투영 매트릭스
	public static final float[] mViewMatrix = new float[16]; // 뷰 매트릭스
	// public static final float[] mLightPosInEyeSpace = new float[4]; // 광원 정보를
	// 뷰로 매핑한 매트릭스

	private Context mContext;
	public static int mObjectProgramHandle; // Object Shader Handle
	public static int mFontProgramHandle; // Font Shader Handle
	// public static int mBWObjectProgramHandle; // black&white Shader Handle
	// public static int mOutlineObjectProgramHandle; // outline Shader Handle

	// 게임 화면 크기
	// 일반적인 화면 resolution은 1280x720 임
	public static float mScreenWidth = 1280f;
	public static float mScreenHeight = 720f;
	// 실제 디바이스 화면 크기
	// Nexus5의 resolution은 1080x1920 임
	public static float pScreenWidth;
	public static float pScreenHeight;
	// 게임화면과 실제 디바이스 화면의 비율차를 보정하기 위한 scale
	public static float ssx = 1.0f;
	public static float ssy = 1.0f;

	private Scene currentScene; // 현재 씬을 나타냄
	private FrameCounter framecounter;
	public void SetupScaling() {
		// The screen resolutions
		float swp = (int) (mContext.getResources().getDisplayMetrics().widthPixels);
		float shp = (int) (mContext.getResources().getDisplayMetrics().heightPixels);
		// Orientation is assumed portrait
		ssx = swp / mScreenWidth;
		ssy = shp / mScreenHeight;
	}

	public void SetCurrentScene(Scene curr) {
		currentScene = curr;
	}

	public myGLRenderer(Context context, Scene curr) {
		mContext = context;
		currentScene = curr;
	}

	boolean instance = false;

	private void Initialize() {
		if (instance == false) {
			/*
			 * final String bwVertexShader =
			 * RawResourceReader.readTextFileFromRawResource(mContext,
			 * R.raw.bw_vertex_shader); final String bwFragmentShader =
			 * RawResourceReader.readTextFileFromRawResource(mContext,
			 * R.raw.bw_fragment_shader); final int bwVertexShaderHandle =
			 * ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER,
			 * bwVertexShader); final int bwFragmentShaderHandle =
			 * ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER,
			 * bwFragmentShader); mBWObjectProgramHandle =
			 * ShaderHelper.createAndLinkProgram(bwVertexShaderHandle,
			 * bwFragmentShaderHandle, new String[] {"a_Position", "a_Color",
			 * "a_TexCoordinate"});
			 */
			/*
			 * final String outlineVertexShader =
			 * RawResourceReader.readTextFileFromRawResource(mContext,
			 * R.raw.outline_font_vertex_shader); final String
			 * outlineFragmentShader =
			 * RawResourceReader.readTextFileFromRawResource(mContext,
			 * R.raw.outline_font_fragment_shader); final int
			 * outlineVertexShaderHandle =
			 * ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER,
			 * outlineVertexShader); final int outlineFragmentShaderHandle =
			 * ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER,
			 * outlineFragmentShader); mOutlineObjectProgramHandle =
			 * ShaderHelper.createAndLinkProgram(outlineVertexShaderHandle,
			 * outlineFragmentShaderHandle, new String[] {"a_Position",
			 * "a_Color", "a_TexCoordinate"});
			 */
			final String vertexShaderCode = RawResourceReader
					.readTextFileFromRawResource(mContext,
							R.raw.object_vertex_shader);
			final String fragmentShaderCode = RawResourceReader
					.readTextFileFromRawResource(mContext,
							R.raw.object_fragment_shader);
			final int vertexShaderHandle = ShaderHelper.compileShader(
					GLES20.GL_VERTEX_SHADER, vertexShaderCode);
			final int fragmentShaderHandle = ShaderHelper.compileShader(
					GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
			mObjectProgramHandle = ShaderHelper.createAndLinkProgram(
					vertexShaderHandle, fragmentShaderHandle, new String[] {
							"a_Position", "a_Color", "a_TexCoordinate" });

			final String pointVertexShader = RawResourceReader
					.readTextFileFromRawResource(mContext,
							R.raw.font_vertex_shader);
			final String pointFragmentShader = RawResourceReader
					.readTextFileFromRawResource(mContext,
							R.raw.font_fragment_shader);
			final int pointVertexShaderHandle = ShaderHelper.compileShader(
					GLES20.GL_VERTEX_SHADER, pointVertexShader);
			final int pointFragmentShaderHandle = ShaderHelper.compileShader(
					GLES20.GL_FRAGMENT_SHADER, pointFragmentShader);
			mFontProgramHandle = ShaderHelper
					.createAndLinkProgram(pointVertexShaderHandle,
							pointFragmentShaderHandle,
							new String[] { "a_Position", "a_Color",
									"a_TexCoordinate" });

			// Initialize managers
			AtlasManager.getInstance().LoadAtlasManager(mContext);
			FontManager.getInstance().FontLoad("font/Roboto-Regular.ttf",
					mContext);// Roboto-Regular // whitecat
			SoundManager.getInstance().SoundLoad(mContext);
			ObjectManager.getInstance().Create(200);
			framecounter = new FrameCounter();
			instance = true;
		}
	}

	/* Surface가 생성됬을 때 초기화하는 함수 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		Initialize();

		// Initialize GLES20
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		// No culling of back faces
		GLES20.glDisable(GLES20.GL_CULL_FACE);
		// No depth testing
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		 
		// Enable blending
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		// Initialize basic Scene
		framecounter.resetFrameCounter();
		currentScene.Init();
	}

	/* 매 프레임마다 실행되는 드로우 함수 */
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Matrix.setIdentityM(mLightModelMatrix, 0);
		// Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 1.0f);
		// Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0,
		// mLightPosInModelSpace, 0);
		// Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0,
		// mLightPosInWorldSpace, 0);

		// 일반적으로 Object를 그리기 떄문에 호출함
		// 프레임 간격을 얻음 < ms단위 >
		currentScene.run(framecounter.Count());

		// 빛을 화면에 출력할 때 호출함
		// drawLight(mPointProgramHandle);
	}

	/* SurfaceView가 재생성될 때 호출되는 함수 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Set the OpenGL viewport to the same size as the surface.
		pScreenWidth = width;
		pScreenHeight = height;
		GLES20.glViewport(0, 0, width, height);

		Matrix.orthoM(mProjectionMatrix, 0, 0f, width, 0.0f, height, 0, 1);
		Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f,
				0.0f);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		SetupScaling();

		/*
		 * frustumM 방식의 Projection final float ratio = (float) width / height;
		 * final float left = -ratio; final float right = ratio; final float
		 * bottom = -1.0f; final float top = 1.0f; final float near = 3.0f;
		 * final float far = 7.0f; Matrix.frustumM(mProjectionMatrix, 0, left,
		 * right, bottom, top, near, far);
		 */
	}
	/*
	 * 빛을 그리는 함수 < 일반적으로 사용하지 않아 주석으로 함 > private void drawLight(int
	 * mPointProgramHandle) { final int pointMVPMatrixHandle =
	 * GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix"); final
	 * int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle,
	 * "a_Position");
	 * 
	 * // Pass in the position. GLES20.glVertexAttrib3f(pointPositionHandle,
	 * mLightPosInModelSpace[0], mLightPosInModelSpace[1],
	 * mLightPosInModelSpace[2]);
	 * 
	 * // Since we are not using a buffer object, disable vertex arrays for this
	 * attribute. GLES20.glDisableVertexAttribArray(pointPositionHandle);
	 * 
	 * // Pass in the transformation matrix. float[] mMVPMatrix = new float[16];
	 * Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mLightModelMatrix, 0);
	 * Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
	 * GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);
	 * 
	 * // Draw the point. GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1); }
	 */
}
