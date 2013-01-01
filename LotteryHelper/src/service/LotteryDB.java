package service;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class LotteryDB {
	
	private static LotteryDB myInstance;
	private static ArrayList<Key> _tk5Key;
	private static ArrayList<Key> _numbersKey;
	private static Key _tk5WinKey;
	private static ArrayList<Key> _numbersWinKey;
	private static DatastoreService _dataStore;
	
	private static String _twnameTk5 = "Take 5 Win";
	private static String _twnameNumbers = "NumbersWin";
	private static String _tnameTk5 = "Take 5";
	private static String _tnameNumbers = "Numbers";
	
	private LotteryDB(){
		_tk5Key = new ArrayList<Key>();
		_numbersKey = new ArrayList<Key>();
		_tk5WinKey = null;
		_numbersWinKey = new ArrayList<Key>();
		_dataStore = DatastoreServiceFactory.getDatastoreService();
	}
	
	public static LotteryDB getLotteryDBInstance(){
		if (myInstance == null)
			myInstance = new LotteryDB();
		return myInstance;
	}
	
	private void InitTk5Table(){
		for (int i = 1; i < 40; i++){
			Entity tk5Entity = new Entity("Take 5");
			tk5Entity.setProperty("Number", i);
			tk5Entity.setProperty("count", 0);
			tk5Entity.setProperty("Winning", 0.0);
			_dataStore.put(tk5Entity);
			_tk5Key.add(tk5Entity.getKey());
		}
	}
	
	private void InitNmbsTable(){
		for (int i = 0; i < 10; i++){
		//	Key k = KeyFactory.createKey("Numbers");
			Entity nmbsEntity = new Entity("Numbers");
			nmbsEntity.setProperty("Number", i);
			nmbsEntity.setProperty("count", 0);
			nmbsEntity.setProperty("Winning", 0.0);
			_dataStore.put(nmbsEntity);
			_numbersKey.add(nmbsEntity.getKey());
		}
	}
	
	private void getKeysFromTable(String tableName){
		Query q = new Query(tableName).addSort("Number", SortDirection.ASCENDING);
		PreparedQuery pq = _dataStore.prepare(q);
		List<Entity> res = pq.asList(FetchOptions.Builder.withDefaults());
		if (tableName.equals(_tnameTk5)){
			for (int i = 0; i < res.size(); i++){
				this._tk5Key.add(res.get(i).getKey());
			}
		}
		if (tableName.equals(_tnameNumbers)){
			for (int i = 0; i < res.size(); i++){
				this._numbersKey.add(res.get(i).getKey());
			}
		}
	}
	
	private List<Entity> fetchWinningTables(String tableName){
		Query q = new Query(tableName);
		PreparedQuery pq = _dataStore.prepare(q);
		List<Entity> res = pq.asList(FetchOptions.Builder.withDefaults());
		return res;
	}
	
	private void updatePickingTables(int [] source, String tableName){
		if (source.length == 0){
			System.out.println("Empty source data from winning table");
			return;
		}
		int sum = 0;
		for (int i = 0; i < source.length; i++){
			sum += source[i];
		}
		for (int i = 0; i < source.length; i++){
			double p = ((double)source[i])/sum;
			Filter filter = new Query.FilterPredicate("Number", FilterOperator.EQUAL, i);
			Query q = new Query(tableName).setFilter(filter);
			PreparedQuery pq = _dataStore.prepare(q);
			List<Entity> res = pq.asList(FetchOptions.Builder.withDefaults());
			if (res.size() != 0){
				res.get(0).setProperty("Winning", p);
				this._dataStore.put(res.get(0));
			}
		}
	}
	
	public String getTake5TableName(){
		return this._tnameTk5;
	}
	
	public String getNumbersTableName(){
		return this._tnameNumbers;
	}
	
	public String getTk5WinningTableName(){
		return this._twnameTk5;
	}
	
	public String getNumbersWinningTableName(){
		return this._twnameNumbers;
	}
	
	public void UpdateNumbsWinningTable(int [] numbers, String time){
		Filter filter = new Query.FilterPredicate("time", FilterOperator.EQUAL, time);
		Query q = new Query("NumbersWin").setFilter(filter);
		PreparedQuery pq = _dataStore.prepare(q);
		List<Entity> res = pq.asList(FetchOptions.Builder.withDefaults());
		if (res.size() != 0)
			return;
		Entity numbersWinning = new Entity("NumbersWin");
		for (int i = 0; i < numbers.length; i++){
			numbersWinning.setProperty("number" + Integer.toString(i + 1), numbers[i]);
		}
		numbersWinning.setProperty("time", time);
		_dataStore.put(numbersWinning);
	}
	
	public void UpdateTake5WinningTable(int [] numbers, String time){
		Filter filter = new Query.FilterPredicate("time", FilterOperator.EQUAL, time);
		Query q = new Query("Take 5 Win").setFilter(filter);
		PreparedQuery pq = _dataStore.prepare(q);
		List<Entity> res = pq.asList(FetchOptions.Builder.withDefaults());
		if (res.size() != 0)
			return;
		Entity numbersWinning = new Entity("Take 5 Win");
		for (int i = 0; i < numbers.length; i++){
			numbersWinning.setProperty("number" + Integer.toString(i + 1), numbers[i]);
		}
		numbersWinning.setProperty("time", time);
		_dataStore.put(numbersWinning);
	}
	
	public void updateTake5Table(ArrayList<ArrayList<Integer>> obj){
		if (_tk5Key.size() == 0){
			this.getKeysFromTable(this._tnameTk5);
			if (this._tk5Key.size() == 0)
				InitTk5Table();
		}
		for(ArrayList<Integer> subList: obj){
			for (int i = 0; i < subList.size(); i++){
				int idx = subList.get(i);
				Entity tk5entity = null;
				try {
					tk5entity = _dataStore.get(_tk5Key.get(idx - 1));
				} catch (EntityNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long count = (Long) tk5entity.getProperty("count");
				count++;
				tk5entity.setProperty("count", count);
				_dataStore.put(tk5entity);
			}
		}
	}
	
	public void updateNumbersTable(ArrayList<ArrayList<Integer>> obj){
		if (_numbersKey.size() == 0){
			this.getKeysFromTable(this._tnameNumbers);
			if (this._numbersKey.size() == 0)
				InitNmbsTable();
		}
		for (ArrayList<Integer> subList: obj){
			for (int i = 0; i < subList.size(); i++){
				int idx = subList.get(i);
				Entity nmbsentity = null;
				try {
					nmbsentity = _dataStore.get(_numbersKey.get(idx));
				} catch (EntityNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long count = (Long) nmbsentity.getProperty("count");
				count++;
				nmbsentity.setProperty("count", count);
				_dataStore.put(nmbsentity);
			}
		}
	}
	
	public void updatePickingTables(){
		List<Entity> tk5 = fetchWinningTables(_twnameTk5);
		List<Entity> numbers = fetchWinningTables(_twnameNumbers);
		int [] tk5_counter = new int [40];
		int [] numbers_counter = new int [10];
		for (int i = 0; i < tk5.size(); i++){
			tk5_counter[((Long) tk5.get(i).getProperty("number1")).intValue()]++;
			tk5_counter[((Long) tk5.get(i).getProperty("number2")).intValue()]++;
			tk5_counter[((Long) tk5.get(i).getProperty("number3")).intValue()]++;
			tk5_counter[((Long) tk5.get(i).getProperty("number4")).intValue()]++;
			tk5_counter[((Long) tk5.get(i).getProperty("number5")).intValue()]++;
		}
		for (int i = 0; i < numbers.size(); i++){
			numbers_counter[((Long) numbers.get(i).getProperty("number1")).intValue()]++;
			numbers_counter[((Long) numbers.get(i).getProperty("number2")).intValue()]++;
			numbers_counter[((Long) numbers.get(i).getProperty("number3")).intValue()]++;
		}
		this.updatePickingTables(tk5_counter, this._tnameTk5);
		this.updatePickingTables(numbers_counter, _tnameNumbers);
		System.out.println("finish");
	}
	
	public List<Entity> QueryMaxNumbersTable(){
		Query q = new Query("Numbers").addSort("count", SortDirection.DESCENDING);
		PreparedQuery pq = _dataStore.prepare(q);
		List<Entity> res = pq.asList(FetchOptions.Builder.withLimit(3));
		return res;
	}
	
	public List<Entity> QueryMaxtk5Table(){
		Query q = new Query("Take 5").addSort("count", SortDirection.DESCENDING);
		PreparedQuery pq = _dataStore.prepare(q);
		List<Entity> res = pq.asList(FetchOptions.Builder.withLimit(5));
		return res;
	}
	
	public List<Entity> QueryMaxWinning(String tableName, int count){
		Query q = new Query(tableName).addSort("Winning", SortDirection.DESCENDING);
		PreparedQuery pq = _dataStore.prepare(q);
		List<Entity> res = pq.asList(FetchOptions.Builder.withLimit(count));
		return res;
	}
	
}
