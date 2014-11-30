package Compress;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

import Constant.FileConstant;
import FileOperation.FileOperation;

public class CompressManager {
	private static CompressManager mCompressManager;
	private static final String FLAG_FRAGMENT = "f";
	private static final String FLAG_BLOCK = "b";
	private HashMap<String, Block> mBlockMap;
	private HashMap<Integer, Block> mBlockSeeker;
	private HashMap<Integer, String> mFragment;
	private int BLOCKSIZE = 3;
	private int blockCount = 0;
	private int fragmentCount = 0;

	public CompressManager() {
		// inFile=s;

		mBlockMap = new HashMap<String, Block>();
		mBlockSeeker = new HashMap<Integer, Block>();
		mFragment = new HashMap<Integer, String>();
		String[] bFileNames = FileOperation.getAllFile(FileConstant.DIR_BLOCK);
		String[] fFileNames = FileOperation
				.getAllFile(FileConstant.DIR_FRAGMENT);
		for (int i = 0; i < bFileNames.length; i++) {
			if (blockCount < Integer.parseInt(bFileNames[i])) {
				blockCount = Integer.parseInt(bFileNames[i]);
			}
			String[] eBlock = FileOperation
					.readFileByLines(FileConstant.DIR_BLOCK + File.separator
							+ bFileNames[i]);
			Block rBlock = new Block(Integer.parseInt(bFileNames[i]),
					eBlock[0], Integer.parseInt(eBlock[1]));

			mBlockMap.put(Hash.MD5Cal(eBlock[0]), rBlock);
			mBlockSeeker.put(Integer.parseInt(bFileNames[i]), rBlock);
		}
		for (int m = 0; m < fFileNames.length; m++) {
			if (fragmentCount < Integer.parseInt(fFileNames[m])) {
				fragmentCount = Integer.parseInt(fFileNames[m]);
			}
			String eFrag = FileOperation.readFile(FileConstant.DIR_FRAGMENT
					+ File.separator + fFileNames[m]);
			mFragment.put(Integer.parseInt(fFileNames[m]), eFrag);
		}
		blockCount++;
		fragmentCount++;
	}

	/**
	 * this is a detailed method to implement the compression
	 * 
	 * @param content
	 *            content of the file
	 * @return the compressed version of the file
	 */
	private String[] compFile(String fileName, String content) {
		ArrayList<String> comFile = new ArrayList<String>();
		int slideStart = 0;
		int lastEnd = 0;
		// scan through the whole file character by character to detect blocks
		while (slideStart < content.length()) {
			// if left characters are less than a block, then put it to fragment

			if (slideStart + BLOCKSIZE > content.length()) { //
				slideStart = content.length();
			} else { // determine the window of block scanning
				String temp = content.substring(slideStart, slideStart
						+ BLOCKSIZE);
				String tempMD5 = Hash.MD5Cal(temp);
				Block tempBlock = mBlockMap.get(tempMD5);
				if (tempBlock != null) {
					if (lastEnd < slideStart) {
						if (slideStart - lastEnd == BLOCKSIZE) {
							comFile.add(FLAG_BLOCK + blockCount);
							addBlock(content.substring(lastEnd, slideStart));
						} else if (lastEnd < slideStart) {
							comFile.add(FLAG_FRAGMENT + fragmentCount);
							addFragment(content.substring(lastEnd, slideStart));
						}
					}
					comFile.add(FLAG_BLOCK + tempBlock.getIndex());
					tempBlock.addParentCount();
					FileOperation.overwriteFile(FileConstant.DIR_BLOCK
							+ File.separator + tempBlock.getIndex(),
							tempBlock.getParentCount() + "", BLOCKSIZE + 2);
					slideStart += BLOCKSIZE;
					lastEnd = slideStart;
				} else if (slideStart - lastEnd == BLOCKSIZE) {
					comFile.add(FLAG_BLOCK + blockCount);
					addBlock(content.substring(lastEnd, slideStart));
					lastEnd = slideStart;
				} else {
					slideStart++;
				}
			}

		}
		if (lastEnd != slideStart) {
			// String temp = String.valueOf(buf);
			if (lastEnd <= content.length() - BLOCKSIZE) {
				comFile.add(FLAG_BLOCK + blockCount);
				addBlock(content.substring(lastEnd));
				lastEnd += BLOCKSIZE;
			}
			if (lastEnd < content.length()) {
				comFile.add(FLAG_FRAGMENT + fragmentCount);
				addFragment(content.substring(lastEnd));
			}
		}
		return comFile.toArray(new String[comFile.size()]);
	}

	private void addBlock(String content) {
		Block block = new Block(blockCount, content, 1);
		mBlockMap.put(Hash.MD5Cal(content), block);
		mBlockSeeker.put(blockCount, block);
		FileOperation.createFile(FileConstant.DIR_BLOCK + File.separator
				+ blockCount, content + "\r\n" + 1);
		blockCount++;
	}

	public void addFragment(String content) {
		mFragment.put(fragmentCount, content);
		FileOperation.createFile(FileConstant.DIR_FRAGMENT + File.separator
				+ fragmentCount, content);
		fragmentCount++;
	}

	/**
	 * 
	 */
	public static void init() {
		mCompressManager = new CompressManager();
	}

	public static CompressManager getInstance() {
		return mCompressManager;
	}

	/**
	 * This method is to compress the input data stream
	 * 
	 * @param content
	 * @return an array of string regarding the map and key of each segment
	 */
	public String[] compress(String fileName, String content) {
		String iFile = content;
		String[] cFile;
		cFile = compFile(fileName, iFile);
		return cFile;
	}

	/**
	 * this method is to uncompress the input to a data stream
	 * 
	 * @param fileContent
	 *            the array of strings that stores the map and key of each chunk
	 *            of the file
	 * @return a data stream, supposed to be identical to the original input
	 */
	public String discompress(String[] fileContent) {
		String outFile = "";
		for (int i = 0; i < fileContent.length; i++) {
			String[] theBlock = new String[2];
			theBlock[0] = fileContent[i].charAt(0) + "";
			theBlock[1] = fileContent[i].substring(1);
			int index = Integer.parseInt(theBlock[1]);
			if (theBlock[0].equals("f")) {
				outFile += mFragment.get(index);
			} else {
				outFile += mBlockSeeker.get(index).getContent();
			}
		}
		return outFile;
	}

	/***
	 * Delete operations. When delete file, first delete blocks parent, if block
	 * has no more parent, delete block. Then delete fragments
	 * 
	 * @param fileName
	 *            target file name
	 * @param fileContent
	 *            target file content
	 * @return true if success.
	 */
	public boolean deleteChunk(String fileName, String[] fileContent) {
		for (int i = 0; i < fileContent.length; i++) {
			String[] theBlock = new String[2];
			theBlock[0] = fileContent[i].charAt(0) + "";
			theBlock[1] = fileContent[i].substring(1);
			int index = Integer.parseInt(theBlock[1]);
			if (theBlock[0].equals(FLAG_BLOCK)) {
				Block temp = mBlockSeeker.get(index);
				temp.subParentCount();
				if (temp.getParentCount() == 0) {
					FileOperation.deleteFile(FileConstant.DIR_BLOCK
							+ File.separator + index);
					mBlockMap.remove(Hash.MD5Cal(temp.getContent()));
					mBlockSeeker.remove(temp.getIndex());
				} else {
					FileOperation.overwriteFile(FileConstant.DIR_BLOCK
							+ File.separator + index, temp.getParentCount()
							+ "", BLOCKSIZE + 2);
				}
			} else {
				mFragment.remove(index);
				FileOperation.deleteFile(FileConstant.DIR_FRAGMENT
						+ File.separator + index);
			}
		}
		return true;
	}

}
