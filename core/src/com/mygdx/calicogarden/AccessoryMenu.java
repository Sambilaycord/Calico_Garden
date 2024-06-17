package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class AccessoryMenu implements Screen {
    private CalicoGarden game;
    private GameScreen maingame;
    private SpriteBatch sprite;
    private OrthographicCamera camera;
    private ShelfSystem shelfSystem;

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

    private Texture buyButton;
    private Sprite exitButton;
    private Rectangle exitButtonBounds;

    private Rectangle buyButtonBounds1;
    private Rectangle buyButtonBounds2;
    private Rectangle buyButtonBounds3;
    private Rectangle buyButtonBounds4;
    private Rectangle buyButtonBounds5;
    private Rectangle buyButtonBounds6;

    private Sound buySFX;

    private int money = 1000;


    public AccessoryMenu(CalicoGarden game) {
        this.game = game;
        this.camera = game.getCamera();
    }

    @Override
    public void show() {
        sprite = new SpriteBatch();
        buySFX = Gdx.audio.newSound(Gdx.files.internal("music/buy.mp3"));
        bg = new Texture("accessory_bg.png");
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

        exitButton = new Sprite(new Texture("exit.png"));
        exitButtonBounds = new Rectangle(1200, 0, exitButton.getWidth(), exitButton.getHeight());

        buyButton = new Texture("buy_button.png");
        buyButtonBounds1 = new Rectangle(0, 330, buyButton.getWidth(), buyButton.getHeight());
        buyButtonBounds2 = new Rectangle(350, 330, buyButton.getWidth(), buyButton.getHeight());
        buyButtonBounds3 = new Rectangle(650, 330, buyButton.getWidth(), buyButton.getHeight());
        buyButtonBounds4 = new Rectangle(950, 330, buyButton.getWidth(), buyButton.getHeight());

        buyButtonBounds5 = new Rectangle(410, 0, buyButton.getWidth(), buyButton.getHeight());
        buyButtonBounds6 = new Rectangle(820, 0, buyButton.getWidth(), buyButton.getHeight());

        // Initialize the bounds for each accessory
        accessoryBounds1 = new Rectangle(100, 450, accessory1.getWidth(), accessory1.getHeight());
        accessoryBounds2 = new Rectangle(400, 450, accessory2.getWidth(), accessory2.getHeight());
        accessoryBounds3 = new Rectangle(720, 450, accessory3.getWidth(), accessory3.getHeight());
        accessoryBounds4 = new Rectangle(1030, 450, accessory4.getWidth(), accessory4.getHeight());
        accessoryBounds5 = new Rectangle(500, 100, accessory5.getWidth(), accessory5.getHeight());
        accessoryBounds6 = new Rectangle(900, 100, accessory6.getWidth(), accessory6.getHeight());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        sprite.setProjectionMatrix(camera.combined);

        sprite.begin();
        handleInput();
        sprite.draw(bg, 0, 0);
        sprite.draw(accessory1, accessoryBounds1.x, accessoryBounds1.y);
        sprite.draw(accessory2, accessoryBounds2.x, accessoryBounds2.y);
        sprite.draw(accessory3, accessoryBounds3.x, accessoryBounds3.y);
        sprite.draw(accessory4, accessoryBounds4.x, accessoryBounds4.y);
        sprite.draw(accessory5, accessoryBounds5.x, accessoryBounds5.y);
        sprite.draw(accessory6, accessoryBounds6.x, accessoryBounds6.y);

        sprite.draw(buyButton, buyButtonBounds1.x, buyButtonBounds1.y);
        sprite.draw(buyButton, buyButtonBounds2.x, buyButtonBounds2.y);
        sprite.draw(buyButton, buyButtonBounds3.x, buyButtonBounds3.y);
        sprite.draw(buyButton, buyButtonBounds4.x, buyButtonBounds4.y);
        sprite.draw(buyButton, buyButtonBounds5.x, buyButtonBounds5.y);
        sprite.draw(buyButton, buyButtonBounds6.x, buyButtonBounds6.y);

        sprite.draw(exitButton, exitButtonBounds.x, exitButtonBounds.y);
        sprite.end();


    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            buySFX.play();
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if(exitButtonBounds.contains(touchPos.x, touchPos.y)){
                game.showGameScreen();
            }

            if (buyButtonBounds1.contains(touchPos.x, touchPos.y)) {
                if (!accessory1Bol && money >= 100) {
                    accessory1Bol = true;
                    money -= 100;
                }

                if (accessory1Bol) {
                    game.setSelectedAccessory(catAccessory1);
                    game.showGameScreen();
                }

            } else if (buyButtonBounds2.contains(touchPos.x, touchPos.y)) {
                if (!accessory2Bol && money >= 100) {
                    accessory2Bol = true;
                    money -= 100;
                }

                if (accessory2Bol) {
                    game.setSelectedAccessory(catAccessory2);
                    game.showGameScreen();
                }

            } else if (buyButtonBounds3.contains(touchPos.x, touchPos.y)) {
                if (!accessory3Bol && money >= 100) {
                    accessory3Bol = true;
                    money -= 100;
                }

                if (accessory3Bol) {
                    game.setSelectedAccessory(catAccessory3);
                    game.showGameScreen();
                }
            } else if (buyButtonBounds4.contains(touchPos.x, touchPos.y)) {
                if (!accessory4Bol && money >= 100) {
                    accessory4Bol = true;
                    money -= 100;
                }

                if (accessory4Bol) {
                    game.setSelectedAccessory(catAccessory4);
                    game.showGameScreen();
                }
            } else if (buyButtonBounds5.contains(touchPos.x, touchPos.y)) {
                if (!accessory5Bol && money >= 100) {
                    accessory5Bol = true;
                    money -= 100;
                }

                if (accessory5Bol) {
                    game.setSelectedAccessory(catAccessory5);
                    game.showGameScreen();
                }
            } else if (buyButtonBounds6.contains(touchPos.x, touchPos.y)) {
                if (!accessory6Bol && money >= 100) {
                    accessory6Bol = true;
                    money -= 100;
                }

                if (accessory6Bol) {
                    game.setSelectedAccessory(catAccessory6);
                    game.showGameScreen();
                }
            }


        }
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
        accessory3.dispose();
        accessory4.dispose();
        accessory5.dispose();
        accessory6.dispose();
    }
}
