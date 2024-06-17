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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.text.DecimalFormat;

public class GameScreen implements Screen {
    private SpriteBatch sprite;
    private Texture bg;
    private Sprite cat;
    private Sprite shopLogo;
    private Sprite accessoryLogo;
    private CalicoGarden game;
    private OrthographicCamera camera;
    private ShelfSystem shelfSystem;
    private PlantGrowthSystem plantGrowthSystem;
    private Texture potTexture;
    private Texture snapTexture;
    private BitmapFont font;
    private float timer = 0;
    private int day = 1; // Add a day variable
    private DecimalFormat decimalFormat;
    private Rectangle accessoryLogoBounds;
    private Rectangle shopLogoBounds;
    private Preferences prefs;
    private Rectangle[] plantBounds;

    public GameScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
        font = new BitmapFont();
        timer = 0;
        decimalFormat = new DecimalFormat("00"); // Format for two-digit numbers

        prefs = Gdx.app.getPreferences("CalicoGardenGameData");

        plantBounds = new Rectangle[game.getPlants().size()];
        for (int i = 0; i < game.getPlants().size(); i++) {
            plantBounds[i] = new Rectangle(100 + i * 120, 300, 100, 100); // Example positions and sizes
        }
    }


    @Override
public void show() {
    sprite = new SpriteBatch();
    bg = new Texture("GameScreen/GameScreenBackground.png");
    cat = new Sprite(new Texture("ming.png"));
    accessoryLogo = new Sprite(new Texture("accessory_icon.png"));
    shopLogo = new Sprite(new Texture("shop_icon.png"));
    potTexture = new Texture("Pots/pot.png");
    snapTexture = new Texture("Pots/potSnap.png");

    day = prefs.getInteger("day", 1);
    timer = prefs.getInteger("timeInSeconds", 0);

    accessoryLogoBounds = new Rectangle(0, 450, accessoryLogo.getWidth(), accessoryLogo.getHeight());
    shopLogoBounds  = new Rectangle(0, 600, shopLogo.getWidth(), shopLogo.getHeight());

    plantBounds = new Rectangle[game.getPlants().size()];
    for (int i = 0; i < game.getPlants().size(); i++) {
        plantBounds[i] = new Rectangle(100 + i * 120, 300, 100, 100); // Example positions and sizes
    }

    shelfSystem = new ShelfSystem(game.getPlants().toArray(new Plant[0]));
    plantGrowthSystem = new PlantGrowthSystem();

    game.music();
}


@Override
public void render(float delta) {
    ScreenUtils.clear(1, 0, 0, 1);
    shelfSystem.setPlantSize(0, 100, 100);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // Get touch input for dragging and resizing
    int x = Gdx.input.getX(0);
    int y = Gdx.input.getY(0);
    boolean isTouched = Gdx.input.isTouched(0);

    int secondX = Gdx.input.isTouched(1) ? Gdx.input.getX(1) : x;
    int secondY = Gdx.input.isTouched(1) ? Gdx.input.getY(1) : y;
    boolean isSecondTouch = Gdx.input.isTouched(1);

    // Update the shelf system with the touch input
    shelfSystem.update(delta, x, y, isTouched, isSecondTouch, secondX, secondY, camera);

    // Update UI elements
    shopLogo.setPosition(0, 600);
    accessoryLogo.setPosition(0, 450);

    // Update the in-game timer
    timer += delta * 480; // 480 in-game seconds per real second
    int inGameSeconds = (int) timer;

    if (inGameSeconds >= 86400) { // 86400 in-game seconds in 24 hours
        day++;
        timer -= 86400; // Reset the timer, keep the excess time
        inGameSeconds = (int) timer;
    }

    int hours = (inGameSeconds / 3600) % 24;
    int minutes = (inGameSeconds % 3600) / 60;
    String formattedTime = decimalFormat.format(hours) + ":" + decimalFormat.format(minutes);

    // Handle input
    handleInput();

    // Begin sprite batch
    sprite.begin();

    // Draw background
    sprite.draw(bg, 0, 0);

    // Draw cat and logos
    cat.setPosition(-110, -110);
    cat.draw(sprite);
    shopLogo.draw(sprite);
    accessoryLogo.draw(sprite);

    // Draw the shelf system (plants)
    shelfSystem.draw(sprite);

    // Draw additional plants from the game
  
    // Call accessory method (if it handles additional drawing)
    accessory();

    // Update camera
    camera.update();
    sprite.setProjectionMatrix(camera.combined);

    // Draw HUD (timer, day, plant growth information)
    font.draw(sprite, "Time: " + formattedTime, 100, 760);
    font.draw(sprite, "Day: " + day, 100, 740);
    font.draw(sprite, "Plant Growth Stage: " + plantGrowthSystem.getGrowthStage(), 100, 720);
    if (plantGrowthSystem.isFullyGrown()) {
        font.draw(sprite, "Plant is fully grown!", 100, 420);
    }

    // End sprite batch
    sprite.end();
}


    private void handleInput() {
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.showAccessoryMenu();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            plantGrowthSystem.waterPlant();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.showShopScreen();
        }
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

    public void timer() {
        // Not implemented
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        cat.setSize(Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 1.4f);
    }

    @Override
    public void pause() {
        prefs.putInteger("day", day);
        prefs.putInteger("timeInSeconds", (int) timer);
        prefs.flush();
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
