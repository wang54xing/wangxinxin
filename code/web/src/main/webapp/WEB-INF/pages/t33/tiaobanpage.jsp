<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>  
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title class="i18n" name='title'></title>
    <meta id="i18n_pagename" content="index-common">
    <meta name="viewport" content="width=device-width">
    <meta name="keywords" content="" />
    <meta name="description" content=""/>
    <link rel="stylesheet" href="/static_new/css/t33/reset.css"></link>
    <script type="text/javascript" src="/static_new/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript">
    $(function(){
    	load();
    })
    
    
    function load(sub)
    {
    	var url="/t33/fenban/tiaoban.do?userId="+jQuery("#userId").val();
    	if(sub)
    	{
    		url=url+"&subject="+sub;
    	}	
        $.ajax({
            url:url ,
            type: "get",
            dataType: "json",
            data: {
                'inJson': true
            },
            success: function (data) {
                
            	var hs='';
            	for(var i=0;i<data.message.currentList.length;i++)
            	{
            		var o=data.message.currentList[i];
            		hs+='<button class="a-cor" id="'+o.id+'">'+o.name+'</button>';
            	}	
            	
            	if(!sub)
                {
            	  jQuery("#alreadyDIV").html(hs);
            	  
            	  
            	  jQuery("#alreadyDIV").find("button").click(function(){
            		  
            		  
            		  var ss=jQuery(this).attr("id");
            		  load(ss);
            		  
            	  });
                }
            	
            	
            	
            	if(data.message.list1.length>0)
            	{
            		var fei='';
                	for(var i=0;i<data.message.list1.length;i++)
                	{
                		var o=data.message.list1[i];
                		fei+='<button  id="'+o.id+'">'+o.name+'</button>';
                	}
            		jQuery("#feichongtu").html(fei);
            		
            		jQuery("#feichongtu").find("button").click(function(){
            			var id=jQuery(this).attr("id");
            			operTiaoban(id);
            		});
            	}
            	
            	
            	if(data.message.list2.length>0)
            	{
            		var ch='';
                	for(var i=0;i<data.message.list2.length;i++)
                	{
                		var o=data.message.list2[i];
                		ch+='<button  class="" id="'+o.id+'">'+o.name+'</button>';
                	}
            		jQuery("#chongtu").html(ch);
            		
            		jQuery("#chongtu").find("button").click(function(){
            			var id=jQuery(this).attr("id");
            			operTiaoban(id);
            		});
            	}
            	
            	
            }
        });
    }
    
    
    
    
    function operTiaoban(ss)
    {
    	alert(ss);
    	jQuery("#"+ss).addClass("a-cor");
    	
    	var url="/t33/fenban/tiaoban/oper.do?userId="+jQuery("#userId").val();
    	url+="&subject="+ss;
    	
    	 $.ajax({
             url:url ,
             type: "post",
             dataType: "json",
             success: function (data) {
             }
         });
    }
    </script>
</head>
<body>
	<div class="top">
		<div class="top-center">
			<a href="/t33/load/page.do">选科数据</a>
			<a href="/t33/fenban/page.do?tag=1">教学班管理</a>
			<a href="/t33/fenban/check/page.do?tag=1" class="a-cur">分班检测</a>
			<a href="/t33/fenban/zuhe/page.do">组合教学班</a>
		</div>
	</div>
	<input id="userId" hidden="hidden" value="${userId }"/>
	<div class="mr1200 mt55">
		<div class="popup-top">
            <a href="index-dc.html" class="prev-lt">&lt;返回</a>
        </div>
        <dl class="prev-dl">
        	<dd class="prev-d">
        		<span>当前教学班</span>
        		<div id="alreadyDIV">
        		
        			
        			
        		</div>
        	</dd>
        	<dd class="prev-d">
        		<span>调至</span>
        	</dd>
        	<dd class="prev-d prev-a">
        		<span>非冲突目标教学班</span>
        		<div id="feichongtu">
	        		<button>物理2</button>
	        		<button>物理3</button>
	        		<button>物理4</button>
	        		<button>物理5</button>
        		</div>
        	</dd>
        	<dd class="prev-d prev-b">
        		<span>冲突目标教学班</span>
        		<div id="chongtu">
	        		<button>物理2</button>
	        		<button>物理3</button>
	        		<button>物理4</button>
	        		<button>物理5</button>
        		</div>
        	</dd>
        </dl>
        <div class="com-bottom mt30">
			<button class="qd">确定</button>
			<button class="qx">取消</button>
		</div>
    </div>
</body>
</html>
