package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class JefeRonda2 extends TemplateJefe {
    private float velocidadMovimiento;
    private float tiempoTotal = 0;
    private float tiempoDesdeUltimoDisparo = 0;
    private float frecuenciaDisparo = 1.0f; // Frecuencia moderada

    public JefeRonda2(Texture textura, int vida, float velocidadMovimiento) {
        super(textura, vida);
        this.velocidadMovimiento = velocidadMovimiento;
        this.spr.setSize(120, 120);
    }

    @Override
    protected void moverPatron(float delta) {
        tiempoTotal += delta;
        float centroX = Gdx.graphics.getWidth() / 2f - spr.getWidth() / 2f;
        x = centroX + (float) Math.sin(tiempoTotal * velocidadMovimiento) * 150f;
    }

    @Override
    protected void procesarAtaque(float delta, PantallaJuego juego) {
        tiempoDesdeUltimoDisparo += delta;

        if (tiempoDesdeUltimoDisparo >= frecuenciaDisparo) {
            tiempoDesdeUltimoDisparo = 0;
            lanzarEspiralCaotica(juego, 9, 3.0f); // 9 proyectiles a velocidad media
        }
    }

    private void lanzarEspiralCaotica(PantallaJuego juego, int cantidadDisparos, float velocidadBase) {
        float xOrigen = spr.getX() + spr.getWidth() / 2f;
        float yOrigen = spr.getY() + spr.getHeight() / 2f;
        float anguloInicial = tiempoTotal * 80f;

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