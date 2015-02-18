package Core;

import CoreInterfaces.AirfieldInterface;
import CoreInterfaces.DeclaredRunwayInterface;
import Exceptions.VariableDeclarationException;

/**
 * This so far is a stub for editing
 * 
 * @author Shakib
 * @Editor Stefan
 * @Tester 
 *
 */
public class DeclaredRunway implements DeclaredRunwayInterface{

	private double decTora;

	private double disThreshold, stopway, clearway;

	private int angle, ascentAngle, descentAngle;

	private char direction;

	/**
	 * 
	 * @param runway - The airfield with the runway on it that you want to redeclare
	 * @param displacedThreshold - The user defined threshold to start from
	 * @param angleFromNorth - The angle (in degrees) divided by 10 and rounded to the nearest 10
	 * @param leftOrRight - Which side of the runway the plane will start from/take off to
	 * 
	 * @throws VariableDeclarationException is thrown when an invalid distance variable is declared
	 */
	public DeclaredRunway(AirfieldInterface runway, double displacedThreshold ,int angleFromNorth, char leftOrRight) throws VariableDeclarationException{
		
		setDisplacedThreshold(displacedThreshold);
		if(!runway.hasObstacle()){
			
			setTORA(runway.getRunwayLength());
			setStopway(runway.getInitialStopway());
			
			double clearway = getStopway()+runway.getClearedLength();
			setClearway(clearway);
			
		}else{
			//These are garuneteed to fail, as we have not handled the obstacle scenarios yet!
			//TODO handle an obstacle
			setTORA(-1);
			setStopway(-1);
			setClearway(-1);
		}
		//TODO include final check: TORA <= ASDA <= TODA 

		ascentAngle = DeclaredRunwayInterface.DEFAULT_ANGLE_OF_ASCENT;
		descentAngle = DeclaredRunwayInterface.DEFAULT_ANGLE_OF_DESCENT;

		angle = angleFromNorth;
		direction = leftOrRight;
	}

//====[ Direction Methods  ]=====================================
//----[ Getters ]------------------------------------------------------
	@Override
	public char getDirection() {
		return direction;
	}

	@Override
	public int getAngle() {
		return angle;
	}

	@Override
	public String getIdentifier() {
		String out = "";
		if(angle<10){
			out += "0";
		}
		out += String.valueOf(angle)+direction;
		return out;
	}
//----[ Setters ]------------------------------------------------------
	private void setDirection(char leftOrRight) throws VariableDeclarationException{
		//If LoR is not L or R
		if ( !(leftOrRight == 'L' || leftOrRight == 'R') )
			throw new VariableDeclarationException("Direction",leftOrRight,"Must be 'L' or 'R'");
		
		this.direction = leftOrRight;
	}
	
	public void setAngle(int angle) {
		while(angle<0){
			angle += 36;
		}
		this.angle = angle%36;
	}
	
	
//====[ Inert Direction Methods  ]=====================================
//----[ Getters ]------------------------------------------------------
	@Override
	public double getTORA() {
		return decTora;
	}

	@Override
	public double getClearway() {
		return clearway;
	}

	@Override
	public double getStopway() {
		return stopway;
	}

	@Override
	public double getDisplacedThreshold() {
		return disThreshold;
	}
//----[ Setters ]---------------------------------------------------------
	private void setTORA(double tora) throws VariableDeclarationException{
		if(tora <= 0) throw new VariableDeclarationException("TORA", tora, "TORA > 0");

		this.decTora = tora;
	}

	private void setDisplacedThreshold(double threshold) throws VariableDeclarationException{
		if(threshold < 0) throw new VariableDeclarationException("Displaced Threshold", threshold, "Threshold > 0");

		this.disThreshold = threshold;
	}

	private void setStopway(double stopway) throws VariableDeclarationException{
		if( stopway < 0 ) throw new VariableDeclarationException("Stopway", stopway, "Stopway >= 0");
	}

	private void setClearway(double clearway) throws VariableDeclarationException{
		if( clearway < 0 ) throw new VariableDeclarationException("Clearway", clearway, "Clearway > 0");
		if( clearway < getStopway() ) throw new VariableDeclarationException("Clearway", clearway, "Clearway >= Stopway");

		this.clearway = clearway;
	}

//====[ Calculated Distance Methods  ]=====================================
	@Override
	public double getASDA() {
		return getTORA()+getStopway();
	}

	@Override
	public double getTODA() {
		return getTORA()+getClearway();
	}

	@Override
	public double getLDA() {
		return getTORA()-getDisplacedThreshold();
	}


//====[ Angle Methods  ]==================================================
	@Override
	public int getAngleOfAscent() {
		return ascentAngle;
	}

	@Override
	public int getAngleOfDescent() {
		return descentAngle;
	}

}
