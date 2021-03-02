package model;

public class InHouse extends Part {

    private int machineID;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);

    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
        //this.machineID = Integer.parseInt(CompanyNameOrMachineID);
    }

    public int getMachineID() {
        return machineID;
    }
}
