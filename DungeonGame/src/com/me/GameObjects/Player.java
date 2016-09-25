package com.me.GameObjects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.Engine.Enemy;
import com.me.GameWorlds.GWorld;

public class Player implements Collideable{
	
	private Vector2 pos;
	private Vector2 vel;
	private Vector2 acceleratoin;
	private Vector2 lampPos;
	private Vector2 startPos;

	private int spd = 100;
	private int w, h;	
	private int health = 3;
	private int hitTimer;
	
	private float mousex = 0, mousey=0;
	private float dir;
	private float dirRange=30;
	
	private boolean canMoveU = true, canMoveD = true, canMoveL = true, canMoveR = true;
	private boolean u,d,l,r;
	private boolean isHit = false;
	
	private Rectangle hitBox, hitBoxUD, hitBoxLR;
	private Circle lampC;
	private ArrayList<Rectangle> currentRects;
	private GWorld gWorld;
	private OrthographicCamera cam;
	
	public Player(int x, int y, GWorld gw){
		w = 8;
		h = 8;
		gWorld = gw;
		startPos = new Vector2(x,y);
		pos = new Vector2(x,y);
		vel = new Vector2(0,0);
		lampPos = pos;
		hitBox = new Rectangle();
		hitBoxLR = new Rectangle();
		hitBoxUD = new Rectangle();
		hitBox.set(pos.x, pos.y, w, h);
		hitBoxLR.set(pos.x-1,pos.y,w+2,h);
		hitBoxUD.set(pos.x, pos.y-1,w, h+2);
		
		lampC = new Circle();
		lampC.set(lampPos,30);
	}
	
	public void update(float delta){
		move();
		stop();
		//vel is changed with the move()
		//vel is added to the vel which is scaled based on lag.
		pos.add(vel.cpy().scl(delta));
		hitBox.set(pos.x,pos.y,w,h);
		hitBoxLR.set(pos.x-1,pos.y,w+2,h);
		hitBoxUD.set(pos.x, pos.y-1,w, h+2);
		
		if(cam!=null){
		mousex = cam.position.x-cam.viewportWidth/2+Gdx.input.getX();
//				(gWorld.getCurrentXBounds() +gWorld.getSize()/2)-(Gdx.graphics.getWidth()/2-Gdx.input.getX()+(pos.x-gWorld.getCurrentXBounds()));
		mousey = cam.position.y-cam.viewportHeight/2+Gdx.input.getY(); 
//				(gWorld.getCurrentXBounds()+gWorld.getSize()/2)-(Gdx.graphics.getHeight()/2-Gdx.input.getY() +(pos.y - gWorld.getCurrentYBounds()));
		}
		//Get dir of the player and the mouse
		if(mousex >= pos.x)
			dir = (float) Math.atan( (mousey - pos.y) / (mousex - pos.x) );
		else
			dir = (float) ( Math.atan( (mousey - pos.y) / (mousex - pos.x) )+Math.toRadians(180));

//		dir = (float) Math.toDegrees(dir);
		
		lampC.set(new Vector2((float)((Math.cos(dir)*50)+pos.x),(float)((Math.sin(dir)*50)+pos.y)),30 );
		
		isHit = hitEnemy();
		
		
		
		if(isHit && hitTimer <= 0){
			hitTimer = 50;
			health--;
			System.out.println("Im hurt");
		}
					
		if(hitTimer > 0)
			hitTimer--;
		else
			isHit = false;
		if(health > 4){
			health = 4;
		}
	}
	
	private void collisionCheckDir(){
		if(currentRects!=null){
			for(Rectangle rec: currentRects){
				float distanceX = 0;
				float distanceY = 0;
				if(Intersector.overlaps(hitBoxLR, rec)){
//					System.out.println("hitting LR");

					if(pos.x > rec.x){
						if(pos.x > rec.x && l){
							//Rec to the left
							//Finds how deep the player is in the wall
							distanceX = Math.abs((rec.x+rec.width)-pos.x);
//							if(distanceX > w){
//								distanceX = w;
//								System.out.println("Distance: "+distanceX);
//							}
							//Changes the position of the player based on this distance
//							pos.add(distanceX, 0);
							//Cannot move left anymore
							canMoveL = false;
							//Sets// the velocity to 0
							vel.x = 0;
						
						}
					}
					else
						canMoveL = true;
					if(pos.x < rec.x && r){
						//Rec to the right
						distanceX -= Math.abs((pos.x+w)-rec.x);
//						if(distanceX > w){
//							distanceX -= w;
//							System.out.println("Distance: "+distanceX);
//						}
//						pos.add(-distance, 0);
						vel.x = 0;
//						System.out.println("right");
						canMoveR = false;
					}
					else
						canMoveR = true;
				}
				else{
					canMoveR = true;
					canMoveL = true;
				}
				
				if(Intersector.overlaps(hitBoxUD,rec)){
					if(pos.y > rec.y && u){
						//Rec above
						distanceY = Math.abs((rec.y+rec.height)-pos.y);
//						if(distanceY > h){
//							distanceY = h;
//							System.out.println("distanceY: "+distanceY);
//						}
//						pos.add(0,distanceY);
						vel.y = 0;
						canMoveU = false;
					}
					else
						canMoveU = true;
					if (pos.y < rec.y && d){
						//Rec below
						distanceY = Math.abs((pos.y+h)-rec.y);
//						if(distanceY > h){
//							distanceY = h;
//							System.out.println("distanceY: "+distanceY);
//						}
//						pos.add(0,-distanceY);
						vel.y = 0;
						canMoveD = false;
					}
					else{
						canMoveD = true;
					}
					
				}
				else{
					canMoveD = true;
					canMoveU = true;
				}
				
				if(Math.abs(distanceX) > w)
					distanceX = w *(distanceX >0? 1:-1);
				if(Math.abs(distanceY) > h){
					distanceY = h*(distanceY > 0? 1:-1);
				}
				
				pos.add(distanceX, distanceY);
				if(pos.x < gWorld.getSize())
					pos.add(Math.abs(pos.x-gWorld.getSize())+w,0);
				if(pos.x > gWorld.getWidth()-gWorld.getSize())
					pos.add(gWorld.getWidth()-gWorld.getSize() - pos.x,0);
				if(pos.y < gWorld.getSize())
					pos.add(0,gWorld.getSize()-pos.y);
				if(pos.y > gWorld.getHeight()-gWorld.getSize())
					pos.add(0,gWorld.getHeight()-gWorld.getSize()-pos.y);
			}
		}
	}
	
	private Boolean hitEnemy(){
		for(Enemy e:gWorld.getEnemies()){
			if(Intersector.overlaps(e.getHitBox(),hitBoxLR)){
				return true;
			}
		}
		
		return false;
	}
	
	
	public void move(){

		if(u && canMoveU){
			vel.y = -spd;
//			collisionCheckDir(0);
		}
		if(d && canMoveD){
			vel.y = spd;
//			collisionCheckDir(2);
		}
		if(l && canMoveL){
			vel.x = -spd;
//			collisionCheckDir(3);
		}
		if(r && canMoveR){
			vel.x = spd;
//			collisionCheckDir(1);
		}
		collisionCheckDir();
//
	}
	
	public void stop(){
		if( u && d)
			vel.y = 0;
		else if(!u && vel.y < 0)
			vel.y = 0;
		else if(!d && vel.y > 0)
			vel.y = 0;
		if(r && l)
			vel.x = 0;
		else if(!r && vel.x > 0)
			vel.x = 0;
		else if(!l && vel.x < 0)
			vel.x = 0;
	}
	
	
	
	public void getInputD(int dir){
		switch(dir){
		case 0:
			u = true;
			break;
		case 1:
			r = true;
			break;
		case 2:
			d = true;
			break;
		case 3:
			l = true;
			break;
		}
	}
	
	public void getInputU(int dir){
		switch(dir){
		case 0:
			u = false;
			break;
		case 1:
			r = false;
			break;
		case 2:
			d = false;
			break;
		case 3:
			l = false;
			break;
		}
	}

	@Override
	public Rectangle box() {
		// TODO Auto-generated method stub
		return hitBox;
	}
	
	public Circle getCircle(){
		return lampC;
	}

	public Vector2 getPos() {
		return pos;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}
	
	public void set(float x, float y){
		pos.set(x, y);
	}
	
	public void setRects(ArrayList<Rectangle> arrayList){
		currentRects = arrayList;
	}
	
	public ArrayList<Rectangle> getRects(){
		return currentRects;
	}
	
	public Rectangle getHitBox(){
		return hitBoxLR;
	}
	
	public void getMouse(int x, int y){
		mousex = x;
		mousey = y;
	}
	
	public float getDir(){
		return dir;
	}
	
	public void getCam(OrthographicCamera cam){
		this.cam = cam;
	}
	
	public void setHealth(int h){
		health += h;
	}

	public int getHealth() {
		// TODO Auto-generated method stub
		return health;
	}
	
	public boolean isPlayerMoving(){
		return l||u||r||d;
	}
}
