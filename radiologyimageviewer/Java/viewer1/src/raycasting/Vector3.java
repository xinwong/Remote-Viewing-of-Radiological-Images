package raycasting;

public class Vector3{//��ά������Ҳ���Դ���ռ��еĵ㣩
	public float x,y,z;
	public Vector3() 
	{
		x=y=z=0;
	}
	public Vector3 copy(Vector3 clone)
	{
		return new Vector3(clone.x, clone.y, clone.z);
	}
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void Set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	
	public float Length()//�õ�����
	{
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
	public Vector3 normalize()//ʹ����Ϊ1����Ϊ��λ����
	{
		return new Vector3(x / Length(), y / Length(), z / Length());
	}
	public Vector3 add(Vector3 vec)//��
	{
		return new Vector3(this.x + vec.x,this.y + vec.y,this.z + vec.z);
	}
	public Vector3 subtract(Vector3 vec)//��
	{
		return new Vector3(this.x - vec.x,this.y - vec.y,this.z - vec.z);
	}
	public Vector3 multi(float m)//��������
	{
		return new Vector3(this.x*m, this.y*m, this.z*m);
	}
	public float dot(Vector3 vec)//�������
	{
		return vec.x*x + vec.y*y + vec.z*z;
	}
	public Vector3 cross(Vector3 v)//�������
	{
		return new Vector3(-this.z * v.y + this.y * v.z, this.z * v.x - this.x * v.z, -this.y * v.x + this.x * v.y);
	}
	public String toString()
	{
		return "["+x+" "+y+" "+z+"]";
	}
	
}