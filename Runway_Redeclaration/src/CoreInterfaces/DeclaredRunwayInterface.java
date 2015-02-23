package CoreInterfaces;

import Exceptions.UnusableRunwayException;
import Exceptions.VariableDeclarationException;

/**
 * 
 * This represents a newly declared runway.
 *  It will include any it's angle and direction to identify it and the 
 * 
 * @author Stefan
 * @Editors Stefan
 *
 */
public interface DeclaredRunwayInterface {
	
	/** 
	 * The direction/side of the runway we are referring to.
	 * 
	 * @return 'L' for left or 'R' for right or 'C' for centre
	 */
	char getSideLetter();
	
	void setSideLetter(char side) throws VariableDeclarationException;
	
	/**
	 * The anti-clockwise angle in degrees of the that runway from North
	 * e.g. 150 (degrees) 
	 */
	int getAngle();
	
	/**
	 * The way of uniquely identifying a runway
	 * e.g. 27R  or 03L
	 * @return 
	 */
	String getIdentifier();
	
	/**
	 * Take-Off-Runway-Available
	 */
	double getTORA() throws UnusableRunwayException;
	
	double getClearway() throws UnusableRunwayException;
	
	double getStopway() throws UnusableRunwayException;
	
	double getDisplacedThreshold() throws UnusableRunwayException;
	
	/**
	 * ASDA = TORA + Stopway
	 */
	double getASDA() throws UnusableRunwayException;
	
	/**
	 * TODA = TORA + Clearway
	 */
	double getTODA() throws UnusableRunwayException;
	
	/**
	 * LDA = TORA - Displaced Threshold
	 */
	double getLDA() throws UnusableRunwayException;
	
	double getRESA() throws UnusableRunwayException;
	
//=====[ Mutators ]=================================================
	void landOver(DeclaredRunwayInterface original, AirfieldInterface parent) throws UnusableRunwayException;
	
	void landTowards(DeclaredRunwayInterface original, AirfieldInterface parent);
	
	void takeOffAwayFrom(DeclaredRunwayInterface original, AirfieldInterface parent);
	
	void takeOffTowardsOver(DeclaredRunwayInterface original, AirfieldInterface parent);
}
