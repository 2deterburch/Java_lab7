
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Transaction {

    private String id;
    private String category;
    private BigDecimal amount;
    private LocalDate date;

    public Transaction(String id, String category,
                       BigDecimal amount, LocalDate date) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    // Створення об'єкта з рядка CSV
    public static Transaction fromCsv(String line) {
        String[] parts = line.split(";", -1);

        if (parts.length != 4) {
            throw new IllegalArgumentException(
                    "Invalid transaction: " + line
            );
        }

        return new Transaction(
                parts[0].trim(),
                parts[1].trim(),
                new BigDecimal(parts[2].trim()),
                LocalDate.parse(parts[3].trim())
        );
    }

    // Повторні транзакції визначаються за ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Transaction that = (Transaction) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
