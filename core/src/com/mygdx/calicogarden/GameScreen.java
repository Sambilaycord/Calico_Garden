package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
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
import com.badlogic.gdx.graphics.Color;

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
    private int currentDay;
    private DecimalFormat decimalFormat;
    private Rectangle accessoryLogoBounds;
    private Rectangle shopLogoBounds;
    private Preferences prefs;
    private Rectangle[] plantBounds;
    private Rectangle sellLogoBounds;

    private Array<Plant> plantsToAdd;

    public GameScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
        font = new BitmapFont();
        decimalFormat = new DecimalFormat("00"); // Format for two-digit numbers

        prefs = Gdx.app.getPreferences("CalicoGardenGameData");

        plantBounds = new Rectangle[game.getPlants().size()];
        for (int i = 0; i < game.getPlants().size(); i++) {
            plantBounds[i] = new Rectangle(100 + i * 120, 300, 100, 100); // Example positions and sizes
        }

        currentDay = prefs.getInteger("day", 1);
        sellLogo = new Sprite(new Texture("sell_icon.png"));
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
        shopLogoBounds = new Rectangle(0, 600, shopLogo.getWidth(), shopLogo.getHeight());
        sellLogoBounds = new Rectangle(0, 300, sellLogo.getWidth(), sellLogo.getHeight());

        shelfSystem = new ShelfSystem(game.getPlants().toArray(new Plant[0]));
        plantGrowthSystem = new PlantGrowthSystem();

        font.setColor(Color.ORANGE);



        game.music();
        shelfSystem.loadState();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        shelfSystem.setPlantSize(0, 100, 100);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int x = Gdx.input.getX(0);
        int y = Gdx.input.getY(0);
        boolean isTouched = Gdx.input.isTouched(0);

        int secondX = Gdx.input.isTouched(1) ? Gdx.input.getX(1) : x;
        int secondY = Gdx.input.isTouched(1) ? Gdx.input.getY(1) : y;
        boolean isSecondTouch = Gdx.input.isTouched(1);

        shelfSystem.update(delta, x, y, isTouched, isSecondTouch, secondX, secondY, camera, sellLogoBounds, game);

        updateUIElements();
        updateTime(delta);
        updatePlantGrowth();

        handleInput();

        sprite.begin();
        drawGameScreen();
        sprite.end();
    }

    private void updateUIElements() {
        shopLogo.setPosition(0, 600);
        accessoryLogo.setPosition(0, 450);
        sellLogo.setPosition(0, 300);
    }

    private void updateTime(float delta) {
        timer += delta * 360; // 360 in-game seconds per real second
        int inGameSeconds = (int) timer;

        if (inGameSeconds >= 86400) { // 86400 in-game seconds in 24 hours
            day++;
            timer -= 86400; // Reset the timer, keep the excess time
            inGameSeconds = (int) timer;
        }
    }

    private void updatePlantGrowth() {
        int previousDay = currentDay;
        currentDay = day;
        if (currentDay > previousDay) {
            plantGrowthSystem.newDay();
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            shelfSystem.resetState();
        }

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            Vector3 cursorPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(cursorPos);

            for (int i = 0; i < shelfSystem.getLockPositionsArray().length; i++) {
                float[] lockPos = shelfSystem.getLockPositionsArray()[i];
                float lockX = lockPos[1];
                float lockY = lockPos[0];
                float lockWidth = lockPos[2] - lockPos[1];
                float lockHeight = 100f; // Adjust as needed

                if (cursorPos.x >= lockX && cursorPos.x <= lockX + lockWidth && cursorPos.y >= lockY && cursorPos.y <= lockY + lockHeight) {
                    plantGrowthSystem.waterPlant();
                    break; // Exit loop once watered
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.showAccessoryMenu();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.showShopScreen();
        }
    }

    private void drawGameScreen() {
        sprite.draw(bg, 0, 0);
        cat.setPosition(-110, -110);
        cat.draw(sprite);
        shopLogo.draw(sprite);
        accessoryLogo.draw(sprite);
        if (sellLogo != null) {
            sellLogo.draw(sprite);
        }

        shelfSystem.draw(sprite);

        accessory();

        camera.update();
        sprite.setProjectionMatrix(camera.combined);

        int inGameSeconds = (int) timer;
        int hours = (inGameSeconds / 3600) % 24;
        int minutes = (inGameSeconds % 3600) / 60;
        String formattedTime = decimalFormat.format(hours) + ":" + decimalFormat.format(minutes);

        font.draw(sprite, "Coins: " + game.getCoins(), 200, 730);
        font.draw(sprite, "Time: " + formattedTime, 200, 780);
        font.draw(sprite, "Day: " + day, 400, 780);
        if (plantGrowthSystem.isFullyGrown()) {
            font.draw(sprite, "Plant is fully grown!", 100, 420);
        }
    }

    public void addPlantToGameScreen(Plant plant) {
        plantsToAdd.add(plant);
    }

    public void accessory() {
        Texture accessory = game.getSelectedAccessory();
        if (accessory != null) {
            Sprite accessorySprite = new Sprite(accessory);
            accessorySprite.setSize(Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 1.4f);
            accessorySprite.setPosition(-110, -110); // Adjust position to be on top of the cat
            accessorySprite.draw(sprite);
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        cat.setSize(Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 1.4f);
        font.getData().setScale(2.0f, 2.0f);
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
        shelfSystem.saveState();
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
