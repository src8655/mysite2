<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath}/assets/css/board.css" rel="stylesheet" type="text/css">
<script type="text/javascript">

function showhide(obj) {
	if(document.getElementById(obj).style.display == "none")
		document.getElementById(obj).style.display = "";
	else
		document.getElementById(obj).style.display = "none";
}

</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<div id="content">
			<div id="board" class="board-form">
				<table class="tbl-ex">
					<tr>
						<th colspan="6">글보기</th>
					</tr>
					<tr>
						<td class="label">제목</td>
						<td colspan="5">${boardVo.title}</td>
					</tr>
					<tr>
						<td class="label">작성자</td>
						<td>${boardVo.userName}</td>
						<td class="label">작성일</td>
						<td>${boardVo.regDate}</td>
						<td class="label">조회수</td>
						<td>${boardVo.hit}</td>
					</tr>
					<tr>
						<td class="label">내용</td>
						<td colspan="5">
							<div class="view-content">
								${boardVo.contents}
							</div>
						</td>
					</tr>
				</table>
				

				<c:forEach items="${commentList}" var="commentData">
				<table class="tbl-ex" style="border-bottom:0px;">
				<col />
				<col width="100" />
				<col />
				<col width="80" />
					<tr>
						<td style="width:${commentData.depth * 20}px;padding:0px;border-top:0px;border-bottom:0px;text-align:right;">
							<c:if test="${commentData.depth ne 0}">
								<img src="${pageContext.servletContext.contextPath}/assets/images/reply.png" alt="답글" />
							</c:if>
						</td>
						<td style="font-weight:bold;text-align:center;color:#000000;background:#eeeeee;">
							${commentData.userName}<br />
							${commentData.regDate}
						</td>
						<td>${commentData.contents}</td>
						<td style="text-align:center;">
							<c:if test="${authUser ne null}">
								<c:if test="${authUser.no eq commentData.userNo}">
									<a href="${pageContext.servletContext.contextPath}/board/comment/delete?no=${commentData.no}&boardNo=${boardVo.no}&pages=${pages}&kwd=${kwd_encode}">[삭제]</a>
									<br />
								</c:if>
								<a href="#100" onclick="showhide('commentRe_${commentData.no}')">[답글]</a>
							</c:if>
						</td>
					</tr>
					<c:if test="${authUser ne null}">
					<tr style="display:none;" id="commentRe_${commentData.no}">
						<form action="${pageContext.servletContext.contextPath}/board/comment/write" method="post">
						<input type="hidden" name="groupNo" value="${commentData.groupNo}" />
						<input type="hidden" name="orderNo" value="${commentData.orderNo}" />
						<input type="hidden" name="depth" value="${commentData.depth}" />
						<input type="hidden" name="no" value="${commentData.no}" />
						
						<input type="hidden" name="boardNo" value="${boardVo.no}" />
						<input type="hidden" name="pages" value="${pages}" />
						<input type="hidden" name="kwd" value="${kwd_decode}" />
						
						<td style="padding:0px;border-top:0px;border-bottom:0px;"></td>
						<td colspan="2"><textarea name="contents" style="width:98%;height:45px;"></textarea></td>
						<td><input type="submit" value="답글작성" /></td>
						
						</form>
					</tr>
					</c:if>
				</table>
				</c:forEach>
				
				<c:if test="${authUser ne null}">
				<table class="tbl-ex">
				<col width="100" />
				<col />
				<col width="80" />
					<form action="${pageContext.servletContext.contextPath}/board/comment/write" method="post">
					<input type="hidden" name="groupNo" value="-1" />
					<input type="hidden" name="orderNo" value="-1" />
					<input type="hidden" name="depth" value="-1" />
					
					<input type="hidden" name="boardNo" value="${boardVo.no}" />
					<input type="hidden" name="pages" value="${pages}" />
					<input type="hidden" name="kwd" value="${kwd_decode}" />
					<tr>
						<td colspan="2"><textarea name="contents" style="width:98%;height:45px;"></textarea></td>
						<td><input type="submit" value="댓글작성" /></td>
					</tr>
					</form>
				</table>
				</c:if>
				
				<div class="bottom">
					<a href="${pageContext.servletContext.contextPath}/board/list?pages=${pages}&kwd=${kwd_encode}">글목록</a>
					<c:if test="${authUser ne null}">
						<c:if test="${authUser.no eq boardVo.userNo}">
							<a href="${pageContext.servletContext.contextPath}/board/modify?no=${boardVo.no}&pages=${pages}&kwd=${kwd_encode}">글수정</a>
						</c:if>
						<a href="${pageContext.servletContext.contextPath}/board/write?no=${boardVo.no}&groupNo=${boardVo.groupNo}&orderNo=${boardVo.orderNo}&depth=${boardVo.depth}&pages=${pages}&kwd=${kwd_encode}">답글</a>
					</c:if>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div>
</body>
</html>