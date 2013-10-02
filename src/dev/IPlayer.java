package dev;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayer extends Remote {

	public void sendMessage(String message) throws RemoteException;

}
