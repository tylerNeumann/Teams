package fvtc.edu.teams;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TeamSettingsActivity extends AppCompatActivity {
public static final String TAG = "Settings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_settings);
        Log.i(TAG, "onCreate: ");
        Navbar.initListButton(this);
        Navbar.initMapButton(this);
        Navbar.initSettingsButton(this);

        initSortByClick();
        initSortOrderClick();
        initSettings();

        this.setTitle(getString(R.string.team_settings));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);

        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbName = findViewById(R.id.radioName);
                RadioButton rbCity = findViewById(R.id.radioCity);
                RadioButton rbIsFavorite = findViewById(R.id.radioIsFavorite);

                String sortBy;

                if(rbName.isChecked())
                {
                    sortBy = "name";
                }
                else if(rbCity.isChecked())
                {
                    sortBy = "city";
                }
                else
                {
                    sortBy = "isfavorite";
                }

                getSharedPreferences("teamspreferences",
                        Context.MODE_PRIVATE)
                        .edit()
                        .putString("sortby", sortBy)
                        .apply();
                Log.d(TAG, "onCheckedChanged: " + sortBy);
            }
        });

    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);

        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                RadioButton rbDescending = findViewById(R.id.radioDescending);

                String sortOrder;

                if(rbAscending.isChecked())
                {
                    sortOrder = "ASC";
                }
                else
                {
                    sortOrder = "DESC";
                }

                getSharedPreferences("teamspreferences",
                        Context.MODE_PRIVATE)
                        .edit()
                        .putString("sortorder", sortOrder)
                        .apply();
                Log.d(TAG, "onCheckedChanged: " + sortOrder);
            }
        });
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("teamspreferences",
                Context.MODE_PRIVATE)
                .getString("sortby", "name");
        String sortOrder = getSharedPreferences("teamspreferences",
                Context.MODE_PRIVATE)
                .getString("sortorder", "ASC");

        RadioButton rbName = findViewById(R.id.radioName);
        RadioButton rbCity = findViewById(R.id.radioCity);
        RadioButton rbIsFavorite = findViewById(R.id.radioIsFavorite);
        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);

        rbName.setChecked(true);
        rbCity.setChecked(sortBy.equalsIgnoreCase("city"));
        rbIsFavorite.setChecked(sortBy.equalsIgnoreCase("isfavorite"));

        rbAscending.setChecked(true);
        rbDescending.setChecked(sortOrder.equalsIgnoreCase("DESC"));
    }
}