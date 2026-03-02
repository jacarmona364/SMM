package SM.JACM.iu;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */


/**
 * Enum que representa las diferentes herramientas de dibujo disponibles en la aplicación.
 * Cada valor de este enum corresponde a una herramienta que se puede utilizar para dibujar
 * en el lienzo, permitiendo al usuario seleccionar qué tipo de figura o acción desea realizar.
 */
public enum jaHerramientaDibujo {
    
    /**
     * Herramienta vacía, que no realiza ninguna acción. Se utiliza como valor predeterminado
     * o cuando no se ha seleccionado ninguna herramienta.
     */
    VACIO,

    /**
     * Herramienta de dibujo para líneas rectas. Permite al usuario dibujar líneas entre dos puntos
     * en el lienzo.
     */
    LINEA,

    /**
     * Herramienta de dibujo para rectángulos. Permite al usuario dibujar rectángulos definidos por
     * dos puntos opuestos en el lienzo.
     */
    RECTANGULO,

    /**
     * Herramienta de dibujo para rectángulos redondeados. Permite al usuario dibujar rectángulos con
     * bordes redondeados en el lienzo.
     */
    ROUNDRECTANGULO,

    /**
     * Herramienta de dibujo para elipses. Permite al usuario dibujar elipses en el lienzo.
     */
    ELIPSE,

    /**
     * Herramienta de dibujo para curvas cuadráticas. Permite al usuario dibujar curvas que se definen
     * por un punto de control y un punto final.
     */
    CURVA,

    /**
     * Herramienta de dibujo para arcos. Permite al usuario dibujar arcos sobre el lienzo.
     */
    ARCO,

    /**
     * Herramienta de dibujo para curvas cuadráticas (similar a CURVA, pero con un enfoque específico para 
     * curvas de tipo cuadrático).
     */
    QUADCURVE,

    /**
     * Herramienta de dibujo para curvas cúbicas. Permite al usuario dibujar curvas cúbicas que se definen
     * por dos puntos de control.
     */
    CUBICCURVE,

    /**
     * Herramienta para mover figuras existentes en el lienzo. Permite al usuario seleccionar y mover
     * figuras ya creadas.
     */
    MOVER,
    
    FIJAR,
    
    ELIMINAR
}

