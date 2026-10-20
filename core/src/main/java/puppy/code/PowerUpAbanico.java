package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class PowerUpAbanico extends PowerUp {
    
    private final Texture txBalaAbanico;
    private final Sound sonidoAbanico;

    // Recibe los recursos necesarios y llama al constructor de la clase abstracta
    public PowerUpAbanico(float x, float y, Texture txPowerUp, Texture txBalaAbanico, Sound sonidoAbanico) {
        super(x, y, txPowerUp); // Inicializa el sprite y la posicion en la clase padre
        this.txBalaAbanico = txBalaAbanico;
        this.sonidoAbanico = sonidoAbanico;
    }

    @Override
    public void aplicarEfecto(Nave4 nave) {
        DisparoStrategy abanico = new DisparoAbanicoStrategy(txBalaAbanico, sonidoAbanico, 5, 60f, 4f);
        nave.setEstrategiaDisparo(abanico);
    }
}