<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <title>图书推荐系统</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/main.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      #search-area {
      	margin:0 auto 20px;
      	width:689px;
      }
    </style>
    <link href="/resources/css/bootstrap-responsive.min.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="resources/js/html5shiv.js"></script>
    <![endif]-->
    
  </head>
  <body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="/">图书推荐系统</a>
          <div class="nav-collapse collapse">
            <form class="navbar-form pull-right">
              <input class="span2" type="text" placeholder="Email">
              <input class="span2" type="password" placeholder="Password">
              <input class="checkbox" type="checkbox">Remember me
              <button type="submit" class="btn">登录</button>
              <button type="button" class="btn">注册</button>
            </form>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
    <div class="container">
      <div id="search-area">
      	<input type="hidden" id="search_type_value" value=
      		<c:if test="${searchType==null }">"titleResultList"</c:if>
      		<c:if test="${searchType!=null }">"${searchType }"</c:if>>
      	<input type="hidden" id="search_count" value=
      		<c:if test="${pagination.count==null }">"20"</c:if>
      		<c:if test="${pagination.count!=null }">"${pagination.count }"</c:if>>
        <div class="input-append input-prepend">
          <div class="btn-group">
            <button class="btn dropdown-toggle" data-toggle="dropdown">
            	<span id="search_type">
            	<c:choose>
            		<c:when test="${searchType == 'authorResultList' }">作者</c:when>
            		<c:when test="${searchType == 'publisherResultList' }">出版社</c:when>
            		<c:otherwise>标题</c:otherwise>
            	</c:choose>
            	</span>
            	<span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
              <li><a id="search_type1" href="#">标题</a></li>
              <li><a id="search_type2" href="#">作者</a></li>
              <li><a id="search_type3" href="#">出版社</a></li>
            </ul>
          </div>
          <input class="input-xxlarge search_keyword" id="appendedInputButton appendedPrependedDropdownButton" type="text" value="${pagination.keyword }">
          <button id="search_button" class="btn" type="button">搜索</button>
        </div>
      </div>
    </div>
    <div class="container">