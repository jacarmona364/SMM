/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SM.JACM.Graficos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Clase abstracta base que representa una forma geométrica con atributos gráficos comunes.
 * Proporciona la estructura básica para todas las formas del lienzo de dibujo.
 * 
 * @author ASUS
 */
public abstract class MiShape implements Shape {
    /**
    * Atributo que define el color de la figura.
    * Es de tipo Color, y puede ser cualquier valor válido de color en Java.
    */
   protected Color color;

   /**
    * Atributo que define el grosor de la línea de la figura.
    * Se especifica en píxeles. Un valor más alto indica un grosor mayor.
    */
   protected int grosor;

   /**
    * Atributo que determina si la figura será rellena o no.
    * Si es true, la figura se rellenará con el color especificado.
    * Si es false, la figura será solo un contorno.
    */
   protected boolean relleno;

   /**
    * Atributo que indica si la figura tendrá transparencia.
    * Si es true, la figura será parcialmente transparente, 
    * permitiendo que se vean los elementos detrás de ella.
    * Si es false, la figura no tendrá transparencia.
    */
   protected boolean transparencia;

   /**
    * Atributo que indica si se debe alisar la línea de la figura.
    * Si es true, se aplicará un suavizado o alisado a la línea.
    * Si es false, la línea será dibujada sin alisar, con bordes más ásperos.
    */
   protected boolean alisar;

   /**
    * Atributo transitorio que guarda la última posición del mouse en el lienzo.
    * Este atributo no se serializa (de ahí el uso de 'transient'), ya que solo se necesita
    * durante el proceso de dibujo o manipulación de la figura y no es necesario guardarlo.
    * Es utilizado para mover las figuras en el lienzo.
    */
   protected transient Point2D lastMousePosition;
   
   protected boolean seleccionada = false;
   
   protected static final int MARGEN_BOUNDING_BOX = 5;
    
    /**
     * Constructor base para las formas geométricas.
     * @param color Color de la forma
     * @param grosor Grosor del trazo en píxeles
     * @param relleno Indica si la forma debe rellenarse
     * @param transparencia Indica si la forma tiene transparencia
     * @param alisar Indica si se aplica antialiasing al dibujar
     */
    public MiShape(Color color, int grosor, boolean relleno, 
                 boolean transparencia, boolean alisar) {
        this.color = color;
        this.grosor = grosor;
        this.relleno = relleno;
        this.transparencia = transparencia;
        this.alisar = alisar;
    }
    
    /**
     * Dibuja la forma en el contexto gráfico especificado.
     * @param g2d Contexto gráfico donde se dibujará la forma
     */
    public abstract void draw(Graphics2D g2d);
    
    /**
     * Obtiene la posición de referencia (punto de anclaje) de la forma.
     * @return Punto2D que representa la posición de referencia
     */
    public abstract Point2D getLocation();
    
    /**
     * Establece la posición de referencia de la forma.
     * @param pos Nueva posición de referencia para la forma
     */
    public abstract void setLocation(Point2D pos);
    
    /**
     * Verifica si la forma contiene un punto específico.
     * @param p Punto a verificar
     * @return true si la forma contiene el punto, false en caso contrario
     */
    @Override
    public abstract boolean contains(Point2D p);
    
    /**
     * Establece la última posición conocida del ratón para movimiento relativo.
     * @param lastPoint Última posición registrada del cursor
     */
    public void setLastMousePosition(Point2D lastPoint) {
        this.lastMousePosition = lastPoint;
    }
    
    /**
     * Obtiene la última posición registrada del ratón.
     * @return Punto2D con la última posición del cursor o null si no se ha establecido
     */
    public Point2D getLastMousePosition() {
        return lastMousePosition;
    }
    
    /**
     * Establece el color de la forma.
     * @param color Nuevo color para la forma
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Obtiene el color actual de la forma.
     * @return Color actual de la forma
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Establece el grosor del trazo de la forma.
     * @param grosor Nuevo grosor en píxeles
     */
    public void setGrosor(int grosor) {
        this.grosor = grosor;
    }
    
    /**
     * Obtiene el grosor actual del trazo.
     * @return Grosor actual en píxeles
     */
    public int getGrosor() {
        return grosor;
    }
    
    /**
     * Establece si la forma debe dibujarse rellena.
     * @param relleno true para rellenar la forma, false para solo contorno
     */
    public void setRelleno(boolean relleno) {
        this.relleno = relleno;
    }
    
    /**
     * Verifica si la forma está configurada para dibujarse rellena.
     * @return true si la forma se dibujará rellena, false en caso contrario
     */
    public boolean isRelleno() {
        return relleno;
    }
    
    /**
     * Establece el nivel de transparencia de la forma.
     * @param transparencia true para aplicar transparencia, false para opaco
     */
    public void setTransparencia(boolean transparencia) {
        this.transparencia = transparencia;
    }
    
    /**
     * Verifica si la forma tiene transparencia aplicada.
     * @return true si la forma es transparente, false si es opaca
     */
    public boolean isTransparencia() {
        return transparencia;
    }
    
    /**
     * Establece si se debe aplicar alisado (antialiasing) al dibujar la forma.
     * @param alisar true para aplicar antialiasing, false para dibujo estándar
     */
    public void setAlisar(boolean alisar) {
        this.alisar = alisar;
    }
    
    /**
     * Verifica si la forma tiene activado el alisado (antialiasing).
     * @return true si se aplica antialiasing, false en caso contrario
     */
    public boolean isAlisar() {
        return alisar;
    }
    
    public void seleccionar() {
        this.seleccionada = true;
    }

    // Método para deseleccionar el rectángulo (cambiar la bandera)
    public void deseleccionar() {
        this.seleccionada = false;
    }
    
    public boolean isSeleccionada() {
        return seleccionada;
    }
    
    /**
     * Dibuja el bounding box de selección
     */
    public void drawBoundingBox(Graphics2D g2d) {
        if (!seleccionada) return;
        
        // Guardar configuración actual
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();
        
        // Configurar estilo del bounding box
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, 
            BasicStroke.JOIN_BEVEL, 0, new float[]{5, 5}, 0)); // Línea discontinua
        
        // Dibujar rectángulo de selección
        Rectangle2D bounds = this.getBounds2D();
        Rectangle2D boundsConMargen = new Rectangle2D.Double(
            bounds.getX() - MARGEN_BOUNDING_BOX,
            bounds.getY() - MARGEN_BOUNDING_BOX,
            bounds.getWidth() + 2 * MARGEN_BOUNDING_BOX,
            bounds.getHeight() + 2 * MARGEN_BOUNDING_BOX
        );
        g2d.draw(boundsConMargen);
        
        // Dibujar cuadrados en las esquinas (tamaño 8x8 píxeles)
        int squareSize = 8;
        drawCornerSquare(g2d, boundsConMargen.getMinX(), boundsConMargen.getMinY(), squareSize);
        drawCornerSquare(g2d, boundsConMargen.getMaxX(), boundsConMargen.getMinY(), squareSize);
        drawCornerSquare(g2d, boundsConMargen.getMinX(), boundsConMargen.getMaxY(), squareSize);
        drawCornerSquare(g2d, boundsConMargen.getMaxX(), boundsConMargen.getMaxY(), squareSize);

        // Restaurar configuración
        g2d.setColor(oldColor);
        g2d.setStroke(oldStroke);
    }
    
    private void drawCornerSquare(Graphics2D g2d, double x, double y, int size) {
        // Guardar configuración original
        Color oldColor = g2d.getColor();
        Stroke oldStroke = g2d.getStroke();

        // Configurar estilo deseado
        g2d.setColor(Color.RED); // Mismo color que el bounding box
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, 
                     BasicStroke.JOIN_BEVEL, 0, new float[]{3, 3}, 0)); // Línea discontinua (3px on, 3px off)

        // Dibujar rectángulo SIN relleno (solo contorno)
        Rectangle2D square = new Rectangle2D.Double(x - size/2, y - size/2, size, size);
        g2d.draw(square);

        // Restaurar configuración original
        g2d.setColor(oldColor);
        g2d.setStroke(oldStroke);
    }
    
    //////// IDEA DE MEJORA POSTERIOR //////// (Consultar con el profesor)
    
//    /**
//     * Aplica los atributos gráficos actuales al contexto especificado.
//     * @param g2d Contexto gráfico donde se aplicarán los atributos
//     */
//    protected void aplicarAtributos(Graphics2D g2d) {
//        // Implementación común para todas las formas
//    }
//    
//    /**
//     * Restaura los atributos originales del contexto gráfico.
//     * @param g2d Contexto gráfico a restaurar
//     * @param oldStroke Trazo original a restaurar
//     * @param oldComposite Composite original a restaurar
//     * @param oldHints RenderingHints originales a restaurar
//     * @param oldColor Color original a restaurar
//     */
//    protected void restaurarAtributos(Graphics2D g2d, 
//                                    Stroke oldStroke, 
//                                    Composite oldComposite,
//                                    RenderingHints oldHints,
//                                    Color oldColor) {
//        // Implementación común
//    }
}
