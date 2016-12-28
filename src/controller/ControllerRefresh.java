package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

//import ihm.modele.*;
import view.View;

public class ControllerRefresh implements ActionListener
{

	private JButton buttonRefresh;
	private View view;

	public ControllerRefresh(JButton buttonRefresh, View view)
	{
		this.view = view;
		this.buttonRefresh = buttonRefresh;
	}

	public void actionPerformed(ActionEvent e)
	{
		if ((e.getSource()).equals(buttonRefresh))
			this.view.updateStats();
	}
}
