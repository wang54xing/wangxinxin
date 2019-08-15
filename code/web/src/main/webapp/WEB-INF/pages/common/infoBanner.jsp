<%--
  Created by IntelliJ IDEA.
  User: Niu Xin
  Date: 14-10-14
  Time: 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="/static/css/common/info-banner.css"/>
<script type="text/javascript" src="/static/js/common/info-banner.js"></script>
<script type="text/javascript" src="/static/js/common/protocol-detect.js"></script>

<div class="cloud-tabs">
	<c:if test="${param.bannerType == 'coursePage' && param.template == 'student'}">

        <%--<a href="/cloudres/load.do" class="title_a" style="margin-top: 15px;"><span class="item cloud-tile ${param.menuIndex == 0? 'active-l': ''}">云课程</span></a>--%>
		<c:choose>
			<c:when test="${not empty voteClass}">
				<a href="/vote/course" class="title_a"><span class="item cloud-tile ${param.menuIndex == 1? 'active': ''}">微课评比</span></a>
			</c:when>
			<c:otherwise>
				<a  href="/lesson/class.do" class="title_a" style="margin-top: 15px;display:none;"><span class="item cloud-tile ${param.menuIndex == 1? 'active': ''}">班级课程</span></a>
			</c:otherwise>
		</c:choose>

		<span class="fast_container" style="height: 1px;">
	     	
	    </span>
    </c:if>
</div>


<div id="ad_bones_content" style="display:none;padding: 0 50px;">
	<dl class="ad_bones_rule">
		<dt class="ad_title">轻松布置作业，在线领取教师进修基金</dt>
		<dd class="ad_descript"><em>奖金领取规则：</em></dd>
		<dd class="ad_item">老师布置在线作业，每位老师将获得1元教师进修基金（每位老师每日最多记3条，重复作业内容记一条），本活动自2月3日至3月30日截止。</dd>
		<dd class="ad_item_end"></dd>
	</dl>
	<dl class="ad_bones_rule">
		<dt class="ad_title">轻松推送课程，在线领取教师进修基金</dt>
		<dd class="ad_descript"><em>奖金领取规则：</em></dd>
		<dd class="ad_item">老师将云课程（或备课空间、校本资源）推送到“班级课程”，每位老师将获得1元教师进修基金（每位老师每日最多记3条，重复推送课程记一条），本活动自2月3日至3月30日截止。</dd>
		<dd class="ad_item_end"></dd>
	</dl>
	<dl class="ad_bones_rule">
		<dt class="ad_title">及时发通知，在线领取教师进修基金</dt>
		<dd class="ad_descript"><em>奖金领取规则：</em></dd>
		<dd class="ad_item">在老师在线发布“班级”通知，每位老师将获得1元教师进修基金（每位老师每日最多记3条，重复发布通知记一条），本活动自2月3日至3月30日截止。</dd>
		<dd class="ad_item_end"></dd>
	</dl>
	<dl class="ad_bones_rule">
		<dt class="ad_title">教师进修基金领取流程：</dt>
		<dd class="ad_item">在活动期间，老师可同时获得布置作业、推送课程、发布通知的三项教师进修基金，满足要求的老师，会在两个工作日(春节期间奖金自2月25日发放)收到k6kt小助手的私信通知，奖金通过k6kt平台中发放，获得进修基金的老师可以在k6kt中“电子超市”右侧的“个人账户中心”中查看余额，可以通过“提现”直接将余额转入支付宝。</dd>
		<dd class="ad_item_end"></dd>
	</dl>
</div>