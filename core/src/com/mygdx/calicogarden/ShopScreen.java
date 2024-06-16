package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ShopScreen implements Screen {

    private SpriteBatch sprite;
    private Texture bg;
    private CalicoGarden game;
    private OrthographicCamera camera;
    private BitmapFont font;    // Font for rendering text

    public ShopScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
        this.font = new BitmapFont(); // Initialize the font for text rendering
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        bg = new Texture("shop.png"); // Replace with your background texture path
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen with black color
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.showGameScreen();
        }
        sprite.begin();
        // Render your shop screen elements here
        sprite.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.draw(sprite, "Coins: " + game.getCoins(), 20, 40);
        sprite.end();
    }

    // Other overridden methods (resize, pause, resume, hide, dispose) can remain as is or with appropriate implementation

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
    }
}
