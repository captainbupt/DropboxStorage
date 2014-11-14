package Compress;


import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

import FileOperation.FileOperation;

public class CompressManager {
	private static CompressManager mCompressManager;
	// checkSum, <hash, fileName>
	//private HashMap<Integer, HashMap<String, String>> mBlockMap;
	private HashMap<String,String> mBlockMap;
	private int BLOCKSIZE;
	private String inFile;
	private ArrayList<String> curFiles=new ArrayList<String>();
	private ArrayList<String> fileContents=new ArrayList<String>();
	
	 
	
	
	
	public CompressManager() {
		//inFile=s;
		FileOperation.readFile("");

	}
/**
 * 把所有以保存的file写入map
 */
	public static void init() {
		mCompressManager = new CompressManager();
	}

	public static CompressManager getInstance() {
		return mCompressManager;
	}

	public String[] compress(String content) {
		return null;
	}

	public String discompress(String[] fileContent) {
		return null;
	}
}

