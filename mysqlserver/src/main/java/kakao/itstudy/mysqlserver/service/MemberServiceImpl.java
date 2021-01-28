package kakao.itstudy.mysqlserver.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kakao.itstudy.mysqlserver.dao.MemberDAO;
import kakao.itstudy.mysqlserver.domain.Member;
import kakao.itstudy.mysqlserver.util.CryptoUtil;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberDAO memberDao;
	
	CryptoUtil cryptoUtil = new CryptoUtil();
	
	@Override
	@Transactional
	public void join(MultipartHttpServletRequest request) {
		// 기본값 저장
		Map<String, Object> map = new HashMap<>();
		map.put("result", true);
		map.put("emailcheck", true);
		map.put("nicknamecheck", true);

		// 파라미터 가져오기
		String email = request.getParameter("email");
		String nickname = request.getParameter("nickname");
		String pw = request.getParameter("pw");

		// email 중복 체크
		try {
			List<String> emailList = memberDao.emailcheck(cryptoUtil.encrypt(email));
			if (emailList != null && emailList.size() >= 1) {
				map.put("result", false);
				map.put("emailcheck", false);
				request.setAttribute("result", map);
				return;
			}
		} catch (Exception e) {
			System.out.println("이메일 체크 예외" + e.getMessage());
		}

		// 닉네임 중복체크
		List<String> nicknameList = memberDao.nicknamecheck(nickname);
		if (nicknameList != null && nicknameList.size() >= 1) {
			map.put("result", false);
			map.put("nicknamecheck", false);
			request.setAttribute("result", map);
			return;
		}

		String profile = "default.jpg";
		MultipartFile image = request.getFile("profile");

		// 파일이 존재하면 새로운 파일이름을 만들고 파일 업로드
		if (image != null && image.isEmpty() == false) {
			String filePath = request.getServletContext().getRealPath("/profile");
			profile = UUID.randomUUID() + image.getOriginalFilename();
			filePath = filePath + "/" + profile;

			File file = new File(filePath);
			FileOutputStream fos = null;

			try {
				fos = new FileOutputStream(file);
				fos.write(image.getBytes());
				System.out.println("fos:" + fos);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("전송 실패");
			}
		}

		// DAO 파라미터 만들기
		Member member = new Member();

		try {
			member.setEmail(cryptoUtil.encrypt(email));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		member.setPw(BCrypt.hashpw(pw, BCrypt.gensalt()));
		member.setNickname(nickname);
		member.setProfile(profile);
		member.setRegdate(new Date());
		member.setLogindate(new Date());

		// DAO 메소드를 호출하고 결과를 확인해서 결과를 저장
		Serializable result = memberDao.join(member);
		if (result == null) {
			map.put("result", false);
		}
		request.setAttribute("result", map);
	}

	@Override
	@Transactional
	public void login(HttpServletRequest request) {
		System.out.println("로그인");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("login", false);
		String email = request.getParameter("email");
		String pw = request.getParameter("pw");
		System.out.println(email);
		System.out.println(pw);

		try {
			List<Member> list = memberDao.login(cryptoUtil.encrypt(email));
			System.out.println("list:" + list.toString());

			for (Member member : list) {
				if (BCrypt.checkpw(pw, member.getPw())) {
					// 로그인 한 날짜 업데이트
					member.setLogindate(new Date());
					memberDao.update(member);

					// 결과를 저장
					result.put("login", true);
					result.put("email", cryptoUtil.decrypt(member.getEmail()));
					result.put("nickname", member.getNickname());
					result.put("profile", member.getProfile());
					result.put("regdate", member.getRegdate());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		request.setAttribute("result", result);
	}

	@Override
	@Transactional
	public void update(MultipartHttpServletRequest request) {
		System.out.println("UpdqteService");
		Map<String, Object> map = new HashMap<>();
		map.put("update", false);

		String email = request.getParameter("email");
		String nickname = request.getParameter("nickname");
		String pw = request.getParameter("pw");
		String regdate = request.getParameter("regdate");
		String profile = request.getParameter("oldprofile");
		
		try {
			MultipartFile image = request.getFile("profile");
			// 파일이 존재하면 새로운 파일이름을 만들고 파일 업로드
			if (image != null && image.isEmpty() == false) {
				String filePath = request.getServletContext().getRealPath("/profile");
				profile = UUID.randomUUID() + image.getOriginalFilename();
				filePath = filePath + "/" + profile;

				File file = new File(filePath);
				FileOutputStream fos = null;


				fos = new FileOutputStream(file);
				fos.write(image.getBytes());
				fos.close();
			}

			// DAO 파라미터 만들기
			Member member = new Member();

			member.setEmail(cryptoUtil.encrypt(email));
			member.setPw(BCrypt.hashpw(pw, BCrypt.gensalt()));
			member.setNickname(nickname);
			member.setProfile(profile);
			member.setRegdate(new Date(Long.parseLong(regdate)));
			member.setLogindate(new Date());
			System.out.println(member);

			memberDao.update(member);
			map.put("update", true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// DAO 메소드를 호출하고 결과를 확인해서 insert라는 이름으로 결과를 저장
		request.setAttribute("result", map);
	}

	@Override
	@Transactional
	public void secession(HttpServletRequest request) {
		String email = request.getParameter("email");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("secession", false);
		Member member = new Member();
		try {
			member.setEmail(cryptoUtil.encrypt(email));
			memberDao.delete(member);
			map.put("secession", true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		request.setAttribute("result", map);
	}

	@Override
	@Transactional
	public void nicknamelist(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> list = memberDao.nicknamelist();
		map.put("list", list);
		request.setAttribute("result", map);
	}
}
