package com.me.Sceens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DeathScene implements Screen {

	private SpriteBatch batch;
	private Texture startScreen;
	private Game g;
	private ShapeRenderer sr;
	
	public DeathScene( Game g){
		startScreen = new Texture(Gdx.files.internal("data/Ending.png"));
		startScreen.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		this.g = g;
	}
	
	@Override
	public void render(float delta) {
		int midx = Gdx.graphics.getWidth()/2-455/2;
		int midy = Gdx.graphics.getHeight()/2-341/2;
		// TODO Auto-generated method stub
		 Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
         batch.begin();
         batch.draw(startScreen, midx, midy);
         batch.end();
         
         if(Gdx.input.justTouched()){
        		 g.setScreen(new OpeningScreen(g));
        		 System.out.println("Start");
        	 
        	 

         }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
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
