<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MobileServer</title>
</head>
<body>
	<h2>ITEM</h2>
	<h3>목록 보기 - item/list(GET)</h3>
	<p><b>파라미터</b></p>
	<p>pageno(페이지번호 - 생략 가능), count(페이지 당 데이터 개수 - 기본값 3), searchtype(검색항목-itemname, description, both), keyword(검색어)
	<br>모든 파라미터 생략 가능</p>
	<p><b>결과</b></p>
	<p>count: 데이터 개수, list: 데이터 목록(itemid:숫자, itemname:문자열, price:숫자, description:문자열, pictureurl:문자열, updatedate:문자열)</p>
	
	<h3>마지막 업데이트 시간 보기 - item/date(GET)</h3>
	<p><b>결과</b></p>
	<p>result: 마지막 업데이트 된 시간</p>
	
	<h3>상세 보기 - item/detail(GET)</h3>
	<p><b>파라미터</b></p>
	<p>itemid:정수 - 필수 
	<p><b>결과</b></p>
	<p>result:데이터 존재 여부(boolean) item(itemid:숫자, itemname:문자열, price:숫자, description:문자열, pictureurl:문자열, updatedate:문자열)</p>
	
	<h3>이미지 가져오기: img/이미지 파일명 - pictureurl</h3>
	
	<h3>ITEM 삽입 - item/insert(POST)</h3>
	<p><b>파라미터</b></p>
	<p>itemname:문자열, price:숫자, description:문자열, pictureurl:문자열(파일), updatedate:문자열 
	<p><b>결과</b></p>
	<p>result:삽입 성공 여부 error:에러 발생 이유</p>
	
	<h3>ITEM 수정 - item/update(POST)</h3>
	<p><b>파라미터</b></p>
	<p>itemid:정수 itemname:문자열, price:숫자, description:문자열, pictureurl:문자열(파일), updatedate:문자열 , oldpictureurl:문자열(예전 파일 이름)
	<p><b>결과</b></p>
	<p>result:수정 성공 여부 error:에러 발생 이유</p>
	
	<h3>ITEM 삭제 - item/delete(GET)</h3>
	<p><b>파라미터</b></p>
	<p>itemid:정수
	<p><b>결과</b></p>
	<p>result:삭제 성공 여부 error:에러 발생 이유</p>
	
	<h2>MEMBER</h2>
	<h3>회원가입 - member/join(POST)</h3>
	<p><b>파라미터</b></p>
	<p>email:문자열, nickname:숫자, pw:문자열, profile:문자열(파일)
	<p><b>결과</b></p>
	<p>result:가입 성공 여부 emailcheck:이메일 중복 검사 통과 여부 nicknamecheck:별명 중복 검사 통과 여부</p>
	
	<h3>nickname 확인 - member/nicknamelist(GET)</h3>
	<p><b>결과</b></p>
	<p>list:별명 목록</p>
	
	<h3>로그인 - member/login(POST)</h3>
	<p><b>파라미터</b></p>
	<p>email:문자열, pw:문자열
	<p><b>결과</b></p>
	<p>result:로그인 성공 여부 emailcheck:이메일 중복 검사 통과 여부 nicknamecheck:별명 중복 검사 통과 여부</p>
	
	<h3>회원정보수정 - member/update(POST)</h3>
	<p><b>파라미터</b></p>
	<p>email:문자열, nickname:숫자, pw:문자열, profile:문자열, regdate:문자열, oldprofile:문자열(파일)
	<p><b>결과</b></p>
	<p>update:수정 성공 여부</p>
	
	<h3>회원탈퇴 - member/secession(POST)</h3>
	<p><b>파라미터</b></p>
	<p>email:문자열
	<p><b>결과</b></p>
	<p>secession:탈퇴 성공 여부</p>
	
	<h2>Movie</h2>
	<h3>목록 보기 - movie/list(GET)</h3>
	<p><b>파라미터</b></p>
	<p>pageno(페이지번호 - 생략 가능), count(페이지 당 데이터 개수 - 기본값 3), searchtype(검색항목-genre, title, both), keyword(검색어)
	<br>모든 파라미터 생략 가능</p>
	<p><b>결과</b></p>
	<p>count: 데이터 개수, list: 데이터 목록(movieid:숫자, title:문자열, subtitle:문자열, pubdate:문자열, director:문자열, actor:문자열, genre:문자열, rating:실수, thumbnail:문자열, link:문자열)</p>
	
	<h3>상세 보기 - movie/detail(GET)</h3>
	<p><b>파라미터</b></p>
	<p>movieid:숫자
	<br>모든 파라미터 생략 가능</p>
	<p><b>결과</b></p>
	<p>count: 데이터 개수, list: 데이터 목록(movieid:숫자, title:문자열, subtitle:문자열, pubdate:문자열, director:문자열, actor:문자열, genre:문자열, rating:실수, thumbnail:문자열, link:문자열)</p>

	<h3>이미지파일 - movieimage/파일명</h3>
</body>
</html>