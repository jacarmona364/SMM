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
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Clase jaLinea que representa una línea dibujable con opciones de estilo como
 * color, grosor, transparencia y suavizado.
 * Hereda de la clase MiShape.
 * 
 * @author ASUS
 */
public class jaLinea extends MiShape {
    /**
    * Atributo que representa una línea 2D utilizando las coordenadas flotantes (float).
    * Es utilizado para dibujar líneas en el lienzo, especificando los puntos de inicio y fin.
    */
   private Line2D.Float linea;

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
     * Constructor de la clase jaLinea. Crea una línea con los puntos dados, color, grosor,
     * y opciones de transparencia y suavizado.
     *
     * @param p1             Punto inicial de la línea.
     * @param p2             Punto final de la línea.
     * @param color          Color de la línea.
     * @param grosor         Grosor del trazo de la línea.
     * @param transparencia  Indica si la línea debe ser semitransparente.
     * @param alisar         Indica si se debe aplicar suavizado de bordes.
     */
    public jaLinea(Point2D p1, Point2D p2, Color color, int grosor, 
                  boolean transparencia, boolean alisar) {
        
        super(color, grosor, false, transparencia, alisar);
        this.linea = new Line2D.Float(p1, p2);
    }
    
    /**
     * Establece nuevos puntos para la línea.
     *
     * @param p1  Nuevo punto inicial.
     * @param p2  Nuevo punto final.
     */
    public void setLine(Point2D p1, Point2D p2) {
        this.linea.setLine(p1, p2);
    }
    
    /**
     * Determina si un punto dado está lo suficientemente cerca de la línea.
     * Se utiliza una tolerancia de 20 píxeles para considerar la proximidad.
     *
     * @param p  Punto a comprobar.
     * @return   true si el punto está cerca de la línea, false en caso contrario.
     */
    public boolean isNear(Point2D p) {
        if(linea.getP1().equals(linea.getP2())) 
            return linea.getP1().distance(p) <= 20.0;
        return linea.ptLineDist(p) <= 20.0;
    }
    
    /**
     * Comprueba si la línea contiene un punto, usando la lógica de proximidad.
     *
     * @param p  Punto a comprobar.
     * @return   true si el punto está dentro del área cercana a la línea.
     */
    @Override
    public boolean contains(Point2D p) {
        return isNear(p);
    }
    
    /**
     * Dibuja la línea en el contexto gráfico especificado, aplicando
     * las opciones de transparencia, suavizado y estilo de trazo.
     *
     * @param g2d  Objeto Graphics2D sobre el cual se dibuja la línea.
     */
    @Override
    public void draw(Graphics2D g2d) {
        // Guardar configuración original
        Stroke oldStroke = g2d.getStroke();
        Composite oldComposite = g2d.getComposite();
        RenderingHints oldHints = g2d.getRenderingHints();
        Color oldColor = g2d.getColor();

        // Configurar transparencia (dinámico)
        if (transparencia) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

        // Configurar alisado
        if (alisar) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                RenderingHints.VALUE_ANTIALIAS_ON);
        }

        // Configurar grosor (dinámico)
        g2d.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        g2d.setPaint(color);

        // Dibujar
        if (relleno) {
            g2d.fill(linea);
        }
        g2d.draw(linea);

        // Dibujar bounding box si está seleccionada
        if (seleccionada) {
            drawBoundingBox(g2d);
        }

        // Restaurar configuración
        g2d.setStroke(oldStroke);
        g2d.setComposite(oldComposite);
        g2d.setRenderingHints(oldHints);
        g2d.setPaint(oldColor);
    }
    
    /**
     * Obtiene la posición de la línea, definida por su punto inicial.
     *
     * @return  Punto inicial de la línea como un Point2D.
     */
    @Override
    public Point2D getLocation() {
        return new Point2D.Float(linea.x1, linea.y1);
    }
    
    /**
     * Establece una nueva ubicación para la línea, desplazándola desde su
     * posición actual según la diferencia respecto a la última posición del ratón.
     *
     * @param pos  Nueva posición del ratón.
     */
    @Override
    public void setLocation(Point2D pos) {
        if (lastMousePosition != null) {
            // Usamos el desplazamiento desde el último punto del mouse
            double dx = pos.getX() - lastMousePosition.getX();
            double dy = pos.getY() - lastMousePosition.getY();
            
            // Aplicamos el desplazamiento a ambos puntos
            linea.x1 += dx;
            linea.y1 += dy;
            linea.x2 += dx;
            linea.y2 += dy;
            
            // Actualizamos la última posición del mouse
            lastMousePosition = pos;
        }
    }


    
    @Override
    public Rectangle getBounds() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Rectangle2D getBounds2D() {
        return linea.getBounds2D();
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