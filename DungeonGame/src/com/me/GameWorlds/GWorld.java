package com.me.GameWorlds;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.Data.AssetLoader;
import com.me.Engine.DungeonCreater;
import com.me.Engine.Enemy;
import com.me.GameObjects.Artifact;
import com.me.GameObjects.Bullet;
import com.me.GameObjects.Health;
import com.me.GameObjects.Player;
import com.me.GameObjects.Room;

public class GWorld {

	private Renderer render;
	private DungeonCreater dCreator;
	private Player player;
	private AssetLoader assLoader ;
	
	private int width, height;
	private int size = 512;
	private int currentXBounds, currentYBounds;
	private int goalNumb = 5;
	private int soundTimer = 0, walkingtimer =0;
	
	private ArrayList<Artifact> aList; //Is gathered in DungeonCreation;
	private ArrayList<Enemy> eList;
	private ArrayList<Bullet> bList;
	private ArrayList<Health> hList;
	
	private boolean create = true;
	private boolean follow = false;
//	private GameState currentState;
	
	public GWorld(float w, float h, AssetLoader a){
		if(w < size * 2)
			w = size * 2;
		if(h < size * 2)
			h = size * 2;
//		width = (int)w;
//		height = (int)h;
		width = 4096;
		height = 4096;
		dCreator = new DungeonCreater(width,height,size,a);
		createDungeon();
		System.out.println(dCreator);
		
	}
	
	public void update(float delta){
//		System.out.println(delta);
		if(create){
			
			
			create = false;
			System.out.println("Created");
		}
		

		if(player.getPos().x > currentXBounds+size ||player.getPos().x < currentXBounds){
			int dir = (player.getPos().x > currentXBounds+size? 1:-1);
			if(dCreator.isRoomValid(currentXBounds/size+1*dir,currentYBounds/size)){
			dCreator.getRooms(currentXBounds/size,currentYBounds/size).destroyArray();
			Room d = (Room)dCreator.getRooms(currentXBounds/size+1*dir,currentYBounds/size);
			d.updateRects();
//			d.changePosition(player.getPos().x, player.getPos().y);
			player.setRects(getRects(currentXBounds/size+1*dir,currentYBounds/size));
//			player.setRects(d.getHitBoxes());
			currentXBounds = currentXBounds+size*dir;
			}
		}
		if(player.getPos().y > currentYBounds+size ||player.getPos().y < currentYBounds){
			int dir = (player.getPos().y > currentYBounds+size? 1:-1);
			if(dCreator.isRoomValid(currentXBounds/size,currentYBounds/size+1*dir)){
			dCreator.getRooms(currentXBounds/size,currentYBounds/size).destroyArray();
			Room d = (Room)dCreator.getRooms(currentXBounds/size,currentYBounds/size+1*dir);
			d.updateRects();
//			d.changePosition(player.getPos().x, player.getPos().y);
			player.setRects(getRects(currentXBounds/size,currentYBounds/size+1*dir));
//			player.setRects(d.getHitBoxes());
			currentYBounds = currentYBounds+size*dir;
			}
		}
		
		Vector2 oldPos = player.getPos();
		System.out.println(oldPos);

		player.update(delta);
		System.out.println(player.getPos()+" 2");
		if(player.isPlayerMoving()){
			if(walkingtimer <= 0){
			AssetLoader.walking.play();
			System.out.println("play");
			walkingtimer = 170;
			}
			else
				walkingtimer --;
		}
		else{
			AssetLoader.walking.stop();
			walkingtimer=0;
		}
		
		updateArtis();
		updateHealth();
		
		updateEnemies(delta);
		
		updateBullets(delta);
		if(soundTimer <= 0){
			switch((int)(Math.random()*3)){
			case 0:
				AssetLoader.cave1.play();
				break;
			case 1:
				AssetLoader.cave2.play();
				break;
			case 2:
				AssetLoader.cave3.play();
				break;
			}
			soundTimer += (int)(Math.random()*1000+500);
		}
		else
			soundTimer--;
	}
	
	private void updateBullets(float delta) {
		if(bList.size()>0){
			for(int i = 0; i < bList.size(); i++){
				Bullet b = bList.get(i);
				if(b.getPos().x>=currentXBounds && b.getPos().x<currentXBounds+size && b.getPos().y >= currentYBounds && b.getPos().y < currentYBounds+size)
					b.update(delta);
				else
					b.setDest(true);
				if(b.getDest()){
					bList.remove(b);
					i--;
				}
			}
		}
	}

	private void updateEnemies(float delta) {
		if(eList.size()>0){
			for(int i = 0; i < eList.size(); i++){
				Enemy e = eList.get(i);
				if(e.getPlayer() == null)
					e.setPlayer(player);
				if(e.getPos().x>=currentXBounds && e.getPos().x<currentXBounds+size && e.getPos().y >= currentYBounds && e.getPos().y < currentYBounds+size)
					e.update(delta);
				if(Intersector.overlaps(player.getHitBox(),e.getHitBox()))
					e.setHealth(0);
				if(e.getHealth()<0){
					eList.remove(e);
					i--;
				}
			}
		}		
	}

	public void updateArtis(){
		for(int i = 0; i < aList.size(); i++){
			Artifact a = aList.get(i);
			if(Intersector.overlaps(a.getHitbox(), player.getHitBox())){
				if(!a.getActiviated()){
					System.out.println("ADeactivated");
					a.setActiviated(true);
					aList.remove(i);
					i--;
				}
			}
		}
	}
	
	public void updateHealth(){
		for(int i = 0; i < hList.size(); i++){
			Health a = hList.get(i);
			if(Intersector.overlaps(a.getHitbox(), player.getHitBox())){
				if(!a.getActiviated()){
					System.out.println("Deactivated");
					player.setHealth(1);
					a.setActiviated(true);
					hList.remove(i);
					i--;
				}
			}
		}
	}
	
	public ArrayList<Rectangle> getRects(int x, int y){
		ArrayList<Rectangle> rects = new ArrayList();
		ArrayList<Room> rooms = dCreator.getAdjacentRooms(x,y);
		for(Room r:rooms){
			r.updateRects();
//			r.changePosition(player.getPos().x, player.getPos().y);
			for(Rectangle rs : r.getHitBoxes())
				rects.add(rs);
		}
//		System.out.println("Rects: "+rects);
		return rects;
	}
	
	private void createDungeon(){
//		dCreator.createTest();
		eList = new ArrayList<Enemy>();
		bList = new ArrayList<Bullet>();
		dCreator.createDungeon2(goalNumb);
		aList = dCreator.getArti();
		hList = dCreator.getHealth();
		currentXBounds = dCreator.getStartX()*size;
		currentYBounds = dCreator.getStartY()*size;
		createPlayer();
//		eList.add(new Kamikaze(player.getPos().x,player.getPos().y));
	}
	
	private void createPlayer(){
		
			Room d = (Room)dCreator.getRooms(dCreator.getStartX(), dCreator.getStartY());

			if(player == null)
				player = new Player(size*dCreator.getStartX()+d.getOpenX().get(0), size*dCreator.getStartY()+d.getOpenY().get(0),this);
			else
				player.set(size*dCreator.getStartX()+d.getOpenX().get(0), size*dCreator.getStartY()+d.getOpenY().get(0));
			
		player.setRects(getRects(currentXBounds/size,currentYBounds/size));
		
	}
	
	public DungeonCreater getDungeon(){
		return dCreator;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getSize() {
		return size;
	}
	
	public Player getPlayer(){
		return player;
	}

	public int getCurrentXBounds() {
		return currentXBounds;
	}

	public int getCurrentYBounds() {
		return currentYBounds;
	}
	
	public void setCreator(){
		createDungeon();
		System.out.println("Restart");
	}
	
	public void setFollower(){
		follow = !follow;
	}

	public boolean getFollow() {
		// TODO Auto-generated method stub
		return follow;
	}
	
	public ArrayList<Artifact> getAList(){
		return aList;
	}
	
	public Artifact getArti(float x, float y){
		for(int i = 0; i < aList.size(); i++){
			if(aList.get(i).getX() == x && aList.get(i).getY() == y)
				return aList.get(i);
		}
		return null;
	}
	
	public Health gethealth(float x, float y){
		for(int i = 0; i < hList.size(); i++){
			if(hList.get(i).getX() == x && hList.get(i).getY() == y)
				return hList.get(i);
		}
		return null;
	}
	
	public ArrayList<Enemy> getEnemies(){
		return eList;
	}
	public ArrayList<Bullet> getBullet(){
		return bList;
	}
	
	public ArrayList<TextureRegion> getR(){
		ArrayList<TextureRegion> rs= new ArrayList<TextureRegion>();
		for(int i =0; i <= size/8;i++){
			rs.add(Math.random()*2 >= 1? AssetLoader.rock1:AssetLoader.rock2);
		}
		return rs;
	}
	
	public int getGoalNumb(){
		return goalNumb;
	}
}
