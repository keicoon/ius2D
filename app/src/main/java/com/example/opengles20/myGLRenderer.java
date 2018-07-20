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
 * Function : GLSurfaceView.Renderer�� ��ӹ޴� Ŭ������ openGLES�� �׸��� �׸��� �κ��� ����Ѵ�.
 */
public class myGLRenderer implements GLSurfaceView.Renderer {

	public static final float[] mMVPMatrix = new float[16]; // model + view +
															// projection
	// private final float[] mLightPosInWorldSpace = new float[4]; // ���� ���� ��ġ
	// ����
	// private float[] mLightModelMatrix = new float[16]; // ���� ��Ʈ���� ����
	// private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f,
	// 0.0f, 1.0f}; // ���� �� ��ġ ����

	public static final float[] mProjectionMatrix = new float[16]; // ���� ��Ʈ����
	public static final float[] mViewMatrix = new float[16]; // �� ��Ʈ����
	// public static final float[] mLightPosInEyeSpace = new float[4]; // ���� ������
	// ��� ������ ��Ʈ����

	private Context mContext;
	public static int mObjectProgramHandle; // Object Shader Handle
	public static int mFontProgramHandle; // Font Shader Handle
	// public static int mBWObjectProgramHandle; // black&white Shader Handle
	// public static int mOutlineObjectProgramHandle; // outline Shader Handle

	// ���� ȭ�� ũ��
	// �Ϲ����� ȭ�� resolution�� 1280x720 ��
	public static float mScreenWidth = 1280f;
	public static float mScreenHeight = 720f;
	// ���� ����̽� ȭ�� ũ��
	// Nexus5�� resolution�� 1080x1920 ��
	public static float pScreenWidth;
	public static float pScreenHeight;
	// ����ȭ��� ���� ����̽� ȭ���� �������� �����ϱ� ���� scale
	public static float ssx = 1.0f;
	public static float ssy = 1.0f;

	private Scene currentScene; // ���� ���� ��Ÿ��
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

	/* Surface�� �������� �� �ʱ�ȭ�ϴ� �Լ� */
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

	/* �� �����Ӹ��� ����Ǵ� ��ο� �Լ� */
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Matrix.setIdentityM(mLightModelMatrix, 0);
		// Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 1.0f);
		// Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0,
		// mLightPosInModelSpace, 0);
		// Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0,
		// mLightPosInWorldSpace, 0);

		// �Ϲ������� Object�� �׸��� ������ ȣ����
		// ������ ������ ���� < ms���� >
		currentScene.run(framecounter.Count());

		// ���� ȭ�鿡 ����� �� ȣ����
		// drawLight(mPointProgramHandle);
	}

	/* SurfaceView�� ������� �� ȣ��Ǵ� �Լ� */
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
		 * frustumM ����� Projection final float ratio = (float) width / height;
		 * final float left = -ratio; final float right = ratio; final float
		 * bottom = -1.0f; final float top = 1.0f; final float near = 3.0f;
		 * final float far = 7.0f; Matrix.frustumM(mProjectionMatrix, 0, left,
		 * right, bottom, top, near, far);
		 */
	}
	/*
	 * ���� �׸��� �Լ� < �Ϲ������� ������� �ʾ� �ּ����� �� > private void drawLight(int
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
