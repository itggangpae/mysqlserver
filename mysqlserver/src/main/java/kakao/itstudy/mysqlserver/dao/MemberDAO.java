package kakao.itstudy.mysqlserver.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kakao.itstudy.mysqlserver.domain.Member;

@Repository
public class MemberDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public List<String> emailcheck(String email) {
		List<String> list = sessionFactory.getCurrentSession().createNativeQuery("select email from member where email= :email").setParameter("email", email).getResultList();
		return list;
	}
	public List<String> nicknamecheck(String nickname) {
		List<String> list = sessionFactory.getCurrentSession()
				.createNativeQuery("select nickname from member where nickname = :nickname") .setParameter("nickname", nickname).getResultList();
		return list;
	}
	
	public Serializable join(Member member) {
		return sessionFactory.getCurrentSession().save(member);
	}
	
	public List<String> nicknamelist() {
		List<String> list = sessionFactory.getCurrentSession().createNativeQuery("select nickname from member").getResultList();
		return list;
	}
	
	public List<Member> login(String email) {
		List<Member> list = sessionFactory.getCurrentSession().
				createNativeQuery("select * from member where email= :email", Member.class).setParameter("email", email)
				.getResultList();
		return list;
	}

	public Object update(Member member) {
		return sessionFactory.getCurrentSession().merge(member);
	}

	public void delete(Member member) {
		sessionFactory.getCurrentSession().clear();
		sessionFactory.getCurrentSession().delete(member);
	}
}