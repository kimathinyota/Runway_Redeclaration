package view.createLoadSaveListeners;

import io.FileSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import core.concrete.Obstacle;
import core.interfaces.Savable;
import view.TopFrame;
import view.panels.Tab;

public class SaveObjectListener implements ActionListener{
	
	private TopFrame frame;
	private Savable saveItem;
	
	public SaveObjectListener(TopFrame frame){
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			FileSystem fs = new FileSystem();
			saveItem = (Savable) ((Tab)frame.getTabbePanel().getSelectedComponent()).getField().getPositionedObstacle().getUnpositionedObstacle();
			fs.saveObs((Obstacle)saveItem);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "There is no object to save...", "No Object Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}