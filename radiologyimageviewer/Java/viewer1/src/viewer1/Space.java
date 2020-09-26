package viewer1;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 *
 *
 *  @version �� 1.1
 *  
 *  @author  �� ������         <a href="mailto:DennisIT@163.com">�����ʼ�</a>
 *    
 *  @since   �� 1.0      ����ʱ��:    2013-5-6 ����02:50:08
 *     
 *  TODO     : Java3D ������ά��״չʾ
 *
 */
public class Space {

    public void Java3DShape(){
        
        //�����ռ� ������
        
        // ����һ������ռ�
        SimpleUniverse universe = new  SimpleUniverse();
        // ����һ������������������ݽṹ
        BranchGroup group = new BranchGroup();
        // ����һ��׵�岢�������뵽group��
          Cone cone=new Cone(.5f,1.0f,1,null) ;    //׶��
        group.addChild(cone);
       
        //�ƹ⹹��

        Color3f light1Color = new Color3f(1.8f, 0.1f, 0.1f);
        // ���ù��ߵ���ɫ
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        // ���ù��ߵ����÷�Χ
        Vector3f light1Direction  = new Vector3f(4.0f, -7.0f, -12.0f);
        // ���ù��ߵķ���
        DirectionalLight light1= new DirectionalLight(light1Color, light1Direction);
          // ָ����ɫ�ͷ��򣬲��������Դ
        light1.setInfluencingBounds(bounds);
        // �ѹ��ߵ����÷�Χ�����Դ��
        group.addChild(light1);
        // ����Դ����group��
        // ���Ź۲��
        universe.getViewingPlatform().setNominalViewingTransform();
        // ��group���뵽����ռ���
        universe.addBranchGraph(group);
    }
    
    public static void main(String[] args) {

    }
}