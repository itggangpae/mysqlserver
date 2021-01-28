package kakao.itstudy.mysqlserver.domain;

import lombok.Data;

@Data
public class Movie {
	private int movieid;
	private String title;
	private String subtitle;
	private String pubdate;
	private String director;
	private String actor;
	private String genre;
	private double rating;
	private String thumbnail;
	private String link;
}
