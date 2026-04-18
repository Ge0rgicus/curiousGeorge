package Assignment1;
import java.util.*;

enum LollySize {
    LARGE, MEDIUM, SMALL
}
interface Payable {
    boolean processPayment();
}


class Lollies {
    private String name;
    private LollySize size;
    private String colour;
    private double price;

    public Lollies(String name, LollySize size, String colour, double price) {
        this.name = name;
        this.size = size;
        this.colour = colour;
        this.price = price;
    }
    public Lollies(String name, double price) {
        this(name, LollySize.MEDIUM, "Unknown", price);
    }

    public String getName() { 
        return name; 
    }

    public LollySize getSize() { 
        return size; 
    }

    public String getColour() { 
        return colour; 
    }

    public double getPrice() { 
        return price;
    }

    public String toString() {
        return name + " | " + size + " | " + colour + " | $" + price;
    }
}

class LollyDetails extends Lollies {
    private boolean sugarFree;
    private boolean glutenFree;

    public LollyDetails(String name, LollySize size, String colour, double price, boolean sugarFree, boolean glutenFree) {
        super(name, size, colour, price);
        this.sugarFree = sugarFree;
        this.glutenFree = glutenFree;
    }

    public String toString() {
        return super.toString() + " | SugarFree=" + sugarFree + " | GlutenFree=" + glutenFree;
    }
}

class LollyInventory {
    private ArrayList<Lollies> stock = new ArrayList<Lollies>();
    private int lowStockThreshold = 2;

    public void addLolly(Lollies l) {
        stock.add(l);
        System.out.println("Added: " + l.getName());
    }
    public void addLolly(String name, double price) {
        addLolly(new Lollies(name, price));
    }
    public void removeLolly(String name) {
        for (int i = 0; i < stock.size(); i++) {
            if (stock.get(i).getName().equalsIgnoreCase(name)) {
                stock.remove(i);
                System.out.println("Removed " + name);
                return;
            }
        }
        System.out.println("Not found.");
    }

    public Lollies getLolly(String name) {
        for (Lollies l : stock) {
            if (l.getName().equalsIgnoreCase(name)) return l;
        }
        return null;
    }

    public void showInventory() {
        if (stock.size() == 0) {
            System.out.println("Inventory empty.");
            return;
        }
        for (Lollies l : stock) {
            System.out.println(l);
        }
            
    }

    public void sortByName() {
        Collections.sort(stock, new Comparator<Lollies>() {
            public int compare(Lollies a, Lollies b) {
                return a.getName().compareToIgnoreCase(b.getName());
            }
        });
        System.out.println("Sorted by name.");
    }

    public void sortBySize() {
        Collections.sort(stock, new Comparator<Lollies>() {
            public int compare(Lollies a, Lollies b) {
                return a.getSize().compareTo(b.getSize());
            }
        });
        System.out.println("Sorted by size.");
    }

    public void filterByColour(String colour) {
        for (Lollies l : stock) {
            if (l.getColour().equalsIgnoreCase(colour)) {
                System.out.println(l);
            }
        }
    }

    public void checkLowStock() {
        HashMap<String, Integer> count = new HashMap<String, Integer>();

        for (Lollies l : stock) {
            String name = l.getName();
            if (!count.containsKey(name)) count.put(name, 0);
            count.put(name, count.get(name) + 1);
        }

        for (String name : count.keySet()) {
            if (count.get(name) <= lowStockThreshold) {
                System.out.println("WARNING: Low stock on " + name + " (" + count.get(name) + ")");
            }
        }
    }
}

class LollySales {
    protected ArrayList<Lollies> sold = new ArrayList<Lollies>();
    protected double discountRate = 0.10;

    public double applyDiscount(double price) {
        return price - (price * discountRate);
    }

    public void recordSale(Lollies l) {
        sold.add(l);
        System.out.println("Sold: " + l.getName());
    }

    public void showSales() {
        for (Lollies l : sold) System.out.println(l);
    }
}


class LolliesRecommended extends LollySales {

    public Lollies recommendBySize(LollySize size) {
        for (Lollies l : sold) {
            if (l.getSize() == size) return l;
        }
        return null;
    }

    public double applyDiscount(double price) {
        return price - (price * 0.20); 
    }
}

class Customer {
    private String name;
    private String email;

    public Customer(String n, String e) {
        name = n;
        email = e;
    }

    public String toString() {
        return name + " | " + email;
    }
}

class Payment implements Payable{
    protected double amount;

    public Payment(double amount) {
        this.amount = amount;
    }

    public boolean processPayment() {
        return true;
    }
}

class CardPayment extends Payment {
    private String cardNumber;

    public CardPayment(double amount, String cardNumber) {
        super(amount);
        this.cardNumber = cardNumber;
    }

    public boolean processPayment() {
        System.out.println("Processing card payment...");
        return true;
    }
}

class CashPayment extends Payment {
    private double cashGiven;

    public CashPayment(double amount, double cashGiven) {
        super(amount);
        this.cashGiven = cashGiven;
    }

    public boolean processPayment() {
        if (cashGiven >= amount) {
            System.out.println("Cash accepted. Change: $" + (cashGiven - amount));
            return true;
        }
        System.out.println("Not enough cash.");
        return false;
    }
}

public class LolliesApp {

    public static void main(String[] args) {

        LollyInventory inventory = new LollyInventory();
        LollySales sales = new LollySales();
        LolliesRecommended rec = new LolliesRecommended();

        inventory.addLolly(new Lollies("Rainbow Pop", LollySize.LARGE, "Rainbow", 4.50));
        inventory.addLolly(new Lollies("Rainbow Pop", LollySize.LARGE, "Rainbow", 4.50));
        inventory.addLolly(new Lollies("Choco Swirl", LollySize.MEDIUM, "Brown", 3.20));
        inventory.addLolly(new Lollies("Mint Twist", LollySize.SMALL, "Green", 2.00));
        inventory.addLolly(new Lollies("Berry Blast", LollySize.MEDIUM, "Red", 3.00));

        sales.recordSale(new Lollies("Berry Blast", LollySize.MEDIUM, "Red", 3.00));
        sales.recordSale(new Lollies("Mint Twist", LollySize.SMALL, "Green", 2.00));
        rec.recordSale(new Lollies("Mint Twist", LollySize.SMALL, "Green", 2.00));

        boolean running = true;

        while (running) {
            System.out.println("\n===== LOLLY SHOP MENU =====");
            System.out.println("1. Add Lolly");
            System.out.println("2. Remove Lolly");
            System.out.println("3. Show Inventory");
            System.out.println("4. Sort by Name");
            System.out.println("5. Sort by Size");
            System.out.println("6. Filter by Colour");
            System.out.println("7. Check Low Stock");
            System.out.println("8. Make Sale");
            System.out.println("9. Recommend Lolly");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = In.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Name: ");
                    String name = In.nextLine();
                    System.out.print("Colour: ");
                    String colour = In.nextLine();
                    System.out.print("Price: ");
                    double price = In.nextDouble();
                    System.out.print("Size (L/M/S): ");
                    char s = In.nextUpperChar();

                    LollySize size = (s == 'L') ? LollySize.LARGE :
                                     (s == 'M') ? LollySize.MEDIUM :
                                                  LollySize.SMALL;

                    inventory.addLolly(new Lollies(name, size, colour, price));
                    break;

                case 2:
                    System.out.print("Enter name to remove: ");
                    inventory.removeLolly(In.nextLine());
                    break;

                case 3:
                    inventory.showInventory();
                    break;

                case 4:
                    inventory.sortByName();
                    break;

                case 5:
                    inventory.sortBySize();
                    break;

                case 6:
                    System.out.print("Colour: ");
                    inventory.filterByColour(In.nextLine());
                    break;

                case 7:
                    inventory.checkLowStock();
                    break;

                case 8:
                    System.out.print("Enter lolly name to sell: ");
                    Lollies l = inventory.getLolly(In.nextLine());
                    if (l == null) {
                        System.out.println("Not found.");
                        break;
                    }

                    double finalPrice = sales.applyDiscount(l.getPrice());
                    System.out.println("Final price after discount: $" + finalPrice);

                    System.out.print("Payment type (C=Card, H=Cash): ");
                    char pay = In.nextUpperChar();

                    boolean ok = false;

                    if (pay == 'C') {
                        System.out.print("Card number: ");
                        String card = In.nextLine();
                        ok = new CardPayment(finalPrice, card).processPayment();
                    } else {
                        System.out.print("Cash given: ");
                        double cash = In.nextDouble();
                        ok = new CashPayment(finalPrice, cash).processPayment();
                    }

                    if (ok) {
                        sales.recordSale(l);
                        inventory.removeLolly(l.getName());
                    }
                    break;

                case 9:
                    System.out.print("Recommend size (L/M/S): ");
                    char rs = In.nextUpperChar();
                    LollySize rsize;
                    if (rs == 'L'){
                        rsize = LollySize.LARGE;
                    }else if (rs == 'M'){
                        rsize = LollySize.MEDIUM;
                    }else if (rs == 'S'){
                        rsize = LollySize.SMALL;
                    }else{
                        System.err.println("That Size does not exist.");
                        break;
                    }

                    Lollies recommended = rec.recommendBySize(rsize);
                    if (recommended == null) {
                        System.out.println("No recommendations yet.");
                    } else {
                        System.out.println("Recommended: " + recommended);
                    }
                    break;

                case 0:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid.");
            }
        }

        System.out.println("Goodbye!");
    }
}
