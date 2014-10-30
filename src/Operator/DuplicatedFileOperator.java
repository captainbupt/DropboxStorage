package Operator;

import java.util.ArrayList;
import java.util.HashMap;

import Compress.CompressManager;

public class DuplicatedFileOperator {
	public DuplicatedFileOperator mDuplicatedFileOperator;
	public HashMap<String, ArrayList<String>> mFileMap;
	
	public DuplicatedFileOperator(){
		
	}
	
	public DuplicatedFileOperator getInstance(){
		if(mDuplicatedFileOperator==null)
			mDuplicatedFileOperator = new DuplicatedFileOperator();
		return mDuplicatedFileOperator;
	}
	
	public String loadFile(String fileName){
		return fileName;
	}
	
	public boolean insertFile(String filePath){
		return false;
	}
	
	public boolean deleteFile(String fileName){
		return false;
	}
	
	public String[] getAllFile(){
		return null;		
	}
}
