package test.mc.alk.scoreboardapi;

import junit.framework.TestCase;
import mc.alk.scoreboardapi.api.SEntry;
import mc.alk.scoreboardapi.scoreboard.SAPIEntry;
import mc.alk.scoreboardapi.scoreboard.SAPIScore;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * @author alkarin
 */
public class TestBObjective extends TestCase {
    HashMap<String, SEntry> emap = new HashMap<String,SEntry>();
    HashMap<SEntry, SAPIScore> map = new HashMap<SEntry, SAPIScore>();
    TreeSet<SAPIScore> scores = new TreeSet<SAPIScore>(new Comparator<SAPIScore>() {
        @Override
        public int compare(SAPIScore o1, SAPIScore o2) {
            int c = o2.getScore() - o1.getScore();
            if (c != 0)
                return c;
            return o1.getEntry().getID().compareTo(o2.getEntry().getID());
        }
    });

    public SAPIScore getScore(String s) {
        return map.get(emap.get(s));
    }

    public void testBObjective(){
        for (int i=0;i< 20;i++) {
            SAPIEntry se = new SAPIEntry("p"+i,"p"+i);
            SAPIScore sc = new SAPIScore(se,i);
            map.put(se, sc);
            emap.put("p" + i, se);
            scores.add(sc);
        }
        SAPIScore score = getScore("p3");
        scores.remove(score);

        for (SAPIScore sc : scores){
            assertFalse(sc.getEntry().getID().equals("p3"));
        }

        score = getScore("p4");
        score.setScore(23);
        scores.remove(score);
        for (SAPIScore sc : scores){
            assertFalse(sc.getEntry().getID().equals("p4"));
        }

    }
}
