package vn.techplus.youtube.model;

public class YoutubeCategory {
	private int id;
	private String name;
	private int parentId;
	public YoutubeCategory(){}
	public YoutubeCategory(int id, String name, int parent){
		this.id = id;
		this.name = name;
		this.parentId = parent;
	}
	// getters
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public int getParentId(){
		return this.parentId;
	}
	
	// setters
	public void setId(int id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setParentId(int parentId){
		this.parentId = parentId;
	}
}
