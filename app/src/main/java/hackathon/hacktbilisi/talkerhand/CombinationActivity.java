package hackathon.hacktbilisi.talkerhand;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hackathon.hacktbilisi.talkerhand.DB.CTable;
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

    @Bind(R.id.main_layout_id)protected RelativeLayout mainLayout;

    private boolean updateStatus = false;
    TalkHand mTalkHand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination);
        ButterKnife.bind(this);

        initToolbar();

        setTypeFaces();


        attachListeners();

        Intent intent = getIntent();
        mTalkHand = (TalkHand) intent.getSerializableExtra("HAND");
        if (mTalkHand != null){
            updateStatus = true;
            word.setText(mTalkHand.getWord());
            for (int i = 0; i < mTalkHand.getGesturesList().size(); i++) {
                addToContainer(mTalkHand.getGesturesList().get(i));
            }
        }else {
            updateStatus = false;
            word.setHintTextColor(getResources().getColor(R.color.hint_text_color));
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
        mainLayout.setOnClickListener(this);
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

            case R.id.main_layout_id:
                hideKeyBoard();
                word.clearFocus();
                break;


            case R.id.save_button_id:
                save();
                break;
        }


    }

    private void hideKeyBoard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
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



    private ContentValues values;
    /**
     * შევავსოთ მოდელის ობიექტი
     */
    private void save(){
        if (updateStatus){
            update(mTalkHand);
        }else {
            TalkHand mTalkHand = new TalkHand();
            ArrayList<Integer> idList = new ArrayList<>();

            if (containerLayout.getChildCount() > 0 && word.getText().toString().length() > 0) {
                for (int i = 0; i < containerLayout.getChildCount(); i++) {
                    idList.add((Integer) containerLayout.getChildAt(i).getTag());
                }
                mTalkHand.setWord(word.getText().toString());
                mTalkHand.setGesturesList(idList);

                insert(mTalkHand);

                //დავბრუნდეთ მთავარ გვერდზე
                onBackPressed();
            }else {
                Toast.makeText(getBaseContext(),"კომბინაცია ცარიელია",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * update into DataBase
     * @param mTalkHand TalkHand
     */
    private void update(TalkHand mTalkHand){
        values = new ContentValues();
        ArrayList<Integer> idList = new ArrayList<>();

        if (containerLayout.getChildCount() > 0 && word.getText().toString().length() > 0) {

            for (int i = 0; i < containerLayout.getChildCount(); i++) {
                idList.add((Integer) containerLayout.getChildAt(i).getTag());
            }
            values.put(CTable.WORD, word.getText().toString());
            values.put(CTable.ACTIONS, convertListToJson(idList));
            MainActivity.db.update(CTable.TABLE_NAME, values, "id = ?", new String[]{String.valueOf(mTalkHand.getId())});

            onBackPressed();
        }else {
            Toast.makeText(getBaseContext(),"კომბინაცია ცარიელია",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * insert into Database
     * @param mTalkHand TalkHand
     */
    private void insert(TalkHand mTalkHand){

        values = new ContentValues();
        values.put(CTable.WORD, mTalkHand.getWord());
        values.put(CTable.ACTIONS, convertListToJson(mTalkHand.getGesturesList()));

        MainActivity.db.insert(CTable.TABLE_NAME, null, values);
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
