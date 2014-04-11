package vn.techplus.filmon.youtube.entity;

import java.util.List;

public class VideoWrapper {
	public List<Video> videos;

	public VideoWrapper(List<Video> videos) {
		this.videos = videos;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

}
