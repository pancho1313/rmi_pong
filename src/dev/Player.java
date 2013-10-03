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
	
	private void closeUserWindow(){
		runUserWindow = false;
	}
	
	public boolean runUserWindow;
	
	public Player() throws RemoteException{
		super();
		
		//variables de estado
		runUserWindow = true;
	}
	
	public void messageFromServer(String message) throws RemoteException{
		U.localMessage("server: " + message);
	}
	
	public void startYourGame() throws RemoteException{
		
	}
}
