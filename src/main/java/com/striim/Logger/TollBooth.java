package com.striim.Logger;

// A java program that implement the following:
// Imagine a tollbooth at highways. The buses passing by the booth is expected to pay a toll. The tollbooth keeps the track of the number of buses that is gone by and the total amount of cash collected.
// # Create a class tollbooth with the data members as :-
// -total number of buses passed.
// -total toll collected.
// Write necessary member functions:
// 1. A constructors that initializes both data members to zero.
// 2. Paying bus(): when any bus passes through the tollbooth, that much toll gets added into total toll collected
// and total number of buses passed is incremented by one.
// 3. Nonpaying bus(): increments the bus total but adds nothing to cash total.
// 4. Display(): displays total no. of buses passed,no. of paid buses, no. of unpaid buses and the total amount collected.

import java.util.Scanner;

public class TollBooth {

    private int noBuses;
    private int noNonPaidBuses;
    private double amount;

    //constructor
    public TollBooth() {
        noBuses = 0;
        amount = 0;
        noNonPaidBuses = 0;
    }

    // This function increases the bus count and adds the amount to the total amount
    public void addPayToll(double toll) {
        noBuses++;
        amount += toll;
    }

    // This function increases the bus count alone
    public void addNonPayToll() {
        noBuses++;
        noNonPaidBuses++;
    }

    // This function will display the current status of the toll booth collection
    public void displayAll() {
        System.out.println("Total no. of Buses: " + noBuses);
        System.out.println("Total no. of Paying Buses: " + noNonPaidBuses);
        System.out.println("Total no. of Non-Paying Buses: " + (noBuses - noNonPaidBuses));
        System.out.println("Total amount: " + amount);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        double amt;
        TollBooth tb = new TollBooth();
        while (true) {
            System.out.println("Enter the choice: 1.Paying Bus\t 2.Non-Paying Bus\t 3. Display\t 4.Exit");
            choice = sc.nextInt();
            switch (choice) {
                case 1 : {
                    System.out.print("Enter amount: ");
                    amt = sc.nextDouble();
                    tb.addPayToll(amt);
                }
                case 2 : tb.addNonPayToll();
                case 3 : tb.displayAll();
                case 4 : System.exit(0);
                default : System.out.println("Invalid choice ");
            }
        }
    }
}

