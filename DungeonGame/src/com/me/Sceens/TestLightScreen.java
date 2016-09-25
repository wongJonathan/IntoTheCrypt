package com.me.Sceens;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class TestLightScreen implements ApplicationListener{
	OrthographicCamera camera;
	float width, height;
	FPSLogger logger;
	
	final float meterToPixel = 32;
	
	World world;
	Box2DDebugRenderer renderer;
	RayHandler handler;
	
	Body circleBody;
	
	PointLight pointLight;

	@Override
	public void create() {
		// TODO Auto-generated method stub
		width = Gdx.graphics.getWidth()/5;
		height = Gdx.graphics.getHeight()/5;
		
		camera = new OrthographicCamera(width, height);
		camera.position.set(width *.05f, height *.5f,0);
		camera.update();
		
		world = new World(new Vector2(0,-9.8f),false);
		
		renderer = new Box2DDebugRenderer();
		
		logger = new FPSLogger();
		
		BodyDef circleDef = new BodyDef();
		circleDef.type = BodyType.DynamicBody;
		circleDef.position.set(0, 20);
		
		circleBody = world.createBody(circleDef);
		
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(3f);
//		circleShape.setPosition(new Vector2(5,5));
		
		FixtureDef circleFixture = new FixtureDef();
		circleFixture.shape = circleShape;
		circleFixture.density = .4f;
		circleFixture.friction = .2f;
		circleFixture.restitution = .8f;
		
		circleBody.createFixture(circleFixture);
		
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0,50);
		System.out.println(-Gdx.graphics.getWidth());
		System.out.println(groundBodyDef.position);
		
		Body groundBody = world.createBody(groundBodyDef);
		
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(.5f,.5f);
		
		groundBody.createFixture(groundBox, 3.0f);
		
		PolygonShape gb = new PolygonShape();
//		gb.setAsBox(.5f, .5f);
		gb.setAsBox(.5f, .5f, new Vector2(1,0), 0);
		groundBody.createFixture(gb, 3.0f);
		
		RayHandler.useDiffuseLight(true);
		
		handler = new RayHandler(world);
		handler.setCombinedMatrix(camera.combined);
		handler.setAmbientLight(0);
		pointLight = new PointLight(handler, 5000, Color.YELLOW, 1000, 50, 50);		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		pointLight.setPosition(Gdx.input.getX()/2, Gdx.input.getY()/2);
		renderer.render(world, camera.combined);
		handler.updateAndRender();
		world.step(1/60f, 6, 2);
		
		logger.log();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		world.dispose();
	}
	
}
