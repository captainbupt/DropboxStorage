package Compress;

import java.util.HashMap;
import java.util.HashSet;

public class CompressManager {
	private static CompressManager mCompressManager;
	// checkSum, <hash, fileName>
	private HashMap<Integer, HashMap<String, String>> mBlockMap;

	public CompressManager() {

	}

	public static void init() {
		mCompressManager = new CompressManager();
	}

	public CompressManager getInstance() {
		return mCompressManager;
	}

	public String compress(String content) {
		return content;
	}

	public String discompress(String fileContent) {
		return fileContent;
	}
}
