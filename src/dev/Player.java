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
	public static final int BRACE_YOURSELF = 1;//transitorio, para saludar a los contendientes
	public static final int PLAYING_MATCH = 2;
	public static final int SHOW_MATCH_RESULTS = 3;//TODO: opcion para jugar de nuevo?
	public static final int GAME_OVER = 4;
	public static final int RESET = 5;
	
	
	public boolean refreshEnemyBars;
	public double[][] barsPos;
	public boolean[] activePlayers;//para saber a que players considerar en la partida
	public boolean refreshScores;
	public int[] scores;
	public boolean refreshBallPos;
	public boolean refreshBallColor;
	public double[] ballParameters;
	private int gameState;//estado del juego del player
	
	/*-------------------------------*/
	
	
	
	static MyUtil U = new MyUtil();
	
	
	/**
	 * para cerrar la ventana (frame) que muestra la animacion del juego
	 * */
	private void closeUserWindow(){
		runUserWindow = false;
	}
	
	private void setBall(int enemyId, boolean missedBall, double x, double y, double vx, double vy){
		refreshBallPos = true;
		
		ballParameters[0] = x;
		ballParameters[1] = y;
		ballParameters[2] = vx;
		ballParameters[3] = vy;
		
		refreshBallColor = !missedBall;
		ballParameters[4] = (double)enemyId;
	}
	
	/**
	 * Constructor:
	 * crea un nuevo player
	 * */
	public Player() throws RemoteException{
		super();
		
		//variables de estado
		
		reInit();
		gameState = WAITING_NEW_MATCH;
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
	
	public void setGameState(int newState){
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
		U.localMessage("...Game Over");
		//System.exit(0);
	}
	
	
	private void reInit(){
		runUserWindow = true;
		gameState = RESET;
		refreshEnemyBars = false;
		barsPos = new double[4][2];//4 players, 2 coordenadas cada uno
		activePlayers = new boolean[4];//inicialmente false
		scores = new int[4];
		refreshBallPos = false;
		refreshBallColor = false;
		refreshScores = true;
		ballParameters = new double[5];//[x,y,vx,vy,color]
	}
	/**
	 * setea las variables del player para prepararse a empezar una nueva partida de pong
	 **/
	public void preNewGame() throws RemoteException{
		reInit();
	}
	
	public void startNewGame(double ballVX, double ballVY) throws RemoteException{
		reInit();
		setBall(-1, false, 10,30,ballVX,ballVY);//TODO: empezar desde la mitad del tablero
	}
	
	
	/**
	 * actualiza el registro de posiciones de bars.
	 * */
	public void refreshEnemyPos(int enemyId, double x, double y) throws RemoteException{
		refreshEnemyBars = true;
		activePlayers[enemyId] = true;//TODO: eto desperdicia el sistema local
		barsPos[enemyId][0] = x;
		barsPos[enemyId][1] = y;
	}
	
	/**
	 * actualiza la posicion de la bola luego de un rebote "ajeno".
	 * */
	public void refreshBall(int enemyId, boolean missedBall, double x, double y, double vx, double vy) throws RemoteException{
		setBall(enemyId,missedBall,x,y,vx,vy);
	}
	
	public void refreshScores(int[] scores) throws RemoteException{
		this.scores = scores;
		refreshScores = true;
	}
	
	public void showMatchResults() throws RemoteException{
		setGameState(SHOW_MATCH_RESULTS);
	}
	
	public void enemyGone(int enemyId) throws RemoteException{
		scores[enemyId] = 0;
		activePlayers[enemyId] = false;
		refreshEnemyBars = true;
	}
}
