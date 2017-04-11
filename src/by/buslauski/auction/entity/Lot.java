package by.buslauski.auction.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Acer on 14.03.2017.
 */
public class Lot {
    private String title;
    private long id;
    private long userId;
    private BigDecimal price;
    private String image;
    private String description;
    private boolean availability;
    private int categoryId;
    private String category;
    private LocalDate dateAvailable;
    private BigDecimal currentPrice;
    private ArrayList<Bet> bets;

    public Lot(long id, long userId, String title, String description, String image, int categoryId, BigDecimal price,
               boolean availability, LocalDate dateAvailable, BigDecimal currentPrice, String category) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.categoryId = categoryId;
        this.price = price;
        this.availability = availability;
        this.dateAvailable = dateAvailable;
        this.currentPrice = currentPrice;
        this.bets = new ArrayList<>();
        this.category=category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDate getDateAvailable() {
        return dateAvailable;
    }

    public void setDateAvailable(LocalDate dateAvailable) {
        this.dateAvailable = dateAvailable;
    }

    public ArrayList<Bet> getBets() {
        return bets;
    }

    public void setBets(ArrayList<Bet> bets) {
        this.bets = bets;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory(){
        return category;
    }
}
