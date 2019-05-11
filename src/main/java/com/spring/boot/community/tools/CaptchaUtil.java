package com.spring.boot.community.tools;
import java.awt.Color;  
import java.awt.Font;  
import java.awt.Graphics;  
import java.awt.image.BufferedImage;  
import java.util.Random;  
 
public class CaptchaUtil {  
    private BufferedImage image;
    private String str;
    private static char code[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789".toCharArray();  
  
    public static final String SESSION_CODE_NAME="code";  
      
    private CaptchaUtil() {  
        init();
    }  
  
     
    public static CaptchaUtil Instance() {  
        return new CaptchaUtil();  
    }  
   
    public BufferedImage getImage() {  
        return this.image;  
    }  
    
    public String getString() {  
        return this.str;  
    }  
  
    private void init() {  
        
        int width = 85, height = 20;  
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
       
        Graphics g = image.getGraphics();  
       
        Random random = new Random();  
    
        g.setColor(getRandColor(200, 250));  
        g.fillRect(0, 0, width, height);  
       
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));  
       
        g.setColor(getRandColor(160, 200));  
        for (int i = 0; i < 155; i++) {  
            int x = random.nextInt(width);  
            int y = random.nextInt(height);  
            int xl = random.nextInt(12);  
            int yl = random.nextInt(12);  
            g.drawLine(x, y, x + xl, y + yl);  
        }  
     
        String sRand = "";  
        for (int i = 0; i < 4; i++) {  
            String rand = String.valueOf(code[random.nextInt(code.length)]);  
            sRand += rand;  
          
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));  
          
            g.drawString(rand, 13 * i + 6, 16);  
        }  
        
        this.str = sRand;  
  
       
        g.dispose();  
        
        this.image = image;
    }  
    
    private Color getRandColor(int fc, int bc) {  
        Random random = new Random();  
        if (fc > 255)  
            fc = 255;  
        if (bc > 255)  
            bc = 255;  
        int r = fc + random.nextInt(bc - fc);  
        int g = fc + random.nextInt(bc - fc);  
        int b = fc + random.nextInt(bc - fc);  
        return new Color(r, g, b);  
    }  
}   
