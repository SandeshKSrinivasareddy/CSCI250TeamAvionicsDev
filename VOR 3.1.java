/**
 * Representation of a VOR.  With each new radio signal, calculated values 
 * are stored as variables that can be accessed from another class.
 */
public class VOR {
	
	/* radio inputs, */
	
	double stationRad; 
	String sigQuality; //ignore for now
	int stationCode;  //ignore for now
	
	/* pilot/GUI input */
	
	double pilotRad; 
	
	/* Calculated values, updated with every new signal*/
	
	String direction = ""; //either "to"/"from"/"bad"
	double degOffset; //number of degrees from doubleendedRad
	double side; //which side is the needle tilting, -1 = left, 1 = right
	
	/**
	 * Constructor, create a new VOR object
	 */
	public VOR(){
		
	}
	/**
	 * Give this VOR a new input signal, updates calculated values and stationCode.
	 * @param radial The radial input from radio.
	 * @param stationCode Station ID code.
	 * @param quality True = good signal, false = bad signal
	 * @param doubleendedRad The radial from pilot.
	 */
	public void newSignal(double radial, int stationCode, String quality, double doubleendedRad){
		this.stationRad = radial; 
		this.stationCode = stationCode; //irrelevant for now
		this.sigQuality = quality;
		this.pilotRad = doubleendedRad;
		//update calculated values
		this.direction = calcToFrom(); //to, from or red
		double degOff = calcDeflection();
		if(degOff > 10){
			this.degOffset = 10;
		}
		else{
			this.degOffset = degOff;
		}
	}
	
}