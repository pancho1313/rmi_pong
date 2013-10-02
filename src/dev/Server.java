package dev;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class Server {

	static MyUtil U = new MyUtil();
	
	public static void main(String[] args) {
		String ipHost = U.getIpHost(args);
		////////////////////////////////
		
		
		try {
			System.setProperty("java.rmi.server.hostname", ipHost);
			IPong pongServer = new Pong(2, ipHost);
			Naming.rebind("rmi://localhost:1099/PongServer", pongServer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
