package vendingMachine;

public enum Coins {
    
	PENNY(1), NICKLE(5), DIME(10), QUARTER(25);

    private int denomination;

    private Coins(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }
}

