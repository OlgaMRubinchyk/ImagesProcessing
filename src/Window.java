import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Window extends JFrame {


    private int width_;
    private int height_;
    private Pixel[][] pixels;

    public Window(int width, int height) {
        width_ = width;
        height_ = height;
        setTitle("Pixel Drawer");
        setSize(width_, height_);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPixels((Graphics2D) g);
            }
        };

        setContentPane(panel);
    }

    public void setPixelData(Pixel[][] pix){
        pixels = new Pixel[width_][height_];
        for(int x = 0; x < width_; x++){
            for(int y = 0; y < height_; y++){
                pixels[x][y] = pix[x][y];
            }
        }
        reverse();
    }

    public void reverse(){
        for (int i = 0; i < pixels.length / 2; i++) {
            Pixel[] temp = pixels[i];
            pixels[i] = pixels[pixels.length - 1 - i];
            pixels[pixels.length - 1 - i] = temp;
        }
    }

    private void drawPixels(Graphics2D g2d) {
        BufferedImage image = new BufferedImage(width_, height_, BufferedImage.TYPE_INT_BGR);

        for (int y = 0; y < width_; y++) {
            for (int x = 0; x < height_; x++) {
                int r = pixels[y][x].red & 0xff;
                int g = pixels[y][x].green & 0xff;
                int b = pixels[y][x].blue & 0xff;
                int color = (b << 16) | (g << 8) | r;
                image.setRGB(x, y, color);
            }
        }
        g2d.drawImage(image, 0, 0, width_, height_, null);
    }

}
