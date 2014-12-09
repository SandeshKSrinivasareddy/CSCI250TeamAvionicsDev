import java.awt.Dimension;

import javax.swing.*;

public class Driver {
	public static void main(String[] args){
		JFrame window = new JFrame("VOR");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Interface VOR = new Interface();
		window.setContentPane(VOR.container);
		window.setPreferredSize(new Dimension(436,600));
		window.pack();
		window.setVisible(true);
	}
}