package com.sys.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;



public class FileUtil {

    // 上传微博图片路径
    public final static String UPLOADBLOGDIR = "/upload/blog/pic";

    //群聊上传文件夹
    public final static String GROUP_FILE = "/upload/group";

    public final static String SCHOOL_SECURITY = "/upload/schoolSecurity/pic";
    //上传新闻图片
    public final static String UPLOADNEWSDIR = "/upload/news";

    // 上传微博图片路径
    public final static String UPLOADMATCHDIR = "/upload/microlesson/pic";

	public final static String SCHOOL_GOODS = "/upload/schoolGoods/pic";

	// 上传应用图片路径
	public final static String UPLOADAPPDIR = "/upload/desk/pic";
    /**
     * 随机数
     * @return
     */
    public static String randomPath() {
        String randomPath = System.currentTimeMillis() + StringUtil.getRandom(6);
        return randomPath;
    }


    public static File mkDir(String realPath) {
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    
    /**
     * 得到txt文件编码
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String getTextFileEncoding(File file) throws Exception{  
	    BufferedInputStream bin = new BufferedInputStream(  
	    new FileInputStream(file));  
	    int p = (bin.read() << 8) + bin.read();  
	    String code = null;  
	      
	    switch (p) {  
	        case 0xefbb:  
	            code = "UTF-8";  
	            break;  
	        case 0xfffe:  
	            code = "Unicode";  
	            break;  
	        case 0xfeff:  
	            code = "UTF-16BE";  
	            break;  
	        default:  
	            code = "GBK";  
	    } 
	    
	    bin.close();
	      
	    return code;  
	}

	/**
	 *  根据路径删除指定的目录或文件，无论存在与否
	 *@param sPath  要删除的目录或文件
	 *@return 删除成功返回 true，否则返回 false。
	 */
	public static boolean deleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) {  // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) {  // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else {  // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * @param   sPath    被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * @param   sPath 被删除目录的文件路径
	 * @return  目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		//如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		//如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		//删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			//删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) break;
			} //删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) break;
			}
		}
		if (!flag) return false;
		//删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
}
