package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class ShopScreen implements Screen {

    private SpriteBatch sprite;
    private Texture bg;
    private CalicoGarden game;
    private OrthographicCamera camera;
    private BitmapFont font;
    private Plant[] plants;
    private Rectangle[] plantBounds;

    private boolean isHovered = false;
    private ShelfSystem shelfSystem;
    private Sprite exitButton;
    private Rectangle exitButtonBounds;
    private Sound buySFX;
    private Plant[] plantsDisplay;

    public ShopScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
        this.font = new BitmapFont();
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        buySFX = Gdx.audio.newSound(Gdx.files.internal("music/buy.mp3"));
        bg = new Texture("GameScreen/cart.png");

        // Initialize plants
        plants = new Plant[]{
                new Plant("Plant1", new Texture("plants/seedling1.png"), 5),
                new Plant("Plant2", new Texture("plants/seedling2.png"), 10),
                new Plant("Plant3", new Texture("plants/seedling3.png"), 15),
                new Plant("Plant4", new Texture("plants/seedling4.png"), 20),
                new Plant("Plant5", new Texture("plants/seedling5.png"), 25),
        };

        plantsDisplay = new Plant[]{
            new Plant("Plant1", new Texture("plants/grow1.png"), 5),
            new Plant("Plant2", new Texture("plants/grow2.png"), 10),
            new Plant("Plant3", new Texture("plants/grow3.png"), 15),
            new Plant("Plant4", new Texture("plants/grow4.png"), 20),
            new Plant("Plant5", new Texture("plants/grow5.png"), 25),
    };


        // Initialize plant bounds
        plantBounds = new Rectangle[plants.length];
        for (int i = 0; i < plants.length; i++) {
            plantBounds[i] = new Rectangle(280 + i * 120, 300, 100 * 2, 100 * 2);
        }


        exitButton = new Sprite(new Texture("exit.png"));
        exitButtonBounds = new Rectangle(1200, 0, exitButton.getWidth(), exitButton.getHeight());

        // Initialize ShelfSystem with plants array
        shelfSystem = new ShelfSystem(plants);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        handleInput();

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.showGameScreen();
        }

        // Check if cursor is over the button
        Vector3 cursorPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(cursorPos);


        if (Gdx.input.justTouched()) {
            for (int i = 0; i < plants.length; i++) {
                if (plantBounds[i].contains(cursorPos.x, cursorPos.y)) {
                    if (game.getCoins() >= plants[i].getPrice()) {
                        game.setCoins(game.getCoins() - plants[i].getPrice());

                        // Create a new Plant instance with specific coordinates
                        Plant purchasedPlant = new Plant(plants[i].getName(), plants[i].getTexture(),
                                650, 50, 100, 100, plants[i].getPrice());

                        // Add the purchased plant to the game screen
                        game.addPlantToGameScreen(purchasedPlant);
                        
                        // Update plantBounds with the correct position for the purchased plant
                        plantBounds[i].x = 650; // Set x position
                        plantBounds[i].y = 50; // Set y position

                        // Print the purchase message
                        System.out.println(plants[i].getName() + " bought! Remaining coins: " + game.getCoins());

                        // Print the position of "Plant1" after purchase
                        if (i == 0) {
                            System.out.println("Position of Plant1 after purchase - x: " + plantBounds[i].x + ", y: " + plantBounds[i].y);
                        }

                        // Save the updated state

                        return; // Exit render to prevent further drawing this frame
                    } else {
                        System.out.println("Not enough coins for " + plants[i].getName() + "!");
                    }
                }
            }
        }

        sprite.begin();
        sprite.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sprite.draw(exitButton, exitButtonBounds.x, exitButtonBounds.y);
        font.draw(sprite, "Coins: " + game.getCoins(), 20, 40);



        // Draw plants
        for (int i = 0; i < plants.length; i++) {
            sprite.draw(plantsDisplay[i].getTexture(), 280 + i * 120, 300, plantBounds[i].width, plantBounds[i].height);
        }

        sprite.end();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            buySFX.play();
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (exitButtonBounds.contains(touchPos.x, touchPos.y)) {
                game.showGameScreen();
            }
        }
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
        if (sprite != null) {
            sprite.dispose();
        }
        if (bg != null) {
            bg.dispose();
        }
        if (font != null) {
            font.dispose();
        }
        if (shelfSystem != null) {
            shelfSystem.dispose();
        }

        if (buySFX != null) {
            buySFX.dispose();
        }
    }
}
