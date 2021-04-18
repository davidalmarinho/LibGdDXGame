package com.davidalmarinho.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class Menu implements Screen {
    private final Drop game;
    private final OrthographicCamera camera;
    private float time;

    public Menu(Drop game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();

        time += Gdx.graphics.getDeltaTime();

        game.batch.setProjectionMatrix(camera.combined);


        game.batch.begin();
        game.font.getData().setScale(2);
        game.font.draw(game.batch, "Welcome to this Game!!!",
                (float) (Constants.WIDTH / 2 - 160), (float) (Constants.HEIGHT / 2));
        if (time <= 1.0f) {
            game.font.getData().setScale(1);
            game.font.draw(game.batch, "Tap to continue",
                    (float) (Constants.WIDTH / 2) - 65, (float) (Constants.HEIGHT / 2 - 64));
        } else if (time >= 1.5f) {
            time = .0f;
        }
        game.batch.end();
        // Check touch / any key
        if (Gdx.input.isTouched()) {
            game.gameScreen = new GameScreen(game);
            game.setScreen(game.gameScreen);
            dispose();
        }
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
