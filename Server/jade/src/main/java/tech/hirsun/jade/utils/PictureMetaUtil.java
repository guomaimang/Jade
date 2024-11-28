package tech.hirsun.jade.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


@Slf4j
public class PictureMetaUtil {

    public static HashMap printImageTags(File file) throws ImageProcessingException, IOException {
        HashMap<String, String> tagMap = new HashMap<>();
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
//                log.info("Tag: {}", tag);
                if (tag.getTagName().equals("Image Height")) {
                    tagMap.put("Image Height", tag.getDescription());
                } else if (tag.getTagName().equals("Image Width")) {
                    tagMap.put("Image Width", tag.getDescription());
                } else if (tag.getTagName().equals("Date/Time Original")) {
                    tagMap.put("Date/Time Original", tag.getDescription());
                } else if (tag.getTagName().equals("GPS Latitude")) {
                    tagMap.put("GPS Latitude", convertLtdeToNum(tag.getDescription()));
                } else if (tag.getTagName().equals("GPS Longitude")) {
                    tagMap.put("GPS Longitude", convertLtdeToNum(tag.getDescription()));
                } else if (tag.getTagName().equals("Make")){
                    tagMap.put("Make", tag.getDescription());
                } else if (tag.getTagName().equals("Model")){
                    tagMap.put("Model", tag.getDescription());
                } else if (tag.getTagName().equals("Aperture Value")){
                    tagMap.put("Aperture Value", tag.getDescription());
                } else if (tag.getTagName().equals("Exposure Time")){
                    tagMap.put("Exposure Time", tag.getDescription());
                } else if (tag.getTagName().equals("ISO Speed Ratings")){
                    tagMap.put("ISO Speed Ratings", tag.getDescription());
                } else if (tag.getTagName().equals("Focal Length")){
                    tagMap.put("Focal Length", tag.getDescription());
                }
            }
        }
        return tagMap;
    }


    private static String convertLtdeToNum(String point) {
        double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());
        double fen = Double.parseDouble(point.substring(point.indexOf("°") + 1, point.indexOf("'")).trim());
        double miao = Double.parseDouble(point.substring(point.indexOf("'") + 1, point.indexOf("\"")).trim());
        double duStr = du + fen / 60 + miao / 60 / 60;
        return String.valueOf(duStr);
    }

    public static void main(String[] args) {
        try {
            File file = new File("/Users/hanjiaming/Downloads/IMG_20241128_194513.jpg");
            HashMap<String, String> tagMap = printImageTags(file);
            log.info("Image Height: {}", tagMap.get("Image Height"));
            log.info("Image Width: {}", tagMap.get("Image Width"));
            log.info("Date/Time Original: {}", tagMap.get("Date/Time Original"));
            log.info("GPS Latitude: {}", tagMap.get("GPS Latitude"));
            log.info("GPS Longitude: {}", tagMap.get("GPS Longitude"));
            log.info("Make: {}", tagMap.get("Make"));
            log.info("Model: {}", tagMap.get("Model"));
            log.info("Aperture Value: {}", tagMap.get("Aperture Value"));
            log.info("Exposure Time: {}", tagMap.get("Exposure Time"));
            log.info("ISO Speed Ratings: {}", tagMap.get("ISO Speed Ratings"));
            log.info("Focal Length: {}", tagMap.get("Focal Length"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

