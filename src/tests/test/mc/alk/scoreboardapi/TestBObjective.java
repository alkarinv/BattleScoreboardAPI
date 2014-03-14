package test.mc.alk.scoreboardapi;

import junit.framework.TestCase;
import mc.alk.scoreboardapi.SAPIUtil;
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
    public void testIt(){

    }
    public void testScoreboardName(){
        int max = 32;
        int[] a = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        char[] b = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        char[] c = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        for (int i =0;i<40;i++) {
            String s1 = "";
            String s2 = "";
            for (int k = 0 ; k < i; k++) {
                s2 += a[k % 10] + "";
            }
            for (int j = 0 ; j < 10; j++){
                s1 += b[j % 10] + "";
                String s3 = "";
                for (int k = 0 ; k < 10; k++) {
                    s3 += c[k % 10] + "";

                    String s = s1 + s2 + s3;
                    String n = "";
                    try{
                        n = SAPIUtil.createLimitedString(s1, s2, s3, max);
                    } catch (Exception e){
                        System.out.println(n +" " + n.length() +"  " +s1 +"  " + s2 +"   " + s3);
                        e.printStackTrace();
                    }
//                    System.out.println(n +" " + n.length() +"  ");
                    assertEquals( "  "+s1.length() +" + " + s2.length() +" + " + s3.length() +" != " + n.length()
                            +"   p=" + s1 +"   b=" + s2 +"   s=" + s3 +"   n="+n,
                             true, Math.abs(Math.min(s.length(), max) - n.length()) < 4 );
                    assertEquals( "  "+s1.length() +" + " + s2.length() +" + " + s3.length()
                            +"   p=" + s1 +"   b=" + s2 +"   s=" + s3,
                            true, Math.min(s.length(), max) >= n.length());

                }
            }
        }

    }
}
