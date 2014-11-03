package Operator;

import java.io.File;
import java.util.HashMap;

import Compress.CompressManager;
import Constant.FileConstant;
import FileOperation.FileOperation;

public class DuplicatedFileOperator {

	public static DuplicatedFileOperator mDuplicatedFileOperator;
	public HashMap<String, String[]> mFileMap;

	public DuplicatedFileOperator() {
		mFileMap = new HashMap<>();
		String[] fileNames = FileOperation.getAllFile(FileConstant.DIR_FILE);
		for (int i = 0; fileNames != null && i < fileNames.length; i++) {
			String[] content = FileOperation.readFile(
					FileConstant.DIR_FILE + File.separator + fileNames[i])
					.split(FileConstant.CONTENT_SEPARATOR);
			mFileMap.put(fileNames[i], content);
		}
	}

	public static void init() {
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
		String[] content = mFileMap.get(fileName);
		if (content == null || content.length == 0)
			return null;
		return CompressManager.getInstance().discompress(content);
	}

	/**
	 * insert a file in disk to dropbox storage
	 * 
	 * @param filePath
	 *            the full path of target file
	 * @return true is success
	 */
	public boolean insertFile(String filePath) {
		String[] tmp = filePath.split(File.separator);
		String fileName = tmp[tmp.length - 1];
		String[] content = CompressManager.getInstance().compress(
				FileOperation.readFile(filePath));
		if (content == null || content.length == 0)
			return false;
		String fileContent = "";
		for (int i = 0; i < content.length; i++) {
			if (i == 0)
				fileContent += content[i];
			else
				fileContent = fileContent + FileConstant.CONTENT_SEPARATOR
						+ content[i];
		}
		boolean result = FileOperation.createFile(FileConstant.DIR_FILE
				+ File.separator + fileName, fileContent);
		if (result) {
			mFileMap.put(fileName, content);
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
		if (mFileMap.remove(fileName) != null)
			return FileOperation.deleteFile(FileConstant.DIR_FILE
					+ File.separator + fileName);
		return false;
	}

	/**
	 * show all file in application
	 * 
	 * @return the array of files name; or null if it is empty
	 */
	public String[] getAllFile() {
		String[] files = new String[mFileMap.size()];
		return mFileMap.keySet().toArray(files);
	}

}
