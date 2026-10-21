package puppy.code;

public interface DisparoStrategy {
    void disparar(float x, float y, PantallaJuego juego);
    
    default void manejarInput(float x, float y, PantallaJuego juego) {
        // Implementación por defecto
    }

    // Método para consultar si está cargando un ataque
    default boolean estaCargando() {
        return false;
    }
}