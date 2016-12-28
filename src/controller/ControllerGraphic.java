package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jfree.ui.RefineryUtilities;

import view.Graph_bcap;

public class ControllerGraphic implements ActionListener
{
    private JButton bcap, bpagechar, bpoid, bcookie, bsite, bmethode;

    public ControllerGraphic(JButton bcap, JButton bpagechar, JButton bpoid, JButton bcookie, JButton bsite, JButton bmethode)
    {
        this.bcap=bcap;
        this.bpagechar=bpagechar;
        this.bpoid=bpoid;
        this.bcookie=bcookie;
        this.bsite=bsite;
        this.bmethode=bmethode;
    }

    public void actionPerformed(ActionEvent e)
    {
        if ((e.getSource()).equals(bcap))
        {
            Graph_bcap demo = new Graph_bcap( "Mobile Sales" );
            demo.setSize( 560 , 367 );
            RefineryUtilities.centerFrameOnScreen( demo );
            demo.setVisible( true );
            demo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if ((e.getSource()).equals(bpagechar))
        {

        }

        if ((e.getSource()).equals(bpoid))
        {

        }

        if ((e.getSource()).equals(bcookie))
        {

        }

        if ((e.getSource()).equals(bsite))
        {

        }

        if ((e.getSource()).equals(bmethode))
        {

        }
    }
}
