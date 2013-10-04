/**
 * @author Richard Ibarra Ram�rez (richard.ibarra@gmail.com)
 * 
 *  CC5303 - Primavera 2013
 *  C�tedra. Javier Bustos.
 *  DCC. Universidad de Chile
 */

package dev;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle {

	double x, y;
	double w, h;
	Color color;

	public Rectangle(double x, double y, double w, double h, Color color) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.color = color;
	}

	public void draw(Graphics g) {
		Color prevColor = g.getColor();//TODO: necesario?
		g.setColor(color);
		g.fillRect(left(), bottom(), (int) w, (int) h);
		g.setColor(prevColor);//TODO: necesario?
	}

	public int top() {
		return (int) (y + h * 0.5);
	}

	public int left() {
		return (int) (x - w * 0.5);
	}

	public int bottom() {
		return (int) (y - h * 0.5);
	}

	public int right() {
		return (int) (x + w * 0.5);
	}

}
