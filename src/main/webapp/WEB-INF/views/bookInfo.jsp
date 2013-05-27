<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="includes/header.jsp"%>

<div class="row">
	<div class="span8 offset2">
		<h3>${publishing.title }</h3>
		<div class="row">
			<div class="span2">
				<img alt="" src="${imageSrc }${publishing.title_page_images }">
			</div>
			<ul class="span4 unstyled">
				<li>
					<dl class="dl-horizontal">
						<dt>作者：</dt>
						<dd>${publishing.auther_name }</dd>
						<dt>出版社：</dt>
						<dd>${publishing.publisher }</dd>
						<dt>出版年：</dt>
						<dd>${publishing.publication_date }</dd>
						<dt>页数：</dt>
						<dd>${publishing.page }</dd>
						<dt>定价：</dt>
						<dd>${publishing.list_price }</dd>
						<dt>装帧：</dt>
						<dd>${publishing.binding }</dd>
						<dt>ISBN：</dt>
						<dd>${publishing.isbn }</dd>
					</dl>
				</li>
			</ul>
			<div class="span2">
				
			</div>
		</div>
		<div>
			<h4>内容简介</h4>
			<div>
				${publishing.directory }
			</div>
		</div>
		<div>
			<h4>作者简介</h4>
			<div>
				${author.introduction }
			</div>
		</div>
	</div>
</div>

<%@ include file="includes/footer.jsp"%>