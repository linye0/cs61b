import static org.junit.Assert.*;
import org.junit.Test;

import java.util.LinkedList;

public class TestArrayDequeGold {
    @Test
    public void testEqual() {
        LinkedList<Integer> ads = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            if (i % 5 == 0) {
                msg.append("size()\n");
                assertEquals(msg.toString(), ads.size(), sad.size());
            }
            int s = 0;
            double numBetweenZeroAndOne = StdRandom.uniform();
            if (numBetweenZeroAndOne < 0.25) {
                ads.addFirst(i);
                sad.addFirst(i);
                s++;
                msg.append("addFirst(" + i + ")\n");
                assertEquals(msg.toString(), ads.get(0), sad.get(0));
            } else if (numBetweenZeroAndOne < 0.5) {
                ads.addLast(i);
                sad.addLast(i);
                s++;
                msg.append("addLast(" + i + ")\n");
                assertEquals(msg.toString(), ads.get(s - 1), sad.get(s - 1));
            } else if (numBetweenZeroAndOne < 0.75) {
                if (ads.isEmpty()) {
                    msg.append("isEmpty()\n");
                    assertTrue(msg.toString(), ads.isEmpty());
                    continue;
                }
                Integer numAds = ads.removeFirst();
                Integer numSad = sad.removeFirst();
                s--;
                msg.append("removeFirst()\n");
                assertEquals(msg.toString(), numAds, numSad);
            } else {
                if (ads.isEmpty()) {
                    msg.append("isEmpty()\n");
                    assertTrue(msg.toString(), ads.isEmpty());
                    continue;
                }
                Integer numAds = ads.removeLast();
                Integer numSad = sad.removeLast();
                s--;
                msg.append("removeLast()\n");
                assertEquals(msg.toString(), numAds, numSad);
            }
        }
    }
}
