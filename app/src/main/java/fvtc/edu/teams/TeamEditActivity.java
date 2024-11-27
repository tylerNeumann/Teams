package fvtc.edu.teams;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TeamEditActivity extends AppCompatActivity implements RaterDialog.SaveRatingListener{
    public static final String TAG = TeamEditActivity.class.toString();
    Team team;
    boolean loading = true;
    int teamId = -1;
    ArrayList<Team> teams;
    public static final int PERMISSION_REQUEST_PHONE = 102;
    public static final int PERMISSION_REQUEST_CAMERA = 103;
    public static final int CAMERA_REQUEST = 1888;
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
        /*initCallFunction();
        initImgBtn();*/


        Log.i(TAG, "onCreate: " + teamId);
        SetForEditing(false);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    /*private void initCallFunction() {
        EditText editCell = findViewById(R.id.editCell);
        editCell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                checkPhonePermission(team.getCellPhone());
                return false;
            }
        });
    }
    private void checkPhonePermission(String cellphone) {
        // Check the API version
        if(Build.VERSION.SDK_INT >= 23)
        {
            // Check for the manifest permission
            if(ContextCompat.checkSelfPermission(TeamEditActivity.this, android.Manifest.permission.CALL_PHONE) != PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(TeamEditActivity.this, android.Manifest.permission.CALL_PHONE)){
                    Snackbar.make(findViewById(R.id.activity_team_edit), "Teams requires this permission to place a call form the app.",
                            Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: snackBar");
                            ActivityCompat.requestPermissions(TeamEditActivity.this,
                                    new String[] {android.Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_PHONE);
                        }
                    }).show();
                }
                else {
                    Log.d(TAG, "checkPhonePermission: 1");
                    ActivityCompat.requestPermissions(TeamEditActivity.this,
                            new String[] {android.Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_PHONE);
                    callTeam(cellphone);
                }
            }
            else{
                Log.d(TAG, "checkPhonePermission: 2");
                callTeam(cellphone);
            }
        }
        else {
            // Only rely on the previous permissions
            callTeam(cellphone);
        }
    }
    private void callTeam(String cellphone) {
        Log.d(TAG, "callTeam: " + cellphone);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + cellphone));
        startActivity(intent);
    }
    private void initImgBtn() {
        ImageButton imageTeam = findViewById(R.id.imgTeam);
        imageTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 23){
                    // Check for the manifest permission
                    if(ContextCompat.checkSelfPermission(TeamEditActivity.this, android.Manifest.permission.CAMERA) != PERMISSION_GRANTED){
                        if(ActivityCompat.shouldShowRequestPermissionRationale(TeamEditActivity.this, android.Manifest.permission.CAMERA)){
                            Snackbar.make(findViewById(R.id.activity_team_edit), "Teams requires this permission to take a photo.",
                                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d(TAG, "onClick: snackBar");
                                    ActivityCompat.requestPermissions(TeamEditActivity.this,
                                            new String[] {android.Manifest.permission.CAMERA},PERMISSION_REQUEST_PHONE);
                                }
                            }).show();
                        }
                        else {
                            Log.d(TAG, "onClick: ");
                            ActivityCompat.requestPermissions(TeamEditActivity.this,
                                    new String[] {android.Manifest.permission.CAMERA},PERMISSION_REQUEST_PHONE);
                            takePhoto();
                        }
                    }
                    else{
                        Log.d(TAG, "onClick: ");
                        takePhoto();
                    }
                }
                else{
                    // Only rely on the previous permissions
                    Log.d(TAG, "onClick: ");
                    takePhoto();
                }
            }
        });
    }
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == CAMERA_REQUEST){
            if(resultCode == RESULT_OK){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo,144,144,true);
                ImageButton imageButton = findViewById(R.id.imgTeam);
                imageButton.setImageBitmap(scaledPhoto);
            }
        }
    }*/
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
            ImageButton imageTeam = findViewById(R.id.imgTeam);
            if(team.getPhoto() != null) imageTeam.setImageBitmap(team.getPhoto());
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
                    //team.setImgId(R.drawable.photoicon);
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