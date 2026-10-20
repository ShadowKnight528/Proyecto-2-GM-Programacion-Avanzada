package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.Random;

public class JefeRonda3 extends TemplateJefe {
    private float velocidadMovimiento;
    private float tiempoTotal = 0;
    private float tiempoDesdeUltimoDisparo = 0;
    private float frecuenciaDisparo = 0.5f; // Ráfagas muy frecuentes (Bullet Hell)
    private Random rand = new Random();

    public JefeRonda3(Texture textura, int vida, float velocidadMovimiento) {
        super(textura, vida);
        this.velocidadMovimiento = velocidadMovimiento;
        this.spr.setSize(150, 150); // El jefe más grande
    }

    @Override
    protected void moverPatron(float delta) {
        tiempoTotal += delta;
        // Movimiento compuesto en X e Y (Patrón en 8 u óvalo flotante)
        float centroX = 800f / 2f - spr.getWidth() / 2f;
        float centroY = 640f - 160;

        x = centroX + (float) Math.sin(tiempoTotal * velocidadMovimiento) * 150f;
        y = centroY + (float) Math.cos(tiempoTotal * velocidadMovimiento * 2f) * 30f;
    }

    @Override
    protected void procesarAtaque(float delta, PantallaJuego juego) {
        tiempoDesdeUltimoDisparo += delta;

        if (tiempoDesdeUltimoDisparo >= frecuenciaDisparo) {
            tiempoDesdeUltimoDisparo = 0;
            lanzarEspiralCaotica(juego);
        }
    }

    private void lanzarEspiralCaotica(PantallaJuego juego) {
        float xOrigen = spr.getX() + spr.getWidth() / 2f;
        float yOrigen = spr.getY() + spr.getHeight() / 2f;

        int cantidadDisparos = 12; // Anillo completo de disparos
        float anguloInicial = tiempoTotal * 100f; // Hace rotar la espiral fotograma a fotograma

        for (int i = 0; i < cantidadDisparos; i++) {
            float anguloGrados = anguloInicial + (360f / cantidadDisparos) * i;
            float anguloRad = (float) Math.toRadians(anguloGrados);

            float velocidadBase = 3.5f + rand.nextFloat() * 1.5f;
            float xSpeed = (float) (Math.cos(anguloRad) * velocidadBase);
            float ySpeed = (float) (Math.sin(anguloRad) * velocidadBase);

            Ball2 proyectilEnemigo = new Ball2(
                (int) xOrigen,
                (int) yOrigen,
                7, // Pequeño proyectil
                (int) xSpeed,
                (int) ySpeed,
                new Texture(Gdx.files.internal("aGreyMedium4.png"))
            );

            juego.agregarAsteroide(proyectilEnemigo);
        }
    }
}