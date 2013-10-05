package dev;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;



public class Player extends UnicastRemoteObject implements IPlayer{

	/*-------variables de estado-----*/
	public boolean runUserWindow;
	private int id = -1;//TODO: verificar correctitud (el -1 podría servir para debug)
	
	public static final int WAITING_NEW_MATCH = 0;
	public static final int PLAYING_MATCH = 1;
	public static final int MATCH_FINISHED = 2;
	public static final int SHOW_MATCH_RESULTS = 3;//TODO: opcion para jugar de nuevo?
	
	public double[][] barsPos;
	public boolean[] activePlayers;//para saber a que players considerar en la partida
	private int gameState;//estado del juego del player
	
	/*-------------------------------*/
	
	
	
	static MyUtil U = new MyUtil();
	
	
	/**
	 * para cerrar la ventana (frame) que muestra la animacion del juego
	 * */
	private void closeUserWindow(){
		runUserWindow = false;
	}
	
	
	/**
	 * Constructor:
	 * crea un nuevo player
	 * */
	public Player() throws RemoteException{
		super();
		
		//variables de estado
		runUserWindow = true;
		gameState = WAITING_NEW_MATCH;
		barsPos = new double[4][2];//4 players, 2 coordenadas cada uno
		activePlayers = new boolean[4];
	}
	
	public void messageFromServer(String message) throws RemoteException{
		U.localMessage("server: " + message);
	}
	
	/*------------GETTERS y SETTERS-------------*/
	public void setPlayerId(int _id) throws RemoteException{
		id = _id;
	}
	
	public int getPlayerId(){
		return id;
	}
	
	private void setGameState(int newState){
		gameState = newState;
	}
	
	public int getGameState(){
		return gameState;
	}
	
	/**
	 * para saber si es necesario mostrar la interfaz (JFrame) del player.
	 * */
	public boolean showPlayerInterface(){
		return runUserWindow;
	}
	
	/*---------------------------------------------*/
	
	/**
	 * gestiona la retirada de un player
	 * */
	public void closePlayer() throws RemoteException{
		closeUserWindow();
	}
	
	/**
	 * setea las variables del player para prepararse a empezar una nueva partida de pong
	 **/
	public void preNewGame() throws RemoteException{
		setGameState(this.WAITING_NEW_MATCH);
	}
	
	public void startNewGame() throws RemoteException{
		setGameState(this.PLAYING_MATCH);
	}
	
	/**
	 * actualiza el registro de posiciones de bars.
	 * */
	public void refreshEnemyPos(int enemyId, double x, double y) throws RemoteException{
		barsPos[enemyId][0] = x;
		barsPos[enemyId][1] = y;
	}
	
}
