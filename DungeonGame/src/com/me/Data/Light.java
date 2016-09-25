package com.me.Data;

import com.badlogic.gdx.graphics.Color;

public class Light {
	private float x, y;
	private Color color;
	
	public Light(float x, float y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	
}
