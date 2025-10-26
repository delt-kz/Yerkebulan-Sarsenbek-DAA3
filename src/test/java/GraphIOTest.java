import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphIOTest {

    private Locale defaultLocale;

    @BeforeEach
    void rememberDefaultLocale() {
        defaultLocale = Locale.getDefault();
    }

    @AfterEach
    void restoreDefaultLocale() {
        Locale.setDefault(defaultLocale);
    }

    @Test
    void writeComparisonCsvUsesInvariantLocale() throws IOException {
        Locale.setDefault(Locale.GERMANY);

        Path tempFile = Files.createTempFile("comparison", ".csv");

        MSTResult prim = new MSTResult(List.of(), 16.0, 10, 15, 5, 4.603, true);
        MSTResult kruskal = new MSTResult(List.of(), 16.0, 11, 14, 6, 2.63, true);
        GraphComputationResult result = new GraphComputationResult(
                "small-1",
                new InputStats(5, 7),
                prim,
                kruskal
        );

        try {
            GraphIO.writeComparisonCsv(tempFile, List.of(result));

            String csv = Files.readString(tempFile);

            assertTrue(csv.contains("small-1,16.000,16.000,4.603,2.630,10,15,5,30,11,14,6,31"));
            assertTrue(csv.lines().findFirst().orElseThrow().contains("prim_comparisons"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
