package com.davidalmarinho.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOver implements Screen {
    private final Drop game;
    private float time;

    public GameOver(Drop game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    private void update() {
        time += Gdx.graphics.getDeltaTime();

        if (Gdx.input.isTouched()) {
            game.gameScreen = new GameScreen(game);
            game.setScreen(game.gameScreen);
        }
    }

    @Override
    public void render(float delta) {
        update();

        ScreenUtils.clear(0, 0, 0.2f, 1);

        game.batch.begin();
        game.batch.setColor(1.0f, 1.0f, 1.0f, 0.8f);
        game.font.draw(game.batch, "Congratulations, you have collected " + game.gameScreen.score + " drops!",
                (float) (Constants.WIDTH / 2 - 160), (float) (Constants.HEIGHT / 2));
        game.font.getData().setScale(1);
        if (time <= 1.0f) {
            game.font.getData().setScale(1);
            game.font.draw(game.batch, "Tap to play again",
                    (float) (Constants.WIDTH / 2) - 65, (float) (Constants.HEIGHT / 2 - 64));
        } else if (time >= 1.5f) {
            time = .0f;
        }
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
