package com.me.Engine;

import com.badlogic.gdx.math.Rectangle;

public class Item {

private float x, y, wx, wy;
	
	private int w,h;
	
	private Rectangle hitbox;
	
	private boolean activated = false;
	
	protected Item(float x, float y, int w, int h, int wx, int wy) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.wx = wx;
		this.wy = wy;
		hitbox = new Rectangle(wx+x,wy+y,w,h);
//		hitbox.set(wx, wy, w, h);
	}
	
	public boolean getActiviated(){
		return activated;
	}
	
	public void setActiviated(boolean t){
//		System.out.println("Deactivated");
		activated = t;
	}

//	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return x;
	}

//	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return y;
	}
	
	public Rectangle getHitbox(){
		return hitbox;
	}
	
	
}
