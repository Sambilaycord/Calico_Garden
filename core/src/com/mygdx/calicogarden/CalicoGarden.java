package com.mygdx.calicogarden;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CalicoGarden extends Game {
    private GameScreen gameScreen;
    private ShopScreen shopScreen;
    private AccessoryMenu accessoryMenu;
    private OrthographicCamera camera;
    private Texture selectedAccessory;
    public SpriteBatch batch;
    private int coins;    // Coin counter variable
    private BitmapFont font; // For displaying text
    private float potX = -1; // Initialize with an invalid position
    private float potY = -1;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // Initialize the font for text rendering
        setScreen(new CatAnimation(batch, this));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1400, 800); // Set the viewport size to your game's resolution
        gameScreen = new GameScreen(this);
        accessoryMenu = new AccessoryMenu(this);
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

    public void setCoins(int coins) {
        this.coins = coins;
        accessoryMenu = new AccessoryMenu(this);
    }

    public void showGameScreen() {
        setScreen(gameScreen);
    }

    public void showShopScreen() {
        setScreen(shopScreen);
    }

    public void showMenuScreen() {
        setScreen(accessoryMenu);
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

    public void setPotPosition(float x, float y) {
        this.potX = x;
        this.potY = y;
    }

    public float getPotX() {
        return potX;
    }

    public float getPotY() {
        return potY;
    }
}
