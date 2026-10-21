package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class DisparoLaserStrategy implements DisparoStrategy {

    private final Texture txLaserNivel1;
    private final Texture txLaserNivel2;
    private final Texture txLaserNivel3;
    private final Sound sonidoLaser;
    private final float velocidad;
    
    private float tiempoCarga = 0f;
    private boolean cargando = false;

    public DisparoLaserStrategy(Texture tx1, Texture tx2, Texture tx3, Sound sonido, float velocidad) {
        this.txLaserNivel1 = tx1;
        this.txLaserNivel2 = tx2;
        this.txLaserNivel3 = tx3;
        this.sonidoLaser = sonido;
        this.velocidad = velocidad;
    }

    @Override
    public void disparar(float x, float y, PantallaJuego juego) {
        // No se usa directamente ya que el laser puede cargar el ataque manteniendo presionado el espacio
    }

    @Override
    public void manejarInput(float x, float y, PantallaJuego juego) {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            cargando = true;
            tiempoCarga += delta;
        } else if (cargando) {
            int nivel = 1;
            if (tiempoCarga >= 1.5f) {
                nivel = 3;
            } else if (tiempoCarga >= 0.7f) {
                nivel = 2;
            }

            juego.agregarBala(new RayoLaser(
                x, y, 0, (int)velocidad, 
                txLaserNivel1, txLaserNivel2, txLaserNivel3, 
                10f, 30f, nivel
            ));

            if (sonidoLaser != null) {
            	sonidoLaser.play(0.3f + (nivel * 0.1f));
            }

            tiempoCarga = 0f;
            cargando = false;
        }
    }
    
    @Override
    public boolean estaCargando() {
        return cargando;
    }
}