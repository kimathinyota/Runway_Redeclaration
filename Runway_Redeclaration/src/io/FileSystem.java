package io;
import java.io.File;
import java.util.ArrayList;

import Core.Airport;
import Core.Obstacle;
import CoreInterfaces.AirportInterface;
import CoreInterfaces.ObstacleInterface;

/**
 * Handles the saving and loading of objects
 * @author Jonathan
 *
 */
public class FileSystem {
	
	private String wd = "./";
	private String datDir = "/dat";
	private String airDir = "/airports";
	private String objDir = "/obs";
	
	private String xmlext = ".xml";
	private String objext = ".obj";
	private String airext = ".air";
	
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

	public boolean saveObs(Obstacle o){
		String dir = wd + datDir + objDir + "/" + o.getName() + objext + xmlext;
		return XMLSaver.serialise(o, dir);
	}
	
	public ObstacleInterface loadObs(String fileName){
		File obsFile = new File(fileName);
		ObstacleInterface loadedObs = (ObstacleInterface) XMLSaver.deserialise(Obstacle.class, obsFile);
		return loadedObs;
	}
	
	public boolean saveAir(Airport a){
		String dir = wd + datDir + airDir + "/" + a.getName()+airext + xmlext;
		return XMLSaver.serialise(a, dir);
	}
	
	public AirportInterface loadAir(String fileName){
		File airFile = new File(wd + datDir + airDir + "/" + fileName);
		AirportInterface a = (AirportInterface) XMLSaver.deserialise(Airport.class, airFile);
		return a;
	}
	
	public boolean checkAir(File chosen){
		MasterFileFilter f = new MasterFileFilter();
		return f.checkAir(chosen);
	}
	
	public boolean checkObs(File chosen){
		MasterFileFilter f = new MasterFileFilter();
		return f.checkObs(chosen);
	}
}
