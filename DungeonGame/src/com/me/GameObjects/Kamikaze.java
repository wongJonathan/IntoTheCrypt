package com.me.GameObjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.Engine.Enemy;

public class Kamikaze implements Enemy{
	
	private int spd = 50;
	private int health=300;
	private int w =8,h=8;
	private int maxHealth = 300;
	private int type = 0;
	
	private float dir;
	
//	private float x,y;
	
	private Vector2 pos;
	private Vector2 vel;
	private Vector2 accel;
	
	private  Rectangle hitBox;
	
	private Player player;
	
	public Kamikaze(float x, float y){
		pos = new Vector2(x,y);
		vel = new Vector2(0,0);
		hitBox = new Rectangle();

	}
	
	
	public Kamikaze(float x, float y,int health, int spd, int w, int h, int t){
		pos = new Vector2(x,y);
		vel = new Vector2(0,0);
		hitBox = new Rectangle();
		this.spd = spd;
		this.health = health;
		this.w = w;
		this.h = h;
		maxHealth = health;
		type =t;
	}
	
	@Override
	public void update(float delta){
		
		if (player != null){
			checkCollision();
			double angle = Math.atan((player.getPos().y-pos.y)/(player.getPos().x-pos.x));
			if(player.getPos().x >= pos.x){
				vel.x = (float) (spd*Math.cos(angle));
				vel.y = (float) (spd*Math.sin(angle));
			}
			else{
				vel.x = (float) (-spd*Math.cos(angle));
				vel.y = (float) (-spd*Math.sin(angle));
			}
//			System.out.println("VelX: "+vel.x+" VelY: "+vel.y);
			if(player.getPos().x >= pos.x)
				dir = (float)Math.atan((player.getPos().y-pos.y)/(player.getPos().x-pos.x));
			else
				dir = (float)(Math.atan((player.getPos().y-pos.y)/(player.getPos().x-pos.x))+Math.toRadians(180));
			checkCollision();
			
		}
		else{
			vel.x = 0;
			vel.y = 0;
		}
		hitBox.set(pos.x,pos.y,w,h);
		pos.add(vel.cpy().scl(delta));
//		System.out.println("Health"+health);

	}
	
	public float distanceTo(Vector2 e, Vector2 p){
		float distance = (float) Math.sqrt(Math.pow(e.x-p.x, 2)+Math.pow(e.y-p.y, 2));
//		System.out.println("Disatnce "+distance);
		return distance;
	}
	
	private void checkCollision(){
		if(Intersector.overlaps(player.getHitBox(), hitBox)){
			health=-10;
		}
		if(Intersector.overlaps(player.getCircle(), hitBox)){
			health --;
		}
	}

	@Override
	public int getHealth() {
		// TODO Auto-generated method stub
		return health;
	}

	@Override
	public int getDmg() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public int setHealth(int dmg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2 getPos(){
		return pos;
	}

	@Override
	public void setPlayer(Player p) {
		player = p;
		
	}
	
	@Override
	public Player getPlayer(){
		return player;
	}

	@Override
	public float getW() {
		// TODO Auto-generated method stub
		return w;
	}

	@Override
	public float getH() {
		// TODO Auto-generated method stub
		return h;
	}

	@Override
	public Rectangle getHitBox(){
		return hitBox;
	}
	
	public int getMax(){
		return maxHealth;
	}


	@Override
	public float getDir() {
		// TODO Auto-generated method stub
		return dir;
	}
}
