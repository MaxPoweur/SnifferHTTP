import java.io.IOException;
import java.net.ServerSocket;

public class InterceptionProxy2
{

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverProxy = null;
        boolean listening = true;
        int port = 5055;
        
        try
        {
        	serverProxy = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.out.println("Port Error");
            System.exit(-1);
        }
        
        while(listening/* ou interruption de l'utilisateur */)
        {
        	new ProxyThread(serverProxy.accept()).start();
        }
        serverProxy.close();
    }
}