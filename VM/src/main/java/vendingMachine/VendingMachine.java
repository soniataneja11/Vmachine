package vendingMachine;

import java.util.List;


public interface VendingMachine {
	
	public Bucket<Item, List<Coins>> collectItemAndChange();

    public long selectItemAndGetPrice(Item item);

    public List<Coins> refund();

    public void insertCoin(Coins coin);

    public void reset();

}
