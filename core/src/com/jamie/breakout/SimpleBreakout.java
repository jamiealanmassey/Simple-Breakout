package com.jamie.breakout;

import java.util.ArrayList;

import org.omg.CosNaming._BindingIteratorImplBase;

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
		
		ball = new Ball(new TextureRegion(gameSprites, 48, 136, 8, 8), screenWidth, screenHeight);
		paddle = new Paddle(new TextureRegion(gameSprites, 8, 151, 64, 20), screenWidth, screenHeight);
		
		GameObject barrier1 = new GameObject(new TextureRegion(gameSprites, 93, 159, 4, 4));
		GameObject barrier2 = new GameObject(new TextureRegion(gameSprites, 93, 159, 4, 4));
		GameObject barrier3 = new GameObject(new TextureRegion(gameSprites, 93, 159, 4, 4));
		GameObject barrier4 = new GameObject(new TextureRegion(gameSprites, 93, 159, 4, 4));
		
		barrier1.MoveTo(-2, 0);
		barrier2.MoveTo(0, 0);
		barrier3.MoveTo(0, screenHeight - 4);
		barrier4.MoveTo(screenWidth - 4, 0);
		
		barrier1.SetDimensions(screenWidth + 4, 4);
		barrier2.SetDimensions(4, screenHeight + 4);
		barrier3.SetDimensions(screenWidth + 4, 4);
		barrier4.SetDimensions(4, screenHeight + 4);
		
		gameObjects.add(barrier1);
		gameObjects.add(barrier2);
		gameObjects.add(barrier3);
		gameObjects.add(barrier4);
		
		for (int i = 0; i < 5; i++) {
			for (int x = 0; x < 5; x++) {
				GameObject block = new GameObject(new TextureRegion(gameSprites, 48, 72, 64, 16));
				block.coordX = (screenWidth / 2) - 32 - ((x - 2) * 100);
				block.coordY = (screenHeight / 2) + (i * 60);
				gameObjects.add(block);
			}
		}
		
		/*GameObject block = new GameObject(new TextureRegion(gameSprites, 48, 72, 64, 16));
		block.coordX = (screenWidth / 2) + 70 - 10;
		block.coordY = (screenHeight / 2) + 60;
		gameObjects.add(block);*/
		
		gameObjects.add(ball);
		gameObjects.add(paddle);
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean mouseMoved(int x, int y) {
				paddle.MoveTo(x - (paddle.GetWidth() / 2), paddle.GetCoordY()/*(screenHeight - y) - (paddle.GetHeight() / 2)*/);
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
