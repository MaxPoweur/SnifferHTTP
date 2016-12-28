package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import view.View;

public class ControllerConfiguration implements ActionListener
{

	private JRadioButton buttonConfigAutomatic;
	private JRadioButton buttonConfigManual;
	private View view;

	public ControllerConfiguration(JRadioButton buttonConfigAutomatic, JRadioButton buttonConfigManual, View view)
	{
		this.view = view;
	    this.buttonConfigAutomatic = buttonConfigAutomatic;
	    this.buttonConfigManual = buttonConfigManual;
	}

	public void actionPerformed(ActionEvent e)
	{
		if ((e.getSource()).equals(buttonConfigAutomatic))
			this.view.setAutomaticConfiguration();
	    else if((e.getSource()).equals(buttonConfigManual))
	    	this.view.setManualConfiguration();
	}
}
