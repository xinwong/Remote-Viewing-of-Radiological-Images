package raycasting;

public class Ray3{//3ά����
	public Vector3 oril, dir;//ʼ ĩ
	public Ray3() {
		oril = new Vector3();
		dir = new Vector3();
	}
	public Ray3(Vector3 oril, Vector3 dir) {
		this.oril = oril;
		this.dir = dir;
	}
	public Vector3 getPoint(float t)//�õ������䵽�ĵ�
	{
		return this.oril.add(this.dir.multi(t));
	}
	public String toString()
	{
		return "Origin: " + oril + " Direction: "+dir;
	}
}