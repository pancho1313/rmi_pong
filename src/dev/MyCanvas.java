package dev;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Canvas;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

public class MyCanvas extends Canvas {

	/* convention	 verde (3)
	 * 				 _______
	 * 				|		|
	 * 		azul (0)|		|amarillo (1)
	 * 				|_______|
	 * 				
	 * 				 rojo (2)
	 * */
	public static final int LEFT_BLUE = 0;
	public static final int RIGHT_YELLOW = 1;
	public static final int BOTTOM_RED = 2;
	public static final int TOP_GREEN = 3;
	
	public static final Color[] COLORS = new Color[]{Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN};
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1104510752775347794L;
	
	public int myPlayerId;
	public Color ballColor;
	public Bar[] bars;
	public Bar ball;
	public int gameState;
	public int[] scores;

	public MyCanvas(int WIDTH, int HEIGHT){
		super();
		this.setSize(WIDTH, HEIGHT);
		
		//default values
		ballColor = Color.WHITE;
		ball = new Bar(getWidth() / 2, getHeight() / 2, 10, 10, ballColor);
		bars = new Bar[4];
		bars[0] = new Bar(10, getHeight() / 2, 10, 100, Color.BLUE);
		bars[1] = new Bar(getWidth() - 10, getHeight() / 2, 10, 100, Color.YELLOW);
		bars[2] = new Bar(getWidth() / 2, getHeight() -10, 100, 10, Color.RED);
		bars[3] = new Bar(getWidth() / 2, 10, 100, 10, Color.GREEN);
		scores = new int[4];
		myPlayerId = 0;
		gameState = Player.WAITING_NEW_MATCH;
	}
	
	public void paint(Graphics g) {

		switch (gameState) {
        case Player.WAITING_NEW_MATCH:
        	paintWaiting(g);
        	break;
        case Player.PLAYING_MATCH:
        	paintPlaying(g);
        	break;
        case Player.GAME_OVER:
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
	
	private void paintWaiting(Graphics g){
		//waiting message
		String waitPlease = "Waiting more players...";
		
		//fondo
		g.setColor(bars[myPlayerId].color);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//texto
		g.setColor(Color.WHITE);
		FontMetrics fm = g.getFontMetrics();
	    fm = g.getFontMetrics();
	    int w = fm.stringWidth(waitPlease);
	    int h = fm.getAscent();
	    g.drawString(waitPlease, (getWidth()/2) - (w / 2), (getHeight()/2) + (h / 4));
	}
	
	private void paintPlaying(Graphics g){
		//fondo
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		//ball
		g.setColor(Color.WHITE);
		ball.draw(g);
		
		//bars
		for (Bar rectangle : bars) {
			rectangle.draw(g);
		}
		
		//scores
		for(int i = 0; i < scores.length; i++){
			String score = scores[i] + "";
			if(!bars[i].hidden){
				g.setColor(Color.BLACK);
				FontMetrics fm = g.getFontMetrics();
			    fm = g.getFontMetrics();
			    int w = fm.stringWidth(score);
			    int h = fm.getAscent();
			    g.drawString(score, (int)bars[i].x - (w / 2), (int)bars[i].y + (h / 2));
			}
		}
	}
}
