package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class PowerUpAbanico extends PowerUp {
    
    private final Texture txBalaAbanico;
    private final Sound sonidoAbanico;

    // Recibe la textura del power up generico y sus recursos necesarios para el disparo
    public PowerUpAbanico(float x, float y, Texture txCajaMisteriosa, Texture txBalaAbanico, Sound sonidoAbanico) {
        super(x, y, txCajaMisteriosa); // Muestra la textura generica de power up en pantalla
        this.txBalaAbanico = txBalaAbanico;
        this.sonidoAbanico = sonidoAbanico;
    }

    @Override
    public void aplicarEfecto(Nave4 nave) {
        DisparoStrategy abanico = new DisparoAbanicoStrategy(txBalaAbanico, sonidoAbanico, 5, 60f, 4f);
        nave.setEstrategiaDisparo(abanico);
    }
}