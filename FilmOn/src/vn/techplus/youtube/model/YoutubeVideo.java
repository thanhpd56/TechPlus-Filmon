package vn.techplus.youtube.model;

public class YoutubeVideo {
	private int id;
	private String title;
	private String videoId;
	private String image_thumb;
	
	
	// setter
	public void setId(int id){
		this.id = id;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setImageThumb(String img_thumb){
		this.image_thumb = img_thumb;
	}
	public void setVideoId(String videoId){
		this.videoId = videoId;
	}
	
	// getter
	public int getId(){
		return this.id;
	}
	public String getTitle(){
		return this.title;
	}
	public String getVideoId(){
		return this.videoId;
	}
	public String getImageThumb(){
		return this.image_thumb;
	}
	
}
