package Operator;

import java.util.ArrayList;
import java.util.HashMap;

import Compress.Compresser;

public class DuplicatedFileOperator {
	public HashMap<String, ArrayList<String>> mFileMap;
	public Compresser compresser;
	
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
