package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen implements Screen {
    private SpriteBatch sprite;
    private Texture bg;
    private Texture cat;
    private CalicoGarden game;
    private OrthographicCamera camera;

    public GameScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        bg = new Texture("bg.jpg");
        cat = new Texture("ming.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);

        camera.update();
        sprite.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.showMenuScreen();
        }

        sprite.begin();
        sprite.draw(bg, 0, 0);
        sprite.draw(cat, 0, 0);

        Texture accessory = game.getSelectedAccessory();
        if (accessory != null) {
            sprite.draw(accessory, 0, 0); // Adjust position to be on top of the cat
        }

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
        cat.dispose();
    }
}
