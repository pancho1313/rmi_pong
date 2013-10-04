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
	public ArrayList<Rectangle> bars;
	public Rectangle ball;
	public int gameState;

	public MyCanvas(int WIDTH, int HEIGHT){
		super();
		this.setSize(WIDTH, HEIGHT);
		/* convention	 verde (2)
		 * 				 _______
		 * 				|		|
		 * 		azul (3)|		|amarillo (1)
		 * 				|_______|
		 * 				
		 * 				 rojo (0)
		 * 
		 * 
		 * */
		//default values
		ballColor = Color.WHITE;
		ball = new Rectangle(getWidth() / 2, getHeight() / 2, 10, 10, ballColor);
		bars = new ArrayList<Rectangle>();
		bars.add(new Rectangle(10, getHeight() / 2, 10, 100, Color.BLUE));
		bars.add(new Rectangle(getWidth() - 10, getHeight() / 2, 10, 100, Color.YELLOW));
		bars.add(new Rectangle(getWidth() / 2, getHeight() -10, 100, 10, Color.GREEN));
		bars.add(new Rectangle(getWidth() / 2, 10, 100, 10, Color.RED));
		myPlayerId = 0;
		bars = new ArrayList<Rectangle>();
		gameState = Player.WAITING_NEW_MATCH;
	}
	
	public void paint(Graphics g) {

		switch (gameState) {
        case Player.WAITING_NEW_MATCH:
        	paintWaiting(g);
        	break;
        case Player.PLAYING_MATCH:
        	g.setColor(Color.BLACK);
    		g.fillRect(0, 0, getWidth(), getHeight());

    		g.setColor(Color.WHITE);
    		for (Rectangle rectangle : bars) {
    			rectangle.draw(g);
    		}
    		
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
}
