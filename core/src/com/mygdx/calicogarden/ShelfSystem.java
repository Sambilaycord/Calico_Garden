package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShelfSystem {

    private final Texture potTexture;
    private final Texture snapTexture;
    private float potX;
    private float potY;
    private final float[][] lockPositions;
    private int currentLockIndex;
    private boolean dragging;

    public ShelfSystem(Texture potTexture, Texture snapTexture, float[][] lockPositions) {
        this.potTexture = potTexture;
        this.snapTexture = snapTexture;
        this.lockPositions = lockPositions;
        this.currentLockIndex = 0;
        this.potY = lockPositions[currentLockIndex][0];
        this.potX = (Gdx.graphics.getWidth() - potTexture.getWidth()) * 1.5f;
    }

    public void update(float delta, float mouseX, float mouseY) {
        float invertedMouseY = Gdx.graphics.getHeight() - mouseY;

        if (isDragging(mouseX, invertedMouseY)) {
            dragging = true;
        }

        if (dragging) {
            potX = Math.max(Math.min(mouseX - potTexture.getWidth() / 6f, lockPositions[currentLockIndex][2]), lockPositions[currentLockIndex][1]);
            potY = lockPositions[currentLockIndex][0];
        }

        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && dragging) {
            dragging = false;
            currentLockIndex = getClosestLockPosition(invertedMouseY);
            potY = lockPositions[currentLockIndex][0];
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

    public void draw(SpriteBatch batch) {
        if (dragging) {
            float invertedMouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            int closestLockIndex = getClosestLockPosition(invertedMouseY);
            float snapY = lockPositions[closestLockIndex][0];
            float snapX = potX - (Gdx.graphics.getWidth() / 7f - potTexture.getWidth()) / 30f / 30f;

            batch.draw(snapTexture, snapX, snapY, Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 5f);
        }

        batch.draw(potTexture, potX, potY, Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 5f);
    }

    private boolean isDragging(float mouseX, float mouseY) {
        return Gdx.input.isButtonPressed(Input.Buttons.LEFT) &&
               mouseX >= potX && mouseX <= potX + potTexture.getWidth() &&
               mouseY >= potY && mouseY <= potY + potTexture.getHeight();
    }

    public boolean isPotClicked(float clickX, float clickY) {
        boolean clicked = Gdx.input.isButtonPressed(Input.Buttons.LEFT) &&
               clickX >= potX && clickX <= potX + potTexture.getWidth() &&
               clickY >= potY && clickY <= potY + potTexture.getHeight();
        Gdx.app.log("ShelfSystem", "isPotClicked - clicked: " + clicked);
        return clicked;
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
