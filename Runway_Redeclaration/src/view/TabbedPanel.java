package view;

import java.util.Collection;
import java.util.HashMap;

import javax.swing.JTabbedPane;

import CoreInterfaces.AirfieldInterface;
import CoreInterfaces.AirportInterface;
/**
 * This is just a holder class for all the tabs.
 * @author shakib-binhamid
 *
 */
public class TabbedPanel extends JTabbedPane{

	private AirportInterface airport;
	private HashMap<String, Tab> tabs;
	
	public TabbedPanel(){
		tabs = new HashMap<>();
	}
	
	public void updateTabs(AirportInterface airport){
		tabs.clear();
		this.removeAll();
		
		this.airport = airport;
		
		Collection<AirfieldInterface> airfields = airport.getAirfields();
		
		for(AirfieldInterface airfield: airfields){
			tabs.put(airfield.getName(), new Tab(airfield));
			this.addTab(airfield.getName(), new Tab(airfield));
		}
		
	}

	public AirportInterface getAirport() {
		this.getSelectedComponent();
		return airport;
	}

	public void setAirport(AirportInterface airport) {
		this.airport = airport;
	}
	
	public Tab getTab(String airfieldId){
		return tabs.get(airfieldId);
	}
}