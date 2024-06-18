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

    // Add these fields to store the original sizes of the plants
    private float[] originalWidths;
    private float[] originalHeights;

    public ShelfSystem(Plant[] plants) {
        this.plants = plants;

        // Initialize plantBounds based on the number of plants
        plantBounds = new Rectangle[plants.length];
        originalWidths = new float[plants.length];
        originalHeights = new float[plants.length];
        for (int i = 0; i < plants.length; i++) {
            Texture plantTexture = plants[i].getTexture(); 
            float plantWidth = plantTexture.getWidth(); 
            float plantHeight = plantTexture.getHeight();
            plantBounds[i] = new Rectangle(650, 50, plantWidth, plantHeight);
            originalWidths[i] = plantWidth;
            originalHeights[i] = plantHeight;
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

    public void update(float delta, int x, int y, boolean isTouched, boolean isSecondTouch, int secondX, int secondY, OrthographicCamera camera, Rectangle sellAreaBounds, CalicoGarden game) {
        Vector3 cursorPos = new Vector3(x, y, 0);
        Vector3 secondCursorPos = new Vector3(secondX, secondY, 0);
        camera.unproject(cursorPos);
        camera.unproject(secondCursorPos);

        if (isDragging) {
            if (sellAreaBounds.contains(cursorPos.x, cursorPos.y)) {
                sellPlant(draggingIndex, game);
                isDragging = false;
                draggingIndex = -1;
            } else {
                int closestLockIndex = getClosestLockPosition(cursorPos.y);
                float[] closestLock = lockPositionsArray[closestLockIndex];

                plantBounds[draggingIndex].x = Math.max(closestLock[1], Math.min(cursorPos.x - plantBounds[draggingIndex].width / 2, closestLock[2] - plantBounds[draggingIndex].width));
                plantBounds[draggingIndex].y = closestLock[0];
                isLocked[draggingIndex] = true;
            }
        }

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
                plantBounds[resizingIndex].width = originalWidths[resizingIndex] * scale;
                plantBounds[resizingIndex].height = originalHeights[resizingIndex] * scale;
                System.out.println("Resizing plant_" + resizingIndex + " to width: " + plantBounds[resizingIndex].width + " and height: " + plantBounds[resizingIndex].height);
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
                plantBounds[draggingIndex].x = cursorPos.x - plantBounds[draggingIndex].width / 2;
                plantBounds[draggingIndex].y = cursorPos.y - plantBounds[draggingIndex].height / 2;
                System.out.println("Dragging plant_" + draggingIndex + " to x: " + plantBounds[draggingIndex].x + " and y: " + plantBounds[draggingIndex].y);
            }
        } else {
            if (isDragging) {
                if (sellAreaBounds.contains(cursorPos.x, cursorPos.y)) {
                    game.getShelfSystem().sellPlant(draggingIndex, game);
                } else {
                    int closestLockIndex = getClosestLockPosition(cursorPos.y);
                    float[] closestLock = lockPositionsArray[closestLockIndex];

                    plantBounds[draggingIndex].x = Math.max(closestLock[1], Math.min(cursorPos.x - plantBounds[draggingIndex].width / 2, closestLock[2] - plantBounds[draggingIndex].width));
                    plantBounds[draggingIndex].y = closestLock[0];
                    isLocked[draggingIndex] = true;
                }
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
            if (i == 0) {
                // First plant
                batch.draw(plants[i].getTexture(), plantBounds[i].x, plantBounds[i].y, plantBounds[i].width * 2, plantBounds[i].height * 2);
                // Scale the rectangle for the first plant
                plantBounds[i].width = originalWidths[i] * 2;
                plantBounds[i].height = originalHeights[i] * 2;
            } else {
                // Second and subsequent plants
                batch.draw(plants[i].getTexture(), plantBounds[i].x, plantBounds[i].y, (plantBounds[i].width), (plantBounds[i].height));
                // Scale the rectangles for the second and subsequent plants
                plantBounds[i].width = (originalWidths[i] / 10) * 2;
                plantBounds[i].height = (originalHeights[i] / 10) * 2;
            }
        }
    }

    public void saveState() {
        Preferences prefs = Gdx.app.getPreferences("PlantState");

        // Clear the existing preferences before saving the new state
        prefs.clear();

        for (int i = 0; i < plants.length; i++) {
            prefs.putFloat("plant_" + i + "_x", plantBounds[i].x);
            prefs.putFloat("plant_" + i + "_y", plantBounds[i].y);
            prefs.putFloat("plant_" + i + "_width", plantBounds[i].width);
            prefs.putFloat("plant_" + i + "_height", plantBounds[i].height);
            prefs.putBoolean("plant_" + i + "_isLocked", isLocked[i]);

            System.out.println("Saving plant_" + i + " x: " + plantBounds[i].x + ", y: " + plantBounds[i].y + 
                               ", width: " + plantBounds[i].width + ", height: " + plantBounds[i].height);
        }

        prefs.flush();
        System.out.println("State Saved");
    }

    public void loadState() {
        Preferences prefs = Gdx.app.getPreferences("PlantState");

        for (int i = 0; i < plants.length; i++) {
            float x = prefs.getFloat("plant_" + i + "_x", plantBounds[i].x);
            float y = prefs.getFloat("plant_" + i + "_y", plantBounds[i].y);
            float width = prefs.getFloat("plant_" + i + "_width", plantBounds[i].width);
            float height = prefs.getFloat("plant_" + i + "_height", plantBounds[i].height);
            plantBounds[i] = new Rectangle(x, y, width, height);

            isLocked[i] = prefs.getBoolean("plant_" + i + "_isLocked", isLocked[i]);
            System.out.println("Loaded plant_" + i + " x: " + x + ", y: " + y + 
                               ", width: " + width + ", height: " + height);
        }
    }


    public float[][] getLockPositionsArray() {
        return lockPositionsArray;
    }

    public void resetState() {
        Preferences prefs = Gdx.app.getPreferences("PlantState");
        prefs.clear(); // This will remove all key-value pairs from the preferences
        prefs.flush();
        System.out.println("Preferences cleared.");

        // Reset plant positions and sizes to their original values
        for (int i = 0; i < plants.length; i++) {
            plantBounds[i].x = 650;
            plantBounds[i].y = 50;
            plantBounds[i].width = originalWidths[i];
            plantBounds[i].height = originalHeights[i];
            isLocked[i] = false;

            System.out.println("Reset plant_" + i + " to x: " + plantBounds[i].x + ", y: " + plantBounds[i].y + 
                               ", width: " + plantBounds[i].width + ", height: " + plantBounds[i].height);
        }

        saveState(); // Save the default state after resetting
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

    public void sellPlant(int plantIndex, CalicoGarden game) {
        if (plantIndex >= 0 && plantIndex < plants.length && game.getPlants().size() > plantIndex) {
            Plant plant = game.getPlants().get(plantIndex);
            int sellPrice = calculateSellPrice(plant);
            game.setCoins(game.getCoins() + sellPrice);
            game.removePlant(plantIndex);
            
            // Move the sold plant out of the screen
            plantBounds[plantIndex].x = -1000; // Move it outside the left boundary
            plantBounds[plantIndex].y = -1000; // Move it outside the bottom boundary
            
            System.out.println("Sold " + plant.getName() + " for " + sellPrice + " coins!");
        }
    }

    private int calculateSellPrice(Plant plant) {
        // Example logic: Calculate sell price based on plant details
        return 10; // Example sell price, replace with your logic
    }

    @Override
    public void dispose() {
        // Dispose resources if any
    }
}
