package dev;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class Player extends UnicastRemoteObject implements IPlayer{

	/*-------variables de estado-----*/
	public boolean runUserWindow;
	private int id = -1;//TODO: verificar correctitud (el -1 podría servir para debug)
	/*-------------------------------*/
	
	
	
	static MyUtil U = new MyUtil();
	
	
	private void closeUserWindow(){
		runUserWindow = false;
	}
	
	public Player() throws RemoteException{
		super();
		
		//variables de estado
		runUserWindow = true;
	}
	
	public void messageFromServer(String message) throws RemoteException{
		U.localMessage("server: " + message);
	}
	
	public void setPlayerId(int _id) throws RemoteException{
		id = _id;
	}
	
	public int getPlayerId(){
		return id;
	}
	
	public void closePlayer() throws RemoteException{
		closeUserWindow();
	}
	
}
