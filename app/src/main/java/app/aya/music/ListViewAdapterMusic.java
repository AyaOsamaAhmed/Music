package app.aya.music;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewAdapterMusic  extends BaseAdapter {


    Activity context;
    String ls_username ;
    Integer list_position = 0 ;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> pos_username = new HashMap<String, String>();


    public ListViewAdapterMusic(Activity context,
                                  ArrayList<HashMap<String, String>> arraylist ) {

        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup viewGroup) {
        // Declare Variables
        TextView song ;
        final String      song_name;

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewClient = inflater.inflate(R.layout.list_music_inside, null, true);

        // Locate the TextViews
        song = (TextView) listViewClient.findViewById(R.id.song_name);

        final HashMap<String, String> dataClients = data.get(position);
        //------------------
        song_name = dataClients.get("name"+position);
        song.setText(dataClients.get("name"+position));
        //--------------------
        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( context ,PlaySong.class);
                intent.putExtra("songname",song_name);
                context.startActivity(intent);
            }
        });

        return listViewClient;

    }

}
