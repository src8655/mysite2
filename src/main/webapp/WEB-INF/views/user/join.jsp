<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath}/assets/css/user.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/assets/js/jquery/jquery-1.9.0.js"></script>
<script type="text/javascript">
	$(function(){
		$("#email").change(function(){
			$("#check-button").show();
			$("#check-image").hide();
		});
		
		$("#check-button").click(function(){
			var email = $("#email").val();
			if(email == "") {
				return;
			}
			
			/* ajax 통신 */
			$.ajax({
				url: "${pageContext.servletContext.contextPath}/user/api/checkemail?email="+email,
				type: "get",
				dataType: "json",
				data: "",
				success: function(response){
					if(response.result != "success") {
						console.log(response);
						return;
					}

					if(response.data == true) {
						alert("이미 존재하는 이메일입니다.\n다른 이메일을 사용해 주세요.");
						$("#email").val("");
						return;
					}
					$("#check-button").hide();
					$("#check-image").show();
					
				},
				error: function(xhr, error){
					console.error("error:" + error);
				}
			});
		});
	});
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<div id="content">
			<div id="user">

				<form:form
				modelAttribute="userVo"
				id="join-form" name="joinForm" method="post" action="${pageContext.servletContext.contextPath}/user/join">
				
					<label class="block-label" for="name">이름</label>
					<input id="name" name="name" type="text" value="" />
					
					<spring:hasBindErrors name="userVo">
						<c:if test="${errors.hasFieldErrors('name')}">
							<p style="text-align:left;font-weight:bold;color:red;padding:0px;">
								<spring:message
								code="${errors.getFieldError('name').codes[0]}"
								text="${errors.getFieldError('name').defaultMessage}"
								></spring:message>
							</p>
						</c:if>
					</spring:hasBindErrors>

					<label class="block-label" for="email">이메일</label>
					<form:input path="email"/>
					<input type="button" id="check-button" value="체크" />
					<img id="check-image" style="display:none;" src="${pageContext.servletContext.contextPath}/assets/images/check.png" alt="체크" />
					
					<p style="text-align:left;font-weight:bold;color:red;padding:0px;">
						<form:errors path="email"></form:errors>
					</p>
					
					<label class="block-label">패스워드</label>
					<input name="password" type="password" value="" />
					
					<fieldset>
						<legend>성별</legend>
						<label>여</label> <input type="radio" name="gender" value="female" checked="checked" />
						<label>남</label> <input type="radio" name="gender" value="male" />
					</fieldset>
					
					<fieldset>
						<legend>약관동의</legend>
						<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
						<label>서비스 약관에 동의합니다.</label>
					</fieldset>
					
					<input type="submit" value="가입하기">
					
				</form:form>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div>
</body>
</html>