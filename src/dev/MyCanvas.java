package dev;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

public class MyCanvas extends Canvas {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1104510752775347794L;
	
	public List<Rectangle> rectangles = new ArrayList<Rectangle>();

	public void paint(Graphics graphics) {

		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		graphics.setColor(Color.WHITE);
		for (Rectangle rectangle : rectangles) {
			rectangle.draw(graphics);
		}

	}
}
