package com.me.GameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.me.Engine.Item;

public class Artifact extends Item{
	
//	private float x, y, wx, wy;
//	
//	private int w,h;
//	
	private Rectangle hitbox;
//	
//	private boolean activated = false;
//	
	Artifact(float x, float y, int w, int h, int wx, int wy) {
		super(x,y,w,h,wx,wy);
		hitbox = new Rectangle(wx+x,wy+y,w,h);
//		hitbox.set(wx, wy, w, h);
	}
//	
//	public boolean getActiviated(){
//		return activated;
//	}
//	
//	public void setActiviated(boolean t){
//		System.out.println("Deactivated");
//		activated = t;
//	}
//
////	@Override
//	public float getX() {
//		// TODO Auto-generated method stub
//		return x;
//	}
//
////	@Override
//	public float getY() {
//		// TODO Auto-generated method stub
//		return y;
//	}
//	
//	public Rectangle getHitbox(){
//		return hitbox;
//	}
	
}
