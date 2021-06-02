package shell;

import java.util.Scanner;
import java.util.List;

import backend.*;

public class TraderShell {
    private static Scanner input;
    private static Controller controller;
    public static void main(String[] args) {
        controller = new Controller();
        input = new Scanner(System.in);

        System.out.print("> ");
        String cmd = input.nextLine();

        while (!cmd.equals("q") && !cmd.equals("quit")) {
            switch(cmd) {
                case "help":
                printHelp();
                break;
                case "signup":
                promptSignup();
                break;
                case "login":
                promptLogin();
                break;
                case "logout":
                logout();
                break;
                case "reset":
                controller.resetDatastore();
                break;
                case "deposit":
                promptDeposit();
                break;
                case "withdraw":
                promptWithdraw();
                break;
                case "buy":
                promptBuy();
                break;
                case "sell":
                promptSell();
                break;
                case "balance":
                displayBalance();
                break;
                case "market_transactions":
                displayMarketTransactions();
                break;
                case "stock_transactions":
                displayStockTransactions();
                break;
                case "stock_info":
                displayStockInfo();
                break;
                case "movie_info":
                promptMovieInfo();
                break;
                case "top_movies":
                promptTopMovies();
                break;
                case "add_interest":
                addInterest();
                break;

            }
            System.out.print("> ");
            cmd = input.nextLine();
        }

        input.close();
    }

    public static void addInterest() {
        List<MarketAccount> accounts = controller.getAllMarketAccounts();
        
        for(int i = 0; i < accounts.size(); i++) {
            MarketAccount account = accounts.get(i);
            final int prevBalance = controller.getBalance(account.getTaxID());
            controller.addInterest(account.getTaxID());
            final int newBalance = controller.getBalance(account.getTaxID());

            System.out.println(String.format("%s - INTEREST for account %d: $%d.%02d + (%.2f * $%d.%02d) = $%d.%02d", 
            controller.getDate(),
            account.getTaxID(), 
            prevBalance / 100, 
            prevBalance % 100,
            controller.getInterestRate(), 
            prevBalance / 100, 
            prevBalance % 100,
            newBalance / 100, 
            newBalance % 100));
        }
    }
    
    public static void promptTopMovies() {
        System.out.print("Enter the lowest rating: ");
        final String sRating = input.nextLine();
        int rating = 0;
        for (char c : sRating.toCharArray()) {
            if (c == '.') continue;
            rating = rating * 10 + (c - '0'); 
        }
        System.out.print("Enter the earliest year in your range: "); 
        final int start = input.nextInt();
        System.out.print("Enter the last year in your range: "); 
        final int end = input.nextInt();
        List<String> names = controller.getTopMovieNames(rating, start, end);
        if (names.isEmpty()) {
            System.out.println("There are no movies that fit the criteria");
        } else {
            for (String n : names) {
                System.out.println(n);
            } 
        }
    }
    
    public static void promptMovieInfo() {
        System.out.print("Enter the movie name to display info for: ");
        final String movieName = input.nextLine();
        MovieData d = controller.getMovieData(movieName);
        if (d == null) {
            System.out.println("No movie exists with that name"); 
        } else {
            System.out.println(d); 
        }
    }
    
    public static void displayStockInfo() {
        List<StockProfile> profiles = controller.getAvailableStockProfiles();
        for (StockProfile p : profiles) {
            System.out.println(p); 
        }
    }

    public static void displayMarketTransactions() {
        if (!controller.isLoggedIn()) {
            System.out.println("Must be logged in to display transaction history"); 
            return;
        }
        
        List<DepositTransaction> deposits = controller.getDepositTransactions();
        List<WithdrawTransaction> withdraws = controller.getWithdrawTransactions();

        if (deposits.size() == 0 && withdraws.size() == 0) {
            System.out.println("No market transaction history"); 
        }
        
        int i = 0, j = 0;
        while (i < deposits.size() && j < withdraws.size()) {
            final long bTimestamp = deposits.get(i).getTimestamp();
            final long sTimestamp = withdraws.get(j).getTimestamp();
            if (bTimestamp >= sTimestamp) {
                System.out.println(deposits.get(i));
                i++;
            } else {
                System.out.println(withdraws.get(j)); 
                j++;
            }
        }
    
        for (; i < deposits.size(); i++) System.out.println(deposits.get(i));
        for (; j < withdraws.size(); j++) System.out.println(withdraws.get(j));
    }
    
    public static void displayStockTransactions() {
        if (!controller.isLoggedIn()) {
            System.out.println("Must be logged in to display transaction history"); 
            return;
        }
        
        // Probably cleaner if we created a function in controller to aggregate these into
        // one list and use polymorphism; but it's been a while since I usede 
        // that stuff so we'll go for this manual merging for now. Can look into this later
        List<BuyTransaction> buys = controller.getStockBuyTransactions();
        List<SellTransaction> sells = controller.getStockSellTransactions();
        
        int i = 0, j = 0;
        while (i < buys.size() && j < sells.size()) {
            final long bTimestamp = buys.get(i).getTimestamp();
            final long sTimestamp = sells.get(j).getTimestamp();
            if (bTimestamp >= sTimestamp) {
                System.out.println(buys.get(i));
                i++;
            } else {
                System.out.println(sells.get(j)); 
                j++;
            }
        }
    
        for (; i < buys.size(); i++) System.out.println(buys.get(i));
        for (; j < sells.size(); j++) System.out.println(sells.get(j));
    }
    
    public static void displayBalance() {
        final int balance = controller.getBalance(); 
        if (balance != -1) {
            int bInt = balance / 100, bDec = balance % 100;
            System.out.println(String.format("Current balance: $%d.%02d", bInt, bDec)); 
        }
    }

    public static void promptDeposit() {
        System.out.print("Enter the amount you want to deposit (ex. 4.24, 424, 4.20): $");
        final String valueString = input.nextLine();
        int decimalIndex = valueString.indexOf(".");
        int value;
        if (decimalIndex == -1) { //whole dollars
            try {
                value = Integer.parseInt(valueString) * 100;
            } catch (NumberFormatException e) {
                System.out.println("invalid amount format");
                return;
            }
        } else {
            try {
                int dollars = Integer.parseInt(valueString.substring(0, decimalIndex));
                int cents = Integer.parseInt(valueString.substring(decimalIndex + 1));
                if (decimalIndex == valueString.length() - 2) {
                    cents *= 10;
                }
                value = dollars * 100 + cents;
            } catch (NumberFormatException e) {
                System.out.println("invalid amount format");
                return;
            }
        }
        if (!controller.deposit(value)) {
            System.out.println(String.format("Failed to deposit $%d.%02d!", value / 100, value % 100));
        } else {
            System.out.println(String.format("Successfully deposited $%d.%02d!", value / 100, value % 100));
            displayBalance();
        }

        System.out.println();
    }

    public static void promptWithdraw() {
        System.out.print("Enter the amount you want to withdraw (ex. 4.24, 424, 4.20): $");
        final String valueString = input.nextLine();
        int decimalIndex = valueString.indexOf(".");
        int value;
        if (decimalIndex == -1) { //whole dollars
            try {
                value = Integer.parseInt(valueString) * 100;
            } catch (NumberFormatException e) {
                System.out.println("invalid amount format");
                return;
            }
        } else {
            try {
                int dollars = Integer.parseInt(valueString.substring(0, decimalIndex));
                int cents = Integer.parseInt(valueString.substring(decimalIndex + 1));
                if (decimalIndex == valueString.length() - 2) {
                    cents *= 10;
                }
                value = dollars * 100 + cents;
            } catch (NumberFormatException e) {
                System.out.println("invalid amount format");
                return;
            }
        }
        if (!controller.withdraw(value)) {
            System.out.println(String.format("Failed to withdraw $%d.%02d!", value / 100, value % 100));
        } else {
            System.out.println(String.format("Successfully withdrew $%d.%02d", value / 100, value % 100));
            displayBalance();
        }

        System.out.println();
    }
    
    public static void promptSell() {
        if (!controller.isMarketOpen()) {
            System.out.println("Market is closed, no selling allowed"); 
            return;
        }
    
        List<StockAccountData> saData = controller.getStockAccountData();
        if (saData.isEmpty()) {
            System.out.println("No stocks to sell"); 
            return;
        }
    
        System.out.println("Stocks you own:");
        for (StockAccountData d : saData) {
            System.out.println(d); 
        }
        
        System.out.print("Enter the stock symbol you wish to sell: ");
        final String symbol = input.nextLine();
        System.out.print("Enter the price of the stock you wish to sell: ");
        final String sPrice = input.nextLine();
       
        int price = 0;
        for (char c : sPrice.toCharArray()) {
            if (c == '.') continue;
            price = price * 10 + (c - '0'); // dirty ASCII trick, maybe cleanup later
        }
        
        // Again, linear check here... replace if this becomes a problem
        // Can use set, also since we group by stock symbol we can optimize using that
        StockAccountData tbs = null;
        for (StockAccountData d : saData) {
            if (d.getSymbol().equals(symbol) && d.getPrice() == price) {
                tbs = d;
                break;
            }
        }
        
        if (tbs == null) {
            System.out.println("You don't own " + symbol + " at " + price);
            return;
        }

        System.out.print("Enter the number of shares you wish to sell: ");
        final int shares = input.nextInt();
        
        boolean res = controller.sell(tbs, shares);
        if (res) {
            System.out.println("Sold stocks successfully"); 
        } else {
            System.out.println("Error occurred when trying to sell");
        }
    }

    public static void promptBuy() {
        if (!controller.isLoggedIn()) {
            System.out.println("You must be logged in to make a purchase");
            return;
        } 
        if (!controller.isMarketOpen()) {
            System.out.println("Market is closed, no purchases allowed"); 
            return;
        }

        System.out.println("Currently available stocks:");
        List<Stock> availableStocks = controller.getAvailableStocks(); 
        if (availableStocks.isEmpty()) {
            System.out.println("No stocks are available"); 
            return;
        }

        for (Stock s : availableStocks) {
            System.out.println(s); 
        }
        
        System.out.print("Enter the stock symbol to purchase: ");
        String symbol = input.nextLine();
       
        // For demo purposes assuming that there are not that many
        // stocks, we can linearly search; in the future we can modify
        // availableStocks to return a HashSet for a faster check
        boolean found = false;
        Stock tbp = null;
        for (Stock s : availableStocks) {
            if (s.getSymbol().equals(symbol)) {
                found = true;
                tbp = s;
                break; 
            }
        }
        
        if (tbp == null) {
            System.out.println("Invalid stock symbol"); 
            return;
        }
        
        System.out.print("Enter the number of shares to purchase: ");
        int numShares = input.nextInt();
        
        boolean res = controller.purchase(tbp, numShares);
        if (res) {
            System.out.println("Purchase successful"); 
        } else {
            System.out.println("Error occurred while attempting to purchase"); 
        }
        
    }

    public static void promptSignup() {
        Customer customer = new Customer();
        System.out.print("Name: ");
        customer.name = input.nextLine();
        System.out.print("Username: ");
        customer.username = input.nextLine();
        System.out.print("Password: ");
        customer.password = input.nextLine();
        System.out.print("Address: ");
        customer.address = input.nextLine();
        System.out.print("State: ");
        customer.state = input.nextLine();
        System.out.print("Phone: ");
        customer.phone = input.nextLine();
        System.out.print("Email: ");
        customer.email = input.nextLine();
        System.out.print("Tax Id: ");
        customer.taxid = Integer.parseInt(input.nextLine());
        System.out.print("SSN: ");
        customer.ssn = input.nextLine();
        boolean res = controller.createTrader(customer);
        
        if (!res) {
            System.out.println("Failed to create new user: " + String.join(",", new String[]{customer.name, customer.username, customer.password, customer.address, customer.state, customer.email, String.valueOf(customer.taxid), customer.ssn}));
        } else {
            System.out.println("Created new user with username " + customer.username + "!");
        }
    }

    public static void promptLogin() {
        if (controller.isLoggedIn()) {
            System.out.println("You are already logged into an account! Please logout before logging into another account"); 
            return;
        }
        System.out.print("Username: "); 
        String user = input.nextLine();
        System.out.print("Password: ");
        String pass = input.nextLine();
        controller.authenticateTrader(user, pass);
    }

    public static void logout() {
        boolean res = controller.logout();
        if (!res) {
            System.out.println("You are not logged in to any account.");
        } else {
            System.out.println("You have been logged out.");
        }
    }

    public static void printHelp() {
        System.out.println("--- Available Commands ---");
        if (!controller.isLoggedIn()) {
            System.out.println("login");
            System.out.println("signup");
        } else {
            System.out.println("logout");
            System.out.println("balance");
            System.out.println("deposit");
            System.out.println("withdraw");
            System.out.println("buy");
            System.out.println("sell");
            System.out.println("stock_transactions");
        }
        System.out.println("reset");
        System.out.println("help");
        System.out.println("q, quit");
        System.out.println("--------------------------\n");
    }
}
