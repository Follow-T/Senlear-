function zancomment(obj){
	
	if($(obj).parent().hasClass("zanok")){
		 layer.msg("您已经点赞过了!", { icon: 2, time: 1000 });
		 return ;
	}
	
	var username=$('.fly-nav-user li').attr('username');
	if(username==null){
		layer.msg("请登录后再进行点赞操作!", { icon: 2, time: 1000 });
	}else{
		var commentid = $(obj).parent().parent().attr("commentid");
		var replyid = $(obj).parent().parent().attr("replyid");
		var zan =parseInt($(obj).next().text())+parseInt(1) ;
		var formData = new FormData();
	    formData.append('username', username);
	    if(replyid==null){
	    formData.append('commentid', commentid);
	    $.ajax({
	        url: '/'+username+'/likeComment',
	        type: 'POST',
	        data: formData,
	        //这两个设置项必填
	        contentType: false,
	        processData: false,
	        dataType: 'jsonp',
	        calback: "callback",
	        cache: false,
	        success: function (data) {
	          console.log(data);
	          if (data['status'] == '200') {
	            layer.msg("点赞成功", { icon: 1, time: 1000 });
	            $(obj).next().text(zan);
	            $(obj).parent().addClass("zanok");
	          } else {
	            layer.msg('点赞失败!', { icon: 2, time: 1000 });
	            //console.log(data);
	          }
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	          alert(XMLHttpRequest.status);
	          alert(XMLHttpRequest.readyState);
	          alert(textStatus);
	        }
	      });
	    }else{
	    	formData.append('replyid', replyid);
	    	$.ajax({
		        url: '/'+username+'/likeReply',
		        type: 'POST',
		        data: formData,
		        //这两个设置项必填
		        contentType: false,
		        processData: false,
		        dataType: 'jsonp',
		        calback: "callback",
		        cache: false,
		        success: function (data) {
		          console.log(data);
		          if (data['status'] == '200') {
		            layer.msg("点赞成功", { icon: 1, time: 1000 });
		            $(obj).next().text(zan);
		            $(obj).parent().addClass("zanok");
		          } else {
		            layer.msg('点赞失败!', { icon: 2, time: 1000 });
		            //console.log(data);
		          }
		        },
		        error: function (XMLHttpRequest, textStatus, errorThrown) {
		          alert(XMLHttpRequest.status);
		          alert(XMLHttpRequest.readyState);
		          alert(textStatus);
		        }
		      });
	    }
	}
	
}
function reply(){
	
	var username=$('.fly-nav-user li').attr('username');
	if(username==null){
		layer.msg("请登录后再进行点赞操作!", { icon: 2, time: 500 });
	}else{
		var tousername=$("#L_content").attr("touser");
		var commentid=$("#L_content").attr("commentid");
		var articleid=$("#jieda").attr("articleid");
		if(commentid==null){
			var formData = new FormData();
		    formData.append('content', $("#L_content").val());
		    formData.append('username', username);
		    formData.append('articleid', articleid);
			$.ajax({
		        url: '/'+username+'/submitComment',
		        type: 'POST',
		        data: formData,
		        //这两个设置项必填
		        contentType: false,
		        processData: false,
		        dataType: 'jsonp',
		        calback: "callback",
		        cache: false,
		        success: function (data) {
		          console.log(data);
		          if (data['status'] == '200') {
		            layer.msg("评论成功", { icon: 1, time: 1000 });
		           var html="";
		            
		            //$("li[commentid="+commentid+"]").append(html);
		            $("#L_content").val("");
		            window.location.reload();
		          } else {
		            layer.msg('评论失败!', { icon: 2, time: 1000 });
		            //console.log(data);
		          }
		        },
		        error: function (XMLHttpRequest, textStatus, errorThrown) {
		          alert(XMLHttpRequest.status);
		          alert(XMLHttpRequest.readyState);
		          alert(textStatus);
		        }
		      });
		}else{
			var formData = new FormData();
		    formData.append('content', $("#L_content").val());
		    formData.append('username', username);
		    formData.append('commentid', commentid);
		    formData.append('tousername', tousername);
		    
			$.ajax({
		        url: '/reply/'+username+'',
		        type: 'POST',
		        data: formData,
		        //这两个设置项必填
		        contentType: false,
		        processData: false,
		        dataType: 'jsonp',
		        calback: "callback",
		        cache: false,
		        success: function (data) {
		          console.log(data);
		          if (data['status'] == '200') {
		            layer.msg("评论成功", { icon: 1, time: 1000 });
		           var html="";
		            
		           //获取li标签并且commentid=commentid
		            $("li[commentid="+commentid+"]").append(html);
		            
		            $("#L_content").removeAttr("touser");
		            $("#L_content").removeAttr("commentid");
		            $("#L_content").val("");
		            window.location.reload();
		          } else {
		            layer.msg('评论失败!', { icon: 2, time: 1000 });
		            //console.log(data);
		          }
		        },
		        error: function (XMLHttpRequest, textStatus, errorThrown) {
		          alert(XMLHttpRequest.status);
		          alert(XMLHttpRequest.readyState);
		          alert(textStatus);
		        }
		      });
		}
	}
}


