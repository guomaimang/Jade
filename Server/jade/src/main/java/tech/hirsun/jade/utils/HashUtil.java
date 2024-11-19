package tech.hirsun.jade.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {

    /**
     * This is a fixed salt, which is used to prevent rainbow table attack.
     * length >= 8
     */
    private static final String defaultFixedSalt = "o1dr7u5io9";

    /**
     * @param str: the string to be hashed
     * @return the hashed string of SHA256
     */
    private static String sha256(String str) {
        return DigestUtils.sha256Hex(str);
    }

    /**
     * @param plainPass: the password input by the user in apps
     * @return the password after formPlainPassToMidPass
     */
    private static String formPlainPassToMidPass(String plainPass,String fixedSalt) {
        String midPass = String.valueOf(fixedSalt.charAt(0)) + fixedSalt.charAt(6) + plainPass + fixedSalt.charAt(4) + fixedSalt.charAt(7);
        return sha256(midPass);
    }

    /**
     * @param midPass: the password after formPlainPassToMidPass
     * @param randomSalt: the random salt generated by the server, must >= 6
     */
    private static String formMidPassToDBPass(String midPass, String randomSalt) {
        String finalPass = String.valueOf(randomSalt.charAt(2)) + randomSalt.charAt(5) + midPass + randomSalt.charAt(3) + randomSalt.charAt(1);
        return sha256(finalPass);
    }

    /**
     * @param plainPass: the password input by the user in apps
     * @param randomSalt: the random salt generated by the server, must >= 6
     * @return the password for database
     */
    public static String formPlainPassToDBPass(String plainPass, String randomSalt, String fixedSalt) {
        if (fixedSalt == null || fixedSalt.isEmpty()) {
            fixedSalt = defaultFixedSalt;
        }
        String midPass = formPlainPassToMidPass(plainPass, fixedSalt);
        return formMidPassToDBPass(midPass, randomSalt);
    }

    public static void main(String[] args) {
        System.out.println(sha256("hongshu"));
        System.out.println(formPlainPassToDBPass("3bfee8da00881801d4cccfe1e8549e9dde7c5cb8559984905831e68da01c4bbb","MB6uwS", ""));
    }

}