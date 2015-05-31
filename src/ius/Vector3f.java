package ius;

public class Vector3f {
	public float x;
	public float y;
	public float z;
	public int iZ;
	public Vector3f(float px, float py, float pz) {
		// TODO Auto-generated constructor stub
		x= px; y = py; z = pz;
	}
	public Vector3f(float px, float py, int pz) {
		// TODO Auto-generated constructor stub
		x= px; y = py; iZ = pz;
	}
}
