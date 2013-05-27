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
		<c:if test="${pagination.totalCount != 0 }">
		<%@ include file="includes/pagination.jsp" %>
		</c:if>
	</div>
</div>

<div id="content_right" class="span3 offset1">
	<h4 class="book_recommend_title">
		图书排行
	</h4>
	<div class="book_recommend">
		<c:forEach items="${bookRankList}" var="item" varStatus="status">
		<div class="row book_recommend_row">
			<a class="span1" href="/bookInfo?id=${item.id }">
				<img class="book_recommend_item_image" alt="" src="${item.imageSrc }">
			</a>
			<dl class="span2">
				<dt class="book_recommend_item_title">
					<a class="span1" href="/bookInfo?id=${item.id }">${item.title }</a>
				</dt>
				<dd class="book_recommend_item_author">${item.author }</dd>
				<dd class="book_recommend_item_star_${item.star }"></dd>
			</dl>
		</div>
		</c:forEach>
	</div>
</div>
</div>
<%@ include file="includes/footer.jsp"%>