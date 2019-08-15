var urls=[
	"http://www.k6kt.com/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//0
	"http://puyangxian.k6kt.com/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//1
	"http://www.fulaan.com/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//2
	"http://midong.k6kt.com/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//3
	"http://172.18.19.10/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//4
	"http://172.18.19.13/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//5
	"http://yun.k6kt.com/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//6
	"http://app.k6kt.com/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//7
	"http://61.163.2.100/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//8
	"http://61.163.2.101/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//9
    "http://121.40.229.225:8080/user/k6kt/sso/login.do?jsonpCallback=callbackFunction",//10
];


var clientSuccess=0;
var thisIndex=-1;
var location1="",location2="",location3="";

//thisIndex 0 k6kt
//thisIndex 1 yun
//thisIndex 2
//thisIndex 3 app.k6kt.com
function loginK6ktSso()
{
	var ui=getCookie("ui");
	var cui=getCookie("c_ui");

	var cValue=(cui?cui:ui);

	if(cValue)
	{
		var url=location.href;

		if(url.indexOf("puyangxian.k6kt.com")>=0)
		{
			thisIndex=1;
			location1=urls[0];
			location2=urls[2];
		}
		else if(url.indexOf("172.18.19.10")>=0)
		{
			thisIndex=1;
			location1=urls[5];
			location2=urls[1];
		}
		else if(url.indexOf("61.163.2.100")>=0)
		{
			thisIndex=1;
			location1=urls[9];
			location2=urls[1];
		}
		else if(url.indexOf("www.k6kt.com")>=0)
		{
			thisIndex=0;
			location1=urls[1];
			location2=urls[2];
			location3=urls[7];
            location3=urls[10];
		}
		else if(url.indexOf("61.163.2.101")>=0)
		{
			thisIndex=0;
			location1=urls[8];
			location2=urls[2];
		}
		else if(url.indexOf("172.18.19.13")>=0)
		{
			thisIndex=0;
			location1=urls[4];
			location2=urls[2];
		}
		else if(url.indexOf("www.fulaan.com")>=0)
		{
			thisIndex=2;
			location1=urls[0];
			location2=urls[1];
			location3=urls[3];
            location3=urls[10];
		}
		else if(url.indexOf("midong.k6kt.com")>=0)
		{
			thisIndex=1;
			location1=urls[0];
			location2=urls[2];
		}
		else if(url.indexOf("yun.k6kt.com")>=0)
		{
			thisIndex=1;
			location1=urls[0];
			location2=urls[2];
		}
		else if(url.indexOf("app.k6kt.com")>=0)
		{
			thisIndex=3;
			location1=urls[0];
			location2=urls[2];
            location2=urls[10];
		}
		else if(url.indexOf("121.40.229.225:8080")>=0)
        {
            thisIndex=4;
            location1=urls[0];
            location1=urls[1];
            location2=urls[2];
            location3=urls[7];
        }

		if(thisIndex==-1)
		{
			return;
		}

		location1=location1+"&cValue="+cValue;
		location2=location2+"&cValue="+cValue;


		var iframe = document.createElement("iframe");
		iframe.src = location1;
		iframe.style.display="none";

		if (iframe.attachEvent){
			iframe.attachEvent("onload", function(){
				gotoPage();
			});
		} else {
			iframe.onload = function(){
				gotoPage();
			};
		}
		document.body.appendChild(iframe);




		var iframe2 = document.createElement("iframe");
		iframe2.src = location2;
		iframe2.style.display="none";

		if (iframe2.attachEvent){
			iframe2.attachEvent("onload", function(){
				gotoPage();
			});
		} else {
			iframe2.onload = function(){
				gotoPage();
			};
		}
		document.body.appendChild(iframe2);

		if(location3!=""){
			location3=location3+"&cValue="+cValue;
			var iframe3 = document.createElement("iframe");
			iframe3.src = location3;
			iframe3.style.display="none";

			if (iframe3.attachEvent){
				iframe3.attachEvent("onload", function(){
					gotoPage();
				});
			} else {
				iframe3.onload = function(){
					gotoPage();
				};
			}
			document.body.appendChild(iframe3);
		}
	}
}


function gotoPage()
{
	clientSuccess=clientSuccess+1;
	if(clientSuccess==2)
	{
		if(thisIndex==0)
		{

			var url=location.href;
			if(url && url.indexOf("puyangxian")>0  && url.indexOf("k6kt.com")>0)
			{
				location.href="http://yun.k6kt.com/user/gotomainpage.do";
			}
			if(url && url.indexOf("puyangxian")>0  && url.indexOf("61.163.2.101")>0)
			{
				location.href="http://61.163.2.100/user/gotomainpage.do";
			}
			if(url && url.indexOf("puyangxian")>0  && url.indexOf("172.18.19.13")>0)
			{
				location.href="http://172.18.19.10/user/gotomainpage.do";
			}
			else
			{
				location.href="/user/homepage.do";
			}

			//location.href="/user/homepage.do";
		}
		if(thisIndex==1)
		{
			location.href="/user/gotomainpage.do";
		}
		if(thisIndex==3)
		{
			location.href="/user/homepage.do";
		}
        if (thisIndex==4) {
            location.href="/";
        }
	}
}





function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for(var i=0; i<ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1);
		if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
	}
	return "";
}


