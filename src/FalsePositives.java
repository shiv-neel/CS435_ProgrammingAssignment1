import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class FalsePositives {
    public static void main(String[] args) throws IOException {
        int filterSize = 40000;
        int setSize = 10000;
        int bitsPerElement = filterSize / setSize;
        int expectedInsertions = 1000; 
        double falsePositiveRate = 0.01; 

        // Create the three random bloom filters
        BloomFilterRan bfr = new BloomFilterRan(setSize, bitsPerElement);
        BloomFilterRanPlus bfrp = new BloomFilterRanPlus(setSize, bitsPerElement);
        MultiMultiBloomFilter mmbf = new MultiMultiBloomFilter(setSize, expectedInsertions);

        // add words to bloom filters
        loadWordsIntoBFR("FreqWords.txt", bfr);
        loadWordsIntoBFRP("FreqWords.txt", bfrp);
        loadWordsIntoMMBF("FreqWords.txt", mmbf);

        // test bloom filters for positives
        testFalsePositives("test_file.txt", bloomFilter);
    }

    private static void loadWordsIntoBFR(String filename, BloomFilterRan bfr) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bfr.add(line);
            }
        }
    }

    private static void loadWordsIntoBFRP(String filename, BloomFilterRanPlus bfpr) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bfpr.add(line);
            }
        }
    }

    private static void loadWordsIntoMMBF(String filename, MultiMultiBloomFilter mmbf) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                mmbf.add(line);
            }
        }
    }

    private static void testFalsePositives(String testFilename, BloomFilter<String> bloomFilter) throws IOException {
        int falsePositives = 0;
        int totalItems = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(testFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalItems++;
                if (bloomFilter.mightContain(line)) {
                    falsePositives++;
                }
            }
        }

        System.out.println("Total items tested: " + totalItems);
        System.out.println("False positives: " + falsePositives);
        System.out.println("False positive rate: " + (falsePositives / (double) totalItems));
    }
}