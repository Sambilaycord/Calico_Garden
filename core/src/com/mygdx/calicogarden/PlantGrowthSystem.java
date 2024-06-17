package com.mygdx.calicogarden;

public class PlantGrowthSystem {

    private boolean isWatered;
    private int growthStage;
    private float elapsedTime;
    private final float hourDuration;
    private final float dayDuration;
    private final int totalGrowthStages;
    private final int basePrice; // Base price of the plant

    public PlantGrowthSystem() {
        this.isWatered = false;
        this.growthStage = 0;
        this.elapsedTime = 0;
        this.hourDuration = 10; // 1 hour in game time (10 seconds real time)
        this.dayDuration = 24 * hourDuration; // 24 hours in game time
        this.totalGrowthStages = 4; // Define total growth stages
        this.basePrice = 10; // Example base price (can be adjusted)
    }

    public void update(float delta) {
        elapsedTime += delta;

        if (isWatered && elapsedTime >= dayDuration / totalGrowthStages) {
            growthStage = Math.min(growthStage + 1, totalGrowthStages);
            elapsedTime = 0;
        }

        if (elapsedTime >= dayDuration) {
            resetGrowth();
        }
    }

    public void waterPlant() {
        isWatered = true;
    }

    public void resetGrowth() {
        elapsedTime = 0;
        growthStage = 0;
        isWatered = false;
    }

    public int getGrowthStage() {
        return growthStage;
    }

    public boolean isFullyGrown() {
        return growthStage == totalGrowthStages;
    }

    public int calculateSellingPrice() {
        // Only allow selling when fully grown
        if (isFullyGrown()) {
            // Example: Selling price increases with growth stage
            // Adjust this logic based on your game's economy and balance
            return basePrice * growthStage;
        } else {
            // Return 0 if not fully grown (can't sell yet)
            return 0;
        }
    }
}
