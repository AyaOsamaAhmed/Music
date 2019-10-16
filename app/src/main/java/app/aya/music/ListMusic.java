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
        list_music.put("name0","fairoz.mp3");
        list_music.put("name1","promiseyou.mp3");
        list_music.put("name2","livelife.mp3");
        list_music.put("name3","tellmelovely.mp3");
        list_music.put("name4","pianther.mp3");
    }

    public void musicName (){
        list_name_music.put("key0","فيروز- كنا نتلاقى");
        list_name_music.put("key1","عمرو دياب - وعدتك");
        list_name_music.put("key2","محمد فؤاد - عيش الحياه");
        list_name_music.put("key3","عمرو دياب - قولى يا حبيبى");
        list_name_music.put("key4","عمرو دياب - رسمها");

    }
    private void Bulider() {
        list_view = (ListView)findViewById(R.id.list_music);
        list_name_music = new HashMap<String, String>();
        list_music =new HashMap<String, String>();

    }
}
