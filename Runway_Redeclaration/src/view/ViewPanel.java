package view;

import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import coreInterfaces.AirfieldInterface;
import coreInterfaces.DeclaredRunwayInterface;

public class ViewPanel extends JSplitPane{
	
	private AirfieldInterface field;
	private DeclaredRunwayInterface runway;
	private ViewTop view1;
	private ViewSide view2;

	public ViewPanel(AirfieldInterface field, DeclaredRunwayInterface runway){
		super(JSplitPane.VERTICAL_SPLIT);
		
		this.field = field;
		this.runway = runway;
		view1 = new ViewTop(field, runway);
		view2 = new ViewSide(field, runway);
		init();
	}
	
	private void init(){
		setLeftComponent(view1);
		setRightComponent(view2);
		
		setResizeWeight(0.5);
		setOneTouchExpandable(true);
		
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setDividerLocation(getSize().height /2);
            }
        });

		view1.setBorder(BorderFactory.createTitledBorder("Top View"));
		view2.setBorder(BorderFactory.createTitledBorder("Side View"));
	}
	
	public void updateView(DeclaredRunwayInterface run){
		this.runway = run;
		view1.setRunway(run);
		view1.repaint();
		view2.setRunway(run);
		view2.repaint();
	}
	
	public ViewTop getTopView(){
		return view1;
	}
}
