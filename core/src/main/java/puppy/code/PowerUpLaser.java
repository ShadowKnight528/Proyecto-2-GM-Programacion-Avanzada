package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class PowerUpLaser extends PowerUp {
    
    private final Texture txRayoLaser;
    private final Sound sonidoLaser;

    public PowerUpLaser(float x, float y, Texture txPowerUp, Texture txRayoLaser, Sound sonidoLaser) {
        super(x, y, txPowerUp);
        this.txRayoLaser = txRayoLaser;
        this.sonidoLaser = sonidoLaser;
    }

    @Override
    public void aplicarEfecto(Nave4 nave) {
        // Al tocarlo cambia la estrategia de la nave al disparo laser
        DisparoStrategy laser = new DisparoLaserStrategy(txRayoLaser, sonidoLaser, 6f);
        nave.setEstrategiaDisparo(laser);
    }
}