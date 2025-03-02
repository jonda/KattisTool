package kattistool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dahjon
 */
public class JDFiles {

    public static List<String> file2list(String fil) throws IOException {
        //trace("innan path");
        Path path = Paths.get(fil);
        //trace("skapat path: " + path);
        List<String> stringList = Files.readAllLines(path, StandardCharsets.UTF_8);
        return stringList;
    }

    public static String[] file2arr(String fil) throws IOException {
        List<String> list = file2list(fil);
        String[] arr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String el = list.get(i);
            arr[i] = el;
        }
        return arr;
    }

    public static void arr2file(String[] arr, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
        //writer.write();
        //System.out.println("------------------Skriver till Fil-------------------");
        for (int i = 0; i < arr.length; i++) {
            String row = arr[i];
            //System.out.println("row = " + row);
            writer.write(row + "\n");
        }
        //writer.write();
        writer.close();
    }

    public static String file2String(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
        return content;
    }

    public static void string2File(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes("UTF-8"));
    }

    public static void string2FileIso(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes("iso-8859-1"));
    }

    public static ArrayList<File> searchFolder(File parent, String extention) {
        ArrayList<File> ret = new ArrayList<>();

        // Populates the array with names of files and directories
        File[] names = parent.listFiles();
        if (names != null) {
            ArrayList<File> nameList = new ArrayList(Arrays.asList(names));

            nameList.sort(null);
//        System.out.println("searchFolder parent: " + parent + ", names = " + Arrays.deepToString(names));
            // For each pathname in the pathnames array
            for (File path : nameList) {

                // Print the names of files and directories
                //System.out.println(path);
                if (path.getName().endsWith("." + extention)) {
                    ret.add(path);
                }
                if (path.isDirectory() && !path.getName().equals("venv")) {
                    //System.out.println("S\u00f6ker \u00e4ven i: " + path);
                    ret.addAll(searchFolder(path, extention));
                }
            }
            //System.out.println("searchFolder " + parent + " ret = " + ret);
        }
        return ret;
    }

    public static void main(String[] args) {
        String pathStr = "\\\\SR-Disk-2\\Hem0010\\dahjon\\Mina dokument\\NetbeansProjects";
        File path = new File(pathStr);
        File latestFile = searchLatest(path, "java");
        //System.out.println("found = " + found);
        System.out.println("latestFile = " + latestFile);
        System.out.println("latestFile = " + latestFile.lastModified());
    }

    public static File searchLatest(File path, String extention) {
        ArrayList<File> found = searchFolder(path, extention);
        long latestTime = 0;
        File latestFile = null;
        for (int i = 0; i < found.size(); i++) {
            File fil = found.get(i);
            long time = fil.lastModified();
            //System.out.println("time = " + time);
            if (time > latestTime) {
                latestTime = time;
                latestFile = fil;
            }

        }
        return latestFile;
    }
}
