<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/jquery/jquery-3.4.1.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/ejs/ejs.js"></script>
<script>	
	
	var listItemTemplate = new EJS({
		url: "${pageContext.request.contextPath }/assets/js/ejs/list-item-template.ejs"
	});
	var listTemplate = new EJS({
		url: "${pageContext.request.contextPath }/assets/js/ejs/list-template.ejs"
	});
	
// 	var render = function(vo, mode) {
// // 		var postCountList = '${postCountList}';

// 		var html = 
// 			"<tr><td>" + (count++) + "</td>" +
// 			"<td>" + vo.name + "</td>" +
// 			"<td>" + vo.postCount + "</td>" +
// 			"<td>" + vo.description + "</td></tr>";
			
// 			$("#menu-title").after(html);
// 	}

	var list = function() {
		count = 1;
		$.ajax({
			url: '${pageContext.request.contextPath }/${authUser.id}/admin/api/category/list',
			async: true,
			type: 'get',
			dataType: 'json',
			data: '',
			success: function(response) {
				if(response.result != "success") {
					console.error(response.message);
					return;
				}
				response.contextPath = "${ pageContext.request.contextPath }";
				var html = listTemplate.render(response);
				$(".admin-cat").append(html);
			},
			error: function(xhr, status, e) {
				console.error(status + ":" + e);
			}
		});
	}
	
	$(function() {
		$("#add-form").submit(function(event) {
			event.preventDefault();
			
			var vo = {};
			vo.name = $("#input-name").val();
			vo.description = $('#input-description').val();
			vo.id = "${authUser.id}";
			
			$.ajax({
				url: '${ pageContext.request.contextPath }/${authUser.id}/admin/api/category/insert',
				async: true,
				type: 'post',
				dataType: 'json',
				contentType: 'application/json',
				data: JSON.stringify(vo),
				success: function(response) {
					if(response.result != "success") {
						console.error(response.message);
						return;
					}
					response.data.listCount = $('.admin-cat ').find('tr').length;
					response.data.contextPath = "${ pageContext.request.contextPath }";
					var html = listItemTemplate.render(response.data);
					$(".admin-cat").append(html);
					
					// form reset
					$("#add-form")[0].reset();
				},
				error: function(xhr, status, e) {
					console.error(status + ":" + e);
				}
			});
		
		});
		
		$(document).on('click','.admin-cat tr td a',function(event){
			event.preventDefault();
			var no = $(this).data('no');

			$.ajax({
				url: '${pageContext.request.contextPath }/${authUser.id}/admin/api/category/delete/' + no,
				async: true,
				type: 'delete',
				dataType: 'json',
				data: '',
				success: function(response) {
					 if(response.data != -1){
				            console.log(response.data);
				             $(".admin-cat tr[data-no=" + response.data + "]").remove(); 
				            
				            return;
				         }

					
				},
				error: function(xhr, status, e) {
					console.error(status + ":" + e);
				}
			});
		});
		
		$("#delete-button").click(function() {
			var no = $(this).no;
			$.ajax({
				url: '${pageContext.request.contextPath }/${authUser.id}/admin/api/category/delete' + no,
				async: true,
				type: 'delete',
				dataType: 'json',
				success: function(response) {
					if(response.result != "success") {
						console.error(response.message);
						return;
					}
				},
				error: function(xhr, status, e) {
					console.error(status + ":" + e);
				}
			});
		});
		
		list();
		
	});		
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/include/blog-header.jsp" />
		<div id="wrapper">
			<div id="content" class="full-screen">
				<ul class="admin-menu">
					<li><a href="${pageContext.request.contextPath }/${authUser.id}/admin/basic">기본설정</a></li>
					<li><a href="${pageContext.request.contextPath }/${authUser.id}/admin/category">카테고리</a></li>
					<li class="selected">api 카테고리</li>
					<li><a href="${pageContext.request.contextPath }/${authUser.id}/admin/write">글작성</a></li>
				</ul>
		      	<table class="admin-cat">
		      		<tr id = 'menu-title'>
		      			<td>번호</td>
		      			<td>카테고리명</td>
		      			<td>포스트 수</td>
		      			<td>설명</td>
		      			<td>삭제</td>      			
		      		</tr>
				</table>
      	
               <h4 class="n-c">새로운 카테고리 추가</h4>
               <form action="" id="add-form"  method="post">
                  <table id="admin-cat-add">
                     <tr>
                        <td class="t">카테고리명</td>
                        <td><input type="text" name="name" id="input-name"></td>
                     </tr>
                     <tr>
                        <td class="t">설명</td>
                        <td><input type="text" name="description" id="input-description"></td>
                     </tr>
                     <tr>
                        <td class="s">&nbsp;</td>
                        <td><input type="submit" value="카테고리 추가"></td>
                     </tr>                        
                  </table> 
               </form>
			</div>
		</div>
		<div id="footer">
			<p>
				<strong>Spring 이야기</strong> is powered by JBlog (c)2016
			</p>
		</div>
	</div>
</body>
</html>