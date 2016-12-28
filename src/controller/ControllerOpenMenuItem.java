 package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ControllerOpenMenuItem implements ActionListener
{

    private JMenuItem openMenu;
	/*private CarnetAdresse c;*/


    public ControllerOpenMenuItem(JMenuItem openMenu)
    {
        this.openMenu = openMenu;
    }

    public void actionPerformed(ActionEvent e)
    {
        if ((e.getSource()).equals(openMenu))
        {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.json", "json");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION)
                System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
        }
    }
}
