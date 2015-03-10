package view;

import io.FileSystem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import CoreInterfaces.AirportInterface;
import CoreInterfaces.ObstacleInterface;

public class LoadListener{

	private TopFrame frame;
	private javax.swing.filechooser.FileFilter filter;
	FileSystem fs;
	private String typeDir;
	File chosen = null;
	
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
					
					new ObjectFrame();
				}
			}
			else{
				if(fs.checkAir(chosen)){
					AirportInterface airport = fs.loadAir(chosen);
					Notification.notify(airport.getName() +" loaded.", "file");
					frame.loadOrCreateAirport(airport);
					
					frame.getLogPanel().repaint();
				}
			}
		}
	}
	class ObjectFrame extends JFrame{
		JLabel directionLeft, directionRight;
		JTextField distanceFromLeft, distanceFromRight;
		JButton ok;
		
		ObjectFrame(){
			directionLeft = new JLabel("Please Enter the Distance from the Left Side");
			directionRight = new JLabel("Please Enter the Distances from the Right Side");
			distanceFromLeft = new JTextField();
			distanceFromRight = new JTextField();
			ok = new JButton("OK");
			
			JPanel top = new JPanel();
			JPanel pane = new JPanel();
			JPanel buttonPane = new JPanel();
			
			this.setContentPane(top);
			top.setLayout(new BorderLayout ());
			
			top.add(pane, BorderLayout.CENTER);
			top.add(buttonPane, BorderLayout.SOUTH);
			
			pane.setLayout(new GridLayout(2,2));
			pane.add(directionLeft); pane.add(directionRight);
			pane.add(distanceFromLeft); pane.add(distanceFromRight);
			
			buttonPane.add(ok);
			
			ok.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					ObstacleInterface obs = fs.loadObs(chosen);
					Notification.notify(obs.getName() + " loaded.", "file");
					try{
						frame.loadOrCreateObstacle(obs, ((Tab)frame.getTabbePanel().getSelectedComponent()).getField(), Double.parseDouble(distanceFromLeft.getText()), Double.parseDouble(distanceFromRight.getText()));
					}catch(NumberFormatException nf){
						JOptionPane.showMessageDialog(frame, "Enter a valid number for distances please!", "ERROR: Distance", JOptionPane.ERROR_MESSAGE);
					}
					ObjectFrame.this.dispose();
				}
			});
			
			setPreferredSize(new Dimension(300,110));
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setResizable(false);
			this.pack();
			this.setVisible(true);
		}
}
}

