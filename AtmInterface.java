import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class UserAccount {
    private double balance;
    private int pin;

    public UserAccount(double initialBalance, int pin) {
        this.balance = initialBalance;
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds. Withdrawal failed.");
            return false;
        }
        balance -= amount;
        return true;
    }

    public void changePin(int newPin) {
        pin = newPin;
        System.out.println("PIN successfully changed.");
    }

    public int getPin() {
        return pin;
    }
}

class Bank {
    private Map<Integer, UserAccount> accounts;
    private Scanner scanner;

    public Bank() {
        this.accounts = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    public void createAccount(int accountNumber, double initialBalance, int pin) {
        if (!accounts.containsKey(accountNumber)) {
            UserAccount userAccount = new UserAccount(initialBalance, pin);
            accounts.put(accountNumber, userAccount);
            System.out.println("Account created successfully!");
        } else {
            System.out.println("Account number already exists. Please choose a different account number.");
        }
    }

    public UserAccount getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    public void transferFunds(int sourceAccountNumber, int targetAccountNumber, double amount) {
        UserAccount sourceAccount = getAccount(sourceAccountNumber);
        UserAccount targetAccount = getAccount(targetAccountNumber);

        if (sourceAccount != null && targetAccount != null) {
            if (sourceAccount.withdraw(amount)) {
                targetAccount.deposit(amount);
                System.out.println("Transfer successful. Updated Balance (Source): $" + sourceAccount.getBalance());
                System.out.println("Updated Balance (Target): $" + targetAccount.getBalance());
            }
        } else {
            System.out.println("Invalid account number(s). Transfer failed.");
        }
    }

    public void changePin(int accountNumber, int newPin) {
        UserAccount userAccount = getAccount(accountNumber);
        if (userAccount != null) {
            userAccount.changePin(newPin);
        } else {
            System.out.println("Invalid account number. PIN change failed.");
        }
    }
}

class ATM {
    private Bank bank;
    private Scanner scanner;

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("\nATM Options:");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer Funds");
        System.out.println("5. Change PIN");
        System.out.println("6. Exit");
    }

    public void processUserInput(int accountNumber) {
        int choice;
        do {
            displayOptions();
            System.out.print("Enter your choice (1-6): ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    checkBalance(accountNumber);
                    break;
                case 2:
                    deposit(accountNumber);
                    break;
                case 3:
                    withdraw(accountNumber);
                    break;
                case 4:
                    transferFunds(accountNumber);
                    break;
                case 5:
                    changePin(accountNumber);
                    break;
                case 6:
                    System.out.println("Exiting ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    private void checkBalance(int accountNumber) {
        UserAccount userAccount = bank.getAccount(accountNumber);
        if (userAccount != null) {
            double balance = userAccount.getBalance();
            System.out.println("Current Balance: $" + balance);
        } else {
            System.out.println("Invalid account number. Balance check failed.");
        }
    }

    private void deposit(int accountNumber) {
        UserAccount userAccount = bank.getAccount(accountNumber);
        if (userAccount != null) {
            System.out.print("Enter the deposit amount: $");
            double amount = scanner.nextDouble();
            userAccount.deposit(amount);
            System.out.println("Deposit successful. Updated Balance: $" + userAccount.getBalance());
        } else {
            System.out.println("Invalid account number. Deposit failed.");
        }
    }

    private void withdraw(int accountNumber) {
        UserAccount userAccount = bank.getAccount(accountNumber);
        if (userAccount != null) {
            System.out.print("Enter the withdrawal amount: $");
            double amount = scanner.nextDouble();
            if (userAccount.withdraw(amount)) {
                System.out.println("Withdrawal successful. Updated Balance: $" + userAccount.getBalance());
            }
        } else {
            System.out.println("Invalid account number. Withdrawal failed.");
        }
    }

    private void transferFunds(int sourceAccountNumber) {
        System.out.print("Enter the target account number: ");
        int targetAccountNumber = scanner.nextInt();

        System.out.print("Enter the transfer amount: $");
        double amount = scanner.nextDouble();

        bank.transferFunds(sourceAccountNumber, targetAccountNumber, amount);
    }

    private void changePin(int accountNumber) {
        System.out.print("Enter the new PIN: ");
        int newPin = scanner.nextInt();

        bank.changePin(accountNumber, newPin);
    }
}

public class AtmInterface {
    public static void main(String[] args) {
        Bank bank = new Bank();

        // Create accounts for testing
        bank.createAccount(123456, 1000, 1234);
        bank.createAccount(789012, 500, 5678);

        // Create ATM instance and connect it with the bank
        ATM atm = new ATM(bank);

        // Simulate user interaction
        System.out.print("Enter your account number: ");
        int accountNumber = new Scanner(System.in).nextInt();

        // Validate account number
        if (bank.getAccount(accountNumber) != null) {
            System.out.print("Enter your PIN: ");
            int pin = new Scanner(System.in).nextInt();

            // Validate PIN
            if (pin == bank.getAccount(accountNumber).getPin()) {
                // Process user input in the ATM
                atm.processUserInput(accountNumber);
            } else {
                System.out.println("Incorrect PIN. Exiting program.");
            }
        } else {
            System.out.println("Invalid account number. Exiting program.");
        }
    }
}
