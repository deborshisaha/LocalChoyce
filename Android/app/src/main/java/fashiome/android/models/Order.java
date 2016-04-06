package fashiome.android.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by dsaha on 4/5/16.
 */
@ParseClassName("Order")
public class Order  extends ParseObject {

    private Date dueDate;

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
        put("seller", seller);
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
        put("buyer", buyer);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        put("product", product);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        put("amount", amount);
    }

    public double getDaysRented() {
        return daysRented;
    }

    public void setDaysRented(double daysRented) {
        this.daysRented = daysRented;
        put("daysRented", daysRented);
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
        put("quantity", quantity);
    }

    private User seller;
    private User buyer;
    private Product product;
    private double amount;
    private double daysRented;
    private double quantity;

    public Date getDueDate() {
        return addDays(getCreatedAt(), (int) daysRented);
    }

    private Date addDays(Date createdAt, int numberOfDays) {
        Date today = new Date();
        Date returnDate = new Date(today.getTime() + (1000 * 60 * 60 * 24 * numberOfDays));
        return returnDate;
    }
}
