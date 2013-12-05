package com.example.fridge.UI;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import fridge.DaoMaster;
import fridge.DaoMaster.DevOpenHelper;
import fridge.DaoSession;
import fridge.FridgeItem;
import fridge.FridgeItemDao;
import fridge.InFridge;
import fridge.InFridgeDao;
import fridge.InFridgeDao.Properties;

/**
 * //TODO :: !! memory cache in den sql adapter CacheManager verwaltet den
 * Cache. Erste Stufe ist der direkte Hauptspeicher, zweite der die SQLite DB.
 * Sämtliche Objekte werden über Transaktionen ( {@link CacheOperationsE} hier
 * per {@link #getObject(CacheOperationsE, Long)} oder
 * {@link #createOrUpdateObject(CacheOperationsE, Object, long)} gelesen oder
 * gesetzt. Dabei wird ständig die Write-Trough Strategie verwendet: Schreiben:
 * Daten sowohl in 1level als auch in 2level schreiben. Lesen: Lese Daten aus
 * dem 1level, wenn keine vorhanden sind dann aus 2level. Jede Transaktion
 * (set), die Offline ausgeführt wird, wird zusätzlich als Transaktion
 * persistent gespeichert bis die Verbindung zum Server wiederhergestellt werden
 * kann. Danach werden die Transaktionen neu ausgeführt.
 * 
 * @author Huebers
 */
// TODO zweite Getter funktion entfernen: man muss aufpassen, wann man welche
// verwendet. war Quickfix für die Flexlisten
// TODO insgesamt noch sehr unschön mit den CacheOperations gelöst...
// überarbeiten!
public final class CacheManager {
	private static String TAG = null;

	private static CacheManager mInstance = null;

	private DevOpenHelper mDevOpenHelper;

	private DaoMaster daoMaster;

	public static CacheManager getInst(Context c) {

		if (mInstance == null) {
			synchronized (CacheManager.class) {
				if (mInstance == null) {
					mInstance = new CacheManager(c);
				}
			}
		}

		return mInstance;
	}

	DaoSession daoSession;

	private FridgeItemDao fid;
	private InFridgeDao inFridgeDao;

	public DaoSession getDaoSession() {
		if (daoSession == null) {
			synchronized (DaoSession.class) {
				daoSession = daoMaster.newSession();
			}
		}
		return daoSession;

	}

	public FridgeItemDao getFridgeItemDao() {
		if (fid == null) {
			fid = getDaoSession().getFridgeItemDao();
			
		}
		return fid;
	}

	private InFridgeDao getInFridgeDao() {
		// TODO Auto-generated method stub
		if (inFridgeDao == null) {
			inFridgeDao = getDaoSession().getInFridgeDao();
		}
		return inFridgeDao;
	}

	public ArrayList<InFridge> getAllInFridgeItems() {
		ArrayList<InFridge> allItems = new ArrayList<InFridge>();

		QueryBuilder<InFridge> qb = getInFridgeDao().queryBuilder();
		allItems.addAll(qb.list());

		return allItems;

	}

	public CacheManager(Context context) {
		mInstance = this;
		TAG = this.getClass().getName();

		mDevOpenHelper = new DaoMaster.DevOpenHelper(context, "MyFridgeDB4",
				null);
		SQLiteDatabase db = mDevOpenHelper.getWritableDatabase();
		daoMaster = new DaoMaster(db);

	}

	public void addInFridgeItem(FridgeItem fridgeItem) {
		getFridgeItemDao().insertOrReplace(fridgeItem);

	}

	public long insertFridgeItem(FridgeItem ready) {
		FridgeItem temp = ready;

		long id = getFridgeItemDao().insert(temp);

		return temp.getInternalId();

	}

	public void insertOrReplaceInFridgeItem(InFridge ready) {
		getInFridgeDao().insertOrReplace(ready);

	}

	public ArrayList<InFridge> getInFridgeItem(long internalId) {
		// TODO Auto-generated method stub
		Query<InFridge> q = getInFridgeDao().queryBuilder()
				.where(Properties.InternalId.eq(internalId)).build();
		List<InFridge> item = q.list();
		ArrayList<InFridge> ret = new ArrayList<InFridge>(item);
		return ret;
	}

	public FridgeItem getFridgeItem(long test) {
		Query<FridgeItem> q = getFridgeItemDao().queryBuilder()
				.where(Properties.InternalId.eq(test)).build();
		FridgeItem item = q.unique();
		return item;
	}

	public List<FridgeItem> getFridgeItemByChars(CharSequence s) {
		String search = s.toString() + "%";
		Query<FridgeItem> q = getFridgeItemDao().queryBuilder()
				.where(fridge.FridgeItemDao.Properties.Name.like(search))
				.build();
		List<FridgeItem> items = q.list();
		return items;

	}

	public void insertOrReplaceFridgeItem(FridgeItem fridgeItem) {
		getFridgeItemDao().insertOrReplace(fridgeItem);

	}

	public void insertInFrigeItem(InFridge inFridgeItem) {
		getInFridgeDao().insert(inFridgeItem);

	}

	public void updateInFridgeItem(InFridge inFridgeItem) {

		getInFridgeDao().insertOrReplace(inFridgeItem);

	}

	public void deleteInFridge(InFridge actual) {
		QueryBuilder<InFridge> querybuilder = getInFridgeDao().queryBuilder()
				.where(Properties.InternalId.eq(actual.getInternalId()),
						Properties.Anzahl.eq(actual.getAnzahl()));
//		querybuilder.LOG_SQL=true;
//		querybuilder.LOG_VALUES=true;
		
		DeleteQuery<InFridge> dq = querybuilder.buildDelete();

		dq.executeDeleteWithoutDetachingEntities();

	}

	// public FridgeItem getFridgeItemByName(String name) {
	//
	// Query q = getFridgeItemDao().queryBuilder()
	// .where(Properties.Name.eq(name))
	// .build();
	//
	// List<AntonyFlexListDaoW> resultList = q.list();
	// AntonyFlexListDaoW ret = null;
	// if (resultList.size() > 0)
	// ret = resultList.get(resultList.size() - 1);
	// else
	// return null;
	// }

}
