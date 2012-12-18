package firstbook;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;

import util.Quick5Generator;
import util.ResultGenerator;

public class QueryServlet extends HttpServlet {
	private ResultGenerator _generator = null;
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
	//	resp.getWriter().println("Hello, this is QueryPage");
		String type = req.getParameter("category");
		if (type.equals("take5")){
			resp.getWriter().println(QueryTake5(req.getParameter("param1")));
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
}
