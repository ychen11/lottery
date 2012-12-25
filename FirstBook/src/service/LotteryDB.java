package service;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

public class LotteryDB {
	
	private static LotteryDB myInstance;
	private static ArrayList<Key> _tk5Key;
	private static ArrayList<Key> _numbersKey;
	private static Key _tk5WinKey;
	private static Key _numbersWinKey;
	private static DatastoreService _dataStore;
	
	private LotteryDB(){
		_tk5Key = new ArrayList<Key>();
		_numbersKey = new ArrayList<Key>();
		_tk5WinKey = null;
		_numbersWinKey = null;
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
			Entity nmbsEntity = new Entity("Numbers");
			nmbsEntity.setProperty("Number", i);
			nmbsEntity.setProperty("count", 0);
			nmbsEntity.setProperty("Winnind", 0.0);
			_dataStore.put(nmbsEntity);
			_numbersKey.add(nmbsEntity.getKey());
		}
	}
	
	public void updateTake5Table(ArrayList<ArrayList<Integer>> obj){
		if (_tk5Key.size() == 0){
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
	
}
