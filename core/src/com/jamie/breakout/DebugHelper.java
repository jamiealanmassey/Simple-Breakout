package com.jamie.breakout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class DebugHelper {
	public static ShapeRenderer debugRender = new ShapeRenderer();
	
	public static void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        //debugRender.setProjectionMatrix(projectionMatrix);
        debugRender.begin(ShapeRenderer.ShapeType.Line);
        debugRender.setColor(color);
        debugRender.line(start, end);
        debugRender.end();
        Gdx.gl.glLineWidth(1);
    }

    public static void DrawDebugLine(Vector2 start, Vector2 end, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(2);
        //debugRender.setProjectionMatrix(projectionMatrix);
        debugRender.begin(ShapeRenderer.ShapeType.Line);
        debugRender.setColor(Color.WHITE);
        debugRender.line(start, end);
        debugRender.end();
        Gdx.gl.glLineWidth(1);
    }
}
