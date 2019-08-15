<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--<%@ page import="com.jintizi.entity.UserInfo" %>--%>
<link rel="stylesheet" type="text/css" href="/static/css/style.css?v=1209"/>
<link rel="stylesheet" href="/static/css/activate.css" type="text/css"/>
<%--<link rel="stylesheet" href="/css/jquery-ui.css" type="text/css"/>--%>
<link rel="stylesheet" href="/static/css/forIE.css" type="text/css"/>
<link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
<style>
    #fubg {
        background-color: gray;
        left: 0;
        opacity: 0.5;
        position: fixed;
        top: 0;
        z-index: 30;
        filter: alpha(opacity=50);
        -moz-opacity: 0.5;
        -khtml-opacity: 0.5;
        width: 100%;
        height: 100%;
        display: none;
    }

    .popu div {
        overflow: hidden
    }
</style>
<script type="text/javascript" src="/static/js/validate-form.js"></script>
<script type="text/javascript" src="/static/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/js/timepicker.js"></script>
<script type="text/javascript" src="/static/js/sharedpart.js"></script>
<script type="text/javascript" src="/static/js/ypxxUtility.js"></script>
<%@ include file="header.jsp" %>


<div id="loading" class="loading"><img alt="" src="/img/loading.gif" style="position:fixed; left:50%;top:50%;"></div>
<div id="fubg" style="width: 100%; height: 100%; display: none;"></div>



<style type="text/css">
    .player-container {
        position:fixed;
        top:50%;
        left:50%;
        margin-left:-415px;
        margin-top:-240px;
        z-index: 1000;
        overflow: visible;
        display: none;
        background-color: white;
        box-shadow: 0 10px 25px #000;
        box-sizing: content-box;
        border-radius: 4px;
        padding:15px;
    }
    .player-container * {
        overflow: visible;
    }

    .player-div {
        background: #000;
        width: 800px;
        height: 450px;
        overflow: hidden;
    }

    .player-close-btn {
        position: absolute;
        top: -18px;
        right: -18px;
        width: 36px;
        height: 36px;
        cursor: pointer;
        z-index: 8040;
        background-image: url('/static/plugins/fancyBox/fancybox_sprite.png');
    }

    .dialog-bg {
        background-color: #000;
        left: 0;
        position: fixed;
        top: 0;
        z-index: 30;
        opacity: 0.5;
        filter: alpha(opacity = 50);
        -moz-opacity: 0.5;
        -khtml-opacity: 0.5;
        width: 100%; height: 100%; display: none;
    }
</style>