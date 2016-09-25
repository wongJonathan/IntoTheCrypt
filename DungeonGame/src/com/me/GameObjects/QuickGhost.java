package com.me.GameObjects;

import com.me.Engine.Enemy;

public class QuickGhost extends Kamikaze implements Enemy{

	private static int spd = 100;
	private static int w = 2,h =2;
	private static int health = 25;
	
	public QuickGhost(float x, float y) {
		super(x, y, health, spd, 2, 2,1);
		// TODO Auto-generated constructor stub
	}

}
