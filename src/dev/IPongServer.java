package dev;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPongServer extends Remote {

	//pide una coneccion para jugar
	//TODO: deberia retornar un String?
	public boolean iWantToPlay(IPlayer p) throws RemoteException;
	public void iWantToLeave(int playerId) throws RemoteException;
	public void iMovedMyBar(int playerId, double x, double y) throws RemoteException;
}
