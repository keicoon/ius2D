package common;

/* FrameCounter Class
 * Function : 프레임을 측정하고 프레임 간격을 얻음
 */
public class FrameCounter {

	private int FrameCount;
	private static int preFrameCount;
	private long FrameTime;
	private long mLastTime;

	/* 생성될 때 변수를 초기화 함 */
	public FrameCounter() {
		mLastTime = System.currentTimeMillis();
		FrameCount = 0;
	}
	/* 화면이 정지되고 다시 복구될 때 실행되는 함수 */
	public void resetFrameCounter() {
		mLastTime = System.currentTimeMillis();
	}
	/* 시간을 측정하는 함수 < 프레임 간격을 ms단위로 반환한다 >*/
	public float Count() {
		long now = System.currentTimeMillis();
		long elapsed = now - mLastTime;

		FrameTime += elapsed;

		if (FrameTime > 1000L) {
			FrameTime = 0L;
			preFrameCount = FrameCount;
			FrameCount = 0;
		} else
			FrameCount++;

		mLastTime = now;
		return (float)elapsed*0.001f;
	}
	/* 현재 프레임을 반환하는 함수 */
	public static int getFrameCount() {
		return preFrameCount;
	}
}
