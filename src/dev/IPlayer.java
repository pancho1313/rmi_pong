package dev;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayer extends Remote {

	public void messageFromServer(String message) throws RemoteException;
	public void setPlayerId(int id) throws RemoteException;
	public void closePlayer() throws RemoteException;

}
