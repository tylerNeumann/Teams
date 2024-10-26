package fvtc.edu.teams;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamListActivity extends AppCompatActivity {
    public static final String TAG = "TeamsListActivity";
    ArrayList<Team> teams;

    RecyclerView teamList;
    //TeamAdapter teamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_list);

        teams = new ArrayList<Team>();
        if(teams.size() == 0) createTeams();

        Navbar.initListButton(this);
        Navbar.initMapButton(this);
        Navbar.initSettingsButton(this);

        this.setTitle(getString(R.string.teams_list));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d(TAG, "onCreate: end");
    }
    private void createTeams() {
        Log.d(TAG, "createTeams: start");
        teams = new ArrayList<Team>();

        teams.add(new Team(1,"Packers","Green Bay","9205551234", 1, R.drawable.packers, true, 0.0, 0.0));
        teams.add(new Team(2,"Lions","Detroit","9204441234", 2, R.drawable.lions, false, 0.0, 0.0));
        teams.add(new Team(3,"Vikings","Minneapolis","9203331234", 3, R.drawable.vikings, false, 0.0, 0.0));
        teams.add(new Team(4,"Bears","Chicago","9202221234", 4, R.drawable.bears, false, 0.0, 0.0));

        //FileIO.writeFile(FILENAME, this, createDataArray(teams));
       // teams = readTeams(this);
        Log.d(TAG, "createTeams: end: " + teams.size());
    }
}