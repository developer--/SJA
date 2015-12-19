package baasi.hackathon.sja;

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
                Intent intent = new Intent(MainActivity.this,HelloWorldActivity.class);
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
     * @return
     */
    private ArrayList<TalkHand> getData(){

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();

        ArrayList<TalkHand> list = new ArrayList<>();
        TalkHand mTalkHand;
        if (db != null){
            Cursor cursor = db.rawQuery("SELECT * FROM " + CTable.TABLE_NAME +" ORDER BY "+CTable.id + " DESC", null);
            if (cursor.moveToFirst()){
                do{
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(String.valueOf(CTable.id))));
                    String word = cursor.getString(cursor.getColumnIndex(String.valueOf(CTable.WORD)));
                    String actions = cursor.getString(cursor.getColumnIndex(String.valueOf(CTable.ACTIONS)));
                    mTalkHand = new TalkHand(id,word, (ArrayList<Integer>) gson.fromJson(actions,type));
                    list.add(mTalkHand);
                    System.out.println("1123 "+list.size());
                }while (cursor.moveToNext());
            }
        }


        return list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            ArrayList<TalkHand> list = getData();
            mAdapter = new TalkerAdapter(list,this);
            if (listView != null) {
                listView.setAdapter(mAdapter);
            }
        }catch (Exception e){
            System.out.println("error");
        }
    }
}
