package vn.techplus.filmon.moviesreview;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class MovieInfo implements Parcelable {
	
	private int id;
	private String title;
	private List<String> genres;
	private String backdropImage;
	private String posterImage;
	private String releaseDate;
	private String overview;
	private String country;
	private List<String> productionCompanies;
	private String spokenLanguage;
	private List<String> reviewStreams;
	private List<String> videoYoutubeIds;
	private List<String> casts;
	private String director;
	private String imdbId;
	private String imdbVote;

	
	public MovieInfo() {}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public String getBackdropImage() {
		return backdropImage;
	}

	public void setBackdropImage(String backdropImage) {
		this.backdropImage = backdropImage;
	}

	public String getPosterImage() {
		return posterImage;
	}

	public void setPosterImage(String posterImage) {
		this.posterImage = posterImage;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<String> getProductionCompanies() {
		return productionCompanies;
	}

	public void setProductionCompanies(List<String> productionCompanies) {
		this.productionCompanies = productionCompanies;
	}

	public String getSpokenLanguage() {
		return spokenLanguage;
	}

	public void setSpokenLanguage(String spokenLanguage) {
		this.spokenLanguage = spokenLanguage;
	}

	public List<String> getReviewStreams() {
		return reviewStreams;
	}

	public void setReviewStreams(List<String> reviewStreams) {
		this.reviewStreams = reviewStreams;
	}

	public List<String> getVideoYoutubeIds() {
		return videoYoutubeIds;
	}

	public void setVideoYoutubeIds(List<String> videoYoutubeIds) {
		this.videoYoutubeIds = videoYoutubeIds;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public List<String> getCasts() {
		return casts;
	}

	public void setCasts(List<String> casts) {
		this.casts = casts;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getImdbVote() {
		return imdbVote;
	}

	public void setImdbVote(String imdbVote) {
		this.imdbVote = imdbVote;
	}

	@Override
	public String toString() {
		return "MovieInfo [id=" + id + ", title=" + title + ", genres="
				+ genres + ", backdropImage=" + backdropImage
				+ ", posterImage=" + posterImage + ", releaseDate="
				+ releaseDate + ", overview=" + overview + ", country="
				+ country + ", productionCompanies=" + productionCompanies
				+ ", spokenLanguage=" + spokenLanguage + ", reviewStreams="
				+ reviewStreams + ", videoYoutubeIds=" + videoYoutubeIds
				+ ", casts=" + casts + ", director=" + director + ", imdbId="
				+ imdbId + ", imdbVote=" + imdbVote + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeStringList(genres);
		dest.writeString(backdropImage);
		dest.writeString(posterImage);
		dest.writeString(releaseDate);
		dest.writeString(overview);
		dest.writeString(country);
		dest.writeStringList(productionCompanies);
		dest.writeString(spokenLanguage);
		dest.writeStringList(reviewStreams);
		dest.writeStringList(videoYoutubeIds);
		dest.writeStringList(casts);
		dest.writeString(director);
		dest.writeString(imdbId);
		dest.writeString(imdbVote);
	}

	public static Parcelable.Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
		
		@Override
		public MovieInfo[] newArray(int size) {
			return new MovieInfo[size];
		}
		
		@Override
		public MovieInfo createFromParcel(Parcel source) {
			return new MovieInfo(source);
		}
	};
	
	private MovieInfo(Parcel src) {
		id = src.readInt();
		title = src.readString();
		genres = new ArrayList<String>();
		src.readStringList(genres);
		backdropImage = src.readString();
		posterImage = src.readString();
		releaseDate = src.readString();
		overview = src.readString();
		country = src.readString();
		productionCompanies = new ArrayList<String>();
		src.readStringList(productionCompanies);
		spokenLanguage = src.readString();
		reviewStreams = new ArrayList<String>();
		src.readStringList(reviewStreams);
		videoYoutubeIds = new ArrayList<String>();
		src.readStringList(videoYoutubeIds);
		casts = new ArrayList<String>();
		src.readStringList(casts);
		director = src.readString();
		imdbId = src.readString();
		imdbVote = src.readString();
	}
	
	public static class Genre {
		public String name;
		public int id;
	}
	
	public static class ProductionCompany {
		public String name;
		public int id;
	}

}
