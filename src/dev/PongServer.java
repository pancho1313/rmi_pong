package dev;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PongServer extends UnicastRemoteObject implements IPongServer{
	
	private static final int WAITING_FOR_PLAYERS = 0;
	private static final int PLAYING_MATCH = 1;
	private static final int MATCH_FINISHED = 2;
	
	private int serverState;//estado del pongServer
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	//private static final long serialVersionUID = 6311160989789331741L;
	private int nPlayers = 0;
	private String ipHost;
	private IPlayer[] players;
	private int[] playersScore;
	private int lastPlayerRebound;
	
	private int againPlayers;
	private int activePlayers;
	
	private void reInitMatch(){
		playersScore = new int[4];
		lastPlayerRebound = -1;
	}
	
	private boolean addToPlayers(IPlayer p){
		for(int i = 0; i < players.length; i++){
			if(players[i] == null){
				players[i] = p;
				activePlayers++;
				
				try {
					p.setPlayerId(i);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return true;
			}
		}
		return false;
	}
	
	
	private boolean addPlayer(IPlayer p) throws RemoteException{
		
		if(activePlayers < nPlayers){
			IPlayer newPlayer = null;
			int idNewPlayer = 0;
			for(int i = 0; i < players.length; i++){
				if(players[i] == null){
					idNewPlayer = i;
				}
			}
			String playerPublicName = "rmi://"+ipHost+":1099/player"+idNewPlayer;
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
				addToPlayers(newPlayer);
				
				if(serverState == WAITING_FOR_PLAYERS){
					if(activePlayers == nPlayers){
						serverState = PLAYING_MATCH;
						startNewMatch();
					}else{
						int numPlayers = (nPlayers - activePlayers);
						U.localMessage("Waiting " + numPlayers + ((numPlayers > 1)?" players.":" player."));
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * gestionar el comienzo de una partida, la bandeja de players esta comlpleta.
	 * */
	private void startNewMatch() throws RemoteException{
		U.localMessage("Let's play!");
		
		//reiniciar variables
		reInitMatch();
		
		for(IPlayer p : players){
			if(p != null){
				p.startNewGame(1,0.8);//TODO: random?
				p.messageFromServer("Let's play!");
			}
		}
	}
	

	
	static MyUtil U = new MyUtil();
	
	public PongServer(int _nPlayers, String ipHost) throws RemoteException{
		super();
		U.localMessage("PongServer Started.");
		nPlayers = _nPlayers;//TODO: validar el rango de valores
		this.ipHost = ipHost;
		
		activePlayers = 0;
		players = new IPlayer[4];
		
		serverState = WAITING_FOR_PLAYERS;
		U.localMessage("Waiting " + nPlayers + " players.");
		
		reInitMatch();
	}
	
	/**
	 * para que un player pueda ser publicado por el pongServer
	 * */
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
		switch(serverState){
		case WAITING_FOR_PLAYERS:
			return addPlayer(p);
		case PLAYING_MATCH:
			return false;
		case MATCH_FINISHED:
			return false;
		default:
			return false;
		}
	}
	
	/**
	 * Usado para gestionar la correcta salida de un player
	 * */
	public void iWantToLeave(int playerId) throws RemoteException{
		players[playerId].closePlayer();
	}
	
	/**
	 * informa al resto de los jugadores la nueva posicion de su bar.
	 * */
	public void iMovedMyBar(int playerId, double x, double y) throws RemoteException{
		for(int id = 0; id < players.length; id++){
			IPlayer player = players[id];
			if(player != null){
			
				if(id != playerId){//TODO: quizas se pueda aniadir un filtro de jugadores activos?
					players[id].refreshEnemyPos(playerId, x, y);
				}
			
			}
		}
	}
	
	/**
	 * actualiza la posicion de la bola en los demas players,
	 * ademas informa si el player perdio la bola.
	 * */
	public void refreshBall(int playerId, boolean missedBall, double x, double y, double vx, double vy) throws RemoteException{
		
		//asignar puntaje
		boolean refreshScores = false;
		if(missedBall){
			if(lastPlayerRebound >= 0 && lastPlayerRebound != playerId){
				playersScore[lastPlayerRebound]++;
				refreshScores = true;
			}
		}else{
			lastPlayerRebound = playerId;
		}
		
		for(int id = 0; id < players.length; id++){
			IPlayer player = players[id];
			if(player != null){
				
				if(id != playerId){
					players[id].refreshBall(playerId, missedBall, x, y, vx, vy);
				}
				if(refreshScores){
					players[id].refreshScores(playersScore);
				}
				
			}
		}
		
		if(playersScore[lastPlayerRebound] == 1){//TODO: parametrizar puntaje de termino
			gameOver();
		}
	}
	
	private void gameOver(){
		serverState = MATCH_FINISHED;
		againPlayers = 0;
		
		for(int id = 0; id < players.length; id++){
			IPlayer player = players[id];
			if(player != null){
				try {
					player.showMatchResults();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	
	}
	
	public void iWantToPlayAgain(int playerId) throws RemoteException{
		if(serverState == MATCH_FINISHED){
			//TODO: lo registra como preparado
			againPlayers++;
			players[playerId].preNewGame();
			
			//nadie guatio
			if(againPlayers == nPlayers){
			 startNewMatch();
			}
		}
	}
}
