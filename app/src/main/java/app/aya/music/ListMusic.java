package app.aya.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListMusic extends AppCompatActivity {

    ListView list_view;
    HashMap<String, String> list_name_music ;
    ArrayList<HashMap<String, String>> list_music ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_music);

        Bulider();

        musicList();

        list_view.setAdapter(new ListViewAdapterMusic(ListMusic.this,list_music));
    }

    private void musicList() {
        list_name_music.put("name0","fairoz.mp3");


        list_music.add(list_name_music);
    }

    private void Bulider() {
        list_view = (ListView)findViewById(R.id.list_music);
        list_name_music = new HashMap<String, String>();
        list_music =new ArrayList<HashMap<String, String>>();

    }
}
