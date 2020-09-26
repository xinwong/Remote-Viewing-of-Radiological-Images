package raycasting;

public class Vector3{//三维向量（也可以代表空间中的点）
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
	
	public float Length()//得到长度
	{
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
	public Vector3 normalize()//使长度为1，化为单位向量
	{
		return new Vector3(x / Length(), y / Length(), z / Length());
	}
	public Vector3 add(Vector3 vec)//加
	{
		return new Vector3(this.x + vec.x,this.y + vec.y,this.z + vec.z);
	}
	public Vector3 subtract(Vector3 vec)//减
	{
		return new Vector3(this.x - vec.x,this.y - vec.y,this.z - vec.z);
	}
	public Vector3 multi(float m)//倍率增大
	{
		return new Vector3(this.x*m, this.y*m, this.z*m);
	}
	public float dot(Vector3 vec)//向量点乘
	{
		return vec.x*x + vec.y*y + vec.z*z;
	}
	public Vector3 cross(Vector3 v)//向量叉乘
	{
		return new Vector3(-this.z * v.y + this.y * v.z, this.z * v.x - this.x * v.z, -this.y * v.x + this.x * v.y);
	}
	public String toString()
	{
		return "["+x+" "+y+" "+z+"]";
	}
	
}