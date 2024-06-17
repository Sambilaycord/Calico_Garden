package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShelfSystem {

    private final Texture potTexture;
    private final Texture potTexture2;
    private final Texture snapTexture;

    private float potX;
    private float potY;
    private float potX2;
    private float potY2;

    private final float[][] lockPositions;

    private int currentLockIndex1;
    private int currentLockIndex2;
    private boolean draggingPot1; // Flag to track if pot1 is being dragged
    private boolean draggingPot2; // Flag to track if pot2 is being dragged

    private float snapX; // Position of the snap texture
    private float snapY; // Position of the snap texture

    public ShelfSystem(Texture potTexture, Texture anotherPotTexture, Texture snapTexture, float[][] lockPositions) {
        this.potTexture = potTexture;
        this.potTexture2 = anotherPotTexture;
        this.snapTexture = snapTexture;
        this.lockPositions = lockPositions;
        this.currentLockIndex1 = 0;
        this.currentLockIndex2 = 0;
        this.potY = lockPositions[currentLockIndex1][0];
        this.potX = (Gdx.graphics.getWidth() - potTexture.getWidth()) * 1.5f;
        this.potX2 = (Gdx.graphics.getWidth() - potTexture2.getWidth()) * 0.5f;
        this.potY2 = lockPositions[currentLockIndex2][0];
        this.snapX = potX; // Initialize snapX to potX
        this.snapY = potY; // Initialize snapY to potY
    }

    public void update(float delta, float mouseX, float mouseY, boolean isLeftClick) {
        float invertedMouseY = Gdx.graphics.getHeight() - mouseY;

        if (isLeftClick) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (isDragging(mouseX, invertedMouseY, potX, potY)) {
                    draggingPot1 = true;
                    draggingPot2 = false;
                } else if (isDragging(mouseX, invertedMouseY, potX2, potY2)) {
                    draggingPot1 = false;
                    draggingPot2 = true;
                }
            }
        }

        // Update snap position while dragging
        if (draggingPot1) {
            snapX = mouseX - potTexture.getWidth() / 25f;
            snapY = invertedMouseY - potTexture.getHeight() / 2f;
        } else if (draggingPot2) {
            snapX = mouseX - potTexture2.getWidth() / 25f;
            snapY = invertedMouseY - potTexture2.getHeight() / 2f;
        }

        // Release the pot and place it at the snap position
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (draggingPot1) {
                draggingPot1 = false;
                if (currentLockIndex2 != 0 || snapY != lockPositions[0][0]) {
                    // Only snap to non-bottom positions if another pot is already there
                    potX = Math.max(Math.min(snapX, lockPositions[currentLockIndex1][2]), lockPositions[currentLockIndex1][1]);
                }
                currentLockIndex1 = getClosestLockPosition(invertedMouseY);
                potY = lockPositions[currentLockIndex1][0];
                // Prevent both pots from being on the bottom
                if (currentLockIndex1 == 0 && currentLockIndex2 == 0) {
                    currentLockIndex2 = 1; // Move the second pot to a different position
                    potY2 = lockPositions[currentLockIndex2][0];
                }
            } else if (draggingPot2) {
                draggingPot2 = false;
                if (currentLockIndex1 != 0 || snapY != lockPositions[0][0]) {
                    // Only snap to non-bottom positions if another pot is already there
                    potX2 = Math.max(Math.min(snapX, lockPositions[currentLockIndex2][2]), lockPositions[currentLockIndex2][1]);
                }
                currentLockIndex2 = getClosestLockPosition(invertedMouseY);
                potY2 = lockPositions[currentLockIndex2][0];
                // Prevent both pots from being on the bottom
                if (currentLockIndex2 == 0 && currentLockIndex1 == 0) {
                    currentLockIndex1 = 1; // Move the first pot to a different position
                    potY = lockPositions[currentLockIndex1][0];
                }
            }
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

    private boolean isDragging(float mouseX, float mouseY, float potX, float potY) {
        return mouseX >= potX && mouseX <= potX + potTexture.getWidth() &&
               mouseY >= potY && mouseY <= potY + potTexture.getHeight();
    }

    public void draw(SpriteBatch batch) {
        if (draggingPot1 || draggingPot2) {
            float invertedMouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            int closestLockIndex = getClosestLockPosition(invertedMouseY);
            float snapY = lockPositions[closestLockIndex][0];

            // Draw the snap texture at the cursor position
            batch.draw(snapTexture, snapX, snapY, Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 5f);
        }

        batch.draw(potTexture, potX, potY, Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 5f);
        batch.draw(potTexture2, potX2, potY2, Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 5f);
    }

    public float getPotX() {
        return potX;
    }

    public void setPotX(float potX) {
        this.potX = potX;
    }

    public float getPotY() {
        return potY;
    }

    public void setPotY(float potY) {
        this.potY = potY;
    }
}
