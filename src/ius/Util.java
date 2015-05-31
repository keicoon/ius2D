package ius;

import com.example.opengles20.GameObject;

public class Util {
	/* 사각 충돌
	 * 1. 충돌 여부 (ius_Rect, point)
	 * 2. 충돌 방향
	 * 원 충돌
	 * 1. 충돌 여부 (ius_Circle, point)
	 */
	static boolean RectCrashObject2Object_A(GameObject B, float bIndex, GameObject T, float tIndex){
		ius_Rect BasicRect = B.Get_Rect();
		ius_Rect TargetRect = T.Get_Rect();
		
		if( BasicRect.left + bIndex  <= TargetRect.right + tIndex
		&&	BasicRect.right + bIndex >= TargetRect.left + tIndex
		&& 	BasicRect.top + bIndex   <= TargetRect.bottom + tIndex
		&& 	BasicRect.bottom + bIndex>= TargetRect.top + tIndex		)
			return true;
		
		return false;
	}
	static ius_Rect RectCrashObject2Object_B(GameObject B, float bIndex, GameObject T, float tIndex){
		
		//boolean bVertical = false;
		//boolean bHorizontal = false;
		
		ius_Rect BasicRect = B.Get_Rect();
		ius_Rect TargetRect = T.Get_Rect();
		ius_Rect collisionRect = new ius_Rect(0f, 0f, 0f, 0f);
		
		//Horizontal crash
		if(BasicRect.left < TargetRect.right && BasicRect.right > TargetRect.left){
		//	bHorizontal = true;
			collisionRect.left = (BasicRect.left > TargetRect.left) ? BasicRect.left : TargetRect.left;
			collisionRect.right = (BasicRect.right < TargetRect.right) ? BasicRect.right : TargetRect.right;	
		}
		//Vertical crash
		if(BasicRect.top < TargetRect.bottom && BasicRect.bottom > TargetRect.top){
		//	bVertical = true;
			collisionRect.top = (BasicRect.top > TargetRect.top) ? BasicRect.top :TargetRect.top;
			collisionRect.bottom = (BasicRect.bottom < TargetRect.bottom) ? BasicRect.bottom : TargetRect.bottom;	
		}
		
		//if(bVertical && bHorizontal)
		return collisionRect;
		//return null;
	}
	
	static boolean RectCrashObject2Point(GameObject B, float bIndex, ius_Point T){
		ius_Rect BasicRect = B.Get_Rect();
		
		if( BasicRect.left + bIndex  <= T.x
				&&	BasicRect.right + bIndex >= T.x
				&& 	BasicRect.top + bIndex   <= T.y
				&& 	BasicRect.bottom + bIndex>= T.y		)
					return true;
		
		return false;
	}
	
	static boolean CircleCrashObject2Object_A(GameObject B, float bIndex, GameObject T, float tIndex){
		ius_Circle BasicCircle = new ius_Circle(B.Get_Rect());
		ius_Circle TargetCircle = new ius_Circle(B.Get_Rect());
		
		 if( Multyply(BasicCircle.x - TargetCircle.x)
		   + Multyply(BasicCircle.y - TargetCircle.y)
		   < Multyply(BasicCircle.r - TargetCircle.r + (bIndex - tIndex)))
			 return true;
		 
		 return false;
	}
	static boolean CircleCrashObject2Point(GameObject B, float bIndex, ius_Point T){
		ius_Circle BasicCircle = new ius_Circle(B.Get_Rect());
		
		if(Multyply(T.x - BasicCircle.x)
		 + Multyply(T.y - BasicCircle.y)
		 < Multyply(BasicCircle.r + bIndex))
			return true;
		
		return false;
	}
	
	
	public static class ius_Rect{
		public float left,top,right,bottom;
		public ius_Rect(float pl, float pt, float pr, float pb){
			left = pl; top = pt; right = pr; bottom = pb;
		}
		public float getWidth(){return right - left;}
		public float getHeight(){return bottom - top;}
		public float getCenterX(){return left + getWidth()/2;}
		public float getCenterY(){return top + getHeight()/2;}
	}
	public static class ius_Point{
		public float x,y;
		public ius_Point(float px, float py) {
			x = px; y = py;
		}
	}
	public static class ius_Circle{
		public float x, y, r;
		public ius_Circle(float px, float py, float pr){
			x = px; y = py; r = pr;
		}
		public ius_Circle(ius_Rect R){
			r = (R.getWidth() > R.getHeight()) ? R.getHeight()*0.5f : R.getWidth()*0.5f;
			x = R.getCenterX();
			y = R.getCenterY();
		}
	}
	private static float Multyply(float p, float t){
		return (t - p) * (t - p);
	}
	public static float Multyply(float t){
		return t * t;
	}
	/* 색깔을 표현할때 편하게 하기 위함*/
	public static class FFF{
		public float A,R,G,B;
		public FFF(int VALUE){
			B = (VALUE>>0 & 0xff) / 255f;
			G = (VALUE>>8 & 0xff) / 255f;
			R = (VALUE>>16 & 0xff) / 255f;
			A = (VALUE>>24 & 0xff) / 255f;
		}
	}
}
