package vendingMachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewVendingMachine implements VendingMachine {
    private Inventory<Coins> cashInventory = new Inventory<Coins>();
    private Inventory<Item> itemInventory = new Inventory<Item>();
    private long totalSales;
    private Item currentItem;
    private long currentBalance;

    public NewVendingMachine() {
        initialize();
    }

    private void initialize() {
        //Initialise machine with 5 coins of each denomination
        //and 5 cans of each Item
        for (Coins c : Coins.values()) {
            cashInventory.put(c, 5);
        }

        for (Item i : Item.values()) {
            itemInventory.put(i, 5);
        }

    }

    @Override
    public long selectItemAndGetPrice(Item item) {
        if (itemInventory.hasItem(item)) {
            currentItem = item;
            return currentItem.getPrice();
        }
        throw new SoldOutException("Sold Out, Please choose another item");
    }

    @Override
    public void insertCoin(Coins coins) {
        currentBalance = currentBalance + coins.getDenomination();
        cashInventory.add(coins);
    }

    @Override
    public Bucket<Item, List<Coins>> collectItemAndChange() {
        Item item = collectItem();
        totalSales = totalSales + currentItem.getPrice();

        List<Coins> change = collectChange();

        return new Bucket<Item, List<Coins>>(item, change);
    }

    private Item collectItem() throws NotSufficientChangeException,
            NotFullPaidException {
        if (isFullPaid()) {
            if (hasSufficientChange()) {
                itemInventory.deduct(currentItem);
                return currentItem;
            }
            throw new NotSufficientChangeException("Not Sufficient change in Inventory");

        }
        long remainingBalance = currentItem.getPrice() - currentBalance;
        throw new NotFullPaidException("Price not full paid, remaining : ", remainingBalance);
    }

    private List<Coins> collectChange() {
        long changeAmount = currentBalance - currentItem.getPrice();
        List<Coins> change = getChange(changeAmount);
        updateCashInventory(change);
        currentBalance = 0;
        currentItem = null;
        return change;
    }

    @Override
    public List<Coins> refund() {
        List<Coins> refund = getChange(currentBalance);
        updateCashInventory(refund);
        currentBalance = 0;
        currentItem = null;
        return refund;
    }


    private boolean isFullPaid() {
        if (currentBalance >= currentItem.getPrice()) {
            return true;
        }
        return false;
    }

    
  private List<Coins> getChange(long amount) throws NotSufficientChangeException {

        List<Coins> changes = Collections.EMPTY_LIST;

        if (amount > 0) {
            changes = new ArrayList<Coins>();
            long balance = amount;
            while (balance > 0) {
                if (balance >= Coins.QUARTER.getDenomination()
                        && cashInventory.hasItem(Coins.QUARTER)) {
                    changes.add(Coins.QUARTER);
                    balance = balance - Coins.QUARTER.getDenomination();
                    continue;

                } else if (balance >= Coins.DIME.getDenomination()
                        && cashInventory.hasItem(Coins.DIME)) {
                    changes.add(Coins.DIME);
                    balance = balance - Coins.DIME.getDenomination();
                    continue;

                } else if (balance >= Coins.NICKLE.getDenomination()
                        && cashInventory.hasItem(Coins.NICKLE)) {
                    changes.add(Coins.NICKLE);
                    balance = balance - Coins.NICKLE.getDenomination();
                    continue;

                } else if (balance >= Coins.PENNY.getDenomination()
                        && cashInventory.hasItem(Coins.PENNY)) {
                    changes.add(Coins.PENNY);
                    balance = balance - Coins.PENNY.getDenomination();
                    continue;

                } else {
                    throw new NotSufficientChangeException("NotSufficientChange, Please try another product ");
                }
            }
        }

        return changes;
    }
    
    
 
    @Override
    public void reset() {
        cashInventory.clear();
        itemInventory.clear();
        totalSales = 0;
        currentItem = null;
        currentBalance = 0;
    }

    public void printStats() {
        System.out.println("Total Sales : " + totalSales);
        System.out.println("Current Item Inventory : " + itemInventory);
        System.out.println("Current Cash Inventory : " + cashInventory);
    }


    private boolean hasSufficientChange() {
        return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
    }

    private boolean hasSufficientChangeForAmount(long amount) {
        boolean hasChange = true;
        try {
            getChange(amount);
        } catch (NotSufficientChangeException nsce) {
            return hasChange = false;
        }

        return hasChange;
    }

    private void updateCashInventory(List<Coins> change) {
        for (Coins c : change) {
            cashInventory.deduct(c);
        }
    }

    public long getTotalSales() {
        return totalSales;
    }

}


