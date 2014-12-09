/**
 * Simulated Radio class.
 */
import java.util.Random;

public class Radio {
	int seed;
	int badCounter = 5;
	int ID1 = seed + 767;
	int ID2 = seed + 321;
	double startRad = 0;
	public Radio(int seed){
		this.seed = seed;
	}
	public double getRadial(){
		double radial;
		Random dice = new Random();
		radial = dice.nextDouble() * 3.45;
		int tick = dice.nextInt(2);
		if(tick > 0){
			return startRad + radial;
		}
		return startRad + 360 - radial;
	}
	
	public int getID(){
		Random rnd = new Random();
		int a = rnd.nextInt(2);
		if(a == 0){
			return this.ID1;
		}
		return ID2;
	}
}