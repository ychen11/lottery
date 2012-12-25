package firstbook;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import service.LotteryDB;
import util.NumbersGenerator;
import util.Quick5Generator;
import util.ResultGenerator;

public class QueryServlet extends HttpServlet {
	private ResultGenerator _generator = null;
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
	//	resp.getWriter().println("Hello, this is QueryPage");
		String type = req.getParameter("category");
		if (type == null){
			resp.getWriter().println("error: please set 'category' and 'param1'");
			return;
		}
		if (type.equals("take5")){
			resp.getWriter().println(QueryTake5(req.getParameter("param1")));
		}
		if (type.equals("numbers")){
			resp.getWriter().println(QueryNumbers(req.getParameter("param1")));
		}
	}
	
	private String QueryTake5(String param){
		if (!param.isEmpty()){
			this._generator = new Quick5Generator(Integer.parseInt(param));
			try {
				return this._generator.getNumbers().toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "Need round number";
	}
	
	private String QueryNumbers(String param){
		if (!param.isEmpty()){
			this._generator = new NumbersGenerator(Integer.parseInt(param));
			try {
				return this._generator.getNumbers().toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "Need round number";
	}
}
