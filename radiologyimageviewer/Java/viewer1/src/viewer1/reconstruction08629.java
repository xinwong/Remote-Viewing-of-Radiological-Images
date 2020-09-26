package viewer1;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.RandomAccessFile;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
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
import javax.swing.ImageIcon;
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
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;


public class reconstruction08629 extends JFrame implements KeyListener , Runnable {
	private static final long serialVersionUID = 1L;
	TransformGroup vtg;
	Canvas3D canvas3D;
	public AxisAngle4f rotation = new AxisAngle4f(0.0f, 1.0f, 0.0f, 0.0f);
	public AxisAngle4f rotation2 = new AxisAngle4f(1.0f, 0.0f, 0.0f, 0.0f);
	public float angle = 0.0f;
	public float angle2 = 0.0f;
	SpotLight spotLight = new SpotLight();
    int val, min, max, range, xm, ym;
	BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
	int width;
	int height;
	int depth;
    int slice = 100;
    JPanel pan2D = new JPanel();
	JPanel pan = new JPanel();
	JLabel label = new JLabel();
    private JTextField text1 = new JTextField("Get vertex and normal");
    private JTextField text2 = new JTextField("Generate 3D model");

    JFrame frame = new JFrame("3D Reconstruction");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		reconstruction08629 reconstruction08629 = new reconstruction08629();
		reconstruction08629.reconstruction();
	}
	
	public void reconstruction(){

		
	    this.setSize(1000, 532);

	    canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
	    SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
		simpleU.getViewingPlatform().setNominalViewingTransform();
		simpleU.getViewer().getView().setFrontClipDistance(0.01);
		vtg = simpleU.getViewingPlatform().getViewPlatformTransform();
	    canvas3D.addKeyListener(this);
		spotLight.setCapability(SpotLight.ALLOW_POSITION_WRITE);
		TransformGroup light = new TransformGroup();
		light.addChild(spotLight);
		BranchGroup bg = new BranchGroup();
		bg.addChild(light);
	    simpleU.addBranchGraph(bg);
	    

		try
		{	
		long startTime1 = System.currentTimeMillis();
		
//		File inputFile =  new File("/Users/min/Downloads/test/vertex08629");
		File inputFile =  new File("C:\\datset\\vertex08629");
//		File inputFile =  new File("/home/ln/do/vertex08629");

		
		RandomAccessFile f=new RandomAccessFile(inputFile,"rw");
		long fileLength = f.length();
		System.out.println("f.length()..."+f.length());
//		f.seek(fileLength);
		
//		File inputFile2 =  new File("/Users/min/Downloads/test/normal08629");
		File inputFile2 =  new File("C:\\datset\\normal08629");
//		File inputFile2 =  new File("/home/ln/do/normal08629");
		
		RandomAccessFile f2=new RandomAccessFile(inputFile2,"rw");
	 
		long fileLength2 = f2.length();
		System.out.println("f2.length()..."+f2.length());
//		f2.seek(fileLength2); 
//-------------------------------------------------------------------------------
        if (f.length() != 59132880) {
    		Download.downLoadFromUrl("http://134.175.21.96:8080/do/vertex08629"
    				,"vertex08629"
    				,"C:\\datset\\");//static，静态的可以直接用类名调用
    		System.out.println("f.length()..."+f.length());
		}
        if (f2.length() != 59132880) {
    		Download.downLoadFromUrl("http://134.175.21.96:8080/do/normal08629"
    				,"normal08629"
    				,"C:\\datset\\");//static，静态的可以直接用类名调用
    		System.out.println("f2.length()..."+f2.length());
		}
//-------------------------------------------------------------------------------
		
		float[] normal=new float[4927740*3]; //     51505056/4/3=4292088
		float[] vertex=new float[4927740*3]; 
		
		for(int i=0;i<4927740*3 ;i++) 
		{
			vertex[i]=f.readFloat();
		}
		f.close();

		for(int i=0;i<4927740*3;i++)
		{
			normal[i]=f2.readFloat();
		}
		f2.close();

		long startTime2 = System.currentTimeMillis();
		System.out.println("Get vertex and normal:"+(startTime2-startTime1));
        String Str1 = new String();
        Str1 = String.valueOf("Get vertex and normal="+(startTime2-startTime1)+"ms");
        text1.setText(Str1);
        
        
		TriangleArray triangle;
		triangle = new TriangleArray(4927740, TriangleArray.COORDINATES|TriangleArray.NORMALS);
		triangle.setCoordinates(0,vertex,0,4927740);
		triangle.setNormals(0, normal, 0, 4927740);
		Shape3D tri=new Shape3D();
		tri.addGeometry(triangle);
	

		
		BranchGroup objRoot = new BranchGroup();
	    Appearance app = new Appearance();
		
	    // Create the transform group
		TransformGroup transformGroup = new TransformGroup();

		// Create the mouse rotate and zoom behaviour
		MouseRotate rotate = new MouseRotate();
		MouseWheelZoom wheelZoom = new MouseWheelZoom();
		MouseTranslate translate = new MouseTranslate();
		
		app.setPolygonAttributes(new PolygonAttributes());
		app.getPolygonAttributes().setCullFace(PolygonAttributes.CULL_NONE);
		
		DirectionalLight light2 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(0.0f,0.0f,-2.0f));
		light2.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));
		objRoot.addChild(light2);
		
//		DirectionalLight light3 = new DirectionalLight(new Color3f(1.0f,1.0f,1.0f), new Vector3f(1.0f,1.0f,1.0f));//构造并初始化定向光。;//Color3ffloat x，float y， float z)x - 红色值; y - 绿色值; z - 蓝色值       //Vector3f（float x，float y，float z）x - x坐标; y - y坐标; z - z坐标
//		light3.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));//将Light的影响区域设置为指定的边界  //BoundingSphere（Point3d 中心， double 半径）从中心和半径构造并初始化BoundingSphere。
//		objRoot.addChild(light3);
		
		Material mat = new Material();
		mat.setDiffuseColor(new Color3f(1.0f,1.0f,1.0f));
		mat.setAmbientColor(new Color3f(1.0f,1.0f,1.0f));
		mat.setSpecularColor(new Color3f(1.0f,1.0f,1.0f));
		app.setMaterial(mat);

		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rotate.setTransformGroup(transformGroup);
		rotate.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
		wheelZoom.setTransformGroup(transformGroup);
		wheelZoom.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
		translate.setTransformGroup(transformGroup);
		translate.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
		transformGroup.addChild(translate);
		transformGroup.addChild(rotate);
		transformGroup.addChild(wheelZoom);
		objRoot.addChild(transformGroup);

		JPanel buttons = new JPanel();
	    JButton up = new JButton("Up");
	    JButton down = new JButton("Down");
		label.setIcon(new ImageIcon(image));
		label.addMouseListener(new MyListener());
		
//	    up.addActionListener(new UpListener());             
//	    down.addActionListener(new DownListener());   
	    buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
//	    buttons.add(up);
//	    buttons.add(down);
		
		pan2D.setLayout(new BorderLayout());
	    pan2D.add(label, BorderLayout.CENTER);
	    pan2D.add(buttons,BorderLayout.SOUTH);
	    
        // System.out.println("Nnumber "+dim.tri.numGeometries());
         //System.out.println("Nnumber "+dim.tri.getAllGeometries());
			
		tri.setAppearance(app);
		transformGroup.addChild(tri);
		objRoot.compile();
		simpleU.addBranchGraph(objRoot);
		pan.setLayout(new BorderLayout());
	    pan.add(canvas3D, BorderLayout.CENTER);
	    pan.add(pan2D, BorderLayout.EAST);
	    this.setContentPane(pan);
		this.setVisible(true);
		long startTime3 = System.currentTimeMillis();
		System.out.println("Generate 3D model:"+(startTime3-startTime2));
        String Str2 = new String();
        Str2 = String.valueOf("Generate 3D model="+(startTime3-startTime2)+"ms");
      
        text2.setText(Str2);
        
        frame.getContentPane().add(label, BorderLayout.CENTER);
		text1.setBounds(200, 200, 200, 20);
        text2.setBounds(400, 200, 200, 20);
        frame.add(text1);
        frame.add(text2);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        label.setIcon(new ImageIcon(image));

        
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
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
//		    translation.y = 2*slice/512;
		    transform.setTranslation(translation);
		    vtg.setTransform(transform);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		reconstruction();
	}

}
