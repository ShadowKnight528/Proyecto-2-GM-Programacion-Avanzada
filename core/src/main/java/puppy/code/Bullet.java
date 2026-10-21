package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
    private int xSpeed;
    private int ySpeed;
    private int danio;
    private boolean destroyed = false;
    private Sprite spr;
   
    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.danio = 1; 
    }

    // Constructor usado por DisparoAbanicoStrategy
    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx, float width, float height) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setSize(width, height);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.danio = 1; 
    }

    // Constructor sin tamaño especificando daño
    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx, int danio) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.danio = danio;
    }
    
    // Constructor con tamaño y daño, usado por RayoLaser)
    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx, float width, float height, int danio) {
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setSize(width, height);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.danio = danio;
    }
    
    public void update() {
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) {
            destroyed = true;
        }
        if (spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) {
        	destroyed = true;
        }
    }
    
    public void draw(SpriteBatch batch) {
    	spr.draw(batch);
    }
    
    public boolean checkCollision(Ball2 b2) {
        if (spr.getBoundingRectangle().overlaps(b2.getArea())) {
            efectoAlChocar();
            return true;
        }
        return false;
    }

    protected void efectoAlChocar() {
        destroyed = true;
    }
    
    public boolean isDestroyed() { 
        return destroyed; 
    }
    
    public int getDanio() {
        return danio;
    }
    
    public boolean checkCollisionConJefe(TemplateJefe jefe) {
        if (spr.getBoundingRectangle().overlaps(jefe.spr.getBoundingRectangle())) {
            this.destroyed = true;
            return true;
        }
        return false;
    }
}