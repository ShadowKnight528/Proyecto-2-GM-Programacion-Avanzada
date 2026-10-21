package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class PowerUpLaser extends PowerUp {
    
    private final Texture txRayoLaser;
    private final Sound sonidoLaser;

    public PowerUpLaser(float x, float y, Texture txCajaMisteriosa, Texture txRayoLaser, Sound sonidoLaser) {
        super(x, y, txCajaMisteriosa);
        this.txRayoLaser = txRayoLaser;
        this.sonidoLaser = sonidoLaser;
    }

    @Override
    public void aplicarEfecto(Nave4 nave) { 
        // Se cargan texturas para los laser en sus distintos niveles
        DisparoStrategy laser = new DisparoLaserStrategy(
            txRayoLaser, txRayoLaser, txRayoLaser, 
            sonidoLaser, 6f
        );
        nave.setEstrategiaDisparo(laser);
    }
}