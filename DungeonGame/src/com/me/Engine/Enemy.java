package com.me.Engine;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.GameObjects.Player;

public interface Enemy {

	public int getHealth();
	public int getDmg();
	public int getType();
	public int setHealth(int dmg);
	public int getMax();
	
	public Vector2 getPos();
	
	public Player getPlayer();
	
	public float getW();
	public float getH();
	public float getDir();
	
	public void update(float delta);
	public void setPlayer(Player p);
	
	public Rectangle getHitBox();
	
}
