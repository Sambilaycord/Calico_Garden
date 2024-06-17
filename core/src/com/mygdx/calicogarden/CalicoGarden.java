package com.mygdx.calicogarden;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CalicoGarden extends Game {
	private GameScreen gameScreen;
	private AccessoryMenu AccessoryMenu;
	private OrthographicCamera camera;
	private Texture selectedAccessory;
	public SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
        setScreen(new CatAnimation(batch, this));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1400, 800); // Set the viewport size to your game's resolution
		gameScreen = new GameScreen(this);
		AccessoryMenu = new AccessoryMenu(this);
	}

	public void showGameScreen() {
        setScreen(gameScreen);
    }

	public void showMenuScreen() {
		setScreen(AccessoryMenu);
	}

	public void setSelectedAccessory(Texture accessory) {
		this.selectedAccessory = accessory;
	}

	public Texture getSelectedAccessory() {
		return selectedAccessory;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
}
