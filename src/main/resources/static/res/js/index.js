	layui.use(['form'], function () {
    var $ = layui.jquery;
    
    $("#l").click(function weather(){
		$("#wea").attr("style","display: block;");
		$("#fit").attr("style","display: none;");
		$("#word").attr("style","display: none;");
		$(this).attr("style"," cursor:pointer;color:rgb(95, 184, 120)");
		$("#w").attr("style","cursor:pointer;color:rgb(1, 170, 237)");
		$("#t").attr("style","cursor:pointer;color:rgb(1, 170, 237)");
	})
	
	$("#w").click(function fit(){
		$("#wea").attr("style","display: none;");
		$("#fit").attr("style","display: block;");
		$("#word").attr("style","display: none;");
		$(this).attr("style","cursor:pointer;color:rgb(95, 184, 120)");
		$("#l").attr("style","cursor:pointer;color:rgb(1, 170, 237)");
		$("#t").attr("style","cursor:pointer;color:rgb(1, 170, 237)");
	})
	
	$("#t").click(function word(){
		$("#wea").attr("style","display: none;");
		$("#fit").attr("style","display: none;");
		$("#word").attr("style","display: block;");
		$(this).attr("style","cursor:pointer;color:rgb(95, 184, 120)");
		$("#l").attr("style","cursor:pointer;color:rgb(1, 170, 237)");
		$("#w").attr("style","cursor:pointer;color:rgb(1, 170, 237)");
	})
	
    var city=returnCitySN['cname'];
    
    $.ajax({
        url: 'https://www.tianqiapi.com/api/?version=v1&'+city,
        type: 'GET',
        data: "",
        //这两个设置项必填
        contentType: false,
        processData: false,
        dataType: 'jsonp',
        calback: "callback",
        cache: false,
        success: function (data) {
         	$("#city").text("当前城市:"+data['city']);
         	$("#date").text("日期:"+data['data']['0']['date']+" "+data['data']['0']['week']);
         	$("#weather").text("天气:"+data['data']['0']['wea']);
         	$("#tem").text("当前温度:"+data['data']['0']['tem']);
         	$("#content1").text(data['data']['0']['air_tips']);
         	$("#content2").text("每天都要记得给自己一个微笑.");
          　　		console.log(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	 layer.msg('网络错误!',{icon:2,time:1000});
        }
  	});
    
    
    var pageStart=0;
    var pageSize=10;
    var preurl = document.location.toString();
	var arrUrl = preurl.split("//");
	var key=arrUrl[1].split("/")[1];
	if(key=="index"||key=="index#type=article"){
		var pre="#fly-list-article";
		var id='#1';
		$(id).addClass("layui-this"); 
		$(pre).attr("style","display: block;");
		$("#classfiy").text("综合");
		$("#classfiy").attr("type","0");
	}else if(key=="index#type=question"){
		var pre="#fly-list-question";
		var id='#2';
		$(id).addClass("layui-this"); 
		$(pre).attr("style","display: block;");
		$("#classfiy").text("提问");
		$("#classfiy").attr("type","1");
	}else if(key=="index#type=share"){
		var pre="#fly-list-share";
		var id='#3';
		$(id).addClass("layui-this"); 
		$(pre).attr("style","display: block;");
		$("#classfiy").text("分享");
		$("#classfiy").attr("type","2");
	}else if(key=="index#type=advice"){
		var pre="#fly-list-advice";
		var id='#4';
		$(id).addClass("layui-this"); 
		$(pre).attr("style","display: block;");
		$("#classfiy").text("建议");
		$("#classfiy").attr("type","3");
	}else if(key=="index#type=life"){
		var pre="#fly-list-life";
		var id='#5';
		$(id).addClass("layui-this"); 
		$(pre).attr("style","display: block;");
		$("#classfiy").text("记录生活");
		$("#classfiy").attr("type","4");
	}else if(key=="index#type=resource"){
		var pre="#fly-list-resource";
		var id='#6';
		$(id).addClass("layui-this"); 
		$(pre).attr("style","display: block;");
		$("#classfiy").text("资源分享");
		$("#classfiy").attr("type","5");
	}else if(key=="index#type=joke"){
		var pre="#fly-list-joke";
		var id='#7';
		$(id).addClass("layui-this"); 
		$(pre).attr("style","display: block;");
		$("#classfiy").text("开心一刻");
		$("#classfiy").attr("type","6");
	}else if(key=="index#type=science"){
		var pre="#fly-list-science";
		var id='#8';
		$(id).addClass("layui-this"); 
		$(pre).attr("style","display: block;");
		$("#classfiy").text("科技资讯");
		$("#classfiy").attr("type","7");
	}else if(key=="index#type=other"){
		var pre="#fly-list-other";
		var id='#9';
		$(id).addClass("layui-this"); 
		$(pre).attr("style","display: block;");
		$("#classfiy").text("其他");
		$("#classfiy").attr("type","8");
	}else{
		
	}
    $("#index").click(function(){
    	pageStart=0;
    	window.location.href ="/index#type=article"
    	$("#hostest").removeClass("layui-this");
    	$("#newest").addClass("layui-this");
    	$(id).removeClass("layui-this");
    	$("#1").addClass("layui-this");
    	id="#1";
    	$(pre).attr("style","display: none;");
    	pre="#fly-list-article";
    	$(pre).attr("style","display: block;");
    	$("#classfiy").text("综合");
    	$("#classfiy").attr("type","0");
    })
    $("#question").click(function(){
    	pageStart=0;
    	window.location.href ="/index#type=question"
    	$("#hostest").removeClass("layui-this");
    	$("#newest").addClass("layui-this");
    	$(id).removeClass("layui-this");
    	$("#2").addClass("layui-this");
    	id="#2";
    	$(pre).attr("style","display: none;");
    	pre="#fly-list-question";
    	$(pre).attr("style","display: block;");
    	$("#classfiy").text("提问");
    	$("#classfiy").attr("type","1");
    })
    $("#share").click(function(){
    	pageStart=0;
    	window.location.href ="/index#type=share"
    	$("#hostest").removeClass("layui-this");
    	$("#newest").addClass("layui-this");
    	$(id).removeClass("layui-this");
    	$("#3").addClass("layui-this");
    	id="#3";
    	$(pre).attr("style","display: none;");
    	pre="#fly-list-share";
    	$(pre).attr("style","display: block;");
    	$("#classfiy").text("分享");
    	$("#classfiy").attr("type","2");
    })
    $("#advice").click(function(){
    	pageStart=0;
    	window.location.href ="/index#type=advice"
    	$("#hostest").removeClass("layui-this");
    	$("#newest").addClass("layui-this");
    	$(id).removeClass("layui-this");
    	$("#4").addClass("layui-this");
    	id="#4";
    	$(pre).attr("style","display: none;");
    	pre="#fly-list-advice";
    	$(pre).attr("style","display: block;");
    	$("#classfiy").text("建议");
    	$("#classfiy").attr("type","3");
    })
    $("#life").click(function(){
    	pageStart=0;
    	window.location.href ="/index#type=life"
    	$("#hostest").removeClass("layui-this");
    	$("#newest").addClass("layui-this");
    	$(id).removeClass("layui-this");
    	$("#5").addClass("layui-this");
    	id="#5";
    	$(pre).attr("style","display: none;");
    	pre="#fly-list-life";
    	$(pre).attr("style","display: block;");
    	$("#classfiy").text("记录生活");
    	$("#classfiy").attr("type","4");
    })
    $("#resource").click(function(){
    	pageStart=0;
    	window.location.href ="/index#type=resource"
    	$("#hostest").removeClass("layui-this");
    	$("#newest").addClass("layui-this");
    	$(id).removeClass("layui-this");
    	$("#6").addClass("layui-this");
    	id="#6";
    	$(pre).attr("style","display: none;");
    	pre="#fly-list-resource";
    	$(pre).attr("style","display: block;");
    	$("#classfiy").text("资源分享");
    	$("#classfiy").attr("type","5");
    })
    $("#joke").click(function(){
    	pageStart=0;
    	window.location.href ="/index#type=joke"
    	$("#hostest").removeClass("layui-this");
    	$("#newest").addClass("layui-this");
    	$(id).removeClass("layui-this");
    	$("#7").addClass("layui-this");
    	id="#7";
    	$(pre).attr("style","display: none;");
    	pre="#fly-list-joke";
    	$(pre).attr("style","display: block;");
    	$("#classfiy").text("开心一刻");
    	$("#classfiy").attr("type","6");
    })
    $("#science").click(function(){
    	pageStart=0;
    	window.location.href ="/index#type=science"
    	$("#hostest").removeClass("layui-this");
    	$("#newest").addClass("layui-this");
    	$(id).removeClass("layui-this");
    	$("#8").addClass("layui-this");
    	id="#8";
    	$(pre).attr("style","display: none;");
    	pre="#fly-list-science";
    	$(pre).attr("style","display: block;");
    	$("#classfiy").text("科技资讯");
    	$("#classfiy").attr("type","7");
    })
    $("#other").click(function(){
    	pageStart=0;
    	window.location.href ="/index#type=other"
    	$("#hostest").removeClass("layui-this");
    	$("#newest").addClass("layui-this");
    	$(id).removeClass("layui-this");
    	$("#9").addClass("layui-this");
    	id="#9";
    	$(pre).attr("style","display: none;");
    	pre="#fly-list-other";
    	$(pre).attr("style","display: block;");
    	$("#classfiy").text("综其他");
    	$("#classfiy").attr("type","8");
    })
    
    $("#newest").click(function(){
    	$("#hostest").removeClass("layui-this");
    	$("#newest").addClass("layui-this");
    	var type=$("#classfiy").attr("type");
    	$("#page").attr("sort","1");
    	var formData = new FormData();
    	formData.append('type',type);
        formData.append('sortway',"1");
        formData.append('pageStart',pageStart);
        formData.append('pageSize',pageSize);
        GetArticle(formData,"#fly-list-sort",0);
    	
    });
    
	$("#hostest").click(function(){
    	$("#hostest").addClass("layui-this");
    	$("#newest").removeClass("layui-this");
    	var type=$("#classfiy").attr("type");
    	$("#page").attr("sort","2");
    	var formData = new FormData();
    	formData.append('type',type);
        formData.append('sortway',"2");
        formData.append('pageStart',pageStart);
        formData.append('pageSize',pageSize);
        GetArticle(formData,"#fly-list-sort",0);
    	
    });
	
	$("#LastPage").click(function(){
		if(pageStart==0){
			layer.msg('文章已经到第一页啦!',{icon:2,time:1000});
			return;
		}
		var type=$("#classfiy").attr("type");
	    var sort=$("#page").attr("sort");
	    var formData = new FormData();
        formData.append('type',type);
        formData.append('sortway',sort);
        formData.append('pageStart',pageStart-10);
        formData.append('pageSize',pageSize);
        GetArticle(formData,"#fly-list-page",2);
	});
	
	$("#NextPage").click(function(){
		
		var type=$("#classfiy").attr("type");
	    var sort=$("#page").attr("sort");
	    var formData = new FormData();
        formData.append('type',type);
        formData.append('sortway',sort);
        formData.append('pageStart',pageStart+10);
        formData.append('pageSize',pageSize);
        GetArticle(formData,"#fly-list-page",1);
	});
	
    function GetArticle(formData,box,judge)
    {	
        $.ajax({
            url: '/articles',
            type: 'POST',
            data: formData,
            //这两个设置项必填
            contentType: false,
            processData: false,
            dataType: 'jsonp',
            calback: "callback",
            cache: false,
            success: function (data) {
             	
              　　		  if(data['status']=='200'){
              　　			  if(judge==1){
              　　				  
              　　				  pageStart=pageStart+10;
              　　				 alert(pageStart);
              　　			  }else if(judge==2){
              　　				  pageStart=pageStart-10;
              　　				  alert(pageStart);
              　　			  }
              　　			  
              　　			  $(box).empty();
              　　			  $(pre).attr("style","display: none;");
              　　			    pre=box;
              　　			    $(pre).attr("style","display: block;");
              　　			    
              　　			    console.log(data['data']);
              　　			 var articleList = JSON.parse(data['data']);
              　　			 
              　　			 
                       //http://localhost:8080/945614282@qq.com/article/4
                        for(var i=0;i<articleList.length;i++){
                        	var type;
                        	if(articleList[i]['type']=="1"){
                        		type="资源";
                        	}else if(articleList[i]['type']=="2"){
                        		type="分享";
                        	}else if(articleList[i]['type']=="3"){
                        		type="建议";
                        	}else if(articleList[i]['type']=="4"){
                        		type="记录生活";
                        	}else if(articleList[i]['type']=="5"){
                        		type="资源分享";
                        	}else if(articleList[i]['type']=="6"){
                        		type="开心一刻";
                        	}else if(articleList[i]['type']=="7"){
                        		type="科技资讯";
                        	}else if(articleList[i]['type']=="8"){
                        		type="其他";
                        	}
                        	var html = " <li>\n" +
                            "            <a href=\"user/home.html\" class=\"fly-avatar\">" +
                            "              <img src=\""+articleList[i]['user']['avatar']+"\" alt=\"贤心\">" +
                            "            </a>" +
                            "            <h2>" +
                            "              <a class=\"layui-badge\">"+type+"</a>" +
                            "              <a href=\"/"+articleList[i]['user']['username']+"/article/"+articleList[i]['id']+"\">"+articleList[i]['title']+"</a>" +
                            "            </h2>" +
                            "            <div class=\"fly-list-info\">" +
                            "              <a href=\"user/home.html\" link>" +
                            "                <cite>"+articleList[i]['user']['name']+"</cite>" +
                            "                <!--" +
                            "                <i class=\"iconfont icon-renzheng\" title=\"认证信息：XXX\"></i>" +
                            "                <i class=\"layui-badge fly-badge-vip\">VIP3</i>\n" +
                            "                -->" +
                            "              </a>" +
                            "              <span>"+articleList[i]['createTime']+"</span>" +
                            "              " +
                            "              <span class=\"fly-list-kiss layui-hide-xs\" title=\"悬赏飞吻\"><i class=\"iconfont icon-kiss\"></i> 60</span>" +
                            "              <!--<span class=\"layui-badge fly-badge-accept layui-hide-xs\">已结</span>-->" +
                            "              <span class=\"fly-list-nums\">" +
                            "                <i class=\"iconfont icon-pinglun1\" title=\"回答\"></i> "+articleList[i]['commentsize']+"" +
                            "              </span>" +
                            "            </div>" +
                            "            <div class=\"fly-list-badge\">" +
                            "              <!--<span class=\"layui-badge layui-bg-red\">精帖</span>-->" +
                            "            </div>" +
                            "          </li>";
                            $(box).append(html);
                          }
                        setTimeout(function () {
                        }, 1000);
                        
              }else{
            	  layer.msg('文章已经见底啦!',{icon:2,time:1000});
              }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            	 layer.msg('网络错误!',{icon:2,time:1000});
            }
      	});
    }
  });
layui.cache.page = '';
layui.cache.user = {
  username: '游客'
  ,uid: -1
  ,avatar: '../res/images/avatar/00.jpg'
  ,experience: 83
  ,sex: '男'
};
layui.config({
  version: "3.0.0"
  ,base: '/res/mods/' //这里实际使用时，建议改成绝对路径
}).extend({
  fly: 'index'
}).use('fly');
