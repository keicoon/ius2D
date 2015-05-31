package common;

/* FrameCounter Class
 * Function : �������� �����ϰ� ������ ������ ����
 */
public class FrameCounter {

	private int FrameCount;
	private long FrameTime;
	private long mLastTime;

	/* ������ �� ������ �ʱ�ȭ �� */
	public FrameCounter() {
		mLastTime = System.currentTimeMillis();
		FrameCount = 0;
	}
	/* ȭ���� �����ǰ� �ٽ� ������ �� ����Ǵ� �Լ� */
	public void resetFrameCounter() {
		mLastTime = System.currentTimeMillis();
	}
	/* �ð��� �����ϴ� �Լ� < ������ ������ ms������ ��ȯ�Ѵ� >*/
	public long Count() {
		long now = System.currentTimeMillis();
		long elapsed = now - mLastTime;

		FrameTime += elapsed;

		if (FrameTime > 1000L) {
			FrameTime = 0L;
			FrameCount = 0;
		} else
			FrameCount++;

		mLastTime = now;
		return elapsed;
	}
	/* ���� �������� ��ȯ�ϴ� �Լ� */
	public int getFrameCount() {
		return FrameCount;
	}
}
