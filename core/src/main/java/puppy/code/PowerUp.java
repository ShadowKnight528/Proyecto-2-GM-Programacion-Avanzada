package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class PowerUp {
    protected Sprite spr;
    protected boolean tomado = false;

    // Recibe la textura genérica (ej: la caja misteriosa)
    public PowerUp(float x, float y, Texture textureGenerica) {
        this.spr = new Sprite(textureGenerica);
        this.spr.setPosition(x, y);
        this.spr.setBounds(x, y, 30, 30);
    }

    public void draw(SpriteBatch batch) {
        if (!tomado) {
            spr.draw(batch);
        }
    }

    public boolean checkCollision(Nave4 nave) {
        if (!tomado && nave.checkCollision(this)) {
            tomado = true;
            return true;
        }
        return false;
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    // Cada suptipo implementa su propio efecto
    public abstract void aplicarEfecto(Nave4 nave);

    public boolean isTomado() {
        return tomado;
    }
}