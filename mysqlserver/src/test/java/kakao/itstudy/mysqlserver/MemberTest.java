package kakao.itstudy.mysqlserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import kakao.itstudy.mysqlserver.dao.MemberDAO;
import kakao.itstudy.mysqlserver.domain.Member;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })

public class MemberTest {
	@Autowired
	private MemberDAO memberDao;
	
	@Test
	@Transactional
	public void daoTest() throws Exception {
			//System.out.println(memberDao);
		
			
			//회원가입
			Member member = new Member();
			member.setEmail("itstudy@kakao.com");
			member.setPw("1234");
			member.setNickname("군계");
			member.setProfile("default.jpg");
			System.err.println(memberDao.join(member));
				
			System.err.println(memberDao.emailcheck("itstudy@kakao.com"));
			System.err.println(memberDao.emailcheck("ggangpae1@gmail.com"));
			
			
			System.err.println(memberDao.nicknamecheck("관리자"));
			System.err.println(memberDao.nicknamecheck("군계"));
			
			
			System.out.println(memberDao.login("itstudy@kakao.com").get(0));
			member = new Member();
			member.setEmail("ggangpae1@gmail.com");
			member.setPw("12345678");
			member.setNickname("관리자");
			member.setProfile("default.jpg");
			System.err.println(memberDao.update(member));
			
			
			memberDao.delete(member);
		}
	}
