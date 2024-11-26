package tech.hirsun.jade.utils;

public class CharUtil {
    public static boolean safeCheck(String str) {

        // Check if the string is null or empty
        if (str == null || str.isEmpty()) {
            return false;
        }

        // check if the string contains illegal characters, only allow letters, numbers, and underscores and dot. or Chinese characters
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_' && c != '.' && !Character.toString(c).matches("[\u4e00-\u9fa5]")) {
                return false;
            }
        }

        return true;
    }
}
