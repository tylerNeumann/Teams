package fvtc.edu.teams;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class Navbar {
    public static final String TAG = "Navbar";
    public static void initSettingsButton(Activity activity){
        Log.i(TAG, "initSettingsButton: hit");
        ImageButton ibSettings = activity.findViewById(R.id.imageButtonSettings);
        setupListenerEvent(ibSettings, activity, TeamSettingsActivity.class);
    }

    public static void initMapButton(Activity activity){
        Log.i(TAG, "initMapButton: hit");
        ImageButton ibMap = activity.findViewById(R.id.imageButtonMap);
        setupListenerEvent(ibMap, activity, TeamMapActivity.class);
    }
    public static void initListButton(Activity activity){
        Log.i(TAG, "initListButton: hit");
        ImageButton ibList = activity.findViewById(R.id.imageButtonList);
        setupListenerEvent(ibList, activity, TeamListActivity.class);
    }
    private static void setupListenerEvent(ImageButton imageButton,
                                           Activity fromActivity,
                                           Class<?> destinationActivityClass) {
        //disable one of the buttons
        imageButton.setEnabled(fromActivity.getClass() != destinationActivityClass);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fromActivity, destinationActivityClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                fromActivity.startActivity(intent);
            }
        });
    }
}
