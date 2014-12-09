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
	
	/**
	 * Helper method, calculates the deflection of the bar/needle from center,
	 * @return negative = towards left, positive = towards right, 0 = centered
	 */
	private double calcDeflection(){
		double deflect = findDistance(pilotRad, stationRad, true);
		//closer to opposite side of pilotRad, calculate deflection from there
		if(deflect > 90){
			deflect = 180 - deflect;
		}
		if(deflect == 0){
			this.side = 0;
		}
		return deflect;		
	}
	
	/**
	 * Calculate whether from/to/between based on pilotRad and stationRad
	 * @return
	 */
	private String calcToFrom(){
		if(this.sigQuality.equals("bad")){
			return "RED";
		}
		//if radial is within 90degrees of pilot radial, direction is "from"
		if(findDistance(stationRad, pilotRad, false) <89){
			return "FROM";
		}
		if(findDistance(stationRad, pilotRad, false) > 91){
			return "TO";
		}
		//too close to 90 degrees, neither from nor to
		return "RED";
	}
	
}