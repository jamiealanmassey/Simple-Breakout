package com.jamie.breakout;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class Ball extends GameObject {
	private Vector3 velocity;
	private float speed;
	private float radius;
	
	private static final float EPSILON_BIAS = 0.0001f;
	
	public Ball(TextureRegion textureRegion, int screenWidth, int screenHeight) {
		super(textureRegion);
		
		this.coordX = (screenWidth / 2) - (textureRegion.getRegionWidth() / 2);
		this.coordY = (textureRegion.getRegionHeight() + 150);
		this.velocity = new Vector3();
		this.velocity.x = 0.5f;
		this.velocity.y = 1.0f;
		this.velocity.z = 0.0f;
		this.velocity.nor();
		this.speed = 50.0f;
		this.radius = (textureRegion.getRegionWidth() / 2.0f);
	}
	
	@Override
	public void Update(ArrayList<GameObject> gameObjects) {
		Vector3 origin = new Vector3(coordX + radius, coordY + radius, 0);
		Ray raycast = new Ray(origin, velocity);
		
		Vector3 debugPoint = Vector3.Zero;
		raycast.getEndPoint(debugPoint, 500);
		DebugHelper.DrawDebugLine(new Vector2(coordX + radius, coordY + radius), new Vector2(debugPoint.x, debugPoint.y), null);
		
		for (GameObject gameObject : gameObjects) {
			if (gameObject == this)
				continue;
			
			// Step 1: Calculate extended AABB, perform quick intersection to see if we are within the area
			BoundingBox bounding = gameObject.GetAABB();
			BoundingBox boundingE = new BoundingBox(bounding);
			Vector3 intersection = Vector3.Zero;
			Vector3 minimumE = new Vector3(boundingE.min.x - radius, boundingE.min.y - radius, 0);
			Vector3 maximumE = new Vector3(boundingE.max.x + radius, boundingE.max.y + radius, 0);
			boundingE.set(minimumE, maximumE);
			
			byte region = IntersectRayAABB(raycast, boundingE, intersection);
			
			if (region == 0)
				continue;
			
			// Step 2: If point is inside face voronoi we are done, intersection happened, otherwise we have to check corners
			if (region == (1 << 3)) {
				if (intersection.y >= bounding.min.y -EPSILON_BIAS && intersection.y <= bounding.max.y +EPSILON_BIAS)
					HandleCollision(intersection, region);
				else if(intersection.y < bounding.min.y) {
					if (IntersectRayCircle(raycast, bounding.min, radius, intersection))
						HandleCollision(intersection, region);
				}
				else {
					if (IntersectRayCircle(raycast, new Vector3(bounding.min.x, bounding.max.y, 0), radius, intersection))
						HandleCollision(intersection, region);
				}
			}
			else if(region == (1 << 1)) {
				if (intersection.y >= bounding.min.y -EPSILON_BIAS && intersection.y <= bounding.max.y +EPSILON_BIAS)
					HandleCollision(intersection, region);
				else if(intersection.y < bounding.min.y) {
					if (IntersectRayCircle(raycast, new Vector3(bounding.max.x, bounding.min.y, 0), radius, intersection))
						HandleCollision(intersection, region);
				}
				else {
					if (IntersectRayCircle(raycast, bounding.max, radius, intersection))
						HandleCollision(intersection, region);
				}
			}
			else if(region == (1 << 2)) {
				if (intersection.x >= bounding.min.x -EPSILON_BIAS && intersection.x <= bounding.max.x +EPSILON_BIAS)
					HandleCollision(intersection, region);
				else if(intersection.x < bounding.min.x) {
					if (IntersectRayCircle(raycast, bounding.min, radius, intersection))
						HandleCollision(intersection, region);
				}
				else {
					if (IntersectRayCircle(raycast, new Vector3(bounding.max.x, bounding.min.y, 0), radius, intersection))
						HandleCollision(intersection, region);
				}		
			}
			else if(region == (1 << 0)) {
				if (intersection.x >= bounding.min.x -EPSILON_BIAS && intersection.x <= bounding.max.x +EPSILON_BIAS)
					HandleCollision(intersection, region);
				else if(intersection.x < bounding.min.x) {
					if (IntersectRayCircle(raycast, new Vector3(bounding.min.x, bounding.max.y, 0), radius, intersection))
						HandleCollision(intersection, region);
				}
				else {
					if (IntersectRayCircle(raycast, bounding.max, radius, intersection))
						HandleCollision(intersection, region);
				}	
			}
		}
		
		this.Move((speed * velocity.x * Gdx.graphics.getDeltaTime()), 
				  (speed * velocity.y * Gdx.graphics.getDeltaTime()));
	}
	
	private void HandleCollision(Vector3 intersection, byte region) {
		this.MoveTo(intersection.x, intersection.y);
		
		if (region == (1 << 3) || region == (1 << 1))
			velocity.x *= -1;
		else if (region == (1 << 2) || region == (1 << 0))
			velocity.y *= -1;
	}
	
	private byte IntersectRayAABB(Ray raycast, BoundingBox boundingBox, Vector3 intersection) {
		float delta = speed * Gdx.graphics.getDeltaTime();
		Vector3 endpoint = Vector3.Zero;
		Vector3 origin = raycast.origin;
		raycast.getEndPoint(endpoint, delta * 2.0f);
		
		byte originRegion = GetCohenSoutherland(origin, boundingBox);
		byte endpointRegion = GetCohenSoutherland(endpoint, boundingBox);
		
		if ((originRegion & endpointRegion) != 0)
			return 0;
	
		if ((originRegion & (1 << 3)) != 0) {
			float t = (boundingBox.min.x - origin.x) / (raycast.direction.x);
			
			if (t >= -EPSILON_BIAS && t <= delta +EPSILON_BIAS) {
				float y = origin.y + (raycast.direction.y * t);
				
				if (y >= boundingBox.min.y && y <= boundingBox.max.y) {
					intersection = new Vector3(boundingBox.min.x, y, 0);
					return (1 << 3);
				}
			}
		}
		else if ((originRegion & (1 << 1)) != 0) {
			float t = (boundingBox.max.x - origin.x) / (raycast.direction.x);
			
			if (t >= -EPSILON_BIAS && t <= delta +EPSILON_BIAS) {
				float y = origin.y + (raycast.direction.y * t);
				
				if (y >= boundingBox.min.y && y <= boundingBox.max.y) {
					intersection = new Vector3(boundingBox.max.x, y, 0);
					return (1 << 1);
				}
			}
		}
		
		if ((originRegion & (1 << 2)) != 0) {
			float t = (boundingBox.min.y - origin.y) / (raycast.direction.y);
			
			if (t >= -EPSILON_BIAS && t <= delta +EPSILON_BIAS) {
				float x = origin.x + (raycast.direction.x * t);
				
				if (x >= boundingBox.min.x && x <= boundingBox.max.x) {
					intersection = new Vector3(x, boundingBox.min.y, 0);
					return (1 << 2);
				}
			}
		}
		else if ((originRegion & (1 << 0)) != 0) {
			float t = (boundingBox.max.y - origin.y) / (raycast.direction.y);
			
			if (t >= -EPSILON_BIAS && t <= delta +EPSILON_BIAS) {
				float x = origin.x + (raycast.direction.x * t);
				
				if (x >= boundingBox.min.x && x <= boundingBox.max.x) {
					intersection = new Vector3(x, boundingBox.max.y, 0);
					return (1 << 0);
				}
			}
		}
		
		return 0;
	}
	
	private boolean IntersectRayCircle(Ray raycast, Vector3 centre, float radius, Vector3 intersection) {
		Vector3 deltaCentre = new Vector3(raycast.origin);
		Vector3 direction = new Vector3(raycast.direction).scl(speed);
		deltaCentre.sub(centre);
		
		float dot1 = new Vector3(deltaCentre).dot(direction);
		float dot2 = new Vector3(direction).dot(direction);
		float dot3 = new Vector3(deltaCentre).dot(deltaCentre);
		float discriminant = ((dot1 * dot1) - (dot3) + (radius * radius)) / dot2;
		
		if (discriminant < 0)
			return false;
		
		float t = (-dot1 / dot3) - (float)Math.sqrt(discriminant);
		
		if(t > Gdx.graphics.getDeltaTime() -EPSILON_BIAS)
			return false;
		
		intersection = new Vector3(direction).scl(t).add(raycast.origin);
		return true;
	}
	
	private byte GetCohenSoutherland(Vector3 point, BoundingBox boundingBox) {
		byte region = 0;
		
		if (point.x <= boundingBox.min.x)
			region |= (1 << 3);
		else if (point.x >= boundingBox.max.x)
			region |= (1 << 1);
		
		if (point.y <= boundingBox.min.y)
			region |= (1 << 2);
		else if (point.y >= boundingBox.max.y)
			region |= (1 << 0);
		
		return region;
	}
}
