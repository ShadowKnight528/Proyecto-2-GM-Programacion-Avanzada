package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Ball2 {
    private float x;
    private float y;
    private float xSpeed; // Cambiado a float
    private float ySpeed; // Cambiado a float
    private Sprite spr;

    public Ball2(float x, float y, int size, float xSpeed, float ySpeed, Texture tx) {
        spr = new Sprite(tx);
        this.x = x; 
        this.y = y;
        spr.setPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update() {
        x += xSpeed;
        y += ySpeed;

        spr.setPosition(x, y);
    }
    

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
    
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    // Getters y Setters actualizados a float
    public float getXSpeed() { return xSpeed; }
    public void setXSpeed(float xSpeed) { this.xSpeed = xSpeed; }
    public float getySpeed() { return ySpeed; }
    public void setySpeed(float ySpeed) { this.ySpeed = ySpeed; }
}