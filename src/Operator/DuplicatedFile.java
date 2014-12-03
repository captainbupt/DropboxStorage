package Operator;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import Constant.FileConstant;

public class DuplicatedFile extends Vector<String>{

	@Override
	public synchronized String elementAt(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			return mFileName;
		case 1:
			return getSizeString();
		case 2:
			return getTimeString();
		default:
			break;
		}
		return super.elementAt(index);
	}

	public DuplicatedFile(String fileName, String fileContent) {
		super();
		this.mFileName = fileName;
		String[] tmp = fileContent.split(FileConstant.CONTENT_SEPARATOR);
		int length = tmp.length;
		mContent = new ArrayList<>(length-2);
		for (int i = 0; i < length-2; i++) {
			mContent.add(tmp[i]);
		}
		mTime = Long.parseLong(tmp[length - 2]);
		mSize = Long.parseLong(tmp[length - 1]);
	}

	public DuplicatedFile(String fileName, long mSize,
			ArrayList<String> mContent, long mTime) {
		super();
		this.mFileName = fileName;
		this.mSize = mSize;
		this.mContent = mContent;
		this.mTime = mTime;
	}

	public String getFileName() {
		return mFileName;
	}

	public void setFileName(String fileName) {
		this.mFileName = fileName;
	}

	public String getSizeString() {
		NumberFormat formatter = new DecimalFormat("###,###");
		if (mSize < 1024) {
			return formatter.format(mSize) + " Bytes";
		} else {
			return formatter.format(mSize / 1024) + " KB";
		}
	}

	public long getSize() {
		return mSize;
	}

	public void setSize(long size) {
		this.mSize = size;
	}

	public ArrayList<String> getContent() {
		return mContent;
	}

	public void setContent(ArrayList<String> content) {
		this.mContent = content;
	}

	public long getTime() {
		return mTime;
	}

	public String getTimeString() {
		Date date = new Date(mTime);
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
	}

	public void setTime(long time) {
		this.mTime = time;
	}

	public String getFileContent() {
		String fileContent = "";
		for (String tmp: mContent) {
			fileContent = fileContent + tmp
					+ FileConstant.CONTENT_SEPARATOR;
		}
		fileContent = fileContent + mTime + FileConstant.CONTENT_SEPARATOR;
		fileContent = fileContent + mSize;
		return fileContent;
	}

	private String mFileName;
	private long mSize;
	private ArrayList<String> mContent;
	private long mTime;

}
