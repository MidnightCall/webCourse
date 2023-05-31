var module="project";
var sub="file";
/*================================================================================*/
jQuery(document).ready(function() {
	Page.init();
});
/* ================================================================================ */
//关于页面的控件生成等操作都放在Page里
var Page = function() {
	/*----------------------------------------入口函数  开始----------------------------------------*/
	var initPageControl=function(){
		pageId=$("#page_id").val();
		if(pageId=="project_list"){
			initprojectList();
		}
		if(pageId=="project_add"){
			initprojectAdd();
		}
		if(pageId=="project_modify"){
			initprojectModify();
		}
	};
	/*----------------------------------------入口函数  结束----------------------------------------*/
	var columnsData=undefined;
	var recordResult=undefined;
	/*----------------------------------------业务函数  开始----------------------------------------*/
	/*------------------------------针对各个页面的入口  开始------------------------------*/
	var initprojectList=function(){
		initprojectListControlEvent();
		initprojectRecordList();
	}
	var initprojectAdd=function(){
		initprojectAddControlEvent();
	}
	var initprojectModify=function(){
		initprojectModifyControlEvent();
		initprojectRecordView();
	}
	/*------------------------------针对各个页面的入口 结束------------------------------*/
	var getUrlParam=function(name){
		//获取url中的参数
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
		var r = window.location.search.substr(1).match(reg);  //匹配目标参数
		if (r != null) return decodeURI(r[2]); return null; //返回参数值，如果是中文传递，就用decodeURI解决乱码，否则用unescape
	}
	var initprojectListControlEvent=function(){
		$("#help_button").click(function() {help();});
		$('#add_button').click(function() {onAddRecord();});
	}
	var initprojectAddControlEvent=function(){
		$("#help_button").click(function() {help();});
		$('#add_button').click(function() {submitAddRecord();});
	}
	var initprojectModifyControlEvent=function(){
		$("#help_button").click(function() {help();});
		$('#modify_button').click(function() {submitModifyRecord();});
	}
	var initprojectRecordView=function(){
		var id=getUrlParam("id");
		var data={};
		data.action="get_project_record";
		data.id=id;
		$.post(module+"_"+sub+"_servlet_action",data,function(json){
			console.log(JSON.stringify(json));
			if(json.result_code==0){
				var list=json.aaData;
				if(list!=undefined && list.length>0){
					for(var i=0;i<list.length;i++){
						var record=list[i];
						$("#object_id").val(record.object_id);
						$("#title").val(record.title);
						$("#content").val(record.content);
					}
				}
			}
		})
	}
	var onAddRecord=function(){
		window.location.href="project_add.jsp";
	}
	var submitAddRecord=function(){
		var url="project_file_servlet_action";
		var data={};
		data.action="add_project_record";
		data.object_id=$("#object_id").val();
		data.title=$("#title").val();
		data.content=$("#content").val();
		$.post(url,data,function(json){
			if(json.result_code==0){
				alert("已经完成事件添加。");
				window.location.href="project_list.jsp";
			}
		});
	}
	var submitModifyRecord=function(){
		if(confirm("您确定要修改该记录吗？")){
			var id=getUrlParam("id");
			var url="project_file_servlet_action";
			var data={};
			data.action="modify_project_record";
			data.id=id;
			data.object_id=$("#object_id").val();
			data.title=$("#title").val();
			data.content=$("#content").val();
			$.post(url,data,function(json){
				if(json.result_code==0){
					alert("已经完成事件修改。");
					window.location.href="project_list.jsp";
				}
			});
		}
	}

	
	var initprojectRecordList=function(){
		getprojectRecordList();
	}
	var initprojectMobileRecord=function(){
		getprojectMobileRecord();
	}
	var getprojectRecordList=function(){
		$.post(module+"_"+sub+"_servlet_action?action=get_project_record",function(json){
			console.log(JSON.stringify(json));
			if(json.result_code==0){
				var list=json.aaData;
				var html="";
				if(list!=undefined && list.length>0){
					for(var i=0;i<list.length;i++){
						var record=list[i];
						html=html+"<div>序号："+i+"<div>";
						html=html+"<div>事项ID："+record.object_id+"<div>";
						html=html+"<div>事项名称："+record.title+"<div>";
						html=html+"<div>详细信息："+record.content+"<div>";
						html=html+"<div><a href=\"javascript:Page.onModifyRecord("+record.id+")\">【修改记录】</a><a href=\"javascript:Page.onDeleteRecord("+record.id+")\">【删除记录】</a><div>";
						html=html+"<p>";
					}
				}
				$("#record_list_div").html(html);
			}
		})
	}
	var onDeleteRecord = function(id){
		if(confirm("您确定要删除这条记录吗？")){
			if(id>-1){
				var url="project_file_servlet_action";
				var data={};
				data.action="delete_project_record";
				data.id=id;
				$.post(url,data,function(json){
					if(json.result_code==0){
						getprojectRecordList();
					}
				})
			}
		}
	};
	var onModifyRecord=function(id){
		window.location.href="project_modify.jsp?id="+id;
	}
	//Page return 开始
	return {
		init: function() {
			initPageControl();
		},
		onDeleteRecord:function(id){
			onDeleteRecord(id);
		},
		onModifyRecord:function(id){
			onModifyRecord(id);
		}
	}
}();//Page
/*================================================================================*/
