package com.me.GameWorlds;

import java.util.ArrayList;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.me.Engine.DungeonCreater;
import com.me.Engine.Enemy;
import com.me.GameObjects.Bullet;
import com.me.GameObjects.Kamikaze;
import com.me.GameObjects.Player;
import com.me.GameObjects.Room;

public class Box2dRender {

	
	private GWorld gWorld;
	private World world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private DungeonCreater dCreator;
	private Player player;
	private Room currentRoom;
	private float cameraScaleX, cameraScaleY;
	private Box2DDebugRenderer bRenderer;
	private PointLight lamp;
	private BodyDef rectDef;

	private RayHandler handler; 
	
	private int size;
	private int width, height;
		
	private boolean follow = false;
	
	private BitmapFont font;
	
	
	public Box2dRender(GWorld gWorld, World world){
		this.gWorld = gWorld;
		this.world = world;
		
		cameraScaleX = 1200;
		cameraScaleY = 800;
		cam = new OrthographicCamera();
		cam.setToOrtho(true, cameraScaleX, cameraScaleY);

		bRenderer = new Box2DDebugRenderer();
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		dCreator = gWorld.getDungeon();
		player = gWorld.getPlayer();
//		cam.position.set(player.getPos().x, 7f, 0f);
//		cam.update();
		width = gWorld.getWidth();
		height = gWorld.getHeight();
		size = gWorld.getSize();
		
		batch = new SpriteBatch();
		
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		handler = new RayHandler(world);
		handler.setCombinedMatrix(cam.combined);
		lamp =new PointLight(handler,1000,Color.YELLOW, 1000,gWorld.getPlayer().getPos().x,gWorld.getPlayer().getPos().y);
		
		rectDef = new BodyDef();
		
	}
	
	public void render(float delta, float runTime){

		
		cameraMovement();



		shapeRenderer.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.begin(ShapeType.Filled);
		
		//Block type 1: wall; rest open rooms\
		// x and y value are made to show 9X9 view to allow you to see the next room

		for(int x = 0; x < gWorld.getWidth(); x+=size){
			for(int y = 0; y < gWorld.getHeight(); y +=size){
//		for(int x = gWorld.getCurrentXBounds()-size; x <  gWorld.getCurrentXBounds()+2*size; x+=size){
//			for(int y = gWorld.getCurrentYBounds()-size; y <  gWorld.getCurrentYBounds()+2*size; y+=size){
				//Creates the rooms within the grids
				if(x<0)
					x = 0;
				else if( x > gWorld.getWidth())
					x = gWorld.getWidth();
				if(gWorld.getDungeon().isRoomValid(x/size, y/size)){
				Room r = (Room)dCreator.getRooms(x/size,y/size);
				int type = r.getType();
				switch(type){
				case 1: //Solid block
					shapeRenderer.setColor(Color.BLACK);
					break;
				case 2:
					shapeRenderer.setColor(Color.YELLOW);
					break;
				case 0:
					shapeRenderer.setColor(Color.WHITE);
					break;
				case 3:
					shapeRenderer.setColor(Color.GREEN);
					break;
				}
				shapeRenderer.rect(x, y, size, size);
				
				if(type!= 1)
					//Goes into each x and y value in square
				for(int x1 = x; x1 < x+size; x1 +=r.getW()){
					for(int y1 = y; y1 < y +size; y1+=r.getH()){
						//Creates the blocks within the room
						
						int type1 =r.getBlockType(x1%size,y1%size);
						switch(type1){
						//if tpype1: 1 = wall, 0 = open, 2 = articfact;
						case 1: 
							if(type == 2){
								shapeRenderer.setColor(Color.BLUE);
							}
							else if(type == 3){
								shapeRenderer.setColor(Color.GREEN);
							}
							else
								shapeRenderer.setColor(Color.CYAN);
							break;
						case 0:
							shapeRenderer.setColor(Color.WHITE);
							break;
						case 2:
							if(gWorld.getArti(x1%size, y1%size) != null && !gWorld.getArti(x1%size,y1%size).getActiviated())
								shapeRenderer.setColor(Color.ORANGE);
							break;
						case 3:
							r.getBlocks()[x1%size][y1%size] = 0;
							gWorld.getEnemies().add(new Kamikaze(x1,y1));
							default: 
								shapeRenderer.setColor(Color.RED);
						}
						shapeRenderer.rect(x1, y1, r.getW(), r.getH());
						
					}
				}
				}
			}
		}		
		ArrayList<Enemy> eList = gWorld.getEnemies();
		for(Enemy e:eList){
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(e.getPos().x,e.getPos().y,e.getW(),e.getH());
//			System.out.println("X: "+e.getX()+" Y: "+e.getY());
		}
		
		ArrayList<Bullet> bList = gWorld.getBullet();
		for(Bullet b:bList){
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(b.getPos().x,b.getPos().y,3,3);

		}
//		ArrayList<Room> rooms = dCreator.getAdjacentRooms(gWorld.getCurrentXBounds()/size, gWorld.getCurrentYBounds()/size);
//		for(Room d: rooms){
//			for (int x = 0; x < d.getHitBoxes().size(); x++) {
//				shapeRenderer.setColor(Color.ORANGE);
//				shapeRenderer.rect(d.getHitBoxes().get(x).x, d.getHitBoxes().get(x).y, d.getHitBoxes().get(x).width, d.getHitBoxes().get(x).height);
//			}
//		}
		
//		ArrayList<Artifact> aList = gWorld.getAList();
//		for(Artifact a: aList){
//			shapeRenderer.setColor(Color.ORANGE);
//			shapeRenderer.rect(a.getHitbox().getX(),a.getHitbox().getY(),8,8);
//		}
		
		
//		Room d = (Room) dCreator.getRooms(gWorld.getCurrentXBounds()/size, gWorld.getCurrentYBounds()/size);
		
		
		Rectangle p = player.getHitBox();
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(p.getX(),p.getY(),p.getWidth(),p.getHeight());
		if(player != null){
			shapeRenderer.setColor(Color.PINK);
			shapeRenderer.rect(player.getPos().x, player.getPos().y, player.getW(), player.getH());
		}

		
		shapeRenderer.end();
	

		shapeRenderer.begin(ShapeType.Line);
		for(Bullet b:bList){
		shapeRenderer.line(b.getPos().x, b.getPos().y, b.getTx(), b.getTy());
		}
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		for(int x = 0; x < width+size; x+=size){
			shapeRenderer.line(x, height, x, 0);
		}
		for(int y = 0; y < height+size; y+=size){
			shapeRenderer.line(width,y,0,y);
		}
		
		shapeRenderer.end();
		
		lamp.setPosition(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
		
		bRenderer.render(world,cam.combined);
		handler.updateAndRender();
	}
	
	
	public void cameraMovement(){
		follow = gWorld.getFollow();
		
		if(follow == false){
			Room d = (Room) dCreator.getRooms(gWorld.getCurrentXBounds()/size, gWorld.getCurrentYBounds()/size);
			cam.position.set(d.getX()+gWorld.getSize()/2, d.getY()+gWorld.getSize()/2, 0f);
		}
		else
			cam.position.set(player.getPos().x, player.getPos().y, 0f);
		cam.update();
	}
	public void disposeHandler(){
		handler.dispose();
	}
}
