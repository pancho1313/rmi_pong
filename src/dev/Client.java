package dev;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;





public class Client {

	static MyUtil U = new MyUtil();
	
	public static void main(String[] args) {
		String ipHost = U.getIpHost(args);
		////////////////////////////////
		
		
		IPong server;
		try {
			U.cMessage("Connecting to PongServer...");
			server = (IPong) Naming.lookup("//"+ipHost+":1099/PongServer");
			
			IPlayer myPlayer = new Player();
			myPlayer.showServerMesage(server.iWantToPlay(myPlayer));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
