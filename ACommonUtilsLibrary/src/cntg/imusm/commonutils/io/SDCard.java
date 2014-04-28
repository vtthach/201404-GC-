package cntg.imusm.commonutils.io;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class SDCard {
	
    private static SDCard _obj          = new SDCard();
  
    private SDCard() 
    {
    }
	
    public boolean isSDCardExist()
    {
    	return android.os.Environment.getExternalStorageState().equals(
    			android.os.Environment.MEDIA_MOUNTED);
    }
    
//    public int checkSDCard()
//    {
//    	int flag = 0;
//    	String storageState=android.os.Environment.getExternalStorageState();
//    	if(storageState.equals(android.os.Environment.MEDIA_REMOVED))
//    	{
//    		flag=1;
//    	}
//    	else if(storageState.equals(android.os.Environment.MEDIA_BAD_REMOVAL) 
//    	     || storageState.equals(android.os.Environment.MEDIA_CHECKING)
//    		 || storageState.equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY)
//    		 || storageState.equals(android.os.Environment.MEDIA_NOFS)
//    		 || storageState.equals(android.os.Environment.MEDIA_UNMOUNTED)
//    		 || storageState.equals(android.os.Environment.MEDIA_SHARED)
//    		 || storageState.equals(android.os.Environment.MEDIA_UNMOUNTABLE))
//    	{
//    		flag=2;
//    	}
//    	else if(SDCard.getInstance().getFreeSizeSdCard() < DxLibConstants.CACHE_SIZE_WARNING)
//    	{
//    		flag=3;
//    	}
//    	return flag;
//    }
    
    public boolean isFileExist(String path)
    {
    	if (path == null) return false;
    	File file = new File(getAbsolutePath(path));
    	return file.exists();
    }
    
    public InputStream openFileForReading(String fullFileName) throws Exception
    {
		return new FileInputStream(getAbsolutePath(fullFileName));
    }

    public OutputStream createANewFileForWritting(String fullNameFile, long lastModifiedTime) throws Exception
    {
    	FileOutputStream result = null;
        File file = new File(getAbsolutePath(fullNameFile));
        if (!file.exists()) file.createNewFile();
        result = new FileOutputStream(file);
        file.setLastModified(lastModifiedTime);
        return result;
    }
    
    public long getFolderSize(String path) throws NullPointerException
    {
        long result = 0;
    	if (isFileExist(path) == false)
    		return result;
        File dir = new File(getAbsolutePath(path));
        Stack<File> dirlist = new Stack<File>();
        dirlist.clear();
        dirlist.push(dir);

        while(!dirlist.isEmpty())
        {
            File dirCurrent = dirlist.pop();

            File[] fileList = dirCurrent.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) dirlist.push(fileList[i]);
                else result += fileList[i].length();
            }
        }
        
        return result;
    }
    
    public boolean createFolder(String path) throws Exception
    {
		File folder = new File(getAbsolutePath(path));
		boolean success = false;
		if (folder.exists()) return false;
		else success = folder.mkdirs();
		return success;
    }

	public String getAbsolutePath(String path) {
		return Environment.getExternalStorageDirectory() + "/" + path;
	}
    
    public boolean deleteFile(String fileName)
    {
    	File file = new File(getAbsolutePath(fileName));
    	if (file.exists()) { 
    		file.delete();
    		return true;
    	}
    	return false;
    }
    
    public void  deleteFolder(String path)
    {
    	if (isFileExist(path) == false) return;
    	File dir = new File(getAbsolutePath(path));
    	ArrayList<File> queue = new ArrayList<File>();
    	queue.add(0, dir);
		while (queue.isEmpty() == false)
		{
			File currentFile = queue.remove(0);
			if (currentFile.isDirectory())
			{
				File [] children = currentFile.listFiles();
				if (children.length == 0)
				{
					currentFile.delete();
				}
				else
				{
					for (File child: children)
					{
						queue.add(0, child);
					}
					queue.add(queue.size(), currentFile);
				}
			}
			else
			{
				currentFile.delete();
			}
		}
    }
       
    public void  deleteFolder(String path, AtomicBoolean cancel)
    {
    	if (isFileExist(path) == false) return;
    	File dir = new File(getAbsolutePath(path));
    	ArrayList<File> queue = new ArrayList<File>();
    	queue.add(0, dir);
		while (queue.isEmpty() == false && cancel.get() == false)
		{
			File currentFile = queue.remove(0);
			if (currentFile.isDirectory()&& cancel.get() == false)
			{
				File [] children = currentFile.listFiles();
				if (children.length == 0 )
				{
					currentFile.delete();
				}
				else if(cancel.get() == false)
				{
					for (File child: children)
					{
						queue.add(0, child);
					}
					queue.add(queue.size(), currentFile);
				}
			}
			else if(cancel.get() == false)
			{
				currentFile.delete();
			}
		}
    }
    public long getTimeLastModified(String path)
    {   
    	File file = new File(getAbsolutePath(path));
    	return file.lastModified();
    }
    
    public void setLastModified(String path, long time)
    {
    	File file = new File(getAbsolutePath(path));
    	file.setLastModified(time);
    }
    
    public int numberOfSubDirectories(String parentDirectory)
    {
    	int count = 0;
    	File dir = new File(getAbsolutePath(parentDirectory));
    	for (File file: dir.listFiles())
    	{
    		if(file.isDirectory())
    		{
    			count++;
    		}
    	}
     	return count;
    }
    
    public static SDCard getInstance()
    {
    	return _obj;
    }

    public long getFileSize(String fileName)
    {
    	File file = new File(getAbsolutePath(fileName));
    	return file.length();
    }
    
    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static long getSize(String filePath) {
		File f = new File(filePath);
		if(f.exists()){
			if(f.isDirectory()){
				StatFs statFs = new StatFs(f.getAbsolutePath());
			    long blockSize;
			    if (Build.VERSION.SDK_INT >= 18) {
			        blockSize = statFs.getBlockSizeLong();
			    } else {
			        blockSize = statFs.getBlockSize();
			    }
			    return getDirectorySize(f, blockSize);
			} else {
				return f.length();
			}
		} else {
			return 0;
		}
	}
    
    public static long getDirectorySize(File directory, long blockSize) {
	    File[] files = directory.listFiles();
	    if (files != null) {

	        // space used by directory itself 
	        long size = directory.length();

	        for (File file : files) {
	            if (file.isDirectory()) {
	                // space used by subdirectory
	                size += getDirectorySize(file, blockSize);
	            } else {
	                // file size need to rounded up to full block sizes
	                // (not a perfect function, it adds additional block to 0 sized files
	                // and file who perfectly fill their blocks) 
	                size += (file.length() / (blockSize + 1)) * blockSize;
	            }
	        }
	        return size;
	    } else {
	        return 0;
	    }
	}
    
    public List<String> getSubDirectories(String parentDirectory)
    {
    	List<String> result = new ArrayList<String>();
    	if (isFileExist(parentDirectory) == false)
    		return result;
    	File dir = new File(getAbsolutePath(parentDirectory));
    	for (File file: dir.listFiles())
    	{
    		if(file.isDirectory())
    		{
    			result.add(file.getName());
    		}
    	}
     	return result;
    }
    
    public List<String> listFiles(String parentDirectory)
    {
    	List<String> result = new ArrayList<String>();
    	if (isFileExist(parentDirectory) == false)
    		return result;
    	File dir = new File(getAbsolutePath(parentDirectory));
    	for (File file: dir.listFiles())
    	{
    		if(file.isDirectory() == false)
    		{
    			result.add(file.getName());
    		}
    	}
     	return result;
    }
}
