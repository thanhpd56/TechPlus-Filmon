package vn.techplus.filmon.moviesreview;

import java.util.ArrayList;
import java.util.List;

import vn.techplus.filmon.utils.Log;
import android.text.TextUtils;

import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.TheMovieDbApi;
import com.omertron.themoviedbapi.model.Genre;
import com.omertron.themoviedbapi.model.Language;
import com.omertron.themoviedbapi.model.MovieDb;
import com.omertron.themoviedbapi.model.PersonCast;
import com.omertron.themoviedbapi.model.PersonCrew;
import com.omertron.themoviedbapi.model.ProductionCompany;
import com.omertron.themoviedbapi.model.ProductionCountry;
import com.omertron.themoviedbapi.model.TmdbConfiguration;
import com.omertron.themoviedbapi.model.Trailer;
import com.omertron.themoviedbapi.results.TmdbResultsList;

public class TheMoviesDB extends MovieReview {
	private static final String API_KEY = "30715d01cfefabddf07afcb88d428b78";
	private static enum ImageType {
		BACKDROP, POSTER, LOGO, PROFILE, STILL
	}
	
	// Languages
    private static final String LANG_DEFAULT = "";    
    //response params
    private static final String APPEND_TO_RESPONSE = "alternative_titles,casts,images,keywords,releases,trailers,translations,similar_movies,reviews,lists";
    
	private TheMovieDbApi mMoviesDB;
	private TmdbConfiguration mConfiguration;
	
	
	public void getConfiguration() {
		mConfiguration = mMoviesDB.getConfiguration();
	}
	
	public TheMoviesDB() throws MovieDbException {
		mMoviesDB = new TheMovieDbApi(API_KEY);
		getConfiguration();
	}

	@Override
	public List<MovieInfo> getTopRated() {
		return getTopRated(0);
	}

	@Override
	public List<MovieInfo> getTopRated(int page) {
		Log.i("getTopRate");
		
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		try {
			TmdbResultsList<MovieDb> listMovieDbs = mMoviesDB.getTopRatedMovies(LANG_DEFAULT, page);
			Log.i("Top Rate Total results: " + listMovieDbs.getTotalResults());
			if (listMovieDbs != null) {
				
				List<MovieDb> movieDbs = listMovieDbs.getResults();
				for (MovieDb movieDb: movieDbs) {					
					movieInfos.add(convertMovieDbToMovie(movieDb, false));
				}
			}
		} catch (MovieDbException e) {
			e.printStackTrace();
		}
		return movieInfos;
	}

	@Override
	public List<MovieInfo> getUpcoming() {
		return getUpcoming(0);
	}

	@Override
	public List<MovieInfo> getUpcoming(int page) {
		Log.i("getUpComing");
		
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		try {
			TmdbResultsList<MovieDb> listMovieDbs = mMoviesDB.getUpcoming(LANG_DEFAULT, page);
			Log.i("UpComing Total results: " + listMovieDbs.getTotalResults());
			if (listMovieDbs != null) {
				
				List<MovieDb> movieDbs = listMovieDbs.getResults();
				for (MovieDb movieDb: movieDbs) {					
					movieInfos.add(convertMovieDbToMovie(movieDb, false));
				}
			}
		} catch (MovieDbException e) {
			e.printStackTrace();
		}
		return movieInfos;
	}

	@Override
	public List<MovieInfo> getPopular() {
		return getPopular(0);
	}

	@Override
	public List<MovieInfo> getPopular(int page) {
		Log.i("getPopular");
		
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		try {
			TmdbResultsList<MovieDb> listMovieDbs = mMoviesDB.getPopularMovieList(LANG_DEFAULT, page);
			Log.i("Popular Total results: " + listMovieDbs.getTotalResults());
			if (listMovieDbs != null) {
				
				List<MovieDb> movieDbs = listMovieDbs.getResults();
				for (MovieDb movieDb: movieDbs) {					
					movieInfos.add(convertMovieDbToMovie(movieDb, false));
				}
			}
		} catch (MovieDbException e) {
			e.printStackTrace();
		}
		return movieInfos;
	}

	@Override
	public List<MovieInfo> getNowPlaying() {
		return getNowPlaying(0);
	}

	@Override
	public List<MovieInfo> getNowPlaying(int page) {
		Log.i("getNowPlaying");
		
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		try {
			TmdbResultsList<MovieDb> listMovieDbs = mMoviesDB.getNowPlayingMovies(LANG_DEFAULT, page);
			Log.i("NowPlaying Total results: " + listMovieDbs.getTotalResults());
			if (listMovieDbs != null) {
				
				List<MovieDb> movieDbs = listMovieDbs.getResults();
				for (MovieDb movieDb: movieDbs) {					
					movieInfos.add(convertMovieDbToMovie(movieDb, false));
				}
			}
		} catch (MovieDbException e) {
			e.printStackTrace();
		}
		return movieInfos;
	}

	@Override
	public MovieInfo getMoviesInfo(int movieId) {
		Log.i("getMovieInfo with movieId: " + movieId);
		
		MovieInfo movieInfo = null;
		try {
			MovieDb movieDb = mMoviesDB.getMovieInfo(movieId, LANG_DEFAULT, APPEND_TO_RESPONSE);
			Log.i("Popular Total results: " + movieDb.toString());
			if (movieDb != null) {
				movieInfo = convertMovieDbToMovie(movieDb, true);
			}
		} catch (MovieDbException e) {
			e.printStackTrace();
		}
		return movieInfo;
	}

	@Override
	public List<MovieInfo> searchMovies(String key) {
		return searchMovies(key, 0);
	}

	@Override
	public List<MovieInfo> searchMovies(String key, int page) {
		Log.i("search Movies with key: " + key);
		
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		try {
			TmdbResultsList<MovieDb> listMovieDbs = mMoviesDB.searchMovie(key, 0, LANG_DEFAULT, false, page);
			Log.i("SearchMovies Total results: " + listMovieDbs.getTotalResults());
			if (listMovieDbs != null) {
				
				List<MovieDb> movieDbs = listMovieDbs.getResults();
				for (MovieDb movieDb: movieDbs) {					
					movieInfos.add(convertMovieDbToMovie(movieDb, false));
				}
			}
		} catch (MovieDbException e) {
			e.printStackTrace();
		}
		return movieInfos;
	}
	
	@Override
	public Object getTrailers(int movieId) {
		throw new RuntimeException("not yet implements this method");
		/*try {
			TmdbResultsList<Trailer> trailers = mMoviesDB.getMovieTrailers(movieId, LANG_DEFAULT);
		} catch (MovieDbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;*/
	}
	
	
	private MovieInfo convertMovieDbToMovie(MovieDb movieDb, boolean full) {
		MovieInfo movieInfo = new MovieInfo();
		movieInfo.setId(movieDb.getId());
		movieInfo.setTitle(movieDb.getTitle());
		movieInfo.setBackdropImage(getFullImageLink(movieDb.getBackdropPath(), ImageType.BACKDROP));
		movieInfo.setPosterImage(getFullImageLink(movieDb.getPosterPath(), ImageType.POSTER));
		movieInfo.setReleaseDate(movieDb.getReleaseDate());
		
		if (full) {
			
			// extract full information of movie
			movieInfo.setOverview(movieDb.getOverview());
			movieInfo.setImdbId(movieDb.getImdbID());
			
			List<Genre> listGenres = movieDb.getGenres();
			if (listGenres != null) {
				List<String> genres = new ArrayList<String>();
				for (Genre genre: listGenres) {
					genres.add(genre.getName());
				}
				movieInfo.setGenres(genres);
			}
			
			List<ProductionCompany> listCompanies = movieDb.getProductionCompanies();
			if (listCompanies != null) {
				List<String> companies = new ArrayList<String>();
				for (ProductionCompany company: listCompanies) {
					companies.add(company.getName());
				}
				movieInfo.setProductionCompanies(companies);
			}
	
			List<ProductionCountry> listCountries = movieDb.getProductionCountries();
			if (listCountries != null && listCountries.size() > 0) {
				movieInfo.setCountry(listCountries.get(0).getName());
			}
	
			List<Language> listLanguages = movieDb.getSpokenLanguages();
			if (listLanguages != null && listLanguages.size() > 0) {
				movieInfo.setSpokenLanguage(listLanguages.get(0).getName());
			}
			
			List<Trailer> listTrailers = movieDb.getTrailers(); 
			if (listTrailers != null) {
				List<String> streams = new ArrayList<String>();
				List<String> videoYtIds = new ArrayList<String>();
				for (Trailer trailer: listTrailers) {
					if (trailer.getWebsite().equals(Trailer.WEBSITE_QUICKTIME)) {
						streams.add(trailer.getSource());
					} else if (trailer.getWebsite().equals(Trailer.WEBSITE_YOUTUBE)) {
						videoYtIds.add(trailer.getSource());
					}
				}
				movieInfo.setReviewStreams(streams);
				movieInfo.setVideoYoutubeIds(videoYtIds);
			}
			
			List<PersonCast> listCasts = movieDb.getCast();
			if (listCasts != null) {
				List<String> casts = new ArrayList<String>();
				for (PersonCast cast: listCasts) {
					casts.add(cast.getName() + " - " + cast.getCharacter());
				}
				movieInfo.setCasts(casts);
			}
			
			List<PersonCrew> listCrews = movieDb.getCrew();
			if (listCrews != null) {
				for (PersonCrew crew: listCrews) {
					if (crew.getJob().equalsIgnoreCase("director")) {
						movieInfo.setDirector(crew.getName());
						break;
					}
				}
			}
		}
		//Log.d(movie.toString());
		return movieInfo;
	}
	
	private String getFullImageLink(String filePath, ImageType type) {
		if (mConfiguration == null || TextUtils.isEmpty(filePath)) 
			return null;
		
		String size = "original";
		int index = 0;
		switch (type) {
		case BACKDROP :
			index = mConfiguration.getBackdropSizes().size() - 3;
			index = index < 0 ? mConfiguration.getBackdropSizes().size() - 1 : index;
			size = mConfiguration.getBackdropSizes().get(index);
			break;
			
		case POSTER :
			index = mConfiguration.getBackdropSizes().size() >= 2 ? 1 : mConfiguration.getBackdropSizes().size() - 1;
			size = mConfiguration.getPosterSizes().get(index);
			break;
			
		case PROFILE :
			index = mConfiguration.getBackdropSizes().size() >= 2 ? 1 : mConfiguration.getBackdropSizes().size() - 1;
			size = mConfiguration.getProfileSizes().get(index);
			break;
			
		case LOGO :
			index = mConfiguration.getBackdropSizes().size() - 3;
			index = index < 0 ? mConfiguration.getBackdropSizes().size() - 1 : index;
			size = mConfiguration.getLogoSizes().get(index);
			break;
			
			default :
				break;
		}
		String url = mConfiguration.getSecureBaseUrl() + size + filePath;
		return url;
	}

}
