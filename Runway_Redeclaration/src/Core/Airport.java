package Core;

import java.util.ArrayList;
import java.util.List;

import CoreInterfaces.AirfieldInterface;
import CoreInterfaces.AirportInterface;
import CoreInterfaces.Savable;
import Exceptions.CannotMakeRunwayException;
import Exceptions.ParrallelRunwayException;
import Exceptions.UnrecognisedAirfieldIntifierException;
import Exceptions.VariableDeclarationException;

public class Airport implements AirportInterface, Savable {
	private List<AirfieldInterface> airfields;
	private String name;

	
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
	public void addNewAirfield(int angleFromNorth, double[] dimensions) throws ParrallelRunwayException, CannotMakeRunwayException, VariableDeclarationException {
		angleFromNorth %= 180;
		
		AirfieldInterface newAirfield = new Airfield(angleFromNorth, dimensions);
		
		//Identify parallel runways
		List<AirfieldInterface> parrallelRunways = new ArrayList<AirfieldInterface>();
		for(AirfieldInterface runway : getAirfields()){
			int angle = runway.getSmallAngledRunway().getAngle()/10;
			
			//parallel runways would have the same angle part of the identifier
			if(angleFromNorth == angle){
				parrallelRunways.add(runway);
			}
		}
		if(!parrallelRunways.isEmpty()){
			if(parrallelRunways.size()>=3) {
				throw new CannotMakeRunwayException(newAirfield);
			}
			throw new ParrallelRunwayException(this, parrallelRunways, newAirfield);
		}
		
		this.airfields.add(newAirfield);
	}



}
