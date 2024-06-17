package com.mygdx.calicogarden;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class ShelfSystem implements Disposable {

    private Plant[] plants;
    private Rectangle[] plantBounds;
    private Rectangle[] lockPositions;
    private boolean[] isLocked;

    public ShelfSystem(Plant[] plants) {
        this.plants = plants;

        // Initialize plantBounds based on the number of plants
        plantBounds = new Rectangle[plants.length];
        for (int i = 0; i < plants.length; i++) {
            plantBounds[i] = new Rectangle(100 + i * 120, 300, 100, 100);
        }

        // Initialize lock positions
        float[][] lockPositionsArray = {
            {50f, 450f, 1100f}, 
            {250f, 350f, 1340f}, 
            {460f, 450f, 1300f}, 
            {625f, 650f, 1200f} 
        };
        this.lockPositions = new Rectangle[lockPositionsArray.length];
        for (int i = 0; i < lockPositionsArray.length; i++) {
            this.lockPositions[i] = new Rectangle(lockPositionsArray[i][0], lockPositionsArray[i][1], 100, 100);
        }

        // Initialize isLocked array
        isLocked = new boolean[plants.length];
    }

    public void update(float delta, int x, int y, boolean isTouched, OrthographicCamera camera) {
        Vector3 cursorPos = new Vector3(x, y, 0);
        camera.unproject(cursorPos);

        for (int i = 0; i < plants.length; i++) {
            if (plantBounds[i].contains(cursorPos.x, cursorPos.y) && isTouched) {
                plantBounds[i].x = cursorPos.x - plantBounds[i].width / 2;
                plantBounds[i].y = cursorPos.y - plantBounds[i].height / 2;
            }

            // Check if plant is within any lock position
            for (Rectangle lockPosition : lockPositions) {
                if (plantBounds[i].overlaps(lockPosition)) {
                    plantBounds[i].x = lockPosition.x;
                    plantBounds[i].y = lockPosition.y;
                    isLocked[i] = true;
                    break;
                } else {
                    isLocked[i] = false;
                }
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < plants.length; i++) {
            // Draw plants based on plantBounds positions
            batch.draw(plants[i].getTexture(), plantBounds[i].x, plantBounds[i].y, plantBounds[i].width, plantBounds[i].height);
        }
    }

    @Override
    public void dispose() {
        // Dispose resources if any
    }
}
