import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class BloomFilterFNV {
    private BitSet bitSet;
    private int setSize;
    private int bitsPerElement;
    private int filterSize;
    private int dataSize;
    private int numHashes;

    public BloomFilterFNV(int setSize, int bitsPerElement) {
        this.setSize = setSize;
        this.bitsPerElement = bitsPerElement;
        this.filterSize = (int) Math.ceil(setSize * bitsPerElement);
        this.dataSize = 0;
        this.numHashes = (int) Math.ceil((Math.log(2) * filterSize) / setSize);
        this.bitSet = new BitSet(filterSize);
    }

    private int hashFNV(String s, int seed) {
        long fnvPrime = 1099511628211L; // constant prime number for FNV hash
        long hash = 14695981039346656037L; // initialize FNV hash with another constant

        s = s.toLowerCase(); // Make it case-insensitive

        for (int i = 0; i < s.length(); i++) {
            hash ^= s.charAt(i); // XOR the hash with the current character's ASCII value
            hash *= fnvPrime; //
        }

        return (int) (hash ^ seed) & 0x7FFFFFFF; // Ensure non-negative value
    }

    public void add(String s) {
        for (int i = 0; i < numHashes; i++) {
            int hash = hashFNV(s, i);
            bitSet.set(hash % filterSize, true);
        }
        dataSize++;
    }

    public boolean appears(String s) {
        s = s.toLowerCase(); // Make it case-insensitive

        for (int i = 0; i < numHashes; i++) {
            int hash = hashFNV(s, i);
            if (!bitSet.get(hash % filterSize)) {
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
