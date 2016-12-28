package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import controller.ControllerConfiguration;
import controller.ControllerHelp;
import controller.ControllerOpenMenuItem;
import controller.ControllerProxy;
import controller.ControllerRefresh;
import controller.ControllerStartSniffer;
import controller.ControllerStopSniffer;
import model.Sniffer;


public class View
{
  private JButton buttonStop, buttonStartUpdate;
  private JLabel labelListeningProxy, labelConfiguration, labelProxyOrNot, labelProxyAddress, labelProxyPort;
  private JRadioButton buttonEnabledProxy, buttonDisabledProxy, buttonConfigAutomatic, buttonConfigManual;
  private JTextField fieldProxyAddress; // String field
  private JSpinner fieldPortProxy, listeningPortField; // Integer field
  private ButtonGroup buttonGroupProxy, configurationGroupProxy;
  private JLabel labelCapturedTime, labelLoadedWebsites, labelCookiesCreated, labelMostViewedWebsite, labelMostEmployedMethod, labelSizeLoadedData;

  private Sniffer sniffer;
  
  public View() throws IOException
  {
    JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);

    JPanel configurationPart1 = new JPanel();
    configurationPart1.setLayout(new GridLayout(2, 3));

    this.labelListeningProxy = new JLabel("Port d'ecoute : ");
    configurationPart1.add(this.labelListeningProxy);

    this.listeningPortField = new JSpinner();
    configurationPart1.add(this.listeningPortField);

    JLabel hiddenCell = new JLabel("");
    configurationPart1.add(hiddenCell);

    this.labelConfiguration =new JLabel("Configuration: ");
    configurationPart1.add(this.labelConfiguration);

    this.configurationGroupProxy = new ButtonGroup();
    this.buttonConfigAutomatic = new JRadioButton("automatique");
    this.buttonConfigAutomatic.setSelected(true);
    this.buttonConfigManual = new JRadioButton("manuelle");
    
    this.configurationGroupProxy.add(this.buttonConfigAutomatic);
    this.configurationGroupProxy.add(this.buttonConfigManual);
    configurationPart1.add(this.buttonConfigAutomatic);
    configurationPart1.add(this.buttonConfigManual);
    ControllerConfiguration controllerConfiguration = new ControllerConfiguration(this.buttonConfigAutomatic, this.buttonConfigManual, this);
    this.buttonConfigAutomatic.addActionListener(controllerConfiguration);
    this.buttonConfigManual.addActionListener(controllerConfiguration);

    JPanel configurationPart2 = new JPanel();
    configurationPart2.setLayout(new GridLayout(3, 3));

    this.labelProxyOrNot =new JLabel("utiliser un proxy ?");
    this.labelProxyOrNot.setVisible(false);
    configurationPart2.add(this.labelProxyOrNot);

    this.buttonGroupProxy = new ButtonGroup();
    this.buttonEnabledProxy = new JRadioButton("oui");
    this.buttonDisabledProxy = new JRadioButton("non");
    this.buttonDisabledProxy.setSelected(true);
    this.buttonDisabledProxy.setVisible(false);
    this.buttonEnabledProxy.setVisible(false);

    this.buttonGroupProxy.add(this.buttonEnabledProxy);
    this.buttonGroupProxy.add(this.buttonDisabledProxy);
    configurationPart2.add(this.buttonEnabledProxy); 	
    configurationPart2.add(this.buttonDisabledProxy);

    ControllerProxy control_prox = new ControllerProxy(this.buttonEnabledProxy, this.buttonDisabledProxy, this);
    this.buttonEnabledProxy.addActionListener(control_prox);
    this.buttonDisabledProxy.addActionListener(control_prox);

    this.labelProxyAddress = new JLabel("addresse proxy : ");
    this.labelProxyAddress.setVisible(false);
    configurationPart2.add(labelProxyAddress);

    this.fieldProxyAddress = new JTextField("");
    this.fieldProxyAddress.setVisible(false);
    configurationPart2.add(this.fieldProxyAddress);

    JLabel bricolage = new JLabel("");
    configurationPart2.add(bricolage);

    this.labelProxyPort = new JLabel("port : ");
    this.labelProxyPort.setVisible(false);
    configurationPart2.add(this.labelProxyPort);

    this.fieldPortProxy = new JSpinner();
    this.fieldPortProxy.setVisible(false);
    configurationPart2.add(this.fieldPortProxy);


    JPanel configurationPart3 = new JPanel();
    FlowLayout layout1_3=new FlowLayout();
    layout1_3.setHgap(50);
    configurationPart3.setLayout(layout1_3);

    this.buttonStop = new JButton("Arreter");
    this.buttonStop.setSize(this.buttonStop.getPreferredSize());
    this.buttonStop.setVisible(false);
    configurationPart3.add(this.buttonStop);
    ControllerStopSniffer control_stop = new ControllerStopSniffer(this.buttonStop, this);
    this.buttonStop.addActionListener(control_stop);

    this.buttonStartUpdate = new JButton("Commencer");
    this.buttonStartUpdate.setSize(this.buttonStartUpdate.getPreferredSize());
    configurationPart3.add(this.buttonStartUpdate);
    
    
    ControllerStartSniffer control_start = new ControllerStartSniffer(this.buttonStartUpdate, this);
    this.buttonStartUpdate.addActionListener(control_start);
    
    JPanel configurationTab = new JPanel();

    configurationTab.setLayout(new GridLayout(3, 1));

    configurationTab.add(configurationPart1);
    configurationTab.add(configurationPart2);
    configurationTab.add(configurationPart3);

    onglets.addTab("Proxy", configurationTab);

    JPanel displayTabStats = new JPanel(new GridLayout(6, 2, -1, -1));
    displayTabStats.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

    this.initStats();

    JLabel capturedTime = new JLabel("Nombre de captures");
    capturedTime.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    capturedTime.setHorizontalAlignment(SwingConstants.CENTER);
    
    JLabel loadedWebsites = new JLabel("Nombres de pages chargees");
    loadedWebsites.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    loadedWebsites .setHorizontalAlignment(SwingConstants.CENTER);
    
    JLabel cookiesCreated = new JLabel("Cookies crees");
    cookiesCreated.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    cookiesCreated.setHorizontalAlignment(SwingConstants.CENTER);
    
    JLabel mostViewedWebsite = new JLabel("Site le plus visite");
    mostViewedWebsite.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    mostViewedWebsite.setHorizontalAlignment(SwingConstants.CENTER);
    
    JLabel mostEmployedMethod = new JLabel("Methode la plus utilisee");
    mostEmployedMethod .setBorder(BorderFactory.createLineBorder(Color.BLACK));
    mostEmployedMethod .setHorizontalAlignment(SwingConstants.CENTER);
    
    JLabel sizeLoadedData = new JLabel("Poids des donnees chargees (en Ko)");
    sizeLoadedData.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    sizeLoadedData .setHorizontalAlignment(SwingConstants.CENTER);
    
    displayTabStats.add(capturedTime);
    displayTabStats.add(this.labelCapturedTime);
    displayTabStats.add(loadedWebsites);
    displayTabStats.add(this.labelLoadedWebsites);
    displayTabStats.add(cookiesCreated);
    displayTabStats.add(this.labelCookiesCreated);
    displayTabStats.add(mostViewedWebsite);
    displayTabStats.add(this.labelMostViewedWebsite);
    displayTabStats.add(mostEmployedMethod);
    displayTabStats.add(this.labelMostEmployedMethod);
    displayTabStats.add(sizeLoadedData);
    displayTabStats.add(this.labelSizeLoadedData);
    
    JPanel pannelRefresh = new JPanel();
    JButton buttonRefresh = new JButton("Actualiser");
    pannelRefresh.add(buttonRefresh);
    ControllerRefresh controllerRefresh = new ControllerRefresh(buttonRefresh, this);
    buttonRefresh.addActionListener(controllerRefresh);

    JPanel displayTab=new JPanel();
    displayTab.setLayout(new BorderLayout()); //North, South, East, West
    displayTab.add("Center", displayTabStats);
    displayTab.add("East", pannelRefresh);
    displayTab.add(displayTabStats);
    onglets.addTab("Statistiques", displayTab);

    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem, openMenu;

    //Create the menu bar.
    menuBar = new JMenuBar();

    //Build the first menu.
    menu = new JMenu("Menu");
    menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
    menuBar.add(menu);


    //a group of JMenuItems
    submenu = new JMenu("Fichier");
    submenu.setMnemonic(KeyEvent.VK_S);


    openMenu = new JMenuItem("Ouvrir");
    submenu.add(openMenu);
    ControllerOpenMenuItem controllerOpenMenuItem = new ControllerOpenMenuItem(openMenu);
    openMenu.addActionListener(controllerOpenMenuItem);

    menuItem = new JMenuItem("Enregistrer");
    submenu.add(menuItem);
    menu.add(submenu);

    JMenuItem menuHelp = new JMenuItem("help",KeyEvent.VK_T);


    ControllerHelp controllerHelp = new ControllerHelp(menuHelp);
    menuHelp.addActionListener(controllerHelp);

    menuHelp.getAccessibleContext().setAccessibleDescription("A short help to start sniffing http traffic.");
    menu.add(menuHelp);


    onglets.setOpaque(true);
    
    JPanel mainPanel = new JPanel();
    mainPanel.add(onglets);

    JFrame frame = new JFrame("Sniffer HTTP");

    frame.setJMenuBar(menuBar);
    frame.getContentPane().add(mainPanel);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
  }
  

  public void startSniffer()
  {
	  try
	  {
		  if(this.isConfigurationAutomatic())
		  {
			  this.sniffer = new Sniffer(this.getListeningPort());
			  this.sniffer.setProxyInfos();
		  }
		  else
		  {
			  if(this.isThereProxy())
				  this.sniffer = new Sniffer(this.getListeningPort(), this.fieldProxyAddress.getText(), (int)this.fieldPortProxy.getValue());
			  else
				  this.sniffer = new Sniffer(this.getListeningPort());
		  }
		  this.buttonStartUpdate.setText("Mettre a jour la configuration");
		  this.buttonStop.setVisible(true);
		  this.sniffer.start();
	  }
	  catch(IOException e)
	  {
		  // System.err.println(e.getMessage());
	  }
  }

  public void stopSniffer()
  {
	  this.sniffer.stopSniffer();
	  this.sniffer = null;
	  this.listeningPortField.setValue(0);
	  this.buttonConfigAutomatic.setSelected(true);
	  this.buttonConfigManual.setSelected(false);
	  
	  this.labelProxyOrNot.setVisible(false);
	  
	  this.buttonDisabledProxy.setSelected(true);
	  this.buttonEnabledProxy.setSelected(false);
	  
	  this.buttonDisabledProxy.setVisible(false);
	  this.buttonEnabledProxy.setVisible(false);
	  
	  this.labelProxyAddress.setVisible(false);
	  this.fieldProxyAddress.setVisible(false);
	  
	  this.labelProxyPort.setVisible(false);
	  this.fieldPortProxy.setVisible(false);
	  
	  
	  this.fieldProxyAddress.setText("");
	  this.fieldPortProxy.setValue(0);
	  this.buttonStartUpdate.setText("Commencer");
	  this.buttonStop.setVisible(false);
  }
  
  public void updateSniffer()
  {
	  this.sniffer.stopSniffer();
	  this.sniffer = null;
	  this.startSniffer();
  }
  
  public void setManualConfiguration()
  {
	  this.labelProxyOrNot.setVisible(true);
	  this.buttonEnabledProxy.setVisible(true);
	  this.buttonDisabledProxy.setVisible(true);
  }

  public void setAutomaticConfiguration()
  {
	  this.labelProxyOrNot.setVisible(false);
	  this.buttonEnabledProxy.setVisible(false);
	  this.buttonDisabledProxy.setVisible(false);
	  this.buttonDisabledProxy.setSelected(true);
  }

  public void enableProxy()
  {
	  this.labelProxyAddress.setVisible(true);
	  this.fieldProxyAddress.setVisible(true);
	  this.labelProxyPort.setVisible(true);
	  this.fieldPortProxy.setVisible(true);
  }

  public void disableProxy()
  {
	  this.labelProxyAddress.setVisible(false);
	  this.fieldProxyAddress.setVisible(false);
	  this.labelProxyPort.setVisible(false);
	  this.fieldPortProxy.setVisible(false);
  }

 
  public void initStats()
  {
	  	this.labelCapturedTime = new JLabel("<valeur>");
	    this.labelCapturedTime.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    this.labelCapturedTime.setHorizontalAlignment(SwingConstants.CENTER);
	    
	    this.labelLoadedWebsites = new JLabel("<valeur>");
	    this.labelLoadedWebsites.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    this.labelLoadedWebsites.setHorizontalAlignment(SwingConstants.CENTER);
	    
	    this.labelCookiesCreated = new JLabel("<valeur>");
	    this.labelCookiesCreated.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    this.labelCookiesCreated.setHorizontalAlignment(SwingConstants.CENTER);
	    
	    this.labelMostViewedWebsite = new JLabel("<valeur>");
	    this.labelMostViewedWebsite.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    this.labelMostViewedWebsite.setHorizontalAlignment(SwingConstants.CENTER);
	    
	    this.labelMostEmployedMethod = new JLabel("<valeur>");
	    this.labelMostEmployedMethod.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    this.labelMostEmployedMethod.setHorizontalAlignment(SwingConstants.CENTER);
	    
	    this.labelSizeLoadedData = new JLabel("<valeur>");
	    this.labelSizeLoadedData.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    this.labelSizeLoadedData.setHorizontalAlignment(SwingConstants.CENTER);
  }
  
  public void updateStats()
  {
	  String datas = Sniffer.getDatabaseContent();
	  String capturedTime = Sniffer.getCapturedTime(datas);
	  String loadedWebsites = Sniffer.getLoadedWebsites(datas);
	  String cookiesCreated = Sniffer.getCookiesCreated(datas);
	  String mostViewedWebsite = Sniffer.getMostViewedWebsite(datas);
	  String mostEmployedMethod = Sniffer.getMostEmployedMethod(datas);
	  String sizeLoadedData = Sniffer.getSizeLoadedData(datas);
	  this.labelCapturedTime.setText(capturedTime);
	  this.labelCapturedTime.repaint();
	  this.labelLoadedWebsites.setText(loadedWebsites);
	  this.labelCookiesCreated.setText(cookiesCreated);
	  this.labelMostViewedWebsite.setText(mostViewedWebsite);
	  this.labelMostEmployedMethod.setText(mostEmployedMethod);
	  this.labelSizeLoadedData.setText(sizeLoadedData);
  }
  
  public int getListeningPort()
  {
	  return (int)this.listeningPortField.getValue();
  }

  public int getPortProxy()
  {
	  return (int)this.fieldPortProxy.getValue();
  }

  public boolean isConfigurationAutomatic() throws IOException
  {
	  if(buttonConfigAutomatic.isSelected())
		  return true;
	  else if(buttonConfigManual.isSelected())
		  return false;
	  else
		  throw new IOException("No configuration profile has yet been selected."); // Reconsider to customize this exception
  }

  public boolean isThereProxy() throws IOException
  {
	  if(buttonEnabledProxy.isSelected())
		  return true;
	  else if(buttonDisabledProxy.isSelected())
		  return false;
	  throw new IOException("User has not yet chose the proxy configuration."); // Reconsider to customize this exception
  }

  public static void setBestLookAndFeelAvailable()
  {
	  String system_lf = UIManager.getSystemLookAndFeelClassName().toLowerCase();
	  if(system_lf.contains("metal"))
	  {
		  try 
		  {
			  UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		  }
		  catch (Exception e) {}
	  }
	  else
	  {
		  try
		  {
			  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		  }
		  catch (Exception e) {}
	  }
  }

  public static void main(String[] args) throws Exception
  {
	  View.setBestLookAndFeelAvailable();
	  new View();
  }
}
