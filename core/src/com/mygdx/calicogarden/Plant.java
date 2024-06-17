package com.mygdx.calicogarden;

import com.badlogic.gdx.graphics.Texture;

public class Plant {
    private String name;
    private Texture texture;
    private int price;
    private boolean isOnShelf;

    public Plant(String name, Texture texture, int price) {
        this.name = name;
        this.texture = texture;
        this.price = price;
        this.isOnShelf = false;
    }

    public String getName() {
        return name;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getPrice() {
        return price;
    }

    public void setOnShelf(boolean onShelf) {
        isOnShelf = onShelf;
    }
}
