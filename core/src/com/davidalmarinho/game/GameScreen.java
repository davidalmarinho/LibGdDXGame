package com.davidalmarinho.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    private final Texture dropImage;
    private final Texture bucketImage;
    private final Sound dropSound;
    private final Music rainMusic;
    private final OrthographicCamera camera;
    private final Rectangle bucket;
    private final Array<Rectangle> raindrops;
    private long lastDropTime;
    public int score;
    private boolean lockingCamera;
    private final Drop game;
    private PurpleDrop purpleDrop;
    public boolean gameOver;

    public GameScreen(Drop game) {
        this.game = game;

        // Create a "purple rain"
        purpleDrop = new PurpleDrop();

        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // Detect OS
		/*if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
			// Change window's size
			Gdx.graphics.setWindowedMode(300, 300);
		}*/

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);

        // create a Rectangle to logically represent the bucket
        bucket = new Rectangle();
        bucket.x = (float) (Constants.WIDTH / 2) - (float) (Constants.TILE_SIZE / 2); // center the bucket horizontally
        bucket.y = 20; // bottom left corner of the bucket is 20 pixels above the bottom screen edge
        bucket.width = Constants.TILE_SIZE;
        bucket.height = Constants.TILE_SIZE;

        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<>();
        spawnRaindrop();
    }

    private void update() {
        // make sure the bucket stays within the screen bounds
        if (bucket.x < 0) bucket.x = 0;
        if (bucket.x > Constants.WIDTH - Constants.TILE_SIZE)
            bucket.x = Constants.WIDTH - Constants.TILE_SIZE;

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the latter case we play back
        // a sound effect as well.
        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + Constants.TILE_SIZE < 0) iter.remove();
            if (raindrop.overlaps(bucket)) {
                dropSound.play();
                iter.remove();
                score++;
            }
        }

        purpleDrop.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) lockingCamera = !lockingCamera;

        if (lockingCamera) lockCamera();

        // Game Over
        if (purpleDrop.position.overlaps(bucket)) {
            // Delete rain, because we have a collision with thee bucket
            purpleDrop = new PurpleDrop();
            dropSound.stop();
            rainMusic.stop();
            gameOver = true;
        }

        if (gameOver) {
            gameOver = false;
            game.gameOver = new GameOver(game);
            game.setScreen(game.gameOver);
        }

        // If "purple rain" is outside of down's bounds, we will restart it
        if (purpleDrop.position.y < 0) {
            purpleDrop = new PurpleDrop();
        }
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, Constants.WIDTH - 64);
        raindrop.y = Constants.HEIGHT;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    private void lockCamera() {
        camera.position.x = bucket.x;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the purple, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.font.draw(game.batch, "Drops collected: " + score, 16, Constants.HEIGHT - 16);
        game.batch.draw(bucketImage, bucket.x, bucket.y);
        purpleDrop.render(game.batch);
        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        game.batch.end();

        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - (float) (Constants.TILE_SIZE / 2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

        update();
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
        // dispose of all the native resources
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }
}
