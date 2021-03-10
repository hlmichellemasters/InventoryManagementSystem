package model;

/** is a class that extends Part class to create a part with a Machine ID instead of a company name.
 * @author Heaven-Leigh (Michelle) Masters
 */
public class InHouse extends Part {

    private int machineID;

    /**
     * is the constructor for an in-house part.
     * Includes setting the machine ID for the part.
     * @param id is the unique ID of the part
     * @param name is the name of the part
     * @param price is the price of the part
     * @param stock is the inventory level of the part
     * @param min is the minimum inventory of the part
     * @param max is the maximum inventory of the part
     * @param machineID is the machine ID for the part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {

        super(id, name, price, stock, min, max);
        setMachineID(machineID);
    }

    /**
     * sets the machine ID of the part.
     * @param machineID is passed in to set the machine ID
     */
    public void setMachineID(int machineID) {

        this.machineID = machineID;
    }

    /**
     * gets the machine ID of the part.
     * @return the machine ID
     */
    public int getMachineID() {

        return machineID;
    }
}
