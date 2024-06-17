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

public class AccessoryMenu implements Screen {
    private CalicoGarden game;
    private SpriteBatch sprite;
    private OrthographicCamera camera;

    private Texture bg;
    private Texture accessory1;
    private Texture accessory2;
    private Texture accessory3;
    private Texture accessory4;
    private Texture accessory5;
    private Texture accessory6;

    private Rectangle accessoryBounds1;
    private Rectangle accessoryBounds2;
    private Rectangle accessoryBounds3;
    private Rectangle accessoryBounds4;
    private Rectangle accessoryBounds5;
    private Rectangle accessoryBounds6;

    private Texture catAccessory1;
    private Texture catAccessory2;
    private Texture catAccessory3;
    private Texture catAccessory4;
    private Texture catAccessory5;
    private Texture catAccessory6;

    private boolean accessory1Bol = false;
    private boolean accessory2Bol = false;
    private boolean accessory3Bol = false;
    private boolean accessory4Bol = false;
    private boolean accessory5Bol = false;
    private boolean accessory6Bol = false;

    private int money = 100000;


    public AccessoryMenu(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        bg = new Texture("bg2.jfif");
        accessory1 = new Texture("accessory1.png");
        accessory2 = new Texture("accessory2.png");
        accessory3 = new Texture("accessory3.png");
        accessory4 = new Texture("accessory4.png");
        accessory5 = new Texture("accessory5.png");
        accessory6 = new Texture("accessory6.png");

        catAccessory1 = new Texture("cat_accessory1.png");
        catAccessory2 = new Texture("cat_accessory2.png");
        catAccessory3 = new Texture("cat_accessory3.png");
        catAccessory4 = new Texture("cat_accessory4.png");
        catAccessory5 = new Texture("cat_accessory5.png");
        catAccessory6 = new Texture("cat_accessory6.png");

        float accessory2X = accessory1.getWidth();
        float accessory3X = accessory2X + accessory1.getWidth();
        float accessory5X = accessory4.getWidth();
        float accessory6X = accessory5X + accessory1.getWidth();

        // Initialize the bounds for each accessory
        accessoryBounds1 = new Rectangle(0, 400, accessory1.getWidth(), accessory1.getHeight());
        accessoryBounds2 = new Rectangle(accessory2X, 400, accessory2.getWidth(), accessory2.getHeight());
        accessoryBounds3 = new Rectangle(accessory3X, 400, accessory3.getWidth(), accessory3.getHeight());

        accessoryBounds4 = new Rectangle(0, 0, accessory4.getWidth(), accessory4.getHeight());
        accessoryBounds5 = new Rectangle(accessory5X, 0, accessory5.getWidth(), accessory5.getHeight());
        accessoryBounds6 = new Rectangle(accessory6X, 0, accessory6.getWidth(), accessory6.getHeight());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

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
            } else if (!accessory4Bol && money >= 400){
                accessory4Bol = true;
                money -= 200;
            } else if (!accessory5Bol && money >= 500){
                accessory5Bol = true;
                money -= 300;
            } else if (!accessory6Bol && money >= 600){
                accessory6Bol = true;
                money -= 300;
            }

            // Check if the touch is within the bounds of accessory1
            if (accessoryBounds1.contains(touchPos.x, touchPos.y) && accessory1Bol) {
                game.setSelectedAccessory(catAccessory1);
                game.showGameScreen();
            } else if (accessoryBounds2.contains(touchPos.x, touchPos.y) && accessory2Bol) {
                game.setSelectedAccessory(catAccessory2);
                game.showGameScreen();
            } else if (accessoryBounds3.contains(touchPos.x, touchPos.y) && accessory3Bol) {
                game.setSelectedAccessory(catAccessory3);
                game.showGameScreen();
            } else if (accessoryBounds4.contains(touchPos.x, touchPos.y) && accessory4Bol) {
                game.setSelectedAccessory(catAccessory4);
                game.showGameScreen();
            } else if (accessoryBounds5.contains(touchPos.x, touchPos.y) && accessory5Bol) {
                game.setSelectedAccessory(catAccessory5);
                game.showGameScreen();
            } else if (accessoryBounds6.contains(touchPos.x, touchPos.y) && accessory6Bol) {
                game.setSelectedAccessory(catAccessory6);
                game.showGameScreen();
            }
        }

        sprite.begin();
        sprite.draw(bg, 0, 0);
        sprite.draw(accessory1, accessoryBounds1.x, accessoryBounds1.y);
        sprite.draw(accessory2, accessoryBounds2.x, accessoryBounds2.y);
        sprite.draw(accessory3, accessoryBounds3.x, accessoryBounds3.y);
        sprite.draw(accessory4, accessoryBounds4.x, accessoryBounds4.y);
        sprite.draw(accessory5, accessoryBounds5.x, accessoryBounds5.y);
        sprite.draw(accessory6, accessoryBounds6.x, accessoryBounds6.y);
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
