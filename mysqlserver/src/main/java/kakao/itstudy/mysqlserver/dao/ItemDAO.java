package kakao.itstudy.mysqlserver.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kakao.itstudy.mysqlserver.domain.Item;

@Repository
public class ItemDAO {
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
			sql = "select count(*) from item";
			query = session.createNativeQuery(sql);
		} else if (searchtype.equals("itemname")) {
			sql = "select count(*) from item where itemname like :itemname" ;
			query = session.createNativeQuery(sql);
			query.setParameter("itemname", keyword);
		} else if (searchtype.equals("description")) {
			sql = "select count(*) from item where description like :description" ;
			query = session.createNativeQuery(sql);
			query.setParameter("description", keyword);
		}else if (searchtype.equals("both")) {
			sql = "select count(*) from item where itemname like  :itemname or description like :description" ;
			query = session.createNativeQuery(sql);
			query.setParameter("itemname", keyword);
			query.setParameter("description", keyword);
		}
		list =query.getResultList();
		return list.get(0);
	}
	
	public List<Item> list(Map<String, Object> map) {
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
		List<Item> list = null;
		if (searchtype == null || searchtype.equals("")) {
			sql = "select * from item order by itemid desc limit :start, :size ";
			query = session.createNativeQuery(sql, Item.class);
			query.setParameter("start", start);
			query.setParameter("size", size);
		} else if (searchtype.equals("itemname")) {
			sql = "select * from item where itemname like :itemname order by itemid desc limit :start, :size" ;
			query = session.createNativeQuery(sql, Item.class);
			query.setParameter("itemname", keyword);
			query.setParameter("start", start);
			query.setParameter("size", size);
		} else if (searchtype.equals("description")) {
			sql = "select * from item where description like :description order by itemid desc limit :start, :size" ;
			query = session.createNativeQuery(sql, Item.class);
			query.setParameter("description", keyword);
			query.setParameter("start", start);
			query.setParameter("size", size);
		}
		
		else if (searchtype.equals("both")) {
			sql = "select * from item where itemname like  :itemname or description like :description order by itemid desc limit :start, :size" ;
			query = session.createNativeQuery(sql, Item.class);
			query.setParameter("itemname", keyword);
			query.setParameter("description", keyword);
			query.setParameter("start", start);
			query.setParameter("size", size);
		}
		
		list = query.getResultList();
		return list;
	}

	
	
	public Item detail(int itemid) {
		Session session = sessionFactory.getCurrentSession();
		Item item = session.get(Item.class, itemid);
		return item;
	}

	public int maxId() {
		Session session = sessionFactory.getCurrentSession();
		List<Integer> list = session.createNativeQuery("select max(itemid) from item").getResultList();
		return list.get(0);
	}

	public Serializable insert(Item item) {
		Session session = sessionFactory.getCurrentSession();
		return session.save(item);
	}
	
	public void update(Item item) {
		Session session = sessionFactory.getCurrentSession();
		session.update(item);
	}
	
	public void delete(Item item) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(item);
	}
}

	