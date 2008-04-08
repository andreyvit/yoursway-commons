package com.yoursway.utils;

import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class YsFileUtils {
    
    public static void fileCopy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            saveToFile(in, dst);
        } finally {
            in.close();
        }
    }
    
    public static void download(URL url, File dst) throws IOException {
        InputStream in = url.openStream();
        try {
            saveToFile(in, dst);
        } finally {
            in.close();
        }
    }
    
    public static void saveToFile(InputStream in, File dst) throws FileNotFoundException, IOException {
        OutputStream out = new FileOutputStream(dst);
        try {
            transfer(in, out);
        } finally {
            out.close();
        }
    }
    
    public static void transfer(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024 * 1024];
        int len;
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
    }
    
    public static File createTempFolder(String prefix, String suffix) throws IOException {
        File file = File.createTempFile(prefix, suffix);
        return new File(file.getParentFile(), file.getName() + ".dir");
    }
    
    public static File findEclipsePluginJar(File folder, String bundleName) {
        List<File> jars = newArrayList(findEclipsePluginJars(folder, bundleName));
        if (jars.isEmpty())
            return null;
        Collections.sort(jars, YsStrings.toStringComparator(YsStrings.getNaturalComparatorAscii()));
        return jars.get(jars.size() - 1);
    }
    
    public static Collection<File> findEclipsePluginJars(File folder, String bundleName) {
        Collection<File> result = newArrayList();
        File[] files = folder.listFiles();
        if (files != null)
            for (File file : files)
                if (file.isFile()) {
                    String name = file.getName();
                    if (name.equals(bundleName + ".jar") || name.startsWith(bundleName + "_")
                            && name.endsWith(".jar"))
                        result.add(file);
                }
        return result;
    }
    
}
