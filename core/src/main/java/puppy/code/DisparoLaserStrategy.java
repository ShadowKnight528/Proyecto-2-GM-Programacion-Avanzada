package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class DisparoLaserStrategy implements DisparoStrategy {

    private final Texture txBala;
    private final Sound sonidoBala;
    private final float velocidad;

    public DisparoLaserStrategy(Texture txBala, Sound sonidoBala, float velocidad) {
        this.txBala = txBala;
        this.sonidoBala = sonidoBala;
        this.velocidad = velocidad;
    }

    @Override
    public void disparar(float x, float y, PantallaJuego juego) {
        juego.agregarRayoLaser(new RayoLaser(x, y, 0, (int)velocidad, txBala, 10, 30));
        if (sonidoBala != null) sonidoBala.play();
    }
}
