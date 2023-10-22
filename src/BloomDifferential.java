import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BloomDifferential {
    private BloomFilterRan bloomFilter;

    public BloomDifferential(String differentialFile) {
        // Create a Bloom Filter from the differential file
        this.bloomFilter = createFilter(differentialFile);
    }

    private BloomFilterRan createFilter(String differentialFile) {
        BloomFilterRan bloomFilter = new BloomFilterRan(1200000, 8); 

        try (BufferedReader reader = new BufferedReader(new FileReader(differentialFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line in the differential file contains a key
                bloomFilter.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bloomFilter;
    }

    public String retrieveRecord(String key) {
        if (bloomFilter.appears(key)) {
            try {
                // Load and search the differential file for the key
                try (BufferedReader reader = new BufferedReader(new FileReader("DiffFile.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Assuming each line in the differential file contains a key and record
                        String[] parts = line.split(" ", 2);
                        if (parts[0].equals(key)) {
                            return parts[1];
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // If not found in the Bloom Filter or the differential file, consult database.txt
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader("database.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Assuming each line in database.txt contains a key and record
                    String[] parts = line.split(" ", 2);
                    if (parts[0].equals(key)) {
                        return parts[1];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Key not found
    }
}
