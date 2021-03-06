package FileOperation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class FileOperation {

	/**
	 * read file line by line.
	 * 
	 * @param fileName
	 *            the file name
	 * @param onLineReadListener
	 *            the listener when each line is read
	 */
	public static String[] readFileByLines(String fileName) {
		ArrayList<String> results = new ArrayList<>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				results.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return results.toArray(new String[results.size()]);
	}

	/**
	 * read whole file
	 * 
	 * @param fileName
	 *            the file name
	 * @return the whole content of file
	 */
	public static String readFile(String fileName) {
		InputStreamReader inputReader = null;
		BufferedReader bufferReader = null;
		OutputStream outputStream = null;
		StringBuffer strBuffer = null;
		try {
			InputStream inputStream = new FileInputStream(fileName);
			inputReader = new InputStreamReader(inputStream);
			bufferReader = new BufferedReader(inputReader);
			strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferReader.readLine()) != null) {
				strBuffer.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputReader != null) {
				try {
					inputReader.close();
				} catch (IOException e1) {
				}
			}
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e1) {
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e1) {
				}
			}
		}
		return strBuffer.toString();
	}

	/**
	 * put a string in the end of file. Without automatically '\n'
	 * 
	 * @param fileName
	 *            the file name
	 * @param content
	 *            the string append to file. Beginning with '\n' recommended
	 * @return true if success
	 */
	public static boolean appendToFile(String fileName, String content) {
		try {
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * create a file or replace existed file with content.
	 * 
	 * @param fileName
	 *            the file name
	 * @param content
	 *            the content to file.
	 * @return true if success
	 */
	public static boolean createFile(String fileName, String content) {
		try {
			FileWriter writer = new FileWriter(fileName, false);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * list all files and directories in the directory
	 * 
	 * @param dirName
	 *            the directory name
	 * @return files' name in the directory, contains only file name, no
	 *         directory name.
	 */
	public static String[] getAllFile(String dirName) {
		File file = new File(dirName);
		return file.list();
	}

	/**
	 * remove the target file
	 * 
	 * @param fileName
	 *            the target file name
	 * @return true if success
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	public static void overwriteFile(String fileName, String content, int offset) {
		RandomAccessFile ra = null;
		try {
			ra = new RandomAccessFile(fileName, "rw");
			ra.seek(offset);
			ra.writeBytes(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (ra != null)
				try {
					ra.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
