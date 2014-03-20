
package com.pzad.tui;

import android.content.Context;
import android.content.Intent;

public class PzTuiManager {

	public static void startTui(Context context) {
		context.startService(new Intent(context, PzTuiService.class));
	}
}
