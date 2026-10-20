package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class DisparoAbanicoStrategy implements DisparoStrategy {

    private final Texture txBala;
    private final Sound sonidoBala;
    private final int numBalas;
    private final float anguloApertura;   // apertura del abanico en grados
    private final float velocidad;

    public DisparoAbanicoStrategy(Texture txBala, Sound sonidoBala, int numBalas, float angulo, float velocidad) {
    
        if (numBalas < 1) throw new IllegalArgumentException("El numero de balas debe ser >= 1");
        this.txBala = txBala;
        this.sonidoBala = sonidoBala;
        this.numBalas = numBalas;
        this.anguloApertura = angulo;
        this.velocidad = velocidad;
    }

    @Override
    public void disparar(float x, float y, PantallaJuego juego) {
        // 90° = hacia arriba; el abanico se centra en esa dirección
        float anguloInicial = 90f - anguloApertura / 2f;
        float paso = (numBalas > 1) ? anguloApertura / (numBalas - 1) : 0f;
        if (numBalas == 1) anguloInicial = 90f;

        for (int i = 0; i < numBalas; i++) {
            double rad = Math.toRadians(anguloInicial + paso * i);
            int vx = (int) Math.rint(Math.cos(rad) * velocidad);
            int vy = (int) Math.rint(Math.sin(rad) * velocidad);
            juego.agregarBala(new Bullet(x, y, vx, vy, txBala));
        }
        if (sonidoBala != null) sonidoBala.play();   // un solo sonido por ráfaga
    }
}