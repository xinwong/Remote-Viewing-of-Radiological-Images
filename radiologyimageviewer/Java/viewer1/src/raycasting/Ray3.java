package raycasting;

public class Ray3{//3维射线
	public Vector3 oril, dir;//始 末
	public Ray3() {
		oril = new Vector3();
		dir = new Vector3();
	}
	public Ray3(Vector3 oril, Vector3 dir) {
		this.oril = oril;
		this.dir = dir;
	}
	public Vector3 getPoint(float t)//得到射线射到的点
	{
		return this.oril.add(this.dir.multi(t));
	}
	public String toString()
	{
		return "Origin: " + oril + " Direction: "+dir;
	}
}