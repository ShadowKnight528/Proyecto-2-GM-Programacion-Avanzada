package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
		
		// Inicializar efectos de sonido (la música va por el Singleton)
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1, 0.5f);
		
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
	    
		CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ManejadorJuego.getInstance().getRonda();
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Tiempo: " + tiempoFormateado, Gdx.graphics.getWidth() / 2 - 70, 60);
	    game.getFont().draw(batch, "Score: " + ManejadorJuego.getInstance().getScore(), Gdx.graphics.getWidth() - 150, 30);
	    game.getFont().draw(batch, "HighScore: " + ManejadorJuego.getInstance().getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// 1. Actualizar el tiempo de la ronda en el Singleton
		ManejadorJuego.getInstance().actualizarTiempo(delta);

		// 2. Verificar si se acabó el tiempo de la ronda
		if (ManejadorJuego.getInstance().seAcaboTiempoRonda()) {
			// Guardamos estado actual antes de cambiar de pantalla (seAcaboTiempoRonda ya incrementó la ronda)
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
		dibujaEncabezado();
        
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

		// 6. DIBUJAR BALAS DEL JUGADOR
		for (Bullet b : balas) {       
			b.draw(batch);
		}

		// 7. ACTUALIZAR Y DIBUJAR AL JEFE
		if (jefeActual != null) {
			jefeActual.update(delta, this);
			jefeActual.draw(batch);
		}

		// 8. DIBUJAR PROYECTILES Y COLISIÓN CON LA NAVE
		for (int i = 0; i < balls1.size(); i++) {
			Ball2 b = balls1.get(i);
			b.draw(batch);
			
			if (nave.checkCollision(b)) {
				balls1.remove(i);
				balls2.remove(i);
				i--;
			}   	  
		}

		// 9. DIBUJAR LA NAVE
		nave.draw(batch, this);
      
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
		// Nota: gameMusic ya no se destruye aquí para que la música suene de corrido entre rondas.
	}
	
	public void agregarAsteroide(Ball2 b) {
	    balls1.add(b);
	    balls2.add(b);
	}
}