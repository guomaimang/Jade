package tech.hirsun.jade.utils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ThumbnailUtil {

    public static void generateThumbnail(String inputPath, String outputPath,Integer pixelX, Integer pixelY) throws Exception {
        File imageFile = new File(inputPath);
        InputStream inputStream = new FileInputStream(imageFile);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        if (width > pixelX || height > pixelY) {
            if (width * 1.0 / height >= 2.0 || height * 1.0/width >= 2.0) {
                int newWidth = Math.min(width, height);
                Thumbnails.of(inputPath)
                        .sourceRegion(Positions.CENTER, newWidth, newWidth).size(pixelX, pixelY)
                        .keepAspectRatio(false)
                        .toFile(outputPath);
            }else {
                Thumbnails.of(inputPath)
                        .size(pixelX, pixelY)
                        .toFile(outputPath);
            }
        }else{
            // copy the file from inputPath to outputPath.jpg, use java.nio.file.Files.copy
            java.nio.file.Files.copy(imageFile.toPath(), new File(outputPath).toPath());

        }
    }

    public static void main(String[] args) throws Exception {
        generateThumbnail("/Users/hanjiaming/project/Jade/Server/jade/resource/picture/1/dd.png",
                "/Users/hanjiaming/project/Jade/Server/jade/resource/picture/1/test.jpg",200,300);
    }

}
