package dev;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class PongServer  implements IServer{
	
	private int nPlayers = 0;
	private String[] players;
	
	private boolean addPlayer(){
		for(int i = 0; i < players.length; i++){
			String p = players[i];
			if(p == null){
				players[i] = "p"+i;
				nPlayers++;
				//TODO: publicar los players para hacer broadcast!
				return true;
			}
		}
		
		return false;
	}
	
	private void readyToPlay(){
		//TODO: broadcast para avisar que el juego comienza!
	}
	
	static MyUtil U = new MyUtil();
	
	public PongServer(int _nPlayers){
		this.players = new String[_nPlayers];
		
		//TODO: ver si ya contiene nulls para evitar hacer esto
		for(int i = 0; i < this.players.length; i++){
			this.players[i] = null;
		}
	}
	
	public String iWantToPlay() throws RemoteException{
		U.cMessage("Someone wants to play.");
		
		if(addPlayer()){
			if(nPlayers == players.length){
				readyToPlay();
			}
			return "ok, plase wait.";
		}else{
			return "sorry, we are full.";
		}
	}
	
	public static void main(String[] args) {
		String ipHost = U.getIpHost(args);
		////////////////////////////////
		
		
		try {
			System.setProperty("java.rmi.server.hostname", ipHost);
			PongServer pongServer = new PongServer(2);
			Naming.rebind("rmi://localhost:1099/PongServer", pongServer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
