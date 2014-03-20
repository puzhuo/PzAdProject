
package com.pzad;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button mTuiButton = (Button) findViewById(R.id.tui);
		mTuiButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				PzManager.startTui(MainActivity.this);
				Toast.makeText(MainActivity.this, getString(R.string.tui_opened), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
