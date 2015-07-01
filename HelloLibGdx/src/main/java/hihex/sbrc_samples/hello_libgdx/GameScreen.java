package hihex.sbrc_samples.hello_libgdx;

import android.graphics.PointF;
import android.graphics.Rect;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.UUID;

import hihex.sbrc.ClientFactories;
import hihex.sbrc.Module;
import hihex.sbrc.SbrcManager;
import hihex.sbrc.StandardClient;
import hihex.sbrc.UserInterfaceMode;

final class GdxGame extends Game {
    public final SbrcManager manager;
    public SpriteBatch batch;

    public GdxGame(final SbrcManager manager) {
        this.manager = manager;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}

public final class GameScreen implements Screen {
    private final GdxGame mGame;
    private final Texture mTexture;
    private final Sprite mSprite;

    private float mX = 0;
    private float mY = 0;

    public GameScreen(final GdxGame game) {
        mGame = game;

        mTexture = new Texture(Gdx.files.internal("texture.png"));
        mSprite = new Sprite(mTexture);

        game.manager.runWhenReady(new Runnable() {
            @Override
            public void run() {
                registerClientFactory();
            }
        });
    }

    @Override
    public void render(final float delta) {
        // Pale blue background
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.9f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mGame.batch.begin();
        mSprite.setPosition(mX, mY);
        mSprite.draw(mGame.batch);
        mGame.batch.end();
    }

    private void registerClientFactory() {
        // Here we don't distinguish between different users, and simply position the sprite using the absolute
        // coordinates on the touch-screen.

        final StandardClient client = new StandardClient(
                /*isLandscape*/false,
                new Module(UserInterfaceMode.InteractionType.kStandard, /*isMultitouch*/false) {
                    @Override
                    protected void onBegin(final UUID deviceId, final int fingerId, final PointF pos, final Rect region) {
                        // Touch-down
                        onMove(deviceId, fingerId, pos, region);
                    }

                    @Override
                    protected void onEnd(final UUID deviceId, final int fingerId, final PointF pos, final Rect region) {
                        // Touch-up
                        onMove(deviceId, fingerId, pos, region);
                    }

                    @Override
                    protected void onMove(final UUID deviceId, final int fingerId, final PointF pos, final Rect region) {
                        mX = pos.x;
                        mY = region.height() - pos.y;
                    }

                    @Override
                    protected void onCancel(final UUID deviceId, final int fingerId) {}
                }
        );
        mGame.manager.setClientFactory(ClientFactories.singleton(client));
    }

    @Override
    public void dispose() {
        mTexture.dispose();
    }

    @Override
    public void show() {}

    @Override
    public void resize(final int width, final int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}