package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class ShelfSystem {

    private final Texture potTexture;
    private final Texture potTexture2;

    private Rectangle potBounds1;
    private Rectangle potBounds2;

    private final float[][] lockPositions;

    private int currentLockIndex1;
    private int currentLockIndex2;
    private boolean draggingPot1; // Flag to track if pot1 is being dragged
    private boolean draggingPot2; // Flag to track if pot2 is being dragged

    private Preferences preferences;

    public ShelfSystem(Texture potTexture, Texture anotherPotTexture, float[][] lockPositions) {
        this.potTexture = potTexture;
        this.potTexture2 = anotherPotTexture;
        this.lockPositions = lockPositions;
        this.preferences = Gdx.app.getPreferences("PotPositions");

        this.currentLockIndex1 = preferences.getInteger("pot1LockIndex", 0);
        this.currentLockIndex2 = preferences.getInteger("pot2LockIndex", 0);

        float pot1X = preferences.getFloat("pot1X", (Gdx.graphics.getWidth() - potTexture.getWidth()));
        float pot1Y = preferences.getFloat("pot1Y", lockPositions[currentLockIndex1][0]);
        float pot2X = preferences.getFloat("pot2X", (Gdx.graphics.getWidth() - potTexture2.getWidth()));
        float pot2Y = preferences.getFloat("pot2Y", lockPositions[currentLockIndex2][0]);

        this.potBounds1 = new Rectangle(pot1X, pot1Y, 150, 100);
        this.potBounds2 = new Rectangle(pot2X, pot2Y, 150, 100);
    }

    public void update(float delta, float mouseX, float mouseY, boolean isLeftClick) {
        float invertedMouseY = Gdx.graphics.getHeight() - mouseY;

        if (isLeftClick) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (potBounds1.contains(mouseX, invertedMouseY)) {
                    draggingPot1 = true;
                    draggingPot2 = false;
                } else if (potBounds2.contains(mouseX, invertedMouseY)) {
                    draggingPot1 = false;
                    draggingPot2 = true;
                }
            }
        }

        // Release the pot and snap it to the nearest lock position
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (draggingPot1) {
                draggingPot1 = false;
                snapToLockPosition(potBounds1, 1);
                savePotPosition(1, potBounds1);
            } else if (draggingPot2) {
                draggingPot2 = false;
                snapToLockPosition(potBounds2, 2);
                savePotPosition(2, potBounds2);
            }
        }

        // Update the original pot's position if it's being dragged
        if (draggingPot1) {
            potBounds1.setPosition(mouseX - potBounds1.width / 2f, invertedMouseY - potBounds1.height / 2f);
        } else if (draggingPot2) {
            potBounds2.setPosition(mouseX - potBounds2.width / 2f, invertedMouseY - potBounds2.height / 2f);
        }
    }

    private void snapToLockPosition(Rectangle potBounds, int potIndex) {
        int currentLockIndex = getClosestLockPosition(potBounds.y);
        potBounds.setPosition(potBounds.x, lockPositions[currentLockIndex][0]);
        if (potIndex == 1) {
            currentLockIndex1 = currentLockIndex;
        } else {
            currentLockIndex2 = currentLockIndex;
        }
    }

    private int getClosestLockPosition(float mouseY) {
        int closestIndex = 0;
        float closestDistance = Math.abs(mouseY - lockPositions[0][0]);

        for (int i = 1; i < lockPositions.length; i++) {
            float distance = Math.abs(mouseY - lockPositions[i][0]);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestIndex = i;
            }
        }

        return closestIndex;
    }

    private void savePotPosition(int potIndex, Rectangle potBounds) {
        preferences.putInteger("pot" + potIndex + "LockIndex", potIndex == 1 ? currentLockIndex1 : currentLockIndex2);
        preferences.putFloat("pot" + potIndex + "X", potBounds.x);
        preferences.putFloat("pot" + potIndex + "Y", potBounds.y);
        preferences.flush();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(potTexture, potBounds1.x, potBounds1.y, Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 5f);
        batch.draw(potTexture2, potBounds2.x, potBounds2.y, Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 5f);
    }
}
