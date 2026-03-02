package SM.JACM.iu;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

import SM.JACM.Graficos.MiShape;
import SM.JACM.Graficos.jaCurva;
import SM.JACM.Graficos.jaElipse;
import SM.JACM.Graficos.jaLinea;
import SM.JACM.Graficos.jaRectangulo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Clase que representa un lienzo de dibujo, extendiendo JPanel, donde se pueden dibujar figuras.
 * Esta clase maneja la creación, modificación y visualización de diferentes formas geométricas.
 * Los atributos y métodos permiten trabajar con herramientas de dibujo como líneas, curvas, 
 * rectángulos, elipses y la manipulación de figuras.
 * 
 * @author ASUS
 */
public class jaLienzo extends javax.swing.JPanel {
    
    /**
    * Variable que almacena la figura actualmente seleccionada o que se está dibujando.
    */
   private MiShape figura;
   
   private MiShape figuraSeleccionada = null;

   /**
    * Color predeterminado para las figuras dibujadas. Se establece en negro por defecto.
    */
   private Color color = Color.black;

   /**
    * Grosor de las líneas de las figuras. Se establece en 1 por defecto.
    */
   private int grosor = 1; 

   /**
    * Bandera que indica si la figura debe tener relleno. 
    * Se establece en false por defecto (sin relleno).
    */
   private boolean relleno = false;

   /**
    * Bandera que indica si la figura debe ser dibujada con transparencia.
    * Se establece en false por defecto (sin transparencia).
    */
   private boolean transparencia = false;

   /**
    * Bandera que indica si las líneas deben ser alisadas.
    * Se establece en false por defecto (sin alisar).
    */
   private boolean alisar = false;

   /**
    * Bandera que indica si el modo de curvado está activado o no.
    * Cuando está en false, la herramienta se utiliza para dibujar una línea recta, 
    * y cuando está en true, se puede modificar la curvatura de la figura.
    */
   private boolean modoCurvado = false; // Hacerlo en 2 pasos: crear la línea y cambiar la curvatura

   private boolean modoEdicion = false;
   
   private boolean fijar = false;
   
   private boolean eliminar = false;
   
   private boolean transformar = false;

   private boolean transformarColor = false;

   
   /**
    * Herramienta actualmente seleccionada para dibujar en el lienzo.
    * Se inicializa en "VACIO", lo que significa que no hay herramienta seleccionada.
    */
   private jaHerramientaDibujo herramienta = jaHerramientaDibujo.VACIO;

   /**
    * Punto donde se hizo clic por primera vez en el lienzo. 
    * Se inicializa en (-100, -100), fuera de los límites del lienzo, 
    * para indicar que no se ha registrado un clic aún.
    */
   private Point pressed = new Point(-100, -100);

   /**
    * Punto de control para ajustar la curvatura de una curva o línea. 
    * Se usa solo cuando el modoCurvado está activado.
    */
   private Point puntoControlBase;

   /**
    * El último punto donde el mouse fue presionado o arrastrado en el lienzo. 
    * Se usa para mover las figuras seleccionadas y calcular el desplazamiento.
    */
   private Point2D lastPoint;

   /**
    * Lista de todas las figuras que se han dibujado en el lienzo. 
    * Estas figuras se almacenan como objetos de tipo MiShape.
    */
   private List<MiShape> listaFiguras = new ArrayList();
   
   
   private BufferedImage img;
   
   private URL sonidoFijar, sonidoEliminar;
   
   private int giro;
   

    /**
    * Constructor de la clase jaLienzo. Inicializa los componentes del lienzo.
    */
    public jaLienzo() {
        initComponents();
    }
    
    /**
    * Método sobrescrito para dibujar las figuras en el lienzo.
    * Este método se llama automáticamente cuando el lienzo necesita ser redibujado.
    * 
    * @param g El objeto Graphics utilizado para dibujar sobre el lienzo.
    */
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        
        if(img!=null) g2d.drawImage(img,0,0,this);
        
        //Recorte
        Shape clipArea;
        clipArea = new Rectangle2D.Float(0,0,img.getWidth(),img.getHeight());
        g2d.setClip(clipArea);
        
        for(MiShape s: listaFiguras) {
            s.draw(g2d);
            if (modoEdicion) {
                s.drawBoundingBox(g2d); // Dibuja bounding box si está seleccionada
            }
        }
        
    }
    
    /**
    * Obtiene la herramienta de dibujo actualmente seleccionada.
    * 
    * @return La herramienta de dibujo seleccionada.
    */
    public jaHerramientaDibujo getHerramienta(){
        return herramienta;
    }

    /**
    * Obtiene el color utilizado para dibujar las figuras.
    * 
    * @return El color actual utilizado para dibujar.
    */
    public Color getColor() {
        return color;
    }

    /**
    * Establece un nuevo color para las figuras a dibujar.
    * Este método también provoca un redibujo del lienzo.
    * 
    * @param color El nuevo color a establecer.
    */
    public void setColor(Color color) {
        this.color = color;
        repaint();
        //Meter un if para darle color a la figura que esté seleccionada y en modo editar
    }
    
    /**
    * Verifica si las figuras se dibujarán con relleno.
    * 
    * @return true si las figuras tienen relleno, false en caso contrario.
    */
    public boolean isRelleno(){
        return relleno;
    }
    
    /**
    * Establece si las figuras deben tener relleno o no.
    * Este método también provoca un redibujo del lienzo.
    * 
    * @param relleno true para habilitar el relleno, false para deshabilitarlo.
    */
    public void setRelleno(boolean relleno){
        this.relleno = relleno;
        repaint();
    }
    
    /**
    * Verifica si las figuras se dibujarán con transparencia.
    * 
    * @return true si las figuras tienen transparencia, false en caso contrario.
    */
    public boolean isTransparencia(){
        return transparencia;
    }
    
    /**
    * Establece si las figuras deben ser dibujadas con transparencia o no.
    * Este método también provoca un redibujo del lienzo.
    * 
    * @param transparencia true para habilitar la transparencia, false para deshabilitarla.
    */
    public void setTransparencia(boolean transparencia){
        this.transparencia = transparencia;
        repaint();
    }
        
    /**
    * Verifica si las figuras deben ser alisadas (suavizadas) al ser dibujadas.
    * 
    * @return true si las figuras deben ser alisadas, false en caso contrario.
    */
    public boolean isAlisar(){
        return alisar;
    }
    
    /**
    * Establece si las figuras deben ser alisadas (suavizadas) al ser dibujadas.
    * Este método también provoca un redibujo del lienzo.
    * 
    * @param alisar true para habilitar el alisado, false para deshabilitarlo.
    */
    public void setAlisar(boolean alisar){
        this.alisar = alisar;
        repaint();
    }
    
    /**
    * Establece la herramienta de dibujo que se va a utilizar.
    * 
    * @param herramienta La herramienta de dibujo a seleccionar.
    */
    public void setHerramienta(jaHerramientaDibujo herramienta){
        this.herramienta = herramienta;
    }
    
    /**
    * Limpia el lienzo, eliminando todas las figuras y redibujando el panel.
    * Este método elimina todas las figuras del lienzo y luego llama a repaint
    * para asegurarse de que el panel se redibuje sin ningún contenido.
    */
    public void limpiaLienzo(){
        listaFiguras.clear();
        repaint();
    }
    
    /**
    * Establece el grosor de las figuras a dibujar.
    * Este método también provoca un redibujo del lienzo.
    * 
    * @param grosor El nuevo grosor para las figuras.
    */
    public void setGrosor(int grosor) {
        this.grosor = grosor;
        repaint();
    } 
    
    /**
    * Obtiene el grosor actual utilizado para dibujar las figuras.
    * 
    * @return El grosor actual de las figuras.
    */
    public int getGrosor(){
        return grosor;
    }
    
    public boolean isFijar(){
        return fijar;
    }
    
    public void setFijar(boolean fijar){
        this.fijar = fijar;
        repaint();
    }
    
    public boolean isEliminar(){
        return eliminar;
    }
    
    public void setEliminar(boolean eliminar){
        this.eliminar = eliminar;
        repaint();
    }
    
    public void setImage(BufferedImage img){
        this.img = img;
        if(img!=null) {
            setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
        }
    }
    
    public BufferedImage getImage(){
        return img;
    }
    
    public BufferedImage getPaintedImage() {
        // Crear una nueva imagen con el mismo tamaño y tipo que la original
        BufferedImage imgout = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        // Crear un Graphics2D para dibujar sobre la nueva imagen
        Graphics2D g2dImagen = imgout.createGraphics();

        // Dibujar la imagen de fondo
        if (img != null) {
            deseleccionarTodasFiguras();
            g2dImagen.drawImage(img, 0, 0, null);
        }

        // Dibujar las figuras sobre la imagen
        for (MiShape s : listaFiguras) {
            s.draw(g2dImagen);  // Se asume que MiShape tiene un método draw(Graphics2D) que dibuja la forma
        }

        // Liberar recursos del Graphics2D
        g2dImagen.dispose();

        return imgout;  // Retornar la nueva imagen combinada
    }
    
    public void setModoEdicion(boolean activar) {
        this.modoEdicion = activar;
        if (!activar) {
            deseleccionarTodasFiguras();
        }
        repaint();
    }
        
    // Método para seleccionar una figura
    private void seleccionarFigura(MiShape figura) {
        deseleccionarTodasFiguras();
        if (figura != null) {
            figura.seleccionar();
            figuraSeleccionada = figura;
            // Sincronizar atributos del lienzo con la figura seleccionada
            this.color = figura.getColor();
            this.grosor = figura.getGrosor();
            this.alisar = figura.isAlisar();
            this.relleno = figura.isRelleno();
            this.alisar = figura.isAlisar();
        }else{
            deseleccionarTodasFiguras();
        }
    }
    
    private void deseleccionarTodasFiguras() {
        for (MiShape figura : listaFiguras) {
            figura.deseleccionar();
        }
    }
    
     // Método para actualizar el color de la figura seleccionada
    public void setColorFiguraSeleccionada(Color color) {
        for (MiShape figura : listaFiguras) {
            if (figura.isSeleccionada()) {
                figura.setColor(color);
                repaint();
            }
        }
        
    }

    // Método para actualizar el grosor de la figura seleccionada
    public void setGrosorFiguraSeleccionada(int grosor) {
        for (MiShape figura : listaFiguras) {
            if (figura.isSeleccionada()) {
                figura.setGrosor(grosor);
                repaint();
            }
        }
        
    }

    // Método para actualizar el relleno de la figura seleccionada
    public void setRellenoFiguraSeleccionada(boolean relleno) {
        for (MiShape figura : listaFiguras) {
            if (figura.isSeleccionada()) {
                figura.setRelleno(relleno);
                repaint();
            }
        }
        
    }
    
    // Método para actualizar la transparencia de la figura seleccionada
    public void setTransparenciaFiguraSeleccionada(boolean transparencia) {
        for (MiShape figura : listaFiguras) {
            if (figura.isSeleccionada()) {
                figura.setTransparencia(transparencia);
                repaint();
            }
        }
        
    }
    
    // Método para actualizar el alisado de la figura seleccionada
    public void setAlisadoFiguraSeleccionada(boolean alisado) {
        for (MiShape figura : listaFiguras) {
            if (figura.isSeleccionada()) {
                figura.setAlisar(alisado);
                repaint();
            }
        }
        
    }
    
    
    public void setSonidoFijar(URL sonidoFijar){
        this.sonidoFijar = sonidoFijar;
    }
    
    public void setSonidoEliminar(URL sonidoEliminar){
        this.sonidoEliminar = sonidoEliminar;
    }
    
    private void play(URL f) {
        try {
            Clip sound = AudioSystem.getClip();
            sound.open(AudioSystem.getAudioInputStream(f));
            sound.start();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    
    private void fijarFigura(MiShape figura) {
        if (img != null) {
            // 1. Dibujar la figura en la imagen de fondo
            deseleccionarTodasFiguras();
            Graphics2D g2d = img.createGraphics();
            figura.draw(g2d);
            g2d.dispose();

            // 2. Eliminar de la lista de figuras
            listaFiguras.remove(figura);
            
            // Reproducir sonido
            if (sonidoFijar != null) {
                play(sonidoFijar);
            }
            
            repaint();
        }
    }

    private void eliminarFigura(MiShape figura) {
        // Simplemente eliminar de la lista
        listaFiguras.remove(figura);
        
        // Reproducir sonido
        if (sonidoEliminar != null) {
            play(sonidoEliminar);
        }
        
        repaint();
    }
    
    private int getGiro() {
        return giro;
    }
    
    private void setGiro(int giro) {
        this.giro = giro;
    }
    
    public boolean isTransformar() {
        return transformar;
    }

    public void setTransformar(boolean transformar) {
        this.transformar = transformar;
    }
    
    public boolean isTransformarColor() {
        return transformarColor;
    }

    public void setTransformarColor(boolean transformarColor) {
        this.transformarColor = transformarColor;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(255, 255, 255));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    /**
    * Evento que se dispara cuando se presiona el botón del ratón sobre el lienzo.
    * Dependiendo de la herramienta seleccionada, se inicia la creación o manipulación
    * de una figura en el lienzo.
    *
    * @param evt Evento del ratón que contiene información como la posición del clic.
    */
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if (herramienta == jaHerramientaDibujo.MOVER){
            for (MiShape s : listaFiguras) {
                if (s.contains(evt.getPoint())) {
                    seleccionarFigura(s);
                    lastPoint = evt.getPoint();
                    s.setLastMousePosition(lastPoint);
                    
                    break;
                }else{
                    deseleccionarTodasFiguras();
                }
            }
            
            repaint();

//            if (figura != null) {
//                // Guardar la posición inicial relativa al punto de referencia
//                lastPoint = evt.getPoint();
//                figura.setLastMousePosition(lastPoint);
//            }
            
        } else if (herramienta == jaHerramientaDibujo.LINEA) { // Crear cada figura con sus propiedades
            figura = new jaLinea(evt.getPoint(), evt.getPoint(), color, grosor, transparencia, alisar); // Crea la línea en pressed
            listaFiguras.add(figura);
            repaint();

        } else if (herramienta == jaHerramientaDibujo.RECTANGULO) {
            pressed = evt.getPoint();
            figura = new jaRectangulo(evt.getPoint().x, evt.getPoint().y, 0, 0, color, grosor, relleno, transparencia, alisar); 
            listaFiguras.add(figura);
            
        }else if(herramienta == jaHerramientaDibujo.ELIPSE){
            pressed = evt.getPoint();
            figura = new jaElipse(evt.getPoint().x, evt.getPoint().y, 0, 0, color, grosor, relleno, transparencia, alisar); 
            listaFiguras.add(figura);
        
        } else if (herramienta == jaHerramientaDibujo.CURVA){
            if (!modoCurvado) {
                // Fase 1: Crear curva inicial (línea recta)
                pressed = evt.getPoint();
                figura = new jaCurva(pressed.x, pressed.y, pressed.x, pressed.y, color, grosor, transparencia, alisar);
                listaFiguras.add(figura);
            } else {
                // Fase 2: Verificar si se hizo clic cerca del punto de control
                if (figura instanceof jaCurva) {
                    jaCurva curva = (jaCurva) figura;
                    if (curva.isNearControl(evt.getPoint())) {
                        puntoControlBase = evt.getPoint(); // Guardar posición inicial para el arrastre
                    }
                }
            }
        } 
    }//GEN-LAST:event_formMousePressed
    
    /**
    * Evento que se dispara cuando se arrastra el ratón sobre el lienzo.
    * Dependiendo de la herramienta seleccionada, se actualizan las figuras
    * en el lienzo, ya sea moviéndolas, ajustando sus tamaños o cambiando
    * su forma en función del desplazamiento del ratón.
    *
    * @param evt Evento del ratón que contiene información sobre la posición actual del clic.
    */
    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (herramienta == jaHerramientaDibujo.MOVER && figura != null && lastPoint != null) {
            // Calcula la nueva posición basada en el desplazamiento
            double newX = evt.getX();
            double newY = evt.getY();

            // Crea el punto de destino
            Point2D newPos = new Point2D.Double(newX, newY);

            // Mueve la figura estableciendo su nueva ubicación
            for (MiShape s : listaFiguras) {
                if (s.isSeleccionada()) {
                    s.setLocation(newPos);
                    
                    break;
                }
            }
            

            // Actualiza el último punto
            lastPoint = evt.getPoint();
            repaint();
            
        } else if (herramienta == jaHerramientaDibujo.LINEA) {
            // Actualizar el punto final de la línea
            ((jaLinea)figura).setLine(
                ((jaLinea)figura).getLocation(), 
                evt.getPoint()
            );
            repaint();

        } else if (herramienta == jaHerramientaDibujo.RECTANGULO) {
            ((jaRectangulo) figura).setFrameFromDiagonal(pressed, evt.getPoint());
            repaint();
            
        } else if (herramienta == jaHerramientaDibujo.ELIPSE){
            ((jaElipse) figura).setFrameFromDiagonal(pressed, evt.getPoint());
            repaint();
            
        } else if (herramienta == jaHerramientaDibujo.CURVA) {
             if (!modoCurvado) {
                // Fase 1: Dibujar línea recta
                ((jaCurva) figura).setLine(pressed.x, pressed.y, evt.getX(), evt.getY());
            } else {
                // Fase 2: Ajustar curvatura
                if (puntoControlBase != null && figura instanceof jaCurva) {
                    Point2D nuevoCtrl = new Point2D.Float(evt.getX(), evt.getY());
                    ((jaCurva) figura).setControl(nuevoCtrl);
                }
            }
             
            repaint();
        }
    }//GEN-LAST:event_formMouseDragged
    
    /**
    * Evento que se dispara cuando se suelta el ratón sobre el lienzo.
    * Esta función maneja el cambio de modo de curvatura para la herramienta CURVA.
    * Si la herramienta seleccionada es CURVA y el modo de curvatura no está activado,
    * se activa el modo curvado. Si el modo de curvatura ya está activado, se desactiva.
    *
    * @param evt Evento del ratón que contiene información sobre la posición en la que se suelta el clic.
    */
    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if (herramienta == jaHerramientaDibujo.CURVA && !modoCurvado) {
            modoCurvado = true;
            System.out.println("Modo curvatura ACTIVADO");
        }else if (herramienta == jaHerramientaDibujo.CURVA && modoCurvado) {
            modoCurvado = false;
            System.out.println("Modo curvatura DESACTIVADO");
        }
    }//GEN-LAST:event_formMouseReleased

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if (herramienta == jaHerramientaDibujo.FIJAR){
            for (MiShape s : listaFiguras) {
                if (s.contains(evt.getPoint())) {
                    seleccionarFigura(s);
                    fijarFigura(figuraSeleccionada);
                    break;
                }else{
                    deseleccionarTodasFiguras();
                }
            }
        }else if (herramienta == jaHerramientaDibujo.ELIMINAR){
            for (MiShape s : listaFiguras) {
                if (s.contains(evt.getPoint())) {
                    seleccionarFigura(s);
                    eliminarFigura(figuraSeleccionada);
                    break;
                }else{
                    deseleccionarTodasFiguras();
                }
            }
        }
    }//GEN-LAST:event_formMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
