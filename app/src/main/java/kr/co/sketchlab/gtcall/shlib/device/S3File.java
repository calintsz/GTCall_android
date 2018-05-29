package kr.co.sketchlab.gtcall.shlib.device;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class S3File {
	
	
	public static String getFilename() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		return "IMG_"+ timeStamp + ".jpg";
	}

	public static String getGalleryDirectory(String appDir) {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appDir);

		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				return null;
			}
		}

		return mediaStorageDir.getPath();
	}

	public static File createTempFile(String prefix, String suffix, File directory) throws IOException {
		// Force a prefix null check first
		if (prefix.length() < 3) {
			throw new IllegalArgumentException("prefix must be at least 3 characters");
		}
		if (suffix == null) {
			suffix = ".tmp";
		}
		File tmpDirFile = directory;
		if (tmpDirFile == null) {
			String tmpDir = System.getProperty("java.io.tmpdir", ".");
			tmpDirFile = new File(tmpDir);
		}
		File result;
		result = new File(tmpDirFile, prefix + suffix);

		Random tempFileRandom = new Random();
		while (!result.createNewFile()) {
			result = new File(tmpDirFile, prefix + tempFileRandom.nextInt() + suffix);
		}
		return result;
	}

    public static File createTempFile(Context context) {
        File tmpDir = context.getCacheDir();
        try {
            return File.createTempFile("tmp", ".tmp", tmpDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] InputStreamToByteArr(InputStream is) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toByteArray();
    }

    public static boolean inputStreamToFile(InputStream inputStream, File outputFile) {
        try {
            OutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int read;

            try {
                while ((read = inputStream.read(buffer)) != -1)
                    outputStream.write(buffer, 0, read);

                outputStream.flush();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
	
	public static boolean deleteFile(String filepath) {
		File file = new File(filepath);
		if(file.exists())
			return file.delete();
		return false;
	}

		
	public static File[] loadSavedFiles() {
		
		File path = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "InstMemo");

	    if (path.exists()){
	    	
			
			File f = new File(path.getPath());
			
			File file[] = f.listFiles();
			
			if (file.length > 0) {
				return file;
			}     	
	    }
	    
	    File emptyFilesArray[] = { };
		return emptyFilesArray;
		
	}
	
	public static void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}
    public static long getExternalStorageFreeSpaceInMb() {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        long free = externalStorageDir.getFreeSpace() / 1024 / 1024;
        return free;
    }
	public static String getExt(File f) {
		String filename = f.getAbsolutePath();
		String filenameArray[] = filename.split("\\.");
		String extension = filenameArray[filenameArray.length-1];
		return extension;
	}
	
	public static Date getDateOfFile(String paht) {
		File file = new File(paht);
		Date fileDate = new Date(file.lastModified());
		return fileDate;
	}
	
	public static String getDateOfFile2(String paht) {
		File file = new File(paht);
		Date fileDate = new Date(file.lastModified());
        SimpleDateFormat df = new SimpleDateFormat("MM.dd.yyyy");
		String formattedDate = df.format(fileDate.getTime());
		return formattedDate;
	}
    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }


	public static String getAssetString(Context context, String fileName) {
		AssetManager am = context.getResources().getAssets();
		InputStream is = null;
		try {
			is = am.open(fileName, AssetManager.ACCESS_BUFFER);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			int i;
			try {
				i = is.read();
				while (i != -1)
				{
					byteArrayOutputStream.write(i);
					i = is.read();
				}
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return byteArrayOutputStream.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
