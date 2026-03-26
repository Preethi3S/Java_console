import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SupermarketApp {

    private static class Product {
        private final int id;
        private final String name;
        private final double price;
        private int stock;

        public Product(int id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }

        public void reduceStock(int qty) {
            stock -= qty;
        }

        @Override
        public String toString() {
            return String.format("%d - %s | Rs %.2f | Stock: %d", id, name, price, stock);
        }
    }

    private static class CartItem {
        private final Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void addQuantity(int qty) {
            this.quantity += qty;
        }

        public double getTotalPrice() {
            return product.getPrice() * quantity;
        }

        @Override
        public String toString() {
            return product.getName() + " x" + quantity + " = Rs " + String.format("%.2f", getTotalPrice());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Product> products = seedProducts();
        List<CartItem> cart = new ArrayList<>();

        System.out.println("============================");
        System.out.println("      SUPERMARKET APP");
        System.out.println("============================");

        int choice;
        do {
            printMenu();
            System.out.print("Enter choice: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                scanner.next();
            }
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    listProducts(products);
                    break;
                case 2:
                    listProducts(products);
                    System.out.print("Enter product ID to add: ");
                    int id = readInt(scanner);
                    Product product = findProductById(products, id);
                    if (product == null) {
                        System.out.println("Product not found.");
                        break;
                    }
                    System.out.print("Enter quantity: ");
                    int qty = readInt(scanner);
                    if (qty <= 0) {
                        System.out.println("Quantity must be positive.");
                        break;
                    }
                    if (qty > product.getStock()) {
                        System.out.println("Not enough stock. Available: " + product.getStock());
                        break;
                    }
                    addToCart(cart, product, qty);
                    product.reduceStock(qty);
                    System.out.println("Added to cart.");
                    break;
                case 3:
                    viewCart(cart);
                    break;
                case 4:
                    checkout(scanner, cart);
                    break;
                case 5:
                    System.out.println("Exiting Supermarket app. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        } while (choice != 5);

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("----------- MENU -----------");
        System.out.println("1. List Products");
        System.out.println("2. Add Product to Cart");
        System.out.println("3. View Cart");
        System.out.println("4. Checkout");
        System.out.println("5. Exit");
    }

    private static List<Product> seedProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Milk", 45.0, 20));
        products.add(new Product(2, "Bread", 30.0, 15));
        products.add(new Product(3, "Eggs (12 pcs)", 70.0, 25));
        products.add(new Product(4, "Rice (1kg)", 60.0, 30));
        products.add(new Product(5, "Apples (1kg)", 120.0, 10));
        return products;
    }

    private static void listProducts(List<Product> products) {
        System.out.println("Available products:");
        for (Product p : products) {
            System.out.println(" - " + p);
        }
    }

    private static Product findProductById(List<Product> products, int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private static void addToCart(List<CartItem> cart, Product product, int qty) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                item.addQuantity(qty);
                return;
            }
        }
        cart.add(new CartItem(product, qty));
    }

    private static void viewCart(List<CartItem> cart) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        double total = 0.0;
        System.out.println("Your cart:");
        for (CartItem item : cart) {
            System.out.println(" - " + item);
            total += item.getTotalPrice();
        }
        System.out.println("Total: Rs " + String.format("%.2f", total));
    }

    private static void checkout(Scanner scanner, List<CartItem> cart) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Add items before checkout.");
            return;
        }
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
        }

        double discount = 0.0;
        if (total >= 1000) {
            discount = 0.10 * total;
        } else if (total >= 500) {
            discount = 0.05 * total;
        }

        double finalAmount = total - discount;

        System.out.println("------ BILL ------");
        viewCart(cart);
        System.out.println("Subtotal: Rs " + String.format("%.2f", total));
        System.out.println("Discount: Rs " + String.format("%.2f", discount));
        System.out.println("Amount to pay: Rs " + String.format("%.2f", finalAmount));

        System.out.print("Enter cash received: ");
        double cash = readDouble(scanner);
        if (cash < finalAmount) {
            System.out.println("Not enough cash. Transaction cancelled.");
            return;
        }
        double change = cash - finalAmount;
        System.out.println("Change: Rs " + String.format("%.2f", change));
        System.out.println("Thank you for shopping!");
        cart.clear();
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid number. Enter again: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static double readDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid amount. Enter again: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
