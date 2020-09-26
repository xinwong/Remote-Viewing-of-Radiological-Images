package viewer1;


import java.awt.image.*;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;

import raycasting.Vector3;

public class viewer1 implements ActionListener, MouseWheelListener {

	double startTime1;
	double startTime3;
    int slice = 225;
    
    JPanel buttons = new JPanel(new FlowLayout()); 
    JPanel buttons1 = new JPanel(new FlowLayout()); 
    JPanel buttons2 = new JPanel(new FlowLayout()); 
    JPanel buttons3 = new JPanel(new FlowLayout());

    JPanel p1=new JPanel(new FlowLayout());
    JPanel p2=new JPanel(new FlowLayout());
    JScrollPane scrollPane;
    
    
    JButton PButton = new JButton("Previous");
    JButton NButton = new JButton("Next");
    
    JButton zoom = new JButton("Zoom_in");
    private boolean flag1 = true;
    JButton zoom1 = new JButton("Zoom_out");
    private boolean flag2 = true;
    
    int window_center;
    int window_width;
//    int Thresholding;
//    int range;
    
//    private JTextField text = new JTextField();
    private JTextField name = new JTextField("",5);
//    private JTextField text1 = new JTextField();
    private JTextField name1 = new JTextField("",5); 
    private JLabel wc = new JLabel("window center:");
    private JLabel ww = new JLabel("window width:"); 
    
    private JButton windowing = new JButton("windowing");
    private boolean flag3 = true; 
    JButton download = new JButton("download");
    JButton edge_detection = new JButton("edge_detection");
    private boolean flag4 = true;
    JButton thresholding = new JButton("thresholding");
    private JTextField threshold = new JTextField("thresholding");
    private boolean flag5 = true;   
    JButton median_filtering = new JButton("median_filtering");
    private boolean flag6 = true;
    
    JButton MIP = new JButton("MIP");
    JButton MinIP = new JButton("MinIP");
    JButton AvgIp = new JButton("AvgIp");
    JButton tools = new JButton("measurement tools");
    private boolean flag7 = true;
    
    JButton volume = new JButton("volume rendering");
    private boolean flag8 = true;
    
    
    private JTextField text = new JTextField();
	String str1[] = {"08629CTC", "22118CTC", "08779CTC"};  
	private JComboBox jcDatasets = new JComboBox(str1);   //下拉列表框来选择哪个数据库
    private JButton fly = new JButton("flythrough");
    private JButton reconstruction = new JButton("reconstruction");





    JLabel label = new JLabel();
    JFrame frame = new JFrame("Slice: " + slice);
    String fileName = "";
    BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
    
    int[] pixelvalue = new int[512 * 512];

    float scale = 1;
    int width;
    int height;
    int depth;
    short[][][] data;

 
    public static void main(String args[]) {
        new viewer1(null);
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    	// TODO Auto-generated method stub
  	    if (e.getWheelRotation()==1) {
  	        nextPic();
            String Str = new String();
            Str = String.valueOf("next="+(startTime3-startTime1)+"ms");
            text.setText(Str);
  	      } 
  	    if (e.getWheelRotation()==-1) {
  	        previewPic();
            String Str = new String();
            Str = String.valueOf("next="+(startTime3-startTime1)+"ms");
            text.setText(Str);
  	     } 
    	
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == PButton) {
            previewPic();
            String Str = new String();
            Str = String.valueOf("pre="+(startTime3-startTime1)+"ms");
            text.setText(Str);
        }
        if (ae.getSource() == NButton) {
            nextPic();
            String Str = new String();
            Str = String.valueOf("next="+(startTime3-startTime1)+"ms");
            text.setText(Str);
        }
        
        if (ae.getSource() == zoom) {
            zoom();
            String Str = new String();
            Str = String.valueOf("zomm in="+(startTime3-startTime1)+"ms");
            text.setText(Str);
        }
        if (ae.getSource() == zoom1) {
            zoom1();
            String Str = new String();
            Str = String.valueOf("zoom out="+(startTime3-startTime1)+"ms");
            text.setText(Str);
        }
        if (ae.getSource() == windowing) {
        	windowing();
            String Str = new String();
            Str = String.valueOf("windowing="+(startTime3-startTime1)+"ms");
            text.setText(Str);
        }
        if (ae.getSource() == download) {
        	download();
            String Str = new String();
            Str = String.valueOf("download="+(startTime3-startTime1)+"ms");
            text.setText(Str);
        }
        if (ae.getSource() == edge_detection) {
            edge_detection();
            String Str = new String();
            Str = String.valueOf("edge detection="+(startTime3-startTime1)+"ms");
            text.setText(Str);
        }
        if (ae.getSource() == thresholding) {
        	thresholding();
            String Str = new String();
            Str = String.valueOf("thresholding="+(startTime3-startTime1)+"ms");
            text.setText(Str);
        }
        if(ae.getSource() == median_filtering) {
        	median_filtering();
            String Str = new String();
            Str = String.valueOf("median filtering="+(startTime3-startTime1)+"ms");
            text.setText(Str);
        }
        if (ae.getSource() == fly) {
        	Desktop desktop = Desktop.getDesktop();
    		String dat = new String();
    		dat = (String) jcDatasets.getSelectedItem();
            System.out.println(dat);
            
            if (dat == "22118CTC") {
        		Thread t1=new Thread(new fly22118());
        		t1.start();   
			}
            else if (dat == "08779CTC") {
        		Thread t2=new Thread(new fly08779());
        		t2.start();    
			}
            else {
        		Thread t3=new Thread(new fly08629());
        		t3.start(); 	
			}
                 	
//        	try {
//				desktop.browse(new URI("http://134.175.21.96:8080/radiologyimageviewer/Java/3d/"+dat+"/flythrough/"));
//			} catch (IOException | URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
        if (ae.getSource() == reconstruction) {
        	Desktop desktop = Desktop.getDesktop();
    		String dat = new String();
    		dat = (String) jcDatasets.getSelectedItem();
            System.out.println(dat);
            
            if (dat == "22118CTC") {
        		Thread t4=new Thread(new reconstruction22118());
        		t4.start();   
			}
            else if (dat == "08779CTC") {
        		Thread t5=new Thread(new reconstruction08779());
        		t5.start();    
			}
            else {
        		Thread t6=new Thread(new reconstruction08629());
        		t6.start(); 	
			}
            
//        	try {
//				desktop.browse(new URI("http://134.175.21.96:8080/radiologyimageviewer/Java/3d/"+dat+"/reconstruction/"));
//			} catch (IOException | URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
        
        if (ae.getSource() == this.MIP) {
        	MIP();
            String Str = new String();
            Str = String.valueOf("MIP=" + (this.startTime3 - this.startTime1) + "ms");
            this.text.setText(Str);
          } 
//        if (ae.getSource() == this.MinIP) {
//        	MinIP();
//            String Str = new String();
//            Str = String.valueOf("MinIP=" + (this.startTime3 - this.startTime1) + "ms");
//            this.text.setText(Str);
//          } 
//        if (ae.getSource() == this.AvgIp) {
//        	AvgIp();
//            String Str = new String();
//            Str = String.valueOf("AvgIp=" + (this.startTime3 - this.startTime1) + "ms");
//            this.text.setText(Str);
//          } 
        if (ae.getSource() == this.tools) {
        	tools();
//            String Str = new String();
//            Str = String.valueOf("tools=" + (this.startTime3 - this.startTime1) + "ms");
//            this.text.setText(Str);
          } 
        if (ae.getSource() == this.volume) {
        	volume();
            String Str = new String();
            Str = String.valueOf("volume=" + (this.startTime3 - this.startTime1) + "ms");
            this.text.setText(Str);
          }       
        
        
    }


    private void picAnalysis(int intSlice) {
        try {
            RandomAccessFile f;
            f = new RandomAccessFile(fileName, "r");
            System.out.println(fileName);
            width = 512;
            height = 512;
            depth = (int) (f.length()/512/512/2);
            System.out.println("Total slices:"+depth);

            f.seek(512 * 512 * slice * 2);
            byte[] bpixels = new byte[width * height * 2];//。dat格式的医学图像的每个像素值是由2个字节表示的
            f.read(bpixels, 0, width * height * 2);

//            int[] pixelvalue = new int[width * height];
            int k = 0;
            for (int j = 0; j < width * height; j++) {
                int intValue = 0;
                if (bpixels[k] >= 0) {
                    intValue += 0x00 << (8 * 3); //计算机运算中：乘除都很耗内存，可用左移、右移来代替，运算速度会快很多
                    intValue += 0x00 << (8 * 2);
                    intValue += (bpixels[k++] & 0xFF) << (8 * 1);
                    intValue += (bpixels[k++] & 0xFF);
                } else {
                    intValue += 0xFF << (8 * 3);
                    intValue += 0xFF << (8 * 2);
                    intValue += (bpixels[k++] & 0xFF) << (8 * 1);
                    intValue += (bpixels[k++] & 0xFF);
                }

                pixelvalue[j] = intValue;
            }

            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            int n = 0;
            for (int z = 0; z < height; z++)
                for (int x = 0; x < width; x++) {
                    int voxel = pixelvalue[n++];
                    if (voxel < min) min = voxel;
                    if (voxel > max) max = voxel;
                }

//   			min = -200;
            System.out.println("Min: " + min + " HU");
            System.out.println("Max: " + max + " HU");
            int range = max - min;

//            int window_center = 20;
//            int window_width = 300;
//            int min1 = window_center - window_width / 2 ;
//            int max1 = window_center + window_width / 2 ;
//            int range1 = max1 - min1;
            
            n = 0;
            for (int z = 0; z < height; z++)
                for (int x = 0; x < width; x++) {
                    int voxel = pixelvalue[n];  //voxel is the original data
                    //System.out.println(voxel);
                    
                    int pixel = ((voxel - (-3024)) * 255) / (6095) ;
                    
//                    System.out.println(pixel);
                    
                    if( pixel > 255) {
                    	pixel = 255;
                    }
                    if( pixel < 0) {
                    	pixel = 0;
                    }

                    pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
                    //System.out.println(pixel);
                    image.setRGB(x, z, pixel);
                    n++;
                }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    private void previewPic() {
//        flag = true;
      if (slice != 0) {
    	if(flag3==false) {
        	picAnalysis(slice--);
        	
//            ImageIcon imageIcon = new ImageIcon(image);

            	window_center = change(name.getText());
            	window_width = change(name1.getText());
            	
                int min1 = window_center - window_width / 2 ;
                int max1 = window_center + window_width / 2 ;
                double dFactor = 255.0/(double) (max1 - min1);
                
                int n = 0;
                for (int z = 0; z < 512; z++)
                    for (int x = 0; x < 512; x++) {
                        int voxel = pixelvalue[n];  //voxel is the original data
                        //System.out.println(voxel);
                        
                        int pixel = (int)((voxel - min1) * dFactor) ;
                        
                        //System.out.println(pixel);
                        
                        if( pixel > 255) {
                        	pixel = 255;
                        }
                        if( pixel < 0) {
                        	pixel = 0;
                        }

                        pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
                        //System.out.println(pixel);
                        image.setRGB(x, z, pixel);
                        n++;
                        
                    }  
        	    label.setIcon(new ImageIcon(image));  //ImageIcon创建图片对象;但是我们一般用的就是setIcon(Icon)，其中Icon类是接口，无法创建实例，所以这里的Icon一般用实现了Icon接口的ImageIcon类来代替
                frame.setTitle("Slice : " + Integer.toString(slice));
    	}
    	else {
				startTime1 = System.currentTimeMillis();
	            picAnalysis(slice--);
	            label.setIcon(new ImageIcon(image));  //ImageIcon创建图片对象;但是我们一般用的就是setIcon(Icon)，其中Icon类是接口，无法创建实例，所以这里的Icon一般用实现了Icon接口的ImageIcon类来代替
	            frame.setTitle("Slice : " + Integer.toString(slice));
				startTime3 = System.currentTimeMillis();
				System.out.println(startTime3-startTime1+"ms");
   
    	}
      }
    }

    private void nextPic() {
      if (slice != depth) {	 	
    	if(flag3==false) {
        	picAnalysis(slice++);
        	
//            ImageIcon imageIcon = new ImageIcon(image);

            	window_center = change(name.getText());
            	window_width = change(name1.getText());
            	
                int min1 = window_center - window_width / 2 ;
                int max1 = window_center + window_width / 2 ;
                double dFactor = 255.0/(double) (max1 - min1);
                
                int n = 0;
                for (int z = 0; z < 512; z++)
                    for (int x = 0; x < 512; x++) {
                        int voxel = pixelvalue[n];  //voxel is the original data
                        //System.out.println(voxel);
                        
                        int pixel = (int)((voxel - min1) * dFactor) ;
                        
                        //System.out.println(pixel);
                        
                        if( pixel > 255) {
                        	pixel = 255;
                        }
                        if( pixel < 0) {
                        	pixel = 0;
                        }

                        pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
                        //System.out.println(pixel);
                        image.setRGB(x, z, pixel);
                        n++;
                        
                    }  
        	    label.setIcon(new ImageIcon(image));  //ImageIcon创建图片对象;但是我们一般用的就是setIcon(Icon)，其中Icon类是接口，无法创建实例，所以这里的Icon一般用实现了Icon接口的ImageIcon类来代替
                frame.setTitle("Slice : " + Integer.toString(slice));

    	}   	
    	
//        flag = true;
    	else {
			startTime1 = System.currentTimeMillis();
            picAnalysis(slice++);
            label.setIcon(new ImageIcon(image));
            frame.setTitle("Slice : " + Integer.toString(slice));
            startTime3 = System.currentTimeMillis();
			System.out.println(startTime3-startTime1+"ms");
            
        }
      }
    }


    public viewer1(String num) {
    	
    	String system = System.getProperty("os.name");  
    	System.out.println("System:"+system);  
    	
//    	if(system.equals("Windows 10")) 
    	
    	if(system.equals("Linux")) {
    		if (num != null) {
            	fileName = "/home/hsin/data/"+num+".dat";
            	System.out.println("Current Data Set:" + num);

    		}else {
    	    	fileName = "/home/hsin/data/08629CTC.dat";
    		}
    	}
    	else if(system.equals("Mac OS X")) {
    		if (num != null) {
            	fileName = "/Users/min/Downloads/"+num+".dat";
            	System.out.println("Current Data Set:" + num);

    		}else {
    	    	fileName = "/Users/min/Downloads/08629CTC.dat";
    		}
    	}     	
    	else{
        	if (num != null) {
            	fileName = "C:\\datset\\"+num+".dat";
            	System.out.println("Current Data Set:" + num);

    		}else {
    	    	fileName = "C:\\datset\\08629CTC.dat";
    		}   				
    	}
 

  
//    	fileName = "/Users/min/Downloads/test/08629CTC.dat";
//    	fileName = "/home/ln/do/08629CTC.dat";
        picAnalysis(slice);
        frame.addMouseWheelListener(this);

        label.setIcon(new ImageIcon(image));
        PButton.addActionListener(this);
        NButton.addActionListener(this);
        zoom.addActionListener(this);
        zoom1.addActionListener(this);
        windowing.addActionListener(this);
        download.addActionListener(this);
        edge_detection.addActionListener(this);
        thresholding.addActionListener(this);
        median_filtering.addActionListener(this);
        jcDatasets.addActionListener(this);
        fly.addActionListener(this);
        reconstruction.addActionListener(this);
        
        this.MIP.addActionListener(this);
        this.MinIP.addActionListener(this);
        this.AvgIp.addActionListener(this);
        this.tools.addActionListener(this);
        this.volume.addActionListener(this);
        
        buttons.add(PButton);
        buttons.add(NButton);
        buttons.add(zoom);
        buttons.add(zoom1);
        buttons.add(windowing);
//        buttons.add(edge_detection);
//        buttons.add(thresholding);
//        buttons.add(median_filtering);
//        buttons.add(download);
        buttons2.add(download);
        buttons2.add(jcDatasets);
        buttons2.add(fly);
        buttons2.add(reconstruction);

        buttons1.add(wc);
        buttons1.add(name);
        buttons1.add(ww);
        buttons1.add(name1);
        
        this.buttons3.add(this.MIP);
//      this.buttons3.add(this.MinIP);
//      this.buttons3.add(this.AvgIp);
        this.buttons3.add(this.tools);
        this.buttons3.add(this.volume);
        this.label.addMouseListener(rayListener);
        
        
//        name.setBounds(130, 600, 100, 20);
//        name1.setBounds(360, 600, 100, 20);
//        wc.setBounds(40, 600, 90, 20);
//        ww.setBounds(270, 600, 90, 20);
//        threshold.setBounds(40, 6200, 200, 20);
        text.setBounds(600, 600, 200, 20);

//        text.setBounds(40, 600, 200, 20);
//        text1.setBounds(240, 600, 200, 20);
        
		p1.setLayout(new BorderLayout());
        p1.add(buttons,BorderLayout.NORTH);
        p1.add(buttons1,BorderLayout.SOUTH);
        this.p1.add(this.buttons3, "Center");

        
        p2.add(label);
        scrollPane = new JScrollPane(p2);
        scrollPane.setPreferredSize(new Dimension(525,525));
        
        frame.add(text);
//        frame.add(name);
//        frame.add(name1);
//        frame.add(wc);
//        frame.add(ww);
//        frame.add(threshold);
//        frame.add(text);
//        frame.add(text1);

//        windowing.addActionListener(new ButtonListener(name,text,name1,text1,windowing));   
        
        
        frame.setLayout(new BorderLayout()); //setLayout是对当前组件设置为流式布局.组件在窗体中从左到右依次排列 如果排到行的末尾 换行排列 排列会随着窗体的大小而改变
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(p1, BorderLayout.SOUTH);
        frame.getContentPane().add(buttons2, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    
        
    }

    private void zoom() {
//        picAnalysis(slice);
		startTime1 = System.currentTimeMillis();
        ImageIcon imageIcon = new ImageIcon(image);// 创建图片对象
        scale -= 0.1;

        imageIcon.setImage(imageIcon.getImage().getScaledInstance((int)(scale * width), (int)(scale * height), Image.SCALE_DEFAULT));

        label.setIcon(imageIcon);
        frame.setTitle("Slice : " + Integer.toString(slice));
        startTime3 = System.currentTimeMillis();
		System.out.println(startTime3-startTime1+"ms");
    }

    private void zoom1() {
//        picAnalysis(slice);
		startTime1 = System.currentTimeMillis();
        ImageIcon imageIcon = new ImageIcon(image);
        scale += 0.1;
        imageIcon.setImage(imageIcon.getImage().getScaledInstance((int)(scale * width), (int)(scale * height), Image.SCALE_DEFAULT));
        label.setIcon(imageIcon);
        frame.setTitle("Slice : " + Integer.toString(slice));
        startTime3 = System.currentTimeMillis();
     	System.out.println(startTime3-startTime1+"ms");
    }
    
    private int change( String str ) {
    	int a = 0;
    	try {
    		a = Integer.parseInt(str);
    	}catch(NumberFormatException e) {
    		e.printStackTrace();
    	}
    	return a;
    }
    
    private void windowing() {
		startTime1 = System.currentTimeMillis();

    	picAnalysis(slice);
    	
        ImageIcon imageIcon = new ImageIcon(image);
        if (flag3) {
        
        	window_center = change(name.getText());
        	window_width = change(name1.getText());
        	
            int min1 = window_center - window_width / 2 ;
            int max1 = window_center + window_width / 2 ;
            double dFactor = 255.0/(double) (max1 - min1);
            
            int n = 0;
            for (int z = 0; z < 512; z++)
                for (int x = 0; x < 512; x++) {
                    int voxel = pixelvalue[n];  //voxel is the original data
                    //System.out.println(voxel);
                    
                    int pixel = (int)((voxel - min1) * dFactor) ;
                    
                    //System.out.println(pixel);
                    
                    if( pixel > 255) {
                    	pixel = 255;
                    }
                    if( pixel < 0) {
                    	pixel = 0;
                    }

                    pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
                    //System.out.println(pixel);
                    image.setRGB(x, z, pixel);
                    n++;
                    
                }
            flag3 = false;
            startTime3 = System.currentTimeMillis();
         	System.out.println(startTime3-startTime1+"ms");
        } else {
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(512, 512, Image.SCALE_DEFAULT));
            flag3 = true;
        }
        label.setIcon(imageIcon);
    }
    

    private void download() {
		startTime1 = System.currentTimeMillis();
		String dat = new String();
		dat = (String) jcDatasets.getSelectedItem();
		
//		String data_name = dat.substring(0,5);
        System.out.println(dat);

//        Download dl = new Download();
    	try{
    		startTime1 = System.currentTimeMillis();
//    		Download.downLoadFromUrl("http://192.168.1.110:8080/do/22118CTC"
//					,"normal08629"
//					,"/home/ln/do/");
    		
    		
//    		Download.downLoadFromUrl("http://192.168.0.53:8080//do/normal"+data_name
//									,"normal"+data_name
//									,"/home/hsin/data/");
//    		
//    		Download.downLoadFromUrl("http://192.168.0.53:8080//do/vertex"+data_name
//					,"vertex"+data_name
//					,"/home/hsin/data/");

//    		
			Download.downLoadFromUrl("http://134.175.21.96:8080/do/"+dat+".dat"
									,dat+".dat"
									,"C:\\datset\\");//static，静态的可以直接用类名调用
    		
    		
			startTime3 = System.currentTimeMillis();
			System.out.println(((startTime3-startTime1)/1000)+"s");
		}catch (Exception e) {
		}
        new viewer1(dat);	
    	
    }
    
    private void thresholding() {
		startTime1 = System.currentTimeMillis();

    	picAnalysis(slice);

        ImageIcon imageIcon = new ImageIcon(image);
        if (flag5) {
        
        	int thresh = change(threshold.getText());	
 
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            
            int n = 0;
            for (int z = 0; z < 512; z++)
                for (int x = 0; x < 512; x++) {
                    int voxel = pixelvalue[n++];
                    if (voxel < min) min = voxel;
                    if (voxel > max) max = voxel;
                }

            int range = max - min;


            n = 0;
            for (int z = 0; z < 512; z++)
                for (int x = 0; x < 512; x++) {
                    int voxel = pixelvalue[n];  //voxel is the original data
                    //System.out.println(voxel);
                    
                    int pixel = ((voxel - min) * 255) / range ;
                    
//                    System.out.println(pixel);
                    
                    if( pixel > thresh) {
                    	pixel = 255;
                    }
                    else
                    	pixel = 0;
                   
                    pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
                    //System.out.println(pixel);
                    image.setRGB(x, z, pixel);
                    n++;
                }
        	System.out.println("thresholding:"+thresh);
        	flag5 = false;
			startTime3 = System.currentTimeMillis();
			System.out.println(startTime3-startTime1+"ms");

        } else {
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(512, 512, Image.SCALE_DEFAULT));
            flag5 = true;
        }
        label.setIcon(imageIcon);
    	
    }
    
    private void edge_detection() {
		startTime1 = System.currentTimeMillis();

    	picAnalysis(slice);
    	int gx = 0;
    	int gy = 0;
    	int[] sobel1 = new int[512*512];
    	int[] sobel2 = new int[512*512];
        ImageIcon imageIcon = new ImageIcon(image);
        if (flag4) {
        	int count = 0;

        	for(int x = 1 ;x<511 ;x++) 
        		for(int y = 1; y<511 ; y++) {

        			gx=Math.abs(pixelvalue[x+1+(y-1)*512]+2*pixelvalue[x+1+y*512]+pixelvalue[x+1+(y+1)*512]-pixelvalue[x-1+(y-1)*512]-2*pixelvalue[x-1+y*512]-pixelvalue[x-1+(y+1)*512]);
        			gy=Math.abs(pixelvalue[x-1+(y+1)*512]+2*pixelvalue[x+(y+1)*512]+pixelvalue[x+1+(y+1)*512]-pixelvalue[x-1+(y-1)*512]-2*pixelvalue[x+(y-1)*512]-pixelvalue[x+1+(y-1)*512]);
        			sobel1[count] = gx;
//        			System.out.println(((x-1)*512+y)+":"+sobel1[count]);
        			sobel2[count] = gy;
//        			System.out.print(count+":");
//        			System.out.println(sobel2[count]);
        			count++;
        		}
        		
        	int min1 = Integer.MAX_VALUE;
        	int max1 = Integer.MIN_VALUE;
        	int min2 = Integer.MAX_VALUE;
        	int max2 = Integer.MIN_VALUE;
        	
        	for(int i=0;i<sobel1.length;i++){
        		if(min1>sobel1[i]) min1=sobel1[i];
        		if(max1<sobel1[i]) max1=sobel1[i];
        		}

        	for(int i=0;i<sobel2.length;i++){
        		if(min2>sobel2[i]) min2=sobel2[i];
        		if(max2<sobel2[i]) max2=sobel2[i];
        		}
        	
//        	for(int j=0;j<sobel2.length;j++){
//        		sobel1[j]=((sobel1[j]-min1)/(max1-min1))*255;
//        		sobel2[j]=((sobel2[j]-min2)/(max2-min2))*255;
//        		}
        	
        	int n = 0;
        	for (int z = 1; z < 511; z++)
        		for (int x = 1; x < 511; x++) {
        			int voxel1=((sobel1[n]-min1)*255)/(max1-min1);
            		int voxel2=((sobel2[n]-min2)*255)/(max2-min2);
        			int pixel = voxel1 + voxel2;
        			
        			pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel; 
        			image.setRGB(z, x, pixel); 
        			n++;
        		}
        	
        	flag4 = false;
			startTime3 = System.currentTimeMillis();
			System.out.println(startTime3-startTime1+"ms");
        }
        else {
        	imageIcon.setImage(imageIcon.getImage().getScaledInstance(512, 512, Image.SCALE_DEFAULT));
            flag4 = true;
        }
        label.setIcon(imageIcon);

    	
    }

    private void median_filtering() {
		startTime1 = System.currentTimeMillis();

    	picAnalysis(slice);

        ImageIcon imageIcon = new ImageIcon(image);
        if (flag6) {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            
            int n = 0;
            for (int z = 0; z < 512; z++)
                for (int x = 0; x < 512; x++) {
                    int voxel = pixelvalue[n++];
                    if (voxel < min) min = voxel;
                    if (voxel > max) max = voxel;
                }

            int range = max - min;

            n = 0;
            int[][] p = new int[512][512];
            for (int i = 0; i < 512; i++)
                for (int j = 0; j < 512; j++) {
                    int voxel = pixelvalue[n];  //voxel is the original data
                    //System.out.println(voxel);
                    p[i][j] = ((voxel - min) * 255) / range ;
                    n++;  
                }
                    
//                    System.out.println(pixel);
//                    
//                    pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
//                    //System.out.println(pixel);
//                    image.setRGB(x, z, pixel);
//                    n++;
//                }
            int pixel;
            for (int z = 0; z < 512; z++)
                for (int x = 0; x < 512; x++) {
                	if( (z!=0) && (z!=511) && (x!=0) && (x!=511) ) {
                		pixel = ( p[z-1][x-1] + p[z-1][x] + p[z-1][x+1] + p[z][x-1] + p[z][x] + p[z][x+1] + p[z+1][x-1] + p[z+1][x] + p[z+1][x+1] )/9;
                		pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
                        //System.out.println(pixel);
                        image.setRGB(x, z, pixel);
                	}
                	else {
                		pixel = p[z][x];
                        pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
                        //System.out.println(pixel);
                        image.setRGB(x, z, pixel);
                	}
    
                }
        	flag6 = false;
			startTime3 = System.currentTimeMillis();
			System.out.println(startTime3-startTime1+"ms");

        } else {
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(512, 512, Image.SCALE_DEFAULT));
            flag6 = true;
        }
        label.setIcon(imageIcon);
    	
    }
    
    public void dataset() {
//  	  int[][][] data = new int[depth][width][height];
  	  
  	  
  		try{
  			RandomAccessFile f;
  			f=new RandomAccessFile(fileName,"r");
  			byte[] bpixels=new byte[depth*width*height*2];
  			f.read(bpixels);
  			data=new short[depth][width][height];
  			int k=0;
  			for(int y=0; y<depth; y++)
  				for(int z=0; z<512; z++)
  					for(int x=0; x<512; x++)
  			{
  				int intValue=0;
     				if(bpixels[k]>=0)
     				{
     				    intValue +=0x00<<(8*3);  
     				    intValue +=0x00<<(8*2); 
     				    intValue +=(bpixels[k++] & 0xFF)<<(8*1);
     				    intValue +=(bpixels[k++] & 0xFF);
     				}
     				else
     				{
     					intValue +=0xFF<<(8*3);  
     				    intValue +=0xFF<<(8*2);
     				    intValue +=(bpixels[k++] & 0xFF)<<(8*1);
     				    intValue +=(bpixels[k++] & 0xFF);
     				}
     				
     				data[y][z][x]=(short) intValue;
     			}
  		 bpixels=null;
  		 f.close();
  		}
  		catch(Exception e)
  		{
  			System.out.println(e.toString());
  		}
  		
  		
    }
    
    
    public void MIP() {
  	  
//  	    picAnalysis(this.slice);
  	    BufferedImage image_mip = new BufferedImage(512, depth, 2);

  		ImageIcon imageIcon = new ImageIcon(image_mip);
  		dataset();

		startTime1 = System.currentTimeMillis();
		  
  				for(int y=0; y<depth; y++)
  					for(int z=0; z<height; z++)
  						for(int x=0; x<width; x++) 
  						{
  							if(data[y][z][x]<-1024){
  								data[y][z][x] = -1024;
  							}  //mip 只投影骨头		
  						}	
  				
  				  int[][] mip_data = new int[depth][width];
  				  for(int y=0; y<depth; y++)
  					  for(int x=0; x<width; x++) {
  						  int blank = Integer.MIN_VALUE;
  						  for(int z=0; z<height; z++){
  							  if(data[y][z][x]>blank) {
  								  blank = data[y][z][x];
  							  }
  						  }
  						  mip_data[y][x]=blank;  
  					  }
  				  
//  				  for(int i=0; i<512; i++)	 
//  					  System.out.println(mip_data[200][i]);

  				
  //----------------------------------------------------------				
  				int min = Integer.MAX_VALUE;
  				int max = Integer.MIN_VALUE;
  				
  				for(int y=0; y<depth; y++)
  						for(int x=0; x<512; x++)
  						{
  							if(mip_data[y][x] < min) min = mip_data[y][x];
  							if(mip_data[y][x] > max) max = mip_data[y][x];
  						}	
//  				System.out.println(max);
//  				System.out.println(min);
  				int range = max - min;
  				for(int y=0; y<depth; y++)
  						for(int x=0; x<width; x++) 
  						{
  							int pixel = ((mip_data[y][x]-min) * 255)/range;		
  							mip_data[y][x] = (short) pixel;			
  						}
  				
  //-------------------------------------------------------
  			  
  				for(int y=0; y<depth; y++)
  					for(int x=0; x<width; x++) 
  					{
  						int pixel = mip_data[y][x];
  						pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel; 
  						image_mip.setRGB(x,y,pixel);
  					}

  				startTime3 = System.currentTimeMillis();
  				System.out.println( (startTime3-startTime1) +"ms" );	

  		JPanel p_mip=new JPanel(new FlowLayout());
  	    JLabel l_mip = new JLabel();
  	    JFrame f_mip = new JFrame("mip");

  	    p_mip.add(l_mip);
  	    f_mip.setLayout(new BorderLayout()); //setLayout是对当前组件设置为流式布局.组件在窗体中从左到右依次排列 如果排到行的末尾 换行排列 排列会随着窗体的大小而改变
  	    f_mip.getContentPane().add(p_mip, BorderLayout.CENTER);
  	    f_mip.pack();
  	    f_mip.setVisible(true);
  	    f_mip.setSize(530,depth+45);
  	    f_mip.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  		
  	    l_mip.setIcon(imageIcon);
    }
    
    public void MinIP() {
  	    this.startTime1 = System.currentTimeMillis();
//  	    picAnalysis(this.slice);
  	    BufferedImage image_minip = new BufferedImage(512, depth, 2);

  		ImageIcon imageIcon = new ImageIcon(image_minip);

  			  dataset();
  			  
  			  //
  			    this.startTime1 = System.currentTimeMillis();

  				for(int y=0; y<depth; y++)
  					for(int z=0; z<height; z++)
  						for(int x=0; x<width; x++) 
  						{
  							if(data[y][z][x] < 1000){
  								data[y][z][x] = 3071;
  							}  //minip 只投影lung		
  						}			  
  				
  				  int[][] minip_data = new int[depth][width];
  				  for(int y=0; y<depth; y++)
  					  for(int x=0; x<width; x++) {
  						  int blank = Integer.MAX_VALUE;
  						  for(int z=0; z<height; z++){
  							  if(data[y][z][x]<blank) {
  								  blank = data[y][z][x];
  							  }
  						  }
  						  minip_data[y][x]=blank;  
  					  }
  			  
  				int min = Integer.MAX_VALUE;
  				int max = Integer.MIN_VALUE;
  				
  				for(int y=0; y<depth; y++)
  						for(int x=0; x<512; x++)
  						{
  							if(minip_data[y][x] < min) min = minip_data[y][x];
  							if(minip_data[y][x] > max) max = minip_data[y][x];
  						}	
//  				System.out.println(max);
  				int range = max - min;
  				for(int y=0; y<depth; y++)
  						for(int x=0; x<width; x++) 
  						{
  							int pixel = ((minip_data[y][x]-min) * 255)/range;		
  							minip_data[y][x] = (short) pixel;
  			
  						}
  			  //
  			  

  				
  				for(int y=0; y<depth; y++)
  					for(int x=0; x<width; x++) 
  					{
  						int pixel = minip_data[y][x];
  						pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel; 
  						image_minip.setRGB(x,y,pixel);
  					}

  		JPanel p_minip=new JPanel(new FlowLayout());
  	    JLabel l_minip = new JLabel();
  	    JFrame f_minip = new JFrame("minip");

  	    p_minip.add(l_minip);
  	    f_minip.setLayout(new BorderLayout()); //setLayout是对当前组件设置为流式布局.组件在窗体中从左到右依次排列 如果排到行的末尾 换行排列 排列会随着窗体的大小而改变
  	    f_minip.getContentPane().add(p_minip, BorderLayout.CENTER);
  	    f_minip.pack();
  	    f_minip.setVisible(true);
  	    f_minip.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  		
  	    l_minip.setIcon(imageIcon);
  		
  }
    
    public void AvgIp() {
  	    this.startTime1 = System.currentTimeMillis();
//  	    picAnalysis(this.slice);
  	    BufferedImage image_avgip = new BufferedImage(512, depth, 2);
  		ImageIcon imageIcon = new ImageIcon(image_avgip);

  		dataset();
  			  
  			  //
  			  int[][] avgip_data = new int[depth][width];
  			  for(int y=0; y<depth; y++)
  				  for(int x=0; x<width; x++) {
  					  int blank = 0;
  					  for(int z=0; z<height; z++){
  						  blank = blank + data[y][z][x];

  					  }
  					  avgip_data[y][x]=blank / height;  
  				  }
  			  
  			  //
  				int min = Integer.MAX_VALUE;
  				int max = Integer.MIN_VALUE;
  				
  				for(int y=0; y<depth; y++)
  						for(int x=0; x<512; x++)
  						{
  							if(avgip_data[y][x] < min) min = avgip_data[y][x];
  							if(avgip_data[y][x] > max) max = avgip_data[y][x];
  						}	
//  				System.out.println(max);
  				int range = max - min;
  				for(int y=0; y<depth; y++)
  						for(int x=0; x<width; x++) 
  						{
  							int pixel = ((avgip_data[y][x]-min) * 255)/range;
  							avgip_data[y][x] = (short) pixel;
  			
  						}


  				for(int y=0; y<depth; y++)
  					for(int x=0; x<width; x++) 
  					{
  						int pixel = avgip_data[y][x];
  						pixel = 0xff000000 | pixel << 16 | pixel << 8 | pixel; 
  						image_avgip.setRGB(x,y,pixel);
  					}

  		
  		JPanel p_avgip=new JPanel(new FlowLayout());
  	    JLabel l_avgip = new JLabel();
  	    JFrame f_avgip = new JFrame("avgip");

  	    p_avgip.add(l_avgip);
  	    f_avgip.setLayout(new BorderLayout()); //setLayout是对当前组件设置为流式布局.组件在窗体中从左到右依次排列 如果排到行的末尾 换行排列 排列会随着窗体的大小而改变
  	    f_avgip.getContentPane().add(p_avgip, BorderLayout.CENTER);
  	    f_avgip.pack();
  	    f_avgip.setVisible(true);
  	    f_avgip.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  		
  	    l_avgip.setIcon(imageIcon);
  }
    

  	private class DrawListener extends MouseInputAdapter {

  	    public void mousePressed(MouseEvent ee) {
  	    	if(clicknum==0) {
  		        x1 = ee.getX();
  		        y1 = ee.getY();
  		        clicknum++;
  	    	}
  	    	else
  	    		tools();
  	    }    
  		public void mouseReleased(MouseEvent e) {
  			x2=e.getX();
  			y2=e.getY();
  	        System.out.println("x1:"+x1+" y1:"+y1+" x2:"+x2+" y2:"+y2);
  			double distance = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
  			System.out.println(distance);
  			d=String.format("%.2f", distance);
  		}

  	}  
      
  	DrawListener tool_listener =new DrawListener();
  	int x1,x2,y1,y2,clicknum;
  	String d;
  	
  	public void tools() {
  		if(flag7==true) {
  			this.label.addMouseListener(tool_listener);
  			flag7=false;	
  			clicknum=0;
  		}
  		else {
  			this.label.removeMouseListener(tool_listener);
  			flag7=true;
  			
  			long startTime_tool1 = System.currentTimeMillis();

  			Graphics2D g2d=(Graphics2D)scrollPane.getGraphics();
  		    Line2D line = new Line2D.Double(x1,y1,x2,y2);
  		    g2d.setColor(Color.RED);
  		    g2d.draw(line);
  		    g2d.drawString(d, x1, y1);
  		    
  			long startTime_tool2 = System.currentTimeMillis();
			System.out.println( (startTime_tool2-startTime_tool1) +"ms" );	

  		}
  		
  	}
  	
  	public static Vector3 eye = new Vector3(-1000,50,50); // eye position
  	rayListener rayListener =new rayListener();
  	int u1=390;
  	int u2=250;
  	int u3=slice;
	JPanel p_ray=new JPanel(new FlowLayout());
    JLabel l_ray = new JLabel();
    JFrame f_ray = new JFrame("raycasting");
  	public void volume() {
  			// TODO Auto-generated method stub
		startTime1 = System.currentTimeMillis();

  		  dataset();
//  		  Raycasting.main(data);

			
  		  if(u1<50)
  			  u1=50;
  		  else if(u1>462)
  			  u1=462;
  		  else if(u2<50)
  			  u2=50;
  		  else if(u2>462)
  			  u2=462;
  		  
			Graphics2D g2d=(Graphics2D)scrollPane.getGraphics();
  		    Line2D line1 = new Line2D.Double(u1-50,u2-50,u1-50,u2+50);
  		    Line2D line2 = new Line2D.Double(u1-50,u2-50,u1+50,u2-50);
  		    Line2D line3 = new Line2D.Double(u1+50,u2+50,u1-50,u2+50);
  		    Line2D line4 = new Line2D.Double(u1+50,u2-50,u1+50,u2+50);

  		    g2d.setColor(Color.RED);
  		    g2d.draw(line1);
  		    g2d.draw(line2);
  		    g2d.draw(line3);
  		    g2d.draw(line4);
  		  
  			int h = 200;
  			int w = 200;
  			int d = 200;
  			int[][][] raydata = new int[100][100][100];
  			double[][] ct = new double[h][w];

  			int min = Integer.MAX_VALUE;
  			int max = Integer.MIN_VALUE;
  			
  			for(int y=u3; y<u3+100; y++)
  				for (int z = u2-50; z < u2+50; z++) 
  					for (int x = u1-50; x < u1+50; x++) {
  						raydata[y-u3][z-u2+50][x-u1+50] = data[y][z][x];
  						if(data[y][z][x] < min) min = data[y][z][x];
  						if(data[y][z][x] > max) max = data[y][z][x];
  					}
  			System.out.println(max);
  			System.out.println(min);
  			
  	  		long startTime_ray = System.currentTimeMillis();

  	  		
  			int range = max - min;
  			for(int y=0; y<100; y++)
  				for (int z = 0; z < 100; z++) 
  					for (int x = 0; x < 100; x++) {
  						raydata[y][z][x] = (raydata[y][z][x]-min) * 255 / range;
  					}
  			
  			BufferedImage raycasting_img = new BufferedImage(w, h, 2);		
  			ImageIcon imageIcon = new ImageIcon(raycasting_img);


//  			Cube8 cube8 = new Cube8(new Vector3(0,0,0), 100);

  			for(int i = 0; i<w; i++)
  				for(int j = 0; j<h; j++) {
  					Vector3 datapos = new Vector3(0,(float)(j)/2,(float)(i)/2);
  					
  					ct[i][j] = Composite(datapos, raydata);
  					
//  					if(ct[i][j]<1 && i<199 && j<199 && i>5 && j>5){
//  						System.out.println("i:"+i+" j:"+j+" color:"+ct[i][j]);
//  					}
  				}
  			
  			 //smooth
  			 for(int i=1;i<200-1;i++)
  				 for (int j=1;j<200-1;j++)
  				{  
  					 double grey1= ct[i-1][j-1];
  					 double grey2= ct[i-1][j];
  					 double grey3= ct[i-1][j+1];
  					 double grey4= ct[i][j-1];			 
  					 double grey5= ct[i][j];
  					 double grey6= ct[i][j+1];
  					 double grey7= ct[i+1][j-1];
  					 double grey8= ct[i+1][j];
  					 double grey9= ct[i+1][j+1];
  					 double averageGrey=(grey1+grey2+grey3+grey4+grey5+grey6+grey7+grey8+grey9)/9;
  					 ct[i][j]=averageGrey;
  				}

  			for(int i = 0; i<w; i++)
  				for(int j = 0; j<h; j++) {
  					int color = (int)ct[i][j];
  					if(color<0)
  						System.out.println(color);
  					if(color>255)
  						System.out.println(color);					
  						
  					if (color > 255)
  						color = 255; 
  		            if (color < 0)
  		            	color = 0; 

  		            color = 0xFF000000 | color << 16 | color << 8 | color;
  					raycasting_img.setRGB(i,j,color);
  					
  				}
			startTime3 = System.currentTimeMillis();
			System.out.println( "data+ray time:"+(startTime3-startTime1) +"ms" );	
		
			System.out.println( "ray time:"+(startTime_ray-startTime1) +"ms" );	


  		    
  		    p_ray.add(l_ray);
  		    f_ray.setLayout(new BorderLayout()); //setLayout是对当前组件设置为流式布局.组件在窗体中从左到右依次排列 如果排到行的末尾 换行排列 排列会随着窗体的大小而改变
  		    f_ray.getContentPane().add(p_ray, BorderLayout.CENTER);
  		    f_ray.pack();
  		    f_ray.setVisible(true); 		 
  		    f_ray.setSize(218,245);
  		    f_ray.addKeyListener(rayListener);
  		    f_ray.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 			
  		    l_ray.setIcon(imageIcon);
  	}	
  		private static double Composite(Vector3 datapos, int[][][] raydata) {
  			// TODO Auto-generated method stub
  			double[] samplepos = new double[] {datapos.getX(), datapos.getY(), datapos.getZ()};
//  			double cumcolor = 0;
  			double cumalpha = 0;
  			double alpha ; //透明度
  			double stepsize = 1;//采样步长
  			double samplecolor;
  			
  			double C_out=0;
  			double C_in=0;
  			
  			Vector3 dir = new Vector3(datapos.getX()-eye.getX(), datapos.getY()-eye.getY(), datapos.getZ()-eye.getZ());
  			dir = dir.normalize();


//  			System.out.println("1:"+datapos.getX()+" 2:"+datapos.getY()+" 3:"+datapos.getZ()+" 透明度："+cumalpha); 

  			while((cumalpha<1) && (samplepos[0]<100) && (samplepos[1]<100) && (samplepos[2]<100) && (samplepos[0]>=0) && (samplepos[1]>=0) && (samplepos[2]>=0)) {
  				samplecolor = TrInterpolation(samplepos, raydata);//三线性插值获得采样点处的颜色及不透明度
//  				System.out.println(samplecolor); 
//  				System.out.println(raydata[(int) Math.floor(samplepos[0])][(int) Math.floor(samplepos[1])][(int) Math.floor(samplepos[2])]); 
//  				System.out.println("................"); 


  				//下一个采样点
  				samplepos[0]+=dir.getX()*stepsize;
  				samplepos[1]+=dir.getY()*stepsize;
  				samplepos[2]+=dir.getZ()*stepsize;
  				 if(samplecolor<7 || samplecolor==0) // density=0;
  					{
//  						System.out.println(samplecolor);
  						continue;
  					}
  				//合成颜色及不透明度,采用的是从前到后的合成公式
  				alpha = samplecolor/255;
  				

  				C_out = C_in*(alpha) + samplecolor*(1-alpha);		
  				if(C_out>=255){
  					System.out.println(C_out);;
  					return C_in;
  					}				
  				C_in = C_out;

//  				cumcolor = cumcolor+samplecolor*alpha*(1-cumalpha); 
  				cumalpha = cumalpha+alpha;

//  				//下一个采样点
//  				samplepos[0]+=dir.getX()*stepsize;
//  				samplepos[1]+=dir.getY()*stepsize;
//  				samplepos[2]+=dir.getZ()*stepsize;
//  				System.out.println(samplepos[0]); 
//  				System.out.println(samplepos[1]); 
//  				System.out.println(samplepos[2]); 
//  				System.out.println(C_out); 
//  				System.out.println(".........."); 
  				
  			}
//  			System.out.println("1:"+datapos.getX()+" 2:"+datapos.getY()+" 3:"+datapos.getZ()+" color："+C_in+" 透明度:"+cumalpha); 

//  			System.out.println(cumalpha); 


  			return C_out;
  		}

  		private static double TrInterpolation(double[] samplepos, int[][][] raydata) {
  			// TODO Auto-generated method stub
//  			System.out.println(samplepos[0]); 
//  			System.out.println(samplepos[1]); 
//  			System.out.println(samplepos[2]); 
//  			System.out.println("--------------------"); 

  			int x0, y0, z0, x1, y1 ,z1;
  			double fx, fy, fz;
  			
  			x0=(int) Math.floor(samplepos[0]);//整数部分
  			y0=(int) Math.floor(samplepos[1]);
  			z0=(int) Math.floor(samplepos[2]);
  			fx=samplepos[0]-x0;//小数部分
  			fy=samplepos[1]-y0;
  			fz=samplepos[2]-z0;
  			
  			if(x0==99 || y0==99 || z0==99){
  				x1=x0;
  				y1=y0;
  				z1=z0;
  			}
  			else{
  				x1=x0+1;
  				y1=y0+1;
  				z1=z0+1;
  			}
//  			System.out.println("x0:"+x0+" y0:"+y0+" z0:"+z0);

  			double p0 = raydata[x0][y0][z0]*(1-fz)+raydata[x0][y0][z1]*fz;
  			double p1 = raydata[x0][y1][z0]*(1-fz)+raydata[x0][y1][z1]*fz;
  			double p2 = raydata[x1][y0][z0]*(1-fz)+raydata[x1][y0][z1]*fz;
  			double p3 = raydata[x1][y1][z0]*(1-fz)+raydata[x1][y1][z1]*fz;
  			double p4 = p0*(1-fy)+p1*fy;
  			double p5 = p2*(1-fy)+p3*fy;
  			double p6 = p4*(1-fx)+p5*fx;
  			
  			return p6;
  		}

  		private class rayListener extends MouseInputAdapter implements KeyListener {
  			//发生单击事件时被触发
  			public void mouseClicked(MouseEvent e) {
  				u1=e.getX();
  				u2=e.getY();
  		        System.out.println("x:"+u1+" y:"+u2);
  		        u3=slice;
  			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
	            System.out.println("。。。。。。。。。。");
	  		    f_ray.removeKeyListener(rayListener);

		        if( arg0.getKeyCode()==KeyEvent.VK_DOWN ){
		            u2 = u2+5;
		            volume();
		        } else if (arg0.getKeyCode()==KeyEvent.VK_UP){
		            u2 = u2-5;
		            volume();
		        } else if (arg0.getKeyCode()==KeyEvent.VK_RIGHT){
		            u1 = u1+5;
		            volume();
		        } else if (arg0.getKeyCode()==KeyEvent.VK_LEFT){
		        	u1 = u1-5;
		            volume();
		        } else if (arg0.getKeyCode()==33){
		        	u3 = u3+5;
		            volume();
		        } else if (arg0.getKeyCode()==34){
		        	u3 = u3-5;
		            volume();
		        } else {
					System.out.println(arg0.getKeyCode());
		        }

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
//				System.out.println("release");
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
//				System.out.println("type");
			}
  		}     
    
    
}

