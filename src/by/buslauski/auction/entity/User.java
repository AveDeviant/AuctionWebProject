package by.buslauski.auction.entity;


import java.util.ArrayList;

/**
 * Created by Acer on 28.02.2017.
 */
public class User {
    private long userId;
    private int roleId;
    private String userName;
    private String email;
    private String city;
    private String address;
    private String phoneNumber;
    private boolean access;
    private String name;
    private Role role;
    private BankCard bankCard;
    private ArrayList<Bet> bets;
    private ArrayList<Bet> winningBets;
    private ArrayList<UserMessage> userMessages;
    private boolean unreadMessages;

    public User(long userId, int roleId, String userName, String email, boolean access,
                Role role) {
        this.roleId = roleId;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.access = access;
        this.role = role;
        this.bets = new ArrayList<>();
        this.winningBets = new ArrayList<>();
        this.userMessages = new ArrayList<>();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long user_id) {
        this.userId = user_id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public Role getRole() {
        return role;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }

    public BankCard getBankCard() {
        return bankCard;
    }

    public void setWinningBets(ArrayList<Bet> winningBets) {
        this.winningBets = winningBets;
    }

    public ArrayList<Bet> getWinningBets() {
        return winningBets;
    }

    public void setBets(ArrayList<Bet> bets) {
        this.bets = bets;
    }

    public ArrayList getBets() {
        return bets;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<UserMessage> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(ArrayList<UserMessage> userMessages) {
        this.userMessages = userMessages;
    }

    public boolean isUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(boolean unreadMessages) {
        this.unreadMessages = unreadMessages;
    }
}
