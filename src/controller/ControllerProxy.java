package controller;

import java.awt.event.*;
import javax.swing.*;
import view.*;

public class ControllerProxy implements ActionListener
{
	
	private JRadioButton buttonEnabledProxy;
	private JRadioButton buttonDisabledProxy;
	private View view;

	public ControllerProxy(JRadioButton buttonEnabledProxy, JRadioButton buttonDisabledProxy, View view)
	{
		this.view = view;
	    this.buttonEnabledProxy = buttonEnabledProxy;
	    this.buttonDisabledProxy = buttonDisabledProxy;
	}

	public void actionPerformed(ActionEvent e)
	{
		if ((e.getSource()).equals(buttonEnabledProxy))
			this.view.enableProxy();
	    else if((e.getSource()).equals(buttonDisabledProxy))
	    	this.view.disableProxy();
	}
}
