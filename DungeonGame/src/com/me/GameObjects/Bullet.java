package com.me.GameObjects;

import com.badlogic.gdx.math.Vector2;

public class Bullet {

	private float x,y;
	private float tx,ty;
	
	private int spd = 100;
	
	private Vector2 pos;
	private Vector2 vel;
	
	private boolean destroyed = false;
	
	public Bullet(float x, float y, float tx, float ty){
		pos = new Vector2(x,y);
		System.out.println("X: "+pos.x+", Y: "+pos.y+" tx: "+tx+" ty: "+ty);
		vel = new Vector2(0,0);
		this.tx = tx;
		this.ty = ty;
	}
	
	public void update(float delta){
		if(Math.abs(pos.x - tx) >3 && Math.abs(pos.y - ty) > 3){
			double angle = Math.atan((ty-pos.y)/(tx-pos.x));
			if(tx >= pos.x){
				vel.x = (float) (spd*Math.cos(angle));
				vel.y = (float) (spd*Math.sin(angle));
			}
			else{
				vel.x = (float) (-spd*Math.cos(angle));
				vel.y = (float) (-spd*Math.sin(angle));
			}
//			System.out.println("X: "+pos.x+", Y: "+pos.y);
			pos.add(vel.cpy().scl(delta));

		}
		else{
			System.out.println("Destroyed");
			destroyed = true;
		}
	}

	public Vector2 getPos() {
		return pos;
	}
	
	public boolean getDest(){
		return destroyed;
	}
	
	public void setDest(boolean s){
		destroyed = s;
	}

	public float getTx() {
		return tx;
	}

	public float getTy() {
		return ty;
	}
	
	
}
