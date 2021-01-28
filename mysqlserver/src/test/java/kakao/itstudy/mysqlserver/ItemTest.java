package kakao.itstudy.mysqlserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import kakao.itstudy.mysqlserver.dao.ItemDAO;
import kakao.itstudy.mysqlserver.domain.Item;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })

public class ItemTest {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private SessionFactory factory;
	
	@Autowired
	private ItemDAO itemDao;

	@Test
	public void sqlSessionTest() throws Exception {
		// 데이터베이스 연결 테스트
		System.out.println(dataSource.getConnection().toString());

		// Hibernate 설정 테스트
		System.out.println(factory.toString());
		
		// ItemDAO 설정 테스트
		System.out.println(itemDao.toString());

	}
	
	@Test
	@Transactional
	public void daoTest() throws Exception {
		/*
		Map<String, Object> map = new HashMap<>();

		map.put("start", 0);
		map.put("size", 5);
		System.out.println("데이터 개수:" + itemDao.count(map));
		System.out.println("데이터 목록:" + itemDao.list(map));

		
		map.put("start", 5);
		map.put("size", 5);
		System.out.println("데이터 목록:" + itemDao.list(map));
		
		
		map.put("start", 0);
		map.put("size", 5);
		map.put("searchtype", "description");
		map.put("keyword", "비타민 C");
		System.out.println("데이터 개수:" + itemDao.count(map));
		System.out.println("데이터 목록:" + itemDao.list(map));
		
		
		map.put("searchtype", "both");
		map.put("keyword", "비타민");
		System.out.println("데이터 개수:" + itemDao.count(map));
		System.out.println("데이터 목록:" + itemDao.list(map));
		
		
		System.out.println(itemDao.detail(1).toString()); 
		*/
		
		
		/*
		int maxid = itemDao.maxId(); 
		System.out.println("가장 큰 아이디:" + maxid);
		
		Item item = new Item(); 
		item.setItemid(maxid + 1); 
		item.setItemname("과자");
		item.setPrice(1000); 
		item.setDescription("맛있다.");
		item.setPictureurl("default.jpg"); 
		
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		item.setUpdatedate(sdf.format(today));
		
		System.out.println("데이터 삽입:" + itemDao.insert(item));
		
		map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("size", 5);
		System.out.println("데이터 개수:" + itemDao.count(map));
		System.out.println("데이터 목록:" + itemDao.list(map));
		*/
		
		/*
		Item item = new Item(); 
		item.setItemid(7); 
		item.setItemname("아이패드");
		item.setPrice(1000000); 
		item.setDescription("괜찮다.");
		item.setPictureurl("default.jpg"); 
		
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		item.setUpdatedate(sdf.format(today));
		
		itemDao.update(item);
		
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("size", 5);
		System.out.println("데이터 목록:" + itemDao.list(map));
		*/
		
		Item item = new Item(); 
		item.setItemid(7); 
		
		itemDao.delete(item);
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("size", 5);
		System.out.println("데이터 목록:" + itemDao.list(map));
		
		
	}

}
