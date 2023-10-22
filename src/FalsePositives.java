import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



public class FalsePositives {
    private static final int setSize = 10000; // size of data set (N)

    private static final int bitsPerElement = 8; // number of bits per element (b)
    private static final int filterSize = setSize * bitsPerElement; // size of bloom filter (M)


    public static void main(String[] args) throws IOException {
        final String database = "src/data/fp_database.txt";
        final String differential = "src/data/fp_test.txt";

        BloomFilterFNV bloomFilterFNV = new BloomFilterFNV(setSize, bitsPerElement);
        BloomFilterRan bloomFilterRan = new BloomFilterRan(setSize, bitsPerElement);
        BloomFilterRanPlus bloomFilterRanPlus = new BloomFilterRanPlus(setSize, bitsPerElement);
        MultiMultiBloomFilter multiMultiBloomFilter = new MultiMultiBloomFilter(setSize, bitsPerElement);

        // add words to bloom filters

        loadWordsIntoBloomFilterRan(database, bloomFilterRan);
        loadWordsIntoBloomFilterRanPlus(database, bloomFilterRanPlus);
        loadWordsIntoMultiMultiBloomFilter(database, multiMultiBloomFilter);

        // test bloom filters for positives
        testFalsePositivesForBloomFilterRan(differential, bloomFilterRan);
    }

    private static void loadWordsIntoBloomFilterFNV(String filename, BloomFilterFNV bloomFilterFNV) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bloomFilterFNV.add(line);
            }
        }
    }

    private static void loadWordsIntoBloomFilterRan(String filename, BloomFilterRan bloomFilterRan) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bloomFilterRan.add(line);
            }
        }
    }

    private static void loadWordsIntoBloomFilterRanPlus(String filename, BloomFilterRanPlus bloomFilterRanPlus) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bloomFilterRanPlus.add(line);
            }
        }
    }

    private static void loadWordsIntoMultiMultiBloomFilter(String filename, MultiMultiBloomFilter multiMultiBloomFilter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                multiMultiBloomFilter.add(line);
            }
        }
    }

    private static void testFalsePositivesForBloomFilterFNV(String testFilename, BloomFilterFNV bloomFilterFNV) throws IOException {
        int falsePositives = 0;
        int totalItems = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(testFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalItems++;
                if (bloomFilterFNV.appears(line)) {
                    falsePositives++;
                }
            }
        }

        System.out.println("Total items tested: " + totalItems);
        System.out.println("False positives: " + falsePositives);
        System.out.println("False positive rate: " + (falsePositives / (double) totalItems));
    }

    private static void testFalsePositivesForBloomFilterRan(String testFilename, BloomFilterRan bloomFilterRan) throws IOException {
        int falsePositives = 0;
        int totalItems = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(testFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalItems++;
                if (bloomFilterRan.appears(line)) {
                    falsePositives++;
                }
            }
        }

        System.out.println("Total items tested: " + totalItems);
        System.out.println("False positives: " + falsePositives);
        System.out.println("False positive rate: " + (falsePositives / (double) totalItems));
    }
}