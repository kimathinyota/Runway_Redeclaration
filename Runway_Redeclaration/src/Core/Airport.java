package Core;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import CoreInterfaces.AirfieldInterface;
import CoreInterfaces.AirportInterface;
import CoreInterfaces.Savable;
import Exceptions.CannotMakeRunwayException;
import Exceptions.UnrecognisedAirfieldIntifierException;
import Exceptions.VariableDeclarationException;

@Root
/**
 * This is what represents an Airport, airports can have multiple airfields.
 * Please note that an airfield cannot handle more than 3 parallel runways.
 *  
 * This class discourages the use of constructing new AirfieldInterface implementations 
 * and parsing them as arguments to be stored in this object. This is to prevent 
 * two or more airports having the same airfield.
 * 
 * @author Stefan
 *
 */
public class Airport implements AirportInterface, Savable {
	@ElementList
	private List<AirfieldInterface> airfields;
	@Attribute
	private String name;

	//nullary constructor
	public Airport(){
		//Used for XML
	}
	
	public Airport(String name) {
		this.name = name;
		this.airfields = new ArrayList<>();
	}
	
	@Override
	public List<AirfieldInterface> getAirfields() {
		return this.airfields;
	}

	@Override
	public AirfieldInterface getAirfield(String name) throws UnrecognisedAirfieldIntifierException {
		for(AirfieldInterface af : getAirfields()){
			if(af.getName().equals(name)){
				return af;
			}
		}
		throw new UnrecognisedAirfieldIntifierException(this, name);
	}

	@Override
	public List<String> getAirfieldNames() {
		List<String> names = new ArrayList<String>();
		for(AirfieldInterface af : getAirfields()){
			names.add(af.getName());
		}
		return names;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void addNewAirfield(int angleFromNorth, char side,
			double[] smallAngledDistances, double[] largeAngledDistances) 
					throws CannotMakeRunwayException, VariableDeclarationException {
		
		AirfieldInterface newAirfield = new Airfield(angleFromNorth, side, smallAngledDistances,largeAngledDistances);
		
		for(AirfieldInterface runway : getAirfields()){
			if(runway.getName().equals(newAirfield.getName())){
				throw new CannotMakeRunwayException(newAirfield,"that identifier is already in use");
			}
		
		}
		this.airfields.add(newAirfield);
//		NotificationPanel.notifyIt(newAirfield.getName() + " created", newAirfield.getName() + " added to "+ this.getName(), Notification.FILE);
		
	}
	
	
	public String toString(){
		String out = getName()+": [";
		for(int i = 0; i < getAirfieldNames().size()-1; i++){
			out += getAirfields().get(i).getName()+", ";
		}
		out += getAirfields().get(getAirfields().size()-1).getName();
		return out+"]";
	}


}
