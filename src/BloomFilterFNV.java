import java.util.BitSet;

public class BloomFilterFNV {
    private BitSet bitSet;
    private int filterSize;
    private int dataSize;
    private int numHashes;

    public BloomFilterFNV(int setSize, int bitsPerElement) {
        this.filterSize = setSize * bitsPerElement;
        this.bitSet = new BitSet(filterSize);
        this.numHashes = (int) Math.round(Math.log(2) * filterSize / setSize);
        this.dataSize = 0;
    }

    public void add(String s) {
        s = s.toLowerCase();
        for (int i = 0; i < numHashes; i++) {
            int hash = fnvHash(s, i);
            bitSet.set(Math.abs(hash % filterSize));
        }
        dataSize++;
    }

    public boolean appears(String s) {
        s = s.toLowerCase();
        for (int i = 0; i < numHashes; i++) {
            int hash = fnvHash(s, i);
            if (!bitSet.get(Math.abs(hash % filterSize))) {
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

    private int fnvHash(String s, int i) {
        final int FNV_PRIME = 0x01000193; //16777619
        int hash = 0x811C9DC5; //2166136261
        for (char c : s.toCharArray()) {
            hash = (hash ^ c) * FNV_PRIME;
        }
        return hash ^ i;
    }
}
