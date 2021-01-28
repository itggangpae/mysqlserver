package kakao.itstudy.mysqlserver;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kakao.itstudy.mysqlserver.service.MemberService;

@RestController
public class MemberDataController {
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value="member/join", method=RequestMethod.POST)
	public Map<String, Object> join(MultipartHttpServletRequest request) {
		memberService.join(request);
		Map<String, Object>map = (Map<String, Object>)request.getAttribute("result");
		return map;
	}
	
	@RequestMapping(value="member/nicknamelist", method=RequestMethod.GET)
	public Map<String, Object> nicknamelist(HttpServletRequest request) {
		memberService.nicknamelist(request);
		Map<String, Object>map = (Map<String, Object>)request.getAttribute("result");
		return map;
	}
	

	@RequestMapping(value="member/login", method=RequestMethod.POST)
	public Map<String, Object> login(HttpServletRequest request) {
		memberService.login(request);
		Map<String, Object> map = (Map<String, Object>)request.getAttribute("result");
		return map;
	}
	
	@RequestMapping(value="member/update", method=RequestMethod.POST)
	public Map<String, Object> update(MultipartHttpServletRequest request) {
		memberService.update(request);
		
		Map<String, Object>map = (Map<String, Object>)request.getAttribute("result");
		return map;
	}
	
	@RequestMapping(value="member/secession", method=RequestMethod.POST)
	public Map<String, Object> delete(HttpServletRequest request) {
		memberService.secession(request);
		Map<String, Object> map = (Map<String, Object>)request.getAttribute("result");
		return map;
	}
}