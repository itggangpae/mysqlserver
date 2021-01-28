package kakao.itstudy.mysqlserver.service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kakao.itstudy.mysqlserver.dao.MovieDAO;
import kakao.itstudy.mysqlserver.domain.Item;
import kakao.itstudy.mysqlserver.domain.Movie;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieDAO movieDao;

	@Override
	@Transactional
	public void list(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 페이지 번호와 페이지 당 데이터 개수 읽어와서 데이터의 인덱스를 생성
		String pageNo = request.getParameter("page");
		String pagecnt = request.getParameter("pagecnt");
		// 검색조건과 검색어를 저장
		String searchtype = request.getParameter("searchtype");
		String keyword = request.getParameter("keyword");

		int size = 10;
		// 한 번에 가져올 데이터 개수를 설정
		if (pagecnt != null) {
			size = Integer.parseInt(pagecnt);
		}
		// 시작번호와 종료번호를 계산
		int start = 0;

		if (pageNo != null) {
			start = (Integer.parseInt(pageNo) - 1) * size;
		}

		if (searchtype == null) {
			searchtype = "";
		}

		if (keyword == null) {
			keyword = "";
		} else {
			keyword = keyword.toLowerCase();
		}

		// 파라미터를 가지고 DAO의 파라미터 만들기
		Map<String, Object> map = new HashMap<>();
		map.put("start", start);
		map.put("size", size);
		map.put("searchtype", searchtype);
		map.put("keyword", keyword);
		System.out.println(map);
		int count = movieDao.count(map).intValue();
		List<Movie> list = movieDao.list(map);

		// DAO의 메소드를 호출해서 결과 가져오기 - 데이터 개수와 데이터 목록
		map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("count", count);

		// 결과 저장하기
		request.setAttribute("result", map);
	}

	
	@Override
	@Transactional
	public void detail(HttpServletRequest request) {
		String movieid = request.getParameter("movieid");
		Movie movie = movieDao.detail(movieid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("movie", movie);
		request.setAttribute("result", map);
	}

	
	@Override
	@Transactional
	public void insert(HttpServletRequest request) {

		int auto = 1;
		try {
			for (int i = 1; i <= 427; i = i + 1) {
				String addr = "http://swiftapi.rubypaper.co.kr:2029/hoppin/movies?version=1&page=" + i
						+ "&count=10&genreId=&order=releasedateasc";
				URL url = new URL(addr);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(30000);
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				StringBuilder sb = new StringBuilder();
				while (true) {
					String line = br.readLine();
					if (line == null)
						break;
					sb.append(line + "\n");
				}

				JSONObject main = new JSONObject(sb.toString());
				JSONObject hoppin = main.getJSONObject("hoppin");
				JSONObject moviejson = hoppin.getJSONObject("movies");

				JSONArray movies = moviejson.getJSONArray("movie");
				
				try {

					for (int j = 0; j < movies.length(); j = j + 1) {
						JSONObject item = movies.getJSONObject(j);
						Movie movie = new Movie();
						movie.setMovieid(auto++);
						String title = item.getString("title");
						
						int idx = title.lastIndexOf("]");
						if(idx > 0) {
							title = title.substring(idx+1, title.length()-1);
						}
						movie.setTitle(title);
						movie.setGenre(item.getString("genreNames"));
						movie.setRating(item.getFloat("ratingAverage"));
						String [] imageNames = item.getString("thumbnailImage").split("/");
						movie.setThumbnail(imageNames[imageNames.length-1]);
						movie.setLink(item.getString("linkUrl"));
						Thread.sleep(1000);
						title = URLEncoder.encode(title, "utf-8");
						String movieaddr = "https://openapi.naver.com/v1/search/movie.json?query=" + title;
						System.out.println(movieaddr);
						String clientId = "e8skPBzl_nGhkGyiKjPU"; //애플리케이션 클라이언트 아이디값"
				        String clientSecret = "ySW9Y4g4iv"; //애플리케이션 클라이언트 시크릿값"
				       
				        Map<String, String> requestHeaders = new HashMap<>();
				        requestHeaders.put("X-Naver-Client-Id", clientId);
				        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
				        
				        URL moiveurl = new URL(movieaddr);
						HttpURLConnection moviecon = 
							(HttpURLConnection)
							moiveurl.openConnection();
						moviecon.setConnectTimeout(30000);
						
						for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
							moviecon.setRequestProperty(header.getKey(), header.getValue());
						}
				        
						BufferedReader moviebr = 
							new BufferedReader(
								new InputStreamReader(
									moviecon.getInputStream()));
						StringBuilder moviesb = new StringBuilder();
						while(true) {
							String line = moviebr.readLine();
							if(line == null) {
								break;
							}
							moviesb.append(line + "\n");
						}
						
						moviebr.close();
						moviecon.disconnect();
						try {
							String naverjson = moviesb.toString();
							JSONObject naverobj = new JSONObject(naverjson);
							if(naverobj.getInt("total") > 0) {
								JSONArray ar = naverobj.getJSONArray("items");
								JSONObject ob = ar.getJSONObject(0);
								movie.setSubtitle(ob.getString("subtitle"));
								movie.setDirector(ob.getString("director"));
								movie.setActor(ob.getString("actor"));
								movie.setPubdate(ob.getString("pubDate"));
								movie.setLink(ob.getString("link"));
								
							}
						}
						catch (Exception e) {
							System.out.println(e.getMessage());
						}
						finally {
							System.out.println(movie);
							movieDao.insert(movie);
						}
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
