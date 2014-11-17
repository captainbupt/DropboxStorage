package Compress;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import Constant.FileConstant;
import FileOperation.FileOperation;

public class CompressManager {
	private static CompressManager mCompressManager;
	private static final String FLAG_FRAGMENT = "f";
	private static final String FLAG_BLOCK = "b";
	private static final String FILE_FRAGMENT = FileConstant.DIR_FRAGMENT
			+ File.separator + FLAG_FRAGMENT;
	private static final String FILE_BLOCK = FileConstant.DIR_BLOCK
			+ File.separator + FLAG_BLOCK;
	private HashMap<String, Block> mBlockMap;// <hash, value>
	private HashMap<Integer, Block> mBlockSeeker;// <index, >
	private HashMap<Integer, String> mFragment;
	private int BLOCKSIZE = 2;
	// private String[] inFiles;
	private String iFile;
	private String[] cFile;
	private String outFile;
	private int blockCount = 0;
	private int fragmentCount = 0;

	public CompressManager() {
		// inFile=s;
		FileOperation.readFile("");

	}


	/**
	 * initiate the blockmap when reading the first file
	 * 
	 * @param content
	 *            : the input data stream, content of the original file return
	 *            the compressed version of the very first file
	 */
	private String[] initBlocks(String fileName, String content) {
		char buf[] = new char[BLOCKSIZE];
		int fileLen = (int) Math.ceil(content.length() / BLOCKSIZE);
		String[] comFile = new String[fileLen];
		for (int i = 0; i < content.length(); i += BLOCKSIZE) {
			if (i + BLOCKSIZE > (content.length() - 1)) {
				// something to be defined here
				content.getChars(i, content.length() - 1, buf, 0);
				String temp = String.valueOf(buf);
				mFragment.put(fragmentCount, temp);
				comFile[i] = FLAG_FRAGMENT + fragmentCount;
				fragmentCount++;
				// break;
			} else {
				content.getChars(i, i + BLOCKSIZE - 1, buf, 0);
				String temp = String.valueOf(buf);
				String tempMd5 = Hash.MD5Cal(temp);
				Block block = new Block(blockCount, temp, fileName);
				mBlockMap.put(tempMd5, block);
				mBlockSeeker.put(blockCount, block);
				comFile[i] = FLAG_BLOCK + blockCount;
				blockCount++;
			}
		}
		return comFile;
	}

	/**
	 * 
	 * @param content
	 *            : input data stream, content of the file to be compressed
	 * @param start
	 *            : start point of the block scanning
	 * @param end
	 *            : end point of the block scanning
	 * @return the either detected or new block
	 */
	private String[] slideBlock(String content, int start, int end) {
		char buf1[] = new char[2 * BLOCKSIZE];
		char buf[] = new char[BLOCKSIZE];
		String blocks[] = new String[2];
		boolean hasBlock = false;
		// scan the input from the first character,stop when a block is detected
		// return the sub string before the block and the block
		for (int i = 0; i + start < end - BLOCKSIZE + 1; i++) {
			content.getChars(start + i, start + i + BLOCKSIZE - 1, buf, 0);
			String temp = String.valueOf(buf);
			String tempMd5 = Hash.MD5Cal(temp);
			if (mBlockMap.containsKey(tempMd5)) {
				hasBlock = true;
				if (i != 0) {
					content.getChars(start, start + i - 1, buf1, 0);
					blocks[0] = String.valueOf(buf1);
				} else {
					blocks[0] = null;
				}
				blocks[1] = temp;
				break;
			}
		}
		// if no block detected, regard the first window as a new block
		if (hasBlock == false) {
			content.getChars(start, start + BLOCKSIZE - 1, buf, 0);
			blocks[0] = String.valueOf(buf);
			blocks[1] = null;
		}
		return blocks;
	}

	/**
	 * this is a detailed method to implement the compression
	 * 
	 * @param content
	 *            : content of the file
	 * @return the compressed version of the file
	 */
	private String[] compFile(String fileName, String content) {
		char buf[] = new char[BLOCKSIZE];
		int fileLen = (int) Math.ceil(content.length() / BLOCKSIZE);
		String[] comFile = new String[2 * fileLen];
		String[] blocks = new String[2];
		int count = 0;
		int subcount = 0;
		// scan through the whole file character by character to detect blocks
		while (count < content.length()) {
			if (count + BLOCKSIZE - 1 > (content.length() - 1)) {
				content.getChars(count, content.length() - 1, buf, 0);
				String temp = String.valueOf(buf);
				mFragment.put(fragmentCount, temp);
				comFile[subcount] = FLAG_FRAGMENT + fragmentCount;
				subcount++;
				fragmentCount++;
				count += temp.length();
				// break;
			} else {
				// determine the window of block scanning
				if (count + 2 * BLOCKSIZE - 1 < content.length()) {
					blocks = slideBlock(content, count, count + 2 * BLOCKSIZE
							- 1);
				} else {
					blocks = slideBlock(content, count, content.length() - 1);
				}
				// save the substring as a fragment and add the block address to
				// the compressed file
				if (blocks[1] != null) {
					if (blocks[0] != null) {
						mFragment.put(fragmentCount, blocks[0]);
						comFile[subcount] = FLAG_FRAGMENT + fragmentCount;
						fragmentCount++;
						comFile[subcount + 1] = FLAG_BLOCK
								+ mBlockMap.get(blocks[1]).getIndex();
						subcount += 2;
						count += blocks[0].length() + BLOCKSIZE;
					} else {
						comFile[subcount] = FLAG_BLOCK
								+ mBlockMap.get(blocks[1]).getIndex();
						subcount += 1;
						count += BLOCKSIZE;
					}
				} else {
					String tempMd5 = Hash.MD5Cal(blocks[0]);
					Block block = new Block(blockCount, blocks[0], fileName);
					mBlockMap.put(tempMd5, block);
					mBlockSeeker.put(blockCount, block);
					comFile[subcount] = FLAG_BLOCK + blockCount;
					subcount += 1;
					blockCount++;
					count += BLOCKSIZE;
				}
			}
		}
		return comFile;
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
		iFile = content;
		if (mBlockMap == null) {
			cFile = initBlocks(fileName, iFile);
		} else {
			cFile = compFile(fileName, iFile);
		}
		return cFile;
	}

	/**
	 * this method is to uncompress the input to a data stream
	 * 
	 * @param fileContent
	 *            : the array of strings that stores the map and key of each
	 *            chunk of the file
	 * @return a data stream, supposed to be identical to the original input
	 */
	public String discompress(String[] fileContent) {
		for (int i = 0; i < fileContent.length - 1; i++) {
			String[] theBlock = fileContent[i].split(",");
			if (theBlock.length != 2
					|| (theBlock[0] != "f" && theBlock[0] != "b")) {
				System.out.println("error in the file");
				return null;
			} else {
				int index = Integer.parseInt(theBlock[1]);
				if (theBlock[0] == "f") {
					outFile += mFragment.get(index);
				} else {
					outFile += mBlockSeeker.get(index);
				}
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
		for (int i = 0; i < fileContent.length - 1; i++) {
			String[] theBlock = new String[2];
			theBlock[0] = fileContent[i].charAt(0)+"";
			theBlock[1] = fileContent[i].substring(1);
			int index = Integer.parseInt(theBlock[1]);
			if (theBlock[0] == "f") {
				Block temp = mBlockSeeker.get(index);
				temp.delParent(fileName);
				if (temp.getParentSize() == 0) {
					FileOperation.deleteFile(FILE_BLOCK + index);
				}
			} else {
				mFragment.remove(index);
				FileOperation.deleteFile(FILE_FRAGMENT + index);
			}
		}
		return true;
	}
}
