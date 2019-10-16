package app.aya.music.list;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import app.aya.music.play.PlaySong;
import app.aya.music.R;

public class ListViewAdapterMusic  extends BaseAdapter {


    Activity context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> musicname = new HashMap<String, String>();
    HashMap<String, String> hashdata = new HashMap<String, String>();
     ArrayList<String>           serial_music= new ArrayList<String>();
    ArrayList<String>           name_music= new ArrayList<String>();

    public ListViewAdapterMusic(Activity context,HashMap<String, String> hashlist,
                                  HashMap<String, String> hashname ) {

        this.context = context;
        hashdata = hashlist;
        musicname = hashname;
    }

    @Override
    public int getCount() {
        return hashdata.size();
    }

    @Override
    public Object getItem(int position) {
        return hashdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup viewGroup) {
        // Declare Variables
        TextView song ;
        final String      song_name , title;

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewClient = inflater.inflate(R.layout.list_music_inside, null, true);

        // Locate the TextViews
        song = (TextView) listViewClient.findViewById(R.id.song_name);
        // final HashMap<String, String> dataClients = data.get(position);
        //------------------
        song_name = hashdata.get("name"+position);
        serial_music.add(position,hashdata.get("name"+position));
        name_music.add(position,musicname.get("key"+position));
        title = musicname.get("key"+position);
        song.setText(musicname.get("key"+position));
        //--------------------
        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( context , PlaySong.class);
                intent.putExtra("songname",song_name);
                intent.putExtra("playing",title);
                intent.putExtra("position",position+"");
                intent.putStringArrayListExtra("serial" , serial_music);
                intent.putStringArrayListExtra("name" , name_music);
                context.startActivity(intent);
            }
        });

        return listViewClient;

    }

}
