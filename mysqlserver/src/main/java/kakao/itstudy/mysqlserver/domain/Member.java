package kakao.itstudy.mysqlserver.domain;

import java.util.Date;
import lombok.Data;

@Data
public class Member {
	private String email;
	private String nickname;
	private String pw;
	private String profile;
	private Date regdate;
	private Date logindate;
}

