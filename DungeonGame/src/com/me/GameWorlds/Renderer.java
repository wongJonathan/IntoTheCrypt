package com.me.GameWorlds;


import java.util.ArrayList;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.me.Data.AssetLoader;
import com.me.Engine.DungeonCreater;
import com.me.Engine.Enemy;
import com.me.GameObjects.Artifact;
import com.me.GameObjects.Bullet;
import com.me.GameObjects.Kamikaze;
import com.me.GameObjects.Player;
import com.me.GameObjects.QuickGhost;
import com.me.GameObjects.Room;
import com.me.GameObjects.TankyGhost;

public class Renderer {

	private GWorld gWorld;
	private World world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch, hud;
	private DungeonCreater dCreator;
	private Player player;
	private Room currentRoom;
	private float cameraScaleX, cameraScaleY;
	private Box2DDebugRenderer bRenderer;
	private PointLight lamp;
	private BodyDef rectDef;
	private Sprite playerS;

	private RayHandler handler; 
	
	private int size;
	private int width, height;
		
	private boolean follow = false;
	private boolean loadBackground = false;
	
	private BitmapFont font;
	
	private ArrayList<TextureRegion> trs;
	
	public Renderer(GWorld gWorld, World world){
		this.gWorld = gWorld;
		this.world = world;
		
		cameraScaleX = 1200;
		cameraScaleY = 800;
		cam = new OrthographicCamera();
		cam.setToOrtho(true, cameraScaleX, cameraScaleY);

		bRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		hud = new SpriteBatch();
		
		dCreator = gWorld.getDungeon();
		player = gWorld.getPlayer();
//		cam.position.set(player.getPos().x, 7f, 0f);
//		cam.update();
		width = gWorld.getWidth();
		height = gWorld.getHeight();
		size = gWorld.getSize();
		
		
		
		font = new BitmapFont();
		font.setColor(Color.RED);
		font.setScale(5);
		
//		RayHandler.useDiffuseLight(true);
		handler = new RayHandler(world);
		handler.setCombinedMatrix(cam.combined);
		handler.setAmbientLight(.1f);
		lamp =new PointLight(handler,500,Color.YELLOW, 150,0,0);
		
		rectDef= new BodyDef();
		rectDef.type = BodyType.StaticBody;
		
		playerS = AssetLoader.player;
		trs = gWorld.getR();
		
		createBox2ds();
		
		player.getCam(cam);
	}
	
	public void render(float delta, float runTime){

		
		cameraMovement();



		shapeRenderer.setProjectionMatrix(cam.combined);
		batch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		AssetLoader.ground.draw(batch);
//		batch.end();
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
//				case 2:
//					shapeRenderer.setColor(Color.YELLOW);
//					break;
				default:
					shapeRenderer.setColor(Color.DARK_GRAY);
//					break;
//				case 3:
//					shapeRenderer.setColor(Color.GREEN);
//					break;
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
							else{
								shapeRenderer.setColor(Color.BLACK);
							}
							break;
						case 0:
							shapeRenderer.setColor(Color.DARK_GRAY);
							break;
						case 2:
							if(gWorld.getArti(x1%size, y1%size) != null && !gWorld.getArti(x1%size,y1%size).getActiviated())
								shapeRenderer.setColor(Color.ORANGE);
							break;
						case 4:
							if(gWorld.gethealth(x1%size, y1%size) != null && !gWorld.gethealth(x1%size,y1%size).getActiviated())
								shapeRenderer.setColor(Color.RED);
							break;
						case 5:
							r.getBlocks()[x1%size][y1%size] = 0;
							gWorld.getEnemies().add(new Kamikaze(x1,y1));
							break;
						case 6:
							r.getBlocks()[x1%size][y1%size] = 0;
							gWorld.getEnemies().add(new QuickGhost(x1,y1));
							break;
						case 7:
							r.getBlocks()[x1%size][y1%size] = 0;
							gWorld.getEnemies().add(new TankyGhost(x1,y1));
							break;
							default: 
								shapeRenderer.setColor(Color.RED);
						}
//						
//							shapeRenderer.rect(x1, y1, r.getW(), r.getH());
//						
					}
				}
				}
			}
		}		
		ArrayList<Enemy> eList = gWorld.getEnemies();
		for(Enemy e:eList){
			if(e.getPos().x>gWorld.getCurrentXBounds() && e.getPos().x < gWorld.getCurrentXBounds()+size
					&& e.getPos().y > gWorld.getCurrentYBounds() && e.getPos().y < gWorld.getCurrentYBounds() + size){
			shapeRenderer.setColor(Color.GREEN);
			shapeRenderer.rect(e.getPos().x,e.getPos().y+10,(float)e.getHealth()/e.getMax()*10f,5);
			}
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
//		
//		shapeRenderer.circle(player.getCircle().x, player.getCircle().y, player.getCircle().radius);
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
		
		batch.begin();
		batch.enableBlending();
		
//		batch.draw(region, x, y, originX, originY, runTime, runTime, scaleX, scaleY, rotation)
		playerS.draw(batch);
		playerS.setPosition(player.getPos().x-9, player.getPos().y-playerS.getHeight()+10);
		playerS.setRotation((float)(Math.toDegrees(player.getDir())+Math.toDegrees(45+13)));
		
		batch.end();
		
//		lamp.setPosition(new Vector2(
//				convert(-(gWorld.getCurrentXBounds() +gWorld.getSize()/2)+(player.getPos().x)+Gdx.graphics.getWidth()/2),
//				convert(-(gWorld.getCurrentYBounds() + gWorld.getSize()/2)+(player.getPos().y)+Gdx.graphics.getHeight()/2)));
//		lamp.setPosition(new Vector2(cam.position.x-cam.viewportWidth/2+Gdx.input.getX(),cam.position.y-cam.viewportHeight/2+Gdx.input.getY()));
		lamp.setPosition(new Vector2(player.getCircle().x,player.getCircle().y));
		
		createSprites();
//		bRenderer.render(world,cam.combined);
		//Somehow gets the light to follow based on world position not camera
		handler.setCombinedMatrix(cam.combined, cam.position.x, cam.position.y, cam.viewportWidth, cam.viewportHeight);
		handler.updateAndRender();
		batch.end();
		
		hud.begin();
		Sprite hearts = AssetLoader.hearts;
		for(int i = 0; i< player.getHealth(); i++){
			hearts.draw(hud);
			hearts.setPosition(hearts.getWidth()*i+10, 10);
		}
		
		Sprite artiL = AssetLoader.artil;
		artiL.draw(hud);
		artiL.setPosition(Gdx.graphics.getWidth()-(100+artiL.getWidth()), 10);
		font.draw(hud,Math.abs(gWorld.getAList().size()-gWorld.getGoalNumb())+"/"+gWorld.getGoalNumb(),Gdx.graphics.getWidth()-100,100);
		hud.end();
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
	
	public void zoomOut(){
		cam.zoom += .02;
		cam.update();
	}
	
	public void zoomIn(){
		cam.zoom = -10;
	}
	
	public void disposeHandler(){
		handler.dispose();
	}
	
	private float convert(float x){
		return x*1f;
	}
	
	
	private void createSprites(){
		batch.begin();
		
		
		
//		for(int x = 0; x < gWorld.getWidth(); x+=size){
//			for(int y = 0; y < gWorld.getHeight(); y +=size){
		for(int x = gWorld.getCurrentXBounds()-size; x <  gWorld.getCurrentXBounds()+2*size; x+=size){
			for(int y = gWorld.getCurrentYBounds()-size; y <  gWorld.getCurrentYBounds()+2*size; y+=size){
				//Creates the rooms within the grids
//				if(x<0)
//					x = 0;
//				else if( x > gWorld.getWidth())
//					x = gWorld.getWidth();
				if(gWorld.getDungeon().isRoomValid(x/size, y/size)){
				Room r = (Room)dCreator.getRooms(x/size,y/size);
				int type = r.getType();
				
				if(type!= 1){
					//To give each block their own texture
					int regionCounter = 0;
					//Goes into each x and y value in square
				for(int x1 = x; x1 < x+size; x1 +=r.getW()){
					for(int y1 = y; y1 < y +size; y1+=r.getH()){
						//Creates the blocks within the room
						
						int type1 =r.getBlockType(x1%size,y1%size);
						TextureRegion rocks =trs.get(regionCounter);

						switch(type1){
						//if tpype1: 1 = wall, 0 = open, 2 = articfact;
						
						case 1: 
							if(type == 2){
								batch.draw(rocks,x1,y1);
								regionCounter++;
								}
							else if(type == 3){
								batch.draw(rocks,x1,y1);
								regionCounter++;
								}
							else{
								batch.draw(rocks,x1,y1);
								regionCounter++;
								}
							break;
						case 2:
							if(gWorld.getArti(x1%size, y1%size) != null && !gWorld.getArti(x1%size,y1%size).getActiviated()){
								Sprite arti = AssetLoader.arti;
								arti.draw(batch);
								arti.setPosition(x1,y1);
							}
//							break;
						case 4:
							if(gWorld.gethealth(x1%size, y1%size) != null && !gWorld.gethealth(x1%size,y1%size).getActiviated()){
								Sprite health = AssetLoader.health;
								health.draw(batch);
								health.setPosition(x1,y1);			
							}
								break;

						
						}
					}
				}
				}
				}
			}
		}		
		
		ArrayList<Enemy> eList = gWorld.getEnemies();
		for(Enemy e:eList){
//			if(e.getPos().x>gWorld.getCurrentXBounds() && e.getPos().x < gWorld.getCurrentXBounds()+size
//					&& e.getPos().y > gWorld.getCurrentYBounds() && e.getPos().y < gWorld.getCurrentYBounds() + size){
			switch(e.getType()){
			case 0:
				Sprite enemyR = AssetLoader.ghostReg;
				enemyR.draw(batch);
				enemyR.setPosition(e.getPos().x-e.getW(), e.getPos().y-e.getH());
				enemyR.setRotation((float)(Math.toDegrees(e.getDir()+Math.toRadians(180))));
				break;
			case 1:
				Sprite enemyF = AssetLoader.ghostFast;
				enemyF.draw(batch);
				enemyF.setPosition(e.getPos().x, e.getPos().y);
				enemyF.setRotation((float)(Math.toDegrees(e.getDir()+Math.toRadians(180))));

				break;
			case 2:
				Sprite enemyT = AssetLoader.ghostTank;
				enemyT.draw(batch);
				enemyT.setPosition(e.getPos().x-e.getW(), e.getPos().y-e.getH());
				enemyT.setRotation((float)(Math.toDegrees(e.getDir()+Math.toRadians(180))));

//			}
			}
		}
//		ArrayList<Bullet> bList = gWorld.getBullet();
//		for(Bullet b:bList){
//			shapeRenderer.setColor(Color.RED);
//			shapeRenderer.rect(b.getPos().x,b.getPos().y,3,3);
//
//		}
//		ArrayList<Room> rooms = dCreator.getAdjacentRooms(gWorld.getCurrentXBounds()/size, gWorld.getCurrentYBounds()/size);
//		for(Room d: rooms){
//			for (int x = 0; x < d.getHitBoxes().size(); x++) {
//				shapeRenderer.setColor(Color.ORANGE);
//				shapeRenderer.rect(d.getHitBoxes().get(x).x, d.getHitBoxes().get(x).y, d.getHitBoxes().get(x).width, d.getHitBoxes().get(x).height);
//			}
//		}
		
//		ArrayList<Artifact> aList = gWorld.getAList();
//		for(Artifact a: aList){
//			Sprite arti = AssetLoader.arti;
//			arti.draw(batch);
//			arti.setPosition(a.getHitbox().x, a.getHitbox().y);
//		}
		
		
//		Room d = (Room) dCreator.getRooms(gWorld.getCurrentXBounds()/size, gWorld.getCurrentYBounds()/size);
		
//		
//		Rectangle p = player.getHitBox();
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.rect(p.getX(),p.getY(),p.getWidth(),p.getHeight());
//		if(player != null){
//			shapeRenderer.setColor(Color.PINK);
//			shapeRenderer.rect(player.getPos().x, player.getPos().y, player.getW(), player.getH());
//		}
		
		
	}
	
	private void createBox2ds(){
		
		PolygonShape wall = new PolygonShape();

		
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
				
				if(type == 1){
					shapeRenderer.setColor(Color.RED);
					rectDef.position.set(x+size/2,y+size/2);
					
					Body rectBody = world.createBody(rectDef);
					
					wall.setAsBox(size/2, size/2);
					rectBody.createFixture(wall,3.0f);
				}
				
				if(type!= 1)
					//Goes into each x and y value in square
				for(int x1 = x; x1 < x+size; x1 +=r.getW()){
					for(int y1 = y; y1 < y +size; y1+=r.getH()){
						//Creates the blocks within the room
						
						int type1 =r.getBlockType(x1%size,y1%size);
						switch(type1){
						//if tpype1: 1 = wall, 0 = open, 2 = articfact;
						case 1: 
//							if(type == 2){
//							}
//							else if(type == 3){
//							}
//							if(type !=2 && type!=3){
								shapeRenderer.setColor(Color.RED);
								rectDef.position.set(x1+r.getW()/2,y1+r.getH()/2);
								
								Body rectBody = world.createBody(rectDef);
								
								wall.setAsBox(r.getW()/2, r.getH()/2);
								rectBody.createFixture(wall,3.0f);
//							}
							break;
						}
						
					}
				}
				}
			}
		}
		wall.dispose();
//
	}
	
	public OrthographicCamera getCam(){
		return cam;
	}
}
