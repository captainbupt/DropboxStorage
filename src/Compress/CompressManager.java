package Compress;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import FileOperation.FileOperation;

public class CompressManager {
	private static CompressManager mCompressManager;
	// checkSum, <hash, fileName>
	//private HashMap<Integer, HashMap<String, String>> mBlockMap;
	private HashMap<String,String> mBlockMap;
	private HashMap<Integer,String> mBlockSeeker;
	private HashMap<String,Integer> mCountSeeker;
	private HashMap<Integer,String> mFragment;
	private int BLOCKSIZE=2;
	//private String[] inFiles;
	private String iFile;
	private String[] cFile;
	private String outFile;
	private int blockCount=0;
	private int fragmentCount=0;
	
	
	
	
	 
	
	
	
	public CompressManager() {
		//inFile=s;
		FileOperation.readFile("");

	}
	/**
	 * Converts an array of bytes into a readable set of characters in the range ! through ~
	 * @param bytes The array of bytes
	 * @return A string with characters in the range ! through ~
	 */
	private static String mMakeReadable(byte[] bytes) {
		for (int ii=0; ii<bytes.length; ii++) {
			bytes[ii]=(byte) ((bytes[ii] & 0x5E)+32); // Convert to character ! through ~
		}
		return new String(bytes);
	}

	/**
	 * produce a hash of a given string
	 * @param str The string to hash
	 * @return Returns a collection of sixteen "readable" characters (! through ~) corresponding to this string.
	 */
	private static String mMd5Cal(String str) {
		// setup the digest
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Hash digest format not known!");
			System.exit(-1);
		}
		return mMakeReadable(md.digest());
	}
	/**
	 * initiate the blockmap when reading the first file
	 * @param content: the input data stream, content of the original file
	 * return the compressed version of the very first file
	 */
	private String[] mInitBlocks(String content){
		char buf[]=new char[BLOCKSIZE];
		int fileLen=(int) Math.ceil(content.length()/BLOCKSIZE);
		String[] comFile = new String[fileLen];
		for(int i=0;i<content.length();i+=BLOCKSIZE){
			if(i+BLOCKSIZE>(content.length()-1)){
				//something to be defined here
				content.getChars(i, content.length()-1, buf, 0);
				String temp=String.valueOf(buf);
				mFragment.put(fragmentCount, temp);
				comFile[i]="f,"+""+fragmentCount;
				fragmentCount++;
				//break;
			}else{
				content.getChars(i, i+BLOCKSIZE-1, buf, 0);
				String temp=String.valueOf(buf);
				String tempMd5=mMd5Cal(temp);
				mBlockMap.put(tempMd5,temp);
				mBlockSeeker.put(blockCount,tempMd5);
				mCountSeeker.put(tempMd5, blockCount);
				comFile[i]="b,"+""+blockCount;
				blockCount++;				
			}
		}
		return comFile;
	}
	/**
	 * 
	 * @param content: input data stream, content of the file to be compressed
	 * @param start: start point of the block scanning
	 * @param end: end point of the block scanning
	 * @return the either detected or new block
	 */
	private String[] mSlideBlock(String content,int start,int end){
		char buf1[]=new char[2*BLOCKSIZE];
		char buf[]=new char[BLOCKSIZE];
		String blocks[]=new String[2];
		boolean hasBlock=false;
		//scan the input from the first character,stop when a block is detected
		//return the sub string before the block and the block 
		for(int i=0;i+start<end-BLOCKSIZE+1;i++){
			content.getChars(start+i, start+i+BLOCKSIZE-1, buf, 0);
			String temp=String.valueOf(buf);
			String tempMd5=mMd5Cal(temp);
			if(mBlockMap.containsKey(tempMd5)){
				hasBlock=true;
				if(i!=0){
					content.getChars(start, start+i-1, buf1, 0);
					blocks[0]=String.valueOf(buf1);
				}else{
					blocks[0]=null;
				}
				blocks[1]=temp;
				break;
			}
		}
		//if no block detected, regard the first window as a new block
		if(hasBlock==false){
			content.getChars(start, start+BLOCKSIZE-1, buf, 0);
			blocks[0]=String.valueOf(buf);
			blocks[1]=null;
		}
		return blocks;
	}

	
	/**
	 * this is a detailed method to implement the compression
	 * @param content: content of the file
	 * @return the compressed version of the file
	 */
	private String[] mCompFile(String content){
		char buf[]=new char[BLOCKSIZE];
		int fileLen=(int) Math.ceil(content.length()/BLOCKSIZE);
		String[] comFile=new String[2*fileLen];
		String[] blocks=new String[2];
		int count=0;
		int subcount=0;
		//scan through the whole file character by character to detect blocks
		while(count<content.length()){
			if(count+BLOCKSIZE-1>(content.length()-1)){
				content.getChars(count, content.length()-1, buf, 0);
				String temp=String.valueOf(buf);
				mFragment.put(fragmentCount, temp);
				comFile[subcount]="f,"+""+fragmentCount;
				subcount++;
				fragmentCount++;
				count+=temp.length();
				//break;
			}else{
				//determine the window of block scanning
				if(count+2*BLOCKSIZE-1<content.length()){
					blocks=mSlideBlock(content,count,count+2*BLOCKSIZE-1);
				}else{
					blocks=mSlideBlock(content,count,content.length()-1);
				}
				//save the substring as a fragment and add the block address to the compressed file
				if(blocks[1]!=null){
					if(blocks[0]!=null){
						mFragment.put(fragmentCount, blocks[0]);
						comFile[subcount]="f,"+""+fragmentCount;
						fragmentCount++;
						comFile[subcount+1]="b,"+""+mCountSeeker.get(blocks[1]);
						subcount+=2;
						count+=blocks[0].length()+BLOCKSIZE;
					}else{
						comFile[subcount]="b,"+""+mCountSeeker.get(blocks[1]);
						subcount+=1;
						count+=BLOCKSIZE;
					}
				}else{
					String tempMd5=mMd5Cal(blocks[0]);
					mBlockMap.put(blocks[0], tempMd5);
					mBlockSeeker.put(blockCount, tempMd5);
					mCountSeeker.put(tempMd5, blockCount);					
					comFile[subcount]="b,"+""+blockCount;
					subcount+=1;
					blockCount++;
					count+=BLOCKSIZE;
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
	 * @param content
	 * @return an array of string regarding the map and key of each segment
	 */
	public String[] compress(String content) {
		iFile=content;
		if(mBlockMap==null){
			cFile=mInitBlocks(iFile);
		}else{
			cFile=mCompFile(iFile);
		}		
		return cFile;
	}

	/**
	 * this method is to uncompress the input to a data stream
	 * @param fileContent: the array of strings that stores the map and key of each chunk of the file
	 * @return a data stream, supposed to be identical to the original input
	 */
	public String discompress(String[] fileContent) {
		for(int i=0;i<fileContent.length-1;i++){
			String[] theBlock=fileContent[i].split(",");
			if(theBlock.length!=2||(theBlock[0]!="f"&&theBlock[0]!="b")){
				System.out.println("error in the file");
				return null;
			}else{
				int index=Integer.parseInt(theBlock[1]);
				if(theBlock[0]=="f"){
					outFile+=mFragment.get(index);
				}else{
					outFile+=mBlockSeeker.get(index);
				}
			}
		}
		return outFile;
	}
	
	public boolean deleteChunk(String[] fileContent){
		return true;
	}
}

