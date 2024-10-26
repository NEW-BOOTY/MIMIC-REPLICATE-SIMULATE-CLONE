/*
 * Copyright © 2024 Devin B. Royal. All rights reserved.
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
        try {
            batch = new SpriteBatch();
            squareTexture = new Texture("square.png");
            screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            float squareSize = Math.min(screenSize.x, screenSize.y) / 4;
            squareBounds = new Rectangle((screenSize.x - squareSize) / 2, (screenSize.y - squareSize) / 2, squareSize, squareSize);
        } catch (Exception e) {
            System.err.println("Error during creation: " + e.getMessage());
        }
    }

    @Override
    public void render() {
        try {
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
        } catch (Exception e) {
            System.err.println("Error during rendering: " + e.getMessage());
        }
    }

    @Override
    public void dispose() {
        try {
            if (batch != null) batch.dispose();
            if (squareTexture != null) squareTexture.dispose();
        } catch (Exception e) {
            System.err.println("Error during disposal: " + e.getMessage());
        }
    }
}

/*
 * This Java program is a simple game application using the LibGDX framework.
 * It initializes a square texture and displays it on the screen.
 * The program handles user input to detect clicks on the square and prints a message when the square is clicked.
 * Error handling is added to manage potential exceptions during the creation, rendering, and disposal phases.
 * The code is ready to be compiled using javac.
 */
