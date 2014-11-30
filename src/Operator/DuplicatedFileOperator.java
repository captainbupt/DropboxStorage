package Operator;

import java.io.File;
import java.util.HashMap;

import Compress.CompressManager;
import Constant.FileConstant;
import FileOperation.FileOperation;

public class DuplicatedFileOperator {

	public static DuplicatedFileOperator mDuplicatedFileOperator;
	public HashMap<String, DuplicatedFile> mFileMap;

	public DuplicatedFileOperator() {
		mFileMap = new HashMap<>();
		String[] fileNames = FileOperation.getAllFile(FileConstant.DIR_FILE);
		for (int i = 0; fileNames != null && i < fileNames.length; i++) {
			DuplicatedFile duplicatedFile = new DuplicatedFile(fileNames[i],
					FileOperation.readFile(FileConstant.DIR_FILE
							+ File.separator + fileNames[i]));
			mFileMap.put(fileNames[i], duplicatedFile);
		}
	}

	public static void init() {
		File file = new File(FileConstant.DIR_FILE);
		if (!file.isDirectory())
			file.mkdirs();
		file = new File(FileConstant.DIR_BLOCK);
		if (!file.isDirectory())
			file.mkdirs();
		file = new File(FileConstant.DIR_FRAGMENT);
		if (!file.isDirectory())
			file.mkdirs();
		mDuplicatedFileOperator = new DuplicatedFileOperator();
		CompressManager.init();
	}

	public static DuplicatedFileOperator getInstance() {
		return mDuplicatedFileOperator;
	}

	/**
	 * show the content of compresses file
	 * 
	 * @param fileName
	 *            the file name
	 * @return the content of file; null if file is not found
	 */
	public String loadFile(String fileName) {
		String[] content = mFileMap.get(fileName).getContent();
		if (content == null || content.length == 0)
			return null;
		return CompressManager.getInstance().discompress(content);
	}

	/**
	 * insert a file in disk to dropbox storage
	 * 
	 * @param filePath
	 *            the full path of target file
	 * @param fileName
	 *            the file's name
	 * @return true is success
	 */
	public boolean insertFile(String filePath, String fileName) {
		String[] content = CompressManager.getInstance().compress(fileName,
				FileOperation.readFile(filePath));
		if (content == null || content.length == 0)
			return false;
		long time = System.currentTimeMillis();
		long size = new File(fileName).length();
		DuplicatedFile duplicatedFile = new DuplicatedFile(fileName, size,
				content, time);
		boolean result = FileOperation.createFile(FileConstant.DIR_FILE
				+ File.separator + fileName, duplicatedFile.getFileContent());
		if (result) {
			mFileMap.put(fileName, duplicatedFile);
			return true;
		}
		return false;
	}

	/**
	 * delete stored file in application
	 * 
	 * @param fileName
	 *            the file name
	 * @return true is success
	 */
	public boolean deleteFile(String fileName) {
		String[] content = mFileMap.get(fileName).getContent();
		if (content != null) {
			mFileMap.remove(fileName);
			CompressManager.getInstance().deleteChunk(fileName, content);
			return FileOperation.deleteFile(FileConstant.DIR_FILE
					+ File.separator + fileName);
		}
		return false;
	}

	/**
	 * show all file in application
	 * 
	 * @return the array of files name; or null if it is empty
	 */
	public DuplicatedFile[] getAllFile() {
		DuplicatedFile[] files = new DuplicatedFile[mFileMap.size()];
		return mFileMap.values().toArray(files);
	}

}
