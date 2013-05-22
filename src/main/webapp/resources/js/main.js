$(document).ready(function(){
  $("#search_button").click(function(){
	getPage();
  });

  $("#search_type1").click(function(){
	  $("#search_type").html($("#search_type1").html());
	  $("#search_type_value").val("titleResultList");
  });
  $("#search_type2").click(function(){
	  $("#search_type").html($("#search_type2").html());
	  $("#search_type_value").val("authorResultList");
  });
  $("#search_type3").click(function(){
	  $("#search_type").html($("#search_type3").html());
	  $("#search_type_value").val("publisherResultList");
  });
  
  $(".pagination_page").each(function(){
	  $(this).click(function(){
		  getPageWithFrom($(this).attr("value"));
	  });
  });
});

function getPage() {
	url = $("#search_type_value").val();
	url += "?keyword=" + $(".search_keyword").val();
	url += "&count=" + $("#search_count").val();
	url += "&from=0&type=0";
	location.href = encodeURI(url);
}
function getPageWithFrom(from) {
	url = $("#search_type_value").val();
	url += "?keyword=" + $(".search_keyword").val();
	url += "&count=" + $("#search_count").val();
	url += "&from=" + from;
	url += "&type=0";
	location.href = encodeURI(url);
}