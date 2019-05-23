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
				<form id="search_form" action="${pageContext.servletContext.contextPath}/board/list" method="get">
					<input type="text" id="kwd" name="kwd" value="${bpv.kwd_decode}" />
					<input type="submit" value="찾기" />
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
					<c:if test="${data.status ne 1}">
					<tr>
						<td>${pagingMap.count - pagingMap.startNum - cnt}</td>
						<td style="text-align:left;padding-left:${20*data.depth}px;">
							삭제된 게시글 입니다.
						</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
						<td>-</td>
					</tr>
					</c:if>
					<c:if test="${data.status eq 1}">
					<tr>
						<td>${pagingMap.count - pagingMap.startNum - cnt}</td>
						<td style="text-align:left;padding-left:${20*data.depth}px;">
							<c:if test="${data.depth ne 0}">
								<img src="${pageContext.servletContext.contextPath}/assets/images/reply.png" alt="답글" />
							</c:if>
							<a href="${pageContext.servletContext.contextPath}/board/view?no=${data.no}&pages=${bpv.pages}&kwd=${bpv.kwd_encode}">
								${data.title}
								<c:if test="${data.commentCount ne 0}">
									<span style="color:red;font-size:11px;font-weight:normal;">(${data.commentCount})</span>
								</c:if>
							</a>
						</td>
						<td>${data.userName}</td>
						<td>${data.hit}</td>
						<td>${data.regDate}</td>
						<td>
							<c:if test="${authUser ne null}">
							<c:if test="${authUser.no eq data.userNo}">
								<a href="${pageContext.servletContext.contextPath}/board/delete?no=${data.no}&pages=${bpv.pages}&kwd=${bpv.kwd_encode}" class="del">삭제</a>
							</c:if>
							</c:if>
						</td>
					</tr>
					</c:if>
					<c:set var="cnt" scope="page" value="${cnt+1}"></c:set>
					</c:forEach>

				</table>
				
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<!-- 1보다 작으면 버튼 비활성화 -->
						<c:if test="${(pagingMap.rangeStart - 1) lt 1}">
							<li>◀</li>
						</c:if>
						<!-- 1보다 같거나 크면 버튼 활성화-->
						<c:if test="${(pagingMap.rangeStart - 1) ge 1}">
							<li><a href="${pageContext.servletContext.contextPath}/board/list?pages=${pagingMap.rangeStart - 1}&kwd=${bpv.kwd_encode}">◀</a></li>
						</c:if>
						<c:forEach begin="${pagingMap.rangeStart}" end="${pagingMap.rangeStart + pagingMap.pageCnt - 1}" var="i">
							<!-- 최대 페이지를 넘어가면 비활성화 -->
							<c:if test="${i gt pagingMap.lastPage}">
								<li>${i}</li>
							</c:if>
							<c:if test="${i le pagingMap.lastPage}">
								<c:if test="${i eq bpv.pages}">
									<li class="selected">${i}</li>
								</c:if>
								<c:if test="${i ne bpv.pages}">
									<li><a href="${pageContext.servletContext.contextPath}/board/list?pages=${i}&kwd=${bpv.kwd_encode}">${i}</a></li>
								</c:if>
							</c:if>
						</c:forEach>
						<!-- 최대 페이지보다 작거나 같으면 버튼활성화 -->
						<c:if test="${(pagingMap.rangeStart + pagingMap.pageCnt) le pagingMap.lastPage}">
							<li><a href="${pageContext.servletContext.contextPath}/board/list?pages=${pagingMap.rangeStart + pagingMap.pageCnt}&kwd=${bpv.kwd_encode}">▶</a></li>
						</c:if>
						<!-- 최대 페이지보다 크면 버튼 비활성화 -->
						<c:if test="${(pagingMap.rangeStart + pagingMap.pageCnt) gt pagingMap.lastPage}">
							<li>▶</li>
						</c:if>
							
					</ul>
				</div>					
				<!-- pager 추가 -->
				
				
				
				<div class="bottom">
					<c:if test="${authUser ne null}">
						<a href="${pageContext.servletContext.contextPath}/board/write?pages=${bpv.pages}&kwd=${bpv.kwd_encode}" id="new-book">글쓰기</a>
					</c:if>
				</div>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div>
</body>
</html>