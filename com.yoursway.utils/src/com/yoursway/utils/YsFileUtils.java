package com.yoursway.utils;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.yoursway.utils.relativepath.Pathes.relativePath;
import static java.util.Collections.emptyList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.yoursway.utils.relativepath.RelativePath;

public class YsFileUtils {
    
    public static final String UTF8_ENCODING = "utf-8";
    
    public static Object readAsObject(File source) throws IOException, ClassNotFoundException {
        FileInputStream in = new FileInputStream(source);
        try {
            ObjectInputStream oi = new ObjectInputStream(in);
            return oi.readObject();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void writeObject(Object object, File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        try {
            writeObject(object, out);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeObject(Object object, OutputStream out) throws IOException {
        ObjectOutputStream oo = new ObjectOutputStream(out);
        oo.writeObject(object);
        oo.flush();
    }
    
    public static String readAsString(File source) throws IOException {
        return readAsString(source, UTF8_ENCODING);
    }
    
    public static String readAsString(File source, String encoding) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        loadFromFile(source, baos);
        byte[] bytes = baos.toByteArray();
        return new String(bytes, encoding);
    }
    
    public static String readAsStringAndClose(InputStream source) throws IOException {
        try {
            return readAsString(source, UTF8_ENCODING);
        } finally {
            source.close();
        }
    }
    
    public static String readAsString(InputStream source) throws IOException {
        return readAsString(source, UTF8_ENCODING);
    }
    
    public static String readAsString(InputStream source, String encoding) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        transfer(source, baos);
        byte[] bytes = baos.toByteArray();
        return new String(bytes, encoding);
    }
    
    public static void writeString(File destination, String data) throws IOException {
        writeString(destination, data, UTF8_ENCODING);
    }
    
    public static void writeString(File destination, String data, String encoding) throws IOException {
        writeBytes(destination, data.getBytes(encoding));
    }
    
    public static void writeBytes(File destination, byte[] data) throws IOException {
        saveToFile(new ByteArrayInputStream(data), destination);
    }
    
    public static void writeStringAndClose(OutputStream destination, String data) throws IOException {
        try {
            writeString(destination, data, UTF8_ENCODING);
        } finally {
            destination.close();
        }
    }
    
    public static void writeString(OutputStream destination, String data) throws IOException {
        writeString(destination, data, UTF8_ENCODING);
    }
    
    public static void writeString(OutputStream destination, String data, String encoding) throws IOException {
        writeBytes(destination, data.getBytes(encoding));
    }
    
    public static void writeBytes(OutputStream destination, byte[] data) throws IOException {
        transfer(new ByteArrayInputStream(data), destination);
    }
    
    public static void cp_r(File source, File destinationParentFolder) throws IOException {
        List<File> e = emptyList();
        cp_r_exclude(source, destinationParentFolder, e);
    }
    
    public static void cp_r_exclude(File source, File destinationParentFolder, Collection<File> excluded)
            throws IOException {
        destinationParentFolder.mkdirs();
        cp_r_(source, destinationParentFolder, excluded);
    }
    
    /**
     * Prereq: <code>destinationParentFolder</code> exists.
     * 
     * @param excluded
     */
    private static void cp_r_(File source, File destinationParentFolder, Collection<File> excluded)
            throws IOException {
        if (excluded.contains(source))
            return;
        if (!source.isDirectory()) {
            fileCopy(source, new File(destinationParentFolder, source.getName()));
        } else {
            File childrenDestination = new File(destinationParentFolder, source.getName());
            cp_r_children(source, childrenDestination, excluded);
        }
    }
    
    public static void cp_r_children(File source, File childrenDestination) throws IOException {
        List<File> e = emptyList();
        cp_r_children(source, childrenDestination, e);
    }
    
    public static void cp_r_children(File source, File childrenDestination, Collection<File> excluded)
            throws IOException {
        childrenDestination.mkdirs();
        
        File[] children = source.listFiles();
        if (children != null)
            for (File child : children)
                cp_r_(child, childrenDestination, excluded);
    }
    
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
    
    public static void loadFromFile(File src, OutputStream out) throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(src);
        try {
            transfer(in, out);
        } finally {
            in.close();
        }
    }
    
    public static void transfer(InputStream in, OutputStream out) throws IOException {
        byte[] buf = allocateBuffer();
        int len;
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
    }
    
    public static void transfer(InputStream in, OutputStream out, int maxBytes) throws IOException {
        byte[] buf = new byte[maxBytes];
        int done = 0;
        int len;
        while (done < maxBytes && (len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
            done += len;
        }
    }
    
    private static byte[] allocateBuffer() {
        return new byte[1024 * 1024];
    }
    
    public static File createTempFolder(String prefix, String suffix) throws IOException {
        File file = File.createTempFile(prefix, suffix);
        return new File(file.getParentFile(), file.getName() + ".dir");
    }
    
    public static File findLatestOsgiBundle(File folder, String bundleName) {
        return chooseLatestVersion(findOsgiBundles(folder, bundleName));
    }
    
    public static File chooseLatestVersion(Collection<File> jars) {
        List<File> jarsList = newArrayList(jars);
        if (jarsList.isEmpty())
            return null;
        Collections.sort(jarsList, YsStrings.toStringComparator(YsStrings.getNaturalComparatorAscii()));
        return jarsList.get(jarsList.size() - 1);
    }
    
    public static Collection<File> findOsgiBundles(File folder, String bundleName) {
        Collection<File> result = newArrayList();
        File[] files = folder.listFiles();
        if (files != null)
            for (File file : files)
                addPluginIfMatches(file, bundleName, result);
        return result;
    }
    
    public static void addPluginIfMatches(File folderOrJar, String bundleName, Collection<File> result) {
        String name = folderOrJar.getName();
        if (folderOrJar.isFile()) {
            if (name.equals(bundleName + ".jar") || name.startsWith(bundleName + "_")
                    && name.endsWith(".jar"))
                result.add(folderOrJar);
        } else {
            if (name.equals(bundleName) || name.startsWith(bundleName + "_"))
                result.add(folderOrJar);
        }
    }
    
    public static File urlToFileWithProtocolCheck(URL url) {
        if (!url.getProtocol().equals("file"))
            throw new IllegalArgumentException("URL is not a file: " + url);
        return new File(url.getPath());
    }
    
    public static void deleteFile(File file) {
        if (file.exists() && !file.delete())
            throw new RuntimeException("Cannot delete file " + file);
    }
    
    public static void deleteRecursively(File directory) {
        File[] children = directory.listFiles();
        if (children != null) {
            for (File child : children)
                if (child.isDirectory())
                    deleteRecursively(child);
                else
                    deleteFile(child);
            
            if (!directory.delete())
                throw new RuntimeException("Cannot delete directory " + directory);
        }
    }
    
    public static void zipFolderContents(File folder, File zip) throws IOException {
        zip(zip, newArrayList(new ZipRoot(folder, "")));
    }
    
    public static void zip(File zip, Collection<ZipRoot> roots) throws IOException {
        FileOutputStream fout = new FileOutputStream(zip);
        ZipOutputStream out = new ZipOutputStream(fout);
        try {
            for (ZipRoot root : roots)
                zipChildren(root.folder(), root.prefix(), out);
        } finally {
            out.close();
        }
    }
    
    public static void zipChildren(File folder, String prefix, ZipOutputStream out) throws IOException {
        File[] files = folder.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            if (file.isFile()) {
                String name = prefix + file.getName();
                ZipEntry entry = new ZipEntry(name);
                entry.setTime(file.lastModified());
                out.putNextEntry(entry);
                loadFromFile(file, out);
                out.closeEntry();
            } else if (file.isDirectory()) {
                zipChildren(file, prefix + file.getName() + "/", out);
            }
        }
        
    }
    
    public static void rewriteZipBasic(File source, OutputStream outf, Map<String, InputStream> overrides)
            throws IOException {
        InputStream inf = new FileInputStream(source);
        try {
            rewriteZipBasic(inf, outf, overrides);
        } finally {
            inf.close();
        }
    }
    
    public static void rewriteZip(File source, OutputStream outf, Map<String, StreamFilter> overrides)
            throws IOException {
        InputStream inf = new FileInputStream(source);
        try {
            rewriteZip(inf, outf, overrides);
        } finally {
            inf.close();
        }
    }
    
    public static void rewriteZipBasic(InputStream source, OutputStream outf,
            Map<String, InputStream> overrides) throws IOException {
        Map<String, StreamFilter> data = newHashMap();
        for (Map.Entry<String, InputStream> entry : overrides.entrySet())
            data.put(entry.getKey(), new CopyFromStreamFilter(entry.getValue()));
        rewriteZip(source, outf, data);
    }
    
    public static void rewriteZip(InputStream source, OutputStream outf, Map<String, StreamFilter> rewrites)
            throws IOException {
        Map<String, StreamFilter> rewritesRemaining = newHashMap(rewrites);
        ZipInputStream in = new ZipInputStream(source);
        ZipOutputStream out = new ZipOutputStream(outf);
        ZipEntry entry;
        while (null != (entry = in.getNextEntry())) {
            String name = entry.getName();
            StreamFilter filter = rewritesRemaining.remove(name);
            if (filter == null) {
                out.putNextEntry(entry);
                transfer(in, out);
            } else {
                ZipEntry newEntry = new ZipEntry(name);
                newEntry.setTime(entry.getTime());
                out.putNextEntry(newEntry);
                filter.process(in, out);
            }
        }
        for (Map.Entry<String, StreamFilter> item : rewritesRemaining.entrySet()) {
            out.putNextEntry(new ZipEntry(item.getKey()));
            item.getValue().process(new ByteArrayInputStream(new byte[0]), out);
        }
        out.finish();
    }
    
    public static String readFileOrZipAsString(File directoryOrArchive, String relativePath)
            throws IOException {
        return readFileOrZipAsString(directoryOrArchive, relativePath, UTF8_ENCODING);
    }
    
    public static String readFileOrZipAsString(File directoryOrArchive, String relativePath, String encoding)
            throws IOException {
        byte[] bytes = readFileOrZipAsBytes(directoryOrArchive, relativePath);
        return new String(bytes, encoding);
    }
    
    public static byte[] readFileOrZipAsBytes(File directoryOrArchive, String relativePath)
            throws IOException {
        InputStream in = openFileOrZip(directoryOrArchive, relativePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        transfer(in, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }
    
    public static InputStream openFileOrZip(File directoryOrArchive, String relativePath) throws IOException {
        if (directoryOrArchive.isDirectory()) {
            return new FileInputStream(new File(directoryOrArchive, relativePath));
        } else {
            ZipFile file = new ZipFile(directoryOrArchive);
            ZipEntry entry = file.getEntry(relativePath);
            if (entry == null)
                throw new IOException("Entry " + relativePath + " not found in " + directoryOrArchive);
            InputStream in = file.getInputStream(entry);
            return new DelegatingZipClosingInputStream(in, file);
        }
    }
    
    public static RelativePath calculateRelativePath(File root, File path) {
        return calculateRelativePath_(root, path, relativePath(""));
    }
    
    private static RelativePath calculateRelativePath_(File root, File path, RelativePath currentPath) {
        if (root == null)
            return null;
        if (root.equals(path))
            return currentPath;
        return calculateRelativePath_(root, path.getParentFile(), relativePath(path.getName()).append(
                currentPath));
    }
    
    /**
     * @deprecated Use {@link YsPathUtils#joinPath(String,String)} instead
     */
    public static String joinPath(String a, String b) {
        return YsPathUtils.joinPath(a, b);
    }

    public static boolean isBogusFile(String name) {
        return Pattern.compile("^([._](git|svn|darcs)|CVS|\\.DS_Store)$").matcher(name).find();
    }
    
}
