package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class ShopScreen implements Screen {
    

    private SpriteBatch sprite;
    private Texture bg;
    private Texture buttonTexture;
    private Texture buttonTextureHover;
    private CalicoGarden game;
    private OrthographicCamera camera;
    private BitmapFont font;
    private Plant[] plants;
    private Rectangle[] plantBounds;

    private Rectangle buttonBounds;
    private boolean isHovered = false;
    private ShelfSystem shelfSystem;

    public ShopScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
        this.font = new BitmapFont();
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        bg = new Texture("shop.png");

        // Initialize plants
        plants = new Plant[]{
            new Plant("Plant1", new Texture("plants/plant1.png"), 5),
            new Plant("Plant2", new Texture("plants/plant2.png"), 10),
            new Plant("Plant3", new Texture("plants/plant3.png"), 15),
            new Plant("Plant4", new Texture("plants/plant4.png"), 20),
            new Plant("Plant5", new Texture("plants/plant5.png"), 25),
        };

        // Initialize plant bounds
        plantBounds = new Rectangle[plants.length];
        for (int i = 0; i < plants.length; i++) {
            plantBounds[i] = new Rectangle(100 + i * 120, 300, 100, 100);
        }

        buttonTexture = new Texture("TestButtons/RedSquareButton.png");
        buttonTextureHover = new Texture("TestButtons/GreenSquareButton.png");
        buttonBounds = new Rectangle(150, 225, 200, 50); // Initialize button bounds

        // Initialize ShelfSystem with plants array
        shelfSystem = new ShelfSystem(plants);

        shelfSystem.loadState();
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

    if (Gdx.input.justTouched()) {
        for (int i = 0; i < plants.length; i++) {
            if (plantBounds[i].contains(cursorPos.x, cursorPos.y)) {
                if (game.getCoins() >= plants[i].getPrice()) {
                    game.setCoins(game.getCoins() - plants[i].getPrice());
                    game.addPlantToGameScreen(plants[i]);
                    System.out.println(plants[i].getName() + " bought! Remaining coins: " + game.getCoins());
                    return; // Exit render to prevent further drawing this frame
                } else {
                    System.out.println("Not enough coins for " + plants[i].getName() + "!");
                }
            }
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

    // Draw plants
    for (int i = 0; i < plants.length; i++) {
        sprite.draw(plants[i].getTexture(), plantBounds[i].x, plantBounds[i].y, plantBounds[i].width * 2f, plantBounds[i].height * 2f);
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
        shelfSystem.dispose(); // Dispose shelfSystem resources
    }
}


