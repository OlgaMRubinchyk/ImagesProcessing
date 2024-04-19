import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class BMPImage {
    private Header header;
    private InfoHeader infoHeader;
    byte[] rawData;
    Pixel[][] imageData;
    Pixel[][] OriginalImageData;
    BMPImage(String fileName){
        //File myObj = new File("filename.txt");
        try {
            rawData = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (rawData[0] != 'B' || rawData[1] != 'M') {
//            if (!(rawData[0] == 'B' && rawData[1] == 'M')) {
            throw new RuntimeException("File is not BMP image!!!");
        }

        header = new Header();

        infoHeader = new InfoHeader();
        infoHeader.width = byteToInt(rawData, 18, 21);
        infoHeader.height = byteToInt(rawData, 22, 25);
        infoHeader.bitCount = byteToInt(rawData, 28, 29);
        infoHeader.compression = byteToInt(rawData, 30, 33);

        // hometask
        infoHeader.imageSize = byteToInt(rawData, 34, 37);
        infoHeader.xPixelsPerM = byteToInt(rawData, 38, 41);
        infoHeader.yPixelsPerM = byteToInt(rawData, 42, 45);
        infoHeader.colorsUsed = byteToInt(rawData, 46, 49);

        fillPixelData();


    }

    private int byteToInt(byte inputBytes[], int startIndex, int endIndex) {
        int number = 0;
        byte[] bytes = Arrays.copyOf(inputBytes, inputBytes.length);
        if (endIndex - startIndex > 1) {
            bytes = convertToBigEndian(bytes, startIndex, 4);
        } else {
            bytes = convertToBigEndian(bytes, startIndex, 2);
        }
        for (int i = startIndex; i < endIndex; i++) {
            number = number | (bytes[i] & 0xFF);
            number = number << 8;
        }
        number = number | (bytes[endIndex] & 0xFF);
        return number;
    }

    private byte[] convertToBigEndian(byte bytes[], int startIndex, int count) {
        byte tmp;
        if (count == 2) {
            tmp = bytes[startIndex];
            bytes[startIndex] = bytes[startIndex+1];
            bytes[startIndex+1] = tmp;
            return bytes;
        }

        tmp = bytes[startIndex];
        bytes[startIndex] = bytes[startIndex+3];
        bytes[startIndex+3] = tmp;
        tmp = bytes[startIndex+1];
        bytes[startIndex+1] = bytes[startIndex+2];
        bytes[startIndex+2] = tmp;
        return bytes;
    }

    public void saveBMP(String fileName) {
        byte[] data = new byte[rawData.length];
        for (int i = 0; i < rawData.length; i++) {
            data[i] = rawData[i];
        }

        int counter = 0;
        int k = 54;
        for (int i = 0; i < imageData[0].length; i++) {
            for (int j = 0; j < imageData[i].length; j++, k+=3) {
                data[k] = imageData[i][j].red;
                data[k+1] = imageData[i][j].green;
                data[k+2] = imageData[i][j].blue;
                counter++;
            }
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            try {
                fileOutputStream.write(data);
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    private void fillPixelData() {
        imageData = new Pixel[infoHeader.width][infoHeader.height];
        OriginalImageData = new Pixel[infoHeader.width][infoHeader.height];
        for (int i = 0; i < imageData[0].length; i++) {
            for (int j = 0; j < imageData[i].length; j++) {
                imageData[i][j] = new Pixel();
                OriginalImageData[i][j] = new Pixel();

            }
        }

        int k = 54;
        for (int i = 0; i < imageData[0].length; i++) {
            for (int j = 0; j < imageData[i].length; j++, k+=3) {
                imageData[i][j].red = rawData[k];
                imageData[i][j].green = rawData[k+1];
                imageData[i][j].blue = rawData[k+2];

                OriginalImageData[i][j].red = rawData[k];
                OriginalImageData[i][j].green = rawData[k+1];
                OriginalImageData[i][j].blue = rawData[k+2];
            }
        }

    }

    public void grayScale() {

        for (int i = 0; i < imageData[0].length; i++) {
            for (int j = 0; j < imageData[i].length; j++) {
                byte average = (byte) ((imageData[i][j].red + imageData[i][j].green + imageData[i][j].blue)/3);
                //byte average = (byte) ((0.299*imageData[i][j].red + 0.587*imageData[i][j].green + 0.114*imageData[i][j].blue));
                imageData[i][j].red = average;
                imageData[i][j].green = average;
                imageData[i][j].blue = average;

            }
        }

    }

    public Pixel[][] getPixels() {
        return imageData;
    }

    public Pixel[][] getOriginalPixels() {
        return OriginalImageData;
    }

    public int getWidth() {
        return infoHeader.width;
    }

    public int getHeight() {
        return infoHeader.height;
    }
}

class Header {
    public int signature;
    public int fileSize;
    public int reserved = 0;
    public int dataOffset;
}

class InfoHeader {
    public int size = 40;
    public int width;
    public int height;
    public int bitCount;
    public int compression;
    public int imageSize;
    public int xPixelsPerM;
    public int yPixelsPerM;
    public int colorsUsed;
}

class Pixel {
    public byte red;
    public byte green;
    public byte blue;

}