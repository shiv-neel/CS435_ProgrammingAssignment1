import java.util.BitSet;
import java.util.Random;

public class MultiMultiBloomFilter {
    private BitSet[] bitSets;
    private int numArrays;
    private int arraySize;
    private int dataSize;
    private int[] aValues;
    private int[] bValues;
    private int prime;

    public MultiMultiBloomFilter(int setSize, int numArrays) {
        this.numArrays = numArrays;
        this.arraySize = setSize;
        this.bitSets = new BitSet[numArrays];
        for (int i = 0; i < numArrays; i++) {
            bitSets[i] = new BitSet(arraySize);
        }
        this.dataSize = 0;
        this.prime = findNextPrime(arraySize+1);

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
            bitSets[i].set(Math.abs(hash % arraySize));
        }
        dataSize++;
    }

    public boolean appears(String s) {
        s = s.toLowerCase();
        for (int i = 0; i < numArrays; i++) {
            int hash = randomHash(s, i);
            if (!bitSets[i].get(Math.abs(hash % arraySize))) {
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
}
