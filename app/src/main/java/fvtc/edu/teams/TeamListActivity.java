package fvtc.edu.teams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamListActivity extends AppCompatActivity {
    public static final String TAG = "TeamsListActivity";
    public static final String FILENAME =  "teams.txt";
    ArrayList<Team> teams;
    TeamsDataSource ds;

    RecyclerView teamList;
    TeamsAdapter teamAdapter;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Team team = teams.get(position);
            Log.d(TAG, "onClick: " + team.getName());

            Intent intent = new Intent(TeamListActivity.this, TeamEditActivity.class);
            intent.putExtra("teamId", team.getId());
            Log.d(TAG, "onClick: teamID" + team.getId());
            startActivity(intent);
        }
    };
    private CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "onCheckedChanged: " + isChecked);
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) buttonView.getTag();
            //not actually being set
            Log.d(TAG, "onCheckedChanged: " + buttonView.getTag());
            Log.d(TAG, "onCheckedChanged: made viewHolder");
            Log.d(TAG, "onCheckedChanged: " + viewHolder);
            int position = viewHolder.getAdapterPosition();
            Log.d(TAG, "onCheckedChanged: set position");
            teams.get(position).setIsFavorite(isChecked);
            ds.update(teams.get(position));
            /*FileIO.writeFile(TeamListActivity.FILENAME,
                    TeamListActivity.this,
                    createDataArray(teams));*/
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_list);

        teams = new ArrayList<Team>();

        /*teams = readTeams(this);*/
        //if(teams.size() == 0) createTeams();

        Navbar.initListButton(this);
        Navbar.initMapButton(this);
        Navbar.initSettingsButton(this);

        this.setTitle(getString(R.string.teams_list));

        initDeleteSwitch();
        initAddTeamButton();
        initDatabase();

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                double levelScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int batteryPercent = (int)Math.floor(batteryLevel/levelScale * 100);

                TextView txtBatteryLevel = findViewById(R.id.txtBatteryLevel);
                txtBatteryLevel.setText(batteryPercent + "%");
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, filter);

        RebindTeams();
        Log.i(TAG, "onCreate: teams size " + teams.size());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d(TAG, "onCreate: end");
    }
    private void initDatabase() {
        String sortBy = getSharedPreferences("teamspreferences",
                Context.MODE_PRIVATE)
                .getString("sortby", "name");
        String sortOrder = getSharedPreferences("teamspreferences",
                Context.MODE_PRIVATE)
                .getString("sortorder", "ASC");
        ds = new TeamsDataSource(this);
        ds.open();
        teams = ds.get(sortBy, sortOrder);
        Log.d(TAG, "initDatabase: Teams: " + teams.size());
    }
    /*private void createTeams() {
        Log.d(TAG, "createTeams: start");
        teams = new ArrayList<Team>();

        teams.add(new Team(1,"Packers","Green Bay","9205551234", 1, R.drawable.packers, true, 0.0, 0.0));
        teams.add(new Team(2,"Lions","Detroit","9204441234", 2, R.drawable.lions, false, 0.0, 0.0));
        teams.add(new Team(3,"Vikings","Minneapolis","9203331234", 3, R.drawable.vikings, false, 0.0, 0.0));
        teams.add(new Team(4,"Bears","Chicago","9202221234", 4, R.drawable.bears, false, 0.0, 0.0));

        FileIO.writeFile(FILENAME, this, createDataArray(teams));
        teams = readTeams(this);
        Log.d(TAG, "createTeams: end: " + teams.size());
    }*/
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume: Start");
        
        teamList = findViewById(R.id.rvTeams);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        teamList.setLayoutManager(layoutManager);
        teamAdapter = new TeamsAdapter(teams, this);
        teamAdapter.setOnItemClickListener(onClickListener);
        teamList.setAdapter(teamAdapter);

        Log.d(TAG, "onResume: End");
    }
    public static String[] createDataArray(ArrayList<Team> teams){
        String[] teamData = new String[teams.size()];
        for(int count = 0; count < teams.size(); count ++){
            teamData[count] = teams.get(count).toString();
        }
        Log.i(TAG, "createDataArray: " + teamData);
        return teamData;
    }
    /*public static ArrayList<Team> readTeams(AppCompatActivity activity) {
        ArrayList<String> strData = FileIO.readFile(FILENAME, activity);
        ArrayList<Team>  teams1 = new ArrayList<Team>();

        for (String s : strData){
            Log.d(TAG, "readTeams: " + s);
            String[] data = s.split("\\|");
            teams1.add(new Team(
                    Integer.parseInt(data[0]),
                    data[1],
                    data[2],
                    data[3],
                    Float.parseFloat(data[4]),
                    Integer.parseInt(data[5]),
                    Boolean.parseBoolean(data[6]),
                    Double.parseDouble(data[7]),
                    Double.parseDouble(data[8])
            ));
        }
        return teams1;
    }*/
    private void initDeleteSwitch() {
        SwitchCompat switchDelete = findViewById(R.id.switchDelete);
        switchDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: " + isChecked);
                teamAdapter.setDelete(isChecked);
                teamAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initAddTeamButton() {
        Button btnAddTeam = findViewById(R.id.btnAddTeam);
        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamListActivity.this, TeamEditActivity.class);
                intent.putExtra("teamId", -1);
                Log.d(TAG, "onClick: hit add item");
                startActivity(intent);
            }
        });
    }
    private void RebindTeams() {
        // rebind the recycler view
        teamList = findViewById(R.id.rvTeams);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        teamList.setLayoutManager(layoutManager);
        teamAdapter = new TeamsAdapter(teams, this);
        teamAdapter.setOnItemClickListener(onClickListener);
        teamAdapter.setOnItemCheckedChangeListener(onCheckedChangedListener);
        teamList.setAdapter(teamAdapter);
    }
}