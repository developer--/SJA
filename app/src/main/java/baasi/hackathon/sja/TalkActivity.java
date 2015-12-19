/*
 * Copyright (C) 2014 Thalmic Labs Inc.
 * Distributed under the Myo SDK license agreement. See LICENSE.txt for details.
 */

package baasi.hackathon.sja;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import java.util.ArrayList;
import java.util.Locale;

import baasi.hackathon.sja.DB.CTable;
import baasi.hackathon.sja.util.MyGson;
import butterknife.Bind;
import butterknife.ButterKnife;

public class TalkActivity extends AppCompatActivity implements View.OnClickListener{


    @Bind(R.id.toolbar) protected Toolbar toolbar;
    @Bind(R.id.talk_container_layout)protected LinearLayout containerLayout;
    @Bind(R.id.talk_help_text_view_id)protected TextView helpTextView;
    @Bind(R.id.talk_reset_button_id)protected Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        ButterKnife.bind(this);
        initToolbar();
        attachListeners();
        setTypeFaces();

        // First, we initialize the Hub singleton with an application identifier.
        Hub hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            // We can't do anything with the Myo device if the Hub can't be initialized, so exit.
            Toast.makeText(this, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Hub.getInstance().setLockingPolicy(Hub.LockingPolicy.NONE);

        // Next, register for DeviceListener callbacks.
        hub.addListener(mListener);

    }



    /**
     * შევამოწმოთ თუ არჩეული კომბინაცია არსებობს ბაზაში მაშინ დავალაპარაკოთ
     */
    private void checkCombination(){

        Cursor cursor = MainActivity.db.rawQuery("SELECT * FROM " + CTable.TABLE_NAME, null);
        ArrayList<Integer> list = new ArrayList<>();

        if (containerLayout.getChildCount() > 0 ) {
            for (int i = 0; i < containerLayout.getChildCount(); i++) {
                list.add((Integer) containerLayout.getChildAt(i).getTag());
            }
        }

        if (cursor.moveToFirst()){
            do {
                String gsonString = cursor.getString(cursor.getColumnIndex(String.valueOf(CTable.ACTIONS)));
                String word = cursor.getString(cursor.getColumnIndex(String.valueOf(CTable.WORD)));
                //თუ ასეთი კომბინაცია აქვს
                if (gsonString.equals(MyGson.convertListToJson(list))){
                    startTalk(word);
                    System.out.println("daemtxva da ibazrebs");
                }

            }while (cursor.moveToNext());
        }
    }

    private TextToSpeech speech;
    /**
     * დავიწყოთ ლაპარაკი
     * @param word String
     */
    private void startTalk(final String word){
        speech = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.UK);
                    speech.speak(word,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }


    @Override
    public void onPause(){
        stopSpeech();
        super.onPause();
    }

    /**
     * გავაჩეროთ სფიჩი
     */
    private void stopSpeech(){
        if(speech !=null){
            speech.stop();
            speech.shutdown();
        }
    }



    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {

        // onConnect() is called whenever a Myo has been connected.
        @Override
        public void onConnect(Myo myo, long timestamp) {
            toast("connected");
        }

        // onDisconnect() is called whenever a Myo has been disconnected.
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            toast("disconnected");
        }

        // onArmSync() is called whenever Myo has recognized a Sync Gesture after someone has put it on their
        // arm. This lets Myo know which arm it's on and which way it's facing.
        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            toast("syncing");
        }

        // onArmUnsync() is called whenever Myo has detected that it was moved from a stable position on a person's arm after
        // it recognized the arm. Typically this happens when someone takes Myo off of their arm, but it can also happen
        // when Myo is moved around on the arm.
        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
//            mTextView.setText(R.string.hello_world);
        }

        // onUnlock() is called whenever a synced Myo has been unlocked. Under the standard locking
        // policy, that means poses will now be delivered to the listener.
        @Override
        public void onUnlock(Myo myo, long timestamp) {
            toast("unlock");
        }

        // onLock() is called whenever a synced Myo has been locked. Under the standard locking
        // policy, that means poses will no longer be delivered to the listener.
        @Override
        public void onLock(Myo myo, long timestamp) {
            toast("lock");
        }

        // onOrientationData() is called whenever a Myo provides its current orientation,
        // represented as a quaternion.
        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            // Calculate Euler angles (roll, pitch, and yaw) from the quaternion.
            float roll = (float) Math.toDegrees(Quaternion.roll(rotation));
            float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
            float yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));

            // Adjust roll and pitch for the orientation of the Myo on the arm.
            if (myo.getXDirection() == XDirection.TOWARD_ELBOW) {
                roll *= -1;
                pitch *= -1;
            }

            // Next, we apply a rotation to the text view using the roll, pitch, and yaw.
        }

        // onPose() is called whenever a Myo provides a new pose.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Handle the cases of the Pose enumeration, and change the text of the text view
            // based on the pose we receive.
            switch (pose) {
                case UNKNOWN:
                    toast("UNKNOWN");
                    break;
                case DOUBLE_TAP:
                    checkCombination();
                    System.out.println("111 vamowmebt");
                    resetCombination();
                    break;
                case REST:
//                case DOUBLE_TAP:
//                    toast("DOUBLE_TAP");
//                    switch (myo.getArm()) {
//                        case LEFT:
//                            toast("LEFT");
//                            addToContainer(R.drawable.left_black);
//                            break;
//                        case RIGHT:
//                            toast("RIGHT");
//                            addToContainer(R.drawable.left_black);
//                            break;
//                    }
                    break;
                case FIST:
                    toast("FIST");
                    addToContainer(R.drawable.mushti_black);
                    break;
                case WAVE_IN:
                    addToContainer(R.drawable.left_black);
                    toast("WAVE_IN");
                    break;
                case WAVE_OUT:
                    addToContainer(R.drawable.right_black);
                    toast("OUT");
                    break;
                case FINGERS_SPREAD:
                    addToContainer(R.drawable.spread_black);
                    toast("SPREAD");
                    break;
            }

//            if (pose != Pose.UNKNOWN && pose != Pose.REST) {
//                // Tell the Myo to stay unlocked until told otherwise. We do that here so you can
//                // hold the poses without the Myo becoming locked.
//                myo.unlock(Myo.UnlockType.HOLD);
//
//                // Notify the Myo that the pose has resulted in an action, in this case changing
//                // the text on the screen. The Myo will vibrate.
//                myo.notifyUserAction();
//            } else {
//                // Tell the Myo to stay unlocked only for a short period. This allows the Myo to
//                // stay unlocked while poses are being performed, but lock after inactivity.
//                myo.unlock(Myo.UnlockType.TIMED);
//            }
        }
    };


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


            PlayAnim(image, this, R.anim.slide_right_in, 0);

            image.setTag(resId);
            containerLayout.addView(image);


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



    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getColoredArrow());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        TalkActivity.this.overridePendingTransition(R.anim.right_in,
                R.anim.left_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                onScanActionSelected();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Activity is gone, so unregister the listener.
        Hub.getInstance().removeListener(mListener);

        if (isFinishing()) {
            // The Activity is finishing, so shutdown the Hub. This will disconnect from the Myo.
            Hub.getInstance().shutdown();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    private void onScanActionSelected() {
        // Launch the ScanActivity to scan for Myos to connect to.
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }


    private void toast(String txt){
        Toast.makeText(getBaseContext(),txt, Toast.LENGTH_SHORT).show();
    }

    /**
     * ფონტები
     */
    private void setTypeFaces(){
        Typeface font = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "BPG_GEL_Excelsior.ttf");

        helpTextView.setTypeface(font);
        resetButton.setTypeface(font);
    }

    /**
     * ლისენერები
     */
    private void attachListeners(){
        resetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.talk_reset_button_id:
                resetCombination();
                break;
        }
    }

    /**
     * წავშალოთ კომბინაცია
     */
    private void resetCombination(){
        if (containerLayout != null){
            containerLayout.removeAllViews();
        }
    }
}
