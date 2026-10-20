package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RayoLaser {

	private int xVel;
	private int yVel;
	private boolean destruido = false;
	private Sprite spr;
	
	public RayoLaser(float x, float y, int xVel, int yVel, Texture tx, float ancho, float largo) {
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
    	spr.setSize(ancho, largo);
        this.xVel = xVel;
        this.yVel = yVel;
    }

    public void update() {
        spr.setPosition(spr.getX() + xVel, spr.getY() + yVel);
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) {
            destruido = true;
        }
        if (spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) {
        	destruido = true;
        }
    }
    
    public void draw(SpriteBatch batch) {
    	spr.draw(batch);
    }
    
    public boolean checkCollision(Ball2 b2) {
        if(spr.getBoundingRectangle().overlaps(b2.getArea())){
        	// Retorna true para destruir el asteroide
            // No cambia this.destruido a true para que el laser siga su camino.
            return true;
        }
        return false;
    }
    
    public boolean fueDestruido() { 
        return destruido; 
    }
}
