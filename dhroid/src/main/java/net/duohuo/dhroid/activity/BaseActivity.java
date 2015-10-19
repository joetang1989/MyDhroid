package net.duohuo.dhroid.activity;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.NetReceiver;
import net.duohuo.dhroid.ioc.InjectUtil;
/***
 *  
 * @author duohuo-jinghao 
 *
 */
public abstract  class BaseActivity extends Activity   {
	
	private ActivityTack tack=ActivityTack.getInstanse();
	private NetReceiver mReceiver = new NetReceiver();
	private IntentFilter mFilter = new IntentFilter();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tack.addActivity(this);
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	
	@Override
	public void finish() {
		super.finish();
		tack.removeActivity(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		if(Const.auto_inject){
			InjectUtil.inject(this);
		}
	}
}
