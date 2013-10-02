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
	private String[] playersPublicName;
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
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	private void readyToPlay() throws RemoteException{
		for(IPlayer p : players){
			p.showServerMesage("Game!");
		}
	}
	

	
	static MyUtil U = new MyUtil();
	
	public Pong(int _nPlayers, String ipHost) throws RemoteException{
		super();
		nPlayers = _nPlayers; 
		this.playersPublicName = new String[_nPlayers];
		this.ipHost = ipHost;
		players = new ArrayList<IPlayer>();
	}
	
	public void sendPlayer(String pPublicName, IPlayer p) throws RemoteException{
		try {
			Naming.rebind(pPublicName, p);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			IPlayer he = (IPlayer) Naming.lookup(pPublicName);
			he.showServerMesage("welcome!");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String iWantToPlay(IPlayer p) throws RemoteException{//[String playerPublicName, String message]
		U.cMessage("Someone wants to play.");
		//if(players.size() +1 == nPlayers){
			if(addPlayer(p)){
				return "ok, plase wait...";
			}
		//}
		return "sorry, we are full.";
	}
}
