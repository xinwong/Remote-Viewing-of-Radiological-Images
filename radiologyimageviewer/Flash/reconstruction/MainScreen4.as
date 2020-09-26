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


	public class MainScreen4 extends Sprite
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
		var _cubeVertexes:Array;  //outside
		var _cubeVertexes2:Array;   //inside

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


		//var _fragmentShader:String = [
//		     "dp3 ft0, fc1, v2 ",  //dot the vertex normal (v2) with light direction (fc1) 
//			 "mul ft1, fc2, ft0 ", //invert light amount (ft0) to get correct direction 
//			 "mul ft0, ft1, v1 ",  //multiply vertex r,g,b (v1) by light amount (ft1) 
//			 "mul ft2, fc0, v0 ",  //multiply fog r,g,b (fc0) by vertex depth (v0) 
//			 "add oc, ft2, ft0"    //add fog r,g,b (ft2) to lit vertex r,g,b (ft0) 
//		    ].join("\n");
		var _fragmentShader:String = [
		     "dp3 ft0, fc1, v2 ",  //dot the vertex normal (v2) with light direction (fc1) 
			 "dp3 ft1, fc3, v2 ",  //dot the vertex normal (v2) with light direction (fc3) 
			 "dp3 ft2, fc4, v2 ",  //dot the vertex normal (v2) with light direction (fc4) 
			 "dp3 ft3, fc5, v2 ",  //dot the vertex normal (v2) with light direction (fc5) 
			 "mul ft4, fc2, ft0 ", //invert light amount (ft0) to get correct direction 
			 "mul ft5, fc2, ft1 ", //invert light amount (ft0) to get correct direction 
			 "mul ft6, fc2, ft2 ", //invert light amount (ft0) to get correct direction 
			 "mul ft7, fc2, ft3 ", //invert light amount (ft0) to get correct direction 
			 "add ft1, ft5, ft4 ", 
			 "add ft2, ft1, ft6 ",  
			 "add ft3, ft2, ft7 ", 
			 "mul ft0, ft3, v1 ",  //multiply vertex r,g,b (v1) by light amount (ft1) 
			// "mul ft2, fc0, v0 ",  //multiply fog r,g,b (fc0) by vertex depth (v0) 
			// "add oc, ft2, ft0"    //add fog r,g,b (ft2) to lit vertex r,g,b (ft0) 
			 "mov oc, ft0"
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
		
		 var timedate1;
         var timedate2;
         var timedate3;
		 var timedate4;
		 var first=true;
		
		function MainScreen4()
		{
			read();
		}

		//read file
		function read():void
		{
			timedate1 = new Date(); 
			var request:URLRequest = new URLRequest("vertex08629");
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
		}

		function writeFile(evt:Event):void
		{
			var request:URLRequest = new URLRequest("normal08629");
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
            trace(stream.bytesAvailable);
			stream.readBytes(vertex,0,stream.bytesAvailable);
			stream.close();
			
			timedate2 = new Date(); 

		}

		function writevoxelFile(evt:Event):void
		{
			timedate4 = new Date(); 
			normal = new ByteArray  ;
            trace(stream2.bytesAvailable);
			stream2.readBytes(normal,0,stream2.bytesAvailable);
			stream2.close();

			_cubeVertexes=new Array();
			_cubeIndexes=new Array();

			num_buffer = vertex.length / 12 / 65535;
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

			onAddedToStage();

		}


		function onAddedToStage():void
		{

			stage.align = StageAlign.TOP_LEFT;
			stage.scaleMode = StageScaleMode.NO_SCALE;

			//_width = stage.stageWidth;
//			_height = stage.stageHeight;
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
			//_context.setCulling(Context3DTriangleFace.BACK);
			_context.setCulling(Context3DTriangleFace.NONE);
			_context.setDepthTest(true, Context3DCompareMode.LESS_EQUAL);

			// Setup our initial matrices.
			_modelView = new Matrix3D  ;
			_modelViewProjection = new Matrix3D  ;
			_projection = perspectiveProjection(60,_width / _height,0.001,2048);


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
			//var d:Date = new Date();
//			var t1 :Number=d.getTime();
			
			_context.clear(0, 0, 0);
			
			//updateRotation();

			//// Update the modelView matrix given the current rotation
			_modelView.identity();
			//_modelView.appendRotation(_tweenPitch, Vector3D.X_AXIS);
//			_modelView.appendRotation(_tweenYaw, Vector3D.Y_AXIS);
			//_modelView.appendTranslation(0, 0.18, -0.01);
			_modelView.appendTranslation(.0,-0.5,-2.0);
			/*_modelView.appendRotation(-0.147, Vector3D.X_AXIS);
			_modelView.appendRotation(0, Vector3D.Y_AXIS);
			_modelView.appendRotation(0.661693, Vector3D.Z_AXIS);
			_modelView.appendTranslation(1-0.97265625, 0.5-0.1328125, 1-1.0625);*/
    
			// Concatenate the modelView and projection matrices and set it as
			// a vertex program constant. It will be available as `vc0`.
			_modelViewProjection.identity();
			_modelViewProjection.append(_modelView);
			_modelViewProjection.append(_projection);
			_context.setProgramConstantsFromMatrix(
			        Context3DProgramType.VERTEX, 0, _modelViewProjection, true);

			_context.setProgramConstantsFromVector("fragment", 0, Vector.<Number>([0.6, 0.6, 0.6, 1]));
			_context.setProgramConstantsFromVector("fragment", 1, Vector.<Number>([1,1,-1,1]));
			_context.setProgramConstantsFromVector("fragment", 2, Vector.<Number>([-1,-1,-1,1]));
  //          _context.setProgramConstantsFromVector("fragment", 3, Vector.<Number>([-1, 1, -1, 1]));
         	_context.setProgramConstantsFromVector("fragment", 4, Vector.<Number>([-1,-1,-1,1]));
 //           _context.setProgramConstantsFromVector("fragment", 5, Vector.<Number>([-1, 1, 1, 1]));
		
			
			

			// Tell the GPU to draw all the triangles in indexBuffer. You can also
			// specify an offset and limit within the index buffer, if desired.
			for (var i=0; i<num_buffer; i++)
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
			
			/*for (var i=0; i<num_buffer; i++)
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
*/
           
           
			_context.present();
			
			//var d2:Date = new Date();
//			var t2:Number=d2.getTime();
//              trace("time");
//			  trace(t2-t1);

            if(first==true)
			{
				timedate3 = new Date(); 
			var t1 :Number= timedate1.getTime();
			var t2 :Number= timedate2.getTime();
			var t3 :Number= timedate3.getTime();
			var t4 :Number= timedate4.getTime();
//			download_txt.text=String(t2-t1)+"ms";
//			render_txt.text=String(t3-t4)+"ms";
			first=false;
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
		
		function on_stage_MOUSE_DOWN(e:MouseEvent){
	     mouseXOnMouseDown = mouseX
	     mouseYOnMouseDown = mouseY
	     startCameraMotion = true
		 trace("down");
        }
		
        function on_stage_MOUSE_UP(e:MouseEvent){
	      startCameraMotion = false
        }
        function on_stage_MOUSE_WHEEL(e:MouseEvent){
	      
        }
		

	}
}