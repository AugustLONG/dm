<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="includes/header.jsp"%>

<div id="content_left">
	<%@ include file="resultList.jsp"%>
</div>

<div id="content_right">
	<div id="search_history_title">
	</div>
	<div id="search_history_list">
		<c:forEach items="${searchHistoryList }" var="item" varStatus="status">
		<div class="search_history_item">
			<div class="search_history_item_keyword">
			</div>
			<div class="search_history_item_result_list">
				<c:forEach items="${item.resultList}" var="result" varStatus="resultStatus">
				<div class="book_recommend_item">
					<img class="book_recommend_item_image" alt="" src="${item.imageSrc }">
					<div class="book_recommend_item_title">${item.title }</div>
					<div class="book_recommend_item_author">${item.author }</div>
					<div class="book_recommend_item_star_${item.star }"></div>
				</div>
				</c:forEach>
			</div>
		</div>
		</c:forEach>
	</div>
</div>

<%@ include file="includes/footer.jsp"%>