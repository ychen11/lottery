package util;

import java.util.ArrayList;
import java.util.Hashtable;

import service.LotteryDB;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class Quick5Generator extends ResultGenerator {

	private int _roundNumber = 0;
	private final int _rangeFrom = 1;
	private final int _rangeTo = 39;
	private ArrayList<ArrayList<Integer>> _numberList = null; 
	private JSONObject _response = null;
	
	public Quick5Generator(int roundnumber){
		_roundNumber = roundnumber;
		_numberList = new ArrayList<ArrayList<Integer>>();
		_response = new JSONObject();
	}
	
	@Override
	void GenNumberSequence() {
		Hashtable<Integer, Integer> map= new Hashtable<Integer, Integer>();
		map.clear();
		while(_roundNumber > 0){
			ArrayList<Integer> numbers = new ArrayList<Integer>();
			for(int i = 0; i < 5; i++){
				int value = this.GenerateNumber(_rangeFrom, _rangeTo);
				if (map.containsValue(value) == false){
					map.put(i, value);
					numbers.add(value);
				}else{
					int newValue = this.GenerateNumber(_rangeFrom, _rangeTo);
					while (map.containsValue(newValue) == true){
						newValue = this.GenerateNumber(_rangeFrom, _rangeTo);
					}
					map.put(i, newValue);
					numbers.add(newValue);
				}
			}
			this._numberList.add(numbers);
			_roundNumber--;
		}
	}
	
	@Override
	public JSONObject getNumbers() throws JSONException{
		GenNumberSequence();
		if (_numberList.isEmpty() == true){
			this._response.put("status", "empty");
			return _response;
		}
		this._response.put("status", "full");
		JSONArray numberArray = new JSONArray();
		updateDB();
		for (int i = 0; i < _numberList.size(); i++){
			numberArray.put(_numberList.get(i));
		}
		_response.put("result", numberArray);
		_response.put("category", "take 5");
		return _response;
	}
	
	private void updateDB(){
		LotteryDB.getLotteryDBInstance().updateTake5Table(this._numberList);
	}
}
