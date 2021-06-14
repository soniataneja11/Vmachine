package vendingMachine;

public class VendingMachineFactory {
    public static VendingMachine createVendingMachine() {
        return new NewVendingMachine();
    }
}

