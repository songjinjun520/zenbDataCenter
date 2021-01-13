package com.zrt.zenb.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtils {
	
	// 删除某个目录及目录下的所有子目录和文件
	public static boolean deleteDir(File dir) {
		if(null == dir) {
			return false;
		}
		// 如果是文件夹
		if (dir.isDirectory()) {
			// 则读出该文件夹下的的所有文件
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				// File f=new File（String parent ，String child）
				// parent抽象路径名用于表示目录，child 路径名字符串用于表示目录或文件。
				// 连起来刚好是文件路径
				boolean isDelete = deleteDir(new File(dir, children[i]));
				// 如果删完了，没东西删，isDelete==false的时候，则跳出此时递归
				if (!isDelete) {
					return false;
				}
			}
		}
		// 读到的是一个文件或者是一个空目录，则可以直接删除
		return dir.delete();
	}

	// 删除某个目录下的所有文件
	public static void deleteOnlyFile(File dir) {
		// 如果是文件夹
		if (dir.isDirectory()) {
			// 则读出该文件夹下的的所有文件
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				// File f=new File（String parent ，String child）
				// parent抽象路径名用于表示目录，child 路径名字符串用于表示目录或文件。
				// 连起来刚好是文件路径
				deleteOnlyFile(new File(dir, children[i]));
			}
		}
		else {
			// 读到的是一个文件，则可以直接删除
			dir.delete();
		}
	}
	
	// 复制某个目录及目录下的所有子目录和文件到新文件夹
	public static void copyFolder(String oldPath, String newPath) {
		try {
			// 如果文件夹不存在，则建立新文件夹
			(new File(newPath)).mkdirs();
			// 读取整个文件夹的内容到file字符串数组，下面设置一个游标i，不停地向下移开始读这个数组
			File filelist = new File(oldPath);
			String[] file = filelist.list();
			// 要注意，这个temp仅仅是一个临时文件指针
			// 整个程序并没有创建临时文件
			File temp = null;
			if(null == file) {
//				System.err.println(oldPath + " has no files for copy.");
				return ;
			}
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				}
				else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				// 如果游标遇到文件
				if (temp.isFile()) {
					File dstFile = new File(newPath + "/" + temp.getName());
					if(! dstFile.exists()) {
						FileInputStream input = new FileInputStream(temp);
						// 复制并且改名
						FileOutputStream output = new FileOutputStream(dstFile);
						byte[] bufferarray = new byte[1024 * 64];
						int prereadlength;
						while ((prereadlength = input.read(bufferarray)) != -1) {
							output.write(bufferarray, 0, prereadlength);
						}
						output.flush();
						output.close();
						input.close();
						
						System.err.println(file[i] + " copy to " + dstFile.getPath() + " success.");
					}
					else {
						System.err.println(dstFile.getPath() + " exists, skip file copy.");
					}
				}
				else {
					System.err.println(file[i] + " is not a file. skip file copy.");
				}
				// 如果游标遇到文件夹
				if (temp.isDirectory()) {
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("复制整个文件夹内容操作出错");
		}

	}
	
	// 剪切文件
	// 复制某个文件到新文件夹
	// 从data目录剪切到usr目录
	public static void moveFile(File oldFile) {
		try {
			String oldFileFullName = oldFile.getPath();
			String oldFileName = oldFile.getName();
			int lastFileSepIndex = oldFileFullName.lastIndexOf(File.separator);
			String oldFilePath = oldFileFullName.substring(0, lastFileSepIndex);
			String newFilePath = oldFilePath.replace(File.separator + "data" + File.separator, File.separator + "usr" + File.separator);
//			System.out.println(oldFilePath);
//			System.out.println(newFilePath);
			// 如果文件夹不存在，则建立新文件夹
			(new File(newFilePath)).mkdirs();
			File dstFile = new File(newFilePath + File.separator + oldFileName);
			if(! dstFile.exists()) {
				FileInputStream input = new FileInputStream(oldFile);
				// 复制并且改名
				FileOutputStream output = new FileOutputStream(dstFile);
				byte[] bufferarray = new byte[1024 * 64];
				int prereadlength;
				while ((prereadlength = input.read(bufferarray)) != -1) {
					output.write(bufferarray, 0, prereadlength);
				}
				output.flush();
				output.close();
				input.close();
				
				System.err.println(oldFile + " copy to " + dstFile + " success.");
				
				oldFile.delete();
			}
			else {
				System.err.println(dstFile + " exists, skip file copy.");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("copy file " + oldFile + " exception. see detail.");
		}

	}
    
//	public static void moveFolder(String oldPath, String newPath) {
//		// 先复制文件
//		copyFolder(oldPath, newPath);
//		// 则删除源文件，以免复制的时候错乱
//		deleteDir(new File(oldPath));
//	}
//
//	public static void main(String[] args) {
//		moveFolder("c:/A", "f:/B");
//	}

}
