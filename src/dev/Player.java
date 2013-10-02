package dev;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class Player extends UnicastRemoteObject implements IPlayer{

	/**
	 * 
	 */
	//private static final long serialVersionUID = -2587092213054963543L;
	static MyUtil U = new MyUtil();
	
	public Player() throws RemoteException{
		super();
	}
	
	public void showServerMesage(String message) throws RemoteException{
		U.cMessage("server: " + message);
	}
	
	
}
