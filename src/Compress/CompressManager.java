package Compress;

import java.util.HashMap;

public class CompressManager {
	private static CompressManager mCompressManager;
	// checkSum, <hash, fileName>
	private HashMap<Integer, HashMap<String, String>> mBlockMap;

	public CompressManager() {

	}

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
