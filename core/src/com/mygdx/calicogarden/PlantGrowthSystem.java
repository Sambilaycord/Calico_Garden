package com.mygdx.calicogarden;

public class PlantGrowthSystem {
    private Plant[] plants;
    private boolean isFullyGrown;
    private boolean isWatered;

    public PlantGrowthSystem() {
        this.isFullyGrown = false;
        this.isWatered = false;
    }

    public void newDay() {
        if (isWatered) {
            isFullyGrown = true;
            isWatered = false;
            System.out.println("The plant has grown!");
        } else {
            System.out.println("The plant needs water to grow.");
        }
    }

    public void waterPlant() {
        isWatered = true;
        System.out.println("The plant is watered!");
    }

    public boolean isFullyGrown() {
        return isFullyGrown;
    }

    public void resetGrowth() {
        isFullyGrown = false;
        isWatered = false;
    }
}
