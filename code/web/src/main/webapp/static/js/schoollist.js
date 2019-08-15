        var currentPageID = 3;
    	var currentCity='';
    	var schoolList={};
        $(window).load(function () {
            $('#slider').fadeIn().nivoSlider({
            	pauseOnHover: true,
            	pauseTime: 5000,
                effect: 'fade',
                directionNav: false,
                controlNav: false,
                controlNavThumbs: false
            });
            
            if(typeof $('body').attr("geo")!='undefined')
	      	{
	       	    currentCity=unescape($.trim($('body').attr("geo")));
	      	}
            
            
            initCity(currentCity);
            
            if(currentCity.length>0 && currentCity!='全部')
           	{
           	   getSchoolList(currentCity,16);
           	}
            else
           	{
           	   getAllSchoolList(16);
           	}
           
        });
        
        function initCity(currentCity)
        {
        	var  pc=['全部','北京','上海','天津','重庆','河北','山西','辽宁','吉林','黑龙江','江苏','浙江' ,'安徽' ,'福建','江西',
        	          '山东' ,'河南' ,'湖北','湖南','广东' ,'海南', '四川' ,'贵州','云南' ,'陕西','甘肃',
        	          '青海' ,'台湾','香港','国外'];
        	if(currentCity.length==0)
       		{
        		currentCity='全部';
       		}
        	
            for(var i=0;i<pc.length;i++)
           	{
           	     var city=pc[i];
           	     var eCity= escape(escape(city));
	       		 var elem='';
	       		 
	       		 if(city=='全部')
	       		 {
	       			elem='<a href="javascript:window.location.href=document.location.pathname;">'+  city  +'</a>';
	       		 }
	       		 else
	       		 {
	       			 elem='<a href="?geo='+ eCity +'">'+ city +'</a>';
	       	     }
	       		 
	       		 if(currentCity==city)
	   			 {	
		       	    $("#city-list").append($(elem).attr('class','citySelected'));
	   			 }
	       		 else
    			 {
	       			$("#city-list").append(elem);
    			 }
           	}
        }
        
        function getAllSchoolList(pageSize){
        	var ret=[];
        	$('#school-list').addClass('hardloadingb');
        	$.ajax({
        		url:"listLevelSchool.action",
        		type:"get",
        		dataType:"json",
        		async:true,
        		data:{
        		     },
        		success:function(data){
        			ret = data;
        			schoolList= data;
       				var to=Math.ceil(data.rows.length/pageSize);
       				showSchools(data.rows.slice(0,pageSize));
           			var totalPages = to==0?1:to;
          			    var options = {
          			            currentPage: 1,
          			            totalPages: totalPages,
          			          itemTexts: function (type, page, current) {  
          		                switch (type) {  
          		                    case "first":  
          		                        return "首页";  
          		                    case "prev":  
          		                        return "<";  
          		                    case "next":  
          		                        return ">";  
          		                    case "last":  
          		                        return "末页"+page;  
          		                    case "page":  
          		                        return  page;  
          		                }                 
          		            },
          			            onPageClicked: function(e,originalEvent,type,page){
          			            	showSchools(data.rows.slice((page-1)*pageSize,page*pageSize));
          			            }
          			     };
           	        	
           	       $('#example').bootstrapPaginator(options);
       				
        		},
        		complete:function(){
        			$('#school-list').removeClass('hardloadingb');
        		}
        	});
        	return ret;
        }
        
        function getSchoolList(city,pageSize){
        	var ret=[];
        	$('#school-list').addClass('hardloadingb');
        	$.ajax({
        		url:"listLevelSchoolByName.action",
        		type:"get",
        		dataType:"json",
        		async:true,
        		data:{
        			   'geo':city
        		     },
        		success:function(data){
        			ret = data;
        			schoolList= data;
       				var to=Math.ceil(data.rows.length/pageSize);
       				showSchools(data.rows.slice(0,pageSize));
           			var totalPages = to==0?1:to;
          			    var options = {
          			            currentPage: 1,
          			            totalPages: totalPages,
          			          itemTexts: function (type, page, current) {  
          		                switch (type) {  
          		                    case "first":  
          		                        return "首页";  
          		                    case "prev":  
          		                        return "<";  
          		                    case "next":  
          		                        return ">";  
          		                    case "last":  
          		                        return "末页"+page;  
          		                    case "page":  
          		                        return  page;  
          		                }                 
          		            },
          			            onPageClicked: function(e,originalEvent,type,page){
          			            	showSchools(data.rows.slice((page-1)*pageSize,page*pageSize));
          			            }
          			     };
           	        	
           	       $('#example').bootstrapPaginator(options);
       				
        		},
        		complete:function(){
        			$('#school-list').removeClass('hardloadingb');
        		}
        	});
        	return ret;
        }
        
        function showSchools(videos){
        	//show html
        	var target=$("#school-list");
        	target.empty();
        	var html="";
        	var rows=videos;
        	for(var i=0;i<rows.length;i++){
        		var sc=rows[i];
        		html+='<li class="school-detail"><a href="/university/'+ sc.videoId +'">';
                html+='<img src="upload/pic/school/'+sc.imageUrl+'" class="simg" style="width:201px;height:143px">';
                //html+='<img src="/img/bjdx.jpg" class="simg">';
                html+='</a><h4>'+sc.schoolName+'</h4>';
                //html+='<div><img src="/img/view-times.jpg"><span>8888次</span></div></li>';
                html+='</li>';
        	}
        	target.append(html);
        }