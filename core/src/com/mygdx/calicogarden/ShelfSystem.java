package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShelfSystem {

    private final Texture potTexture; // Texture for the draggable pot
    private final Texture snapTexture; // Texture for the snap guide
    private float potX; // X-coordinate of the pot
    private float potY; // Y-coordinate of the pot
    private final float[][] lockPositions; // Array of Y positions where the pot can lock
    
    private int currentLockIndex; // Index of the current lock position
    private boolean dragging; // Flag to indicate if the pot is being dragged

    public ShelfSystem(Texture potTexture, Texture snapTexture, float[][] lockPositions) {
        this.potTexture = potTexture;
        this.snapTexture = snapTexture;
        this.potX = (Gdx.graphics.getWidth() - potTexture.getWidth()) * 1.5f; // Center X-coordinate (adjust as needed)
        this.lockPositions = lockPositions;
        this.currentLockIndex = 0; // Start at the first lock position
        this.potY = lockPositions[currentLockIndex][0]; // Initial Y-coordinate
    }

    public void update(float delta, float mouseX, float mouseY) {
        float invertedMouseY = Gdx.graphics.getHeight() - mouseY; // Invert the Y-coordinate

        if (isDragging(mouseX, invertedMouseY)) {
            dragging = true;
        }

        if (dragging) {
            // Ensure pot stays within X-axis borders of the current lock position
            potX = Math.max(Math.min(mouseX - potTexture.getWidth() / 6f, lockPositions[currentLockIndex][2]), lockPositions[currentLockIndex][1]);
            potY = lockPositions[currentLockIndex][0]; // Keep potY locked at the current position
        }


        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && dragging) {
            dragging = false; // Stop dragging on mouse release
            // Snap to the closest lock position
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
        // Draw the snap guide
        if (dragging) {
            float invertedMouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert the Y-coordinate
            int closestLockIndex = getClosestLockPosition(invertedMouseY);
            float snapY = lockPositions[closestLockIndex][0];
            float snapX = potX - (Gdx.graphics.getWidth() / 7f - potTexture.getWidth()) / 30f / 30f; // Adjust X for centering

            batch.draw(snapTexture, snapX, snapY, Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 5f);
        }

        // Draw the actual pot
        batch.draw(potTexture, potX, potY, Gdx.graphics.getWidth() / 7f, Gdx.graphics.getHeight() / 5f);
    }

    private boolean isDragging(float mouseX, float mouseY) {
        // Implement your drag detection logic here (e.g., check for click within pot bounds)
        return Gdx.input.isButtonPressed(Input.Buttons.LEFT) && // Check for left mouse button press
               mouseX >= potX && mouseX <= potX + potTexture.getWidth() &&
               mouseY >= potY && mouseY <= potY + potTexture.getHeight();
    }

    public boolean isPotClicked(float clickX, float clickY) {
        boolean clicked = Gdx.input.isButtonPressed(Input.Buttons.LEFT) && // Check for left mouse button press
               clickX >= potX && clickX <= potX + potTexture.getWidth() &&
               clickY >= potY && clickY <= potY + potTexture.getHeight();
        Gdx.app.log("ShelfSystem", "isPotClicked - clicked: " + clicked);
        return clicked;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    // Added getter methods
    public Texture getPotTexture() {
        return potTexture;
    }

    public float getPotX() {
        return potX;
    }

    public float getPotY() {
        return potY;
    }

    public void setPotX(float potX) {
        this.potX = potX;
    }

    public void setPotY(float potY) {
        this.potY = potY;
    }
}
