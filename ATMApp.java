import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ATMApp {

    private static class Transaction {
        private final String type;
        private final double amount;
        private final double balanceAfter;

        public Transaction(String type, double amount, double balanceAfter) {
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
        }

        @Override
        public String toString() {
            return type + ": Rs " + String.format("%.2f", amount) +
                    " | Balance: Rs " + String.format("%.2f", balanceAfter);
        }
    }

    private static class Account {
        private final String cardNumber;
        private final String pin;
        private double balance;
        private final List<Transaction> transactions = new ArrayList<>();

        public Account(String cardNumber, String pin, double initialBalance) {
            this.cardNumber = cardNumber;
            this.pin = pin;
            this.balance = initialBalance;
        }

        public boolean authenticate(String cardNumber, String pin) {
            return this.cardNumber.equals(cardNumber) && this.pin.equals(pin);
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            balance += amount;
            transactions.add(new Transaction("DEPOSIT", amount, balance));
        }

        public boolean withdraw(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            if (amount > balance) {
                return false;
            }
            balance -= amount;
            transactions.add(new Transaction("WITHDRAW", amount, balance));
            return true;
        }

        public List<Transaction> getRecentTransactions(int max) {
            int fromIndex = Math.max(0, transactions.size() - max);
            return transactions.subList(fromIndex, transactions.size());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Account account = new Account("1234567890", "1234", 5000.0);

        System.out.println("============================");
        System.out.println("        ATM MACHINE");
        System.out.println("============================");

        if (!login(scanner, account)) {
            System.out.println("Too many failed attempts. Card blocked.");
            return;
        }

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
                    System.out.println("Current balance: Rs " + String.format("%.2f", account.getBalance()));
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double depAmount = readPositiveDouble(scanner);
                    try {
                        account.deposit(depAmount);
                        System.out.println("Deposited successfully. New balance: Rs " +
                                String.format("%.2f", account.getBalance()));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double withAmount = readPositiveDouble(scanner);
                    try {
                        boolean success = account.withdraw(withAmount);
                        if (success) {
                            System.out.println("Please collect your cash. New balance: Rs " +
                                    String.format("%.2f", account.getBalance()));
                        } else {
                            System.out.println("Insufficient balance.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Last 5 transactions:");
                    List<Transaction> lastTx = account.getRecentTransactions(5);
                    if (lastTx.isEmpty()) {
                        System.out.println("No transactions yet.");
                    } else {
                        for (Transaction t : lastTx) {
                            System.out.println(" - " + t);
                        }
                    }
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        } while (choice != 5);

        scanner.close();
    }

    private static boolean login(Scanner scanner, Account account) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter card number: ");
            String cardNumber = scanner.nextLine().trim();
            if (cardNumber.isEmpty()) {
                cardNumber = scanner.nextLine().trim();
            }

            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine().trim();
            if (pin.isEmpty()) {
                pin = scanner.nextLine().trim();
            }

            if (account.authenticate(cardNumber, pin)) {
                System.out.println("Login successful.\n");
                return true;
            } else {
                attempts++;
                System.out.println("Incorrect card number or PIN. Attempts left: " + (3 - attempts));
            }
        }
        return false;
    }

    private static void printMenu() {
        System.out.println("----------- MENU -----------");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. View Last 5 Transactions");
        System.out.println("5. Exit");
    }

    private static double readPositiveDouble(Scanner scanner) {
        double value;
        while (true) {
            while (!scanner.hasNextDouble()) {
                System.out.print("Invalid amount. Enter a number: ");
                scanner.next();
            }
            value = scanner.nextDouble();
            if (value > 0) {
                return value;
            }
            System.out.print("Amount must be positive. Enter again: ");
        }
    }
}
