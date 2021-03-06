package view.createLoadSaveListeners;

import io.FileSystem;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import view.TopFrame;
import view.createEditForms.FormObstacle;
import view.customComponents.Notification;
import view.customComponents.NotificationPanel;
import core.interfaces.AirportInterface;
import core.interfaces.ObstacleInterface;

public class LoadListener{

	private TopFrame frame;
	private javax.swing.filechooser.FileFilter filter;
	private FileSystem fs;
	private String typeDir;
	private File chosen = null;
	
	public LoadListener(TopFrame frame, javax.swing.filechooser.FileFilter filter, String typeDir){
		this.frame = frame;
		this.filter = filter;
		this.typeDir = typeDir;
		load();
	}

	public void load(){

		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/dat/" + typeDir));

		int result = fc.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			chosen = fc.getSelectedFile();
			fs = new FileSystem();
			if(fs.checkObs(chosen)){
				//If true, user is not allowed to load an obstacle
				if(frame.getAirport() == null){
					JOptionPane.showMessageDialog(null, "You cannot load an obstacle before loading an airport.", "Error",JOptionPane.ERROR_MESSAGE);
				}
				else{
					new FormLoadObstacle(frame);
				}
			}
			else{
				if(fs.checkAir(chosen)){
					AirportInterface airport = fs.loadAir(chosen);
					frame.loadOrCreateAirport(airport);
					NotificationPanel.notifyIt(airport.getName()+" Added", airport.getName() +" loaded.", Notification.FILE);
					frame.getLogPanel().repaint();
				}
			}
		}
	}
	
	private class FormLoadObstacle extends FormObstacle{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private ObstacleInterface currentObstacle;

		private FormLoadObstacle(TopFrame tf){
			super(tf, "Load obstacle");
			currentObstacle = fs.loadObs(chosen);
			setInitialTextfields();
			setPreferredSize(new Dimension(300,350));
			button.setText("Load");
			init();
		}
		
		private void setInitialTextfields(){
			for(JTextField jtf : getTextFields()){
				if(getTextFields().indexOf(jtf) == 0){
					jtf.setText(currentObstacle.getName());
				}
				if(getTextFields().indexOf(jtf) == 1){
					jtf.setText(String.valueOf(currentObstacle.getRadius()));
				}
				if(getTextFields().indexOf(jtf) == 2){
					jtf.setText(String.valueOf(currentObstacle.getHeight()));
				}
				if(getTextFields().indexOf(jtf) == 3){
					SwingUtilities.invokeLater(new Runnable() {
					      public void run() {
					        jtf.requestFocus();
					      }
					});
				}
			}
		}
	}
}

