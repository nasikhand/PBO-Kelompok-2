// Create new file: TransportOption.java
package model; // Or another appropriate package

public class TransportOption {
    private String name;
    private double cost;

    public TransportOption(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    // This is how the item will appear in the JComboBox
    @Override
    public String toString() {
        if (cost > 0) {
            return String.format("%s - Rp %,.0f", name, cost);
        }
        return name; // For free options like "Walking"
    }
}