package project.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class ProjectDao {
	public void showDebug(String msg){
		System.out.println("["+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"][project/dao/Db]"+msg);
	}
	private boolean checkParamValid(JSONObject param,String field) throws JSONException{
		boolean ok=false;
		ok=param.has(field) && param.getString(field)!=null && !param.getString(field).isEmpty() && !param.getString(field).equals("undefined") && !param.getString(field).equals("null");
		return ok;
	}
	/*添加记录*/
	public void addprojectRecord(Data data, JSONObject json) throws JSONException, SQLException {
		//构造sql语句，根据传递过来的条件参数
		String projectId=data.getParam().has("object_id")?data.getParam().getString("object_id"):null;
		String projectName=data.getParam().has("title")?data.getParam().getString("title"):null;
		String projectType=data.getParam().has("content")?data.getParam().getString("content"):null;
		String createTime=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
		if(projectId!=null && projectName!=null){
			String sql="insert into project_todo(object_id,title,content,used_tag,creator_id,creator,create_time)";
			sql=sql+" values('"+projectId+"'";
			sql=sql+" ,'"+projectName+"','"+projectType+"',1,'20220000000000001','student','"+createTime+"')";
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*删除记录*/
	public void deleteprojectRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		if(id!=null){
			String sql="delete from project_todo where id="+data.getParam().getString("id");
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*修改记录*/
	public void modifyprojectRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		String projectId=data.getParam().has("object_id")?data.getParam().getString("object_id"):null;
		String projectName=data.getParam().has("title")?data.getParam().getString("title"):null;
		String projecttext=data.getParam().has("content")?data.getParam().getString("content"):null;
		if(id!=null){
			String sql="update project_todo";
			sql=sql+" set object_id='"+projectId+"'";
			sql=sql+" ,title='"+projectName+"'";
			sql=sql+" ,content='"+projecttext+"'";
			sql=sql+" where id="+id;
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*查询记录*/
	public void getprojectRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的查询条件参数
		String sql=createGetRecordSql(data);			//构造sql语句，根据传递过来的查询条件参数
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	/*
	 * 这是一个样板的函数，可以拷贝做修改用
	 */
	private void updateRecord(Data data,JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		JSONObject param=data.getParam();
		int resultCode=0;
		String resultMsg="ok";
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		Db updateDb = new Db("web");
		String sql=data.getParam().getString("sql");
		showDebug("[updateRecord]"+sql);
		updateDb.executeUpdate(sql);
		updateDb.close();
		/*--------------------数据操作 结束--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);														//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}
	private void queryRecord(Data data,JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		String resultMsg = "ok";
		int resultCode = 0;
		List jsonList = new ArrayList();
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		Db queryDb = new Db("web");
		String sql=data.getParam().getString("sql");
		showDebug("[queryRecord]构造的SQL语句是：" + sql);
		try {
			ResultSet rs = queryDb.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int fieldCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 0; i < fieldCount; i++) {
					map.put(rsmd.getColumnName(i + 1), rs.getString(rsmd.getColumnName(i + 1)));
				}
				jsonList.add(map);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			showDebug("[queryRecord]查询数据库出现错误：" + sql);
			resultCode = 10;
			resultMsg = "查询数据库出现错误！" + e.getMessage();
		}
		queryDb.close();
		/*--------------------数据操作 结束--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("aaData",jsonList);
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);														//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}

	private String createGetRecordSql(Data data) throws JSONException {
		JSONObject param=data.getParam();
		String sql="select * from project_todo";
		String where="";
		if(checkParamValid(param,"id")){
			where="id="+param.getString("id");
		}
		if(checkParamValid(param,"id")){
			where="id="+param.getString("id");
		}
		if(checkParamValid(param,"time_from") && checkParamValid(param,"time_to")){
			if(!where.isEmpty()){
				where=where+" and create_time between '"+param.getString("time_from")+"' and '"+param.getString("time_to")+"'";
			}else{
				where="create_time between '"+param.getString("time_from")+"' and '"+param.getString("time_to")+"'";
			}
		}
		if(checkParamValid(param,"object_id")){
			if(!where.isEmpty()){
				where=where+" and object_id = '"+param.getString("object_id")+"'";
			}else{
				where="object_id ='"+param.getString("object_id")+"'";
			}
		}
		if(checkParamValid(param,"title")){
			if(!where.isEmpty()){
				where=where+" and title = '"+param.getString("title")+"'";
			}else{
				where="title ='"+param.getString("title")+"'";
			}
		}
		if(checkParamValid(param,"content")){
			if(!where.isEmpty()){
				where=where+" and content like '"+param.getString("content")+"'";
			}else{
				where="content like '"+param.getString("content")+"'";
			}
		}
		if(!where.isEmpty()){
			sql=sql+" where "+where;
		}
		if(checkParamValid(param,"order_by")){
			sql=sql+" order by "+param.getString("order_by");
		}
		return sql;
	}
}
