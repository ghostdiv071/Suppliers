public enum ConnectionUtils {
    URL("jdbc:postgresql://127.0.0.1:5432/Suppliers"),
    USER("postgres"),
    PASSWORD("anna");

    public final String value;

    ConnectionUtils(String value) {
        this.value = value;
    }
}
