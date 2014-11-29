package Compress;

//import java.util.HashSet;

public class Block {
	public Block(int mIndex, String mContent,int mParentCount) {
		//public Block(int mIndex, String mContent, String mParent)
		//super();
		this.mIndex = mIndex;
		this.mContent = mContent;
		this.mParentCount=mParentCount;
//		this.mParent = new HashSet<>();
//		this.mParent.add(mParent);
	}
	public int getIndex() {
		return mIndex;
	}
	public void setIndex(int index) {
		this.mIndex = index;
	}
	public String getContent() {
		return mContent;
	}
	public void setContent(String content) {
		this.mContent = content;
	}
	public void setParentCount(int parentCount){
		this.mParentCount= parentCount;
	}
	public int getParentCount(){
		return mParentCount;
	}
	public void addParentCount(){
		this.mParentCount+=1;
	}
	public void subParentCount(){
		this.mParentCount-=1;
	}
//	public int getParentSize() {
//		if(mParent == null)
//			return 0;
//		return mParent.size();
//	}
//	public void addParent(String parent){
//		if(mParent == null)
//			mParent = new HashSet<>();
//		mParent.add(parent);
//	}
//	public void delParent(String parent){
//		mParent.remove(parent);
//	}
	private int mIndex;
	private String mContent;
	private int mParentCount;
//	private HashSet<String> mParent;
}
