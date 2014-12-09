import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Interface implements ActionListener{	
	JPanel container = new JPanel(); //panel holding everything
	JLabel title = new JLabel("VOR Simulator");
	JPanel end = new JPanel(); //panel holding buttons on bottom
	
	JButton start = new JButton("Start Simulation");
	JButton quit = new JButton("Quit Simulation");
	JButton back = new JButton("<");
	JButton front = new JButton(">");
	
	//Meter meter = new meter();
	
	
	public Interface(){
		container.setLayout(new BorderLayout());
		title.setFont(new Font("Courier",Font.BOLD,35));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		Border blackline = BorderFactory.createLineBorder(Color.black);
		title.setBorder(blackline);
		container.add(title,BorderLayout.PAGE_START);
		container.add(new meter(), BorderLayout.CENTER);
		container.add(end,BorderLayout.PAGE_END);
		start.addActionListener(this);
		end.add(start);
		quit.addActionListener(this);
		end.add(quit);
		back.addActionListener(this);
		end.add(back);
		front.addActionListener(this);
		end.add(front);
		
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if(ae.getSource() == front){
			global.offset -= 5;
			//System.out.println("Offset = " + global.offset);
			System.out.println("Degree is: " + global.getDegree(0, global.offset));
			container.repaint();
		}
		if(ae.getSource() == back){
			global.offset += 5;
			//System.out.println("Offset = " + global.offset);
			System.out.println("Degree is: " + global.getDegree(0, global.offset));
			container.repaint();
		}
		if(ae.getSource() == quit){
			System.exit(0);
		}
		if(ae.getSource() == start){
			back.setEnabled(false);
			front.setEnabled(false);
			start.setEnabled(false);
			update up = new update(this.container);
			up.start();
		}
	}

}
class update extends Thread{
	JPanel container;
	public update(JPanel container){
		this.container = container;
	}
	
	public void run(){
		VOR myvor = new VOR();
		Radio rad = new Radio(0);
		for(int x = 0; x < 15; x++){
			//radio Radial, station code, signal Quality, pilot Radial
			int ID = rad.getID(); String sigQ = rad.getSigQual(); double radi = rad.getRadial();
			System.out.println("**DEBUG** ID = " + ID + " Quality = " + sigQ + " Radial " + radi);
			myvor.newSignal(radi, ID, sigQ, global.getDegree(0, global.offset));
			//update global variables
			global.bar = myvor.side * myvor.degOffset;
			global.ID = myvor.stationCode;
			global.dir = myvor.direction;
			//update gui
			container.repaint();
			try {
				Thread.sleep(2000); //sleep 2 seconds
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
