package firstbook;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.LotteryDB;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class DbServlet  extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		String type = req.getParameter("type");
		if (type == null){
			resp.getWriter().println("error: please set 'type'");
			return;
		}
		resp.getWriter().println(fetchMaxfromDB(type));
	}
	
	private String fetchMaxfromDB(String type){
		List<Entity> result = null;
		if (type.equals("take5max")){
			result = LotteryDB.getLotteryDBInstance().QueryMaxtk5Table();
		}
		if (type.equals("numbersmax")){
			result = LotteryDB.getLotteryDBInstance().QueryMaxNumbersTable();
		}
		JSONObject obj = new JSONObject();
		for (int i = 0; i < result.size(); i++){
			String tag = "num " + Integer.toString(i + 1);
			try {
				obj.put(tag, result.get(i).getProperty("Number"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj.toString();
	}
}
