package baasi.hackathon.sja.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by shota on 12/20/2015.
 */
public class MyGson {

    public static ArrayList<Integer> getIntegers(String fromGsonString){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        return gson.fromJson(fromGsonString,type);
    }

    /**
     * გადავიყვანოთ ლისტი ჯსონ ობიექტად (სტრინგი)
     * @param list ლისტი
     * @return ჯსონ სტრინგი
     */
    public static String convertListToJson(ArrayList<Integer> list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
