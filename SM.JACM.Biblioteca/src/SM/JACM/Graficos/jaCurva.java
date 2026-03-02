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
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

/**
 * Clase que representa una curva cuadrática dibujable sobre un componente gráfico.
 * Extiende de la clase abstracta MiShape e incluye propiedades como grosor, color,
 * transparencia, alisado y métodos para manipular la curva.
 * 
 * @author ASUS
 */
public class jaCurva extends MiShape {
    /**
    * Atributo que representa una curva cuadrática 2D utilizando coordenadas flotantes (float).
    * Este atributo se utiliza para definir una curva cuadrática en el lienzo, especificando 
    * dos puntos de control y el punto de destino.
    */
   private QuadCurve2D.Float curva;

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
     * Constructor para crear una curva cuadrática entre dos puntos.
     * 
     * @param x1 Coordenada X del punto inicial.
     * @param y1 Coordenada Y del punto inicial.
     * @param x2 Coordenada X del punto final.
     * @param y2 Coordenada Y del punto final.
     * @param color Color del trazo de la curva.
     * @param grosor Grosor del trazo.
     * @param transparencia True si se desea transparencia en la curva.
     * @param alisar True si se desea suavizado de bordes (antialiasing).
     */
    public jaCurva(float x1, float y1, float x2, float y2, 
                  Color color, int grosor, boolean transparencia, boolean alisar) {
        super(color, grosor, false, transparencia, alisar);
        float ctrlx = (x1 + x2) / 2f;
        float ctrly = (y1 + y2) / 2f;
        this.curva = new QuadCurve2D.Float(x1, y1, ctrlx, ctrly, x2, y2);
       
    }
    
    /**
     * Dibuja la curva en el contexto gráfico proporcionado.
     * 
     * @param g2d El contexto gráfico donde se dibujará la curva.
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
            g2d.fill(curva);
        }
        g2d.draw(curva);

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
     * Obtiene la posición del punto inicial de la curva.
     * 
     * @return Un Point2D con la posición (x1, y1).
     */
    @Override
    public Point2D getLocation() {
        return new Point2D.Float(curva.x1, curva.y1);
    }
    
    /**
     * Cambia la posición de la curva trasladándola en base a la última posición del mouse.
     * 
     * @param pos Nueva posición del mouse.
     */
    @Override
    public void setLocation(Point2D pos) {
        if (lastMousePosition != null) {
            // Calcular desplazamiento desde la última posición del mouse
            double dx = pos.getX() - lastMousePosition.getX();
            double dy = pos.getY() - lastMousePosition.getY();

            // Actualizar todos los puntos de la curva
            float newX1 = (float)(curva.x1 + dx);
            float newY1 = (float)(curva.y1 + dy);
            float newX2 = (float)(curva.x2 + dx);
            float newY2 = (float)(curva.y2 + dy);
            float newCtrlX = (float)(curva.ctrlx + dx);
            float newCtrlY = (float)(curva.ctrly + dy);

            // Asignar nuevos valores
            curva.x1 = newX1;
            curva.y1 = newY1;
            curva.x2 = newX2;
            curva.y2 = newY2;
            curva.ctrlx = newCtrlX;
            curva.ctrly = newCtrlY;

            // Actualizar la curva
            curva.setCurve(newX1, newY1, newCtrlX, newCtrlY, newX2, newY2);

            // Guardar nueva posición del mouse
            lastMousePosition = pos;
        }
    }
    
    /**
     * Verifica si un punto está cerca del punto de control de la curva.
     * 
     * @param p Punto a comprobar.
     * @return true si está a menos de 200 unidades del punto de control.
     */
    public boolean isNearControl(Point2D p) {
        return this.getCtrlPt().distance(p) <= 200.0;
    }
    
    /**
     * Asigna nuevos extremos a la curva, recalculando el punto de control automáticamente.
     * 
     * @param x1 Coordenada X del nuevo punto inicial.
     * @param y1 Coordenada Y del nuevo punto inicial.
     * @param x2 Coordenada X del nuevo punto final.
     * @param y2 Coordenada Y del nuevo punto final.
     */
    public void setCurva(float x1, float y1, float x2, float y2) {
        float ctrlx = (x1 + x2) / 2f;
        float ctrly = (y1 + y2) / 2f;
        curva.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
    }
    
    /**
     * Devuelve el punto de control actual de la curva.
     * 
     * @return Un Point2D con las coordenadas del punto de control.
     */
    public Point2D getCtrlPt() {
        return new Point2D.Float(curva.ctrlx, curva.ctrly);
    }
        
    /**
     * Define los extremos de la curva y posiciona el punto de control en el centro.
     * 
     * @param x1 Coordenada X del extremo inicial.
     * @param y1 Coordenada Y del extremo inicial.
     * @param x2 Coordenada X del extremo final.
     * @param y2 Coordenada Y del extremo final.
     */
    public void setLine(float x1, float y1, float x2, float y2) {
        float ctrlX = (x1 + x2) / 2;
        float ctrlY = (y1 + y2) / 2;
        curva.setCurve(x1, y1, ctrlX, ctrlY, x2, y2);
    }
    
    /**
     * Establece una nueva posición para el punto de control de la curva.
     * 
     * @param p Nueva posición del punto de control.
     */
    public void setControl(Point2D p) {
        curva.ctrlx = (float)p.getX();
        curva.ctrly = (float)p.getY();
        curva.setCurve(curva.x1, curva.y1, curva.ctrlx, curva.ctrly, curva.x2, curva.y2);
    }
    
    /**
     * Comprueba si un punto está dentro del área de influencia de la curva
     * (en cualquiera de sus extremos o cerca del punto de control).
     * 
     * @param p Punto a comprobar.
     * @return true si el punto está dentro del área de detección.
     */
    @Override
    public boolean contains(Point2D p) {
        double dist = 50.0;
        return curva.getP1().distance(p) <= dist || 
               curva.getP2().distance(p) <= dist ||
               isNearControl(p);
    }

    

    @Override
    public Rectangle getBounds() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Rectangle2D getBounds2D() {
        return curva.getBounds2D();
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
