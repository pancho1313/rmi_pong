package dev;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JFrame;

public class Pong implements KeyListener {

	public final static String TITLE = "Pong - CC5303";
	public final static int WIDTH = 640, HEIGHT = 480;
	public final static int UPDATE_RATE = 60;
	public final static int DX = 5;
	public final static double DV = 0.1;

	public JFrame frame;
	public MyCanvas canvas;

	public ArrayList<Bar> bars;
	private Bar myBar;
	public Bar ball;

	private double vx = 0.4, vy = 0.3;

	public boolean[] keysPressed;
	public boolean[] keysReleased;
	
	//TODO: is this right?
	private Player myPlayer;
	private IPongServer pongServer;

	public Pong(Player _myPlayer, IPongServer _pongServer) {

		keysPressed = new boolean[KeyEvent.KEY_LAST];
		keysReleased = new boolean[KeyEvent.KEY_LAST];

		myPlayer = _myPlayer;
		pongServer = _pongServer;
		
		init();

	}

	/* Initializes window frame and set it visible */
	public void init() {

		frame = new JFrame(TITLE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		canvas = new MyCanvas(WIDTH, HEIGHT);
		canvas.myPlayerId = myPlayer.getPlayerId();
		frame.add(canvas);

		//canvas.setSize(WIDTH, HEIGHT);
		bars = canvas.bars;
		myBar = bars.get(myPlayer.getPlayerId());//TODO: ojo con una posible reasignacion de bars
		ball = canvas.ball;

		frame.pack();
		frame.addKeyListener(this);
		frame.addWindowListener(
			new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			    	try {
						pongServer.iWantToLeave(myPlayer.getPlayerId());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
		    }
		);

		Thread game = new Thread(new Runnable(){

			@Override
			public void run(){
				while (myPlayer.showPlayerInterface()){
					
					/* decidir que hacer segun el estado del juego */
					int state = myPlayer.getGameState();
			        switch (state) {
			            case Player.WAITING_NEW_MATCH:
			            	/*algo();*/
			            	break;
			            case Player.BRACE_YOURSELF:
			            	sendMyBarPos();
			            	myPlayer.setGameState(Player.PLAYING_MATCH);
			            	break;
			            case Player.PLAYING_MATCH:
			            	/*algo();*/
			            	moveBall();
			            	break;
			            case Player.MATCH_FINISHED:
			            	/*algo();*/
			            	break;
			            case Player.SHOW_MATCH_RESULTS:
			            	/*algo();*/
			            	break;
			            default:
			            	/*algoDefault();*/
			            	break;
			        }
					
			        //procesar el input del usuario
			        userKeys(state);
			        
			        //actualizar las posiciones de los enemigos
			        refreshEnemyDrawingPos();
			        
					//repintar el canvas
					canvas.gameState = state;
					canvas.repaint();

					//regular los fps
					try {
						Thread.sleep(1000 / UPDATE_RATE); // milliseconds
					} catch (InterruptedException ex) {
					}
				}
				
				frame.dispose();//para matar la ventana del player
			}
		});
		game.start();
	}

	/*------------------------------------------*/
	/*TODO: mover este grupo a MyUtil?*/
	
	@Override
	public void keyPressed(KeyEvent event) {
		keysPressed[event.getKeyCode()] = true;
		keysReleased[event.getKeyCode()] = false;
	}

	@Override
	public void keyReleased(KeyEvent event) {
		keysPressed[event.getKeyCode()] = false;
		keysReleased[event.getKeyCode()] = true;

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	/*----------------------------------------*/
	
	/**
	 * avisa al servidor que el player desea retirarse del juego.
	 * */
	private void exitGame(){
		try {
			pongServer.iWantToLeave(myPlayer.getPlayerId());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void moveBall(){
		//TODO: separar en más sub-metodos.
		// actualiza posicion
		ball.x += vx * DX;
		ball.y += vy * DX;

		// rebote en y
		if (ball.y + ball.h * 0.5 >= HEIGHT
				|| ball.y - ball.h * 0.5 <= 0) {
			vy = -vy;
		}

		

		for (int i = 0; i < bars.size(); i++) {
			Bar bar = bars.get(i);
			if (ball.bottom() < bar.top()
					&& ball.top() > bar.bottom()) { // esta dentro
													// en
													// Y
				if ((vx > 0 && ball.left() <= bar.left() && ball
						.right() >= bar.left()) // esta a la
												// izquierda y se
												// mueve a la
												// derecha
						// o esta a la derecha y se mueve hacia la
						// izquierda
						|| (vx < 0 && ball.right() >= bar.right() && ball
								.left() <= bar.right())) {

					vx = -vx * (1 + DV);
					break;
				}
			}
		}

		/*
		 * for (Rectangle bar : bars) { if (ball.x + ball.w * 0.5 >
		 * bar.x - bar.w * 0.5 && ball.x - ball.w * 0.5 > bar.x +
		 * bar.w * 0.5) { if ((vy > 0 && ball.y + ball.h * 0.5 >=
		 * bar.y - bar.h * 0.5) || (vy < 0 && ball.y - ball.h * 0.5
		 * <= bar.y + bar.h * 0.5)) { vy = -vy; break; } } }
		 */
	}
	
	/**
	 * procesa las teclas presionadas por el usuario segun el estado del juego.
	 * */
	 private void userKeys(int gameState){
		 switch (gameState) {
         case Player.WAITING_NEW_MATCH:
         	/*algo();*/
         	break;
         case Player.PLAYING_MATCH:
			if(keysPressed[KeyEvent.VK_Q]){
				exitGame();
			}
			if(myPlayer.getPlayerId() < 2){//izq der
				if(keysPressed[KeyEvent.VK_UP]){
					if(myBar.top() - DX >= 0)
						myBar.y -= DX;
				}
				if(keysPressed[KeyEvent.VK_DOWN]){
					if(myBar.bottom() + DX < HEIGHT)
						myBar.y += DX;
				}
			}else{//down up
				if(keysPressed[KeyEvent.VK_LEFT]){
					if(myBar.left() - DX > 0)
						myBar.x -= DX;
				}
				if(keysPressed[KeyEvent.VK_RIGHT]){
					if(myBar.right() + DX < WIDTH)
						myBar.x += DX;
				}
			}
			/////////////////////////////////////////////
			boolean keyReleased = false;
			if(keysReleased[KeyEvent.VK_RIGHT]){
				keysReleased[KeyEvent.VK_RIGHT] = false;
				keyReleased = true;
			}
			if(keysReleased[KeyEvent.VK_LEFT]){
				keysReleased[KeyEvent.VK_LEFT] = false;
				keyReleased = true;
			}
			if(keysReleased[KeyEvent.VK_UP]){
				keysReleased[KeyEvent.VK_UP] = false;
				keyReleased = true;
			}
			if(keysReleased[KeyEvent.VK_DOWN]){
				keysReleased[KeyEvent.VK_DOWN] = false;
				keyReleased = true;
			}
			if(keyReleased)
				sendMyBarPos();
         	/*algo();*/
         	break;
         case Player.MATCH_FINISHED:
         	/*algo();*/
         	break;
         case Player.SHOW_MATCH_RESULTS:
         	/*algo();*/
         	break;
         default:
         	/*algoDefault();*/
         	break;
		 }
	 }
	 
	 /**
	  * informa al servidor la nueva posicio de mi bar
	  * */
	 private void sendMyBarPos(){
		 try {
			pongServer.iMovedMyBar(myPlayer.getPlayerId(), myBar.x, myBar.y);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 /**
	  * actualiza las posiciones de los enemigos segun la info del myPlayer asociado.
	  * */
	 private void refreshEnemyDrawingPos(){
		 for(int id = 0; id < bars.size(); id++){
			 if(id != myPlayer.getPlayerId()){
				 Bar enemyBar = bars.get(id);
				 if(!myPlayer.activePlayers[id]){
					 enemyBar.hidden = true;
				 }else{
					 enemyBar.hidden = false;
					 enemyBar.x = myPlayer.barsPos[id][0];
					 enemyBar.y = myPlayer.barsPos[id][1];
				 }
			 }
		 }
	 }
}
