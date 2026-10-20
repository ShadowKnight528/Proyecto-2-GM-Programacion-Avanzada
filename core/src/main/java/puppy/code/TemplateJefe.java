package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class TemplateJefe {
    protected Sprite spr;
    protected float x, y;
    protected int vida;
    protected int vidaMaxima;

    public TemplateJefe(Texture textura, int vidaInicial) {
        this.spr = new Sprite(textura);
        this.vida = vidaInicial;
        this.vidaMaxima = vidaInicial;
        this.spr.setSize(120, 120); // Tamaño imponente para destacar sobre la nave
        
        this.x = 800 / 2f - spr.getWidth() / 2f;
        this.y = 640 - 120; // Justo debajo del borde superior virtual
        this.spr.setPosition(x, y);
    }

    // 🌟 TEMPLATE METHOD: Define la secuencia fija por fotograma
    public final void update(float delta, PantallaJuego juego) {
        moverPatron(delta);
        spr.setPosition(x, y);
        procesarAtaque(delta, juego);
    }

    protected abstract void moverPatron(float delta);
    protected abstract void procesarAtaque(float delta, PantallaJuego juego);

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    // ⚙️ El jefe se encarga internamente de saber qué tipo de jefe instanciar según la ronda
    public static TemplateJefe crearParaRonda(int ronda) {
        if (ronda == 1) {
            return new Jefe(new Texture(Gdx.files.internal("jefe1.png")), 100, 1.5f);
        } else if (ronda == 2) {
            return new JefeRonda2(new Texture(Gdx.files.internal("jefe2.png")), 200, 2.0f);
        } else {
            // Ronda 3 en adelante: permanece este jefe hasta que el jugador pierda
            return new JefeRonda3(new Texture(Gdx.files.internal("jefe3.png")), 400, 1.2f);
        }
    }
}