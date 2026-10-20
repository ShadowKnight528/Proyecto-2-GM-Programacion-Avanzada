package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class PowerUp {
    protected Sprite spr;
    protected boolean tomado = false;

    public PowerUp(float x, float y, Texture texture) {
        this.spr = new Sprite(texture);
        this.spr.setPosition(x, y);
        this.spr.setBounds(x, y, 30, 30);
    }

    public void draw(SpriteBatch batch) {
        if (!tomado) {
            spr.draw(batch);
        }
    }

    // El powerup delega a la nave la accion de comprobar la colision
    public boolean checkCollision(Nave4 nave) {
        if (!tomado && nave.checkCollision(this)) {
            tomado = true;
            return true;
        }
        return false;
    }

    // Metodo para que la nave pueda obtener el area de colision
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public abstract void aplicarEfecto(Nave4 nave);

    public boolean isTomado() {
        return tomado;
    }
}