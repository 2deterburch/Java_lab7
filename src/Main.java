
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        // Назва файлу для варіанта №4
        String fileName = "v4_transactions.txt";

        try (Stream<String> lines =
                     Files.lines(Paths.get(fileName),
                             StandardCharsets.UTF_8)) {

            // 1. Зчитування та обробка даних
            List<Transaction> transactions = lines
                    .skip(1)
                    .filter(line -> !line.trim().isEmpty())
                    .map(Transaction::fromCsv)
                    .distinct()
                    .collect(Collectors.toList());

            System.out.println("===== TRANSACTION REPORT =====");

            System.out.println("Unique transactions: "
                    + transactions.size());

            // 2. Групування за категоріями
            System.out.println("\n===== CATEGORY SUMMARY =====");

            Map<String, List<Transaction>> categories =
                    transactions.stream()
                            .collect(Collectors.groupingBy(
                                    Transaction::getCategory,
                                    TreeMap::new,
                                    Collectors.toList()
                            ));

            categories.forEach((category, list) -> {

                long count = list.size();

                BigDecimal total = list.stream()
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO,
                                BigDecimal::add);

                System.out.println(
                        category +
                                " | Count: " + count +
                                " | Total: " + total.toPlainString()
                );
            });

            // 3. TOP-5 транзакцій за сумою
            System.out.println("\n===== TOP 5 TRANSACTIONS =====");

            transactions.stream()
                    .sorted(
                            Comparator.comparing(
                                    Transaction::getAmount
                            ).reversed().thenComparing(
                                    Transaction::getId
                            )
                    )
                    .limit(5)
                    .forEach(System.out::println);

            // 4. Пошук транзакції за ID
            System.out.println("\n===== SEARCH TRANSACTION =====");

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter transaction ID: ");

            String searchKey = scanner.nextLine().trim();

            Optional<Transaction> result = transactions.stream()
                    .filter(t -> !searchKey.isEmpty()
                            && t.getId().equals(searchKey))
                    .findFirst();

            result.ifPresentOrElse(
                    transaction -> System.out.println(
                            "Found: " + transaction
                    ),
                    () -> System.out.println("Not found")
            );

        } catch (IOException e) {

            System.out.println(
                    "File reading error: " + e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Data processing error: " + e.getMessage()
            );
        }
    }
}
