import java.util.BitSet;
import java.util.Random;

public class BloomFilterRan {
    private BitSet bitSet;
    private int setSize;
    private int bitsPerElement;
    private int filterSize;
    private int dataSize;
    private int numHashes;
    private Random random;
    private int primeP;
    private int[] aValues;
    private int[] bValues;

    public BloomFilterRan(int setSize, int bitsPerElement) {
        this.setSize = setSize;
        this.bitsPerElement = bitsPerElement;
        this.filterSize = (int) Math.ceil(setSize * bitsPerElement);
        this.dataSize = 0;
        this.random = new Random();
        this.numHashes = (int) Math.ceil((Math.log(2) * filterSize) / setSize);
        this.bitSet = new BitSet(filterSize);

        // Choose a prime p slightly larger than filterSize
        primeP = findNextPrime(filterSize + 1);
        aValues = new int[numHashes];
        bValues = new int[numHashes];

        // Generate random a and b values
        for (int i = 0; i < numHashes; i++) {
            aValues[i] = random.nextInt(primeP);
            bValues[i] = random.nextInt(primeP);
        }
    }

    private int findNextPrime(int n) {
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }

    private boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private int randomHash(String s, int i) {
        int hash = (aValues[i] * s.hashCode() + bValues[i]) % primeP;
        return (hash % filterSize);
    }

    public void add(String s) {
        for (int i = 0; i < numHashes; i++) {
            int hash = randomHash(s, i);
            bitSet.set(hash, true);
        }
        dataSize++;
    }

    public boolean appears(String s) {
        for (int i = 0; i < numHashes; i++) {
            int hash = randomHash(s, i);
            if (!bitSet.get(hash)) {
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
}
