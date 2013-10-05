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


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1104510752775347794L;
	
	public int myPlayerId;
	public Color ballColor;
	public ArrayList<Bar> bars;
	public Bar ball;
	public int gameState;

	public MyCanvas(int WIDTH, int HEIGHT){
		super();
		this.setSize(WIDTH, HEIGHT);
		/* convention	 verde (3)
		 * 				 _______
		 * 				|		|
		 * 		azul (0)|		|amarillo (1)
		 * 				|_______|
		 * 				
		 * 				 rojo (2)
		 * */
		//default values
		ballColor = Color.WHITE;
		ball = new Bar(getWidth() / 2, getHeight() / 2, 10, 10, ballColor);
		bars = new ArrayList<Bar>();
		bars.add(new Bar(10, getHeight() / 2, 10, 100, Color.BLUE));
		bars.add(new Bar(getWidth() - 10, getHeight() / 2, 10, 100, Color.YELLOW));
		bars.add(new Bar(getWidth() / 2, getHeight() -10, 100, 10, Color.RED));
		bars.add(new Bar(getWidth() / 2, 10, 100, 10, Color.GREEN));
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
	
	private void paintWaiting(Graphics g){
		//waiting message
		String waitPlease = "Waiting more players...";
		
		//fondo
		g.setColor(bars.get(myPlayerId).color);
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
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (Bar rectangle : bars) {
			rectangle.draw(g);
		}
	}
}
