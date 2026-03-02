/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SM.JACM.Graficos;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Clase jaRectangulo que representa un rectángulo dibujable con propiedades
 * como color, grosor, relleno, transparencia y suavizado.
 * Hereda de la clase MiShape.
 * 
 * @author ASUS
 */
public class jaRectangulo extends MiShape {
    /**
    * Atributo que representa un rectángulo 2D utilizando coordenadas flotantes (float).
    * Se utiliza para definir un rectángulo en el lienzo con posiciones y dimensiones precisas.
    */
   private Rectangle2D.Float rectangulo;

   /**
    * Atributo que define la composición de capas para la operación de dibujo.
    * Este atributo permite combinar diferentes composiciones gráficas (como transparencias o 
    * efectos de mezcla de colores) cuando se dibujan las figuras.
    */
   private Composite composicion;

   /**
    * Atributo que define las sugerencias de renderizado para la representación gráfica.
    * Utiliza los `RenderingHints` para especificar configuraciones relacionadas con la calidad de 
    * la renderización, como la antialiasing, la interpolación y otros efectos visuales.
    */
   private RenderingHints render;

   /**
    * Atributo que define el trazo (o estilo) de la línea.
    * Utiliza una instancia de `Stroke` para configurar el grosor, el patrón (sólido, discontinuo) 
    * y el tipo de línea (recta, curva, etc.) a la hora de dibujar la figura.
    */
   private Stroke trazo;
    
    /**
     * Constructor de la clase jaRectangulo. Crea un rectángulo con las dimensiones,
     * estilo y opciones visuales indicadas.
     *
     * @param x              Coordenada X de la esquina superior izquierda del rectángulo.
     * @param y              Coordenada Y de la esquina superior izquierda del rectángulo.
     * @param ancho          Ancho del rectángulo.
     * @param alto           Alto del rectángulo.
     * @param color          Color del contorno (o relleno si aplica).
     * @param grosor         Grosor del borde del rectángulo.
     * @param relleno        Indica si el rectángulo debe rellenarse.
     * @param transparencia  Indica si debe aplicarse transparencia.
     * @param alisar         Indica si debe aplicarse suavizado (antialiasing).
     */
    public jaRectangulo(float x, float y, float ancho, float alto, 
                       Color color, int grosor, boolean relleno, 
                       boolean transparencia, boolean alisar) {
        super(color, grosor, relleno, transparencia, alisar);
        this.rectangulo = new Rectangle2D.Float(x, y, ancho, alto);

    }
    
    /**
     * Dibuja el rectángulo sobre el contexto gráfico proporcionado,
     * respetando las opciones de estilo, relleno, transparencia y suavizado.
     *
     * @param g2d  Objeto Graphics2D sobre el que se dibuja el rectángulo.
     */
    @Override
    public void draw(Graphics2D g2d) {
         // Guardar configuración original
        Stroke oldStroke = g2d.getStroke();
        Composite oldComposite = g2d.getComposite();
        RenderingHints oldHints = g2d.getRenderingHints();
        Color oldColor = g2d.getColor();

        // Configurar transparencia (actualizado dinámicamente)
        if (transparencia) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        } else {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        // Configurar alisado
        if (alisar) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        // Configurar grosor (actualizado dinámicamente)
        g2d.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        g2d.setPaint(color);

        // Dibujar el rectángulo
        if (relleno) {
            g2d.fill(rectangulo);
        }
        g2d.draw(rectangulo);

        // Dibujar bounding box si está seleccionado
        if (seleccionada) {
            drawBoundingBox(g2d);
        }

        // Restaurar configuración original
        g2d.setStroke(oldStroke);
        g2d.setComposite(oldComposite);
        g2d.setRenderingHints(oldHints);
        g2d.setPaint(oldColor);
    }
    
    /**
     * Obtiene la ubicación actual del rectángulo (esquina superior izquierda).
     *
     * @return  Un objeto Point2D que representa la posición del rectángulo.
     */
    @Override
    public Point2D getLocation() {
        return new Point2D.Float(rectangulo.x, rectangulo.y);
    }
    
    /**
     * Establece una nueva ubicación para el rectángulo, desplazándolo
     * en función de la posición anterior del ratón.
     *
     * @param pos  Nueva posición del ratón.
     */
    @Override
    public void setLocation(Point2D pos) {
        if (lastMousePosition != null) {
            // Usamos el desplazamiento desde el último punto del mouse
            double dx = pos.getX() - lastMousePosition.getX();
            double dy = pos.getY() - lastMousePosition.getY();
            
            // Aplicamos el desplazamiento a la posición del rectángulo
            rectangulo.x += dx;
            rectangulo.y += dy;
            
            // Actualizamos la última posición del mouse
            lastMousePosition = pos;
        }
    }
    
    /**
     * Determina si el punto dado se encuentra dentro del rectángulo.
     *
     * @param p  Punto a comprobar.
     * @return   true si el punto está dentro del rectángulo, false en caso contrario.
     */
    @Override
    public boolean contains(Point2D p) {
        return rectangulo.contains(p);
    }



    public void setFrameFromDiagonal(Point2D p1, Point2D p2) {
        rectangulo.setFrameFromDiagonal(p1, p2);
    }

    @Override
    public Rectangle getBounds() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Rectangle2D getBounds2D() {
        return rectangulo.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean contains(Rectangle2D r) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}