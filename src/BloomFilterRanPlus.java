import java.util.BitSet;
import java.util.Random;

public class BloomFilterRanPlus {
    private BitSet bitSet;
    private int filterSize;
    private int dataSize;
    private int numHashes;
    private int prime;
    private int[] aValues;
    private int[] bValues;
    private int[] cValues;

    public BloomFilterRanPlus(int setSize, int bitsPerElement) {
        this.filterSize = setSize * bitsPerElement;
        this.bitSet = new BitSet(filterSize);
        this.numHashes = (int) Math.round(Math.log(2) * filterSize / setSize);
        this.dataSize = 0;
        this.prime = findNextPrime(filterSize) + 1; // slightly larger than M
        this.aValues = new int[numHashes];
        this.bValues = new int[numHashes];
        this.cValues = new int[numHashes];
        Random rand = new Random();
        for (int i = 0; i < numHashes; i++) {
            aValues[i] = rand.nextInt(prime-1);
            bValues[i] = rand.nextInt(prime-1);
            cValues[i] = rand.nextInt(prime-1);
        }
    }

    public void add(String s) {
        s = s.toLowerCase();
        for (int i = 0; i < numHashes; i++) {
            int hash = randomHash(s, i);
            bitSet.set(Math.abs(hash % filterSize));
        }
        dataSize++;
    }

    public boolean appears(String s) {
        s = s.toLowerCase();
        for (int i = 0; i < numHashes; i++) {
            int hash = randomHash(s, i);
            if (!bitSet.get(Math.abs(hash % filterSize))) {
                // if one of the locations is false, then the element is not in the set
                return false;
            }
        }
        return true;
    }

    public int filterSize() {
        return filterSize;
    }

    public int dataSize() {
        return dataSize;
    }

    public int numHashes() {
        return numHashes;
    }

    public boolean getBit(int j) {
        return bitSet.get(j);
    }

    private int randomHash(String s, int i) {
        int hash = 0;
        for (char c : s.toCharArray()) {
            hash += c;
        }
        // hash function of (ax^2 + bx + c) % p
        return (aValues[i] * hash * hash + bValues[i] * hash + cValues[i]) % prime;
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
}
