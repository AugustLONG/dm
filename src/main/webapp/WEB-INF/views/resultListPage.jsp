<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="includes/header.jsp"%>

<div class="row">
<div id="content_left" class="span6 offset1">
	<div id="statics">
		<c:if test="${pagination.totalCount == 0 }">
		<div class="alert">
			没有找到任何与"${pagination.keyword }"相关的图书
		</div>
		</c:if>
		<c:if test="${pagination.totalCount != 0 }">
			显示： <span>${pagination.from+1 }</span>-<span>${pagination.from+pagination.listCount }</span>条， 共<span>${pagination.totalCount }</span>条
		</c:if>
	</div>
	<div id="result_list">
		<%@ include file="resultList.jsp"%>
	</div>
	<div class="page text-center">
		<%@ include file="includes/pagination.jsp" %>
	</div>
</div>

<div id="content_right" class="span3 offset1">
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
</div>
<%@ include file="includes/footer.jsp"%>