package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MenuScreen implements Screen {
    private SpriteBatch sprite;
    private Texture bg;
    private Texture cat_accessories;
    private CalicoGarden game;
    private Rectangle accessoryBounds;
    private OrthographicCamera camera;

    public MenuScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        bg = new Texture("bg2.jfif");
        cat_accessories = new Texture("cat_loading.png");
        accessoryBounds = new Rectangle(0, 0, cat_accessories.getWidth(), cat_accessories.getHeight()); // Adjust position if necessary
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen with black color

        camera.update();
        sprite.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.showGameScreen();
        }

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (accessoryBounds.contains(touchPos.x, touchPos.y)) {
                game.setSelectedAccessory(cat_accessories);
                game.showGameScreen();
            }
        }

        sprite.begin();
        sprite.draw(bg, 0, 0);
        sprite.draw(cat_accessories, 0, 0); // Adjust position if necessary
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
        cat_accessories.dispose();
    }
}
