package 
{

	import flash.display.Sprite;
	import flash.events.*;
	import flash.ui.*;
	import flash.utils.ByteArray;
	import flash.net.URLRequest;
	import flash.net.URLStream;
	import flash.geom.Vector3D;
    import com.adobe.utils.PerspectiveMatrix3D;
	import com.adobe.utils.AGALMiniAssembler;
	import flash.display.Sprite;
	import flash.display.Stage3D;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	import flash.display3D.Context3D;
	import flash.display3D.Context3DCompareMode;
	import flash.display3D.Context3DProgramType;
	import flash.display3D.Context3DRenderMode;
	import flash.display3D.Context3DTriangleFace;
	import flash.display3D.Context3DVertexBufferFormat;
	import flash.display3D.IndexBuffer3D;
	import flash.display3D.Program3D;
	import flash.display3D.VertexBuffer3D;
	import flash.events.Event;
	import flash.geom.Matrix3D;
	import flash.geom.Rectangle;
	import flash.geom.Vector3D;


	public class Fly1 extends Sprite
	{
		private var vertex:ByteArray;
		private var normal:ByteArray;
		private var stream:URLStream;
		private var stream2:URLStream;
		private var num_buffer:int = 0;
		private var num_left:int = 0;

		var _width:Number;
		var _height:Number;

		var _stage:Stage3D;
		var _context:Context3D;
		var _indexBuffer:IndexBuffer3D;
		var _vertexBuffer:VertexBuffer3D;
		var _program:Program3D;

		var _modelView:Matrix3D;
		var _modelViewProjection:Matrix3D;
		var _projection:Matrix3D;

		var _time:Number = 0;
		var _deltaTime:Number = 0;
		var _tweenTime:Number = 0;
		var _tweenPitch:Number = 0;
		var _tweenYaw:Number = 0;
		var _pitch:Number = 0;
		var _yaw:Number = 0;

		// Vertexes for the cube. Format is (x, y, z, r, g, b), counter-clockwise
		// winding, with normal OpenGL axes (-x/+x = left/right, -y/+y = bottom/top,
		// -z/+z = far/near). This is also a bit overkill on vertexes, but the
		// extras are needed to make each side a solid color.
		var _cubeVertexes:Array;
		var _cubeVertexes2:Array;

		// Indexes into the vertex buffer above for each of the cube's triangles.
		var _cubeIndexes:Array;
		var _cubeIndexes2:Array;
		//var _cubeIndexes:Vector.<uint> = new Vector.<uint>; 
		var _vertexShader:String = [
		      "m44 vt0, va0, vc0", //transform vertex x,y,z (va0) through view matrix (vc0) 
			  "mov op, vt0 ",   //output transformed vertex x,y,z (vt0) 
			  "mov v0, vt0.z ", //move vertex z to fragment shader (v0) 
			  "mov v1, va1 ",   //move vertex r,g,b to fragment shader (v1) 
			  "mov v2, va2"     //move vertex normal to fragment shader (v2)    // copy the vertex color
		    ].join("\n");


		var _fragmentShader:String = [
		     "dp3 ft0, fc1, v2 ",  //dot the vertex normal (v2) with light direction (fc1) 
			 "mul ft1, fc2, ft0 ", //invert light amount (ft0) to get correct direction 
			 "mul ft0, ft1, v1 ",  //multiply vertex r,g,b (v1) by light amount (ft1) 
			 "mul ft2, fc0, v0 ",  //multiply fog r,g,b (fc0) by vertex depth (v0) 
			 "add oc, ft2, ft0"    //add fog r,g,b (ft2) to lit vertex r,g,b (ft0) 
		    ].join("\n");

        var mouseXOnMouseDown:Number ;
        var mouseYOnMouseDown:Number ;
        var previousMouseX:Number = 0;
        var previousMouseY:Number = 0;
        var startCameraMotion:Boolean = false;
        var cameraTargetXAngle:Number = 0;
        var cameraTargetYAngle:Number = 0;
		var cameraSavedXAngle:Number = 0;
        var cameraSavedYAngle:Number = 0;
		
		var times=0;
		/*var pointX=[253,250,248,252,243,240,261,257,254];
		
		var pointY=[562,542,525,511,487,467,447,432,419];
		
		var pointZ=[190,193,184,174,176,193,211,247,250];
  */
 //  var pointX=[260,272,275,240];//,243,252,248,250,253]; //+是右边 -是左边
  
 //  var pointY=[419,431,447,467];//,487,511,525,542,562];
  
 //  var pointZ=[255,261,211,193];//,176,174,184,193,211]; //+是下边 -是上边
   
/*     var pointX=[260,263,266,269,272,275,276.2,277.4,278.6,279.8,261,256.8,252.6,248.4,244.2,240];//,243,252,248,250,253]; //+是右边 -是左边
  
     var pointY=[419,421.6,424.2,426.8,429.4,432,435,438,441,444,447,451,455,459,463,467];//,487,511,525,542,562];
  
     var pointZ=[255,256,257,258,259,260,250.2,240.4,230.6,220.8,211,207.4,203.8,200.2,196.6,193];//,176,174,184,193,211]; //+是下边 -是上边
 */  
 
    var pointX=[260,264,268,272,273,274,275,266.25,257.5,248.75,240];//,243,252,248,250,253]; //+是右边 -是左边
  
   var pointY=[419,423,427,431,435,439,444,447,452,457,462,467];//,487,511,525,542,562];
  
   var pointZ=[255,257,259,261,248.5,236,223.5,211,204,197,193];//,176,174,184,193,211]; //+是下边 -是上边
   
  var directionX:Array=new Array;
  var directionY:Array=new Array;
  var directionZ:Array=new Array;
  
   var eyeX:Array=new Array;
   var eyeY:Array=new Array;
   var eyeZ:Array=new Array;
   
	 var framerate:Array=new Array;
		
		function Fly1()
		{
			read();
		}

		//read file
		function read():void
		{
			
			
			var request:URLRequest = new URLRequest("vertex22118");
			stream = new URLStream  ;
			stream.addEventListener(Event.COMPLETE,writeFile);
			try
			{
				stream.load(request);
			}
			catch (error:Error)
			{
				trace("Unable to load requested URL.");
			}
			trace("down");
		}

		function writeFile(evt:Event):void
		{
			var request:URLRequest = new URLRequest("normal22118");
			stream2 = new URLStream  ;
			stream2.addEventListener(Event.COMPLETE,writevoxelFile);
			try
			{
				stream2.load(request);
			}
			catch (error:Error)
			{
				trace("Unable to load requested URL.");
			}
			trace("down2");

			vertex = new ByteArray  ;
			//var i = stream.readInt();
			trace(stream.bytesAvailable);
			stream.readBytes(vertex,0,stream.bytesAvailable);
			stream.close();


		}

		function writevoxelFile(evt:Event):void
		{
			normal = new ByteArray  ;
			//var i = stream.readInt();
			trace(stream2.bytesAvailable);
			stream2.readBytes(normal,0,stream2.bytesAvailable);
			stream2.close();

			_cubeVertexes=new Array();
			_cubeIndexes=new Array();

			num_buffer = vertex.length / 12 / 65535;
			trace(num_buffer);
			var t:int = 0;
			var i:int = 0;
			var n:int = 0;
			for (t=0; t<num_buffer; t++)
			{
				_cubeVertexes[t] = new Vector.<Number >   ;
				n = 0;
				for (i=0; i<65535; i++)
				{
					_cubeVertexes[t][n++] = vertex.readFloat();
					_cubeVertexes[t][n++] = vertex.readFloat();
					_cubeVertexes[t][n++] = vertex.readFloat();
					_cubeVertexes[t][n++] = 1.0;
					_cubeVertexes[t][n++] = 1.0;
					_cubeVertexes[t][n++] = 1.0;
					_cubeVertexes[t][n++] = normal.readFloat();
					_cubeVertexes[t][n++] = normal.readFloat();
					_cubeVertexes[t][n++] = normal.readFloat();
				}
				_cubeIndexes[t] = new Vector.<uint >   ;
				for (i=0; i<65535; i++)
				{
					_cubeIndexes[t][i] = i;
				}
			}

			_cubeVertexes[num_buffer] = new Vector.<Number >   ;
			n = 0;
			num_left = vertex.length / 12 - 65535 * num_buffer;
			for (i=0; i<num_left; i++)
			{
				_cubeVertexes[num_buffer][n++] = vertex.readFloat();
				_cubeVertexes[num_buffer][n++] = vertex.readFloat();
				_cubeVertexes[num_buffer][n++] = vertex.readFloat();
				_cubeVertexes[num_buffer][n++] = 1.0;
				_cubeVertexes[num_buffer][n++] = 1.0;
				_cubeVertexes[num_buffer][n++] = 1.0;
				_cubeVertexes[num_buffer][n++] = normal.readFloat();
				_cubeVertexes[num_buffer][n++] = normal.readFloat();
				_cubeVertexes[num_buffer][n++] = normal.readFloat();
			}
			_cubeIndexes[num_buffer] = new Vector.<uint >   ;
			for (i=0; i<num_left; i++)
			{
				_cubeIndexes[num_buffer][i] = i;
			}
			
			_cubeVertexes2=new Array();
			_cubeIndexes2=new Array();
			for (t=0; t<num_buffer; t++)
			{
				_cubeVertexes2[t] = new Vector.<Number >   ;
				n = 0;
				for (i=0; i<65535; i++)
				{
					_cubeVertexes2[t][n] = _cubeVertexes[t][n++];
					_cubeVertexes2[t][n] = _cubeVertexes[t][n++];
					_cubeVertexes2[t][n] = _cubeVertexes[t][n++];
					_cubeVertexes2[t][n] = _cubeVertexes[t][n++];
					_cubeVertexes2[t][n] = _cubeVertexes[t][n++];
					_cubeVertexes2[t][n] = _cubeVertexes[t][n++];
					_cubeVertexes2[t][n] = -_cubeVertexes[t][n++];
					_cubeVertexes2[t][n] = -_cubeVertexes[t][n++];
					_cubeVertexes2[t][n] = -_cubeVertexes[t][n++];
				}
				_cubeIndexes2[t] = new Vector.<uint >   ;
				for (i=0; i<65535; i++)
				{
					_cubeIndexes2[t][i] = 65534-i;
				}
			}
			_cubeVertexes2[num_buffer] = new Vector.<Number >   ;
			n = 0;
			for (i=0; i<num_left; i++)
			{
				_cubeVertexes2[num_buffer][n] = _cubeVertexes[num_buffer][n++];
				_cubeVertexes2[num_buffer][n] = _cubeVertexes[num_buffer][n++];
				_cubeVertexes2[num_buffer][n] = _cubeVertexes[num_buffer][n++];
				_cubeVertexes2[num_buffer][n] = _cubeVertexes[num_buffer][n++];
				_cubeVertexes2[num_buffer][n] = _cubeVertexes[num_buffer][n++];
				_cubeVertexes2[num_buffer][n] = _cubeVertexes[num_buffer][n++];
				_cubeVertexes2[num_buffer][n] = -_cubeVertexes[num_buffer][n++];
				_cubeVertexes2[num_buffer][n] = -_cubeVertexes[num_buffer][n++];
				_cubeVertexes2[num_buffer][n] = -_cubeVertexes[num_buffer][n++];
			}
			_cubeIndexes2[num_buffer] = new Vector.<uint >   ;
			for (i=0; i<num_left; i++)
			{
				_cubeIndexes2[num_buffer][i] = num_left-1-i;
			}
			
			normal=null;
			vertex=null;

			onAddedToStage();

		}


		function onAddedToStage():void
		{
			trace("run");

			stage.align = StageAlign.TOP_LEFT; //左上对齐
			stage.scaleMode = StageScaleMode.NO_SCALE;//当查看者调整 Flash Player 或 AIR 窗口大小时，舞台内容将保持定义的大小

			_width = 500;
			_height = 500;

            stage.addEventListener(MouseEvent.MOUSE_DOWN,on_stage_MOUSE_DOWN)
            stage.addEventListener(MouseEvent.MOUSE_UP,on_stage_MOUSE_UP)
            stage.addEventListener(MouseEvent.MOUSE_WHEEL,on_stage_MOUSE_WHEEL)
			
			_stage = stage.stage3Ds[0];
			_stage.addEventListener(Event.CONTEXT3D_CREATE, onContext);
			_stage.requestContext3D(Context3DRenderMode.AUTO);
			//_stage.viewPort = new Rectangle(0,0,_width,_height);
			_stage.x=0;
			_stage.y=0;

		}

		// Called once the context we requested has been created.
		function onContext(event:Event):void
		{
			_context = _stage.context3D;
			_context.configureBackBuffer(_width, _height, 2, true);
			_context.enableErrorChecking = true;

			// Discard triangles pointing away from the camera, and ones
			// behind things that we've already drawn.
			_context.setCulling(Context3DTriangleFace.BACK);
			_context.setDepthTest(true, Context3DCompareMode.LESS_EQUAL);

			// Setup our initial matrices.
			_modelView = new Matrix3D  ;
			_modelViewProjection = new Matrix3D  ;
			_projection = perspectiveProjection(60,_width / _height,0.000001,2048);


			// Create a program from the two shaders.
			var vsAssembler:AGALMiniAssembler = new AGALMiniAssembler  ;
			vsAssembler.assemble(Context3DProgramType.VERTEX, _vertexShader);
			var fsAssembler:AGALMiniAssembler = new AGALMiniAssembler  ;
			fsAssembler.assemble(Context3DProgramType.FRAGMENT, _fragmentShader);
			_program = _context.createProgram();
			_program.upload(vsAssembler.agalcode, fsAssembler.agalcode);
			_context.setProgram(_program);


			// Upload all the vertex data and create two streams. Position data
			// will be available to the vertex program as `va0`, color data as `va1`.
			_vertexBuffer = _context.createVertexBuffer(65535,9);

			// Upload the index data. This is used by drawTriangles (below).
			_indexBuffer = _context.createIndexBuffer(_cubeIndexes[0].length);

			addEventListener(Event.ENTER_FRAME, onEnterFrame);
		}

		function onEnterFrame(event:Event):void
		{

			_context.clear(0, 0, 0);
			
			Path();
			var d:Date = new Date();

			var t:int=times/10;
			//updateRotation();

			//// Update the modelView matrix given the current rotation
			_modelView.identity();
			//_modelView.appendRotation(_tweenPitch, Vector3D.X_AXIS);
//			_modelView.appendRotation(_tweenYaw, Vector3D.Y_AXIS);
//			_modelView.appendTranslation(0, 0.18, -0.01);
			//_modelView.appendTranslation(0, 0.18, -1);
			_modelView.appendRotation(directionX[t], Vector3D.X_AXIS);
			_modelView.appendRotation(directionY[t], Vector3D.Y_AXIS);
			_modelView.appendRotation(directionZ[t], Vector3D.Z_AXIS);
			_modelView.appendTranslation(1-eyeX[times], 0.5-eyeY[times], 1-eyeZ[times]);
    
			// Concatenate the modelView and projection matrices and set it as
			// a vertex program constant. It will be available as `vc0`.
			_modelViewProjection.identity();
			_modelViewProjection.append(_modelView);
			_modelViewProjection.append(_projection);
			_context.setProgramConstantsFromMatrix(
			        Context3DProgramType.VERTEX, 0, _modelViewProjection, true);

			
			_context.setProgramConstantsFromVector("fragment", 1, Vector.<Number>([0,0,-1,1]));
			_context.setProgramConstantsFromVector("fragment", 2, Vector.<Number>([-1,-1,-1,1]));


			// Tell the GPU to draw all the triangles in indexBuffer. You can also
			// specify an offset and limit within the index buffer, if desired.
/*			for (var i=0; i<num_buffer; i++)
			{
				_vertexBuffer.uploadFromVector(_cubeVertexes[i], 0, 65535);
				_context.setVertexBufferAt(0, _vertexBuffer, 0, Context3DVertexBufferFormat.FLOAT_3);
				_context.setVertexBufferAt(1, _vertexBuffer, 3, Context3DVertexBufferFormat.FLOAT_3);
				_context.setVertexBufferAt(2, _vertexBuffer, 6, Context3DVertexBufferFormat.FLOAT_3);
				_indexBuffer.uploadFromVector(_cubeIndexes[i], 0, _cubeIndexes[i].length);
				_context.drawTriangles(_indexBuffer);
			}

			_vertexBuffer.uploadFromVector(_cubeVertexes[num_buffer], 0, num_left);
			_context.setVertexBufferAt(0, _vertexBuffer, 0, Context3DVertexBufferFormat.FLOAT_3);
			_context.setVertexBufferAt(1, _vertexBuffer, 3, Context3DVertexBufferFormat.FLOAT_3);
			_context.setVertexBufferAt(2, _vertexBuffer, 6, Context3DVertexBufferFormat.FLOAT_3);
			_indexBuffer.uploadFromVector(_cubeIndexes[num_buffer], 0, _cubeIndexes[num_buffer].length);
			_context.drawTriangles(_indexBuffer);
	*/		
			for (var i=0; i<num_buffer; i++)
			{
				_vertexBuffer.uploadFromVector(_cubeVertexes2[i], 0, 65535);
				_context.setVertexBufferAt(0, _vertexBuffer, 0, Context3DVertexBufferFormat.FLOAT_3);
				_context.setVertexBufferAt(1, _vertexBuffer, 3, Context3DVertexBufferFormat.FLOAT_3);
				_context.setVertexBufferAt(2, _vertexBuffer, 6, Context3DVertexBufferFormat.FLOAT_3);
				_indexBuffer.uploadFromVector(_cubeIndexes2[i], 0, _cubeIndexes2[i].length);
				_context.drawTriangles(_indexBuffer);
			}
			_vertexBuffer.uploadFromVector(_cubeVertexes2[num_buffer], 0, num_left);
			_context.setVertexBufferAt(0, _vertexBuffer, 0, Context3DVertexBufferFormat.FLOAT_3);
			_context.setVertexBufferAt(1, _vertexBuffer, 3, Context3DVertexBufferFormat.FLOAT_3);
			_context.setVertexBufferAt(2, _vertexBuffer, 6, Context3DVertexBufferFormat.FLOAT_3);
			_indexBuffer.uploadFromVector(_cubeIndexes2[num_buffer], 0, _cubeIndexes2[num_buffer].length);
			_context.drawTriangles(_indexBuffer);

           
           
			_context.present();
			
			 times=times+1;
			 
			 //if(times>=(pointX.length-1)*10)
//			 {
//				 times=0
//			 }
         var d2:Date = new Date(); 
         var sjc = ((d2.getTime()-d.getTime())/1000);   
         framerate[times] = sjc; 
		 
             if(times>=10)
			 {
				 var s=new String();
				 s=s+String(times)+" ";
				 for(var i=0;i<times;i++)
				 {
					 s=s+String(framerate[i])+",";
				 }
				// fly_txt.text=s;
			 }

		}

		function perspectiveProjection(fov:Number=90,
		      aspect:Number=1, near:Number=0.000001, far:Number=200):Matrix3D
		{
			var y2:Number = near * Math.tan(fov * Math.PI / 360);
			var y1:Number =  -  y2;
			var x1:Number = y1 * aspect;
			var x2:Number = y2 * aspect;

			var a:Number = 2 * near / (x2 - x1);
			var b:Number = 2 * near / (y2 - y1);
			var c:Number = (x2 + x1) / (x2 - x1);
			var d:Number = (y2 + y1) / (y2 - y1);
			var q:Number = -(far + near) / (far - near);
			var qn:Number = -2 * (far * near) / (far - near);

			return new Matrix3D(Vector.<Number>([
			        a, 0, 0, 0,
			        0, b, 0, 0,
			        c, d, q, -1,
			        0, 0, qn, 0
			      ]));
		}
		
		function updateRotation():void
		{
			if(startCameraMotion==true)
			{
				cameraTargetXAngle = cameraSavedXAngle + ( ((mouseY - mouseYOnMouseDown)/_height) * 360.0 );
		        cameraTargetYAngle = cameraSavedYAngle + ( ((mouseX - mouseXOnMouseDown)/_width) * 360.0 );
	        }else{
		        cameraSavedXAngle = _tweenPitch;
		        cameraSavedYAngle = _tweenYaw;
	        }
			_tweenPitch += (cameraTargetXAngle - _tweenPitch);
	        _tweenYaw += (cameraTargetYAngle - _tweenYaw);


		}
		
	  function Path()
     {
     var c:Number = 2/512;	// Cube's dimension
     //c=c.toFixed(10);
     //document.write(c); 
            var thertax:Number;        
			var thertay:Number;
			var thertaz:Number;
			var x1:Number; var y1:Number; var z1:Number;
			var x2:Number; var y2:Number; var z2:Number;
			var X:Number; var Y:Number; var Z:Number;
    for(var i=1;i<pointX.length;i++)
    {
       x1=pointX[i-1];
       y1=pointY[i-1];
       z1=pointZ[i-1];
       x2=pointX[i];
       y2=pointY[i];
       z2=pointZ[i];
       
       X=x2-x1; Y= y2-y1; Z=z2-z1;
	   //thertax=Math.acos(X/Math.sqrt(X*X+Y*Y+Z*Z))/Math.PI*180;
	   //thertay=Math.acos(Y/Math.sqrt(X*X+Y*Y+Z*Z))/Math.PI*180;
	   //thertaz=Math.acos(Z/Math.sqrt(X*X+Y*Y+Z*Z))/Math.PI*180;
	   thertax=X/Math.sqrt(X*X+Y*Y+Z*Z);
	   thertay=Y/Math.sqrt(X*X+Y*Y+Z*Z);
	   thertaz=Z/Math.sqrt(X*X+Y*Y+Z*Z);
	   
	   directionX[i-1]=thertax;
	   //trace(directionX[i-1]); 
	   directionY[i-1]=thertay;
	   //document.write(directionY[i-1]); 
	   directionZ[i-1]=thertaz;
	   //document.write(directionZ[i-1]); 

	   var xx1:Number=c*x1;
	   
	   var yy1:Number=c*y1;
	 
	   var zz1:Number=c*z1;
	
	   var xx2:Number=c*x2;
	
	   var yy2:Number=c*y2;
	  
	   var zz2:Number=c*z2;
	   
	   for(var ii=0; ii<10; ii++)
			{
			
			    var eye_x=(xx1)+(xx2-xx1)*ii/10
                eyeX[(i-1)*10+ii]=eye_x;
                var eye_y=(yy1)+(yy2-yy1)*ii/10;
				eyeY[(i-1)*10+ii]=eye_y;
				var eye_z=(zz1)+(zz2-zz1)*ii/10;
				eyeZ[(i-1)*10+ii]=eye_z;
			   /* var eye_x=parseFloat(xx1)+(xx2-xx1)*ii/10
                eyeX[(i-1)*10+ii]=eye_x+0.02;
                var eye_y=parseFloat(yy1)+(yy2-yy1)*ii/10;
				eyeY[(i-1)*10+ii]=eye_y+0.04296875;
				var eye_z=parseFloat(zz1)+(zz2-zz1)*ii/10;
				eyeZ[(i-1)*10+ii]=eye_z;*/

			}
    }
}

		
		function on_stage_MOUSE_DOWN(e:MouseEvent){
	     mouseXOnMouseDown = mouseX
	     mouseYOnMouseDown = mouseY
	     startCameraMotion = true
		 //trace("down");
        }
        function on_stage_MOUSE_UP(e:MouseEvent){
	      startCameraMotion = false
        }
        function on_stage_MOUSE_WHEEL(e:MouseEvent){
	      
        }
		

	}
}