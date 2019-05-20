<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath}/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<div id="content">
			<div id="board">
				<form id="search_form" action="" method="get">
					<input type="text" id="kwd" name="kwd" value="${kwd_decode}">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>				
					<c:forEach items="${list}" var="data">
					<tr>
						<td>${cnt}</td>
						<td style="text-align:left;padding-left:${20*data.depth}px;">
							<c:if test="${data.depth ne 0}">
								<img src="${pageContext.servletContext.contextPath}/assets/images/reply.png" alt="답글" />
							</c:if>
							<a href="${pageContext.servletContext.contextPath}/board/view?no=${data.no}">${data.title}</a>
						</td>
						<td>${data.userName}</td>
						<td>${data.hit}</td>
						<td>${data.regDate}</td>
						<td><a href="${pageContext.servletContext.contextPath}/board/delete?no=${data.no}" class="del">삭제</a></td>
					</tr>
					<c:set var="cnt" scope="page" value="${cnt-1}"></c:set>
					</c:forEach>

				</table>
				
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<li><a href="">◀</a></li>
						<li><a href="">1</a></li>
						<li class="selected">2</li>
						<li><a href="">3</a></li>
						<li>4</li>
						<li>5</li>
						<li><a href="">▶</a></li>
					</ul>
				</div>					
				<!-- pager 추가 -->
				
				
				
				<div class="bottom">
					<a href="${pageContext.servletContext.contextPath}/board/write" id="new-book">글쓰기</a>
				</div>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div>
</body>
</html>