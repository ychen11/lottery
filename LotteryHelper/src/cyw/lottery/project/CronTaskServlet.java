package cyw.lottery.project;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import service.LotteryDB;

public class CronTaskServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		updateNumbersReal();
		updateTake5Real();
		LotteryDB.getLotteryDBInstance().updatePickingTables();
	}
	
	private void updateNumbersReal(){
		Document doc = null;
		try {
			doc = Jsoup.connect("http://www.lotteryusa.com/lottery/NY/NY_fcur.html").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element number = doc.getElementsByClass("state-table").get(0).child(1).child(3).getElementsByClass("results").get(0);
		TextNode content = (TextNode) number.childNodes().get(0);
		String res = content.text().replace(", ", "");
		int [] output = new int[3];
		output[0] = res.charAt(0) - '0';
		output[1] = res.charAt(1) - '0';
		output[2] = res.charAt(2) - '0';
		
		Element time = doc.getElementsByClass("state-table").get(0).child(1).child(3).child(2);
		TextNode content2 = (TextNode) time.childNodes().get(0);
		String timeoutput = content2.text();	

		LotteryDB.getLotteryDBInstance().UpdateNumbsWinningTable(output, timeoutput);
		log("Finish numbers update");
	}
	
	private void updateTake5Real(){
		Document doc = null;
		try {
			doc = Jsoup.connect("http://www.lotteryusa.com/lottery/NY/NY_fcur.html").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element number = doc.getElementsByClass("state-table").get(0).child(1).child(6).getElementsByClass("results").get(0);
		TextNode content = (TextNode) number.childNodes().get(0);
		String res = content.text();
		int [] output = new int[5];
		int idx = 0;
		for (int i = 0; i < res.length(); i ++){
			if (res.charAt(i) == ',' || i == res.length() - 1){
				int j = i;
				while (res.charAt(j) != ' ' && j != 0){
					j--;
				}
				String sub;
				if (j == 0)
					sub = res.substring(j, i);
				else{
					if (i != res.length() - 1)
						sub = res.substring(j + 1, i);
					else
						sub = res.substring(j + 1);
				}
				if (idx < output.length){
					output[idx] = Integer.valueOf(sub);
					idx++;
				}
			}
		}
		Element time = doc.getElementsByClass("state-table").get(0).child(1).child(6).child(2);
		TextNode content2 = (TextNode) time.childNodes().get(0);
		String timeoutput = content2.text();
		LotteryDB.getLotteryDBInstance().UpdateTake5WinningTable(output, timeoutput);
		log("Finish take 5 update");
	}
	
	
}
