<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<div id="statics">
	</div>
	<div id="result_list">
		<c:forEach items="${resultList}" var="item" varStatus="status">
		<div class="result_item">
			<img class="result_item_image" alt="" src="${item.imageSrc }">
			<div class="result_item_title">${item.title }</div>
			<div class="result_item_author">${item.author }</div>
			<div class="result_item_author">${item.publisher }</div>
			<div class="result_item_author">${item.date }</div>
			<div class="result_item_author">${item.price }</div>
			<div class="result_item_star_${item.star }"></div>
		</div>
		</c:forEach>
	</div>
	<div id="page">
	</div>