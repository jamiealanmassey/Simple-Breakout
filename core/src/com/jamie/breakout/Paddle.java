package com.jamie.breakout;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Paddle extends GameObject {
	public Paddle(TextureRegion textureRegion, int screenWidth, int screenHeight) {
		super(textureRegion);
		
		this.coordX = (screenHeight / 2) - (textureRegion.getRegionWidth() / 2);
		this.coordY = (textureRegion.getRegionHeight() + 5);
		this.objectType = "paddle";
	}
}
