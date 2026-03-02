/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SM.JACM.Imagen;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;

/**
 *
 * @author ASUS
 */
public class RedOp extends BufferedImageOpAdapter{

    private int umbral;

    public RedOp(int umbral) {
        this.umbral = umbral;
    }
    
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        if (src == null) {
            throw new NullPointerException("src image is null");
        }
        if (dest == null) {
            dest = createCompatibleDestImage(src, null);
        }
        WritableRaster srcRaster = src.getRaster();
        WritableRaster destRaster = dest.getRaster();
        int[] pixelComp = new int[srcRaster.getNumBands()];
        int[] pixelCompDest = new int[srcRaster.getNumBands()];

        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                srcRaster.getPixel(x, y, pixelComp);

                if (srcRaster.getNumBands() >= 3) { //Para imágenes RGB
                    int R = pixelComp[0];
                    int G = pixelComp[1];
                    int B = pixelComp[2];
                    int media = (R + G + B) / 3;

                    if ((R - (G + B)) > umbral) {
                        //Mantener el color original
                        pixelCompDest[0] = R;
                        pixelCompDest[1] = G;
                        pixelCompDest[2] = B;
                    } else {
                        //Convertir a gris
                        pixelCompDest[0] = media;
                        pixelCompDest[1] = media;
                        pixelCompDest[2] = media;
                    }

                    //Si hay canal alpha, se copia
                    if (srcRaster.getNumBands() == 4) {
                        pixelCompDest[3] = pixelComp[3];
                    }
                } else {
                    //Para imágenes en escala de grises, se usa el valor y se ajusta como gris
                    int gray = pixelComp[0];
                    pixelCompDest[0] = gray;
                    pixelCompDest[1] = gray;
                    pixelCompDest[2] = gray;
                }

                destRaster.setPixel(x, y, pixelCompDest);
            }
        }

        return dest;
    }
}
    
