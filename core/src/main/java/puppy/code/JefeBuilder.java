package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class JefeBuilder {
    private Texture textura;
    private int vida = 100;
    private float velocidadMovimiento = 1.5f;
    private float anchoSprite = 120f;
    private float altoSprite = 120f;
    private int tipoJefe = 1;

    public JefeBuilder setTextura(Texture textura) {
        this.textura = textura;
        return this;
    }

    public JefeBuilder setVida(int vida) {
        this.vida = vida;
        return this;
    }

    public JefeBuilder setVelocidad(float velocidad) {
        this.velocidadMovimiento = velocidad;
        return this;
    }

    public JefeBuilder setTamaño(float ancho, float alto) {
        this.anchoSprite = ancho;
        this.altoSprite = alto;
        return this;
    }

    public JefeBuilder setTipo(int tipoJefe) {
        this.tipoJefe = tipoJefe;
        return this;
    }

    public TemplateJefe build() {
        TemplateJefe jefeCreado;
        
        // Selecciona la subclase correspondiente
        if (tipoJefe == 1) {
            jefeCreado = new Jefe(textura, vida, velocidadMovimiento);
        } else if (tipoJefe == 2) {
            jefeCreado = new JefeRonda2(textura, vida, velocidadMovimiento);
        } else if (tipoJefe == 3) {
            jefeCreado = new JefeRonda3(textura, vida, velocidadMovimiento);
        } else {
            throw new IllegalArgumentException("Tipo de jefe no válido: " + tipoJefe);
        }

        jefeCreado.spr.setSize(anchoSprite, altoSprite);
        jefeCreado.x = 800 / 2f - anchoSprite / 2f;
        jefeCreado.y = 640 - 150; // Se asegura que la posicion vertical sea coherente
        jefeCreado.spr.setPosition(jefeCreado.x, jefeCreado.y);

        return jefeCreado;
    }
}