package baasi.hackathon.sja;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import baasi.hackathon.sja.DB.CTable;
import baasi.hackathon.sja.DB.DBHelper;
import baasi.hackathon.sja.adapters.TalkerAdapter;
import baasi.hackathon.sja.model.TalkHand;
import baasi.hackathon.sja.util.MyGson;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.list_id)protected ListView listView;
    @Bind(R.id.toolbar)protected Toolbar toolbar;

    @Bind(R.id.fab)protected FloatingActionButton fab;
    @Bind(R.id.fab_talk_id)protected FloatingActionButton talk_fab;

    public static SQLiteDatabase db;
    public DBHelper dbHelper;
    private TalkerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        setSupportActionBar(toolbar);

        addFabIconClick();

        talkFabIconClick();

        itemClick();
    }

    private void addFabIconClick(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CombinationActivity.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,
                        R.anim.left_out);
            }
        });
    }

    private void talkFabIconClick(){
        talk_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TalkActivity.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,
                        R.anim.left_out);
            }
        });
    }


    /**x
     * itemze kliki
     */
    private void itemClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TalkHand mTalkHand = (TalkHand) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this,CombinationActivity.class);
                intent.putExtra("HAND",mTalkHand);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,
                        R.anim.left_out);
            }
        });
    }


    /**
     * წამოოვათრიოთ ბაზიდან
     * @return list
     */
    private ArrayList<TalkHand> getData(){

        ArrayList<TalkHand> list = new ArrayList<>();
        TalkHand mTalkHand;
        if (db != null){
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM " + CTable.TABLE_NAME +" ORDER BY "+CTable.id + " DESC", null);
            if (cursor.moveToFirst()){
                do{
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(String.valueOf(CTable.id))));
                    String word = cursor.getString(cursor.getColumnIndex(String.valueOf(CTable.WORD)));
                    String actions = cursor.getString(cursor.getColumnIndex(String.valueOf(CTable.ACTIONS)));
                    mTalkHand = new TalkHand(id,word, MyGson.getIntegers(actions));
                    list.add(mTalkHand);
                }while (cursor.moveToNext());
            }
        }

        System.out.println("111 getData");
        return list;
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            ArrayList<TalkHand> list = getData();
            mAdapter = new TalkerAdapter(list,this);
            if (listView != null) {
                listView.setAdapter(mAdapter);
                System.out.println("111 setAdapter");
            }
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            System.out.println("111 error");
        }
    }
}
