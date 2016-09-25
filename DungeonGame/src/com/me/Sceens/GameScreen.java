package com.me.Sceens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.me.Data.AssetLoader;
import com.me.Data.Inputs;
import com.me.GameWorlds.Box2dRender;
import com.me.GameWorlds.GWorld;
import com.me.GameWorlds.Renderer;

public class GameScreen implements Screen {
	
	private GWorld gworld;
	private World world;
	private Renderer renderer;
	private Box2dRender bRender;
	private float runTime;
	private FPSLogger logger;
	private AssetLoader a;	
	private Game g;
	
	public GameScreen(Game g){
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		a = new AssetLoader();
		int midpointY = (int)(screenHeight/2);
		gworld = new GWorld(screenWidth,screenHeight, a);
		world= new World(new Vector2(0,0), true);
		Gdx.input.setInputProcessor(new Inputs(gworld));
		renderer = new Renderer(gworld,world);
//		bRender = new Box2dRender(gworld, world);
		logger = new FPSLogger();
		this.g = g;
	}
	
	@Override
    public void render(float delta) {
		
		runTime += delta;
		gworld.update(delta);
		renderer.render(delta, runTime);
//		bRender.render(delta, runTime);
//		System.out.println(runTime);
		
		logger.log();
		if(gworld.getPlayer().getHealth() <= 0)
			g.setScreen(new DeathScene(g));        		

		if(gworld.getAList().size()<=0)
			g.setScreen(new CompleteScreen(g));
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("GameScreen - resizing");
    }

    @Override
    public void show() {
        System.out.println("GameScreen - show called");
    }

    @Override
    public void hide() {
        System.out.println("GameScreen - hide called");     
    }

    @Override
    public void pause() {
        System.out.println("GameScreen - pause called");        
    }

    @Override
    public void resume() {
        System.out.println("GameScreen - resume called");       
    }

    @Override
    public void dispose() {
       bRender.disposeHandler();
       AssetLoader.dispose();
    }
    
    public Renderer getRenderer(){
    	return renderer;
    }
}
