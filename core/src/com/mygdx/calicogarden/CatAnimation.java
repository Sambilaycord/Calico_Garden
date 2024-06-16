package com.mygdx.calicogarden;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CatAnimation implements Screen {
    private static final float FRAME_TIME_1 = 3.0f; // 3 seconds for the 1st frame
    private static final float FRAME_TIME_2_3 = 1 / 30f; // fast for the 2nd and 3rd frames
    private static final float FADE_DURATION = 1.0f; // duration for fade in and fade out

    private float elapsed_time;
    private Array<AtlasRegion> frames;
    private Array<Float> frameDurations;
    private SpriteBatch batch;
    private Texture splashTexture;
    private Texture splashHoverTexture;
    private OrthographicCamera camera;
    private Viewport viewport;
    private float catX, catY; // Position variables for the cat
    private Texture backgroundTexture;
    private float splashX, splashY, splashWidth, splashHeight; // Position and size of the splash (start button)
    private CalicoGarden game;

    private boolean fadingIn;
    private boolean fadingOut;
    private float fadeTimer;

    public CatAnimation(SpriteBatch batch, CalicoGarden game) {
        this.batch = batch;
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1400, 800, camera); // Adjust the viewport size as needed
        this.viewport.apply();
        this.camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        this.fadingIn = true;
        this.fadingOut = false;
        this.fadeTimer = 0f;
    }

    @Override
    public void show() {
        TextureAtlas charset = new TextureAtlas(Gdx.files.internal("CatAnimation/CatBlinking.atlas"));
        splashTexture = new Texture("Menu/start.png");
        splashHoverTexture = new Texture("Menu/start2.png"); // Load the hover texture
        backgroundTexture = new Texture("Menu/backgroundMenu.jpg"); // Load your background image
        frames = charset.findRegions("blinking");
        frameDurations = new Array<>(new Float[]{FRAME_TIME_1, FRAME_TIME_2_3, FRAME_TIME_2_3});
        
        // Initialize catX and catY now that the viewport is set up
        catX = camera.viewportWidth / 2.7f;
        catY = camera.viewportHeight / 6f;

        // Position and size of the splash (start button)
        splashWidth = camera.viewportWidth / 2f;
        splashHeight = camera.viewportHeight / 15f;
        splashX = (camera.viewportWidth - splashWidth) / 2;
        splashY = camera.viewportHeight / 1.5f;
    }

    @Override
    public void render(float delta) {
        elapsed_time += Gdx.graphics.getDeltaTime();

        // Handle fading
        if (fadingIn) {
            fadeTimer += delta;
            if (fadeTimer > FADE_DURATION) {
                fadeTimer = FADE_DURATION;
                fadingIn = false;
            }
        } else if (fadingOut) {
            fadeTimer -= delta;
            if (fadeTimer < 0) {
                fadeTimer = 0;
                fadingOut = false;
                game.setScreen(new GameScreen(game)); // Switch to the new screen when fade out is complete
            }
        }

        float alpha = fadeTimer / FADE_DURATION;

        // Determine the current frame based on elapsed_time
        float totalTime = 0;
        int currentFrameIndex = 0;
        for (int i = 0; i < frameDurations.size; i++) {
            totalTime += frameDurations.get(i);
            if (elapsed_time < totalTime) {
                currentFrameIndex = i;
                break;
            }
        }

        if (elapsed_time > totalTime) {
            elapsed_time = 0; // Reset elapsed time if it exceeds the total animation duration
        }
        TextureRegion currentFrame = frames.get(currentFrameIndex);

        // Clear the screen
        Gdx.gl.glClearColor(0.0f, 0, 0.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the camera and batch projection matrix
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Enable alpha blending
        batch.enableBlending();
        batch.setColor(1, 1, 1, alpha);

        // Draw the background
        batch.begin();
        drawBackground();
        
        // Determine which splash texture to use based on mouse hover
        Texture currentSplashTexture = splashTexture;
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        Vector3 touchPos = new Vector3(mouseX, mouseY, 0);
        camera.unproject(touchPos);

        if (touchPos.x >= splashX && touchPos.x <= splashX + splashWidth &&
            touchPos.y >= splashY && touchPos.y <= splashY + splashHeight) {
            currentSplashTexture = splashHoverTexture;
        }

        batch.draw(currentSplashTexture, splashX, splashY, splashWidth, splashHeight);
        batch.draw(currentFrame, catX, catY, currentFrame.getRegionWidth() / 3f, currentFrame.getRegionHeight() / 3f);
        batch.end();

        // Check for touch input
        if (Gdx.input.isTouched() && !fadingOut) {
            if (touchPos.x >= splashX && touchPos.x <= splashX + splashWidth &&
                touchPos.y >= splashY && touchPos.y <= splashY + splashHeight) {
                fadingOut = true; // Start fading out when the splash button is touched
            }
        }
    }

    private void drawBackground() {
        float screenWidth = viewport.getWorldWidth();
        float screenHeight = viewport.getWorldHeight();
        
        // Calculate aspect ratio of the background and screen
        float bgAspectRatio = 19f / 6f;
        float screenAspectRatio = screenWidth / screenHeight;
        
        float bgWidth, bgHeight;

        if (screenAspectRatio > bgAspectRatio) {
            // Screen is wider than the background aspect ratio
            bgWidth = screenWidth;
            bgHeight = screenWidth / bgAspectRatio;
        } else {
            // Screen is taller than the background aspect ratio
            bgHeight = screenHeight;
            bgWidth = screenHeight * bgAspectRatio;
        }

        float bgX = (screenWidth - bgWidth) / 2;
        float bgY = (screenHeight - bgHeight) / 2;

        batch.draw(backgroundTexture, bgX, bgY, bgWidth, bgHeight);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        splashTexture.dispose();
        splashHoverTexture.dispose();
        backgroundTexture.dispose();
        // Dispose of other resources as needed
    }

    // Method to update the cat's position
    public void setCatPosition(float x, float y) {
        this.catX = x;
        this.catY = y;
    }

    public OrthographicCamera getCamera() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCamera'");
    }
}
 