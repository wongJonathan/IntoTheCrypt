package com.me.GameObjects;

import com.me.Data.AssetLoader;

public class GoalRoom extends Room {

	private boolean isConnected = false;
	
	private int[][] blocks;
	
	private int gx, gy;
	
	private Artifact a;
	
	public GoalRoom(int t, int size, int x, int y, AssetLoader as) {
		super(t, size, x, y, as);
		blocks = super.getBlocks();
		//creates goal
		int gy = (int)(Math.random()*size);
		int gx = (int)(Math.random()*size);
		gx = gx - gx%super.getW();
		gy = gy - gy%super.getH();
		blocks[gx][gy] = 2;
		a = new Artifact(gx,gy,super.getW(),super.getH(),x,y);
	}
	
	public void setConnected(boolean t){
		isConnected = t;
	}
	
	public boolean getConnected(){
		return isConnected;
	}
	
	public int getGX(){
		return gx;
	}
	
	public int getGY(){
		return gy;
	}
	
	public Artifact getArti(){
		return a;
	}

}
