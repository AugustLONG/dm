<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="includes/header.jsp"%>

<div class="row">
<div id="content_left" class="span6 offset1">
	<div class="book_recommend row">
		<h4 class="book_recommend_title">
			相关图书推荐
		</h4>
		<c:forEach var="i" begin="0" end="2">
		<div class="row book_line">
			<c:forEach items="${bookRecommendList}" var="item" varStatus="status" begin="${i*2 }" end="${i*2+1 }">
			<div class="book_recommend_item span3">
				<div class="row">
					<a href="/bookInfo?id=${item.id}">
						<img class="book_recommend_item_image span1" alt="" src="${item.imageSrc }">
					</a>
					<dl class="span2">
						<dt class="book_recommend_item_title">
							<a href="/bookInfo?id=${item.id}">${item.title }</a>
						</dt>
						<dd class="book_recommend_item_author">${item.author }</dd>
						<dd class="book_recommend_item_star_${item.star }"></dd>
					</dl>
				</div>
			</div>
			</c:forEach>
		</div>
		</c:forEach>
	</div>
	
	<div class="book_recommend_classify row">
		<h4 class="book_recommend_classify_title">
			分类热门图书
		</h4>
		<c:forEach var="i" begin="0" end="6">
		<div class="row book_line">
			<c:forEach items="${bookRecommendClassifyList}" var="item" varStatus="status" begin="${i*2 }" end="${i*2+1 }">
			<div class="book_recommend_classify_item span3">
				<img class="book_recommend_classify_item_image" alt="" src="${item.imageSrc}">
				<h5 class="book_recommend_classify_item_name">${item.tagName}</h5>
				<ul>
				<c:forEach items="${item.titleList }" var="title" varStatus="s">
				<li class="book_recommend_classify_item_title1">
					<a href="/bookInfo?id=${title.id}">${title.title }</a>
				</li>
				</c:forEach>
				</ul>
			</div>
			</c:forEach>
		</div>
		</c:forEach>
	</div>
</div>
<div id="content_right" class="span3 offset1">
	<div class="tag_list row">
		<h4 class="tag_list_title">
			图书分类
		</h4>
		<div class="tag_list_items">
			<div class="tag_list_item">
				<div class="tag_list_item_row1 row-fluid">
					<a class="tag_list_item_cell span2" href="#">[文学]</a>
					<a class="tag_list_item_cell span2" href="#">小说</a>
					<a class="tag_list_item_cell span2" href="#">随笔</a>
					<a class="tag_list_item_cell span2" href="#">散文</a>
					<a class="tag_list_item_cell span3" href="#">日本文学</a>
				</div>
				<div class="tag_list_item_row2 row-fluid">
					<a class="tag_list_item_cell span2" href="#">童话</a>
					<a class="tag_list_item_cell span2" href="#">诗歌</a>
					<a class="tag_list_item_cell span2" href="#">名著</a>
					<a class="tag_list_item_cell span2" href="#">港台</a>
					<a class="tag_list_item_cell span3" href="#">(更多)</a>
				</div>
			</div>
			<div class="tag_list_item">
				<div class="tag_list_item_row1 row-fluid">
					<a class="tag_list_item_cell span2" href="#">[流行]</a>
					<a class="tag_list_item_cell span2" href="#">漫画</a>
					<a class="tag_list_item_cell span2" href="#">绘本</a>
					<a class="tag_list_item_cell span2" href="#">推理</a>
					<a class="tag_list_item_cell span3" href="#">青春</a>
				</div>
				<div class="tag_list_item_row2 row-fluid">
					<a class="tag_list_item_cell span2" href="#">言情</a>
					<a class="tag_list_item_cell span2" href="#">科幻</a>
					<a class="tag_list_item_cell span2" href="#">武侠</a>
					<a class="tag_list_item_cell span2" href="#">奇幻</a>
					<a class="tag_list_item_cell span3" href="#">(更多)</a>
				</div>
			</div>
			<div class="tag_list_item">
				<div class="tag_list_item_row1 row-fluid">
					<a class="tag_list_item_cell span2" href="#">[文化]</a>
					<a class="tag_list_item_cell span2" href="#">历史</a>
					<a class="tag_list_item_cell span2" href="#">哲学</a>
					<a class="tag_list_item_cell span2" href="#">传记</a>
					<a class="tag_list_item_cell span3" href="#">设计</a>
				</div>
				<div class="tag_list_item_row2 row-fluid">
					<a class="tag_list_item_cell span2" href="#">建筑</a>
					<a class="tag_list_item_cell span2" href="#">电影</a>
					<a class="tag_list_item_cell span2" href="#">回忆录</a>
					<a class="tag_list_item_cell span2" href="#">音乐</a>
					<a class="tag_list_item_cell span3" href="#">(更多)</a>
				</div>
			</div>
			<div class="tag_list_item">
				<div class="tag_list_item_row1 row-fluid">
					<a class="tag_list_item_cell span2" href="#">[生活]</a>
					<a class="tag_list_item_cell span2" href="#">旅行</a>
					<a class="tag_list_item_cell span2" href="#">励志</a>
					<a class="tag_list_item_cell span2" href="#">职场</a>
					<a class="tag_list_item_cell span3" href="#">美食</a>
				</div>
				<div class="tag_list_item_row2 row-fluid">
					<a class="tag_list_item_cell span2" href="#">教育</a>
					<a class="tag_list_item_cell span2" href="#">灵修</a>
					<a class="tag_list_item_cell span2" href="#">健康</a>
					<a class="tag_list_item_cell span2" href="#">家居</a>
					<a class="tag_list_item_cell span3" href="#">(更多)</a>
				</div>
			</div>
			<div class="tag_list_item">
				<div class="tag_list_item_row1 row-fluid">
					<a class="tag_list_item_cell span2" href="#">[经管]</a>
					<a class="tag_list_item_cell span2" href="#">经济学</a>
					<a class="tag_list_item_cell span2" href="#">管理</a>
					<a class="tag_list_item_cell span2" href="#">金融</a>
					<a class="tag_list_item_cell span3" href="#">商业</a>
				</div>
				<div class="tag_list_item_row2 row-fluid">
					<a class="tag_list_item_cell span2" href="#">营销</a>
					<a class="tag_list_item_cell span2" href="#">理财</a>
					<a class="tag_list_item_cell span2" href="#">股票</a>
					<a class="tag_list_item_cell span2" href="#">企业史</a>
					<a class="tag_list_item_cell span3" href="#">(更多)</a>
				</div>
			</div>
			<div class="tag_list_item">
				<div class="tag_list_item_row1 row-fluid">
					<a class="tag_list_item_cell span2" href="#">[科技]</a>
					<a class="tag_list_item_cell span2" href="#">科普</a>
					<a class="tag_list_item_cell span2" href="#">互联网</a>
					<a class="tag_list_item_cell span2" href="#">编程</a>
					<a class="tag_list_item_cell span3" href="#">交互设计</a>
				</div>
				<div class="tag_list_item_row2 row-fluid">
					<a class="tag_list_item_cell span2" href="#">算法</a>
					<a class="tag_list_item_cell span2" href="#">通信</a>
					<a class="tag_list_item_cell span3" href="#">神经网络</a>
					<a class="tag_list_item_cell span2" href="#">(更多)</a>
				</div>
			</div>
		</div>
	</div>
	<div class="book_rank row">
		<h4 class="book_rank_title">
			图书排行
		</h4>
		<c:forEach var="i" begin="0" end="10">
		<div class="row-fluid">
		<c:forEach items="${bookRankList}" var="item" varStatus="status" begin="${i*2 }" end="${i*2+1 }">
		<div class="book_rank_item span6">
			<div class="text-center">
				<a href="/bookInfo?id=${item.id }">
					<img class="book_rank_item_image" alt="" src="${item.imageSrc }">
				</a>
			</div>
			<div class="book_rank_item_name text-center">
				<a href="/bookInfo?id=${item.id }">${item.title}</a>
			</div>
		</div>
		</c:forEach>
		</div>
		</c:forEach>
	</div>
</div>
</div>

<%@ include file="includes/footer.jsp"%>