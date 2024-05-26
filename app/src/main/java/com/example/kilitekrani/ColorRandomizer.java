package com.example.kilitekrani;

import android.graphics.Color;
public class ColorRandomizer {
    public static int rastgeleRenkOlustur() {
        return Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }

    public static void main(String[] args) {
        int rastgeleRenk = rastgeleRenkOlustur();
        System.out.println("Oluşturulan Rastgele Renk Kodu: #" + Integer.toHexString(rastgeleRenk));
    }
}
