
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import com.btr.proxy.search.ProxySearch;

public class InfosProxy 
{
	private String hote;
	private int port;
	
	public InfosProxy()
	{
		try
		{
            System.setProperty("java.net.useSystemProxies","true");
            ProxySearch ps = ProxySearch.getDefaultProxySearch();
            ps.setPacCacheSettings(32, 1000*60*5);                             
            List<Proxy> l = ps.getProxySelector().select(new URI("http://www.google.com/")); // Pour pouvoir effectuer une requête d'essai

            for (Iterator<Proxy> iter = l.iterator(); iter.hasNext();)
            {
                Proxy proxy = (Proxy) iter.next();
                InetSocketAddress addr = (InetSocketAddress)proxy.address();

                if(addr == null) 
                {    
                    this.hote = "null";
                }
                else 
                {
                	this.hote = proxy.type().toString().toLowerCase()+"://"+addr.getHostName();
                	this.port = addr.getPort();  
                }
            }
        } 
		catch (Exception e) 
        {
            e.printStackTrace();
        }  
	}
	
	// https://code.google.com/archive/p/proxy-vole/downloads
	public String getHote()
	{
		return this.hote;
	}
	
	public int getPort()
	{
		return this.port;
	}
	public static void main(String args[])
	{
		InfosProxy i = new InfosProxy();
		System.out.println(i.getHote()+":"+i.getPort());
	}
}
