package viewer1;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.RandomAccessFile;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.SpotLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class fly extends JFrame implements KeyListener{
	private static final long seriaVersionUID = 1L;
	TransformGroup vtg;
	Canvas3D canvas3D; 
	public AxisAngle4f rotation = new AxisAngle4f(0.0f,1.0f,0.0f,0.f);//根据指定的xyzw坐标构造并初始化AxisAngle4f，x（x坐标），y（ y坐标），z（ z坐标），angle（ 以弧度表示的旋转角度）
	public AxisAngle4f rotation2 = new AxisAngle4f(1.0f,0.0f,0.0f,0.0f);
	
	public float rx = 1.05f;
	public float ry = 0.0f;
	public double x = -0.6;
	public double y = -0.28;
	public double z = 0.16;
	


	SpotLight spotLight = new SpotLight();
	int val, min, max, range, xm, ym;
	BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_4BYTE_ABGR);//该BufferedImage亚类描述了一种Image带有图像数据的访问的缓冲器,BufferedImage.TYPE_INT_RGB ： 表示一个图像，该图像具有整数像素的 8 位 RGB 颜色
	int width;
	int height;
	int depth;
	int slice = 100;
	JPanel pan2D = new JPanel();//JPanel就是一个面板 
	JPanel pan = new JPanel();
	JLabel label = new JLabel();

    Transform3D transform = new Transform3D();
//    vtg.getTransform(transform);
    Vector3d translation = new Vector3d();
//    transform.get(translation);
    
    JFrame frame = new JFrame("fly through");
    private JTextField text1 = new JTextField("fly through");

    public static void main(String args[])
   	{
   		fly fly = new fly();
   		fly.flyingg();
   	} 
    
	public void flyingg() {
		
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        label.setIcon(new ImageIcon(image));

		this.setSize(1000, 532);
		canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration()); //构造一个图形环境
		SimpleUniverse simpleU = new SimpleUniverse(canvas3D);//设定用户视角
		simpleU.getViewingPlatform().setNominalViewingTransform();//用于获取SimpleUniverse 实例化后的ViewingPlatform 对象。这个方法再附加调用setNominalViewingTransform()方法来调整视图的位置。
		simpleU.getViewer().getView().setFrontClipDistance(0.01);
		vtg = simpleU.getViewingPlatform().getViewPlatformTransform();//设置观察点
		canvas3D.addKeyListener(this);
		spotLight.setCapability(SpotLight.ALLOW_POSITION_WRITE);//SpotLight对象指定在空间中的固定点处的衰减光源，其从光源以指定方向辐射光
		TransformGroup light = new TransformGroup();//TransformGroup类的实例用于创建场景图并且与其子节点的集合
		light.addChild(spotLight);
		BranchGroup bg = new BranchGroup();//BranchGroup 类 这种类型的对象是用于构建建场景图的
		bg.addChild(light);
		simpleU.addBranchGraph(bg);
		
		try
		{
			File inputFile =  new File("D:\\dat\\vertex08629");
			RandomAccessFile f = new RandomAccessFile(inputFile, "rw");//"rw"打开以便读取和写入
			long fileLength = f.length();
			System.out.println("f.length()..."+f.length());
			File inputFile2 =  new File("D:\\dat\\normal08629");
			RandomAccessFile f2 = new RandomAccessFile(inputFile2, "rw");
			long fileLength2 = f2.length();
			System.out.println("f2.length()..."+f2.length());
			
			float[] normal = new float[4292088*3];
			float[] vertex = new float[4292088*3];
			
			for(int i = 0; i<4292088*3; i++)
			{
				vertex[i] = f.readFloat();
			}
			f.close();
			for(int i = 0; i<4292088*3; i++) {
				normal[i] = f2.readFloat();
			}
			f2.close();
			
			TriangleArray triangle;//TriangleArray对象将顶点数组绘制为单个三角形。每组三个顶点定义要绘制的三角形。
			triangle = new TriangleArray(4292088, TriangleArray.COORDINATES|TriangleArray.NORMALS);//COORDINATES指定此GeometryArray包含坐标数组。必须设置此位  | NORMALS指定此GeometryArray包含法线数组。
			triangle.setCoordinates(0, vertex,0,4292088);//setCoordinates(int index,float []坐标,int start,int length);index - 在此几何数组中开始目标顶点索引; coordinates - 包含n个新坐标的3 * n值的源数组; start- 在coordinates数组中启动源顶点索引。; length - 要复制的顶点数。
			triangle.setNormals(0, normal, 0, 4292088);//参数：index - 在此几何数组中开始目标顶点索引; normals - 包含新法线的向量的源数组; start- 在normals数组中启动源顶点索引。; length - 要复制的法线数。
			Shape3D tri = new Shape3D();
			tri.addGeometry(triangle);

			BranchGroup objRoot = new BranchGroup();//BranchGroup用作指向场景图分支根的指针; 
			Appearance app = new Appearance();//Appearance对象定义可以设置为Shape3D节点的组件对象的所有渲染状态
			
			TransformGroup transformGroup = new TransformGroup();//包含转换的组节点。TransformGroup节点通过Transform3D对象指定单个空间变换，该变换可以定位，定向和缩放其所有子项。
			
			MouseRotate rotate = new MouseRotate();//允许用户通过鼠标控制对象的旋转
			MouseWheelZoom wheelZoom = new MouseWheelZoom();//允许用户通过鼠标滚轮控制对象的Z轴平移
			MouseTranslate translate = new MouseTranslate();//MouseTranslate是一个Java3D行为对象，允许用户通过鼠标拖动动作使用第三个鼠标按钮（在PC上按住alt键）控制对象的平移（X，Y）
			
			app.setPolygonAttributes(new PolygonAttributes());//PolygonAttributes对象定义渲染多边形基元的属性。
			app.getPolygonAttributes().setCullFace(PolygonAttributes.CULL_NONE);// setCullFace设置此外观组件对象的面剔除;CULL_NONE - 禁用面部剔除
			
			DirectionalLight light2 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(-1.0f,-1.0f,-1.0f));//构造并初始化定向光。;//Color3ffloat x，float y， float z)x - 红色值; y - 绿色值; z - 蓝色值       //Vector3f（float x，float y，float z）x - x坐标; y - y坐标; z - z坐标
			light2.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));//将Light的影响区域设置为指定的边界  //BoundingSphere（Point3d 中心， double 半径）从中心和半径构造并初始化BoundingSphere。
			objRoot.addChild(light2);
			
			DirectionalLight light3 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(0.2f,0.25f,0.8f));//构造并初始化定向光。;//Color3ffloat x，float y， float z)x - 红色值; y - 绿色值; z - 蓝色值       //Vector3f（float x，float y，float z）x - x坐标; y - y坐标; z - z坐标
			light3.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));//将Light的影响区域设置为指定的边界  //BoundingSphere（Point3d 中心， double 半径）从中心和半径构造并初始化BoundingSphere。
			objRoot.addChild(light3);
			
			DirectionalLight light4 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(0.0f,0.0f,1.0f));//构造并初始化定向光。;//Color3ffloat x，float y， float z)x - 红色值; y - 绿色值; z - 蓝色值       //Vector3f（float x，float y，float z）x - x坐标; y - y坐标; z - z坐标
			light4.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));//将Light的影响区域设置为指定的边界  //BoundingSphere（Point3d 中心， double 半径）从中心和半径构造并初始化BoundingSphere。
			objRoot.addChild(light4);
			
			Material mat = new Material();
			mat.setDiffuseColor(new Color3f(1.0f,1.0f,1.0f));//设置此材质的漫反射颜色。
			mat.setAmbientColor(new Color3f(1.0f,1.0f,1.0f));//设置此材质的环境颜色。
			mat.setSpecularColor(new Color3f(1.0f,1.0f,1.0f));//设置此材质的镜面反射颜色。
			app.setMaterial(mat);
			
			transformGroup.setCapability(transformGroup.ALLOW_TRANSFORM_WRITE);//设置指定的功能位  //ALLOW_TRANSFORM_WRITE指定节点允许写入其对象的转换信息。
			rotate.setTransformGroup(transformGroup);
			rotate.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
			wheelZoom.setTransformGroup(transformGroup);
			wheelZoom.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
			translate.setTransformGroup(transformGroup);
			translate.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
			transformGroup.addChild(translate);//addChild将指定的子节点追加到此组节点的子节点列表中。
			transformGroup.addChild(rotate);
			transformGroup.addChild(wheelZoom);
			objRoot.addChild(transformGroup);
			

			label.addMouseListener(new MyListener());		
			

			
			tri.setAppearance(app);
			transformGroup.addChild(tri);
			objRoot.compile();//编译与此对象关联的源BranchGroup，并创建和缓存已编译的场景图。
			simpleU.addBranchGraph(objRoot);
			pan.setLayout(new BorderLayout());
			pan.add(canvas3D,BorderLayout.CENTER);
//			pan.add(pan2D, BorderLayout.EAST);
			this.setContentPane(pan);
			this.setVisible(true);//根据参数值显示或隐藏此组件

			rotation.set(1,0,0,rx);

			vtg.getTransform(transform);
			transform.get(translation);
		   
		    	translation.x = x;
			    translation.y = y;
			    translation.z = z;
				rotation.set(1,0,0,rx);
//				rotation2.set(0,1,0,ry);
	
			    transform.setRotation(rotation);
			    transform.setTranslation(translation);
			    vtg.setTransform(transform);  


	        frame.getContentPane().add(label, BorderLayout.CENTER);
			text1.setBounds(200, 200, 200, 20);
	        frame.add(text1);
	        frame.pack();
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	        label.setIcon(new ImageIcon(image));
	
		}catch (Exception e) {
			System.out.println(e.toString());
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	 public void keyReleased(KeyEvent ke)
	  {
		long startTime3 = System.currentTimeMillis();
    	int i = 0;
//	    Transform3D transform = new Transform3D();
	    vtg.getTransform(transform);
//	    Vector3f translation = new Vector3f();
	    transform.get(translation);

	    // Handle the up arrow
	    if(ke.getKeyCode() == KeyEvent.VK_UP)
	    {
	    	while(y <= 0.3) {
	    		i++;
	    		x -= 0.00000000087;
	    		y += 0.00000001;
	    		z -= 0.0000000057;
		    	translation.x = x;
			    translation.y = y;
			    translation.z = z;
//				System.out.println(z);			
//				rotation.set(1,0,0,rx);
//				rotation2.set(0,1,0,ry);
	
//			    transform.setRotation(rotation);
			    transform.setTranslation(translation);
			    vtg.setTransform(transform);  
//		        try {
//		            Thread.sleep(5);
//		        } catch (InterruptedException e) {
//		            e.printStackTrace(); 
//		        }
		 
	    	}
	    	
	    }
		long startTime4 = System.currentTimeMillis();
		System.out.println(i/((startTime4-startTime3)/1000));	
        String Str1 = new String();
        Str1 = String.valueOf("frame frequency="+(i/((startTime4-startTime3)/1000)));
        text1.setText(Str1);

	  }
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	private class MyListener extends MouseInputAdapter {
	    public void mousePressed(MouseEvent e) {
		    Transform3D transform = new Transform3D();
		    vtg.getTransform(transform);
		    Vector3f translation = new Vector3f();
		    transform.get(translation);
	        xm = e.getX();
	        ym = e.getY();
		    translation.z = 2*ym/512;
		    translation.x = 2*xm/512;
		    translation.y = 2*slice/512;
		    transform.setTranslation(translation);
		    vtg.setTransform(transform);
	    }
	}
	
}