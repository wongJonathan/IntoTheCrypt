package com.me.Sceens;

import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

public class AnotherLightTest implements  ApplicationListener {

	OrthographicCamera camera;

	RayHandler rayhandler;
	World world;
	
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		rayhandler = new RayHandler(world);
		rayhandler.setCombinedMatrix(camera.combined);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
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
		
	}

}
