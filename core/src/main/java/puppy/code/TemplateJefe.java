package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class TemplateJefe {
    protected Sprite spr;
    protected float x, y;
    protected int vida;
    protected int vidaMaxima;
    
    // Texturas para la barra de vida
    private static Texture txBarraFondo;
    private static Texture txBarraVida;

    public TemplateJefe(Texture textura, int vidaInicial) {
        this.spr = new Sprite(textura);
        this.vida = vidaInicial;
        this.vidaMaxima = vidaInicial;
        this.spr.setSize(120, 120);
        
        this.x = 800 / 2f - spr.getWidth() / 2f;
        this.y = 640 - 150;
        this.spr.setPosition(x, y);
        
        inicializarTexturasBarra();
    }

    private void inicializarTexturasBarra() {
        if (txBarraFondo == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.DARK_GRAY);
            pixmap.fill();
            txBarraFondo = new Texture(pixmap);
            pixmap.dispose();
        }
        if (txBarraVida == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.FIREBRICK);
            pixmap.fill();
            txBarraVida = new Texture(pixmap);
            pixmap.dispose();
        }
    }

    public final void update(float delta, PantallaJuego juego) {
        moverPatron(delta);
        spr.setPosition(x, y);
        procesarAtaque(delta, juego);
    }

    protected abstract void moverPatron(float delta);
    protected abstract void procesarAtaque(float delta, PantallaJuego juego);

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
        
        // Dibujar barra de vida del jefe en la parte superior central
        float barraAncho = 300f;
        float barraAlto = 16f;
        float barraX = 800f / 2f - barraAncho / 2f;
        float barraY = 640f - 30f;
        
        batch.draw(txBarraFondo, barraX, barraY, barraAncho, barraAlto);
        float porcentajeVida = (float) vida / vidaMaxima;
        if (porcentajeVida < 0) porcentajeVida = 0;
        batch.draw(txBarraVida, barraX, barraY, barraAncho * porcentajeVida, barraAlto);
    }

    public boolean recibirDanio(int cantidad, PantallaJuego juego) {
        vida -= cantidad;
        // Probabilidad de soltar un power-up al recibir daño (25%)
        if (Math.random() < 0.25) {
            juego.soltarPowerUp(spr.getX() + spr.getWidth() / 2f, spr.getY());
        }
        return vida <= 0;
    }

    public boolean estaDerrotado() {
        return vida <= 0;
    }

    public static TemplateJefe crearParaRonda(int ronda) {
        if (ronda == 1) {
            return new JefeBuilder()
                .setTextura(new Texture(Gdx.files.internal("jefe1.png")))
                .setVida(50)
                .setVelocidad(1.5f)
                .setTamaño(120, 120)
                .setTipo(1)
                .build();
        } else if (ronda == 2) {
            return new JefeBuilder()
                .setTextura(new Texture(Gdx.files.internal("jefe2.png")))
                .setVida(100)
                .setVelocidad(2.0f)
                .setTamaño(120, 120)
                .setTipo(2)
                .build();
        } else {
            return new JefeBuilder()
                .setTextura(new Texture(Gdx.files.internal("jefe3.png")))
                .setVida(150)
                .setVelocidad(1.2f)
                .setTamaño(150, 150)
                .setTipo(3)
                .build();
        }
    }
}