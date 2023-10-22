/*
Very similar to BloomFilterRan, but to pick a random hash function, (randomly) pick a prime,
and pick a, b, c uniformly at random from {0, · · · , p − 1}. The hash function is (ax2 + bx + c)%p
 */

import java.util.BitSet;
import java.util.Random;

public class BloomFilterRanPlus extends BloomFilterRan {

    public BloomFilterRanPlus(int setSize, int bitsPerElement) {
        super(setSize, bitsPerElement);
    }
}
