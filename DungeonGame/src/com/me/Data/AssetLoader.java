package com.me.Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

	public static Texture GroundTileSet, pt, grt, grf, grta, backGround, healthT, artiT, heart, artiTL;
	public static Sprite player, ghostReg, ghostFast, ghostTank, ground, health, arti, hearts, artil;
	public static TextureRegion rock1, rock2;
	public static Sound walking, cave1, cave2, cave3;
	
	public AssetLoader(){
		load();
	}
	
	public static void load(){
		GroundTileSet = new Texture(Gdx.files.internal("data/GroundTileSet.png"));
		GroundTileSet.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		backGround = new Texture(Gdx.files.internal("data/DirtBlock.png"));
		backGround.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		backGround.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		pt = new Texture(Gdx.files.internal("data/ExplorerSkinned.png"));
		pt.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		player = new Sprite(pt);
		player.flip(false, true);
		player.setOrigin(10,58);
//		player.flip(false, true);
		
		rock1 = new TextureRegion(GroundTileSet,64,0,16,16);
		rock2= new TextureRegion(GroundTileSet,80,0,16,16);
		
		//Can not get from world so must manually change these
		ground = new Sprite(backGround,0,0,4096,4096);

		
		grt = new Texture(Gdx.files.internal("data/GhostReg.png"));
		ghostReg = new Sprite(grt);
		ghostReg.flip(false,true);
		ghostReg.setOrigin(9,8);
		
		grf = new Texture(Gdx.files.internal("data/GhostFast.png"));
		ghostFast = new Sprite(grf);
		ghostFast.flip(false,true);
		ghostFast.setOrigin(4,4);
		
		grta = new Texture(Gdx.files.internal(("data/GhostTank.png")));
		ghostTank = new Sprite(grta);
		ghostTank.flip(false,true);
		ghostTank.setOrigin(11,14);
		
		healthT = new Texture(Gdx.files.internal(("data/HealthPack.png")));
		health = new Sprite(healthT);
		
		artiT = new Texture(Gdx.files.internal("data/Artifact.png"));
		arti = new Sprite(artiT);
		arti.flip(false, true);
		
		heart = new Texture(Gdx.files.internal("data/Heart.png"));
		hearts = new Sprite(heart);
//		hearts.flip(false, true);
		
		artiTL = new Texture(Gdx.files.internal("data/ArtifactEnlarge.png"));
		artil = new Sprite(artiTL);
//		artil.flip(false, true);
		
		walking = Gdx.audio.newSound(Gdx.files.internal("data/walk.mp3"));
		cave1 = Gdx.audio.newSound(Gdx.files.internal("data/cave1.mp3"));
		cave2 = Gdx.audio.newSound(Gdx.files.internal("data/cave2.wav"));
		cave3 = Gdx.audio.newSound(Gdx.files.internal("data/cave3.mp3"));
		
	}
	
	public static void dispose(){
		GroundTileSet.dispose();
		pt.dispose();
	}
}
