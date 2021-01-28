package kakao.itstudy.mysqlserver.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface ItemService {
	public void list(HttpServletRequest request);
	public void detail(HttpServletRequest request);
	public void insert(MultipartHttpServletRequest request);
	public void update(MultipartHttpServletRequest request);
	public void delete(HttpServletRequest request);
	
	public void date(HttpServletRequest request);
}