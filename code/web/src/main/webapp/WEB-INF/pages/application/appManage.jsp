<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2017/4/25
  Time: 18:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.pojo.app.SessionValue" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>应用管理</title>
    <link rel="stylesheet" type="text/css" href="/static_new/css/appmanage/appManage.css">
    <script type="text/javascript" src="/static/js/askleave/jquery-2.1.1.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
</head>
<body>
<div class="content">
    <div id="head"></div>
    <input hidden="hidden" id="userId" value="${sessionValue.id}">
    <div id="main">
        <div class="appmanage-con">
            <div class="appmanage-info clearfix">
                <h4>应用管理</h4>
            <%--<c:if test="${sessionValue.id=='56e6917e0cf2a5c70a1ccac7'}">--%>
                <button class="app-new adminShow">新建应用</button>
                <label class="fr adminShow">
                    <select class="edit-info-select edit-class-school">
                    </select>
                    <input type="button" id="searchSchool" value="查询学校"/>
                </label>
            <%--</c:if>--%>
            </div>
            <div class="appmanage-main">
                <table class="appmanage-table">
                    <thead>
                    <tr>
                        <th style="width:10%">应用名</th>
                        <th style="width:10%">自定义名称</th>
                        <th style="width:10%">logo</th>
                    <%--<c:if test="${sessionValue.id=='56e6917e0cf2a5c70a1ccac7'}">--%>
                        <th style="width:26%" class="adminShow">URL</th>
                        <th style="width:8%" class="adminShow">是否可见</th>
                    <%--</c:if>--%>
                        <th style="width:13%">自定义分类</th>
                        <th style="width:13%">可见范围</th>
                        <th style="width:10%">操作</th>
                    </tr>
                    </thead>
                    <tbody id="navigations">

                    </tbody>
                    <script type="text/template" id="navigations_templ">
                        {{ if(it.message.length>0){ }}
                        {{ for (var i = 0, l = it.message.length; i < l; i++) { }}
                        {{var obj=it.message[i];}}
                        <tr>
                            <td>{{=obj.name}}</td>
                            <td>
                                <div class="input-wrap">
                                    <div class="input-bg"></div>
                                    <input type="text" value="{{=obj.cname}}" class="appNm" class="noborder">
                                </div>
                            </td>
                            <td>
                                <div class="logo-wrap">
                                    <img src="{{=obj.image}}" class="appImg" alt="">
                                    <br>
                                    <label id="upload-img" for="image-upload" style="cursor:pointer;">
                                    <span class="img-upload">上传图片</span>
                                    </label>
                                    <div class="size-zero">
                                        <input type="file" name="image-upload" id="image-upload" accept="image/*" multiple="multiple"/>
                                    </div>
                                    <img src="/img/loading4.gif" id="picuploadLoading"/>
                                </div>
                            </td>
                        <%--<c:if test="${sessionValue.id=='56e6917e0cf2a5c70a1ccac7'}">--%>
                            <td class="adminShow">
                                <div class="input-wrap">
                                    <div class="input-bg"></div>
                                    <input type="text" value="{{=obj.link}}" class="noborder appLink">
                                </div>
                            </td>
                            <td class="adminShow">
                                <label><input type="radio" name="isOpen{{=i}}" class="appShow" value="1" {{?obj.isShow==1}}checked{{?}}>是</label>
                                <label><input type="radio" name="isOpen{{=i}}" class="appShow" value="0" {{?obj.isShow==0}}checked{{?}}>否</label>
                            </td>
                        <%--</c:if>--%>
                            <td>
                                    <span class="define-sort">
                                        <span class="noborder"><em class="className">{{=obj.appClass}}</em><img src="/images/appManage/triangle.png" alt=""></span>
                                        <ul class="sort-select">
                                        {{~it.message[0].appClassList:value:index}}
                                            <li>
                                                <label><input type="radio" name="classify{{=i}}" class="classType" value="{{=value.value}}">{{=value.name}}</label>
                                            </li>
                                        {{~}}
                                        </ul>

                                    </span>
                            </td>
                            <td>
                                    <span class="open-range">
                                        <span class="noborder"><em class="roles">{{=obj.roleNames}}</em><img src="/images/appManage/triangle.png" alt=""></span>
                                        <ul class="range-select">
                                            <li>
                                                <label><input type="checkbox" name="role{{=i}}" value="" class="checkAll">全部</label>
                                            </li>
                                            {{~obj.roleList:value:index}}
                                            <li>
                                                <label><input type="checkbox" name="role{{=i}}" value="{{=value.value}}" class="checkSingle">{{=value.name}}</label>
                                            </li>
                                            {{~}}
                                        </ul>
                                    </span>
                            </td>
                            <td>
                                <div class="resume-default">
                                    <img src="/images/appManage/app-edit.png" alt="" class="btn-edit" acid="{{=obj.acId}}" roleNms="{{=obj.roleNames}}">
                                    <br>
                                    <span class="reset-default btn" navid="{{=obj.navId}}">恢复默认</span>
                                </div>
                                <div class="re-edit">
                                    <span class="btn-confirm btn" navid="{{=obj.navId}}">确认</span>
                                    <span class="btn-cancel btn">取消</span>
                                </div>
                            </td>
                        </tr>
                        {{ } }}
                        {{ } }}
                    </script>
                </table>
            </div>
        </div>
    </div>
</div>
<!---->
<div class="bg"></div>
<!--新建应用弹出框-->
<div class="app-popup">
    <div class="popup-top">
        <em>新建应用</em>
        <i> </i>
    </div>
    <div class="popup-info">
        <em>应用名称：</em><input type="text" id="appName">
    </div>
    <div class="popup-bottom">
        <button class="popup-bc sure-app">保存</button>
        <button class="popup-qx">取消</button>
    </div>
</div>
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('appManage');
</script>
</body>
</html>
