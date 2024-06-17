package com.mygdx.calicogarden;

import com.badlogic.gdx.graphics.Texture;

public class Plant {
    private float x;
    private float y;
    private float width;
    private float height;
    private String name;
    private Texture texture;
    private int price;

    public Plant(String name, Texture texture, int price) {
        this.name = name;
        this.texture = texture;
        this.price = price;
        this.width = texture.getWidth(); // Initialize width and height based on texture size
        this.height = texture.getHeight();
    }

    public Plant(String name, Texture texture, float x, float y, int width, int height, int price) {
        this.name = name;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.price = price;
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        this.width = texture.getWidth(); // Update width and height when setting new texture
        this.height = texture.getHeight();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
