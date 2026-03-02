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
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Clase jaElipse que representa una elipse dibujable en pantalla.
 * Extiende de la clase abstracta MiShape e incorpora características como
 * relleno, transparencia, suavizado y grosor del borde.
 * 
 * Permite mover la elipse mediante interacción con el ratón.
 * 
 * @author ASUS
 */
public class jaElipse extends MiShape {
    /**
    * Atributo que representa una elipse 2D utilizando coordenadas flotantes (float).
    * Se utiliza para definir una elipse en el lienzo con posiciones y dimensiones precisas.
    */
   private Ellipse2D.Float elipse;

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
     * Constructor de jaElipse. Inicializa una elipse con los parámetros
     * visuales y de comportamiento especificados.
     *
     * @param x              Coordenada X de la esquina superior izquierda de la elipse.
     * @param y              Coordenada Y de la esquina superior izquierda de la elipse.
     * @param ancho          Ancho de la elipse.
     * @param alto           Alto de la elipse.
     * @param color          Color del borde (y relleno si aplica).
     * @param grosor         Grosor del borde.
     * @param relleno        Si true, la elipse se dibuja rellena.
     * @param transparencia  Si true, se aplica transparencia al dibujo.
     * @param alisar         Si true, se aplican técnicas de suavizado (antialiasing).
     */
    public jaElipse(float x, float y, float ancho, float alto, 
                   Color color, int grosor, boolean relleno, 
                   boolean transparencia, boolean alisar) {
        super(color, grosor, relleno, transparencia, alisar);
        this.elipse = new Ellipse2D.Float(x, y, ancho, alto);
        
    }
    
    /**
     * Dibuja la elipse en el contexto gráfico dado, respetando las propiedades
     * de estilo como color, grosor, relleno, transparencia y suavizado.
     *
     * @param g2d Objeto Graphics2D sobre el que se realiza el dibujo.
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
            g2d.fill(elipse);
        }
        g2d.draw(elipse);

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
     * Obtiene la posición de la elipse (esquina superior izquierda).
     *
     * @return Punto con las coordenadas actuales de la elipse.
     */
    @Override
    public Point2D getLocation() {
        return new Point2D.Float(elipse.x, elipse.y);
    }
    
    /**
     * Mueve la elipse a una nueva posición calculada a partir de la
     * diferencia entre la nueva posición del ratón y la última conocida.
     *
     * @param pos Nueva posición del ratón.
     */
    @Override
    public void setLocation(Point2D pos) {
        if (lastMousePosition != null) {
            // Usamos el desplazamiento desde el último punto del mouse
            double dx = pos.getX() - lastMousePosition.getX();
            double dy = pos.getY() - lastMousePosition.getY();
            
            // Aplicamos el desplazamiento a la posición de la elipse
            elipse.x += dx;
            elipse.y += dy;
            
            // Actualizamos la última posición del mouse
            lastMousePosition = pos;
        } else {
            // Comportamiento de respaldo
            elipse.x = (float)pos.getX();
            elipse.y = (float)pos.getY();
        }
    }
    
    /**
     * Verifica si un punto dado se encuentra dentro de la elipse.
     *
     * @param p Punto a comprobar.
     * @return true si el punto está dentro de la elipse, false en caso contrario.
     */
    @Override
    public boolean contains(Point2D p) {
        return elipse.contains(p);
    }
    
    public void setFrameFromDiagonal(Point2D p1, Point2D p2) {
        elipse.setFrameFromDiagonal(p1, p2);
    }

    
    
    //Usar para marcar la figura seleccionada y poder editar
    @Override
    public Rectangle getBounds() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Rectangle2D getBounds2D() {
        return elipse.getBounds2D();
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