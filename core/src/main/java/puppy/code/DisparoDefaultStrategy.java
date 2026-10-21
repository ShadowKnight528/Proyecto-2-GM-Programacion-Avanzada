package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class DisparoDefaultStrategy implements DisparoStrategy {
    private final Texture txBala;
    private final Sound sonidoBala;

    public DisparoDefaultStrategy(Texture txBala, Sound sonidoBala) {
        this.txBala = txBala;
        this.sonidoBala = sonidoBala;
    }

    @Override
    public void disparar(float x, float y, PantallaJuego juego) {
        // Usamos el constructor con daño por defecto 1
        juego.agregarBala(new Bullet(x, y, 0, 7, txBala, 1));
        if (sonidoBala != null) {
            sonidoBala.play(0.2f);
        }
    }

    @Override
    public void manejarInput(float x, float y, PantallaJuego juego) {
        // El disparo por defecto se ejecuta al presionar la tecla con un unico click
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            disparar(x, y, juego);
        }
    }
}