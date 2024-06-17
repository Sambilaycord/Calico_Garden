package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
    private boolean isDragging;
    private boolean isResizing;
    private int draggingIndex;
    private int resizingIndex;
    private float initialDistance;

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
            this.lockPositions[i] = new Rectangle(lockPositionsArray[i][0], lockPositionsArray[i][1], lockPositionsArray[i][2] - lockPositionsArray[i][1], 100);
        }

        // Initialize isLocked array
        isLocked = new boolean[plants.length];
        isDragging = false;
        isResizing = false;
        draggingIndex = -1;
        resizingIndex = -1;
        initialDistance = 0;

        // Load the state if available
        loadState();
    }

    public void update(float delta, int x, int y, boolean isTouched, boolean isSecondTouch, int secondX, int secondY, OrthographicCamera camera) {
        Vector3 cursorPos = new Vector3(x, y, 0);
        Vector3 secondCursorPos = new Vector3(secondX, secondY, 0);
        camera.unproject(cursorPos);
        camera.unproject(secondCursorPos);

        if (isTouched && isSecondTouch) {
            float distance = cursorPos.dst(secondCursorPos);

            if (!isResizing) {
                for (int i = 0; i < plants.length; i++) {
                    if (plantBounds[i].contains(cursorPos.x, cursorPos.y) || plantBounds[i].contains(secondCursorPos.x, secondCursorPos.y)) {
                        isResizing = true;
                        resizingIndex = i;
                        initialDistance = distance;
                        break;
                    }
                }
            } else {
                float scale = distance / initialDistance;
                plantBounds[resizingIndex].width = 100 * scale;
                plantBounds[resizingIndex].height = 100 * scale;
            }
        } else if (isTouched) {
            if (!isDragging) {
                for (int i = 0; i < plants.length; i++) {
                    if (plantBounds[i].contains(cursorPos.x, cursorPos.y)) {
                        isDragging = true;
                        draggingIndex = i;
                        break;
                    }
                }
            } else {
                plantBounds[draggingIndex].x = cursorPos.x - plantBounds[draggingIndex].width / 2;
                plantBounds[draggingIndex].y = cursorPos.y - plantBounds[draggingIndex].height / 2;
            }
        } else {
            if (isDragging) {
                // Check for snapping to lock positions
                boolean snapped = false;
                for (Rectangle lockPosition : lockPositions) {
                    if (lockPosition.contains(plantBounds[draggingIndex].x, plantBounds[draggingIndex].y)) {
                        plantBounds[draggingIndex].x = Math.max(lockPosition.x, Math.min(plantBounds[draggingIndex].x, lockPosition.x + lockPosition.width - plantBounds[draggingIndex].width));
                        plantBounds[draggingIndex].y = lockPosition.y;
                        isLocked[draggingIndex] = true;
                        snapped = true;
                        break;
                    }
                }
                if (!snapped) {
                    isLocked[draggingIndex] = false;
                }
                isDragging = false;
                draggingIndex = -1;
            }

            if (isResizing) {
                isResizing = false;
                resizingIndex = -1;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < plants.length; i++) {
            // Draw plants based on plantBounds positions
            batch.draw(plants[i].getTexture(), plantBounds[i].x, plantBounds[i].y, plantBounds[i].width, plantBounds[i].height);
        }
    }

    public void saveState() {
        Preferences prefs = Gdx.app.getPreferences("PlantState");

        for (int i = 0; i < plants.length; i++) {
            prefs.putFloat("plant_" + i + "_x", plantBounds[i].x);
            prefs.putFloat("plant_" + i + "_y", plantBounds[i].y);
            prefs.putFloat("plant_" + i + "_width", plantBounds[i].width);
            prefs.putFloat("plant_" + i + "_height", plantBounds[i].height);
            prefs.putBoolean("plant_" + i + "_isLocked", isLocked[i]);
        }

        prefs.flush(); // This writes the changes to the file
    }

    public void loadState() {
        Preferences prefs = Gdx.app.getPreferences("PlantState");

        for (int i = 0; i < plants.length; i++) {
            plantBounds[i].x = prefs.getFloat("plant_" + i + "_x", plantBounds[i].x);
            plantBounds[i].y = prefs.getFloat("plant_" + i + "_y", plantBounds[i].y);
            plantBounds[i].width = prefs.getFloat("plant_" + i + "_width", plantBounds[i].width);
            plantBounds[i].height = prefs.getFloat("plant_" + i + "_height", plantBounds[i].height);
            isLocked[i] = prefs.getBoolean("plant_" + i + "_isLocked", isLocked[i]);
        }
    }

    @Override
    public void dispose() {
        // Dispose resources if any
    }
}
