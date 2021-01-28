package kakao.itstudy.mysqlserver.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface MemberService {
	//회원가입을 위한 메소드
	public void join(MultipartHttpServletRequest request);
	//별명 목록을 위한 메소드
	public void nicknamelist(HttpServletRequest request);
	//로그인을 위한 메소드
	public void login(HttpServletRequest request);
	//회원정보 수정을 위한 메소드
	public void update(MultipartHttpServletRequest request);
	//회원탈퇴를 위한 메소드
	public void secession(HttpServletRequest request);
}
