package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ActionMapper implements ActionListener {
	private String IMPORT_BASE = "importBase";
	private String GENERATE_MODELS = "generateModels";
	private String IMPORT_MODELS = "importModels";
	private String FIND_PROMOTERS = "findPromoters";

	@Override
	public void actionPerformed(ActionEvent e) {
		if (IMPORT_BASE.equals(e.getActionCommand())) {
			JOptionPane.showMessageDialog(null, "You Pressed: " + IMPORT_BASE);
		} else if (GENERATE_MODELS.equals(e.getActionCommand())) {
			JOptionPane.showMessageDialog(null, "You Pressed: " + GENERATE_MODELS);
		} else if (IMPORT_MODELS.equals(e.getActionCommand())) {
			JOptionPane.showMessageDialog(null, "You Pressed: " + IMPORT_MODELS);
		} else if (FIND_PROMOTERS.equals(e.getActionCommand())) {
			JOptionPane.showMessageDialog(null, "You Pressed: " + FIND_PROMOTERS);
		}

	}

}
