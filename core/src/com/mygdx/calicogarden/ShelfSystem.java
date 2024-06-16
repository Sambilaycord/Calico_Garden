package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShelfSystem {

    private final Texture potTexture; // Texture for the draggable pot
    private float potX; // X-coordinate of the pot
    private float potY; // Y-coordinate of the pot
    private final float shelfStartY; // Y-coordinate of the shelf (where the pot can be placed)
    private final float shelfHeight; // Height of the shelf

    private boolean dragging; // Flag to indicate if the pot is being dragged

    public ShelfSystem(Texture potTexture, float shelfStartY, float shelfHeight) {
        this.potTexture = potTexture;
        this.potX = Gdx.graphics.getWidth() / 2f; // Initial X-coordinate (adjust as needed)
        this.potY = 0; // Initial Y-coordinate (adjust as needed)
        this.shelfStartY = shelfStartY;
        this.shelfHeight = shelfHeight;
    }

    public void update(float delta, float mouseX, float mouseY) {
        if (isDragging(mouseX, mouseY)) {
            dragging = true;
        }

        if (dragging) {
            potX = mouseX - potTexture.getWidth() / 6f; // Center the pot under the mouse
            potY = mouseY - potTexture.getHeight() / 2f;
            // Ensure the pot stays within the screen bounds (adjust as needed)
            potY = Math.min(potY, Gdx.graphics.getHeight() - potTexture.getHeight());
            potY = Math.max(potY, 0);
        } else if (isOverShelf(mouseX, mouseY)) {
            // Snap the pot to the shelf position when dropped over it
            potY = shelfStartY + shelfHeight - potTexture.getHeight();
        }


        if (isPotClicked(mouseX, mouseY)) {
            dragging = false; // Set dragging to false to prevent further movement
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(potTexture, potX, potY);
    }

    private boolean isDragging(float mouseX, float mouseY) {
        // Implement your drag detection logic here (e.g., check for click within pot bounds)
        return Gdx.input.isButtonPressed(Input.Buttons.LEFT) && // Check for left mouse button press
               mouseX >= potX && mouseX <= potX + potTexture.getWidth() &&
               mouseY >= potY && mouseY <= potY + potTexture.getHeight();
    }

    private boolean isOverShelf(float mouseX, float mouseY) {
        return mouseX >= 0 && mouseX <= Gdx.graphics.getWidth() && // Check within screen bounds
               mouseY >= shelfStartY && mouseY <= shelfStartY + shelfHeight;
    }

    public boolean isPotClicked(float clickX, float clickY) {
        return Gdx.input.isButtonPressed(Input.Buttons.LEFT) && // Check for left mouse button press
               clickX >= potX && clickX <= potX + potTexture.getWidth() &&
               clickY >= potY && clickY <= potY + potTexture.getHeight();
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
