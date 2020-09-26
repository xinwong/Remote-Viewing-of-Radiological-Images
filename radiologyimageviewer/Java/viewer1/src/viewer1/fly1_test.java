package viewer1;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;

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

import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class fly1_test extends JFrame implements KeyListener{
	private static final long seriaVersionUID = 1L;
	TransformGroup vtg;
	Canvas3D canvas3D; 
	public AxisAngle4f rotation = new AxisAngle4f(0.0f,1.0f,0.0f,0.f);//����ָ����xyzw���깹�첢��ʼ��AxisAngle4f��x��x���꣩��y�� y���꣩��z�� z���꣩��angle�� �Ի��ȱ�ʾ����ת�Ƕȣ�
	public AxisAngle4f rotation2 = new AxisAngle4f(1.0f,0.0f,0.0f,0.0f);
	
	public float rx = 1.05f;
	public float ry = 0.0f;
	public double x = -0.6;
	public double y = -0.28;
	public double z = 0.16;
	


	SpotLight spotLight = new SpotLight();
	int val, min, max, range, xm, ym;
	BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_4BYTE_ABGR);//��BufferedImage����������һ��Image����ͼ�����ݵķ��ʵĻ�����,BufferedImage.TYPE_INT_RGB �� ��ʾһ��ͼ�񣬸�ͼ������������ص� 8 λ RGB ��ɫ
	int width;
	int height;
	int depth;
	int slice = 100;
	JPanel pan2D = new JPanel();//JPanel����һ����� 
	JPanel pan = new JPanel();
	JLabel label = new JLabel();

    Transform3D transform = new Transform3D();
//    vtg.getTransform(transform);
    Vector3d translation = new Vector3d();
//    transform.get(translation);
    
	float[] normal = new float[5140116*3];
	float[] vertex = new float[5140116*3];
	SimpleUniverse simpleU = new SimpleUniverse(canvas3D);//�趨�û��ӽ�

    JFrame frame = new JFrame("fly through");
    JPanel buttons = new JPanel(new FlowLayout()); 
    private JTextField text1 = new JTextField("fly through");
    JButton download1 = new JButton("download vertex");
    JButton download2 = new JButton("download normal");

    class SimpleListener implements ActionListener
    {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == download1) {
            	download1();
            }
            if (ae.getSource() == download2) {
            	download2();
            }
        }         
    }

    public static void main(String args[])
   	{
   		fly1_test fly1_test = new fly1_test();
   		fly1_test.flying();
   	} 
    
//    public void actionPerformed(ActionEvent ae) {
//        if (ae.getSource() == download1) {
//        	download1();
//        }
//        if (ae.getSource() == download2) {
//        	download2();
//        }
//    }    
	private void download1() {
		String dat = new String();
        System.out.println(dat);

//        Download dl = new Download();
    	try{
//    		Download.downLoadFromUrl("http://192.168.1.110:8080/do/22118CTC"
//					,"normal08629"
//					,"/home/ln/do/");
    		
//    		Download.downLoadFromUrl("http://134.175.21.96:8080/do/08629CTC.dat"
//									,"08629CTC.dat"
//									,"/Users/min/Desktop/");

			Download.downLoadFromUrl("http://134.175.21.96:8080/do/vertex08779"
									,"vertex08779"
									,"C:\\datset\\");//static����̬�Ŀ���ֱ������������
		}catch (Exception e) {
		}	
    	System.out.println("vertex08779");
	}
	private void download2() {
		String dat = new String();
        System.out.println(dat);

//        Download dl = new Download();
    	try{
//    		Download.downLoadFromUrl("http://192.168.1.110:8080/do/22118CTC"
//					,"normal08629"
//					,"/home/ln/do/");
    		
//    		Download.downLoadFromUrl("http://134.175.21.96:8080/do/08629CTC.dat"
//									,"08629CTC.dat"
//									,"/Users/min/Desktop/");

			Download.downLoadFromUrl("http://134.175.21.96:8080/do/normal08779"
									,"normal08779"
									,"C:\\datset\\");//static����̬�Ŀ���ֱ������������
		}catch (Exception e) {
		}	 
    	System.out.println("normal08779");
	}
    
	public void flying() {
		
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        label.setIcon(new ImageIcon(image));

		this.setSize(1000, 532);
		canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration()); //����һ��ͼ�λ���
//		SimpleUniverse simpleU = new SimpleUniverse(canvas3D);//�趨�û��ӽ�
		simpleU.getViewingPlatform().setNominalViewingTransform();//���ڻ�ȡSimpleUniverse ʵ�������ViewingPlatform ������������ٸ��ӵ���setNominalViewingTransform()������������ͼ��λ�á�
		simpleU.getViewer().getView().setFrontClipDistance(0.01);
		vtg = simpleU.getViewingPlatform().getViewPlatformTransform();//���ù۲��
		canvas3D.addKeyListener(this);
		spotLight.setCapability(SpotLight.ALLOW_POSITION_WRITE);//SpotLight����ָ���ڿռ��еĹ̶��㴦��˥����Դ����ӹ�Դ��ָ����������
		TransformGroup light = new TransformGroup();//TransformGroup���ʵ�����ڴ�������ͼ���������ӽڵ�ļ���
		light.addChild(spotLight);
		BranchGroup bg = new BranchGroup();//BranchGroup �� �������͵Ķ��������ڹ���������ͼ��
		bg.addChild(light);
		simpleU.addBranchGraph(bg);
		SimpleListener ourListener = new SimpleListener();
        // ����һ��actionlistener��������ť����
        download1.addActionListener(ourListener);
        download2.addActionListener(ourListener);
//        download1.addActionListener((ActionListener) this);
//        download2.addActionListener((ActionListener) this);
        buttons.add(download1);
        buttons.add(download2);
        frame.getContentPane().add(buttons, BorderLayout.SOUTH);

        

		
		try
		{
			File inputFile =  new File("C:\\datset\\vertex08779");
//			File inputFile =  new File("/Users/min/Downloads/test/vertex08629");
//			File inputFile =  new File("/home/ln/do/vertex08629");


			RandomAccessFile f = new RandomAccessFile(inputFile, "rw");//"rw"���Ա��ȡ��д��
			long fileLength = f.length();
			System.out.println("f.length()..."+f.length());
			File inputFile2 =  new File("C:\\datset\\normal08779");
//			File inputFile2 =  new File("/Users/min/Downloads/test/normal08629");
//			File inputFile2 =  new File("/home/ln/do/normal08629");

			
			RandomAccessFile f2 = new RandomAccessFile(inputFile2, "rw");
			long fileLength2 = f2.length();
			System.out.println("f2.length()..."+f2.length());
			
//			float[] normal = new float[4292088*3];
//			float[] vertex = new float[4292088*3];
			
			for(int i = 0; i<5140116*3; i++)
			{
				vertex[i] = f.readFloat();
			}
			f.close();
			for(int i = 0; i<5140116*3; i++) {
				normal[i] = f2.readFloat();
			}
			f2.close();
			
			TriangleArray triangle;//TriangleArray���󽫶����������Ϊ���������Ρ�ÿ���������㶨��Ҫ���Ƶ������Ρ�
			triangle = new TriangleArray(5140116, TriangleArray.COORDINATES|TriangleArray.NORMALS);//COORDINATESָ����GeometryArray�����������顣�������ô�λ  | NORMALSָ����GeometryArray�����������顣
			triangle.setCoordinates(0, vertex,0,5140116);//setCoordinates(int index,float []����,int start,int length);index - �ڴ˼��������п�ʼĿ�궥������; coordinates - ����n���������3 * nֵ��Դ����; start- ��coordinates����������Դ����������; length - Ҫ���ƵĶ�������
			triangle.setNormals(0, normal, 0, 5140116);//������index - �ڴ˼��������п�ʼĿ�궥������; normals - �����·��ߵ�������Դ����; start- ��normals����������Դ����������; length - Ҫ���Ƶķ�������
			Shape3D tri = new Shape3D();
			tri.addGeometry(triangle);

			BranchGroup objRoot = new BranchGroup();//BranchGroup����ָ�򳡾�ͼ��֧����ָ��; 
			Appearance app = new Appearance();//Appearance�������������ΪShape3D�ڵ����������������Ⱦ״̬
			
			TransformGroup transformGroup = new TransformGroup();//����ת������ڵ㡣TransformGroup�ڵ�ͨ��Transform3D����ָ�������ռ�任���ñ任���Զ�λ��������������������
			
			MouseRotate rotate = new MouseRotate();//�����û�ͨ�������ƶ������ת
			MouseWheelZoom wheelZoom = new MouseWheelZoom();//�����û�ͨ�������ֿ��ƶ����Z��ƽ��
			MouseTranslate translate = new MouseTranslate();//MouseTranslate��һ��Java3D��Ϊ���������û�ͨ������϶�����ʹ�õ�������갴ť����PC�ϰ�סalt�������ƶ����ƽ�ƣ�X��Y��
			
			app.setPolygonAttributes(new PolygonAttributes());//PolygonAttributes��������Ⱦ����λ�Ԫ�����ԡ�
			app.getPolygonAttributes().setCullFace(PolygonAttributes.CULL_NONE);// setCullFace���ô���������������޳�;CULL_NONE - �����沿�޳�
			
			DirectionalLight light2 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(-1.0f,-1.0f,-1.0f));//���첢��ʼ������⡣;//Color3ffloat x��float y�� float z)x - ��ɫֵ; y - ��ɫֵ; z - ��ɫֵ       //Vector3f��float x��float y��float z��x - x����; y - y����; z - z����
			light2.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));//��Light��Ӱ����������Ϊָ���ı߽�  //BoundingSphere��Point3d ���ģ� double �뾶�������ĺͰ뾶���첢��ʼ��BoundingSphere��
			objRoot.addChild(light2);
			
			DirectionalLight light3 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(0.2f,0.25f,0.8f));//���첢��ʼ������⡣;//Color3ffloat x��float y�� float z)x - ��ɫֵ; y - ��ɫֵ; z - ��ɫֵ       //Vector3f��float x��float y��float z��x - x����; y - y����; z - z����
			light3.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));//��Light��Ӱ����������Ϊָ���ı߽�  //BoundingSphere��Point3d ���ģ� double �뾶�������ĺͰ뾶���첢��ʼ��BoundingSphere��
			objRoot.addChild(light3);
			
			DirectionalLight light4 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(0.0f,0.0f,1.0f));//���첢��ʼ������⡣;//Color3ffloat x��float y�� float z)x - ��ɫֵ; y - ��ɫֵ; z - ��ɫֵ       //Vector3f��float x��float y��float z��x - x����; y - y����; z - z����
			light4.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));//��Light��Ӱ����������Ϊָ���ı߽�  //BoundingSphere��Point3d ���ģ� double �뾶�������ĺͰ뾶���첢��ʼ��BoundingSphere��
			objRoot.addChild(light4);
			
			Material mat = new Material();
			mat.setDiffuseColor(new Color3f(1.0f,1.0f,1.0f));//���ô˲��ʵ���������ɫ��
			mat.setAmbientColor(new Color3f(1.0f,1.0f,1.0f));//���ô˲��ʵĻ�����ɫ��
			mat.setSpecularColor(new Color3f(1.0f,1.0f,1.0f));//���ô˲��ʵľ��淴����ɫ��
			app.setMaterial(mat);
			
			transformGroup.setCapability(transformGroup.ALLOW_TRANSFORM_WRITE);//����ָ���Ĺ���λ  //ALLOW_TRANSFORM_WRITEָ���ڵ�����д��������ת����Ϣ��
			rotate.setTransformGroup(transformGroup);
			rotate.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
			wheelZoom.setTransformGroup(transformGroup);
			wheelZoom.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
			translate.setTransformGroup(transformGroup);
			translate.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
			transformGroup.addChild(translate);//addChild��ָ�����ӽڵ�׷�ӵ�����ڵ���ӽڵ��б��С�
			transformGroup.addChild(rotate);
			transformGroup.addChild(wheelZoom);
			objRoot.addChild(transformGroup);
			

			label.addMouseListener(new MyListener());		
			

			
			tri.setAppearance(app);
			transformGroup.addChild(tri);
			objRoot.compile();//������˶��������ԴBranchGroup���������ͻ����ѱ���ĳ���ͼ��
			simpleU.addBranchGraph(objRoot);
			pan.setLayout(new BorderLayout());
			pan.add(canvas3D,BorderLayout.CENTER);
//			pan.add(pan2D, BorderLayout.EAST);
			this.setContentPane(pan);
			this.setVisible(true);//���ݲ���ֵ��ʾ�����ش����

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
		System.out.println("done");
    	int i = 0;
//	    Transform3D transform = new Transform3D();
	    vtg.getTransform(transform);
//	    Vector3f translation = new Vector3f();
	    transform.get(translation);

	    // Handle the up arrow
	    if(ke.getKeyCode() == KeyEvent.VK_UP)
	    {
	    	while(y <= 0.3) {
	    		TriangleArray triangle;//TriangleArray���󽫶����������Ϊ���������Ρ�ÿ���������㶨��Ҫ���Ƶ������Ρ�
				triangle = new TriangleArray(4292088, TriangleArray.COORDINATES|TriangleArray.NORMALS);//COORDINATESָ����GeometryArray�����������顣�������ô�λ  | NORMALSָ����GeometryArray�����������顣
				triangle.setCoordinates(0, vertex,0,4292088);//setCoordinates(int index,float []����,int start,int length);index - �ڴ˼��������п�ʼĿ�궥������; coordinates - ����n���������3 * nֵ��Դ����; start- ��coordinates����������Դ����������; length - Ҫ���ƵĶ�������
				triangle.setNormals(0, normal, 0, 4292088);//������index - �ڴ˼��������п�ʼĿ�궥������; normals - �����·��ߵ�������Դ����; start- ��normals����������Դ����������; length - Ҫ���Ƶķ�������
				Shape3D tri = new Shape3D();
				tri.addGeometry(triangle);

				BranchGroup objRoot = new BranchGroup();//BranchGroup����ָ�򳡾�ͼ��֧����ָ��; 
				Appearance app = new Appearance();//Appearance�������������ΪShape3D�ڵ����������������Ⱦ״̬
				
				TransformGroup transformGroup = new TransformGroup();//����ת������ڵ㡣TransformGroup�ڵ�ͨ��Transform3D����ָ�������ռ�任���ñ任���Զ�λ��������������������
				
//				MouseRotate rotate = new MouseRotate();//�����û�ͨ�������ƶ������ת
//				MouseWheelZoom wheelZoom = new MouseWheelZoom();//�����û�ͨ�������ֿ��ƶ����Z��ƽ��
//				MouseTranslate translate = new MouseTranslate();//MouseTranslate��һ��Java3D��Ϊ���������û�ͨ������϶�����ʹ�õ�������갴ť����PC�ϰ�סalt�������ƶ����ƽ�ƣ�X��Y��
				
				app.setPolygonAttributes(new PolygonAttributes());//PolygonAttributes��������Ⱦ����λ�Ԫ�����ԡ�
				app.getPolygonAttributes().setCullFace(PolygonAttributes.CULL_NONE);// setCullFace���ô���������������޳�;CULL_NONE - �����沿�޳�
				
//				DirectionalLight light2 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(-1.0f,-1.0f,-1.0f));//���첢��ʼ������⡣;//Color3ffloat x��float y�� float z)x - ��ɫֵ; y - ��ɫֵ; z - ��ɫֵ       //Vector3f��float x��float y��float z��x - x����; y - y����; z - z����
//				light2.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));//��Light��Ӱ����������Ϊָ���ı߽�  //BoundingSphere��Point3d ���ģ� double �뾶�������ĺͰ뾶���첢��ʼ��BoundingSphere��
//				objRoot.addChild(light2);
//				
//				DirectionalLight light3 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(0.2f,0.25f,0.8f));//���첢��ʼ������⡣;//Color3ffloat x��float y�� float z)x - ��ɫֵ; y - ��ɫֵ; z - ��ɫֵ       //Vector3f��float x��float y��float z��x - x����; y - y����; z - z����
//				light3.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));//��Light��Ӱ����������Ϊָ���ı߽�  //BoundingSphere��Point3d ���ģ� double �뾶�������ĺͰ뾶���첢��ʼ��BoundingSphere��
//				objRoot.addChild(light3);
//				
//				DirectionalLight light4 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(0.0f,0.0f,1.0f));//���첢��ʼ������⡣;//Color3ffloat x��float y�� float z)x - ��ɫֵ; y - ��ɫֵ; z - ��ɫֵ       //Vector3f��float x��float y��float z��x - x����; y - y����; z - z����
//				light4.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));//��Light��Ӱ����������Ϊָ���ı߽�  //BoundingSphere��Point3d ���ģ� double �뾶�������ĺͰ뾶���첢��ʼ��BoundingSphere��
//				objRoot.addChild(light4);
				
				Material mat = new Material();
				mat.setDiffuseColor(new Color3f(1.0f,1.0f,1.0f));//���ô˲��ʵ���������ɫ��
				mat.setAmbientColor(new Color3f(1.0f,1.0f,1.0f));//���ô˲��ʵĻ�����ɫ��
				mat.setSpecularColor(new Color3f(1.0f,1.0f,1.0f));//���ô˲��ʵľ��淴����ɫ��
				app.setMaterial(mat);
				
//				transformGroup.setCapability(transformGroup.ALLOW_TRANSFORM_WRITE);//����ָ���Ĺ���λ  //ALLOW_TRANSFORM_WRITEָ���ڵ�����д��������ת����Ϣ��
//
//				objRoot.addChild(transformGroup);
//				
//
//				label.addMouseListener(new MyListener());		
				

				
//				tri.setAppearance(app);
//				transformGroup.addChild(tri);
//				objRoot.compile();//������˶��������ԴBranchGroup���������ͻ����ѱ���ĳ���ͼ��
//				simpleU.addBranchGraph(objRoot);
//				pan.setLayout(new BorderLayout());
//				pan.add(canvas3D,BorderLayout.CENTER);
				
				
//				pan.add(pan2D, BorderLayout.EAST);
//				this.setContentPane(pan);
//				this.setVisible(true);//���ݲ���ֵ��ʾ�����ش����
		
	    		i++;
	    		x -= 0.000174;
	    		y += 0.002;
	    		z -= 0.00114;
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