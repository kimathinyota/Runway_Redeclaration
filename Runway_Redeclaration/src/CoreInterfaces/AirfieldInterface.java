package CoreInterfaces;

import Exceptions.InvalidIdentifierException;
import Exceptions.VariableDeclarationException;


/**
 * //TODO add a docstring
 *  
 * @author Stefan 
 * @Editor Stefan
 *
 */
public interface AirfieldInterface {
//====[ Misc Methods ]===========================================
	String getName();
	
	/**
	 * Valid 'letter's are 'C', 'L' or 'R'
	 * By changing the smallest side letter, this method must 
	 * automatically update the other side as well.
	 * @throws VariableDeclarationException 
	 */
	void setSmallestSideLetter(char letter) throws VariableDeclarationException;
//===============================================================

//====[ Inert Distance Values ]==================================
	/**
	 * The girth of the tarmac runway
	 */
	double getRunwayGirth();
	
	void setRunwayGirth(double girth);
	
	/**
	 * The extra distance on the left side (behind) the left tora of the runway 
	 * that make up longest reach of the cleared and graded Area
	 * 
	 * (60m horizontal on the diagram)
	 */
	double getLeftStripEnd();
	
	void setLeftStripEnd(double lStrpEnd);

	
	/**
	 * The extra distance on the right side (behind) the right tora of the runway 
	 * that make up longest reach of the cleared and graded Area
	 * 
	 * (60m horizontal on the diagram)
	 */
	double getRightStripEnd();
	
	void setRightStripEnd(double rStrpEnd);
	
	/**
	 * (150m vert on diagram)
	 */
	double getLongSpacer();
	
	void setLongSpacer(double lSpacer);
	
	/**
	 * (105m vert on diagram)
	 */
	double getMediumSpacer();
	
	void setMediumSpacer(double mSpacer);

	/**
	 * (75m vert on diagram)
	 */
	double getShortSpacer();
	
	void setShortSpacer(double sSpacer);
	
	/**
	 * (150m horiz on diagram)
	 */
	double getShortLength();
	
	void setShortLength(double sLength);
	
	/**
	 * (3000m horiz on diagram)
	 */
	double getLongLength();
	
	void setLongLength(double lLength);
	
	double getBlastAllowance();
	
	void setBlastAllowance(double blast);
	
	double[] getDimensionsToArray();
	
	double getTotalWidth();
	
	double getTotalHeight();
	
	
//=====================================================	

//====[ Obstacles ]====================================
	/**
	 * Returns null if there is no Obstacle or a PositionedObstacle implementation
	 */
	PositionedObstacleInterface getPositionedObstacle();
	
	/**
	 * Changing/adding the obstacle on the tarmac. 
	 * Will cause a re-declaration  of both declared runways to be stored in this class.
	 * 
	 * Even though an ObstacleInterface is used to add an object,
	 *  a PositionedObstacleObject must be stored. An internal conversion must be made.
	 * @throws UnusableRunwayException 
	 */
	//void addObstacle(ObstacleInterface obj, double distanceFromSmall, double distanceFromLarge) throws InvalidIdentifierException;
	
	void addObstacle(ObstacleInterface obj, String indentifier, double howFarIn) throws InvalidIdentifierException;
	
	
	/** 
	 * Changes the obstacle that is on the runway to null 
	 */
	void removeObstacle();
	
	/**
	 *  true or false whether there is an obstacle on the tarmac
	 */
	boolean hasObstacle();
//===============================================================
	
	
//====[ Declared Runways ]=======================================
	//---[ Active ]--------------------------------------
	DeclaredRunwayInterface[] getRunways();
	
	DeclaredRunwayInterface getSmallAngledRunway();
	
	DeclaredRunwayInterface getLargeAngledRunway();
	
	//---[ Default/Origonal ]------------------------------
	DeclaredRunwayInterface[] getDefaultRunways();
	
	DeclaredRunwayInterface getDefaultLargeAngledRunway();

	DeclaredRunwayInterface getDefaultSmallAngledRunway();
//===============================================================


}
