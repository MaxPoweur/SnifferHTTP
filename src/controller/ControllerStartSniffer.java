package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import view.View;


public class ControllerStartSniffer implements ActionListener
{

	//private JButton start;
	private JButton buttonStartUpdate;
	private View view;
	/*private CarnetAdresse c;*/


	public ControllerStartSniffer(JButton buttonStartUpdate, View view)
	{
		this.view = view;
    	this.buttonStartUpdate = buttonStartUpdate;
	}

	public void actionPerformed(ActionEvent e)
	{
		if ((e.getSource()).equals(buttonStartUpdate))
		{
			JButton button = (JButton)e.getSource();
			switch(button.getText())
			{
				case "Commencer":
					if(this.view.getListeningPort()<1024)
						JOptionPane.showMessageDialog(null,"Les 1023 premiers ports sont reserves, veuillez en choisir un autre.","Port incorrect",JOptionPane.ERROR_MESSAGE);
					else if(this.view.getListeningPort()>65535)
						JOptionPane.showMessageDialog(null,"Les ports au dessus de 65535 sont inexistants, veuillez en choisir un autre.","Port incorrect",JOptionPane.ERROR_MESSAGE);
					else
						this.view.startSniffer();
					break;
					
				case "Mettre a jour la configuration":
					this.view.updateSniffer();
					break;
					
				case "Arreter":
					this.view.stopSniffer();
					break;	
					
			}
		}
	}
}
