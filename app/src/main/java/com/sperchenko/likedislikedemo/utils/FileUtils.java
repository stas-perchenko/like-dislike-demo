package com.sperchenko.likedislikedemo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by stanislav.perchenko on 12/2/2015.
 */
public class FileUtils {

    public static final String IMAGE_EXTENSION = ".png";

    public static final String ASSETS_DIR = "assets";


    public static File getIconFileById(Context context, int iconId) throws ExternalMediaException {
        return new File(getFinalAssetsCacheDirectory(context), iconId+IMAGE_EXTENSION);
    }

    public static File getFinalAssetsCacheDirectory(Context context) throws ExternalMediaException {
        File dir = FileUtils.getDiskCacheDir(context, ASSETS_DIR);
        return FileUtils.createDirIfNeeded(dir);
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use
        // external cache dir
        // otherwise use internal cache dir
        boolean useExternalMemory = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !FileUtils.isExternalStorageRemovable();
        File cacheFolder;
        if (useExternalMemory) {
            cacheFolder = FileUtils.getExternalCacheDir(context);
        } else {
            try {
                cacheFolder = context.getCacheDir();
            } catch (Exception e) {
                cacheFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); // Lastly, try this as a last ditch effort
            }
        }

        return TextUtils.isEmpty(uniqueName) ? cacheFolder : new File(cacheFolder.getPath() + File.separator + uniqueName);
    }

    /**
     * Get the external app cache directory.
     *
     * @param context
     *            The context to use
     * @return The external cache dir
     */
    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
    @TargetApi(9)
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Creates a directory and puts a .nomedia file in it
     *
     *
     * @param dir
     * @return new dir
     * @throws ExternalMediaException
     */
    private static File createDirIfNeeded(File dir) throws ExternalMediaException {
        if ((dir != null) && !dir.exists()) {
            if (!dir.mkdirs() && !dir.isDirectory()) {
                throw new ExternalMediaException("error create directory");
            }
            File noMediaFile = new File(dir, ".nomedia");
            try {
                noMediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                throw new ExternalMediaException("error create .nomedia file");
            }
        }
        return dir;
    }





    public static int deleteFileOrFolder(File f) {
        if (f == null) return 0;
        if (!f.isDirectory()) {
            return f.delete() ? 1 : 0;
        } else {
            int ndel = clearFolder(f);
            boolean deleted = f.delete();
            if (deleted) {
                ndel ++;
            }
            return ndel;
        }
    }

    public static int clearFolder(File folder) {
        int numDeleted = 0;
        if ((folder == null) || !folder.isDirectory()) {
            return numDeleted;
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    numDeleted += clearFolder(f);
                } else {
                    if (f.delete()) {
                        numDeleted ++;
                    }
                }
            }
        }
        return numDeleted;
    }


    /**
     *
     * @param sourceFile
     * @param destFile
     * @param maxSize
     * @return Number of bytes copied
     * @throws IOException
     */
    public static long copyFile(File sourceFile, File destFile, long maxSize) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(destFile);
            source = fis.getChannel();
            destination = fos.getChannel();
            long count = 0;
            long size = source.size();
            if (size > maxSize) size = maxSize;
            while((count += destination.transferFrom(source, count, size-count)) < size);
            return count;
        } finally {
            if(source != null) {
                source.close();
            }
            if (fis != null) {
                fis.close();
            }
            if(destination != null) {
                destination.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static long copyFileToStream(File fDst, InputStream src) {

        BufferedOutputStream bos = null;
        long count = 0;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(fDst));

            byte[] buffer = new byte[1024];
            int len;
            while ((len = src.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
                count += len;
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) { }
            }
        }
        return count;
    }


    public static boolean saveToFile(File f, byte[] rawData) {
        if (f.exists()) {
            if (!f.delete()) return false;
        }

        boolean ret = true;
        ByteBuffer buffer = ByteBuffer.wrap(rawData);
        FileChannel dest = null;
        try {
            dest = new FileOutputStream(f).getChannel();
            dest.write(buffer);
        } catch (FileNotFoundException e) {
            ret = false;
        } catch (IOException e) {
            ret = false;
        } finally {
            if (dest != null) {
                try {
                    dest.close();
                } catch (IOException e) { }
            }
        }
        return ret;
    }


    /**
     * Tryes to create new file. If already exists - delete the previous
     * @param path
     * @return
     * @throws IOException
     */
    public static File createFileFromPath(String path) throws IOException {
        File f = new File(path);
        if (f.createNewFile()) {
            return f;
        } else {
            if (f.delete()) {
                if (f.createNewFile()) {
                    return f;
                } else {
                    throw new IOException("file "+f.getAbsolutePath()+" can not be created.");
                }
            } else {
                throw new IOException("file "+f.getAbsolutePath()+" already exists and can not be deleted.");
            }
        }
    }
}
