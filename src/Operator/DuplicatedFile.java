package Operator;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import Constant.FileConstant;

public class DuplicatedFile {

	public DuplicatedFile(String fileName, String fileContent) {
		super();
		this.mFileName = fileName;
		String[] tmp = fileContent.split(FileConstant.CONTENT_SEPARATOR);
		int length = tmp.length;
		mContent = new String[length - 2];
		for (int i = 0; i < length - 2; i++) {
			mContent[i] = tmp[i];
		}
		mTime = Long.parseLong(tmp[length - 2]);
		mSize = Long.parseLong(tmp[length - 1]);
	}

	public DuplicatedFile(String fileName, long mSize, String[] mContent,
			long mTime) {
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

	public long getSize() {
		return mSize;
	}

	public void setSize(long size) {
		this.mSize = size;
	}

	public String[] getContent() {
		return mContent;
	}

	public void setContent(String[] content) {
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
		for (int i = 0; i < mContent.length; i++) {
			fileContent = fileContent + mContent[i]
					+ FileConstant.CONTENT_SEPARATOR;
		}
		fileContent = fileContent + mTime + FileConstant.CONTENT_SEPARATOR;
		fileContent = fileContent + mSize;
		return fileContent;
	}

	private String mFileName;
	private long mSize;
	private String[] mContent;
	private long mTime;

}
