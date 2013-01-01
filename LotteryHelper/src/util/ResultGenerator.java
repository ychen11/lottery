package util;

import java.util.Random;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public abstract class ResultGenerator {
	protected int _generatedNumber;
	protected int GenerateNumber(int from, int to){
		if (from >= to){
			throw new IllegalArgumentException("Out of Range");
		}
		Random rdmGen = new Random();
		int range = to - from + 1;
		int result = 0;
		result = from + rdmGen.nextInt(range);
		return result;
	}
	
	abstract void GenNumberSequence();
	public abstract JSONObject getNumbers() throws JSONException;
}
