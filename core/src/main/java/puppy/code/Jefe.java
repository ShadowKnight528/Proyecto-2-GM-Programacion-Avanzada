package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Jefe extends TemplateJefe {
    private float velocidadMovimiento;
    private float tiempoTotal = 0;
    private float tiempoDesdeUltimoDisparo = 0;
    private float frecuenciaDisparo = 1.5f; // Espera más tiempo entre ráfagas

    public Jefe(Texture textura, int vida, float velocidadMovimiento) {
        super(textura, vida);
        this.velocidadMovimiento = velocidadMovimiento;
    }

    @Override
    protected void moverPatron(float delta) {
        tiempoTotal += delta;
        x += Math.sin(tiempoTotal * velocidadMovimiento) * 2f;
    }

    @Override
    protected void procesarAtaque(float delta, PantallaJuego juego) {
        tiempoDesdeUltimoDisparo += delta;

        if (tiempoDesdeUltimoDisparo >= frecuenciaDisparo) {
            tiempoDesdeUltimoDisparo = 0;
            lanzarEspiralCaotica(juego, 6, 2.5f); // Menos balas (6) y más lentas (2.5)
        }
    }

    private void lanzarEspiralCaotica(PantallaJuego juego, int cantidadDisparos, float velocidadBase) {
        float xOrigen = spr.getX() + spr.getWidth() / 2f;
        float yOrigen = spr.getY() + spr.getHeight() / 2f;
        float anguloInicial = tiempoTotal * 60f; // Rotación más lenta

        for (int i = 0; i < cantidadDisparos; i++) {
            float anguloGrados = anguloInicial + (360f / cantidadDisparos) * i;
            float anguloRad = (float) Math.toRadians(anguloGrados);

            float xSpeed = (float) (Math.cos(anguloRad) * velocidadBase);
            float ySpeed = (float) (Math.sin(anguloRad) * velocidadBase);

            Ball2 proyectilEnemigo = new Ball2(
                (int) xOrigen, (int) yOrigen, 7,
                (int) xSpeed, (int) ySpeed,
                new Texture(Gdx.files.internal("aGreyMedium4.png"))
            );
            juego.agregarAsteroide(proyectilEnemigo);
        }
    }
}