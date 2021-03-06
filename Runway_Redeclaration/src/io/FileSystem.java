package io;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import view.customComponents.Notification;
import view.customComponents.NotificationPanel;
import core.concrete.Airport;
import core.concrete.Obstacle;
import core.interfaces.AirportInterface;
import core.interfaces.ObstacleInterface;
import core.interfaces.Savable;
import exceptions.NothingToSaveException;

/**
 * Handles the saving and loading of objects
 * @author Jonathan
 * @editor shakib-bin hamid
 *
 */
public class FileSystem {

	private static final String wd = "./";
	private static final String datDir = "/dat";
	private static final String airDir = "/airports";
	private static final String objDir = "/obs";

	private static final String xmlext = ".xml";
	private static final String objext = ".obj";
	private static final String airext = ".air";

	private File dat, airports, obstacles;

	public FileSystem(){
		makedirs();
	}

	//Ensures the save directories exist
	private void makedirs() {
		dat = new File(wd + datDir);
		airports = new File(dat.getAbsolutePath() + airDir);
		obstacles = new File(dat.getAbsoluteFile() + objDir);

		if(!dat.exists())
			dat.mkdirs();
		if(!airports.exists())
			airports.mkdirs();
		if(!obstacles.exists())
			obstacles.mkdirs();
	}

	//Returns a list of all files in dat
	public ArrayList<File> listAllFiles(){
		ArrayList<File> files = new ArrayList<File>();
		files.addAll(listAirports());
		files.addAll(listObstacles());

		return files;
	}

	//Returns a list of all stored airports
	public ArrayList<File> listAirports(){
		ArrayList<File> airportFiles = new ArrayList<File>();
		for(File file : airports.listFiles()){
			airportFiles.add(file);
		}
		return airportFiles;
	}

	//Returns a list of all stored obstacles
	public ArrayList<File> listObstacles(){
		ArrayList<File> objFiles = new ArrayList<File>();
		for(File file : obstacles.listFiles()){
			objFiles.add(file);
		}
		return objFiles;
	}

	public boolean saveObs(Obstacle o) throws NothingToSaveException{
		return saveObs(o, new File(getObsDir() + o.getName() + getObsExt()));
	}

	public boolean saveObs(Obstacle o, File savefile) throws NothingToSaveException{
		checkNull(o);
		String dir = savefile.getAbsolutePath();
		if(XMLSaver.serialise(o, dir)){
			NotificationPanel.notifyIt(o.getName()+" Saved", "Saving obstacle to \n" + dir + "...", Notification.FILE);
			return true;
		}
		else {
			return false;
		}
	}

	public ObstacleInterface loadObs(File obsFile){
		ObstacleInterface loadedObs = (ObstacleInterface) XMLSaver.deserialise(Obstacle.class, obsFile);
		return loadedObs;
	}

	public boolean saveAir(Airport a) throws NothingToSaveException{
		return saveAir(a, new File(getAirDir() + a.getName() + getAirExt()));
	}

	public boolean saveAir(Airport a, File savefile) throws NothingToSaveException{
		checkNull(a);
		String dir = savefile.getAbsolutePath();
		if(XMLSaver.serialise(a, dir)){
			NotificationPanel.notifyIt(a.getName()+" Saved", "Saving Airport "+a.getName()+ " to \n" + dir + "...", Notification.FILE);
			return true;
		}
		else {
			return false;
		}
	}

	private void checkNull(Savable a) throws NothingToSaveException {
		if(a == null){
			throw new NothingToSaveException();
		}
	}

	public AirportInterface loadAir(File airFile){
		AirportInterface a = (AirportInterface) XMLSaver.deserialise(Airport.class, airFile);
		return a;
	}

	//Returns true if chosen file is an airport file
	public boolean checkAir(File chosen) {
		try{
			String name = chosen.getName();
			if (name.split("\\.")[1].equals("air")){
				return true;
			}
		}
		catch (Exception e){

		}
		return false;
	}

	//Returns true if chosen file is an obstacle file
	public boolean checkObs(File chosen) {
		try{
			String name = chosen.getName();
			if (name.split("\\.")[1].equals("obj")){
				return true;
			}
		}
		catch (Exception e){

		}
		return false;
	}

	public boolean checkAirExt(File chosen) {
		String dir = chosen.getName();
		String[] exts = dir.split("\\.");

		if(exts.length < 3)								return false;
		if(!exts[exts.length - 1].equals("xml")) 		return false;
		if(!exts[exts.length - 2].equals("air")) 		return false;

		return true;

	}

	public boolean checkObsExt(File chosen) {
		String dir = chosen.getName();
		String[] exts = dir.split("\\.");

		if(exts.length < 3)								return false;
		if(!exts[exts.length - 1].equals("xml")) 		return false;
		if(!exts[exts.length - 2].equals("obj")) 		return false;

		return true;

	}

	//Getters for standard values
	public String getAirDir(){
		return wd + datDir + airDir + "/";
	}

	public String getObsDir(){
		return wd + datDir + objDir + "/";
	}

	public String getAirExt(){
		return airext + xmlext;
	}

	public String getAirExtOnly(){
		return airext;
	}

	public String getObsExt(){
		return objext + xmlext;
	}

	public String getXMLExt(){
		return xmlext;
	}

	//Returns an array list on every file under a given directory
	public ArrayList<File> getAllFiles(File folder) {

		ArrayList<File> files = new ArrayList<File>();

		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				for(File nestedFile : getAllFiles(file)){
					files.add(nestedFile);
				}
			} else {
				files.add(file);
			}
		}

		return files;
	}

	//Returns an array list on every file that passes a filter under a given directory
	public ArrayList<File> getAllFilteredFiles(File folder, CustomFilter cf) {

		ArrayList<File> files = new ArrayList<File>();

		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				for(File nestedFile : getAllFiles(file)){
					files.add(nestedFile);
				}
			} else {
				if(cf.accept(file)){
					files.add(file);
				}
			}
		}

		return files;
	}

	public String getTextFromFile(File des) {
		String text = null;
		try {
			text = new String(Files.readAllBytes(Paths.get(des.getAbsolutePath())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return text;
	}
}
