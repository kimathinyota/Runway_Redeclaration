package view.customComponents;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class NotificationModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<NotificationCellComponent> feeds;

	protected NotificationModel(List<NotificationCellComponent> feeds) {
		this.feeds = feeds;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int columnIndex) {
		return NotificationCellComponent.class; 
	}

	public int getColumnCount() {
		return 1; 
	}

	public String getColumnName(int columnIndex) {
		return "Notifications"; 
	}

	public int getRowCount() {
		return (feeds == null) ? 0 : feeds.size(); 
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return (feeds == null) ? null : feeds.get(rowIndex); 
	}

	public boolean isCellEditable(int columnIndex, int rowIndex) {
		return true; 
	}
}