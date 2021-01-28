package kakao.itstudy.mysqlserver.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kakao.itstudy.mysqlserver.dao.ItemDAO;
import kakao.itstudy.mysqlserver.domain.Item;

@Service
public class ItemServiceImpl implements ItemService {
	private String filePath = "item.txt";

	@Autowired
	private ItemDAO itemDao;

	@Override
	@Transactional
	public void list(HttpServletRequest request) {
		// 페이지 번호와 페이지 당 데이터 개수 읽어와서 데이터의 인덱스를 생성
		String pageNo = request.getParameter("pageno");
		String pagecnt = request.getParameter("count");
		//검색조건과 검색어를 저장 
		String searchtype = request.getParameter("searchtype");
		String keyword = request.getParameter("keyword");

		int size = 3;
		//한 번에 가져올 데이터 개수를 설정
		if(pagecnt != null) {
			size = Integer.parseInt(pagecnt);
		}
		//시작번호와 종료번호를 계산
		int start = 0;		
		if (pageNo != null) {
			start = (Integer.parseInt(pageNo)-1) * size ;
		}

		if(searchtype==null) {
			searchtype="";
		}

		if(keyword == null) {
			keyword="";
		}else {
			keyword = keyword.toLowerCase();
		}

		//파라미터를 가지고 DAO의 파라미터 만들기
		Map<String, Object> map = new HashMap<>();
		map.put("start", start);
		map.put("size", size);
		map.put("searchtype", searchtype);
		map.put("keyword", keyword);

		int count = itemDao.count(map).intValue();
		List<Item> list = itemDao.list(map);


		//DAO의 메소드를 호출해서 결과 가져오기 - 데이터 개수와 데이터 목록
		map=  new HashMap<String, Object>();
		map.put("list", list);
		map.put("count", count);

		//결과 저장하기
		request.setAttribute("result", map);
	}

	@Override
	@Transactional
	public void detail(HttpServletRequest request) {
		String itemid = request.getParameter("itemid");
		Item item = null;
		if(itemid != null && itemid.trim().length() > 0) {
			item = itemDao.detail(Integer.parseInt(itemid.trim()));
		}
		Map<String, Object> map =  new HashMap<String, Object>();
		if(item == null) {
			map.put("result", false);
		}else {
			map.put("result",true);
		}
		map.put("item", item);
		request.setAttribute("result", map);
	}

	@Override
	@Transactional
	public void insert(MultipartHttpServletRequest request) {
		// 데이터 개수를 확인해서 데이터가 없으면 id는 1 그렇지 않으면 가장 큰 아이디 + 1
		Map<String, Object> map = new HashMap<>();
		try {
			BigInteger count = itemDao.count(map);
			int itemid = 1;
			if (count.intValue() != 0) {
				itemid = itemDao.maxId() + 1;
			}

			// 전송된 파라미터 읽기
			String itemname = request.getParameter("itemname");
			String description = request.getParameter("description");
			int price = Integer.parseInt(request.getParameter("price"));
			String pictureUrl = "default.jpg";
			MultipartFile image = request.getFile("pictureurl");

			// 업로드 하는 파일이 존재하면 새로운 파일이름을 만들고 파일 업로드
			if (image != null && image.isEmpty() == false) {
				// 서버 애플리케이션의 img 디렉토리의 절대 경로 만들기
				String filePath = request.getServletContext().getRealPath("/img");
				// 랜덤한 문자열을 생성하고 원래 파일 이름과 합쳐서 새로운 파일 이름 만들기
				pictureUrl = UUID.randomUUID() + image.getOriginalFilename();

				// 파일을 업로드할 경로 만들기
				File file = new File(filePath + "/" + pictureUrl);
				// 파일 업로드 하기
				try (FileOutputStream fos = new FileOutputStream(file);) {
					fos.write(image.getBytes());
					fos.flush();
				} catch (Exception e) {
					System.out.println("전송 실패:" + e.getMessage());
				}
			}

			// DAO 파라미터 만들기
			Item item = new Item();
			item.setItemid(itemid);
			item.setItemname(itemname);
			item.setPrice(price);
			item.setDescription(description);
			item.setPictureurl(pictureUrl);

			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			item.setUpdatedate(sdf.format(today));
			
			// DAO 메소드를 호출하고 결과를 확인해서 insert라는 이름으로 결과를 저장
			Serializable row = itemDao.insert(item);
			map = new HashMap<String, Object>();
			if (row != null) {
				map.put("result", true);

				String filepath = request.getServletContext().getRealPath("/update") + "/" + filePath;
				PrintWriter pw = new PrintWriter(filepath);
				pw.println(sdf.format(today));
				pw.flush();
				pw.close();
			} else {
				map.put("result", false);
				map.put("error", "서버 처리 오류");
			}
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("result", false);
			map.put("error", e.getMessage());
			System.out.println(e.getMessage());
		}
		request.setAttribute("result", map);
	}

	@Override
	@Transactional
	public void update(MultipartHttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 전송된 파라미터 읽기
			int itemid = Integer.parseInt(request.getParameter("itemid"));
			String itemname = request.getParameter("itemname");
			String description = request.getParameter("description");
			int price = Integer.parseInt(request.getParameter("price"));
			String pictureUrl = request.getParameter("oldpictureurl");
			MultipartFile image = request.getFile("pictureurl");

			// 업로드 하는 파일이 존재하면 새로운 파일이름을 만들고 파일 업로드
			if (image != null && image.isEmpty() == false) {
				// 서버 애플리케이션의 img 디렉토리의 절대 경로 만들기
				String filePath = request.getServletContext().getRealPath("/img");
				// 랜덤한 문자열을 생성하고 원래 파일 이름과 합쳐서 새로운 파일 이름 만들기
				pictureUrl = UUID.randomUUID() + image.getOriginalFilename();

				// 파일을 업로드할 경로 만들기
				File file = new File(filePath + "/" + pictureUrl);
				// 파일 업로드 하기
				try (FileOutputStream fos = new FileOutputStream(file);) {
					fos.write(image.getBytes());
					fos.flush();
				} catch (Exception e) {
					System.out.println("전송 실패:" + e.getMessage());
				}
			}

			// DAO 파라미터 만들기
			Item item = new Item();
			item.setItemid(itemid);
			item.setItemname(itemname);
			item.setPrice(price);
			item.setDescription(description);
			item.setPictureurl(pictureUrl);

			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			item.setUpdatedate(sdf.format(today));
			
			// DAO 메소드를 호출하고 결과를 확인해서 insert라는 이름으로 결과를 저장
			itemDao.update(item);
			map = new HashMap<String, Object>();
			map.put("result", true);

			String filepath = request.getServletContext().getRealPath("/update") + "/" + filePath;
			PrintWriter pw = new PrintWriter(filepath);
			pw.println(sdf.format(today));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("result", false);
			map.put("error", e.getMessage());
			System.out.println(e.getMessage());
		}
		request.setAttribute("result", map);
	}

	@Override
	@Transactional
	public void delete(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 전송된 파라미터 읽기
			String itemid = request.getParameter("itemid");

			// DAO 파라미터 만들기
			Item item = new Item();
			item.setItemid(Integer.parseInt(itemid));

			// DAO 메소드를 호출하고 결과를 확인해서 insert라는 이름으로 결과를 저장
			itemDao.delete(item);
			map.put("result", true);

			String filepath = request.getServletContext().getRealPath("/update") + "/" + filePath;
			PrintWriter pw = new PrintWriter(filepath);
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			pw.println(sdf.format(today));
			pw.flush();
			pw.close();
			
		} catch (Exception e) {
			map.put("result", false);
			map.put("error", e.getMessage());
			System.out.println(e.getMessage());
		}
		request.setAttribute("result", map);
	}

	@Override
	public void date(HttpServletRequest request) {
		// 데이터 개수를 확인해서 데이터가 없으면 id는 1 그렇지 않으면 가장 큰 아이디 + 1
		Map<String, Object> map = new HashMap<>();
		try {
			String filepath = request.getServletContext().getRealPath("/update") + "/" + filePath;
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
			String result = br.readLine();
			br.close();
			map.put("result", result);
		} catch (Exception e) {
			map.put("result", "2020-01-01");
		}
		request.setAttribute("result", map);
	}
}

