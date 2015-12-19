package hackathon.hacktbilisi.talkerhand.util;

import java.util.ArrayList;

import hackathon.hacktbilisi.talkerhand.R;

/**
 * Created by shota on 12/19/2015.
 */
public class MyImages {
   public static ArrayList<Integer> images;

    public MyImages() {
        images.add(R.drawable.left_black);
        images.add(R.drawable.right_black);
        images.add(R.drawable.spread_black);
        images.add(R.drawable.mushti_black);
    }

    public static int getImg(int i){
        return images.get(i);
    }
}
