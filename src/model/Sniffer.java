package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.ServerSocket;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class Sniffer extends Thread
{
	private ServerSocket proxyServer;
	private String proxyAdress;
	private int proxyPort;
	private int listeningPort;
	private boolean running;
	
	public Sniffer(int listeningPort) throws IOException // IOException => Soucis de port (deja utilise?)
	{
		this.listeningPort = listeningPort;
		this.proxyAdress = null;
		this.proxyPort = 0;
		this.running = false;
	}
	
	public Sniffer(int listeningPort, String proxyAdress, int proxyPort) throws IOException
	{
		this(listeningPort);
		boolean proxyHere = false;
    	if(proxyAdress.equals("127.0.0.1") || proxyAdress.equals("localhost"))
    		proxyHere = (proxyPort==this.listeningPort) ? false : true;
    	else
    		proxyHere = false;
    	if(proxyHere)
    	{
    		this.proxyAdress = proxyAdress;
    		this.proxyPort = proxyPort;    		
    	}
	}
	
	public void run()
	{
		try 
		{
			ThreadProxy proxy = null;
			this.proxyServer = new ServerSocket(this.listeningPort);
			this.running = true;
			if(this.proxyAdress==null && this.proxyPort==0)
			{
				while(this.running) 
				{
					proxy = new ThreadProxy(proxyServer.accept());
					proxy.start();
					Thread.sleep(500);
					String request = proxy.getRequest(), answer = proxy.getAnswer(), method = proxy.getMethod();
					if(request!=null && answer!=null && method!=null)
						Database.save(method, request, answer);
					proxy.interrupt();
				}
			}
			else
			{
				while(this.running) 
				{
					proxy = new ThreadProxy(proxyServer.accept(), this.proxyAdress, this.proxyPort);
					proxy.start();
					Thread.sleep(500);
					String request = proxy.getRequest(), answer = proxy.getAnswer(), method = proxy.getMethod();
					if(request!=null && answer!=null && method!=null)
						Database.save(method, request, answer);
					proxy.interrupt();
				}
			}
			this.proxyServer.close();
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
		}
		catch(InterruptedException e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	public void stopSniffer()
	{
		this.running = false;
		try
		{
			this.proxyServer.close();
		}
		catch(IOException e){}
		this.interrupt();
	}
	
	public static String getDatabaseContent()
	{
		String datas = "";
		try
		{
			if(Database.getBackup()!=null)
			{
			    Scanner sc = new Scanner(Database.getBackup());
			    while (sc.hasNextLine())
			    	datas += sc.nextLine()+"\r\n";
				sc.close();
				return datas;				
			}
			return null;
	    }
		catch (FileNotFoundException e)
		{
			return null;
	    }
	}
	
	public static String getCapturedTime(String datas)
	{
		if(datas==null)
			return "Premiere capture";
		return Integer.toString(StringUtils.countMatches(datas, "{\r\n"));
	}
	
	public static String getLoadedWebsites(String datas)
	{
		if(datas==null)
			return "0";
		String[] websites = StringUtils.substringsBetween(datas, "\"Host\": \"", "\",");
		HashMap<String, Integer> frequences = new HashMap<String, Integer>();
		
		int oldFrequency;
		for(int i=0; i<websites.length; i++)
		{
			if(frequences.containsKey(websites[i]))
			{
				oldFrequency = frequences.get(websites[i]);
				frequences.remove(websites[i]);
				frequences.put(websites[i], (oldFrequency+1));
			}
			else
				frequences.put(websites[i], 1);
		}
		return "X";
	}
	
	public static String getCookiesCreated(String datas)
	{
		if(datas==null)
			return "0";
		return "X";
	}
	
	public static String getMostViewedWebsite(String datas)
	{
		if(datas==null)
			return "Aucun";
		String[] websitesArray = StringUtils.substringsBetween(datas, "\"Host\": \"", "\",");
		HashMap<String, Integer> frequences = new HashMap<String, Integer>();
		
		int oldFrequency;
		for(int i=0; i<websitesArray.length; i++)
		{
			if(frequences.containsKey(websitesArray[i]))
			{
				oldFrequency = frequences.get(websitesArray[i]);
				frequences.remove(websitesArray[i]);
				frequences.put(websitesArray[i], (oldFrequency+1));
			}
			else
				frequences.put(websitesArray[i], 1);
		}
		
		Set<String> websites = frequences.keySet();
		String mostViewedWebsite=null;
		Iterator<String> it = websites.iterator();
		String websiteTmp;
		while(it.hasNext())
		{
			websiteTmp = it.next();
			if(mostViewedWebsite==null)
				mostViewedWebsite = websiteTmp;
			else
			{
				if(frequences.get(websiteTmp)>frequences.get(mostViewedWebsite))
				{
					mostViewedWebsite = websiteTmp;
				}
			}
		}
		return mostViewedWebsite;
		
	}
	
	public static String getMostEmployedMethod(String datas)
	{
		if(datas==null)
			return "Aucune";
		return "X";
	}
	
	public static String getSizeLoadedData(String datas)
	{
		if(datas==null)
			return "0 Kb";
		return "X";
	}
	
	public void setProxyInfos()
	{
		try
		{
            List<Proxy> l = ProxySelector.getDefault().select(new URI("http://www.google.com/"));// Pour pouvoir effectuer une requête d'essai
            for (Iterator<Proxy> iter = l.iterator(); iter.hasNext();)
            {
                Proxy proxy = iter.next();
                InetSocketAddress addr = (InetSocketAddress)proxy.address();
                if(addr != null) 
                {
                	String host = addr.getHostName();
                	int port = addr.getPort();
                	boolean proxyHere = false;
                	if(host.equals("127.0.0.1") || host.equals("localhost"))
                		proxyHere = (proxyPort==this.listeningPort) ? true : false;
                	else
                		proxyHere = true;
                	
                	if(proxyHere)
                	{
                		this.proxyAdress = host;
                    	this.proxyPort = port;  
                	}
                }
            }
        } 
		catch (Exception e) 
        {
            e.printStackTrace();
        }  
	}
	
	public String getProxyAdress()
	{
		return this.proxyAdress;
	}
	
	public int getProxyPort()
	{
		return this.proxyPort;
	}
	
	public int getListeningPort()
	{
		return this.listeningPort;
	}
	
	public static void main(String[] args) throws Exception
	{
		Sniffer a = new Sniffer(5055);
		//a.setProxyInfos();
		a.run();
    }
}