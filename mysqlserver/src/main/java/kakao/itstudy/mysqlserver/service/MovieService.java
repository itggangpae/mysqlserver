package kakao.itstudy.mysqlserver.service;

import javax.servlet.http.HttpServletRequest;

public interface MovieService {
	public void list(HttpServletRequest request);
	public void detail(HttpServletRequest request);
	public void insert(HttpServletRequest request);
}
