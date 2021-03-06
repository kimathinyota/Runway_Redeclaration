package view.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import core.concrete.PositionedObstacle;
import core.interfaces.AirfieldInterface;
import core.interfaces.DeclaredRunwayInterface;

/**
 * 
 * @author Shakib-Bin Hamid
 * @editor Stefan Collier
 */
public class ViewTop extends AbstractView{
	/**
	 * [X] Version 0: Nada Complete
	 * [X] Version 1: Points 
	 * [X] Version 2: Rotate Points 
	 * [X] Version 3: Scale/Zoom 
	 * [/] Version 4: Pan by focus points 
	 */
	private static final long serialVersionUID = 35L;


	private static int DT_TO_BARCODE_LENGTH_RATIO_TO_TORA = 100;
	private static int TORA_TO_DASH_RATIO = 100;
	private static int TORA_TO_ZEBRA_CODE_RATIO = 20;

	public static final Color SURROUNDING_AREA_COLOR = new Color(128,128,255);
	public static final Color CLEARED_BLUE_COLOR = new Color(0,128,255);


	public ViewTop(AirfieldInterface airfield, DeclaredRunwayInterface runway) {
		super(airfield, runway, "Top View", true, true);
	}

	protected double init_LEFT_SIDE_BUFFER_M(){ return 60d; }
	protected double init_RIGHT_SIDE_BUFFER_M(){ return 60d; }
	protected double init_TOP_SIDE_BUFFER_M(){ return 0d; }
	protected double init_BOTTOM_SIDE_BUFFER_M(){ return 0d; }

	//======[ Drawing ]=====================================================================================================
	@Override
	protected void drawImage(Graphics2D g2) {
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2.setRenderingHints(rh);
		//Set for default bordering 
		g2.setColor(Color.BLACK);


		//~~[ Begin Drawing ]~~~~~~~~~~~~~~~~~~~~~
		drawBackground(g2);

		//Blue, Purple & runway
		drawAirfield(g2);

		drawThresholds(g2);
		drawClearwayAndStopway(g2);

		//Tora, lda, etc
		drawDistances(g2);


		if(getAirfield().hasObstacle())
			drawObsacle(g2);

		double distanceBetweenThem = 30d;
		int h = !getRunway().isSmallEnd()? -1 : 1;/* helper*/

		drawArrowAround(g2, new Point(runwayWidth()/2+h*distanceBetweenThem,vertToRunway()+getAirfield().getLongSpacer()+2*runwayWidth()/21), runwayWidth()/10, !getRunway().isSmallEnd(), Color.RED, MAROON_COLOUR);
		drawPlane(g2, runwayWidth()/21, new Point(runwayWidth()/2-h*runwayWidth()/20-h*distanceBetweenThem,vertToRunway()+getAirfield().getLongSpacer()+2*runwayWidth()/21), !getRunway().isSmallEnd());

	}

	private void drawAirfield(Graphics2D g2) {
		drawSurroundingArea(g2);
		drawClearedArea(g2);
		drawRunway(g2);
		drawCentreLine(g2);
		drawZebraCrossings(g2);
	}
	//----[ Specific Distance ]-------------------------------------------------------------------
	private void drawBackground(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();

		//We draw in raw pixels so that the grass is unaffected by rotation
		g2.setColor(GRASS_COLOUR);
		g2.drawRect(0, 0, getImage().getWidth(), getImage().getHeight());
		g2.fillRect(0, 0, getImage().getWidth(), getImage().getHeight());

	}
	/** Purple region */
	private void drawSurroundingArea(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();

		double top = vertToRunway() - getAirfield().getLongSpacer();
		double left = leftOfRunway() - getAirfield().getStripEnd();

		Point topLeft = new Point(left,top);
		double boxWidth  = rightOfRunway()-leftOfRunway() + 2*getAirfield().getStripEnd();
		double boxHeight = getAirfield().getLongSpacer()*2;

		g2.setStroke(new BasicStroke(0.5f));
		super.drawRectangle_inM(g2, topLeft,boxWidth, boxHeight, SURROUNDING_AREA_COLOR);
	}	
	/** Blue region */ 
	private void drawClearedArea(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		//All finals are stored for quick reference
		final double SE = getAirfield().getStripEnd();
		final double sSpacer = getAirfield().getShortSpacer();
		final double mSpacer = getAirfield().getMediumSpacer();

		final double sLength = getAirfield().getShortLength();
		final double lLength = getAirfield().getLongLength();

		final double runWidth = rightOfRunway()-leftOfRunway();

		double mostLeft = leftOfRunway()-SE;
		double above_Short = vertToRunway() - sSpacer;		
		Point leftMost_above = new Point(mostLeft,above_Short);

		Point p2 = leftMost_above.offsetXByM(SE+sLength);
		Point p3 = leftMost_above.offsetXByM(SE+lLength).offsetYByM(sSpacer-mSpacer);
		Point p4 = leftMost_above.offsetXByM(SE+runWidth-lLength).offsetYByM(sSpacer-mSpacer);
		Point p5 = leftMost_above.offsetXByM(SE+runWidth-sSpacer);
		Point p6 = p5.offsetXByM(sSpacer+SE);

		Point p7 = p6.offsetYByM(sSpacer*2);
		Point p8 = p7.offsetXByM(-(SE+sLength));
		Point p9 = p7.offsetXByM(-(SE+lLength)).offsetYByM(mSpacer-sSpacer);

		Point p12 = leftMost_above.offsetYByM(sSpacer*2);
		Point p11 = p12.offsetXByM(SE+sLength);
		Point p10 = p12.offsetXByM(SE+lLength).offsetYByM(mSpacer-sSpacer);


		Point[] points = new Point[]{leftMost_above, p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12};

		g2.setStroke(new BasicStroke(0.5f));
		super.drawPolygon_inM(g2, points, CLEARED_BLUE_COLOR);
	}

	private void drawZebraCrossings(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.WHITE);
		final BasicStroke thick = new BasicStroke(Xm_to_pixels(getAirfield().getRunwayGirth())/20f);
		g2.setStroke(thick);

		double defTora;
		if(getRunway().isSmallEnd()){
			defTora =getAirfield().getDefaultSmallAngledRunway().getTORA();			
		}else{
			defTora =getAirfield().getDefaultLargeAngledRunway().getTORA();			
		}

		double dtToBar = defTora/DT_TO_BARCODE_LENGTH_RATIO_TO_TORA;
		double zebraLength = defTora/TORA_TO_ZEBRA_CODE_RATIO;
		int noOfLines= 5; 

		double y = vertToRunway()-getAirfield().getRunwayGirth()/2;
		double vertDrop = getAirfield().getRunwayGirth()/noOfLines+1;


		//LeftSide
		double startXL = leftOfRunway()+getAirfield().getDefaultSmallAngledRunway().getDisplacedThreshold()+dtToBar;

		Point startLine = new Point(startXL,y+vertDrop/2);
		Point endLine;

		for(int i = 0; i < noOfLines; i++){
			endLine = startLine.offsetXByM(zebraLength);
			super.drawLine_inM(g2, startLine, endLine);
			startLine = startLine.offsetYByM(vertDrop);
		}

		//rightSide
		double startXR = rightOfRunway()-(getAirfield().getDefaultLargeAngledRunway().getDisplacedThreshold()+dtToBar+zebraLength);

		startLine = new Point(startXR,y+vertDrop/2);
		for(int i = 0; i < noOfLines; i++){
			endLine = startLine.offsetXByM(zebraLength);
			super.drawLine_inM(g2, startLine, endLine);
			startLine = startLine.offsetYByM(vertDrop);
		}

		IDENTIFIER_COLOR = Color.WHITE;
		double textSpace = g2.getFontMetrics().getHeight()*2;
		drawIdentifiers(g2, vertToRunway()-1, startXL+zebraLength+6*textSpace, startXR-zebraLength-textSpace,true);
	}

	private void drawCentreLine(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		double defTora;
		int defGirthPix = Xm_to_pixels(getAirfield().getRunwayGirth());

		if(getRunway().isSmallEnd()){
			defTora =getAirfield().getDefaultSmallAngledRunway().getTORA();			
		}else{
			defTora =getAirfield().getDefaultLargeAngledRunway().getTORA();			
		}

		final float dash1[] = {Xm_to_pixels(defTora)/TORA_TO_DASH_RATIO+1};
		final BasicStroke dashed =
				new BasicStroke(defGirthPix/25f+0.0001f,
						BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER,
						10.0f, dash1, 0.0f);
		g2.setStroke(dashed);

		double dtToBar = defTora/DT_TO_BARCODE_LENGTH_RATIO_TO_TORA;
		double zebras = defTora/TORA_TO_ZEBRA_CODE_RATIO;
		int fontSize = Ym_to_pixels(3*getAirfield().getRunwayGirth()/4)-1;
		double buffer = 30+Xpix_to_m(4*fontSize);

		double startX = leftOfRunway()+getAirfield().getDefaultSmallAngledRunway().getDisplacedThreshold()+zebras+dtToBar+buffer;
		double endX = rightOfRunway()-(getAirfield().getDefaultLargeAngledRunway().getDisplacedThreshold()+zebras+dtToBar+buffer);
		Point start = new Point(startX,vertToRunway());
		Point end = new Point(endX,vertToRunway());

		g2.setColor(Color.WHITE);
		super.drawLine_inM(g2, start, end);
	}

	private void drawRunway(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();

		double topOfRunway = vertToRunway()-getAirfield().getRunwayGirth()/2;
		Point topLeft = new Point(leftOfRunway(),topOfRunway);

		double runWidth = rightOfRunway()-leftOfRunway();
		double runHeight = getAirfield().getRunwayGirth();

		g2.setStroke(new BasicStroke(0.5f));
		super.drawRectangle_inM(g2, topLeft, runWidth, runHeight, RUNWAY_COLOUR);
	}
	
	private void drawThresholds(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();

		double leftThresh = leftOfRunway()+getAirfield().getSmallAngledRunway().getDisplacedThreshold();
		double rightThresh = rightOfRunway()-getAirfield().getLargeAngledRunway().getDisplacedThreshold();
		double topOfRunway = vertToRunway()-getAirfield().getRunwayGirth()/2;

		Point leftMarker = new Point(leftThresh, topOfRunway);
		Point rightMarker = new Point(rightThresh,topOfRunway);

		g2.setColor(Color.RED);
		super.drawLine_inM(g2, leftMarker, leftMarker.offsetYByM(getAirfield().getRunwayGirth()));
		super.drawLine_inM(g2, rightMarker, rightMarker.offsetYByM(getAirfield().getRunwayGirth()));
	}
	
	private void drawDistances(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int level = 1;
		super.DIMENSION_GAP = getAirfield().getRunwayGirth()/2;
		super.INITIAL_BUFFER = getAirfield().getLongSpacer()+10;
		super.PIXEL_BUFFER = Ym_to_pixels((vertToRunway()-INITIAL_BUFFER)/6);
		super.PIXEL_BUFFER = Math.max(super.PIXEL_BUFFER, 12);
		
		drawDistance(g2, "LDA", getRunway().getLDA(), getRunway().getDisplacedThreshold(), -level++, vertToRunway());
		drawDistance(g2, "TORA", getRunway().getTORA(), getRunway().getStartOfRoll(), -level++, vertToRunway());
		drawDistance(g2, "ASDA", getRunway().getASDA(), getRunway().getStartOfRoll(), -level++, vertToRunway());
		drawDistance(g2, "TODA", getRunway().getTODA(), getRunway().getStartOfRoll(), -level++, vertToRunway());
	}
	
	private void drawClearwayAndStopway(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		double girth = getAirfield().getRunwayGirth();
		double topOfrunway = vertToRunway()-girth/2;

		Point startStop,startClear;

		if(getRunway().isSmallEnd()){
			startStop = new Point(rightOfRunway(),topOfrunway);
		}else{
			startStop = new Point(leftOfRunway(),topOfrunway);
		}
		startClear = startStop.offsetYByM(-girth/2);

		if(getRunway().getClearway()>0){

			g2.setColor(CLEARWAY_COLOR);
			super.drawRectangle_inM(g2, startClear, s()*getRunway().getClearway(), 2*girth, getTransparant(g2.getColor(), 200));

			if(getRunway().getStopway() > 0){
				g2.setColor(STOPWAY_COLOR);
				super.drawRectangle_inM(g2, startStop,s()*getRunway().getStopway(),girth, getTransparant(g2.getColor(), 150));
			}
		}
	}

	private void drawObsacle(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();

		PositionedObstacle obj = (PositionedObstacle) getAirfield().getPositionedObstacle();
		if (obj==null) return;

		double xPos;
		if(getRunway().isSmallEnd()){
			xPos = leftOfRunway() + getAirfield().getDefaultSmallAngledRunway().getDisplacedThreshold()
					+ obj.distanceFromSmallEnd();
		}else{
			xPos = leftOfRunway() + getAirfield().getDefaultLargeAngledRunway().getDisplacedThreshold()
					+ obj.distanceFromLargeEnd();
		}

		Point centre = new Point(xPos,vertToRunway());

		//~~[ Has Radius ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		double radius = obj.getRadius();
		if(radius>0){
			g2.setColor(Color.RED);
			super.drawCircle_inM(g2, centre, radius, getTransparant(g2.getColor(), 100));
		}

		//~~[ Determining if RESA/ALS/Blast ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		double largestFactor; 
		String factorName;

		double ALS = getRunway().getAscentAngle()*getAirfield().getPositionedObstacle().getHeight();
		double SE = getAirfield().getStripEnd();

		//find largest factor
		if(getRunway().getRESA()+SE > ALS+SE &&  ALS+SE > getAirfield().getBlastAllowance()){
			largestFactor = getRunway().getRESA()+SE;
			factorName = "RESA";

		}else if(ALS > getAirfield().getBlastAllowance()){
			largestFactor =  ALS;
			factorName = "ALS";

		}else{
			largestFactor = getAirfield().getBlastAllowance()+SE;
			factorName = "Blast Zone";

		}

		//~~[ draw largest factor ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//must include obs radius
		double affectedRadius = radius + largestFactor;
		g2.setColor(Color.RED);
		super.drawCircle_inM(g2, centre, affectedRadius, getTransparant(g2.getColor(), 50));

		//~~[ text of largest factor ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		g2.setColor(Color.BLACK);
		int size = 10;
		g2.setFont(new Font("verdana", Font.BOLD, size));

		double heightOfText_M = Ypix_to_m(g2.getFontMetrics().getHeight());
		Point textPoint = centre.offsetXByM(affectedRadius).offsetYByM(getAirfield().getLongSpacer()+heightOfText_M);

		super.drawString_inM(g2, factorName, textPoint);

		//~~[ planes ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Graphics2D g4 = (Graphics2D) g.create();
		if (getAirfield().getPositionedObstacle().getName().matches(".*[a-zA-Z][0-9]+.*")) {
			drawPlane(g4, obj.getRadius(), centre, new Random().nextBoolean());
		}

		//~~[ No Radius ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Draw X at spot
		g2.setColor(Color.RED);
		double l = getAirfield().getRunwayGirth()/6;
		g2.setStroke(new BasicStroke(3));
		super.drawLine_inM(g2, centre.offsetXByM(l).offsetYByM(l),  centre.offsetXByM(-l).offsetYByM(-l));
		super.drawLine_inM(g2, centre.offsetXByM(l).offsetYByM(-l),  centre.offsetXByM(-l).offsetYByM(l));

	}

	//======[ Common Distance Methods ]===================================================================
	@Override
	protected double runwayWidth() {
		double defTODA;
		if(getRunway().isSmallEnd()){
			defTODA = getAirfield().getDefaultSmallAngledRunway().getTODA();
		}else{
			defTODA = getAirfield().getDefaultLargeAngledRunway().getTODA();
		}
		return getAirfield().getStripEnd()+defTODA+getAirfield().getStripEnd();
	}

	@Override
	protected double largestHeight() {
		return super.Xpix_to_m(super.getSUB_IMAGE_HEIGHT());
	}

	@Override
	/** Runway mid */
	protected double vertToRunway() {
		return largestHeight()/2;
	}

	@Override
	protected double leftOfRunway() {
		if(getRunway().isSmallEnd()){
			return getAirfield().getStripEnd();			
		}else{
			return runwayWidth()-getAirfield().getStripEnd()-getAirfield().getDefaultLargeAngledRunway().getTORA();
		}
	}

	@Override
	protected double rightOfRunway(){
		if(getRunway().isSmallEnd()){
			return getAirfield().getStripEnd()+getAirfield().getDefaultSmallAngledRunway().getTORA();			
		}else{
			return runwayWidth()-getAirfield().getStripEnd();
		}
	}

	@Override
	protected int[] getScaleLocation() {
		int bottomOfScreen = getSUB_IMAGE_HEIGHT();
		return new int[]{10,bottomOfScreen-10};
	}

	@Override
	protected boolean doesScalePointDown() {
		return false;
	}

	@Override
	protected double metresToScale() {
		return 500d;
	}

}
