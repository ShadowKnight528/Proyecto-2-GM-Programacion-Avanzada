package puppy.code;

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
        juego.agregarBala(new Bullet(x, y, 0, 3, txBala));
        if (sonidoBala != null) sonidoBala.play();
    }
}

