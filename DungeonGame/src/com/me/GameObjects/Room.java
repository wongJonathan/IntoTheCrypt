package com.me.GameObjects;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.me.Data.AssetLoader;


public class Room {
	
	private int type; //1 = wall, 0 = open, 2 = Artifact (goal room specific), 3 = enemy, 4 = health;
	private int size;
	private int x,y;
	private int w,h;
	private int numbOfBlocks;//Determines ahow many walls
	
	private int[][] blocks;
	
	private boolean createHealth = false;
	
	private ArrayList<Integer> openx, openy; //Open blocks in the room
	private ArrayList<Rectangle> hitBoxes;
	private ArrayList<TextureRegion> rlist;
	
	private Health he;
	private AssetLoader assloader;
	
	private boolean isPlayerin = false;
	
	public Room(int t, int size, int x, int y, AssetLoader a){
		type = t;
		w = 16;
		h = 16;
		this.size = size;
		blocks = new int[size][size]; // When created everything is 0
		openx = new ArrayList<Integer>();
		openy = new ArrayList<Integer>();
		hitBoxes = new ArrayList<Rectangle>();
		this.x = x;
		this.y = y;
		numbOfBlocks = size;
		assloader = a;
//		System.out.println("Size:"+size);
		createRoom();
	}
	
	private void createRoom(){
		//Checks if the room is a solid block or not 
		//if it is just skip the whole room creation
		
		if(type != 1){
	
			for(int c = 0; c < size/8;c++){
				int by = (int)(Math.random()*size);
				int bx = (int)(Math.random()*size);
				bx = bx - bx%w;
				by = by - by%h;
//				System.out.println(bx+", "+by);
				if(blocks[bx][by] !=1){
				blocks[bx][by] = 1;
				openx.add(bx);
				openy.add(by);

				}
			}
			if(type != 3)
			for(int e = 0; e < Math.random()*5+1; e++){
				int ey = (int)(Math.random()*size);
				int ex = (int)(Math.random()*size);
				ex = ex - ex%w;
				ey = ey - ey%h;
				blocks[ex][ey] =(int)( Math.random()*3)+5;
//				System.out.println(bx+", "+by);
			}
			
			if(Math.random() <.3){
				createHealth = true;
				System.out.println("Health");
				int convertWall =(int)( Math.random()*openx.size());
				blocks[openx.get(convertWall)][openy.get(convertWall)] = 4;
				he = new Health(openx.get(convertWall),openy.get(convertWall),w,h,x,y);
				openx.remove(convertWall);
				openy.remove(convertWall);
			}
		}
		else{
			for(int[] subarray : blocks) {
		        Arrays.fill(subarray, 1);
			}
		}
	}
	
	//Updates the new room to find walls
	public void updateRects(){
		if(type!=1)
		for(int i = 0; i < size; i +=w){
			for(int j = 0; j < size; j+=h){
				if(blocks[i][j] == 1) // 1 = wall
					{
					hitBoxes.add(new Rectangle(i+x,j+y,w,h));
					}
			}
		}
		else
			hitBoxes.add(new Rectangle(x,y,size,size));
	}
	
	//Updates the hitBoxes if not in correct position?
	public void changePosition(float x2, float y2){
		if( !(x2 >x && x2 < x + size) ){
//			x = (int) (x2-x2%size);
//			y = (int) (y2 - y2%size);
			updateRects();
			System.out.println("Fix");
		}
		
		if( !(y2 >y && y2 < y + size) ){
			x = (int) (x2-x2%size);
			y = (int) (y2 - y2%size);
			updateRects();
			System.out.println("Fix");
		}
	}

	
	public int getType(){
		return type;
	}
	
	public int getBlockType(int x, int y){
		return blocks[x][y];
	}
	
	public ArrayList<Integer> getOpenX(){
		return openx;
	}
	
	public ArrayList<Integer> getOpenY(){
		return openy;
	}
	
	public ArrayList<Rectangle> getHitBoxes(){
		return hitBoxes;
	}
	
	public void destroyArray(){
		hitBoxes = new ArrayList();
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	//Scaled to array size not pixel size
	public int getArrayX(){
		return x/size;
	}
	
	public int getArrayY(){
		return y/size;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}
	public String toString(){
		String printOut = "int x: "+x+" int Y: "+y+"\n";
		return printOut;
	}
	
	public int[][] getBlocks(){
		return blocks;
	}
	
	public boolean isHealth(){
		return createHealth;
	}
	
	public Health getHealth(){
		return he;
	}
	
	public ArrayList<TextureRegion> getR(){
		return rlist;
	}
}