/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kattistool;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;

/**
 *
 * @author dahjon
 */
public class KTUTils {

    public static void debug(Object str) {
        //System.out.println(str);
    }

    static String removeExtension(String fileName, String extension) {
        if (fileName.endsWith(extension)) {
            return fileName.substring(0, fileName.length() - extension.length());
        }
        return fileName;
    }

    static String replace(String code, final String regExp, final String replacement) {
        Pattern p = Pattern.compile(regExp); //"int\\s*\\("
        //code = code.replaceAll(regEx, "Integer.pareInt(\"\"+");
        
//        if(code.contains("println")){
//            debug("---------------\nprintln på raden code: '"+code+"', regExp: "+regExp+ ", replacement: "+replacement);
//            
//        }
        Matcher m = p.matcher(code);
        while (m.find()) {
            debug("Subsequence "+regExp+" found");
            String res = m.group();
            debug("res = '" + res+"' codeBefore"+code);
            code = code.replace(res, res.charAt(0)+replacement);
            debug("---------------\ncode: '"+code+"', regExp: "+regExp+ ", replacement: "+replacement);

        }
        debug("No more found");
        return code;
    }

    static boolean findFunction(final String funktionsNamn, String code) {
        final String regExp = "[^\\w\\.]" + funktionsNamn + "\\s*\\(";
        Pattern p = Pattern.compile(regExp); //"int\\s*\\("
        Matcher m = p.matcher(code);
        return m.find();
    }

    static String replaceFunction(final String funktionsNamn, String newName, String code) {
        final String regExp = "[^\\w\\.]" + funktionsNamn + "\\s*\\(";
        final String replacement = newName + "(";
        return KTUTils.replace(code, regExp, replacement);
    }

    public static String joinAndRemoveNull(String[] rows) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            if (row != null) {
                sb.append(row);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static boolean osIsWindows() {
        String os = System.getProperty("os.name");
        debug("os = " + os);
        return os.startsWith("Windows");
    }

    static public String findHomeDir() {

        String base = System.getenv("user.home");
        debug("user.home: " + base);
        if (base == null) {
            base = System.getenv("HOME");
            debug("HOME: " + base);

        }
        if (base == null) {
            base = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        }
        return base;
    }

    static public String getNetbeansProjectDir() {
        return findHomeDir() + File.separator + "NetBeansProjects";
    }

    public static boolean saveJavaFile(String fileName, String content) {
        String destDir = getKattisSaveDir();
        return saveIfDirExists(fileName, destDir, content, ".java");
    }

    public static String getKattisSaveDir() {
        String destDir = getNetbeansProjectDir() + File.separator + "Kattis";
        return destDir;
    }

    public static boolean saveIfDirExists(String fileName, String destDir, String content, String extention) {
        if (new File(destDir).exists()) {
            if (new File(destDir, "src").exists()) {
                destDir = destDir + File.separator + "src";
            }
            if (extention != null) {
                if (!extention.contains(".")) {
                    extention = "." + extention;
                }
                if (!fileName.toLowerCase().endsWith(extention)) {
                    fileName += extention;
                }
            }
            String filePath = destDir + File.separator + fileName;
            try {
                JDFiles.string2File(filePath, content);
                return true;
            } catch (IOException e) {
                debug("Kunde inte spara fil: " + filePath + "\n" + e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

//        public static String getDefaultJavaSourceDir() {
//        String sourceDir = "";
//        String SOURCE_DIR_NAME = "NetBeansProjects";
//        if (osIsWindows()) {
//            String windoc = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
//            //debug("windoc = " + windoc);
//            sourceDir = windoc + File.separator + SOURCE_DIR_NAME;
//        } else {
//            String userDir = System.getProperty("user.home");
//            //debug("userDir = " + userDir);
//            sourceDir = userDir + File.separator + SOURCE_DIR_NAME;
//        }
//        //debug("sourceDir = " + sourceDir);
//        return sourceDir;
//    }
    public static void main(String[] args) {
        //String home = findHomeDir();
        //debug("home = " + home);
        String s = "println(\"Hej,, alla, barn\"+6)";
        System.out.println("s = " + s);
        System.out.println(isCommaOutsideQuote(s));
        s = "println(\"Hej alla barn\",8)";
        System.out.println("s = " + s);
        System.out.println(isCommaOutsideQuote(s));
    }

    public static boolean isCommaOutsideQuote(String s) {
        boolean inQuote = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (inQuote) {
                if (c == '"') {
                    inQuote = false;
                }
            } else {
                if (c == '"') {
                    inQuote = true;

                }
                else if(c == ','){
                    return true;
                }
            }
        }
        return false;
    }

}
