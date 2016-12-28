package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ControllerHelp implements ActionListener
{

	private JMenuItem menuHelp;
	/*private CarnetAdresse c;*/


	public ControllerHelp(JMenuItem menuHelp)
	{
		this.menuHelp = menuHelp;
	}

	public void actionPerformed(ActionEvent e)
	{
		if ((e.getSource()).equals(menuHelp))
		{

			// String command = "xdg-open help.txt";
			// Runtime runtime = Runtime.getRuntime();
			// Process process = null;
			//
			// try
			// {
			// 	process = runtime.exec(command);
			// } catch(Exception err) {
			// 	System.out.println(err.getMessage());
			// }
			/*
			try{
				File f=new File("help.txt");
				String chaine=new String("");
				InputStream ips=new FileInputStream(f);
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String ligne;
				while ((ligne=br.readLine())!=null){
					//System.out.println(ligne);
					chaine+=ligne+"\n";
				}
				br.close();
				JOptionPane.showMessageDialog(null,chaine ,"help", JOptionPane.PLAIN_MESSAGE);
			}
			catch (Exception exec){
				System.out.println(exec.toString());
			}*/
			JOptionPane.showMessageDialog(null, "Message d'aide temporaire" ,"help", JOptionPane.PLAIN_MESSAGE);
		}
	}
}
