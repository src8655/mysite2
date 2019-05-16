<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath}/assets/css/guestbook.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<div id="content">
			<div id="guestbook">
				<form action="${pageContext.servletContext.contextPath}/guestbook/list" method="post">
					<table>
						<tr>
							<td>이름</td><td><input type="text" name="name"></td>
							<td>비밀번호</td><td><input type="password" name="password"></td>
						</tr>
						<tr>
							<td colspan=4><textarea name="contents" id="content"></textarea></td>
						</tr>
						<tr>
							<td colspan=4 align=right><input type="submit" VALUE=" 확인 "></td>
						</tr>
					</table>
				</form>
				<ul>
				<c:forEach items="${list}" var="data">
					<li>
						<table>
							<tr>
								<td>${cnt}</td>
								<td>${data.name}</td>
								<td>${data.regDate}</td>
								<td><a href="${pageContext.servletContext.contextPath}/guestbook/deleteform?no=${data.no}">삭제</a></td>
							</tr>
							<tr>
								<td colspan=4>
								${data.contents}
								</td>
							</tr>
						</table>
						<br>
					</li>
					<c:set var="cnt" scope="page" value="${cnt-1}"></c:set>
					</c:forEach>
				</ul>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div>
</body>
</html>