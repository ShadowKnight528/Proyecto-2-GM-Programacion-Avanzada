package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Nave4 {
    private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;
    private DisparoStrategy modoDeDisparo;
    private DisparoStrategy disparoDefault; 
    
    private Texture txBalaDefault;
    private Sound sonidoBalaDefault;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound sonidoBala) {
        sonidoHerido = soundChoque;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
        
        txBalaDefault = txBala;
        sonidoBalaDefault = sonidoBala;
        disparoDefault = new DisparoDefaultStrategy(txBala, sonidoBala);
        modoDeDisparo = disparoDefault;
    }

    public void update(float delta, PantallaJuego juego) {
        // GESTIÓN DEL DISPARO Y CARGA (Evaluado en cada frame de forma directa)
        if (modoDeDisparo != null) {    
            float xDisparo = spr.getX() + spr.getWidth() / 2 - 5;
            float yDisparo = spr.getY() + spr.getHeight() - 5;
            modoDeDisparo.manejarInput(xDisparo, yDisparo, juego);
        } else {
            throw new NullPointerException("No se ha asignado una estrategia de disparo valida");
        }
    }

    public void draw(SpriteBatch batch, PantallaJuego juego, float delta){
        // Se actualiza la logica de inputs
        update(delta, juego);
        
        float x = spr.getX();
        float y = spr.getY();
        
        // Efecto visual de cargar el ataque (parpadeo rojo / morado)
        
        if (modoDeDisparo != null && modoDeDisparo.estaCargando()) {
            float pulso = (float) Math.sin(System.currentTimeMillis() * 0.01) * 0.5f + 0.5f;
            spr.setColor(pulso, 0f, 1f - pulso, 1f);
        } else {
            spr.setColor(1f, 1f, 1f, 1f);
        }

        if (!herido) {
            float aceleracion = 1.0f;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) xVel -= aceleracion;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) xVel += aceleracion;
            if (Gdx.input.isKeyPressed(Input.Keys.S)) yVel -= aceleracion;     
            if (Gdx.input.isKeyPressed(Input.Keys.W)) yVel += aceleracion;
            
            float friccion = 0.75f;
            xVel *= friccion;
            yVel *= friccion;
            
            float velocidadMax = 5.0f;
            xVel = MathUtils.clamp(xVel, -velocidadMax, velocidadMax);
            yVel = MathUtils.clamp(yVel, -velocidadMax, velocidadMax);

            float nuevaX = x + xVel;
            float nuevaY = y + yVel;
            
            if (nuevaX < 0) {
                nuevaX = 0;
                xVel *= -0.8f;
            } else if (nuevaX + spr.getWidth() > 800) {
                nuevaX = 800 - spr.getWidth();
                xVel *= -0.8f;
            }
            
            if (nuevaY < 0) {
                nuevaY = 0;
                yVel *= -0.8f;
            } else if (nuevaY + spr.getHeight() > 640) {
                nuevaY = 640 - spr.getHeight();
                yVel *= -0.8f;
            }
            
            spr.setPosition(nuevaX, nuevaY);    
            spr.draw(batch);
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);  
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }
    }
      
    public boolean checkCollision(Ball2 b) {
        if(!herido && b.getArea().overlaps(spr.getBoundingRectangle())){
            if (xVel == 0) xVel += b.getXSpeed() / 2;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int)xVel / 2);
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());
            
            if (yVel == 0) yVel += b.getySpeed() / 2;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int)yVel / 2);
            yVel = -yVel;
            b.setySpeed(-b.getySpeed());
            
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0) destruida = true; 
            return true;
        }
        return false;
    }
    
    public boolean checkCollision(PowerUp p) {
        if (!herido && p.getArea().overlaps(spr.getBoundingRectangle())) {
            p.aplicarEfecto(this);
            return true;
        }
        return false;
    }
    
    public boolean estaDestruido() {
       return !herido && destruida;
    }
    
    public boolean estaHerido() {
       return herido;
    }
    
    public int getVidas() { return vidas; }
    public int getX() { return (int) spr.getX(); }
    public int getY() { return (int) spr.getY(); }
    public void setVidas(int vidas2) { vidas = vidas2; }
    
    public void setEstrategiaDisparo(DisparoStrategy strategy) {
        modoDeDisparo = strategy;
    }
}