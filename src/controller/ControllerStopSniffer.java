package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import view.View;

public class ControllerStopSniffer implements ActionListener
{

	//private JButton stop;
	private JButton buttonStopSniffing;
	private View view;

	public ControllerStopSniffer(JButton buttonStopSniffing, View view)
	{
		this.view = view;
    	this.buttonStopSniffing = buttonStopSniffing;
	}

	public void actionPerformed(ActionEvent e)
	{
		if ((e.getSource()).equals(buttonStopSniffing))
		{
			this.view.stopSniffer();
			/*try {
				anal.finEcoute();
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
		}
	}
}
