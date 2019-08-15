/*	SWFObject v2.2 <http://code.google.com/p/swfobject/> 
	is released under the MIT License <http://www.opensource.org/licenses/mit-license.php> 
*/
var swfobject=function(){var D="undefined",r="object",S="Shockwave Flash",W="ShockwaveFlash.ShockwaveFlash",q="application/x-shockwave-flash",R="SWFObjectExprInst",x="onreadystatechange",O=window,j=document,t=navigator,T=false,U=[h],o=[],N=[],I=[],l,Q,E,B,J=false,a=false,n,G,m=true,M=function(){var aa=typeof j.getElementById!=D&&typeof j.getElementsByTagName!=D&&typeof j.createElement!=D,ah=t.userAgent.toLowerCase(),Y=t.platform.toLowerCase(),ae=Y?/win/.test(Y):/win/.test(ah),ac=Y?/mac/.test(Y):/mac/.test(ah),af=/webkit/.test(ah)?parseFloat(ah.replace(/^.*webkit\/(\d+(\.\d+)?).*$/,"$1")):false,X=!+"\v1",ag=[0,0,0],ab=null;if(typeof t.plugins!=D&&typeof t.plugins[S]==r){ab=t.plugins[S].description;if(ab&&!(typeof t.mimeTypes!=D&&t.mimeTypes[q]&&!t.mimeTypes[q].enabledPlugin)){T=true;X=false;ab=ab.replace(/^.*\s+(\S+\s+\S+$)/,"$1");ag[0]=parseInt(ab.replace(/^(.*)\..*$/,"$1"),10);ag[1]=parseInt(ab.replace(/^.*\.(.*)\s.*$/,"$1"),10);ag[2]=/[a-zA-Z]/.test(ab)?parseInt(ab.replace(/^.*[a-zA-Z]+(.*)$/,"$1"),10):0}}else{if(typeof O.ActiveXObject!=D){try{var ad=new ActiveXObject(W);if(ad){ab=ad.GetVariable("$version");if(ab){X=true;ab=ab.split(" ")[1].split(",");ag=[parseInt(ab[0],10),parseInt(ab[1],10),parseInt(ab[2],10)]}}}catch(Z){}}}return{w3:aa,pv:ag,wk:af,ie:X,win:ae,mac:ac}}(),k=function(){if(!M.w3){return}if((typeof j.readyState!=D&&j.readyState=="complete")||(typeof j.readyState==D&&(j.getElementsByTagName("body")[0]||j.body))){f()}if(!J){if(typeof j.addEventListener!=D){j.addEventListener("DOMContentLoaded",f,false)}if(M.ie&&M.win){j.attachEvent(x,function(){if(j.readyState=="complete"){j.detachEvent(x,arguments.callee);f()}});if(O==top){(function(){if(J){return}try{j.documentElement.doScroll("left")}catch(X){setTimeout(arguments.callee,0);return}f()})()}}if(M.wk){(function(){if(J){return}if(!/loaded|complete/.test(j.readyState)){setTimeout(arguments.callee,0);return}f()})()}s(f)}}();function f(){if(J){return}try{var Z=j.getElementsByTagName("body")[0].appendChild(C("span"));Z.parentNode.removeChild(Z)}catch(aa){return}J=true;var X=U.length;for(var Y=0;Y<X;Y++){U[Y]()}}function K(X){if(J){X()}else{U[U.length]=X}}function s(Y){if(typeof O.addEventListener!=D){O.addEventListener("load",Y,false)}else{if(typeof j.addEventListener!=D){j.addEventListener("load",Y,false)}else{if(typeof O.attachEvent!=D){i(O,"onload",Y)}else{if(typeof O.onload=="function"){var X=O.onload;O.onload=function(){X();Y()}}else{O.onload=Y}}}}}function h(){if(T){V()}else{H()}}function V(){var X=j.getElementsByTagName("body")[0];var aa=C(r);aa.setAttribute("type",q);var Z=X.appendChild(aa);if(Z){var Y=0;(function(){if(typeof Z.GetVariable!=D){var ab=Z.GetVariable("$version");if(ab){ab=ab.split(" ")[1].split(",");M.pv=[parseInt(ab[0],10),parseInt(ab[1],10),parseInt(ab[2],10)]}}else{if(Y<10){Y++;setTimeout(arguments.callee,10);return}}X.removeChild(aa);Z=null;H()})()}else{H()}}function H(){var ag=o.length;if(ag>0){for(var af=0;af<ag;af++){var Y=o[af].id;var ab=o[af].callbackFn;var aa={success:false,id:Y};if(M.pv[0]>0){var ae=c(Y);if(ae){if(F(o[af].swfVersion)&&!(M.wk&&M.wk<312)){w(Y,true);if(ab){aa.success=true;aa.ref=z(Y);ab(aa)}}else{if(o[af].expressInstall&&A()){var ai={};ai.data=o[af].expressInstall;ai.width=ae.getAttribute("width")||"0";ai.height=ae.getAttribute("height")||"0";if(ae.getAttribute("class")){ai.styleclass=ae.getAttribute("class")}if(ae.getAttribute("align")){ai.align=ae.getAttribute("align")}var ah={};var X=ae.getElementsByTagName("param");var ac=X.length;for(var ad=0;ad<ac;ad++){if(X[ad].getAttribute("name").toLowerCase()!="movie"){ah[X[ad].getAttribute("name")]=X[ad].getAttribute("value")}}P(ai,ah,Y,ab)}else{p(ae);if(ab){ab(aa)}}}}}else{w(Y,true);if(ab){var Z=z(Y);if(Z&&typeof Z.SetVariable!=D){aa.success=true;aa.ref=Z}ab(aa)}}}}}function z(aa){var X=null;var Y=c(aa);if(Y&&Y.nodeName=="OBJECT"){if(typeof Y.SetVariable!=D){X=Y}else{var Z=Y.getElementsByTagName(r)[0];if(Z){X=Z}}}return X}function A(){return !a&&F("6.0.65")&&(M.win||M.mac)&&!(M.wk&&M.wk<312)}function P(aa,ab,X,Z){a=true;E=Z||null;B={success:false,id:X};var ae=c(X);if(ae){if(ae.nodeName=="OBJECT"){l=g(ae);Q=null}else{l=ae;Q=X}aa.id=R;if(typeof aa.width==D||(!/%$/.test(aa.width)&&parseInt(aa.width,10)<310)){aa.width="310"}if(typeof aa.height==D||(!/%$/.test(aa.height)&&parseInt(aa.height,10)<137)){aa.height="137"}j.title=j.title.slice(0,47)+" - Flash Player Installation";var ad=M.ie&&M.win?"ActiveX":"PlugIn",ac="MMredirectURL="+O.location.toString().replace(/&/g,"%26")+"&MMplayerType="+ad+"&MMdoctitle="+j.title;if(typeof ab.flashvars!=D){ab.flashvars+="&"+ac}else{ab.flashvars=ac}if(M.ie&&M.win&&ae.readyState!=4){var Y=C("div");X+="SWFObjectNew";Y.setAttribute("id",X);ae.parentNode.insertBefore(Y,ae);ae.style.display="none";(function(){if(ae.readyState==4){ae.parentNode.removeChild(ae)}else{setTimeout(arguments.callee,10)}})()}u(aa,ab,X)}}function p(Y){if(M.ie&&M.win&&Y.readyState!=4){var X=C("div");Y.parentNode.insertBefore(X,Y);X.parentNode.replaceChild(g(Y),X);Y.style.display="none";(function(){if(Y.readyState==4){Y.parentNode.removeChild(Y)}else{setTimeout(arguments.callee,10)}})()}else{Y.parentNode.replaceChild(g(Y),Y)}}function g(ab){var aa=C("div");if(M.win&&M.ie){aa.innerHTML=ab.innerHTML}else{var Y=ab.getElementsByTagName(r)[0];if(Y){var ad=Y.childNodes;if(ad){var X=ad.length;for(var Z=0;Z<X;Z++){if(!(ad[Z].nodeType==1&&ad[Z].nodeName=="PARAM")&&!(ad[Z].nodeType==8)){aa.appendChild(ad[Z].cloneNode(true))}}}}}return aa}function u(ai,ag,Y){var X,aa=c(Y);if(M.wk&&M.wk<312){return X}if(aa){if(typeof ai.id==D){ai.id=Y}if(M.ie&&M.win){var ah="";for(var ae in ai){if(ai[ae]!=Object.prototype[ae]){if(ae.toLowerCase()=="data"){ag.movie=ai[ae]}else{if(ae.toLowerCase()=="styleclass"){ah+=' class="'+ai[ae]+'"'}else{if(ae.toLowerCase()!="classid"){ah+=" "+ae+'="'+ai[ae]+'"'}}}}}var af="";for(var ad in ag){if(ag[ad]!=Object.prototype[ad]){af+='<param name="'+ad+'" value="'+ag[ad]+'" />'}}aa.outerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"'+ah+">"+af+"</object>";N[N.length]=ai.id;X=c(ai.id)}else{var Z=C(r);Z.setAttribute("type",q);for(var ac in ai){if(ai[ac]!=Object.prototype[ac]){if(ac.toLowerCase()=="styleclass"){Z.setAttribute("class",ai[ac])}else{if(ac.toLowerCase()!="classid"){Z.setAttribute(ac,ai[ac])}}}}for(var ab in ag){if(ag[ab]!=Object.prototype[ab]&&ab.toLowerCase()!="movie"){e(Z,ab,ag[ab])}}aa.parentNode.replaceChild(Z,aa);X=Z}}return X}function e(Z,X,Y){var aa=C("param");aa.setAttribute("name",X);aa.setAttribute("value",Y);Z.appendChild(aa)}function y(Y){var X=c(Y);if(X&&X.nodeName=="OBJECT"){if(M.ie&&M.win){X.style.display="none";(function(){if(X.readyState==4){b(Y)}else{setTimeout(arguments.callee,10)}})()}else{X.parentNode.removeChild(X)}}}function b(Z){var Y=c(Z);if(Y){for(var X in Y){if(typeof Y[X]=="function"){Y[X]=null}}Y.parentNode.removeChild(Y)}}function c(Z){var X=null;try{X=j.getElementById(Z)}catch(Y){}return X}function C(X){return j.createElement(X)}function i(Z,X,Y){Z.attachEvent(X,Y);I[I.length]=[Z,X,Y]}function F(Z){var Y=M.pv,X=Z.split(".");X[0]=parseInt(X[0],10);X[1]=parseInt(X[1],10)||0;X[2]=parseInt(X[2],10)||0;return(Y[0]>X[0]||(Y[0]==X[0]&&Y[1]>X[1])||(Y[0]==X[0]&&Y[1]==X[1]&&Y[2]>=X[2]))?true:false}function v(ac,Y,ad,ab){if(M.ie&&M.mac){return}var aa=j.getElementsByTagName("head")[0];if(!aa){return}var X=(ad&&typeof ad=="string")?ad:"screen";if(ab){n=null;G=null}if(!n||G!=X){var Z=C("style");Z.setAttribute("type","text/css");Z.setAttribute("media",X);n=aa.appendChild(Z);if(M.ie&&M.win&&typeof j.styleSheets!=D&&j.styleSheets.length>0){n=j.styleSheets[j.styleSheets.length-1]}G=X}if(M.ie&&M.win){if(n&&typeof n.addRule==r){n.addRule(ac,Y)}}else{if(n&&typeof j.createTextNode!=D){n.appendChild(j.createTextNode(ac+" {"+Y+"}"))}}}function w(Z,X){if(!m){return}var Y=X?"visible":"hidden";if(J&&c(Z)){c(Z).style.visibility=Y}else{v("#"+Z,"visibility:"+Y)}}function L(Y){var Z=/[\\\"<>\.;]/;var X=Z.exec(Y)!=null;return X&&typeof encodeURIComponent!=D?encodeURIComponent(Y):Y}var d=function(){if(M.ie&&M.win){window.attachEvent("onunload",function(){var ac=I.length;for(var ab=0;ab<ac;ab++){I[ab][0].detachEvent(I[ab][1],I[ab][2])}var Z=N.length;for(var aa=0;aa<Z;aa++){y(N[aa])}for(var Y in M){M[Y]=null}M=null;for(var X in swfobject){swfobject[X]=null}swfobject=null})}}();return{registerObject:function(ab,X,aa,Z){if(M.w3&&ab&&X){var Y={};Y.id=ab;Y.swfVersion=X;Y.expressInstall=aa;Y.callbackFn=Z;o[o.length]=Y;w(ab,false)}else{if(Z){Z({success:false,id:ab})}}},getObjectById:function(X){if(M.w3){return z(X)}},embedSWF:function(ab,ah,ae,ag,Y,aa,Z,ad,af,ac){var X={success:false,id:ah};if(M.w3&&!(M.wk&&M.wk<312)&&ab&&ah&&ae&&ag&&Y){w(ah,false);K(function(){ae+="";ag+="";var aj={};if(af&&typeof af===r){for(var al in af){aj[al]=af[al]}}aj.data=ab;aj.width=ae;aj.height=ag;var am={};if(ad&&typeof ad===r){for(var ak in ad){am[ak]=ad[ak]}}if(Z&&typeof Z===r){for(var ai in Z){if(typeof am.flashvars!=D){am.flashvars+="&"+ai+"="+Z[ai]}else{am.flashvars=ai+"="+Z[ai]}}}if(F(Y)){var an=u(aj,am,ah);if(aj.id==ah){w(ah,true)}X.success=true;X.ref=an}else{if(aa&&A()){aj.data=aa;P(aj,am,ah,ac);return}else{w(ah,true)}}if(ac){ac(X)}})}else{if(ac){ac(X)}}},switchOffAutoHideShow:function(){m=false},ua:M,getFlashPlayerVersion:function(){return{major:M.pv[0],minor:M.pv[1],release:M.pv[2]}},hasFlashPlayerVersion:F,createSWF:function(Z,Y,X){if(M.w3){return u(Z,Y,X)}else{return undefined}},showExpressInstall:function(Z,aa,X,Y){if(M.w3&&A()){P(Z,aa,X,Y)}},removeSWF:function(X){if(M.w3){y(X)}},createCSS:function(aa,Z,Y,X){if(M.w3){v(aa,Z,Y,X)}},addDomLoadEvent:K,addLoadEvent:s,getQueryParamValue:function(aa){var Z=j.location.search||j.location.hash;if(Z){if(/\?/.test(Z)){Z=Z.split("?")[1]}if(aa==null){return L(Z)}var Y=Z.split("&");for(var X=0;X<Y.length;X++){if(Y[X].substring(0,Y[X].indexOf("="))==aa){return L(Y[X].substring((Y[X].indexOf("=")+1)))}}}return""},expressInstallCallback:function(){if(a){var X=c(R);if(X&&l){X.parentNode.replaceChild(l,X);if(Q){w(Q,true);if(M.ie&&M.win){l.style.display="block"}}if(E){E(B)}}a=false}}}}();

/**
 * SWFObject v1.5: Flash Player detection and embed - http://blog.deconcept.com/swfobject/
 *
 * SWFObject is (c) 2007 Geoff Stearns and is released under the MIT License:
 * http://www.opensource.org/licenses/mit-license.php
 *
 */
if(typeof deconcept=="undefined"){
	var deconcept=new Object();
	}
if(typeof deconcept.util=="undefined"){
	deconcept.util=new Object();
	}
if(typeof deconcept.SWFObjectUtil=="undefined"){
	deconcept.SWFObjectUtil=new Object();
	}
deconcept.SWFObject=function(_1,id,w,h,_5,c,_7,_8,_9,_a){
		if(!document.getElementById){
			return;}
		this.DETECT_KEY=_a?_a:"detectflash";
		this.skipDetect=deconcept.util.getRequestParameter(this.DETECT_KEY);
		this.params=new Object();
		this.variables=new Object();
		this.attributes=new Array();
		if(_1){
			this.setAttribute("swf",_1);
		}
	if(id){
		this.setAttribute("id",id);
	}
	if(w){
		this.setAttribute("width",w);
		}
	if(h){
		this.setAttribute("height",h);
		}
	if(_5){
		this.setAttribute("version",new deconcept.PlayerVersion(_5.toString().split(".")));
		}
	this.installedVer=deconcept.SWFObjectUtil.getPlayerVersion();
	if(!window.opera&&document.all&&this.installedVer.major>7){
		deconcept.SWFObject.doPrepUnload=true;
		}
	if(c){
		this.addParam("bgcolor",c);
	}
	var q=_7?_7:"high";
	this.addParam("quality",q);
	this.setAttribute("useExpressInstall",false);
	this.setAttribute("doExpressInstall",false);
	var _c=(_8)?_8:window.location;this.setAttribute("xiRedirectUrl",_c);
	this.setAttribute("redirectUrl","");
	if(_9){
		this.setAttribute("redirectUrl",_9);
	}
};
deconcept.SWFObject.prototype={
	useExpressInstall:function(_d){
		this.xiSWFPath=!_d?"expressinstall.swf":_d;
		this.setAttribute("useExpressInstall",true);
		},
	setAttribute:function(_e,_f){
		this.attributes[_e]=_f;
		},
	getAttribute:function(_10){
		return this.attributes[_10];
		},
	addParam:function(_11,_12){
		this.params[_11]=_12;
		},
	getParams:function(){
		return this.params;
		},
	addVariable:function(_13,_14){
		this.variables[_13]=_14;
		},
	getVariable:function(_15){
		return this.variables[_15];
	},
	getVariables:function(){
		return this.variables;
		},
	getVariablePairs:function(){
		var _16=new Array();
		var key;
		var _18=this.getVariables();
		for(key in _18){
			_16[_16.length]=key+"="+_18[key];
			}return _16;
		},
	getSWFHTML:function(){
		var _19="";
		if(navigator.plugins&&navigator.mimeTypes&&navigator.mimeTypes.length){
			if(this.getAttribute("doExpressInstall")){
				this.addVariable("MMplayerType","PlugIn");
				this.setAttribute("swf",this.xiSWFPath);
				}
			_19="<embed type=\"application/x-shockwave-flash\" src=\""+this.getAttribute("swf")+"\" width=\""+this.getAttribute("width")+"\" height=\""+this.getAttribute("height")+"\" style=\""+this.getAttribute("style")+"\"";
			_19+=" id=\""+this.getAttribute("id")+"\" name=\""+this.getAttribute("id")+"\" ";
			var _1a=this.getParams();for(var key in _1a){
				_19+=[key]+"=\""+_1a[key]+"\" ";
				}
			var _1c=this.getVariablePairs().join("&");
			if(_1c.length>0){
				_19+="flashvars=\""+_1c+"\"";
				}
			_19+="/>";
		}
		else{
			if(this.getAttribute("doExpressInstall")){
				this.addVariable("MMplayerType","ActiveX");
				this.setAttribute("swf",this.xiSWFPath);
				}
			_19="<object id=\""+this.getAttribute("id")+"\" classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" width=\""+this.getAttribute("width")+"\" height=\""+this.getAttribute("height")+"\" style=\""+this.getAttribute("style")+"\">";
			_19+="<param name=\"movie\" value=\""+this.getAttribute("swf")+"\" />";
			var _1d=this.getParams();
			for(var key in _1d){
				_19+="<param name=\""+key+"\" value=\""+_1d[key]+"\" />";
			}
			var _1f=this.getVariablePairs().join("&");
			if(_1f.length>0){
				_19+="<param name=\"flashvars\" value=\""+_1f+"\" />";
			}
			_19+="</object>";
			}
		return _19;
		},
	write:function(_20){
		if(this.getAttribute("useExpressInstall")){
			var _21=new deconcept.PlayerVersion([6,0,65]);
			if(this.installedVer.versionIsValid(_21)&&!this.installedVer.versionIsValid(this.getAttribute("version"))){
				this.setAttribute("doExpressInstall",true);
				this.addVariable("MMredirectURL",escape(this.getAttribute("xiRedirectUrl")));
				document.title=document.title.slice(0,47)+" - Flash Player Installation";
				this.addVariable("MMdoctitle",document.title);
				}
			}
		if(this.skipDetect||this.getAttribute("doExpressInstall")||this.installedVer.versionIsValid(this.getAttribute("version"))){
			var n=(typeof _20=="string")?document.getElementById(_20):_20;n.innerHTML=this.getSWFHTML();
			return true;
			}
		else{
			if(this.getAttribute("redirectUrl")!=""){
				document.location.replace(this.getAttribute("redirectUrl"));
				}
			}
		return false;
		}
	};
deconcept.SWFObjectUtil.getPlayerVersion=function(){
		var _23=new deconcept.PlayerVersion([0,0,0]);
		if(navigator.plugins&&navigator.mimeTypes.length){
			var x=navigator.plugins["Shockwave Flash"];
			if(x&&x.description){
				_23=new deconcept.PlayerVersion(x.description.replace(/([a-zA-Z]|\s)+/,"").replace(/(\s+r|\s+b[0-9]+)/,".").split("."));
				}
			}
		else{
			if(navigator.userAgent&&navigator.userAgent.indexOf("Windows CE")>=0){
				var axo=1;
				var _26=3;
				while(axo){
					try{
						_26++;axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash."+_26);
						_23=new deconcept.PlayerVersion([_26,0,0]);
						}catch(e){
							axo=null;
							}
						}
					}else{
						try{
							var axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.7");
							}
							catch(e){
								try{
									var axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.6");
									_23=new deconcept.PlayerVersion([6,0,21]);
									axo.AllowScriptAccess="always";
									}catch(e){
										if(_23.major==6){
											return _23;
											}
										}
									try{
										axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
										}catch(e){}
									}
									if(axo!=null){
										_23=new deconcept.PlayerVersion(axo.GetVariable("$version").split(" ")[1].split(","));
										}
									}
								}return _23;
							};
deconcept.PlayerVersion=function(_29){
	this.major=_29[0]!=null?parseInt(_29[0]):0;
	this.minor=_29[1]!=null?parseInt(_29[1]):0;
	this.rev=_29[2]!=null?parseInt(_29[2]):0;
	};
deconcept.PlayerVersion.prototype.versionIsValid=function(fv){
	if(this.major<fv.major){
		return false;
		}
	if(this.major>fv.major){
		return true;
	}
	if(this.minor<fv.minor){
		return false;
	}
	if(this.minor>fv.minor){
		return true;
	}
	if(this.rev<fv.rev){
		return false;
		}
	return true;
	};
deconcept.util={
	getRequestParameter:function(_2b){
		var q=document.location.search||document.location.hash;
		if(_2b==null){return q;
	}
	if(q){
		var _2d=q.substring(1).split("&");
		for(var i=0;i<_2d.length;i++){
			if(_2d[i].substring(0,_2d[i].indexOf("="))==_2b){
				return _2d[i].substring((_2d[i].indexOf("=")+1));
				}
			}
		}return "";
	}
};
deconcept.SWFObjectUtil.cleanupSWFs=function(){
	var _2f=document.getElementsByTagName("OBJECT");
	for(var i=_2f.length-1;i>=0;i--){
		_2f[i].style.display="none";
		for(var x in _2f[i]){
			if(typeof _2f[i][x]=="function"){
				_2f[i][x]=function(){};
				}
			}
		}
	};
	if(deconcept.SWFObject.doPrepUnload){
		if(!deconcept.unloadSet){
			deconcept.SWFObjectUtil.prepUnload=function(){
				__flash_unloadHandler=function(){};
				__flash_savedUnloadHandler=function(){};
				window.attachEvent("onunload",deconcept.SWFObjectUtil.cleanupSWFs);
				};
			window.attachEvent("onbeforeunload",deconcept.SWFObjectUtil.prepUnload);
			deconcept.unloadSet=true;
		}
	}
	if(!document.getElementById&&document.all){
		document.getElementById=function(id){return document.all[id];
		};
	}
var getQueryParamValue=deconcept.util.getRequestParameter;
var FlashObject=deconcept.SWFObject;
var SWFObject=deconcept.SWFObject;
