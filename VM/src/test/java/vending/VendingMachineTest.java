package vending;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import vendingMachine.Bucket;
import vendingMachine.Coins;
import vendingMachine.Item;
import vendingMachine.NotSufficientChangeException;
import vendingMachine.SoldOutException;
import vendingMachine.VendingMachine;
import vendingMachine.VendingMachineFactory;
import vendingMachine.NewVendingMachine;


public class VendingMachineTest {

    private static VendingMachine VMachine;

    @Before
    public static void setUp() {
    	VMachine = VendingMachineFactory.createVendingMachine();
    }

    @After
    public static void tearDown() {
    	VMachine = null;
    }

    @Test
    public void testBuyItemWithExactPrice() {
        //select item, price in cents
        long price = VMachine.selectItemAndGetPrice(Item.COKE);
        //price should be Coke's price
        assertEquals(Item.COKE.getPrice(), price);
        //25 cents paid
        VMachine.insertCoin(Coins.QUARTER);

        Bucket<Item, List<Coins>> bucket = VMachine.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coins> change = bucket.getSecond();

        //should be Coke
        assertEquals(Item.COKE, item);
        //there should not be any change
        assertTrue(change.isEmpty());
    }

    @Ignore
    public void testBuyItemWithMorePrice() {
        long price = VMachine.selectItemAndGetPrice(Item.SODA);
        assertEquals(Item.SODA.getPrice(), price);

        VMachine.insertCoin(Coins.QUARTER);
        VMachine.insertCoin(Coins.QUARTER);

        Bucket<Item, List<Coins>> bucket = VMachine.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coins> change = bucket.getSecond();

        //should be Coke
        assertEquals(Item.SODA, item);
        //there should not be any change
        assertTrue(!change.isEmpty());
        //comparing change
        assertEquals(50 - Item.SODA.getPrice(), getTotal(change));

    }


    @Test
    public void testRefund() {
        long price = VMachine.selectItemAndGetPrice(Item.PEPSI);
        assertEquals(Item.PEPSI.getPrice(), price);
        VMachine.insertCoin(Coins.DIME);
        VMachine.insertCoin(Coins.NICKLE);
        VMachine.insertCoin(Coins.PENNY);
        VMachine.insertCoin(Coins.QUARTER);

        assertEquals(41, getTotal(VMachine.refund()));
    }

    @Test(expected = SoldOutException.class)
    public void testSoldOut() {
        for (int i = 0; i < 5; i++) {
        	VMachine.selectItemAndGetPrice(Item.COKE);
        	VMachine.insertCoin(Coins.QUARTER);
        	VMachine.collectItemAndChange();
        }

    }

    @Test(expected = NotSufficientChangeException.class)
    public void testNotSufficientChangeException() {
        for (int i = 0; i < 5; i++) {
        	VMachine.selectItemAndGetPrice(Item.SODA);
        	VMachine.insertCoin(Coins.QUARTER);
        	VMachine.insertCoin(Coins.QUARTER);
        	VMachine.collectItemAndChange();

        	VMachine.selectItemAndGetPrice(Item.PEPSI);
        	VMachine.insertCoin(Coins.QUARTER);
        	VMachine.insertCoin(Coins.QUARTER);
        	VMachine.collectItemAndChange();
        }
    }


    @Test(expected = SoldOutException.class)
    public void testReset() {
        VendingMachine vmachine = VendingMachineFactory.createVendingMachine();
        vmachine.reset();

        vmachine.selectItemAndGetPrice(Item.COKE);

    }

    @Test
    public void testVendingMachineImpl() {
        NewVendingMachine vm = new NewVendingMachine();
    }

    private long getTotal(List<Coins> change) {
        long total = 0;
        for (Coins c : change) {
            total = total + c.getDenomination();
        }
        return total;
    }
}


