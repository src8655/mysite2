<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath}/assets/css/board.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
function board_write_check(var1) {
	var forms = document.getElementById(var1);

	if(forms.title.value == "") {
		alert("제목을 입력해 주세요.");
		return;
	}
	if(forms.contents.value == "") {
		alert("내용을 입력해 주세요.");
		return;
	}
	
	forms.submit();
}
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<div id="content">
			<div id="board">
				<form class="board-form" method="post" action="${pageContext.servletContext.contextPath}/board/write" enctype="multipart/form-data" id="board_write">
					<input type="hidden" name="groupNo" value="${bpv.groupNo}" />
					<input type="hidden" name="orderNo" value="${bpv.orderNo}" />
					<input type="hidden" name="depth" value="${bpv.depth}" />
					<input type="hidden" name="no" value="${bpv.no}" />
					<input type="hidden" name="pages" value="${bpv.pages}" />
					<input type="hidden" name="kwd" value="${bpv.kwd_decode}" />
					<table class="tbl-ex">
						<tr>
							<th colspan="2">글쓰기</th>
						</tr>
						<tr>
							<td class="label">제목</td>
							<td><input type="text" name="title" value=""></td>
						</tr>
						<tr>
							<td class="label">내용</td>
							<td>
								<textarea id="content" name="contents"></textarea>
							</td>
						</tr>
						<tr>
							<td class="label">첨부파일</td>
							<td><input type="file" name="files1"></td>
						</tr>
					</table>
					<div class="bottom">
						<c:if test="${bpv.no eq -1}"><a href="${pageContext.servletContext.contextPath}/board/list?pages=${bpv.pages}&kwd=${bpv.kwd_encode}">취소</a></c:if>
						<c:if test="${bpv.no ne -1}"><a href="${pageContext.servletContext.contextPath}/board/view?no=${bpv.no}&pages=${bpv.pages}&kwd=${bpv.kwd_encode}">취소</a></c:if>
						<a href="#100" onclick="board_write_check('board_write');">등록</a>
					</div>
				</form>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div>
</body>
</html>