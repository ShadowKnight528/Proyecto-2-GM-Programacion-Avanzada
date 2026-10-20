package puppy.code;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
	
	// Textura para los recuadros del HUD
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
		
		// Inicializar efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1, 0.5f);
		
		// Recuadro café levemente más claro que el fondo (0.06, 0.03, 0.01)
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
		
		// Recuperar proyectiles pendientes de la ronda anterior
		ArrayList<Ball2> proyectilesGuardados = ManejadorJuego.getInstance().recuperarProyectilesPendientes();
	    if (proyectilesGuardados != null && !proyectilesGuardados.isEmpty()) {
	        for (Ball2 b : proyectilesGuardados) {
	            agregarAsteroide(b);
	        }
	    }
	}
    
	public void dibujaEncabezado() {
	    float tiempoRestante = ManejadorJuego.getInstance().getTiempoRestante();
	    int minutos = (int) (tiempoRestante / 60);
	    int segundos = (int) (tiempoRestante % 60);
	    String tiempoFormateado = String.format("%02d:%02d", minutos, segundos);
	    
	    // Ajuste de escala de la fuente
	    game.getFont().getData().setScale(0.9f);
	    
	    // Posicionamiento en el margen inferior derecho (Coordenadas virtuales de la cámara: X=800, Y=640)
	    float xBase = 460f; 
	    float yPanel = 10f;
	    float altoPanel = 65f;
	    float anchoPanel = 325f;
	    
	    // 1. Recuadro de fondo único para todo el HUD en la esquina inferior derecha
	    batch.draw(txPanelHud, xBase, yPanel, anchoPanel, altoPanel);
	    
	    // 2. Letras blancas sobre el recuadro
	    game.getFont().setColor(1f, 1f, 1f, 1f);
	    
	    // Línea 1 (Arriba): Vidas, Ronda y Tiempo
	    game.getFont().draw(batch, "Vidas: " + nave.getVidas() + "  |  Ronda: " + ManejadorJuego.getInstance().getRonda() + "  |  Time: " + tiempoFormateado, xBase + 12f, yPanel + 52f);
	    
	    // Línea 2 (Abajo): Puntuación y Récord
	    game.getFont().draw(batch, "Score: " + ManejadorJuego.getInstance().getScore() + "  |  Hi-Score: " + ManejadorJuego.getInstance().getHighScore(), xBase + 12f, yPanel + 26f);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.06f, 0.03f, 0.01f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Asegurar que el batch use las dimensiones de la cámara ortográfica (800x640)
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		// 1. Actualizar el tiempo de la ronda en el Singleton
		ManejadorJuego.getInstance().actualizarTiempo(delta);

		// 2. Verificar si se acabó el tiempo de la ronda
		if (ManejadorJuego.getInstance().seAcaboTiempoRonda()) {
			ManejadorJuego.getInstance().setVidas(nave.getVidas());
			ManejadorJuego.getInstance().guardarPosicionNave(nave.getX(), nave.getY());
			ManejadorJuego.getInstance().guardarProyectiles(balls1);
			
			Screen ss = new PantallaJuego(game);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
			return;
		}

		batch.begin();
        
		// 3. ACTUALIZAR Y MOVER BALAS DEL JUGADOR
		for (int i = 0; i < balas.size(); i++) {
			Bullet b = balas.get(i);
			b.update();
			
			for (int j = 0; j < balls1.size(); j++) {    
				if (b.checkCollision(balls1.get(j))) {          
					explosionSound.play();
					balls1.remove(j);
					balls2.remove(j);
					j--;
					ManejadorJuego.getInstance().agregarPuntos(10);
				}   	  
			}
            
			if (b.isDestroyed()) {
				balas.remove(b);
				i--; 
			}
		}

		// 4. ACTUALIZAR MOVIMIENTO DE PROYECTILES DEL JEFE
		for (Ball2 ball : balls1) {
			ball.update();
		}

		// 5. DIBUJAR BALAS DEL JUGADOR
		for (Bullet b : balas) {       
			b.draw(batch);
		}

		// 6. ACTUALIZAR Y DIBUJAR AL JEFE
		if (jefeActual != null) {
			jefeActual.update(delta, this);
			jefeActual.draw(batch);
		}

		// 7. DIBUJAR PROYECTILES Y COLISIÓN CON LA NAVE
		for (int i = 0; i < balls1.size(); i++) {
			Ball2 b = balls1.get(i);
			b.draw(batch);
			
			if (nave.checkCollision(b)) {
				balls1.remove(i);
				balls2.remove(i);
				i--;
			}   	  
		}

		// 8. DIBUJAR LA NAVE
		nave.draw(batch, this);
		
		// 9. DIBUJAR EL HUD (Abajo a la derecha)
		dibujaEncabezado();
      
		// 10. VERIFICAR GAME OVER
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
		if (txPanelHud != null) {
			txPanelHud.dispose();
		}
	}
	
	public void agregarAsteroide(Ball2 b) {
	    balls1.add(b);
	    balls2.add(b);
	}
}