package dev;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPong extends Remote {

	//pide una coneccion para jugar
	//TODO: deberia retornar un String?
	public boolean iWantToPlay(IPlayer p) throws RemoteException;
}
