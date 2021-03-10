package model;

/**
 * is a class that extends abstract Part class to create a part with a company name field.
 * @author Heaven-Leigh (Michelle) Masters
 */
public class Outsourced extends Part {
    
    private String companyName;

    /**
     * constructor for an outsourced part.
     * @param id is the unique ID for the part
     * @param name is the name for the part
     * @param price is the price of the part
     * @param stock is the inventory level of the part
     * @param min is the minimum inventory level allowed for the part
     * @param max is the maximum inventory level allowed for the part
     * @param companyName is the name of the company the part is purchased from
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {

        super(id, name, price, stock, min, max);
        setCompanyName(companyName);
    }

    /**
     * sets the name of the company for the selected part
     * @param companyName is the name of the company that part is bought from
     */
    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    /**
     * gets the company name for the selected part.
     * @return returns the name of the company the part is bought from
     */
    public String getCompanyName() {

        return companyName;
    }
}
