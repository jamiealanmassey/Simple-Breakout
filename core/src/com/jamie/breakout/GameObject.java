package com.jamie.breakout;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.Vector3;

public class GameObject {
	protected TextureRegion textureRegion;
	protected String objectType;
	protected float coordX;
	protected float coordY;
	protected float width;
	protected float height;
	
	public GameObject(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
		this.coordX = 0.0f;
		this.coordY = 0.0f;
		this.width = textureRegion.getRegionWidth();
		this.height = textureRegion.getRegionHeight();
		this.objectType = "undefined";
	}
	
	public void Draw(SpriteBatch batch) {
		batch.draw(textureRegion, coordX, coordY, width, height);
	}
	
	public void Update(ArrayList<GameObject> gameObjects) {
		
	}
	
	public void Move(float coordX, float coordY) {
		this.coordX += coordX;
		this.coordY += coordY;
	}
	
	public void MoveTo(float coordX, float coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}
	
	public void SetDimensions(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public boolean Overlaps(GameObject other) {
		return GetAABB().intersects(other.GetAABB());
	}
	
	public boolean Inside(int coordX, int coordY) {
		return GetAABB().contains(new Vector3(coordX, coordY, 0));
	}
	
	public BoundingBox GetAABB() {
		Vector3 min = new Vector3(coordX, coordY, 0);
		Vector3 max = new Vector3(coordX + GetWidth(), coordY + GetHeight(), 0);
		return new BoundingBox(min, max);
	}
	
	public String GetObjectType() {
		return objectType;
	}
	
	public float GetCoordX() {
		return coordX;
	}
	
	public float GetCoordY() {
		return coordY;
	}
	
	public float GetWidth() {
		return width;
	}
	
	public float GetHeight() {
		return height;
	}
}
