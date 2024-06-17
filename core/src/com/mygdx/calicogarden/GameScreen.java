package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


import java.text.DecimalFormat;

public class GameScreen implements Screen {

    public static final float SPEED = 10;

    private SpriteBatch sprite;
    private Texture bg;
    private Sprite cat;
    private Sprite shopLogo;
    private Sprite accessoryLogo;
    private Sprite sellLogo;
    private CalicoGarden game;
    private OrthographicCamera camera;
    private ShelfSystem shelfSystem;
    private PlantGrowthSystem plantGrowthSystem;
    private Texture potTexture;
    private BitmapFont font;
    private float timer = 0;
    private int day = 1; // Add a day variable
    private DecimalFormat decimalFormat;
    private Rectangle accessoryLogoBounds;
    private Rectangle shopLogoBounds;
    private Rectangle sellLogoBounds;

    private Array<Plant> plantsToAdd;

    public GameScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
        font = new BitmapFont();
        timer = 0;
        decimalFormat = new DecimalFormat("00"); // Format for two-digit numbers
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        bg = new Texture("GameScreen/GameScreenBackground.png");
        cat = new Sprite(new Texture("ming.png"));
        accessoryLogo = new Sprite(new Texture("accessory_icon.png"));
        shopLogo = new Sprite(new Texture("shop_icon.png"));
        sellLogo = new Sprite(new Texture("sell_icon.png"));
        potTexture = new Texture("Pots/pot.png");


        accessoryLogoBounds = new Rectangle(0, 450, accessoryLogo.getWidth(), accessoryLogo.getHeight());
        shopLogoBounds  = new Rectangle(0, 600, shopLogo.getWidth(), shopLogo.getHeight());
        sellLogoBounds  = new Rectangle(0, 250, sellLogo.getWidth(), sellLogo.getHeight());

        plantGrowthSystem = new PlantGrowthSystem();

        game.music();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shopLogo.setPosition(0, 600);
        accessoryLogo.setPosition(0, 450);
        sellLogo.setPosition(0,250);

        timer += delta * 360; // 360 in-game seconds per real second
        int inGameSeconds = (int) timer;

        if (inGameSeconds >= 86400) { // 86400 in-game seconds in 24 hours
            day++;
            timer -= 86400; // Reset the timer, keep the excess time
            inGameSeconds = (int) timer;
        }

        int hours = (inGameSeconds / 3600) % 24;
        int minutes = (inGameSeconds % 3600) / 60;
        String formattedTime = decimalFormat.format(hours) + ":" + decimalFormat.format(minutes);
        handleInput();

        sprite.begin();
        sprite.draw(bg, 0, 0);
        cat.setPosition(-110,-110);
        cat.draw(sprite);
        shopLogo.draw(sprite);
        accessoryLogo.draw(sprite);
        sellLogo.draw(sprite);

        
        shelfSystem.draw(sprite);

        accessory();
        camera.update();

        sprite.setProjectionMatrix(camera.combined);
        font.draw(sprite, "Day: " + day, 100, 540); // Display the current day
        font.draw(sprite, "Time: " + formattedTime, 100, 500);
        // Draw other UI elements
        font.draw(sprite, "Plant Growth Stage: " + plantGrowthSystem.getGrowthStage(), 100, 460);
        if (plantGrowthSystem.isFullyGrown()) {
            font.draw(sprite, "Plant is fully grown!", 100, 420);
        }

        for (Plant plant : plantsToAdd) {
            sprite.draw(plant.getTexture(), 650f, 50f, plant.getWidth(), plant.getHeight());
        }
        plantsToAdd.clear();

        sprite.end();
    }

    public void addPlantToGameScreen(Plant plant) {
        plantsToAdd.add(plant);
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (accessoryLogoBounds.contains(touchPos.x, touchPos.y)) {
                game.showAccessoryMenu();
            } else if (shopLogoBounds.contains(touchPos.x, touchPos.y)) {
                game.showShopScreen();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.showAccessoryMenu();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            plantGrowthSystem.waterPlant();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.showShopScreen();
        }
    }

    public void accessory() {
        Texture accessory = game.getSelectedAccessory();
        if (accessory != null) {
            Sprite accessorySprite = new Sprite(accessory);
            accessorySprite.setSize(Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 1.4f);
            accessorySprite.setPosition(-110,-110); // Adjust position to be on top of the cat
            accessorySprite.draw(sprite);
        }
    }

    public void timer() {
        // Not implemented
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        cat.setSize(Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 1.4f);
    }

  

    @Override
    public void pause() {
        // Not implemented
    }

    @Override
    public void resume() {
        // Not implemented
    }

    @Override
    public void hide() {
        // Not implemented
    }

    @Override
    public void dispose() {
        sprite.dispose();
        bg.dispose();
        cat.getTexture().dispose();
        potTexture.dispose();
        font.dispose();
    }
}
