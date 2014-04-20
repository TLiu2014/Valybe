package com.lybe;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChargeReceiver extends BroadcastReceiver {

	private boolean chargeMark;
    @Override
    public void onReceive(Context context, Intent intent) {
		chargeMark = context.getSharedPreferences("delay",Activity.MODE_PRIVATE)
				.getBoolean("chargeMark", true);
        if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED) && chargeMark){
        	Intent bootActivityIntent=new Intent(context,PreActivity.class);
        	bootActivityIntent.putExtra("chargeMark", true);
            bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(bootActivityIntent);
        }
    }
}
