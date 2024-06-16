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

public class GameScreen implements Screen {

    private SpriteBatch sprite;
    private Texture bg;
    private Sprite cat;
    private CalicoGarden game;
    private OrthographicCamera camera;
    private ShelfSystem shelfSystem;
    private Texture potTexture;
    private BitmapFont font;    // Font for rendering text
    
    public GameScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
        this.font = new BitmapFont(); // Initialize the font for text rendering
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        bg = new Texture("GameScreen/GameScreenBackground.png");
        cat = new Sprite(new Texture("Cat.png"));
        potTexture = new Texture("Pots/pot.png");
        shelfSystem = new ShelfSystem(potTexture, 0, 0);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 0, 0, 1);

        camera.update();
        sprite.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            game.showMenuScreen();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            game.showShopScreen();
        }

        // Check for click on the pot and update dragging state accordingly
        if (shelfSystem.isPotClicked(Gdx.input.getX(), Gdx.input.getY())) {
            shelfSystem.setDragging(false); // Stop dragging on click

            float potX = Gdx.input.getX() - shelfSystem.getPotTexture().getWidth() / 4f;
            float potY = Gdx.input.getY() - shelfSystem.getPotTexture().getHeight() / 4f;
            
            // Clamp pot position to stay within screen bounds
            potX = Math.min(potX, Gdx.graphics.getWidth() - shelfSystem.getPotTexture().getWidth());
            potX = Math.max(potX, 0);
            potY = Math.min(potY, Gdx.graphics.getHeight() - shelfSystem.getPotTexture().getHeight());
            potY = Math.max(potY, 0);
            
            shelfSystem.setPotX(potX);
            shelfSystem.setPotY(potY);
        }

        shelfSystem.update(delta, Gdx.input.getX(), Gdx.input.getY());

        sprite.begin();

        sprite.draw(bg, 0, 0);
        cat.setSize(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f);
        cat.draw(sprite);

        Texture accessory = game.getSelectedAccessory();
        if (accessory != null) {
            sprite.draw(accessory, 0, 0); // Adjust position to be on top of the cat
        }


        sprite.draw(shelfSystem.getPotTexture(), shelfSystem.getPotX(), shelfSystem.getPotY(), shelfSystem.getPotTexture().getWidth() / 4f, shelfSystem.getPotTexture().getHeight() / 4f);
        // Display coins at bottom left corner
        font.draw(sprite, "Coins: " + game.getCoins(), 20, 40);

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
    }
}
