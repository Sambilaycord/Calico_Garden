package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private SpriteBatch sprite;
    private Texture bg;
    private Sprite cat;
    private CalicoGarden game;
    private OrthographicCamera camera;
    private ShelfSystem shelfSystem;
    private Texture potTexture;
    private Texture snapTexture;
    private Texture potTexture2;

    public GameScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        bg = new Texture("GameScreen/GameScreenBackground.png");
        cat = new Sprite(new Texture("Cat.png"));

        potTexture = new Texture("Pots/pot.png");
        potTexture2 = new Texture("Pots/Pot2.png");
        snapTexture = new Texture("Pots/potSnap.png");

        float[][] lockPositions = {
            {50f, 450f, 880f}, // Y: 50, Left X: 100, Right X: 300
            {250f, 350f, 1150f}, // Y: 250, Left X: 50, Right X: 450
            {460f, 450f, 1100f}, // Y: 460, Left X: 0, Right X: (screen width - pot width)
            {625f, 650f, 1050f} // Y: 625, Left X: 200, Right X: screen width / 2
        };
        
        shelfSystem = new ShelfSystem(potTexture, potTexture2, snapTexture, lockPositions);
        
        // Set the pot position if it has been saved previously
        if (game.getPotX() != -1 && game.getPotY() != -1) {
            shelfSystem.setPotX(game.getPotX());
            shelfSystem.setPotY(game.getPotY());
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);

        camera.update();
        sprite.setProjectionMatrix(camera.combined);

        boolean isLeftClick = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        shelfSystem.update(delta, Gdx.input.getX(), Gdx.input.getY(), isLeftClick);
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            // Save the pot position before switching to the menu
            game.setPotPosition(shelfSystem.getPotX(), shelfSystem.getPotY());
            game.showMenuScreen();
        }

        sprite.begin();

        sprite.draw(bg, 0, 0);
        cat.setSize(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f);
        cat.draw(sprite);

        Texture accessory = game.getSelectedAccessory();
        if (accessory != null) {
            sprite.draw(accessory, 0, 0); // Adjust position to be on top of the cat
        }

        shelfSystem.draw(sprite);

        sprite.end();
    }

    @Override
    public void resize(int width, int height) {
        // Handle resize
    }

    @Override
    public void pause() {
        // Handle pause
    }

    @Override
    public void resume() {
        // Handle resume
    }

    @Override
    public void hide() {
        // Handle hide
    }

    @Override
    public void dispose() {
        sprite.dispose();
        bg.dispose();
        cat.getTexture().dispose();
        potTexture.dispose();
        potTexture2.dispose();
        snapTexture.dispose();
    }
}
