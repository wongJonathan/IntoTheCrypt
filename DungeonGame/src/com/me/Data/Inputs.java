package com.me.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.me.GameObjects.Bullet;
import com.me.GameObjects.Kamikaze;
import com.me.GameObjects.Player;
import com.me.GameWorlds.GWorld;

public class Inputs implements InputProcessor{
	
	private Player player;
	private GWorld world;

	public Inputs(GWorld w){
		System.out.println("Input up");
		world = w;
		player = w.getPlayer();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		int dir = 0;
		if(keycode == Keys.W){
//			System.out.println("Up");
			player.getInputD(dir);
		}
		dir++;
		if(keycode == Keys.D){
//			System.out.println("Right");
			player.getInputD(dir);
		}
		dir++;
		if(keycode == Keys.S){
//			System.out.println("Down");
			player.getInputD(dir);
		}
		dir++;
		if(keycode == Keys.A){
			player.getInputD(dir);
//			System.out.println("Left");
		}
		
		if(keycode == Keys.R){
			if(world.getAList().size()==0)
			world.setCreator();
		}

		if(keycode == Keys.F){
			world.setFollower();
			System.out.println("Player X: " + player.getPos().x+" Player Y: "+player.getPos().y);
		}
		
		if(keycode == Keys.E){
			Kamikaze x = new Kamikaze(player.getPos().x,player.getPos().y);
			world.getEnemies().add(x);
//			System.out.println("Numb of enemies: "+world.getEnemies().size());
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		int dir = 0;
		if(keycode == Keys.W){
//			System.out.println("Up");
			player.getInputU(dir);
		}
		dir++;
		if(keycode == Keys.D){
//			System.out.println("Right");
			player.getInputU(dir);
		}
		dir++;
		if(keycode == Keys.S){
//			System.out.println("Down");
			player.getInputU(dir);
		}
		dir++;
		if(keycode == Keys.A){
			player.getInputU(dir);
//			System.out.println("Left");
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println(Gdx.input.getX()+", "+Gdx.input.getY());								
		
		//If camera is following Bounds
//		world.getBullet().add(new Bullet(player.getPos().x,player.getPos().y, 
//				(world.getCurrentXBounds() +world.getSize()/2)-(Gdx.graphics.getWidth()/2-Gdx.input.getX()),//Because mouse input is based on camera's box the corner of the screen will give mousex and mousey =0
//				(world.getCurrentYBounds() + world.getSize()/2)-(Gdx.graphics.getHeight()/2-Gdx.input.getY())));//By adding half the screen size and using that to subtract the players position should give accurate results
		
		//If camrea is following player
//		world.getBullet().add(new Bullet(player.getPos().x,player.getPos().y, 
//				(player.getPos().x )-(Gdx.graphics.getWidth()/2-Gdx.input.getX()),
//				(player.getPos().y )-(Gdx.graphics.getHeight()/2-Gdx.input.getY())));
		
		return false;
	}
	
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
