/*
 * Copyright © 2024 Devin B. Royal. All Rights Reserved.
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture squareTexture;
    private Rectangle squareBounds;
    private Vector2 screenSize;

    @Override
    public void create() {
        batch = new SpriteBatch();
        squareTexture = new Texture("square.png");
        screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float squareSize = Math.min(screenSize.x, screenSize.y) / 4;
        squareBounds = new Rectangle((screenSize.x - squareSize) / 2, (screenSize.y - squareSize) / 2, squareSize, squareSize);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 1, 0, 1); // Green background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        batch.draw(squareTexture, squareBounds.x, squareBounds.y, squareBounds.width, squareBounds.height);
        batch.end();

        if (Gdx.input.isTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            if (squareBounds.contains(touchPos.x, screenSize.y - touchPos.y)) {
                // Handle square click
                System.out.println("Square clicked!");
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        squareTexture.dispose();
    }
}
