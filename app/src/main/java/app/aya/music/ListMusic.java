package app.aya.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class ListMusic extends AppCompatActivity {

    ListView list_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_music);
        list_view = (ListView)findViewById(R.id.list_music);

        list_view.setAdapter(new ListViewAdapterMusic(ListMusic.this,arrayList_employee ,ls_username ));
    }
}
