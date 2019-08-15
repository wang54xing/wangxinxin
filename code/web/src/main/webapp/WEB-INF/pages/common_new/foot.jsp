<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/18
  Time: 下午5:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<div class="bg"></div>
<%--提示弹窗--%>
<div class="notice wind msg" style="min-height: 148px;margin-top: -148px;display: none;width: 448px;background: #fff;position: fixed;top: 50%;left: 50%;margin-left: -224px;z-index: 1001;">
    <div class="notice-top" style="line-height: 40px;height: 40px;background: #546fb4;color: #fff;font-size: 16px;font-weight: bold;padding: 0 15px;">提示</div>
    <div class="notice-cont" style="min-height: 20px;padding: 20px 15px;text-align: center;background: url('/images/jinggao.jpg') no-repeat;border-bottom: 1px solid #ccc;line-height: 20px;"></div>
    <button style="border: 0;margin: 10px 0 10px 188px;padding: 3px 15px;background: #ff7900;color: white;cursor: pointer;border-radius: 5px;">确定</button>
</div>
<%--确定弹窗--%>
<div class="confirm wind" style="display: none">
    <div class="confirm-top">提示</div>
    <div class="confirm-cont"></div>
    <button class="yes">确定</button>
    <button class="no">取消</button>
</div>

<div id="foot">
    <div class="sub-foot clearfix">


        <p>版权所有：上海复兰信息科技有限公司 <a href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a> 沪ICP备14004857号 </p>
        <ul>
            <li><a href="/aboutus/k6kt">关于我们</a></li><li>|</li>
            <li><a href="/contactus/k6kt">联系我们</a></li><li>|</li>
            <li><a href="/service/k6kt">服务条款</a></li><li>|</li>
            <li><a href="/privacy/k6kt">隐私保护</a></li><li>|</li>
            <li><a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' class="qq-online" target="_blank"></a></li>
        </ul>
    </div>
</div>

<script>
    $('.notice button').click(function(){
        $(this).parent().fadeOut();
        $('.bg').fadeOut();
    });

    function showMsg(msg){
        $('.notice .notice-cont').text(msg);
        $('.notice,.bg').fadeIn();
    }

    function hideMsg(){
        $('.msg').fadeOut();
        $('.bg').fadeOut();
    }
</script>
