package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public class RayoLaser extends Bullet {
    private int nivelCarga;

    public RayoLaser(float x, float y, int xSpeed, int ySpeed, 
                     Texture txNivel1, Texture txNivel2, Texture txNivel3, 
                     float width, float height, int nivelCarga) {
    	 super(centrarX(x, width, nivelCarga), y, xSpeed, ySpeed,
    	          seleccionarTextura(nivelCarga, txNivel1, txNivel2, txNivel3),
    	          calcularAncho(nivelCarga, width),
    	          calcularAlto(nivelCarga, height),
    	          calcularDanio(nivelCarga));
        
        this.nivelCarga = nivelCarga;
    }
    
    private static float centrarX(float x, float baseWidth, int nivel) {
        float ancho = calcularAncho(nivel, baseWidth);
        float centro = x + baseWidth / 2f;      
        float nuevaX = centro - ancho / 2f;
        float maxX = Gdx.graphics.getWidth() - ancho;
        return Math.max(0f, Math.min(nuevaX, maxX));
    }

    private static Texture seleccionarTextura(int nivel, Texture t1, Texture t2, Texture t3) {
        if (nivel == 3) return t3;
        if (nivel == 2) return t2;
        return t1;
    }

    private static int calcularDanio(int nivel) {
        switch (nivel) {
            case 3: return 28; // Mayor daño
            case 2: return 14; // Daño medio
            default: return 1; // Daño base
        }
    }

    private static float calcularAncho(int nivel, float baseWidth) {
        switch (nivel) {
            case 3: return baseWidth * 20.5f;
            case 2: return baseWidth * 10.5f;
            default: return baseWidth;
        }
    }

    private static float calcularAlto(int nivel, float baseHeight) {
        switch (nivel) {
            case 3: return baseHeight * 1.8f;
            case 2: return baseHeight * 1.3f;
            default: return baseHeight;
        }
    }

    @Override
    protected void efectoAlChocar() {
        // El rayo atraviesa objetivos sin destruirse
    }

    public int getNivelCarga() {
        return nivelCarga;
    }
}