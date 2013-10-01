package dev;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayer extends Remote {

	public void showServerMesage(String message) throws RemoteException;

}
