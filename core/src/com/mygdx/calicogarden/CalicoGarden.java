package com.mygdx.calicogarden;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public class CalicoGarden extends Game {
	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	private OrthographicCamera camera;
	private Texture selectedAccessory;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1400, 800); // Set the viewport size to your game's resolution
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		setScreen(gameScreen);
	}

	public void showGameScreen() {
		setScreen(gameScreen);
	}

	public void showMenuScreen() {
		setScreen(menuScreen);
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
