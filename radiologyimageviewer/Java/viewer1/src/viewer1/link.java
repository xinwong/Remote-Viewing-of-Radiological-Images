package viewer1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import viewer1.flythrough; 

public class link {
	
	JPanel buttons = new JPanel(new FlowLayout());
    JButton flyButton = new JButton("Fly through");  
    JLabel label = new JLabel();
    JFrame frame = new JFrame();
    flythrough fly=new flythrough();
    
    public static void main(String args[])
	{
		new link();
	} 
    
    class ButtonActionListener implements  ActionListener
    {
    	public void actionPerformed(ActionEvent e) 
    	{
    		JButton button = (JButton)e.getSource();
    		if(button==flyButton)
    		{
    			fly.fly_through();    			
    		}
    		
    	}
    }
    public link()
    {
    	flyButton.addActionListener(new ButtonActionListener());    	 
    	buttons.add(flyButton);
    	frame.setLayout(new BorderLayout());
    	frame.getContentPane().add(buttons, BorderLayout.SOUTH);
    	frame.pack();
	    frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
   
}



 