package com.me.GameObjects;

public class RoomCheck {

	private int x, y, dir;
	private boolean checked = true;
	
	public RoomCheck(int x, int y, int dir){
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getDir(){
		return dir;
	}
	
	public boolean getChecked(){
		return checked;
	}
	
	public void setChecked(boolean c){
		checked = c;
	}
	
}
