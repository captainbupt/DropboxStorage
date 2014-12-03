package Compress;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

import Constant.FileConstant;
import FileOperation.FileOperation;

public class CompressManager {
	private static CompressManager mCompressManager;
	private static final char FLAG_FRAGMENT = 'f';
	private static final char FLAG_BLOCK = 'b';
	private HashMap<String, Integer> mBlockMap;
	private HashMap<Integer, Block> mBlockSeeker;
	private HashMap<Integer, String> mFragment;
	private int BLOCKSIZE = 7000;
	private int blockCount = 0;
	private int fragmentCount = 0;

	public CompressManager() {
		mBlockMap = new HashMap<String, Integer>();
		mBlockSeeker = new HashMap<Integer, Block>();
		mFragment = new HashMap<Integer, String>();
		String[] bFileNames = FileOperation.getAllFile(FileConstant.DIR_BLOCK);
		String[] fFileNames = FileOperation
				.getAllFile(FileConstant.DIR_FRAGMENT);
		for (int i = 0; i < bFileNames.length; i++) {
			int count = Integer.parseInt(bFileNames[i]);
			if (blockCount < count) {
				blockCount = count;
			}
			String[] eBlock = FileOperation
					.readFileByLines(FileConstant.DIR_BLOCK + File.separator
							+ count);
			Block rBlock = new Block(count, eBlock[0],
					Integer.parseInt(eBlock[1]));

			mBlockMap.put(eBlock[0], count);
			mBlockSeeker.put(count, rBlock);
		}
		for (int j = 0; j < fFileNames.length; j++) {
			int count = Integer.parseInt(fFileNames[j]);
			if (fragmentCount < count) {
				fragmentCount = count;
			}
			String eFrag = FileOperation.readFile(FileConstant.DIR_FRAGMENT
					+ File.separator + count);
			mFragment.put(count, eFrag);
		}
		blockCount++;
		fragmentCount++;
	}

	/**
	 * This method is to called when first compress. This is could be fast since no compare is needed
	 * 
	 * @param content
	 * @return an array of string regarding the map and key of each segment
	 */
	private ArrayList<String> firstConpress(String fileName, String content){
		ArrayList<String> comFile = new ArrayList<String>();
		int i;
		for(i=0;i<content.length()-BLOCKSIZE;i+=BLOCKSIZE){
			addBlock(content.substring(i, i+BLOCKSIZE), comFile);
		}
		addFragment(content.substring(i), comFile);
		return comFile;
	}
	
	
	/**
	 * This method is to compress the input data stream
	 * 
	 * @param content
	 * @return an array of string regarding the map and key of each segment
	 */
	public ArrayList<String> compress(String fileName, String content) {
		if(mBlockMap.size()==0)
			return firstConpress(fileName, content);
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
				Integer tempIndex = mBlockMap.get(temp);
				if (tempIndex != null) {
					if (lastEnd < slideStart) {
						if (slideStart - lastEnd == BLOCKSIZE) {
							addBlock(content.substring(lastEnd, slideStart),
									comFile);
						} else if (lastEnd < slideStart) {
							addFragment(content.substring(lastEnd, slideStart),
									comFile);
						}
					}
					comFile.add("" + FLAG_BLOCK + tempIndex);
					Block tempBlock = mBlockSeeker.get(tempIndex);
					tempBlock.addParentCount();
					FileOperation.overwriteFile(FileConstant.DIR_BLOCK
							+ File.separator + tempBlock.getIndex(),
							tempBlock.getParentCount() + "", BLOCKSIZE + 2);
					slideStart += BLOCKSIZE;
					lastEnd = slideStart;
				} else if (slideStart - lastEnd == BLOCKSIZE) {
					addBlock(content.substring(lastEnd, slideStart), comFile);
					lastEnd = slideStart;
				} else {
					slideStart++;
				}
			}

		}
		if (lastEnd != slideStart) {
			if (lastEnd <= content.length() - BLOCKSIZE) {
				addBlock(content.substring(lastEnd), comFile);
				lastEnd += BLOCKSIZE;
			}
			if (lastEnd < content.length()) {
				addFragment(content.substring(lastEnd), comFile);
			}
		}
		return comFile;
	}

	private void addBlock(String content, ArrayList<String> comFile) {
		comFile.add("" + FLAG_BLOCK + blockCount);
		Block block = new Block(blockCount, content, 1);
		mBlockMap.put(content, blockCount);
		mBlockSeeker.put(blockCount, block);
		FileOperation.createFile(FileConstant.DIR_BLOCK + File.separator
				+ blockCount, content + "\r\n" + 1);
		blockCount++;
	}

	public void addFragment(String content, ArrayList<String> comFile) {
		comFile.add("" + FLAG_FRAGMENT + fragmentCount);
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
	 * this method is to uncompress the input to a data stream
	 * 
	 * @param fileContent
	 *            the array of strings that stores the map and key of each chunk
	 *            of the file
	 * @return a data stream, supposed to be identical to the original input
	 */
	public String discompress(ArrayList<String> fileContent) {
		String outFile = "";
		for (String tmp : fileContent) {
			int index = Integer.parseInt(tmp.substring(1));
			if (tmp.charAt(0) == FLAG_FRAGMENT) {
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
	public boolean deleteChunk(String fileName, ArrayList<String> fileContent) {
		for (String tmp : fileContent) {
			int index = Integer.parseInt(tmp.substring(1));
			if (tmp.charAt(0) == FLAG_BLOCK) {
				Block temp = mBlockSeeker.get(index);
				temp.subParentCount();
				if (temp.getParentCount() == 0) {
					FileOperation.deleteFile(FileConstant.DIR_BLOCK
							+ File.separator + index);
					mBlockMap.remove(temp.getContent());
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
