package com.mygdx.calicogarden;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

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

    private List<Plant> plants;

    private Sound sfx;
    private Music bgm1;
    private Music bgm2;

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
        plants = new ArrayList<>();

        bgm1 = Gdx.audio.newMusic(Gdx.files.internal("music/bgm3.mp3"));
        bgm2 = Gdx.audio.newMusic(Gdx.files.internal("music/bgm2.mp3"));
        bgm1.setLooping(true);
        bgm2.setLooping(true);

        music();
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

    public void showAccessoryMenu() {
        setScreen(accessoryMenu);
    }

    public void setSelectedAccessory(Texture accessory) {
        this.selectedAccessory = accessory;
    }

    public Texture getSelectedAccessory() {
        return selectedAccessory;
    }

    public void addPlantToGameScreen(Plant plant) {
        plants.add(plant);
        showGameScreen(); // Switch to GameScreen after adding the plant
    }

    public List<Plant> getPlants() {
        if (plants == null) {
            plants = new ArrayList<>();
        }
        return plants;
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

    public void music(){
        Screen currentScreen = getScreen();
        if (currentScreen instanceof CatAnimation) {
            bgm1.play();
        } else {
            bgm1.stop();
            bgm2.play();
        }
    }
}
