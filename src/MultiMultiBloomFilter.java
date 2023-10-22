import java.util.BitSet;
import java.util.Random;

public class MultiMultiBloomFilter {
    private final BitSet[] bitSets; // array of bitsets

    private final int filterSize; // size of bloom filter (M)
    private final int numArrays; // number of arrays (k)
    private final int setSize; // size of set (N)
    private int dataSize; // number of items added to the set (n)
    private final int[] aValues;
    private final int[] bValues;
    private final int prime;

    public MultiMultiBloomFilter(int setSize, int bitsPerElement) {
        this.filterSize = setSize * bitsPerElement;
        this.numArrays = (int) Math.round(Math.log(2) * filterSize / setSize);;
        this.setSize = setSize;
        this.bitSets = new BitSet[numArrays];
        for (int i = 0; i < numArrays; i++) {
            bitSets[i] = new BitSet(this.setSize);
        }
        this.dataSize = 0;
        this.prime = findNextPrime(this.setSize +1);

        // Generate hash function params
        Random rand = new Random();
        this.aValues = new int[numArrays];
        this.bValues = new int[numArrays];
        for (int i = 0; i < numArrays; i++) {
            aValues[i] = rand.nextInt(prime);
            bValues[i] = rand.nextInt(prime);
        }
    }

    public void add(String s) {
        s = s.toLowerCase();
        for (int i = 0; i < numArrays; i++) {
            int hash = randomHash(s, i);
            bitSets[i].set(Math.abs(hash % setSize));
        }
        dataSize++;
    }

    public boolean appears(String s) {
        s = s.toLowerCase();
        for (int i = 0; i < numArrays; i++) {
            int hash = randomHash(s, i);
            if (!bitSets[i].get(Math.abs(hash % setSize))) {
                return false;
            }
        }
        return true;
    }

    private int randomHash(String s, int i) {
        int hash = 0;
        for (char c : s.toCharArray()) {
            hash += c;
        }
        return (aValues[i] * hash + bValues[i]) % prime;
    }

    private int findNextPrime(int num) {
        int prime = num;
        while (true) {
            if (isPrime(prime)) {
                return prime;
            }
            prime++;
        }
    }

    private boolean isPrime(int num) {
        if (num == 2) {
            return true;
        }
        if (num % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public int getFilterSize() {
        return filterSize;
    }

    public int getDataSize() {
        return dataSize;
    }
}
