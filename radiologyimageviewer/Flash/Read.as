package  {
	
import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.geom.Rectangle;
	import flash.utils.ByteArray;
	import fl.containers.ScrollPane;

	public class Read
	{
       
		public function Read()
		{
			// constructor code
		}

		//此方法返回的Bitmap可以直接赋值给Image的source属性 
		public static function ByteArrayToBitmap(byArr:ByteArray, s:int):Bitmap
		{
			if (byArr == null)
			{
				return null;
			}
			//读取出存入时图片的高和宽,因为是最后存入的数据,所以需要到尾部读取 
			var bmd:ByteArray = byArr;
			//bmd.position=bmd.length-2; 
			var imageWidth:int = 512;
			//bmd.position=bmd.length-4; 
			var imageHeight:int = 512;
			var copyBmp:BitmapData = new BitmapData(imageWidth,imageHeight,true);
			//利用setPixel方法给图片中的每一个像素赋值,做逆操作 
			//ByteArray数组从零开始存储一直到最后都是图片数据,因为读入时的高和宽都是一样的,所以当循环结束是正好读完 
			bmd.position = 0;
			for (var i:uint=0; i<imageHeight; i++)
			{
				for (var j:uint=0; j<imageWidth; j++)
				{
					var pixel = bmd.readShort();
					pixel=((pixel-(-3024)) * 255)/6095;
					var p = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
					copyBmp.setPixel( j, i, p );
				}
			}
			var bmp:Bitmap = new Bitmap(copyBmp);
			
			bmd = null;
			return bmp;
		}

		 public static function zoom(p:ScrollPane,s:Number):Bitmap
		 {
			 var bmp = p.content; 
			 
			 bmp.scaleX=s;
			 bmp.scaleY=s;
			 return bmp;
		 }
		 
		  public static function windowing(byArr:ByteArray,c:Number,w:Number):Bitmap
		 {
			
			 if (byArr == null)
			{
				return null;
			}
			
			var bmd:ByteArray = byArr;
			var imageWidth:int = 512;
			var imageHeight:int = 512;
			var copyBmp:BitmapData = new BitmapData(imageWidth,imageHeight,true);
		    var min=c-w/2;
			var range=w;
			bmd.position = 0;
			for (var i:uint=0; i<imageHeight; i++)
			{
				for (var j:uint=0; j<imageWidth; j++)
				{
					var pixel = bmd.readShort();
					pixel=((pixel-min) * 255)/range;
					pixel = pixel>255 ? 255:pixel;    
				    pixel= pixel<0 ? 0:pixel;
					var p = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
					copyBmp.setPixel( j, i, p );
				}
			}
			var bmp:Bitmap = new Bitmap(copyBmp);
			
			bmd = null;
			return bmp;
		 }
		 
		  
		  public static function mip(byArr:ByteArray,depth:Number):Bitmap
		 {
			
			 if (byArr == null)
			{
				return null;
			}
			
			var bmd:ByteArray = byArr;
			var imageWidth:int = 512;
			var imageHeight:int = 512;
			
			bmd.position = 0;
			var MapData:Array=new Array();
			for (var d:uint=0; d<depth; d++)
			{
			    MapData[d]=new Array();
				for (var h:uint=0; h<imageHeight; h++)
				{
					MapData[d][h]=new Array();
					for (var w:uint=0; w<imageWidth; w++)
					{ 
						MapData[d][h][w] = bmd.readShort();
					}
				}
			}
			
			for (var i1:uint=0; i1<depth; i1++)
			{
				for (var i2:uint=0; i2<imageHeight; i2++)
				{
					for (var i3:uint=0; i3<imageWidth; i3++)
					{ 
						if(MapData[i1][i2][i3]<-1024)
							MapData[i1][i2][i3] = -1024;
					}//mip 只投影骨头		
				}
			}
			
			
			var mip_Data:Array=new Array();
			for(var d:uint=0;d<depth;d++)
			{
       			mip_Data[d]=new Array();
       			for(var w:uint=0;w<imageWidth;w++)
				{
					var blank:int = -9999;
					for (var h:uint=0; h<imageHeight; h++)
					{
						if(MapData[d][h][w]>blank) 
						{
							blank = MapData[d][h][w];
						}
					}
				    mip_Data[d][w]=blank;
       			}                
			}
			var min:int = 9999;
			var max:int = -9999;
			for(var d:uint=0;d<depth;d++)
			{
       			for(var w:uint=0;w<imageWidth;w++)
				{
					if(mip_Data[d][w] < min) 
						min = mip_Data[d][w];
					if(mip_Data[d][w] > max) 
						max = mip_Data[d][w];
				}
			}				
			
			var copyBmp:BitmapData = new BitmapData(imageWidth,depth,true);
			
			for (var i:uint=0; i<depth; i++)
			{
				for (var j:uint=0; j<imageWidth; j++)
				{
					var pixel = mip_Data[i][j];
					pixel=((pixel-min) * 255)/(max-min);
					pixel = pixel>255 ? 255:pixel;    
				    pixel= pixel<0 ? 0:pixel;
					var p = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
					copyBmp.setPixel( j, i, p );
				}
			}
			var bmp:Bitmap = new Bitmap(copyBmp);
			
			bmd = null;
			return bmp;
		 }
	
	/*
		public static function minip(byArr:ByteArray,depth:Number):Bitmap
		 {
			
			 if (byArr == null)
			{
				return null;
			}
			
			var bmd:ByteArray = byArr;
			var imageWidth:int = 512;
			var imageHeight:int = 512;
			
			bmd.position = 0;
			var MapData:Array=new Array();
			for (var d:uint=0; d<depth; d++)
			{
			    MapData[d]=new Array();
				for (var h:uint=0; h<imageHeight; h++)
				{
					MapData[d][h]=new Array();
					for (var w:uint=0; w<imageWidth; w++)
					{ 
						MapData[d][h][w] = bmd.readShort();
					}
				}
			}
			
			for (var i1:uint=0; i1<depth; i1++)
			{
				for (var i2:uint=0; i2<imageHeight; i2++)
				{
					for (var i3:uint=0; i3<imageWidth; i3++)
					{ 
						if(MapData[i1][i2][i3]<1000)
							MapData[i1][i2][i3] = 3071;
					}		
				}
			}
			
			
			var mip_Data:Array=new Array();
			for(var d:uint=0;d<depth;d++)
			{
       			mip_Data[d]=new Array();
       			for(var w:uint=0;w<imageWidth;w++)
				{
					var blank:int = 9999;
					for (var h:uint=0; h<imageHeight; h++)
					{
						if(MapData[d][h][w]<blank) 
						{
							blank = MapData[d][h][w];
						}
					}
				    mip_Data[d][w]=blank;
       			}                
			}
			var min:int = 9999;
			var max:int = -9999;
			for(var d:uint=0;d<depth;d++)
			{
       			for(var w:uint=0;w<imageWidth;w++)
				{
					if(mip_Data[d][w] < min) 
						min = mip_Data[d][w];
					if(mip_Data[d][w] > max) 
						max = mip_Data[d][w];
				}
			}				
			
			var copyBmp:BitmapData = new BitmapData(imageWidth,depth,true);
			
			for (var i:uint=0; i<depth; i++)
			{
				for (var j:uint=0; j<imageWidth; j++)
				{
					var pixel = mip_Data[i][j];
					pixel=((pixel-min) * 255)/(max-min);
					pixel = pixel>255 ? 255:pixel;    
				    pixel= pixel<0 ? 0:pixel;
					var p = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
					copyBmp.setPixel( j, i, p );
				}
			}
			var bmp:Bitmap = new Bitmap(copyBmp);
			
			bmd = null;
			return bmp;
		 }
		 
		public static function avgip(byArr:ByteArray,depth:Number):Bitmap
		 {
			
			 if (byArr == null)
			{
				return null;
			}
			
			var bmd:ByteArray = byArr;
			var imageWidth:int = 512;
			var imageHeight:int = 512;
			
			bmd.position = 0;
			var MapData:Array=new Array();
			for (var d:uint=0; d<depth; d++)
			{
			    MapData[d]=new Array();
				for (var h:uint=0; h<imageHeight; h++)
				{
					MapData[d][h]=new Array();
					for (var w:uint=0; w<imageWidth; w++)
					{ 
						MapData[d][h][w] = bmd.readShort();
					}
				}
			}
			
//			for (var i1:uint=0; i1<depth; i1++)
//			{
//				for (var i2:uint=0; i2<imageHeight; i2++)
//				{
//					for (var i3:uint=0; i3<imageWidth; i3++)
//					{ 
//						if(MapData[i1][i2][i3]<1000)
//							MapData[i1][i2][i3] = 3071;
//					}		
//				}
//			}
			
			
			var mip_Data:Array=new Array();
			for(var d:uint=0;d<depth;d++)
			{
       			mip_Data[d]=new Array();
       			for(var w:uint=0;w<imageWidth;w++)
				{
					var blank:int = 0;
					for (var h:uint=0; h<imageHeight; h++)
					{
						blank = blank + MapData[d][h][w];
					}
				    mip_Data[d][w]=blank / 512;
       			}                
			}
			var min:int = 9999;
			var max:int = -9999;
			for(var d:uint=0;d<depth;d++)
			{
       			for(var w:uint=0;w<imageWidth;w++)
				{
					if(mip_Data[d][w] < min) 
						min = mip_Data[d][w];
					if(mip_Data[d][w] > max) 
						max = mip_Data[d][w];
				}
			}				
			
			var copyBmp:BitmapData = new BitmapData(imageWidth,depth,true);
			
			for (var i:uint=0; i<depth; i++)
			{
				for (var j:uint=0; j<imageWidth; j++)
				{
					var pixel = mip_Data[i][j];
					pixel=((pixel-min) * 255)/(max-min);
					pixel = pixel>255 ? 255:pixel;    
				    pixel= pixel<0 ? 0:pixel;
					var p = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
					copyBmp.setPixel( j, i, p );
				}
			}
			var bmp:Bitmap = new Bitmap(copyBmp);
			
			bmd = null;
			return bmp;
		 }
*/

		public static function raycasting(byArr:ByteArray,depth:Number,u1:Number,u2:Number,u3:Number):Bitmap
		 {
			
			 if (byArr == null)
			{
				return null;
			}
			
			var hh = 200;
			var ww = 200;
			var dd = 200;	
			if(u3>depth-50)
			  u3=depth-50;
			if(u1<50)
			  u1=50;
			else if(u1>462)
			  u1=462;
			else if(u2<50)
			  u2=50;
			else if(u2>462)
			  u2=462;			
			
			var bmd:ByteArray = byArr;
			var imageWidth:int = 512;
			var imageHeight:int = 512;
			
			bmd.position = 0;
			var MapData:Array=new Array();
			for (var d:uint=0; d<depth; d++)
			{
			    MapData[d]=new Array();
				for (var h:uint=0; h<imageHeight; h++)
				{
					MapData[d][h]=new Array();
					for (var w:uint=0; w<imageWidth; w++)
					{ 
						MapData[d][h][w] = bmd.readShort();
					}
				}
			}

			var min:Number = 9999;
			var max:Number = -9999;
			var raydata:Array=new Array();
			for(var y:uint=0;y<100;y++)
			{
       			raydata[y]=new Array();
       			for(var z:uint=0;z<100;z++)
				{
					raydata[y][z]=new Array();
					for(var x:uint=0; x<100; x++)
					{
						raydata[y][z][x] = Number(MapData[y+u3][z+u2-50][x+u1-50]);
						if(raydata[y][z][x] < min) 	min = raydata[y][z][x];
						if(raydata[y][z][x] > max)  max = raydata[y][z][x];
					}
       			}                
			}			
			

			var range:Number = max - min;
			for(var yy:uint=0;yy<100;yy++)
       			for(var zz:uint=0;zz<100;zz++)
					for(var xx:uint=0; xx<100; xx++)
					{
						raydata[yy][zz][xx] = (raydata[yy][zz][xx]-min) * 255 / range;
					}
			
			var ct:Array=new Array();
			for(var i:Number=0;i<ww;i++)
			{
				ct[i]=new Array();
       			for(var j:Number=0;j<hh;j++)
				{
					//Vector3 datapos = new Vector3(0,(float)(j)/2,(float)(i)/2);	
					var datapos:Array=new Array(0,(j)/2,(i)/2);

					ct[i][j] = Composite(datapos, raydata);
				}
			}	
			
//smooth
			for(var ii:uint=1;ii<200-1;ii++)
       			for(var jj:uint=1;jj<200-1;jj++)
				{				
					 var grey1:Number= ct[ii-1][jj-1];
					 var grey2:Number= ct[ii-1][jj];
					 var grey3:Number= ct[ii-1][jj+1];
					 var grey4:Number= ct[ii][jj-1];			 
					 var grey5:Number= ct[ii][jj];
					 var grey6:Number= ct[ii][jj+1];
					 var grey7:Number= ct[ii+1][jj-1];
					 var grey8:Number= ct[ii+1][jj];
					 var grey9:Number= ct[ii+1][jj+1];
					 var averageGrey:Number=(grey1+grey2+grey3+grey4+grey5+grey6+grey7+grey8+grey9)/9;
					 ct[ii][jj]=averageGrey;
				}



		
			var copyBmp:BitmapData = new BitmapData(200,200,true);

			for(var i1:uint=0;i1<200;i1++)
			{
       			for(var i2:uint=0;i2<200;i2++)
				{
					var pixel:int = ct[i1][i2];
					pixel = pixel>255 ? 255:pixel;    
				    pixel= pixel<0 ? 0:pixel;
					var p = 0xff000000 | pixel << 16 | pixel << 8 | pixel;
					copyBmp.setPixel( i1, i2, p );
				}
			}
			var bmp:Bitmap = new Bitmap(copyBmp);
			
			bmd = null;
			return bmp;
		}
		 
		public static function Composite(Vector3:Array,raydata:Array):Number
		{
			var eye:Array=new Array(-1000,50,50);		 
			
			var samplepos:Array=new Array(Vector3[0],Vector3[1],Vector3[2]);
			var cumalpha:Number = 0;
			var samplealpha:Number; //透明度
			var stepsize:Number = 1;//采样步长
			var samplecolor:Number;
			
			var C_out:Number=0;
			var C_in:Number=0;
			
			var dir:Array=new Array(Vector3[0]-eye[0],Vector3[1]-eye[1],Vector3[2]-eye[2]);
			var l:Number=Math.sqrt(dir[0]*dir[0]+dir[1]*dir[1]+dir[2]*dir[2]);
			dir[0]=dir[0]/l;
			dir[1]=dir[1]/l;
			dir[2]=dir[2]/l;
			
			while((cumalpha<1) && (samplepos[0]<100) && (samplepos[1]<100) && (samplepos[2]<100) && (samplepos[0]>=0) && (samplepos[1]>=0) && (samplepos[2]>=0)) {	
				samplecolor = TrInterpolation(samplepos, raydata);//三线性插值获得采样点处的颜色及不透明度
			
				//下一个采样点			
				samplepos[0]+=dir[0]*stepsize;
				samplepos[1]+=dir[1]*stepsize;
				samplepos[2]+=dir[2]*stepsize;			
			
				if(samplecolor<7 || samplecolor==0) // density=0;
				{
					continue;
				}	

				//合成颜色及不透明度,采用的是从前到后的合成公式
				samplealpha = samplecolor/255;
				

				C_out = C_in*(samplealpha) + samplecolor*(1-samplealpha);		
				if(C_out>=255){
					return C_in;
					}				
				C_in = C_out;				

				cumalpha = cumalpha+samplealpha;				
			}		
			
			return C_out;			
		}		 		 

		public static function TrInterpolation(samplepos:Array,raydata:Array):Number 
		{
			var x0, y0, z0, x1, y1 ,z1;
			var fx:Number;
			var fy:Number;			
			var fz:Number;			
			
			x0=Math.floor(samplepos[0]);//整数部分
			y0=Math.floor(samplepos[1]);
			z0=Math.floor(samplepos[2]);
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
//			System.out.println("x0:"+x0+" y0:"+y0+" z0:"+z0);

			var p0:Number = raydata[x0][y0][z0]*(1-fz)+raydata[x0][y0][z1]*fz;
			var p1:Number = raydata[x0][y1][z0]*(1-fz)+raydata[x0][y1][z1]*fz;
			var p2:Number = raydata[x1][y0][z0]*(1-fz)+raydata[x1][y0][z1]*fz;
			var p3:Number = raydata[x1][y1][z0]*(1-fz)+raydata[x1][y1][z1]*fz;
			var p4:Number = p0*(1-fy)+p1*fy;
			var p5:Number = p2*(1-fy)+p3*fy;
			var p6:Number = p4*(1-fx)+p5*fx;
			
			return p6;	
		}		
	}
}
