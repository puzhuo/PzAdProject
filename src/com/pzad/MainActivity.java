
package com.pzad;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.pzad.tui.PzTuiManager;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PzTuiManager.startTui(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
