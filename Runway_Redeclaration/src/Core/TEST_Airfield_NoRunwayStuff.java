package Core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import Exceptions.InvalidIdentifierException;

public class TEST_Airfield_NoRunwayStuff {
	public static final double[] airfieldVars = {100,3500,200,100,200,300,400,500};
	public static final double[] smallVars = {3660,0,0,7};
	public static final double[] largeVars = {3660,0,0,0};
	public static final int angle = 340;
	
	public Airfield air;
	
	
	@Before
	public void setUp() throws Exception {
		this.air = new Airfield(angle, airfieldVars,smallVars,largeVars);
	}

	@Test
	public void testNoObstacle() {
		assertEquals(null, this.air.getObstacle());
	}
	
	@Test
	@Ignore
	public void testAddingObstacle(){
		Obstacle obj = new Obstacle("Spongebob", 2.3, 4.5);
		try {
			this.air.addObstacle(obj, "jim", 20);
		} catch (InvalidIdentifierException e) {
			e.printStackTrace();
			fail("You're an idiot, choose a valid identifier");
		}
	}
	
	@Test
	public void testRemovingObstacle(){
		
	}
	
	@Test
	public void testChangingObstacle(){
		
	}

}
