package puppy.code;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class ManejadorJuego {
	private static ManejadorJuego instance;
	private final float TIEMPO_LIMITE_RONDA = 20f;
	private float tiempoRonda = 0;
    private int score;
    private int vidas;
    private int ronda;
    private int highScore;
    private float ultimaPosX = 600;
    private float ultimaPosY = 100;
    private ArrayList<Ball2> proyectilesPendientes = new ArrayList<>();
    private Music gameMusic;
    
    private ManejadorJuego() {
        score = 0;
        vidas = 3;
        ronda = 1;
        
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Luna-de-miel-en-la-mano.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();
    }

    public static ManejadorJuego getInstance() {
        if (instance == null) {
            instance = new ManejadorJuego();
        }
        return instance;
    }
    
    public void reiniciarJuego() {
        this.score = 0;
        this.vidas = 3;
        this.ronda = 1;
        this.tiempoRonda = 0;
        
        if (gameMusic != null && !gameMusic.isPlaying()) {
            gameMusic.play();
        }
    }

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void agregarPuntos(int puntos) {
        this.score += puntos;
    }	

	public int getVidas() {
		return vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}
	
	public void perderVida() {
        this.vidas--;
    }

	public int getRonda() {
		return ronda;
	}

	public void setRonda(int ronda) {
		this.ronda = ronda;
	}
	
	public void siguienteRonda() {
        this.ronda++;
    }

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

	public float getTiempoRonda() {
		return tiempoRonda;
	}

	public void setTiempoRonda(float tiempoRonda) {
		this.tiempoRonda = tiempoRonda;
	}

	public float getTIEMPO_LIMITE_RONDA() {
		return TIEMPO_LIMITE_RONDA;
	}
	
	public void actualizarTiempo(float d) {
		tiempoRonda += d;
	}
	
	public boolean seAcaboTiempoRonda() {
        if (tiempoRonda >= TIEMPO_LIMITE_RONDA) {
            tiempoRonda = 0f;
            siguienteRonda();
            return true;
        }
        return false;
    }

    public float getTiempoRestante() {
        float restante = TIEMPO_LIMITE_RONDA - tiempoRonda;
        return restante > 0 ? restante : 0;
    }
	
    public void guardarPosicionNave(float x, float y) {
        this.ultimaPosX = x;
        this.ultimaPosY = y;
    }
    
    public float getUltimaPosX() { 
    	return ultimaPosX; 
    }
    
    public float getUltimaPosY() { 
    	return ultimaPosY; 
    }
    
    public void avanzarSiguienteRonda(int vidasActuales) {
        this.vidas = vidasActuales;
        this.ronda++;
        this.tiempoRonda = 0;
    }
    
    public void guardarProyectiles(ArrayList<Ball2> proyectilesActuales) {
        this.proyectilesPendientes = new ArrayList<>(proyectilesActuales);
    }
    
    public ArrayList<Ball2> recuperarProyectilesPendientes(){
        ArrayList<Ball2> temp = this.proyectilesPendientes;
        this.proyectilesPendientes = new ArrayList<>();
        return temp;
    }
}