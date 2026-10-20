package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class RayoLaser extends Bullet {

    public RayoLaser(float x, float y, int xVel, int yVel, Texture tx, float ancho, float largo) {
        super(x, y, xVel, yVel, tx, ancho, largo);
    }

    @Override
    protected void efectoAlChocar() {
        // No se destruye al impactar un asteroide o enemigo, el rayo atraviesa las demas entidades
    }
}