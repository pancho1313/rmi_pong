package dev;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;



public class Player implements IPlayer{

	static MyUtil U = new MyUtil();
	
	public void showServerMesage(String message) throws RemoteException{
		U.cMessage("[server]: " + message);
	}
	
	public static void main(String[] args) {
		String ipHost = U.getIpHost(args);
		////////////////////////////////
		
		
		IServer server;
		try {
			U.cMessage("Connecting to PongServer...");
			server = (IServer) Naming.lookup("//"+ipHost+":1099/PongServer");
			server.iWantToPlay();
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
