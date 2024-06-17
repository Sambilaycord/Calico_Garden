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
import com.badlogic.gdx.utils.ScreenUtils;

import java.text.DecimalFormat;

public class GameScreen implements Screen {

    private SpriteBatch sprite;
    private Texture bg;
    private Sprite cat;
    private CalicoGarden game;
    private OrthographicCamera camera;
    private ShelfSystem shelfSystem;
    private PlantGrowthSystem plantGrowthSystem;
    private Texture potTexture;
    private Texture snapTexture;
    private BitmapFont font;
    private float timer = 0;
    private DecimalFormat decimalFormat;

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

        potTexture = new Texture("Pots/pot.png");
        snapTexture = new Texture("Pots/potSnap.png");
        float[][] lockPositions = {
                {50f, 450f, 880f}, // Y: 50, Left X: 100, Right X: 300
                {250f, 350f, 1150f}, // Y: 250, Left X: 50, Right X: 450
                {460f, 450f, 1100f}, // Y: 460, Left X: 0, Right X: (screen width - pot width)
                {625f, 650f, 1050f} // Y: 625, Left X: 200, Right X: screen width / 2
        };

        shelfSystem = new ShelfSystem(potTexture, snapTexture, lockPositions);
        plantGrowthSystem = new PlantGrowthSystem();

        // Set the pot position if it has been saved previously
        if (game.getPotX() != -1 && game.getPotY() != -1) {
            shelfSystem.setPotX(game.getPotX());
            shelfSystem.setPotY(game.getPotY());
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        sprite.setProjectionMatrix(camera.combined);
        // Handle input
        handleInput();

        // Update and draw other game components
        shelfSystem.update(delta, Gdx.input.getX(), Gdx.input.getY());

        sprite.begin();
        sprite.draw(bg, 0, 0);
        cat.setSize(Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 1.4f);
        cat.setPosition(-110,-110);
        cat.draw(sprite);

        Texture accessory = game.getSelectedAccessory();
        if (accessory != null) {
            Sprite accessorySprite = new Sprite(accessory);
            accessorySprite.setSize(Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 1.4f);
            accessorySprite.setPosition(-110,-110); // Adjust position to be on top of the cat
            accessorySprite.draw(sprite);
        }
        shelfSystem.draw(sprite);
        sprite.end();

        // Update the game timer
        timer += delta;
        plantGrowthSystem.update(delta);

        sprite.begin();
        // Format the timer to display in 24-hour format
        int hours = (int) (timer / 3600);
        int minutes = (int) ((timer % 3600) / 60);
        String formattedTime = decimalFormat.format(hours) + ":" + decimalFormat.format(minutes);
        font.draw(sprite, "Time: " + formattedTime, 100, 500);

        // Draw other UI elements
        font.draw(sprite, "Plant Growth Stage: " + plantGrowthSystem.getGrowthStage(), 100, 460);
        if (plantGrowthSystem.isFullyGrown()) {
            font.draw(sprite, "Plant is fully grown!", 100, 420);
        }
        sprite.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.setPotPosition(shelfSystem.getPotX(), shelfSystem.getPotY());
            game.showMenuScreen();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            plantGrowthSystem.waterPlant();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
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
        snapTexture.dispose();
        font.dispose();
    }
}
