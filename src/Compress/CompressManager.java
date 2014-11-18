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
	//private String iFile;
	//private String[] cFile;
	//private String outFile;
	private int blockCount = 0;
	private int fragmentCount = 0;

	public CompressManager() {
		// inFile=s;		
		String[] bFileNames = FileOperation.getAllFile(FileConstant.DIR_BLOCK);
		String[] fFileNames = FileOperation.getAllFile(FileConstant.DIR_FRAGMENT);
		mBlockMap = new HashMap<String, Block>();
		mBlockSeeker = new HashMap<Integer, Block>();
		mFragment = new HashMap<Integer, String>();
		for(int i=0;i<bFileNames.length;i++){
			if(blockCount<Integer.parseInt(bFileNames[i])){
				blockCount=Integer.parseInt(bFileNames[i]);
			}
			String[] eBlock=FileOperation.readFileByLines(FileConstant.DIR_BLOCK+File.separator+bFileNames[i]);
			Block rBlock=new Block(Integer.parseInt(bFileNames[i]),eBlock[0],eBlock[1]);
			if (eBlock.length>2){
				for(int j=2;j<eBlock.length;j++){
					rBlock.addParent(eBlock[j]);
				}
			}
			mBlockMap.put(Hash.MD5Cal(eBlock[0]), rBlock);
			mBlockSeeker.put(Integer.parseInt(bFileNames[i]), rBlock);
		}
		for(int m=0;m<fFileNames.length;m++){
			if(fragmentCount<Integer.parseInt(fFileNames[m])){
				fragmentCount=Integer.parseInt(fFileNames[m]);
			}
			String eFrag=FileOperation.readFile(FileConstant.DIR_FRAGMENT+File.separator+fFileNames[m]);
			mFragment.put(Integer.parseInt(fFileNames[m]), eFrag);
		}
		blockCount++;
		fragmentCount++;
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
			if (i + BLOCKSIZE-1 > (content.length() - 1)) {
				// something to be defined here
				content.getChars(i, content.length() - 1, buf, 0);
				String temp = String.valueOf(buf);
				System.out.println(temp);
				mFragment.put(fragmentCount, temp);
				comFile[i] = FLAG_FRAGMENT + fragmentCount;
				FileOperation.createFile(FileConstant.DIR_FRAGMENT+File.separator+fragmentCount, temp);
				//FileOperation.appendToFile(FileConstant.DIR_FRAGMENT+File.separator+fragmentCount, "\r\n"+fragmentCount);
				fragmentCount++;
			} else {
				content.getChars(i, i + BLOCKSIZE, buf, 0);
				String temp = String.valueOf(buf);
				System.out.println("here"+temp);
				String tempMd5 = Hash.MD5Cal(temp);
				Block block = new Block(blockCount, temp, fileName);
				mBlockMap.put(tempMd5, block);
				mBlockSeeker.put(blockCount, block);
				comFile[i] = FLAG_BLOCK + blockCount;
				FileOperation.createFile(FileConstant.DIR_BLOCK+File.separator+blockCount, temp);
				//FileOperation.appendToFile(FileConstant.DIR_BLOCK+File.separator+blockCount, "\r\n"+blockCount);
				FileOperation.appendToFile(FileConstant.DIR_BLOCK+File.separator+blockCount, "\r\n"+fileName);
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
			content.getChars(start + i, start + i + BLOCKSIZE , buf, 0);
			String temp = String.valueOf(buf);
			String tempMd5 = Hash.MD5Cal(temp);
			Block block  = mBlockMap.get(tempMd5);
			if (block!=null) {
				hasBlock = true;
				if (i != 0) {
					//content.getChars(start, start + i, buf1, 0);
					blocks[0] = content.substring(start, start + i);
				} else {
					blocks[0] = null;
				}
				blocks[1] = temp;
				break;
			}
		}
		// if no block detected, regard the first window as a new block
		if (hasBlock == false) {
			//content.getChars(start, start + BLOCKSIZE - 1, buf, 0);
			blocks[0] = content.substring(start, start+BLOCKSIZE);
			blocks[1] = null;
		}
		return blocks;
	}

	/**
	 * this is a detailed method to implement the compression
	 * 
	 * @param content
	 *            content of the file
	 * @return the compressed version of the file
	 */
	private String[] compFile(String fileName, String content) {
		char buf[] = new char[BLOCKSIZE];
		int fileLen = (int) Math.ceil(content.length() / BLOCKSIZE);
		ArrayList<String> comFile = new ArrayList<String>();
		String[] blocks = new String[2];
		int count = 0;
		int subcount = 0;
		// scan through the whole file character by character to detect blocks
		while (count < content.length()) {
			if (count + BLOCKSIZE - 1 > (content.length() - 1)) {
				content.getChars(count, content.length() - 1, buf, 0);
				String temp = String.valueOf(buf);
				mFragment.put(fragmentCount, temp);
				comFile.add( FLAG_FRAGMENT + fragmentCount);
				FileOperation.createFile(FileConstant.DIR_FRAGMENT+File.separator+fragmentCount, temp);
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
						comFile.add( FLAG_FRAGMENT + fragmentCount);
						FileOperation.createFile(FileConstant.DIR_FRAGMENT+File.separator+fragmentCount, blocks[0]);
						fragmentCount++;
						comFile.add( FLAG_BLOCK
								+ mBlockMap.get(Hash.MD5Cal(blocks[1])).getIndex());
						mBlockMap.get(Hash.MD5Cal(blocks[1])).addParent(fileName);
						FileOperation.appendToFile(FileConstant.DIR_BLOCK+File.separator+fragmentCount, "\r\n"+fileName);
						subcount += 2;
						count += blocks[0].length() + BLOCKSIZE;
					} else {
						comFile.add( FLAG_BLOCK
								+ mBlockMap.get(Hash.MD5Cal(blocks[1])).getIndex());
						FileOperation.appendToFile(FileConstant.DIR_BLOCK+File.separator+fragmentCount, "\r\n"+fileName);
						subcount += 1;
						count += BLOCKSIZE;
					}
				} else {
					String tempMd5 = Hash.MD5Cal(blocks[0]);
					Block block = new Block(blockCount, blocks[0], fileName);
					mBlockMap.put(tempMd5, block);
					mBlockSeeker.put(blockCount, block);
					FileOperation.createFile(FileConstant.DIR_BLOCK+File.separator+blockCount, blocks[0]+"\r\n"+fileName);
					//FileOperation.appendToFile(FileConstant.DIR_BLOCK+File.separator+blockCount, "\r\n"+blockCount);
					comFile.add( FLAG_BLOCK + blockCount);
					subcount += 1;
					blockCount++;
					count += BLOCKSIZE;
				}
			}
		}
		return comFile.toArray(new String[comFile.size()]);
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
	 *            the array of strings that stores the map and key of each
	 *            chunk of the file
	 * @return a data stream, supposed to be identical to the original input
	 */
	public String discompress(String[] fileContent) {
		String outFile = "";
		for (int i = 0; i < fileContent.length ; i++) {
			String[] theBlock = new String[2];
			theBlock[0] = fileContent[i].charAt(0)+"";
			theBlock[1] = fileContent[i].substring(1);
			int index = Integer.parseInt(theBlock[1]);
			if (theBlock[0].equals("f")) {
				outFile += mFragment.get(index);
			} else {
				outFile += mBlockSeeker.get(index).getContent();
			}
		}
//		for (int i = 0; i < fileContent.length - 1; i++) {
//			String[] theBlock = fileContent[i].split(",");
//			if (theBlock.length != 2
//					|| (theBlock[0] != "f" && theBlock[0] != "b")) {
//				System.out.println("error in the file");
//				return null;
//			} else {
//				int index = Integer.parseInt(theBlock[1]);
//				if (theBlock[0] == "f") {
//					outFile += mFragment.get(index);
//				} else {
//					outFile += mBlockSeeker.get(index);
//				}
//			}
//		}
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
