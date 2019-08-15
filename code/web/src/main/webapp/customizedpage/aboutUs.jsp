<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
        <title>关于我们-K6KT</title>
        <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
        <link rel="stylesheet" type="text/css" href="/static/css/newmain.css"/>
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
        <script type='text/javascript' src='/static/js/newmain.js'></script>
    </head>
    <body>
    <%@ include file="/WEB-INF/pages/common/ypxxhead.jsp"%>
        
        <div class="company-container">
            <div class="error-container">
                <div class="login-bar"></div>
                <div class='title-bar-container' style="height:105px;">
                    <a href="/">
                        <img class="title-logo" src="/img/K6KT-LOGO.png">
                    </a>
                    <ul class="title-ul">
                        <li>
                            <a href="/">
                                <div class="title-warp"></div>
                                <div class="warp-content">首页</div>
                            </a>
                        </li>
                        <li class="active">
                            <a href="/aboutus/k6kt">
                                <div class="title-warp"></div>
                                <div class="warp-content">关于我们</div>
                            </a>
                        </li>
                        <li>
                            <a href="/contactus/k6kt">
                                <div class="title-warp"></div>
                                <div class="warp-content">联系我们</div>
                            </a>
                        </li>
                        <li>
                            <a href="/service/k6kt">
                                <div class="title-warp"></div>
                                <div class="warp-content">服务条款</div>
                            </a>
                        </li>
                        <li>
                            <a href="/privacy/k6kt">
                                <div class="title-warp"></div>
                                <div class="warp-content">隐私保护承诺</div>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="company-info-container">
                <div class="info-left">
                    <div class="company-info-title">关于我们</div>
                    <div class="company-info-content">
                        上海复兰信息科技有限公司，专注于信息化技术、教育互联网科技产品及多媒体互动技术的设计、开发和运营，是互联网在线教育科技产品的创新者和领导者。
                        <br/>复兰科技旗下的教育产品包括数字化校园、精品直录播教室、多媒体互动科技展厅、在线课堂、名师在线、翻转课堂、在线题库、在线招生、3D打印技术等一系列科技产品和平台技术。其中，在线教育平台旨在突破传统教育的时空限制实现优秀教师资源共享，提供给全国各地的师生以最高科技的在线教育平台和最具创造性的学习方式。
                        <br/>复兰科技由复旦校友创立，目前大部分员工具有互联网及管理等领域的中美名校硕士以上学历。创新、自由的企业文化吸引了大批互联网行业高精尖人才的加盟，高科技的产品和客户至上的服务理念让我们从众多同类企业中脱颖而出，并获得客户的广泛赞誉。目前公司的科技产品现已广泛应用于上海、北京、江苏、安徽、福建等百所著名院校、中小学，及上海著名展馆、城市科技馆。
                    </div>
                </div>
                <div class="info-right">
                    <a href="/aboutus/k6kt" class="company-info-title active">关于我们</a>
                    <a href="/contactus/k6kt" class="company-info-title">联系我们</a>
                    <a href="/service/k6kt" class="company-info-title">服务条款</a>
                    <a href="/privacy/k6kt" class="company-info-title">隐私保护承诺</a>
                </div>
            </div>
        </div>
        <!-- 页尾 -->
        <%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>
        <!-- 页尾 -->
    </body>
</html>