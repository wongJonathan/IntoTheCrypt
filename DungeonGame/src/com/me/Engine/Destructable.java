package com.me.Engine;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Destructable {
	
	private Rectangle hitbox;
	
	public Destructable(){
		hitbox = null;
	}
	
	public Destructable(Rectangle r){
		hitbox = r;
	}
	
	public boolean isHit(Rectangle r){
		if(Intersector.overlaps(hitbox, r)){
			return true;
		}
		return false;
	}
	
	public void destroy(){
		
	}
}
