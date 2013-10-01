package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IDate extends Remote {

	public String currentDate() throws RemoteException;

}
