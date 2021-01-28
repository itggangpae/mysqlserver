package kakao.itstudy.mysqlserver;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kakao.itstudy.mysqlserver.service.MemberService;
import kakao.itstudy.mysqlserver.service.MovieService;

@RestController
public class MovieDataController {
	@Autowired
	private MovieService movieService;
	
	@RequestMapping("movie/list")
	public Map<String, Object> list(HttpServletRequest request) {
		movieService.list(request);
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("result");
		return map;
	}

	@RequestMapping("movie/detail")
	public Map<String, Object> detail(HttpServletRequest request) {
		movieService.detail(request);
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("result");
		return map;
	}
	
	@RequestMapping("movie/insert")
	public Map<String, Object> insert(HttpServletRequest request) {
		movieService.insert(request);
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("result");
		return map;
	}
}
