package baasi.hackathon.sja.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import baasi.hackathon.sja.R;
import baasi.hackathon.sja.model.TalkHand;


/**
 * Created by shota on 12/18/2015.
 */
public class TalkerAdapter extends BaseAdapter {
    private ArrayList<TalkHand> talkHands;
    private Context context;
    private LayoutInflater inflater;

    public TalkerAdapter(ArrayList<TalkHand> talkHands, Context context) {
        this.talkHands = talkHands;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return talkHands.size();
    }

    @Override
    public Object getItem(int position) {
        return talkHands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item;
        MyHolder holder;

        if (convertView == null){
            item = inflater.inflate(R.layout.list_item,null);
            holder = new MyHolder();

            LinearLayout gesturesContainer = (LinearLayout) item.findViewById(R.id.combinations_container_id);

            holder.holderWord = (TextView) item.findViewById(R.id.word_id);
            holder.holderLayout = gesturesContainer;

            item.setTag(holder);
        }else {
            item = convertView;
            holder = (MyHolder) item.getTag();
        }

        TalkHand mTalkHand = (TalkHand) getItem(position);

        holder.holderWord.setText(mTalkHand.getWord());
        setTypeFaces(holder.holderWord);


        try {
            for (int i = 0; i < mTalkHand.getGesturesList().size(); i++) {
                ImageView image = new ImageView(context);
                image.setBackgroundResource(mTalkHand.getGesturesList().get(i));
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(70, 70);
                image.setLayoutParams(parms);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.holderLayout.addView(image);
            }
        }catch (Exception e) {
        }


        return item;
    }



    private class MyHolder {
        TextView holderWord;
        LinearLayout holderLayout;
    }



    private void setTypeFaces(TextView textView){
        Typeface font = Typeface.createFromAsset(
                context.getAssets(),
                "BPG_GEL_Excelsior.ttf");

        textView.setTypeface(font);
    }

}
