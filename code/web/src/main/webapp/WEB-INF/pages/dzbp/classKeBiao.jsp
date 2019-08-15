<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'>行政班课表</title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/dzbp/style.css"/>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery-2.1.1.min.js"></script>
</head>
<body>
    <div class="top">
        <div class="logo"><img src="/static_new/images/dzbp/zz2zLogo.jpg" style="width: 70px;height: 70px;"></div>
        <div class="school-name">
            <h3>株洲市第二中学</h3>
            <p>ZhuZhouShiDiErZhongXue</p>
        </div>
        <div class="cla-name">
            <h3 id="className"></h3>
            <p id="renKeTea"></p>
            <input id="clientId" type="hidden" value="${clientId}">
            <input id="currDate" type="hidden" value="${currDate}">
            <input id="currWeekIndex" type="hidden" value="${currWeekIndex}">
        </div>

        <div class="date">
            <p id="date">${currCnDate}</p>
            <p id="week">${currWeekDate}</p>
        </div>
        <div class="time">
            <p id="time"></p>
        </div>
    </div>
    <div class="content">
        <p class="title">教室课表</p>
        <table class="tb">
            <thead>
                <tr>
                    <td>课节/日期</td>
                    <td>星期一</td>
                    <td>星期二</td>
                    <td>星期三</td>
                    <td>星期四</td>
                    <td>星期五</td>
                    <td>星期六</td>
                    <td>星期日</td>
                </tr>
            </thead>
            <tbody id="KeShiList">

            </tbody>
            <script type="text/template" id="KeShiList_tmpl">
                {{~it:value:index}}
                <tr>
                    <td class="keShi ke">
                        <div>{{=value.name}}<br><em>{{=value.start}}-{{=value.end}}</em></div>
                    </td>
                    <td class="itt">
                    </td>
                    <td class="itt">
                    </td>
                    <td class="itt">
                    </td>
                    <td class="itt">
                    </td>
                    <td class="itt">
                    </td>
                    <td class="itt">
                    </td>
                    <td class="itt">
                    </td>
                </tr>
                {{~}}
            </script>
        </table>
    </div>
    <script src="/static_new/js/sea.js"></script>
    <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('dzbpClassKeBiao', function (dzbpClassKeBiao) {
            dzbpClassKeBiao.init();
        });
    </script>
</body>
</html>