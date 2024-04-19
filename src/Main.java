public class Main {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        BMPImage image = new BMPImage("Lenna.bmp");
        Window before = new Window(image.getWidth(), image.getHeight());
        before.setPixelData(image.getOriginalPixels());


        image.grayScale();
        image.saveBMP("test_10245.bmp");


        Window after = new Window(image.getWidth(), image.getHeight());
        after.setPixelData(image.getPixels());
    }
}