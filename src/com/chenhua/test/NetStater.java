package com.chenhua.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class NetStater {
	TextView mStatView;
	ConnectivityManager mConnMgr;
	Handler mHandler;
	Runnable mRunnable;
	long mLastMobileRx;
	long mLastMobileTx;
	long mLastWifiRx;
	long mLastWifiTx;
	int mSecond;
	
	public NetStater(View parent) {
		mStatView = (TextView)parent.findViewById(R.id.txt_netstat);
		//mStatView.setTypeface(Typeface.MONOSPACE);
		mConnMgr = (ConnectivityManager)parent.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		mHandler = new Handler();
		
		mRunnable = new Runnable() {
			public void run() {
				mSecond++;
				NetworkInfo netInfo = mConnMgr.getActiveNetworkInfo();
				if (netInfo != null && netInfo.isConnected())
				{
					long currentMobileRx = TrafficStats.getMobileRxBytes();
					long currentMobileTx = TrafficStats.getMobileTxBytes();
					long currentWifiRx = TrafficStats.getTotalRxBytes() - currentMobileRx;
					long currentWifiTx = TrafficStats.getTotalTxBytes() - currentMobileTx;
					String speedRx;
					String speedTx;
					if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {											
						speedRx = long2Speed(currentMobileRx - mLastMobileRx);
						speedTx = long2Speed(currentMobileTx - mLastMobileTx);
					} else {
						speedRx = long2Speed(currentWifiRx - mLastWifiRx);
						speedTx = long2Speed(currentWifiTx - mLastWifiTx);
					}
					String extra = netInfo.getExtraInfo();
					int endIndex = extra.length() - 1;
					if (extra.charAt(0) == '"' && extra.charAt(endIndex) == '"')
					{
						extra = extra.substring(1, endIndex);
					}
					mStatView.setText(extra + "\n" + mSecond + ": " + speedRx + " /" + speedTx);
					mLastMobileRx = currentMobileRx;
					mLastMobileTx = currentMobileTx;
					mLastWifiRx = currentWifiRx;
					mLastWifiTx = currentWifiTx;
				} else {
					mStatView.setText("Ã»ÓÐÍøÂç\n" + mSecond);
				}
				mHandler.postDelayed(this, 1000);
			}
		};
	}
	
	private String long2Speed(long data) {		
		String result;
		if (data <= 0) {
			result = "0 B";			
		}
		else if (data > 0 && data < 1000) {
			result = Long.toString(data) + " B";
		} else if (data >= 1000 && data < 1000000) {
			result = Long.toString(data/1000) + " KB";
		} else if (data >= 1000000 && data < 1000000000) {
			result = Long.toString(data/1000000) + " MB";
		} else {
			result = Long.toString(data/1000000000) + " GB";
		}
		return result;
	}
	
	public void start() {		
		mLastMobileRx = TrafficStats.getMobileRxBytes();
		mLastMobileTx = TrafficStats.getMobileTxBytes();
		mLastWifiRx = TrafficStats.getTotalRxBytes() - mLastMobileRx;
		mLastWifiTx = TrafficStats.getTotalTxBytes() - mLastMobileTx;
		mSecond = 0;
		mHandler.post(mRunnable);
	}
	
	public void stop() {
		mHandler.removeCallbacks(mRunnable);
		mStatView.setText("");
	}
}
