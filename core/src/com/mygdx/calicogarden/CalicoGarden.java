package com.mygdx.calicogarden;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CalicoGarden extends Game {
	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	private ShopScreen shopScreen;
	private OrthographicCamera camera;
	private Texture selectedAccessory;
	public SpriteBatch batch;
    private int coins;   	 // Coin counter variables
    private BitmapFont font; // For displaying text

	@Override
	public void create() {
		batch = new SpriteBatch();
        font = new BitmapFont(); // Initialize the font for text rendering
        setScreen(new CatAnimation(batch, this));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1400, 800); // Set the viewport size to your game's resolution
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		shopScreen = new ShopScreen(this);
        coins = 30; // Initialize coins
	}

    // Method to increase coins
    public void addCoins(int amount) {
        coins += amount;
    }
    
    // Method to decrease coins
    public void subtractCoins(int amount) {
        coins -= amount;
        if (coins < 0) {
            coins = 0; // Ensure coins don't go negative
        }
    }
    
    // Method to get current coins
    public int getCoins() {
        return coins;
    }

	public void showGameScreen() {
        setScreen(gameScreen);
    }


	public void showShopScreen() {
        setScreen(shopScreen);
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
