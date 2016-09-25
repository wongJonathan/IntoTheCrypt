package com.me.GameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.me.Engine.Item;

public class Health extends Item{
	
	private Rectangle hitbox;
	
	Health(float x, float y, int w, int h, int wx, int wy) {
		super(x,y,w,h,wx,wy);
		hitbox = new Rectangle(wx+x,wy+y,w,h);
//		hitbox.set(wx, wy, w, h);
		
	}	
}
