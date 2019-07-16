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
    HashMap<String, String> list_music ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_music);

        Bulider();

        musicList();

        musicName();
        list_view.setAdapter(new ListViewAdapterMusic(ListMusic.this,list_music,list_name_music));
    }

    private void musicList() {
        list_music.put("name0","love_to_you.mp3");
        list_music.put("name1","gany_lovely.mp3");
        list_music.put("name2","flower_night.mp3");
        list_music.put("name3","no_problem.mp3");
        list_music.put("name4","fairoz.mp3");
    }

    public void musicName (){
        list_name_music.put("key0","من حبى فيك يا جارى");
        list_name_music.put("key1","جانى حبيبى");
        list_name_music.put("key2","فى ليله ورد");
        list_name_music.put("key3","ميشغلكش");
        list_name_music.put("key4","فيروز");

    }
    private void Bulider() {
        list_view = (ListView)findViewById(R.id.list_music);
        list_name_music = new HashMap<String, String>();
        list_music =new HashMap<String, String>();

    }
}
