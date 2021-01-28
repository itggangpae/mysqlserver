package kakao.itstudy.mysqlserver;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kakao.itstudy.mysqlserver.service.ItemService;

@RestController
public class ItemDataController {
	
	@Autowired
	private ItemService itemService;

	@RequestMapping("item/list")
	public Map<String, Object> list(HttpServletRequest request) {
		itemService.list(request);
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("result");
		return map;
	}

	@RequestMapping("item/detail")
	public Map<String, Object> detail(HttpServletRequest request) {
		itemService.detail(request);
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("result");
		return map;
	}

	@RequestMapping(value="item/insert", method=RequestMethod.POST)
	public Map<String, Object> insert(MultipartHttpServletRequest request) {
		itemService.insert(request);
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("result");
		return map;
	}
	
	@RequestMapping(value="item/update", method=RequestMethod.POST)
	public Map<String, Object> update(MultipartHttpServletRequest request) {
		itemService.update(request);
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("result");
		return map;
	}
	
	@RequestMapping(value="item/delete", method=RequestMethod.GET)
	public Map<String, Object> delete(HttpServletRequest request) {
		itemService.delete(request);
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("result");
		return map;
	}
	
	@RequestMapping(value="item/date", method=RequestMethod.GET)
	public Map<String, Object> date(HttpServletRequest request) {
		itemService.date(request);
		Map<String, Object> map = (Map<String, Object>) request.getAttribute("result");
		return map;
	}
}
