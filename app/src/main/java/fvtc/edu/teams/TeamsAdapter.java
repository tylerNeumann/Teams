package fvtc.edu.teams;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamsAdapter extends RecyclerView.Adapter{
    private ArrayList<Team> teamData;
    private View.OnClickListener onItemClickListener;
    public static final String TAG = "TeamAdapter";

    private Context parentContext;

    public class TeamViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvCity;
        public Button btnDelete;
        private CheckBox chkFavorite;
        private ImageButton imgPhoto;
        private View.OnClickListener onClickListener;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCity = itemView.findViewById(R.id.tvCity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            chkFavorite = itemView.findViewById(R.id.chkFavorite);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            //code involved with clicking an item in the list

            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }

        public TextView getTvName() { return tvName; }
        public TextView getTvCity() { return tvCity; }
        public CheckBox getChkFavorite(){ return chkFavorite; }
        public  Button getBtnDelete(){ return btnDelete; }
        public ImageButton getImageButtonPhoto() { return imgPhoto; }
    }

    public TeamsAdapter(ArrayList<Team> data, Context context)
    {
        teamData = data;
        Log.d(TAG, "TeamAdapter: " + data.size());
        parentContext = context;
    }
    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        Log.d(TAG, "setOnItemClickListener: ");
        onItemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TeamViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + teamData.get(position));

        Team team = teamData.get(position);

        TeamViewHolder teamViewHolder = (TeamViewHolder) holder;
        teamViewHolder.getTvName().setText(team.getName());
        teamViewHolder.getTvCity().setText(team.getCity());
        teamViewHolder.getImageButtonPhoto().setImageResource(team.getImgId());
        teamViewHolder.getChkFavorite().setChecked(team.getIsFavorite());

        /*Bitmap teamPhoto = team.getPicture();
        if(teamPhoto != null){
            teamViewHolder.getImageButtonPhoto().setImageBitmap(teamPhoto);
        }
        else {
            teamViewHolder.getImageButtonPhoto().setImageResource(R.drawable.photoicon);
        }*/

        /*teamViewHolder.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: delete");
                deleteItem(position);
            }
        });*/
    }

    @Override
    public int getItemCount() { return teamData.size(); }

    /*private void deleteItem(int position){
        teamData.remove(position);
        //notifyDataSetChanged();
    }*/
}
