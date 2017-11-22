package com.jamie.breakout;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SimpleBreakout extends ApplicationAdapter {
	private SpriteBatch gameBatch;
	private Texture gameSprites;
	
	private int screenWidth;
	private int screenHeight;
	
	private Ball ball;
	private Paddle paddle;
	
	private ArrayList<GameObject> gameObjects;
	
	@Override
	public void create () {
		gameBatch = new SpriteBatch();
		gameSprites = new Texture("breakout_pieces_1.png");
		gameObjects = new ArrayList<GameObject>();
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		ball = new Ball(new TextureRegion(gameSprites, 48,  136, 8, 8), screenWidth, screenHeight);
		paddle = new Paddle(new TextureRegion(gameSprites, 8, 151, 64, 20), screenWidth, screenHeight);
		
		/*for (int i = 0; i < 5; i++) {
			for (int x = 0; x < 5; x++) {
				GameObject block = new GameObject(new TextureRegion(gameSprites, 48, 72, 64, 16));
				block.coordX = (screenWidth / 2) - 32 - ((x - 2) * 100);
				block.coordY = (screenHeight / 2) + (i * 60);
				gameObjects.add(block);
			}
		}*/
		
		GameObject block = new GameObject(new TextureRegion(gameSprites, 48, 72, 64, 16));
		block.coordX = (screenWidth / 2) + 70;
		block.coordY = (screenHeight / 2) + 60;
		gameObjects.add(block);
		
		gameObjects.add(ball);
		gameObjects.add(paddle);
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean mouseMoved(int x, int y) {
				paddle.MoveTo(x - (paddle.GetWidth() / 2), (screenHeight - y) - (paddle.GetHeight() / 2));
				return true;
			}
		});
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		ball.Update(gameObjects);
		paddle.Update(gameObjects);
		
		gameBatch.begin();
		
		for (GameObject gameObject : gameObjects) {
			gameObject.Draw(gameBatch);
		}
		
		gameBatch.end();
	}
	
	@Override
	public void dispose () {
		gameBatch.dispose();
		gameSprites.dispose();
	}
}
