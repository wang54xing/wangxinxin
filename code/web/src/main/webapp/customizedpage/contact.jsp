<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
        <title>联系我们-K6KT</title>
        <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
        <link rel="stylesheet" type="text/css" href="/static/css/newmain.css"/>
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
        <script type="text/javascript" src="/static/js/validate-form.js"></script>
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
                        <li>
                            <a href="/aboutus/k6kt">
                                <div class="title-warp"></div>
                                <div class="warp-content">关于我们</div>
                            </a>
                        </li>
                        <li class="active">
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
                    <div class="company-info-title">联系我们</div>
                    <dl class="contact-us-dl">
                        <dt id="name-title">用户名</dt>
                        <dd id="name-prompt" style="margin:5px;">必填</dd>
                        <dd class="showerror"><input class="inputbar" type="text" id="name" /><span ></span></dd>
        
                        <dt>邮箱地址</dt>
                        <dd id="email-prompt" style="margin:5px;">*邮箱地址和联系电话至少填写一项</dd>
                        <dd class="showerror"><input class="inputbar" type="text" id="email_input" /><span ></span></dd>
        
                        <dt>联系电话</dt>
                        <dd id="phone-prompt" style="margin:5px;">*邮箱地址和联系电话至少填写一项</dd>
                        <dd class="showerror"><input class="inputbar" type="text" id="phone" /><span ></span></dd>
        
                        <dt>留言板</dt>
                        <dd id="message-prompt" style="margin:5px;">请留下您的宝贵意见</dd>
                        <dd class="showerror"><textarea maxlength="1000" id="message_text" style="width:530px;height:220px;padding:10px;background-color:#EFEFEF;font:13px Helvetica,Arial,sans-serif"></textarea><span class="showerror"></span></dd>
        
                        <dt><input id="submitBtn" type="button" value="提交" onclick="submitMessage();"style="background: #00c2f3;color: white;cursor: pointer;height: 28px;width: 80px;" /></dt>

                        <div style="line-height:25px;">
                            <div style="margin-top:30px;">您也可以通过如下方式联系我们：</div>
                            <div>电话：021-80125668</div>
                            <div>传真：021-80125668*850</div>
                            <div>邮箱：info@fulaan.com</div>
                            <div>地址：上海市大渡河路168弄北岸长风E栋12楼 邮编 200233</div>
                        </div>
                    </dl>
                </div>
                <div class="info-right">
                    <a href="/aboutus/k6kt" class="company-info-title">关于我们</a>
                    <a href="/contactus/k6kt" class="company-info-title active">联系我们</a>
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