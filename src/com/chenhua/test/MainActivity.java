package com.chenhua.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	NetStater stater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        stater = new NetStater(findViewById(android.R.id.content));
        
        Button btnStart = (Button)findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	start();
        }});
        
        Button btnStop = (Button)findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	stop();
        }});
    }

    public void start() {
    	stater.start();
    }
    
    public void stop() {
    	stater.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
