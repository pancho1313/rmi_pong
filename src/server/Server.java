package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class Server {

	public static void main(String[] args) {
		//establecer la IP del HOST
		String ipHostDefault = "192.168.2.14";
		String ipHost;
		if(args.length == 1){
			ipHost = args[0];
		}else{
			System.out.println("Warning: no se ha especificado la IP del HOST! (default = "+ipHostDefault+")");
			ipHost = ipHostDefault;
		}
		
		
		try {
			System.setProperty("java.rmi.server.hostname", ipHost);
			IDate date = new MyDate();
			Naming.rebind("rmi://localhost:1099/dateServer", date);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
