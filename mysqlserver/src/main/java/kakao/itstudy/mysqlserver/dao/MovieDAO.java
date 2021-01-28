package kakao.itstudy.mysqlserver.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kakao.itstudy.mysqlserver.domain.Movie;

@Repository
public class MovieDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	public BigInteger count(Map<String, Object> map) {
		Session session = sessionFactory.getCurrentSession();
		
		String searchtype = (String) map.get("searchtype");
		String keyword = (String) map.get("keyword");
	    if(searchtype != null) {
	    	keyword = '%' + keyword + '%';
	    }
		List<BigInteger> list = null;
		
		String sql  = "";
		Query query = null;
		if (searchtype == null || searchtype.equals("")) {
			sql = "select count(*) from movie";
			query = session.createNativeQuery(sql);
		} else if (searchtype.equals("title")) {
			sql = "select count(*) from movie where title like :title" ;
			query = session.createNativeQuery(sql);
			query.setParameter("title", keyword);
		} else if (searchtype.equals("genre")) {
			sql = "select count(*) from movie where genre like :genre" ;
			query = session.createNativeQuery(sql);
			query.setParameter("genre", keyword);
		}
		else if (searchtype.equals("both")) {
			sql = "select count(*) from movie where title like  :title or genre like :genre" ;
			query = session.createNativeQuery(sql);
			query.setParameter("title", keyword);
			query.setParameter("genre", keyword);
		}
		list =query.getResultList();
		return list.get(0);
	}

	public List<Movie> list(Map<String, Object> map) {
		Session session = sessionFactory.getCurrentSession();
		
		String searchtype = (String) map.get("searchtype");
		String keyword = (String) map.get("keyword");
		 if(searchtype != null) {
		    	keyword = '%' + keyword + '%';
		    }
		
		Integer start = (Integer) map.get("start");
		Integer size = (Integer) map.get("size");

		String sql = null;
		Query query = null;
		List<Movie> list = null;
		
		if (searchtype == null || searchtype.equals("")) {
			sql = "select * from movie order by movieId desc limit :start, :size ";
			query = session.createNativeQuery(sql, Movie.class);
			query.setParameter("start", start);
			query.setParameter("size", size);
		} else if (searchtype.equals("title")) {
			sql = "select * from movie where title like :title order by movieId desc limit :start, :size" ;
			query = session.createNativeQuery(sql, Movie.class);
			query.setParameter("title", keyword);
			query.setParameter("start", start);
			query.setParameter("size", size);
		} else if (searchtype.equals("genre")) {
			sql = "select * from movie where genre like :genre order by movieId desc limit :start, :size" ;
			query = session.createNativeQuery(sql, Movie.class);
			query.setParameter("genre", keyword);
			query.setParameter("start", start);
			query.setParameter("size", size);
		}
		else if (searchtype.equals("both")) {
			sql = "select * from movie where title like  :title or genre like :genre order by movieId desc limit :start, :size" ;
			query = session.createNativeQuery(sql, Movie.class);
			query.setParameter("title", keyword);
			query.setParameter("genre", keyword);
			query.setParameter("start", start);
			query.setParameter("size", size);
		}
		
		list = query.getResultList();
		return list;
	}

	
	public Movie detail(String movieId) {
		Session session = sessionFactory.getCurrentSession();
		Movie movie = session.get(Movie.class, Integer.parseInt(movieId));
		return movie;
	}
	
	public Serializable insert(Movie movie) {
		Session session = sessionFactory.getCurrentSession();
		return session.save(movie);
	}
}
