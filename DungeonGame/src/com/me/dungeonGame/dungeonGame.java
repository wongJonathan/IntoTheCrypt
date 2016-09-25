package com.me.dungeonGame;

import com.badlogic.gdx.Game;
import com.me.Sceens.GameScreen;
import com.me.Sceens.OpeningScreen;

public class dungeonGame extends Game{

	@Override
	public void create() {
		// TODO Auto-generated method stub
		setScreen(new OpeningScreen(this));
	}
}
