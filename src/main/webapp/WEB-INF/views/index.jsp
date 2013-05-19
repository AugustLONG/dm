<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="includes/header.jsp"%>

<div id="content_left">
	<div class="book_recommend">
		<div class="book_recommend_title">
			相关图书推荐
		</div>
		<c:forEach items="${bookRecommendList}" var="item" varStatus="status">
		<div class="book_recommend_item">
			<img class="book_recommend_item_image" alt="" src="${item.imageSrc }">
			<div class="book_recommend_item_title">${item.title }</div>
			<div class="book_recommend_item_author">${item.author }</div>
			<div class="book_recommend_item_binding">${item.binding }</div>
			<div class="book_recommend_item_star_${item.star }"></div>
		</div>
		</c:forEach>
	</div>
	
	<div class="book_recommend_classify">
		<div class="book_recommend_classify_title">
			分类热门图书
		</div>
		<c:forEach items="${bookRecommendClassifyList}" var="item" varStatus="status">
		<div class="book_recommend_classify_item">
			<img class="book_recommend_classify_item_image" alt="" src="${item.imageSrc}">
			<div class="book_recommend_classify_item_name">${item.tagName}</div>
			<div class="book_recommend_classify_item_title1">${item.title1 }</div>
			<div class="book_recommend_classify_item_title2">${item.title2 }</div>
		</div>
		</c:forEach>
	</div>
</div>
<div id="content_right">
	<div class="tag_list">
		<div class="tag_list_title">
			图书分类
		</div>
		<div class="tag_list_row">
			<a href="#"></a>
		</div>
		<c:forEach items="${bookClassifyList}" var="item" varStatus="status">
		<div class="book_classify_item">
			<img class="book_classify_item_image" alt="" src="${item.imageSrc }">
			<div class="book_classify_item_name">${item.name}</div>
			<div class="book_classify_item_title1">${item.title1 }</div>
			<div class="book_classify_item_title2">${item.title2 }</div>
		</div>
		</c:forEach>
	</div>
	<div class="book_rank">
		<div class="book_rank_title">
			图书排行
		</div>
		<c:forEach items="${bookRankList}" var="item" varStatus="status">
		<div class="book_rank_item">
			<img class="book_rank_item_image" alt="" src="${item.imageSrc }">
			<div class="book_rank_item_name">${item.name}</div>
		</div>
		</c:forEach>
	</div>
</div>

<%@ include file="includes/footer.jsp"%>