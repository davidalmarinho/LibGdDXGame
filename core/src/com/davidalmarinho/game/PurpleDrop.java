package com.davidalmarinho.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class PurpleDrop {
    public Texture purpleDrop;
    public Rectangle position;

    public PurpleDrop() {
        purpleDrop = new Texture(Gdx.files.internal("purpleDroplet.png"));
        position = new Rectangle(MathUtils.random(0, Constants.WIDTH - 64), Constants.HEIGHT,
                Constants.TILE_SIZE, Constants.TILE_SIZE);
    }

    public void update() {
        position.y -= 200 * Gdx.graphics.getDeltaTime();
    }

    public void render(SpriteBatch batch) {
        batch.draw(purpleDrop, position.x, position.y);
    }
}
