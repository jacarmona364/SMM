/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SM.JACM.Imagen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;

/**
 *
 * @author ASUS
 */
public class TonoOp extends BufferedImageOpAdapter{
    
    private Color C;
    private int umbral;
    private int desplazamiento;

    public TonoOp(Color C, int umbral, int desplazamiento) {
        this.C = C;
        this.umbral = umbral;
        this.desplazamiento = desplazamiento;
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

        float[] hsbPixel = new float[3];
        float[] hsbTarget = new float[3];

        //Obtener el tono del color C
        Color.RGBtoHSB(C.getRed(), C.getGreen(), C.getBlue(), hsbTarget);
        float hueC = hsbTarget[0] * 360;

        int numBands = srcRaster.getNumBands();
        int[] pixel = new int[numBands];

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                srcRaster.getPixel(x, y, pixel);

                int R = pixel[0];
                int G = pixel[1];
                int B = pixel[2];

                Color.RGBtoHSB(R, G, B, hsbPixel);
                float huePx = hsbPixel[0] * 360;

                float diff = Math.abs(huePx - hueC);
                float dist = Math.min(diff, 360 - diff);

                if (dist <= umbral) {
                    // Cambiar el tono
                    float newHue = (huePx + desplazamiento) % 360;
                    float newHueNorm = newHue / 360f;
                    int rgb = Color.HSBtoRGB(newHueNorm, hsbPixel[1], hsbPixel[2]);

                    pixel[0] = (rgb >> 16) & 0xFF;
                    pixel[1] = (rgb >> 8) & 0xFF;
                    pixel[2] = rgb & 0xFF;
                } else {
                    // Convertir a escala de grises
                    int gray = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                    pixel[0] = gray;
                    pixel[1] = gray;
                    pixel[2] = gray;
                }

                // Mantener canal alfa si lo hay
                if (numBands == 4) {
                    pixel[3] = pixel[3];
                }

                destRaster.setPixel(x, y, pixel);
            }
        }


        return dest;
    }
    
}
