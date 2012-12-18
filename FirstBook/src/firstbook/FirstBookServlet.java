package firstbook;

import java.io.IOException;
import javax.servlet.http.*;

import util.Quick5Generator;
import util.ResultGenerator;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class FirstBookServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, really");
	//	ResultGenerator rg = new Quick5Generator();
		JSONObject json = new JSONObject();
		try {
			json.put("name0", "Yiwei");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			json.put("age", "25");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.getWriter().println(json.toString());
	}
}
