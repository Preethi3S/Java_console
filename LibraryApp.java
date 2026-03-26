import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryApp {

    private static class Book {
        private final int id;
        private final String title;
        private final String author;
        private boolean available = true;
        private String borrowedBy;

        public Book(int id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public boolean isAvailable() {
            return available;
        }

        public String getBorrowedBy() {
            return borrowedBy;
        }

        public void borrow(String memberName) {
            this.available = false;
            this.borrowedBy = memberName;
        }

        public void giveBack() {
            this.available = true;
            this.borrowedBy = null;
        }

        @Override
        public String toString() {
            String status = available ? "Available" : "Borrowed by " + borrowedBy;
            return String.format("%d - %s by %s [%s]", id, title, author, status);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Book> books = seedBooks();

        System.out.println("============================");
        System.out.println("        LIBRARY APP");
        System.out.println("============================");

        System.out.print("Enter your name (member): ");
        String memberName = scanner.nextLine().trim();
        if (memberName.isEmpty()) {
            memberName = "Guest";
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
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listBooks(books);
                    break;
                case 2:
                    System.out.print("Enter title keyword to search: ");
                    String keyword = scanner.nextLine().trim().toLowerCase();
                    searchBooks(books, keyword);
                    break;
                case 3:
                    listBooks(books);
                    System.out.print("Enter book ID to borrow: ");
                    int borrowId = readInt(scanner);
                    borrowBook(books, borrowId, memberName);
                    break;
                case 4:
                    listBorrowedBy(books, memberName);
                    System.out.print("Enter book ID to return: ");
                    int returnId = readInt(scanner);
                    returnBook(books, returnId, memberName);
                    break;
                case 5:
                    listBorrowedBy(books, memberName);
                    break;
                case 6:
                    System.out.println("Goodbye, " + memberName + "!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        } while (choice != 6);

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("----------- MENU -----------");
        System.out.println("1. List All Books");
        System.out.println("2. Search Books by Title");
        System.out.println("3. Borrow Book");
        System.out.println("4. Return Book");
        System.out.println("5. View My Borrowed Books");
        System.out.println("6. Exit");
    }

    private static List<Book> seedBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "The Alchemist", "Paulo Coelho"));
        books.add(new Book(2, "Clean Code", "Robert C. Martin"));
        books.add(new Book(3, "Effective Java", "Joshua Bloch"));
        books.add(new Book(4, "Introduction to Algorithms", "Cormen"));
        books.add(new Book(5, "Harry Potter and the Sorcerer's Stone", "J.K. Rowling"));
        return books;
    }

    private static void listBooks(List<Book> books) {
        System.out.println("Books in library:");
        for (Book b : books) {
            System.out.println(" - " + b);
        }
    }

    private static void searchBooks(List<Book> books, String keyword) {
        System.out.println("Search results:");
        boolean found = false;
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword)) {
                System.out.println(" - " + b);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found for keyword: " + keyword);
        }
    }

    private static void borrowBook(List<Book> books, int id, String memberName) {
        Book book = findBookById(books, id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Book is already borrowed by " + book.getBorrowedBy());
            return;
        }
        book.borrow(memberName);
        System.out.println("You have borrowed: " + book.getTitle());
    }

    private static void returnBook(List<Book> books, int id, String memberName) {
        Book book = findBookById(books, id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (book.isAvailable()) {
            System.out.println("This book is not currently borrowed.");
            return;
        }
        if (!memberName.equals(book.getBorrowedBy())) {
            System.out.println("You did not borrow this book.");
            return;
        }
        book.giveBack();
        System.out.println("Book returned. Thank you!");
    }

    private static void listBorrowedBy(List<Book> books, String memberName) {
        System.out.println("Books borrowed by " + memberName + ":");
        boolean any = false;
        for (Book b : books) {
            if (!b.isAvailable() && memberName.equals(b.getBorrowedBy())) {
                System.out.println(" - " + b);
                any = true;
            }
        }
        if (!any) {
            System.out.println("No books currently borrowed.");
        }
    }

    private static Book findBookById(List<Book> books, int id) {
        for (Book b : books) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid number. Enter again: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}
