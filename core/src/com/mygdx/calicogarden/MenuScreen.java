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
    private CalicoGarden game;
    private SpriteBatch sprite;
    private OrthographicCamera camera;

    private Texture bg;
    private Texture accessory1;
    private Texture accessory2;
    private Texture accessory3;

    private Rectangle accessoryBounds1;
    private Rectangle accessoryBounds2;
    private Rectangle accessoryBounds3;

    private boolean accessory1Bol = false;
    private boolean accessory2Bol = false;
    private boolean accessory3Bol = false;
    private int money = 800;


    public MenuScreen(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        bg = new Texture("bg2.jfif");
        accessory1 = new Texture("cat_loading.png");
        accessory2 = new Texture("cat_looking.png");
        accessory3 = new Texture("cat_phone.png");

        float accessory1X = 100;
        float accessory1Y = 100;
        float accessory2X = accessory1X + accessory1.getWidth() + 50; // 50 pixels padding between accessories
        float accessory2Y = 100;
        float accessory3X = accessory2X + accessory1.getWidth() + 50; // 50 pixels padding between accessories
        float accessory3Y = 100;


        // Initialize the bounds for each accessory
        accessoryBounds1 = new Rectangle(accessory1X, accessory1Y, accessory1.getWidth(), accessory1.getHeight());
        accessoryBounds2 = new Rectangle(accessory2X, accessory2Y, accessory2.getWidth(), accessory2.getHeight());
        accessoryBounds3 = new Rectangle(accessory3X, accessory3Y, accessory2.getWidth(), accessory2.getHeight());
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

            if (!accessory1Bol && money >= 100){
                accessory1Bol = true;
                money -= 100;
            } else if (!accessory2Bol && money >= 200){
                accessory2Bol = true;
                money -= 200;
            } else if (!accessory3Bol && money >= 300){
                accessory3Bol = true;
                money -= 300;
            }

            // Check if the touch is within the bounds of accessory1
            if (accessoryBounds1.contains(touchPos.x, touchPos.y) && accessory1Bol) {
                game.setSelectedAccessory(accessory1);
                game.showGameScreen();
            } else if (accessoryBounds2.contains(touchPos.x, touchPos.y) && accessory2Bol) {
                game.setSelectedAccessory(accessory2);
                game.showGameScreen();
            } else if (accessoryBounds3.contains(touchPos.x, touchPos.y) && accessory3Bol) {
                game.setSelectedAccessory(accessory3);
                game.showGameScreen();
            }
        }

        sprite.begin();
        sprite.draw(bg, 0, 0);
        sprite.draw(accessory1, accessoryBounds1.x, accessoryBounds1.y);
        sprite.draw(accessory2, accessoryBounds2.x, accessoryBounds2.y);
        sprite.draw(accessory3, accessoryBounds3.x, accessoryBounds3.y);
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
        accessory1.dispose();
        accessory2.dispose();
    }
}
