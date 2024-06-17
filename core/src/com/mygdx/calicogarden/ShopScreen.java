package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ShopScreen implements Screen {

    private SpriteBatch sprite;
    private Texture bg;
    private Texture buttonTexture;
    private Texture buttonTextureHover;
    private CalicoGarden game;
    private OrthographicCamera camera;
    private BitmapFont font;

    private Rectangle buttonBounds;
    private boolean isHovered = false;

    public ShopScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
        this.font = new BitmapFont();
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        bg = new Texture("shop.png");
        buttonTexture = new Texture("TestButtons/RedSquareButton.png");
        buttonTextureHover = new Texture("TestButtons/GreenSquareButton.png");

        // Initialize button bounds
        buttonBounds = new Rectangle(150, 225, 200, 50);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.showGameScreen();
        }

        // Check if cursor is over the button
        Vector3 cursorPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(cursorPos);
        isHovered = buttonBounds.contains(cursorPos.x, cursorPos.y);

        if (Gdx.input.justTouched() && isHovered) {
            if (game.getCoins() >= 3) {
                game.setCoins(game.getCoins() - 3);
                System.out.println("Plant bought! Remaining coins: " + game.getCoins());
                // Add your logic to give the player the plant
            } else {
                System.out.println("Not enough coins!");
            }
        }

        sprite.begin();
        sprite.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.draw(sprite, "Coins: " + game.getCoins(), 20, 40);

        if (isHovered) {
            sprite.draw(buttonTextureHover, buttonBounds.x, buttonBounds.y, buttonBounds.width, buttonBounds.height);
        } else {
            sprite.draw(buttonTexture, buttonBounds.x, buttonBounds.y, buttonBounds.width, buttonBounds.height);
        }

        sprite.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
        buttonTexture.dispose();
        buttonTextureHover.dispose();
        font.dispose();
    }
}
