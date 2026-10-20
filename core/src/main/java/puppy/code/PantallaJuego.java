package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class PantallaJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;	
	private SpriteBatch batch;
	private Sound explosionSound;
	private TemplateJefe jefeActual;
	
	private Nave4 nave;
	private ArrayList<Ball2> balls1 = new ArrayList<>();
	private ArrayList<Ball2> balls2 = new ArrayList<>();
	private ArrayList<Bullet> balas = new ArrayList<>();
	
	private Array<PowerUp> powerUps = new Array<>();
	private Texture txPowerUp;
	private Texture txBalaAbanico;
	private Sound sonidoBalaAbanico;
	private Texture txRayoLaser;
	private Sound sonidoRayoLaser;
	private Random random = new Random();
	
	private Texture txPanelHud;

	public PantallaJuego(SpaceNavigation game) {
		this.game = game;
		int rondaActual = ManejadorJuego.getInstance().getRonda();
		this.jefeActual = TemplateJefe.crearParaRonda(rondaActual);
		
		int vidasActuales = ManejadorJuego.getInstance().getVidas();
		float xGuardada = ManejadorJuego.getInstance().getUltimaPosX();
		float yGuardada = ManejadorJuego.getInstance().getUltimaPosY();
		
		batch = game.getBatch();
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 800, 640);
		
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1, 0.0f);
		
		txPowerUp = new Texture(Gdx.files.internal("power_up.png"));
		txBalaAbanico = new Texture(Gdx.files.internal("bala_abanico.png"));
		sonidoBalaAbanico = Gdx.audio.newSound(Gdx.files.internal("sonido_abanico.wav"));
		sonidoBalaAbanico.setVolume(1, 0.0f);
		txRayoLaser = new Texture(Gdx.files.internal("laser.png"));
		sonidoRayoLaser = Gdx.audio.newSound(Gdx.files.internal("sonido_laser.mp3"));
		sonidoRayoLaser.setVolume(1, 0.0f);
		
		
		
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(0.18f, 0.11f, 0.05f, 0.85f); 
		pixmap.fill();
		txPanelHud = new Texture(pixmap);
		pixmap.dispose();
		
		nave = new Nave4(
		    (int) xGuardada, 
		    (int) yGuardada, 
		    new Texture(Gdx.files.internal("Vito.png")),
		    Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
		    new Texture(Gdx.files.internal("Rocket2.png")), 
		    Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))
		); 
		
		nave.setVidas(vidasActuales);
		
		ArrayList<Ball2> proyectilesGuardados = ManejadorJuego.getInstance().recuperarProyectilesPendientes();
	    if (proyectilesGuardados != null && !proyectilesGuardados.isEmpty()) {
	        for (Ball2 b : proyectilesGuardados) {
	            agregarAsteroide(b);
	        }
	    }
	}
    
	public void dibujaEncabezado() {
	    game.getFont().getData().setScale(0.9f);
	    
	    float xBase = 460f; 
	    float yPanel = 10f;
	    float altoPanel = 65f;
	    float anchoPanel = 325f;
	    
	    batch.draw(txPanelHud, xBase, yPanel, anchoPanel, altoPanel);
	    game.getFont().setColor(1f, 1f, 1f, 1f);
	    
	    game.getFont().draw(batch, "Vidas: " + nave.getVidas() + "  |  Ronda: " + ManejadorJuego.getInstance().getRonda(), xBase + 12f, yPanel + 52f);
	    game.getFont().draw(batch, "Score: " + ManejadorJuego.getInstance().getScore() + "  |  Hi-Score: " + ManejadorJuego.getInstance().getHighScore(), xBase + 12f, yPanel + 26f);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.06f, 0.03f, 0.01f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		// Verificar si el jefe fue derrotado para avanzar de ronda
		if (jefeActual != null && jefeActual.estaDerrotado()) {
			ManejadorJuego.getInstance().setVidas(nave.getVidas());
			ManejadorJuego.getInstance().guardarPosicionNave(nave.getX(), nave.getY());
			ManejadorJuego.getInstance().siguienteRonda();
			
			Screen ss = new PantallaJuego(game);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
			return;
		}

		batch.begin();
        
		// 1. ACTUALIZAR Y MOVER BALAS DEL JUGADOR (Colisionan con el Jefe)
		for (int i = 0; i < balas.size(); i++) {
			Bullet b = balas.get(i);
			b.update();
			
			// Comprobar colisión de la bala con el jefe
			if (jefeActual != null && b.checkCollisionConJefe(jefeActual)) {
				explosionSound.play(0.2f);
				balas.remove(i);
				i--;
				ManejadorJuego.getInstance().agregarPuntos(15);
				
				// Dañar al jefe (1 de daño por bala)
				if (jefeActual.recibirDanio(1, this)) {
					// El jefe ha sido derrotado en este mismo impacto
					ManejadorJuego.getInstance().agregarPuntos(100);
				}
			} else if (b.isDestroyed()) {
				balas.remove(b);
				i--; 
			}
		}

		// 2. ACTUALIZAR PROYECTILES DEL JEFE
		for (Ball2 ball : balls1) {
			ball.update();
		}

		// 3. DIBUJAR BALAS DEL JUGADOR
		for (Bullet b : balas) {       
			b.draw(batch);
		}

		// 4. ACTUALIZAR Y DIBUJAR AL JEFE
		if (jefeActual != null) {
			jefeActual.update(delta, this);
			jefeActual.draw(batch);
		}

		// 5. ACTUALIZAR Y DIBUJAR POWER-UPS (Caída vertical)
		for (int i = 0; i < powerUps.size; i++) {
			PowerUp p = powerUps.get(i);
			p.update();
			p.draw(batch);
			
			if (p.estaFueraDePantalla()) {
				powerUps.removeIndex(i);
				i--;
			} else if (nave.checkCollision(p)) {
				powerUps.removeIndex(i);
				i--;
			}
		}

		// 6. DIBUJAR PROYECTILES Y COLISIÓN CON LA NAVE
		for (int i = 0; i < balls1.size(); i++) {
			Ball2 b = balls1.get(i);
			b.draw(batch);
			
			if (nave.checkCollision(b)) {
				balls1.remove(i);
				balls2.remove(i);
				i--;
			}   	  
		}

		// 7. DIBUJAR LA NAVE
		nave.draw(batch, this, delta);
		
		// 8. DIBUJAR EL HUD
		dibujaEncabezado();
      
		// 9. VERIFICAR GAME OVER
		if (nave.estaDestruido()) {
			int scoreActual = ManejadorJuego.getInstance().getScore();
			if (scoreActual > ManejadorJuego.getInstance().getHighScore()){
				ManejadorJuego.getInstance().setHighScore(scoreActual);
			}
			batch.end();
			Screen ss = new PantallaGameOver(game);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
			return;
		}

		batch.end();
	}
    
    public boolean agregarBala(Bullet bb) {
    	return balas.add(bb);
    }

	public void soltarPowerUp(float x, float y) {
		if (random.nextBoolean()) {
			powerUps.add(new PowerUpAbanico(x, y, txPowerUp, txBalaAbanico, sonidoBalaAbanico));
		} else {
			powerUps.add(new PowerUpLaser(x, y, txPowerUp, txRayoLaser, sonidoRayoLaser));
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
		this.explosionSound.dispose();
		if (txPanelHud != null) txPanelHud.dispose();
		if (txPowerUp != null) txPowerUp.dispose();
		if (txBalaAbanico != null) txBalaAbanico.dispose();
		if (sonidoBalaAbanico != null) sonidoBalaAbanico.dispose();
		if (txRayoLaser != null) txRayoLaser.dispose();
		if (sonidoRayoLaser != null) sonidoRayoLaser.dispose();
	}
	
	public void agregarAsteroide(Ball2 b) {
	    balls1.add(b);
	    balls2.add(b);
	}
}