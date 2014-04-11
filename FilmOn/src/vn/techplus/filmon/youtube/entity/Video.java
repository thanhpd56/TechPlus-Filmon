package vn.techplus.filmon.youtube.entity;

public class Video {
	public String video_Id;
	public String video_name;
	public String video_thumb;

	public Video(String video_Id, String video_name, String video_thumb) {
		super();
		this.video_Id = video_Id;
		this.video_name = video_name;
		this.video_thumb = video_thumb;
	}

	public String getVideo_Id() {
		return video_Id;
	}

	public void setVideo_Id(String video_Id) {
		this.video_Id = video_Id;
	}

	public String getVideo_name() {
		return video_name;
	}

	public void setVideo_name(String video_name) {
		this.video_name = video_name;
	}

	public String getVideo_thumb() {
		return video_thumb;
	}

	public void setVideo_thumb(String video_thumb) {
		this.video_thumb = video_thumb;
	}

	@Override
	public String toString() {
		return String.format("- %s %s %s\n", video_Id, video_name, video_thumb);
	}

}
