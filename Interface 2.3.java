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
class meter extends JPanel{
	
	double x1, y1; //used for getCoordinate()
	
	public meter(){
		setBorder(BorderFactory.createLineBorder(Color.blue));
	}
	public Dimension getPreferredSize() {
        return new Dimension(500,500);
    }
	/**
	 * Find the point located a certain distance away from (x, y) with given slope.
	 * @param x
	 * @param y
	 * @param distance
	 */
	public void findPoint(double x, double y, double distance, double slope){
		x1 = (distance/Math.sqrt(1 + Math.pow(slope, 2))) + x;
		y1 = ((slope*distance)/Math.sqrt(1 + Math.pow(slope, 2))) + y;
	}
	/**
	 * Find the coordinate on the circle, store in x1, y1.
	 * @param degree The cooresponding degree of coordinate
	 */
	public void getCoordinate(double degree, double r){
		double center = 210; 
		while(degree < 0){
			degree += 360;
		}
		while(degree > 359){
			degree -= 360;
		}
		if(degree <= 90){
		//System.out.println(Math.sin(Math.toRadians(degree)) + " " + Math.cos(Math.toRadians(degree)));
			x1 = center + (r * Math.sin(Math.toRadians(degree)));
			y1 = center - (r * Math.cos(Math.toRadians(degree)));
		}
		
		else if((degree > 90) && (degree<= 180)){
			//System.out.println(Math.sin(Math.toRadians(degree)) + " " + Math.cos(Math.toRadians(degree)));
				x1 = center + (r * Math.sin(Math.toRadians(degree)));
				y1 = center - (r * Math.cos(Math.toRadians(degree)));
		}
		else if((degree > 180) && (degree<= 270)){
			//System.out.println(Math.sin(Math.toRadians(degree)) + " " + Math.cos(Math.toRadians(degree)));
				x1 = center + (r * Math.sin(Math.toRadians(degree)));
				y1 = center - (r * Math.cos(Math.toRadians(degree)));
		}
		else if((degree > 270) && (degree<= 360)){
			//System.out.println(Math.sin(Math.toRadians(degree)) + " " + Math.cos(Math.toRadians(degree)));
				x1 = center + (r * Math.sin(Math.toRadians(degree)));
				y1 = center - (r * Math.cos(Math.toRadians(degree)));
		}
		
		
	}
	
	public double findSlope(double x1, double y1, double x2, double y2){
		double slp = (y1-y2)/(x1-x2);
		if(slp < .000000001){
			return 0;
		}
		if(slp > 99999999){
			return 999999.0;
		}
		return slp;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//draw the 2 circles, center = (210, 210), radius = 150
		g.drawOval(10, 10, 400, 400);
		g.drawOval(60, 60, 300, 300); 
		//draw the line markers centered at x value 110, 130, 150 ... 310
		for(int x = 110; x <= 310; x+= 20){
			if(x == 210){
				g.drawLine(x, 210- 10, x, 210 + 10); //make the center line longer
			}
			else{
				g.drawLine(x, 210 - 5, x, 210 + 5);
			}
		}
		//draw | markers
		g.drawLine(60,210,80,210);
		g.drawLine(210,60,210,80);
		g.drawLine(210,340,210,360);
		g.drawLine(360,210,340,210);
		
		//draw TO FROM
		if(global.dir.equals("TO")){
			g.drawString("TO", 280, 170);
			
			g.drawString("Signal: GOOD", 250, 450);
		}
		if(global.dir.equals("FROM")){
			g.drawString("FR", 280, 260);
			
			g.drawString("Signal: GOOD", 250, 450);
		}
		if(global.dir.equals("RED")){
			g.drawString("TO", 280, 170);
			g.drawString("FR", 280, 260);
			

			g.drawString("Signal: BAD", 250, 450);
		}
		
		//draw stationID (moving)
		//g.drawString("Station ID: " + global.ID, 180, 450);
		g.drawString("Station ID: " + global.ID, 100, 450);
		
		//draw the outer markers (moving)
		for(int x = 0; x <= 360; x+= 15){
			this.getCoordinate(x + global.offset, 150);
			g.drawOval((int)x1 - 2, (int)y1 - 2, 4, 4);
		}
		//draw VOR needle
		g.drawLine(210 + (int)(10 * global.bar), 210 + 50, 210 + (int)(10 * global.bar), 210-50);
		
		//draw N S W E symbols (moving)
		this.getCoordinate(0 + global.offset, 170); // for N
		g.drawString("N", (int)x1, (int)y1);
		
		this.getCoordinate(90  + global.offset, 170); // for E
		g.drawString("E", (int)x1, (int)y1);
		
		this.getCoordinate(180  + global.offset, 170); // for S
		g.drawString("S", (int)x1, (int)y1);
		
		this.getCoordinate(270  + global.offset, 170); // for W
		g.drawString("W", (int)x1, (int)y1);
		
	}
	
}

class global{
	public static int offset = 0;
	public static int ID = 0;
	public static double bar = 0; //deflection needle, 10 is max
	public static String dir = ""; //to, from
	public static double getDegree(double original, double offset){
		double degree = original - offset;
		while(degree < 0){
			degree += 360;
		}
		while(degree > 359){
			degree -= 360;
		}
		return degree;		
	}
	
}