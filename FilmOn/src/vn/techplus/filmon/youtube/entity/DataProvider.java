package vn.techplus.filmon.youtube.entity;

import java.util.List;

public class DataProvider {
	public List<Video> _listVideo;
	public MovieTypeWrapper _movieTypeWrapper;

	public DataProvider(List<Video> _listVideo,
			MovieTypeWrapper _movieTypeWrapper) {
		super();
		this._listVideo = _listVideo;
		this._movieTypeWrapper = _movieTypeWrapper;
	}

	public List<Video> get_listVideo() {
		return _listVideo;
	}

	public void set_listVideo(List<Video> _listVideo) {
		this._listVideo = _listVideo;
	}

	public MovieTypeWrapper getMovieTypeWrapper() {
		return _movieTypeWrapper;
	}

	public void setMovieTypeWrapper(MovieTypeWrapper movieTypeWrapper) {
		this._movieTypeWrapper = movieTypeWrapper;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("List Movie: \n");
		for(MovieType movieType : _movieTypeWrapper.getListMovieType()){
			sb.append(movieType.toString());
		}
		sb.append("\nList Video: \n");
		for (Video video : _listVideo) {
			sb.append(video.toString());
		}
		return sb.toString();
	}

}
