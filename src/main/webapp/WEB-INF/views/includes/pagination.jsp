<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
	<div class="pagination">
		<ul>
			<li <c:if test="${pagination.isFirst }">class="disabled"</c:if>>
				<a href="#"
					<c:if test="${!pagination.isFirst }">class="pagination_page" value="${pagination.prevPage }"</c:if>
				>&laquo;</a>
			</li>
			<c:forEach items="${pagination.pageList }" var="i" varStatus="s">
			<li <c:if test="${pagination.currentPage==i.id }">class="active"</c:if>>
				<c:if test="${i.id==0 }">
				<span>...</span>
				</c:if>
				<c:if test="${i.id!=0 }">
				<a class="pagination_page" href="#" value="${i.from }">${i.id }</a>
				</c:if>
			</li>
			</c:forEach>
			<li <c:if test="${pagination.isLast }">class="disabled"</c:if>>
				<a href="#"
					<c:if test="${!pagination.isLast }">class="pagination_page" value="${pagination.nextPage }"</c:if>
				>&raquo;</a>
			</li>
		</ul>
	</div>