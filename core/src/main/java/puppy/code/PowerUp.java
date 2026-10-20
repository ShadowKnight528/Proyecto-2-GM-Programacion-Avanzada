package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class PowerUp {
    protected Sprite spr;
    protected boolean tomado = false;
    protected float velocidadCaida = 2.0f; // Velocidad de descenso vertical

    public PowerUp(float x, float y, Texture textureGenerica) {
        spr = new Sprite(textureGenerica);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 35, 35);
    }

    public void update() {
        // Caída vertical constante hacia abajo
        spr.setY(spr.getY() - velocidadCaida);
    }

    public void draw(SpriteBatch batch) {
        if (!tomado) {
            spr.draw(batch);
        }
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public boolean checkCollision(Nave4 nave) {
        if (!tomado && nave.checkCollision(this)) {
            tomado = true;
            aplicarEfecto(nave);
            return true;
        }
        return false;
    }

    public boolean estaFueraDePantalla() {
        return spr.getY() + spr.getHeight() < 0;
    }

    public boolean isTomado() {
        return tomado;
    }

    public abstract void aplicarEfecto(Nave4 nave);
}