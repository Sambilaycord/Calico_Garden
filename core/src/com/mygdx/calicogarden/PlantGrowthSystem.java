package com.mygdx.calicogarden;

public class PlantGrowthSystem {
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
            System.out.println("BONER TIME!");
        }
        else {
            System.out.println("cringe no grow");
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
