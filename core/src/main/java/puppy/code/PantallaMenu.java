package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private Texture txFondo;

	public PantallaMenu(SpaceNavigation game) {
		this.game = game;
        
		camera = new OrthographicCamera();
		// Fijamos una resolución virtual limpia, por ejemplo 800x640 o 1200x800
		camera.setToOrtho(false, 800, 640);
		
		// Cargamos la textura de fondo (asegúrate de tener "fondo.png" en tus assets, 
		// o puedes cambiarlo por otra imagen como "menu_bg.png")
		txFondo = new Texture(Gdx.files.internal("fondo.png"));
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.06f, 0.03f, 0.01f, 1f);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		
		// Dibujamos el fondo cubriendo toda la pantalla virtual (800x640)
		game.getBatch().draw(txFondo, 0, 0, 800, 640);

		// Textos centrados o ubicados limpiamente en la pantalla virtual
		game.getFont().getData().setScale(2f);
		game.getFont().draw(game.getBatch(), "Vito Adventures", 250, 400);
		
		game.getFont().getData().setScale(1.2f);
		game.getFont().draw(game.getBatch(), "Press any key to start...", 260, 300);
	
		game.getBatch().end();

		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			ManejadorJuego.getInstance().reiniciarJuego();
			Screen ss = new PantallaJuego(game);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		}
	}
	
	@Override
	public void show() {}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		if (txFondo != null) {
			txFondo.dispose();
		}
	}
}