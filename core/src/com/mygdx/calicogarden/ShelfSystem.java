package com.mygdx.calicogarden;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class ShelfSystem implements Disposable {

    private Plant[] plants;
    private Rectangle[] plantBounds;
    private float[][] lockPositionsArray;
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
            Texture plantTexture = plants[i].getTexture(); 
            float plantWidth = plantTexture.getWidth(); 
            float plantHeight = plantTexture.getHeight();
            plantBounds[i] = new Rectangle(650, 50, plantWidth, plantHeight); // Assuming plants[i] has getHeight()
        }

        // Initialize lock positions array
        lockPositionsArray = new float[][] {
            {50f, 450f, 1100f}, 
            {250f, 350f, 1340f}, 
            {460f, 450f, 1300f}, 
            {625f, 650f, 1200f} 
        };

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
                // Apply offsets here
                plantBounds[draggingIndex].x = cursorPos.x - plantBounds[draggingIndex].width / 2 ;
                plantBounds[draggingIndex].y = cursorPos.y - plantBounds[draggingIndex].height / 2 ;
            }
        } else {
            if (isDragging) {
                int closestLockIndex = getClosestLockPosition(cursorPos.y);
                float[] closestLock = lockPositionsArray[closestLockIndex];

                plantBounds[draggingIndex].x = Math.max(closestLock[1], Math.min(cursorPos.x - plantBounds[draggingIndex].width / 2 , closestLock[2] - plantBounds[draggingIndex].width));
                plantBounds[draggingIndex].y = closestLock[0];
                isLocked[draggingIndex] = true;

                isDragging = false;
                draggingIndex = -1;
            }
        }
    }

    private int getClosestLockPosition(float y) {
        int closestIndex = 0;
        float closestDistance = Math.abs(lockPositionsArray[0][0] - y); // Initialize with first lock

        for (int i = 1; i < lockPositionsArray.length; i++) {
            float distance = Math.abs(lockPositionsArray[i][0] - y);
            if (distance < closestDistance) {
                closestIndex = i;
                closestDistance = distance;
            }
        }

        return closestIndex;
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < plants.length; i++) {
            // Draw plants based on plantBounds positions
            batch.draw(plants[i].getTexture(), plantBounds[i].x, plantBounds[i].y, plantBounds[i].width * 2.5f, plantBounds[i].height * 2.5f);
        }//batch.draw(plants[i].getTexture(), 650f, 50f, plantBounds[i].width * 2.5f, plantBounds[i].height * 2.5f);
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

    public void setPlantSize(int index, float width, float height) {
        if (index >= 0 && index < plantBounds.length) {
            plantBounds[index].width = width;
            plantBounds[index].height = height;
        }
    }

    public void setPlantPosition(int index, float x, float y) {
        if (index >= 0 && index < plantBounds.length) {
            plantBounds[index].x = x;
            plantBounds[index].y = y;
        }
    }
    

    @Override
    public void dispose() {
        // Dispose resources if any
    }
    
}
