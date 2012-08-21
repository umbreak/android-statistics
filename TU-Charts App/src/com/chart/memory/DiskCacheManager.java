package com.chart.memory;

import static com.chart.AppUtils.DISK_CACHE_DIR;
import static com.chart.AppUtils.DISK_CACHE_SIZE;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.chart.pojos.ChartModel;
import com.jakewharton.DiskLruCache;
import com.jakewharton.DiskLruCache.Editor;
import com.jakewharton.DiskLruCache.Snapshot;

public class DiskCacheManager{
	private DiskLruCache mDiskCache;
	private ObjectMapper mapper;
	private final static String TAG="DIskCacheManager";
	public DiskCacheManager(Context context) {
		super();
		File cacheDir = getCacheDir(context, DISK_CACHE_DIR);
		try {
			mDiskCache= DiskLruCache.open(cacheDir, 10, 1, DISK_CACHE_SIZE);
		} catch (IOException e) { Log.e(TAG, "Exception opening the cache dir");
		}
		mapper = new ObjectMapper();

	}

	public void putChart(Integer key, ChartModel chart){
		new putInSSD(key.toString(), chart);
	}
	public ChartModel getChart(Integer key){
		ChartModel chart=null;
		Snapshot snapshot=null;
		try {
			snapshot=mDiskCache.get(key.toString());
			chart=mapper.readValue(snapshot.getInputStream(0), ChartModel.class);
		} catch (Exception e) {
			Log.e(TAG,"Error reading the element '"+key +"' from the DiskCache");
			return null;
		}finally{
			if ( snapshot != null ) snapshot.close();
		}
		return chart;
	}
	public Snapshot get(Integer key){
		try {
			return mDiskCache.get(key.toString());

		} catch (IOException e) {
			Log.e(TAG, "Couldn't read the element '"+ key +"' from the disk cache");
			return null;
		}
	}

	// Creates a unique subdirectory of the designated app cache directory. Tries to use external
	// but if not mounted, falls back on internal storage.
	private File getCacheDir(Context context, String uniqueName) {
		// Check if media is mounted or storage is built-in, if so, try and use external cache dir
		// otherwise use internal cache dir
		final String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
				|| !Environment.isExternalStorageRemovable() ?
						context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();

						return new File(cachePath + File.separator + uniqueName);
	}
	class putInSSD implements Runnable{
		private String key;
		private ChartModel chart;
		public putInSSD(String key, ChartModel chart) {
			this.key=key;
			this.chart=chart;
			Thread t=new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			try {
				Editor editor=mDiskCache.edit(key);
				if (editor == null) return;
				if (writeChartToFile(chart, editor)){
					editor.commit();
					mDiskCache.flush();
				}else
					editor.abort();
			} catch (IOException e) { 
				Log.e(TAG, "Exception writing chart on Disk Cache: " + e.toString());
			}
		}
		
		private boolean writeChartToFile( ChartModel chart, DiskLruCache.Editor editor )
				throws IOException, FileNotFoundException {
			OutputStream out = null;
			boolean end=true;
			try {
				out = new BufferedOutputStream( editor.newOutputStream( 0 ));
				mapper.writeValue(out, chart);
			}catch(Exception e){ 
				Log.e(TAG, "Exception writing chart on Disk Cache: " + e.toString());
				end=false;
			}
			finally {
				if ( out != null ) 
					out.close();
			}
			return end;

		}			
	}

}
