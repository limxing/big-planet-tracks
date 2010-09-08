package tyt.android.bigplanettracks.maps.storage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tyt.android.bigplanettracks.BigPlanet;
import tyt.android.bigplanettracks.maps.Handler;
import tyt.android.bigplanettracks.maps.RawTile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Обертка над LocalStorage Производит декодирование тайла в синхронном и в
 * асинхронном режиме
 * 
 * @author hudvin
 * 
 */
public class LocalStorageWrapper {

	private static ILocalStorage localStorage = SQLLocalStorage.getInstance();

	private static final int threadSize = (int) (6*BigPlanet.density);
	private static ExecutorService mThreadPool = Executors.newFixedThreadPool(threadSize);
	
	public static void switchLocalStorage() {
		SQLLocalStorage.resetLocalStorage();
		localStorage = SQLLocalStorage.getInstance();
	}

	/**
	 * Декодирует тайл
	 * 
	 * @param tile
	 * @return
	 */
	public static Bitmap get(final RawTile tile) {
		byte[] data = localStorage.get(tile);
		Bitmap bmp = null;
		if (data != null) {
			bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		}
		return bmp;
	}

	public static boolean isExists(RawTile tile) {
		return localStorage.isExists(tile);
	}

	public static void put(RawTile tile, byte[] data) {
		localStorage.put(tile, data);
	}

	public static void get(final RawTile tile, final Handler handler) {
		mThreadPool.execute(new Runnable() {
			public void run() {
				handler.handle(tile, get(tile), false);
			}
		});
	}

}
