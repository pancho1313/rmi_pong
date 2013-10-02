package dev;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Pong extends UnicastRemoteObject implements IPong{
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 6311160989789331741L;
	private int nPlayers = 0;
	private String ipHost;
	private ArrayList<IPlayer> players;
	
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
				newPlayer.sendMessage("Welcome to Pong.");
				newPlayer.sendMessage("Waiting for more players...");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(newPlayer != null){//el player fue correctamente inicializado
				players.add(newPlayer);
				
				if(players.size() == nPlayers){
					readyToPlay();
				}else{
					int numPlayers = (nPlayers - players.size());
					U.localMessage("Waiting " + numPlayers + " player"+ ((numPlayers > 1)?"s.":"."));
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	private void readyToPlay() throws RemoteException{
		U.localMessage("Let's play!");
		for(IPlayer p : players){
			p.sendMessage("Let's play!");
		}
	}
	

	
	static MyUtil U = new MyUtil();
	
	public Pong(int _nPlayers, String ipHost) throws RemoteException{
		super();
		nPlayers = _nPlayers;
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
			guest.sendMessage("welcome!");
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
}
