package dev;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PongServer extends UnicastRemoteObject implements IPongServer{
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 6311160989789331741L;
	private int nPlayers = 0;
	private String ipHost;
	private ArrayList<IPlayer> players;
	
	private void assignPlayerId(IPlayer newPlayer){
		/*
		 * Usado para identificar el emisor (player) de un mensaje al servidor.
		 * */
		try {
			newPlayer.setPlayerId(players.indexOf(newPlayer));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean addPlayer(IPlayer p) throws RemoteException{
		
		if(players.size() < nPlayers){
			IPlayer newPlayer = null;
			String playerPublicName = "rmi://"+ipHost+":1099/player"+players.size();
			try {
				Naming.rebind(playerPublicName, p);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				newPlayer = (IPlayer) Naming.lookup(playerPublicName);
				newPlayer.messageFromServer("Welcome to Pong.");
				newPlayer.messageFromServer("Waiting for more players...");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(newPlayer != null){//el player fue correctamente inicializado
				players.add(newPlayer);
				
				assignPlayerId(newPlayer);
				
				if(players.size() == nPlayers){
					readyToPlay();
				}else{
					int numPlayers = (nPlayers - players.size());
					U.localMessage("Waiting " + numPlayers + ((numPlayers > 1)?" players.":" player."));
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * gestionar el comienzo de una partida, la bandeja de players esta comlpleta.
	 * */
	private void readyToPlay() throws RemoteException{
		U.localMessage("Let's play!");
		for(IPlayer p : players){
			p.startNewGame();
			p.messageFromServer("Let's play!");
		}
	}
	

	
	static MyUtil U = new MyUtil();
	
	public PongServer(int _nPlayers, String ipHost) throws RemoteException{
		super();
		nPlayers = _nPlayers;//TODO: validar el rango de valores
		this.ipHost = ipHost;
		players = new ArrayList<IPlayer>();
		U.localMessage("PongServer Started.");
		U.localMessage("Waiting " + nPlayers + " players.");
	}
	
	public void sendPlayer(String pPublicName, IPlayer p) throws RemoteException{
		try {
			Naming.rebind(pPublicName, p);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			IPlayer guest = (IPlayer) Naming.lookup(pPublicName);
			guest.messageFromServer("welcome!");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean iWantToPlay(IPlayer p) throws RemoteException{
			if(addPlayer(p)){
				return true;
			}
		return false;
	}
	
	public void iWantToLeave(int playerId) throws RemoteException{
		/*
		 * Usado para gestionar la correcta salida de un player
		 * */
		
		players.get(playerId).closePlayer();
	}
}
