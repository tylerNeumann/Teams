package fvtc.edu.teams;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class TeamEditActivity extends AppCompatActivity implements RaterDialog.SaveRatingListener{
    public static final String TAG = TeamEditActivity.class.toString();
    Team team;
    boolean loading = true;
    int teamId = -1;
    ArrayList<Team> teams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_edit);

        Bundle extras = getIntent().getExtras();
        teamId = extras.getInt("teamId");
        Log.i(TAG, "onCreate: teamId = " + teamId);
        this.setTitle("Team: " + teamId);
        //teams = TeamListActivity.readTeams(this);
        //if(teams.size() != 4){
            if (teamId != -1) {
                initTeams(teamId);
            }
            else team = new Team();

        Navbar.initListButton(this);
        Navbar.initMapButton(this);
        Navbar.initSettingsButton(this);

        initRatingButton();
        initToggleButton();
        initSaveButton();
        initTextChanged(R.id.etName);
        initTextChanged(R.id.etCity);
        initTextChanged(R.id.editCell);


        Log.i(TAG, "onCreate: " + teamId);
        SetForEditing(false);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public void didFinishTeamRaterDialog(float rating){
        Log.d(TAG, "didFinishTeamRaterDialog: " + rating);
        TextView txtRating = findViewById(R.id.txtRating);
        txtRating.setText(String.valueOf(rating));
        team.setRating(rating);
    }
    private void initTeams(int teamId) {
        //get the teams
        try {
            /*TeamsDataSource ds = new TeamsDataSource(this);
            ds.open();
            team = ds.get(teamId);
            ds.close();*/
            RestClient.execGetOneRequest(getString(R.string.API_URl) + teamId,
                    this,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Team> result) {
                            team = result.get(0);
                            rebindTeam();
                        }
                    });
            Log.d(TAG, "initTeams: " + team.toString());
        }catch (Exception e){
            Log.d(TAG, "initTeams: " + e.getMessage());
        }
        rebindTeam();
    }
    private void rebindTeam() {
        EditText editName = findViewById(R.id.etName);
        EditText editCity = findViewById(R.id.etCity);
        EditText editCellPhone = findViewById(R.id.editCell);
        TextView editRating = findViewById(R.id.txtRating);

        if (team != null) {
            editName.setText(team.getName());
            editCity.setText(team.getCity());
            editCellPhone.setText(team.getCellPhone());
            editRating.setText(String.valueOf(team.getRating()));
        }

    }
    private void initRatingButton(){
        Log.d(TAG, "initRatingButton: hit rating button");
        Button btnRating = findViewById(R.id.btnRating);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                RaterDialog raterDialog = new RaterDialog(team.getRating());
                raterDialog.show(fragmentManager,"Rate Team");
            }
        });
    }
    private void initTextChanged(int controlId){
        EditText editText = findViewById(controlId);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                team.setControlText(controlId,s.toString());
            }
        });
    }
    private void initToggleButton() {
        ToggleButton toggleButton = findViewById(R.id.toggleButtonEdit);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetForEditing(toggleButton.isChecked());
            }
        });
    }
    private void SetForEditing(boolean checked) {
        EditText editName = findViewById(R.id.etName);
        EditText editCity = findViewById(R.id.etCity);
        EditText editCellPhone = findViewById(R.id.editCell);
        Button btnRating = findViewById(R.id.btnRating);

        editName.setEnabled(checked);
        editCity.setEnabled(checked);
        editCellPhone.setEnabled(checked);

        if(checked) editName.requestFocus();//set focus to editName
        else {
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    }
    private void initSaveButton()  {
        Button btnSave = findViewById(R.id.btnSave);
        Log.d(TAG, "initSaveButton: hit");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: hit");
                if(teamId == -1){
                    Log.d(TAG, "onClick: " + team.toString());
                    RestClient.execPostRequest(team, getString(R.string.API_URl), TeamEditActivity.this,
                            new VolleyCallback() {
                                @Override
                                public void onSuccess(ArrayList<Team> result) {
                                    team.setId(result.get(0).getId());
                                    Log.d(TAG, "onSuccess: Post" + team.getId());
                                }
                            });
                    team.setImgId(R.drawable.photoicon);
                } else {
                    RestClient.execPutRequest(team, getString(R.string.API_URl) + teamId, TeamEditActivity.this, new VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<Team> result) {

                            Log.d(TAG, "onSuccess: Put" + team.getId());
                        }
                    });
                }
                startActivity(new Intent(TeamEditActivity.this, TeamListActivity.class));
            }
        });
    }
    private void initCallFunction() {
        EditText editCell = findViewById(R.id.editCell);
        editCell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //checkPhonePermission(team.getCellPhone());
                return false;
            }
        });
    }
}