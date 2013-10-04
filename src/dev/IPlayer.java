package dev;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayer extends Remote {

	public void messageFromServer(String message) throws RemoteException;
	public void setPlayerId(int id) throws RemoteException;
	public void closePlayer() throws RemoteException;
	public void preNewGame() throws RemoteException;
	public void startNewGame() throws RemoteException;
	public void refreshEnemyPos(int enemyId, double x, double y) throws RemoteException;

}
