package util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class Quick5Generator extends ResultGenerator {

	private int _roundNumber = 0;
	private final int _rangeFrom = 1;
	private final int _rangeTo = 39;
	private ArrayList<ArrayList<Integer>> _numberList = null; 
	
	public Quick5Generator(int roundnumber){
		_roundNumber = roundnumber;
		_numberList = new ArrayList<ArrayList<Integer>>();
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
		}
	}
	
	@Override
	JSONObject getNumbers() {
		
		return null;
	}
	
	
}
