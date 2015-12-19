package baasi.hackathon.sja.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jemo on 12/18/2015.
 */
public class TalkHand implements Serializable {
    private int id;
    private String word;
    private ArrayList<Integer> gesturesList;

    public TalkHand(int id, String word, ArrayList<Integer> gestList) {
        this.id = id;
        this.word = word;
        this.gesturesList = gestList;
    }

    public TalkHand() {  }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<Integer> getGesturesList() {
        return gesturesList;
    }

    public void setGesturesList(ArrayList<Integer> gesturesList) {
        this.gesturesList = gesturesList;
    }

    public int getId() {
        return id;
    }
}
