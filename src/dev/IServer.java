package dev;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote {

	//pide una coneccion para jugar
	//TODO: deberia retornar un String?
	public String iWantToPlay() throws RemoteException;

}
