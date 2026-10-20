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
   // private Sound soundBala;
   // private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;
    private DisparoStrategy modoDeDisparo;
    
    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound sonidoBala) {
    	sonidoHerido = soundChoque;
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
    	//spr.setOriginCenter();
    	spr.setBounds(x, y, 45, 45);
    	modoDeDisparo = new DisparoDefaultStrategy(txBala, sonidoBala);

    }
    public void draw(SpriteBatch batch, PantallaJuego juego){
        float x = spr.getX();
        float y = spr.getY();
        
        if (!herido) {
            float aceleracion = 1.0f;
            if (Gdx.input.isKeyPressed(Input.Keys.A))  xVel -= aceleracion;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) xVel += aceleracion;
            if (Gdx.input.isKeyPressed(Input.Keys.S))  yVel -= aceleracion;      
            if (Gdx.input.isKeyPressed(Input.Keys.W))    yVel += aceleracion;
            
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
            } else if (nuevaX + spr.getWidth() > Gdx.graphics.getWidth()) {
                nuevaX = Gdx.graphics.getWidth() - spr.getWidth();
                xVel *= -0.8f;
            }
            
            if (nuevaY < 0) {
                nuevaY = 0;
                yVel *= -0.8f;
            } else if (nuevaY + spr.getHeight() > Gdx.graphics.getHeight()) {
                nuevaY = Gdx.graphics.getHeight() - spr.getHeight();
                yVel *= -0.8f;
            }
            
            spr.setPosition(nuevaX, nuevaY);   
            spr.draw(batch);
            
            /*   if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) spr.setRotation(++rotacion);
	        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) spr.setRotation(--rotacion);
	        
	        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
	        	xVel -=Math.sin(Math.toRadians(rotacion));
	        	yVel +=Math.cos(Math.toRadians(rotacion));
	        	System.out.println(rotacion+" - "+Math.sin(Math.toRadians(rotacion))+" - "+Math.cos(Math.toRadians(rotacion))) ;    
	        }
	        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
	        	xVel +=Math.sin(Math.toRadians(rotacion));
	        	yVel -=Math.cos(Math.toRadians(rotacion));
	        	     
	        }*/
	        
	        // que se mantenga dentro de los bordes de la ventana
            
        } else {
            // Efecto cuando está herida
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);  
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {       
        	float xDisparo = spr.getX() + spr.getWidth() / 2 - 5;
            float yDisparo = spr.getY() + spr.getHeight() - 5;
        	if (modoDeDisparo != null) {
            	modoDeDisparo.disparar(xDisparo, yDisparo, juego);
            } else {
            	throw new NullPointerException("No se ha asignado una estrategia de disparo valida debido a un error");
            }
        }
    }
    
      
    public boolean checkCollision(Ball2 b) {
        if(!herido && b.getArea().overlaps(spr.getBoundingRectangle())){
        	// rebote
            if (xVel ==0) xVel += b.getXSpeed()/2;
            if (b.getXSpeed() ==0) b.setXSpeed(b.getXSpeed() + (int)xVel/2);
            xVel = - xVel;
            b.setXSpeed(-b.getXSpeed());
            
            if (yVel ==0) yVel += b.getySpeed()/2;
            if (b.getySpeed() ==0) b.setySpeed(b.getySpeed() + (int)yVel/2);
            yVel = - yVel;
            b.setySpeed(- b.getySpeed());
            // despegar sprites
      /*      int cont = 0;
            while (b.getArea().overlaps(spr.getBoundingRectangle()) && cont<xVel) {
               spr.setX(spr.getX()+Math.signum(xVel));
            }   */
        	//actualizar vidas y herir
            vidas--;
            herido = true;
  		    tiempoHerido=tiempoHeridoMax;
  		    sonidoHerido.play();
            if (vidas<=0) 
          	    destruida = true; 
            return true;
        }
        return false;
    }
    
    // Sobrecarga para comprobar colision con power ups
    public boolean checkCollision(PowerUp p) {
        if(!herido && p.getArea().overlaps(spr.getBoundingRectangle())){
            p.aplicarEfecto(this); // La nave se pasa a sí misma para recibir el efecto
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
    
    public int getVidas() {return vidas;}
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
	public void setVidas(int vidas2) {vidas = vidas2;}
	public void setEstrategiaDisparo(DisparoStrategy strategy) {modoDeDisparo = strategy;}
}
