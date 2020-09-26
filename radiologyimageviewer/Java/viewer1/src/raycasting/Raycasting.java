package raycasting;

public class Raycasting {

}

/*

package raycasting;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;



public class Raycasting {

	public static Vector3 eye = new Vector3(0,-20,0); // eye position

	public static void main(short[][][] data) {
		// TODO Auto-generated method stub
		int h = 200;
		int w = 200;
		int d = 200;
		int[][][] raydata = new int[d][h][w];

		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		
		for(int y=0; y<200; y++)
			for (int z = 150; z < 350; z++) 
				for (int x = 220; x < 420; x++) {
					raydata[y][z-150][x-220] = data[y][z][x];
					if(data[y][z][x] < min) min = data[y][z][x];
					if(data[y][z][x] > max) max = data[y][z][x];
				}
//		System.out.println(max);
//		System.out.println(min);
		int range = max - min;

		
		BufferedImage raycasting_img = new BufferedImage(w, h, 2);		
		ImageIcon imageIcon = new ImageIcon(raycasting_img);


//		Cube8 cube8 = new Cube8(new Vector3(0,0,0), 100);

		for(int i = 0; i<w; i++)
			for(int j = 0; j<h; j++) {
				Vector3 datapos = new Vector3(0,j,i);
				
				int color = (int)Composite(datapos, raydata);
				if(color<-1024)
					System.out.println(color);
					
				color = ((color-min) * 255)/range;		
				if (color > 255)
					color = 255; 
	            if (color < 0)
	            	color = 0; 
	            color = 0xFF000000 | color << 16 | color << 8 | color;
				raycasting_img.setRGB(i,j,color);
				
			}

		JPanel p_ray=new JPanel(new FlowLayout());
	    JLabel l_ray = new JLabel();
	    JFrame f_ray = new JFrame("raycasting");

	    p_ray.add(l_ray);
	    f_ray.setLayout(new BorderLayout()); //setLayout是对当前组件设置为流式布局.组件在窗体中从左到右依次排列 如果排到行的末尾 换行排列 排列会随着窗体的大小而改变
	    f_ray.getContentPane().add(p_ray, BorderLayout.CENTER);
	    f_ray.pack();
	    f_ray.setVisible(true);
	    f_ray.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
	    l_ray.setIcon(imageIcon);
		
	}

	private static double Composite(Vector3 datapos, int[][][] raydata) {
		// TODO Auto-generated method stub
		double[] samplepos = new double[] {datapos.getX(), datapos.getY(), datapos.getZ()};
		double cumcolor = 0;
		double cumalpha = 0;
		double alpha = 0.005; //透明度
		double stepsize = 0.1;//采样步长
		double samplecolor;
		
		Vector3 dir = new Vector3(datapos.getX()-eye.getX(), datapos.getY()-eye.getY(), datapos.getZ()-eye.getZ());
		dir = dir.normalize();
		
		while((alpha<1) && (samplepos[0]<20) &&(samplepos[1]<199) && (samplepos[2]<199)) {
			samplecolor = TrInterpolation(samplepos, raydata);//三线性插值获得采样点处的颜色及不透明度
			
//			if(samplecolor==-1024){
//				alpha=0;
//			}
//			else
//				alpha=0.6;
			
			//合成颜色及不透明度,采用的是从前到后的合成公式
			cumcolor = cumcolor+samplecolor*alpha*(1-cumalpha); 
			cumalpha = cumalpha+alpha*(1-cumalpha);
			
			//下一个采样点
			samplepos[0]+=dir.getX()*stepsize;
			samplepos[1]+=dir.getY()*stepsize;
			samplepos[2]+=dir.getZ()*stepsize;
			

		}

//		System.out.println(cumcolor); 


		return cumcolor;
	}

	private static double TrInterpolation(double[] samplepos, int[][][] raydata) {
		// TODO Auto-generated method stub
//		System.out.println(samplepos[0]); 
//		System.out.println(samplepos[1]); 
//		System.out.println(samplepos[2]); 
//		System.out.println("--------------------"); 

		int x0, y0, z0, x1, y1 ,z1;
		double fx, fy, fz;
		
		x0=(int) Math.floor(samplepos[0]);//整数部分
		y0=(int) Math.floor(samplepos[1]);
		z0=(int) Math.floor(samplepos[2]);
		fx=samplepos[0]-x0;//小数部分
		fy=samplepos[1]-y0;
		fz=samplepos[2]-z0;
		x1=x0+1;
		y1=y0+1;
		z1=z0+1;
		
//		System.out.println("x0:"+x0+" y0:"+y0+" z0:"+z0);

		double p0 = raydata[x0][y0][z0]*(1-fz)+raydata[x0][y0][z1]*fz;
		double p1 = raydata[x0][y1][z0]*(1-fz)+raydata[x0][y1][z1]*fz;
		double p2 = raydata[x1][y0][z0]*(1-fz)+raydata[x1][y0][z1]*fz;
		double p3 = raydata[x1][y1][z0]*(1-fz)+raydata[x1][y1][z1]*fz;
		double p4 = p0*(1-fy)+p1*fy;
		double p5 = p2*(1-fy)+p3*fy;
		double p6 = p4*(1-fx)+p5*fx;
		
		return p6;
	}



}

*/

