package hackathon.hacktbilisi.talkerhand;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import hackathon.hacktbilisi.talkerhand.DB.CTable;
import hackathon.hacktbilisi.talkerhand.DB.DBHelper;
import hackathon.hacktbilisi.talkerhand.model.TalkHand;

public class CombinationActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.toolbar) protected Toolbar toolbar;

    @Bind(R.id.create_your_word_id) protected EditText word;
    @Bind(R.id.fist_img_id) protected ImageView fist_imgLayout;
    @Bind(R.id.left_img_id) protected ImageView left_imgLayout;
    @Bind(R.id.right_img_id) protected ImageView right_imgLayout;
    @Bind(R.id.spread_img_id) protected ImageView spread_imgLayout;
    @Bind(R.id.save_button_id)protected Button saveButton;

    @Bind(R.id.actions_container_layout_id) protected LinearLayout containerLayout;
    @Bind(R.id.icons_container_id)protected LinearLayoutCompat iconsContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination);
        ButterKnife.bind(this);

        initToolbar();

        setTypeFaces();


        attachListeners();

        Intent intent = getIntent();
        TalkHand mTalkHand = (TalkHand) intent.getSerializableExtra("HAND");
        if (mTalkHand != null){
            word.setText(mTalkHand.getWord());
            for (int i = 0; i < mTalkHand.getGesturesList().size(); i++) {
                addToContainer(mTalkHand.getGesturesList().get(i));
            }
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getColoredArrow());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }



    private void setTypeFaces(){
        Typeface font = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "BPG_GEL_Excelsior.ttf");

        word.setTypeface(font);
        saveButton.setTypeface(font);
    }




    private Drawable getColoredArrow() {
        Drawable arrowDrawable = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        Drawable wrapped = DrawableCompat.wrap(arrowDrawable);

        if (arrowDrawable != null && wrapped != null) {
            arrowDrawable.mutate();
            DrawableCompat.setTint(wrapped, Color.WHITE);
        }

        return wrapped;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CombinationActivity.this.overridePendingTransition(R.anim.right_in,
                R.anim.left_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * მივაბათ ლისენერები
     */
    private void attachListeners(){
        fist_imgLayout.setOnClickListener(this);
        left_imgLayout.setOnClickListener(this);
        right_imgLayout.setOnClickListener(this);
        spread_imgLayout.setOnClickListener(this);

        saveButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.spread_img_id:
                addToContainer(R.drawable.spread_black);
                break;
            case R.id.fist_img_id:
                addToContainer(R.drawable.mushti_black);
                break;
            case R.id.left_img_id:
                addToContainer(R.drawable.left_black);
                break;
            case R.id.right_img_id:
                addToContainer(R.drawable.right_black);
                break;

            case R.id.save_button_id:
                save();
                break;
        }


    }


    /**
     * დავამატოთ კონტეინერ ლეიატში
     * @param resId
     */
    private void addToContainer(int resId){

        if (containerLayout != null && containerLayout.getChildCount() <= 4) {

            ImageView image = new ImageView(getBaseContext());
            image.setImageResource(resId);
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(170, 170);

            image.setLayoutParams(parms);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setOnClickListener(actionsClick);

            PlayAnim(image, this, R.anim.slide_right_in, 0);

            image.setTag(resId);
            containerLayout.addView(image);


        }else {
            iconsContainer.setOnClickListener(this);
        }
    }


    /**
     * ანიმაციით შემოსვლა სურათის
     * @param v View
     * @param Con Context
     * @param animationid animation id
     * @param StartOffset offset
     * @return animation
     */
    public Animation PlayAnim( View v, Context Con, int animationid, int StartOffset ){
        if( v != null ){
            Animation animation = AnimationUtils.loadAnimation(Con, animationid);
            animation.setStartOffset(StartOffset);
            v.startAnimation(animation);

            return animation;
        }
        return null;
    }

    View.OnClickListener actionsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView img = (ImageView) v;
            containerLayout.removeView(img);
        }
    };


    /**
     * შევავსოთ მოდელის ობიექტი
     */
    private void save(){

        TalkHand mTalkHand = new TalkHand();
        ArrayList<Integer> idList = new ArrayList<>();

        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            idList.add((Integer) containerLayout.getChildAt(i).getTag());
        }

        mTalkHand.setWord(word.getText().toString());
        mTalkHand.setGesturesList(idList);

        insert(mTalkHand);

    }

    private void insert(TalkHand mTalkHand){

        ContentValues values = new ContentValues();
        values.put(CTable.WORD, mTalkHand.getWord());
        values.put(CTable.ACTIONS, convertListToJson(mTalkHand.getGesturesList()));

        long id =  MainActivity.db.insert(CTable.TABLE_NAME, null, values);
        System.out.println(id);
    }


    /**
     * გადავიყვანოთ ლისტი ჯსონ ობიექტად (სტრინგი)
     * @param list ლისტი
     * @return ჯსონ სტრინგი
     */
    private String convertListToJson(ArrayList<Integer> list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
