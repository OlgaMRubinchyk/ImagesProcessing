public class Main {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        BMPImage image = new BMPImage("Lenna_1024.bmp");
        image.grayScale();
        image.saveBMP("test_1024.bmp");

    }
}