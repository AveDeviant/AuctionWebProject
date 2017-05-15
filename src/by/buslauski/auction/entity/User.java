package by.buslauski.auction.entity;


import java.util.ArrayList;

/**
 * @author Mikita Buslauski
 */
public class User {
    private long userId;
    private int roleId;
    private String userName;
    private String alias;
    private String email;
    private String city;
    private String address;
    private String phoneNumber;
    private boolean access;
    private String name;
    private Role role;
    private ArrayList<Bet> bets;
    private ArrayList<Bet> winningBets;
    private ArrayList<UserMessage> userMessages;
    private boolean unreadMessages;
    private double userRating;

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

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (roleId != user.roleId) return false;
        if (access != user.access) return false;
        if (unreadMessages != user.unreadMessages) return false;
        if (!userName.equals(user.userName)) return false;
        if (!email.equals(user.email)) return false;
        if (!city.equals(user.city)) return false;
        if (!address.equals(user.address)) return false;
        if (!phoneNumber.equals(user.phoneNumber)) return false;
        if (!name.equals(user.name)) return false;
        if (role != user.role) return false;
        if (!bets.equals(user.bets)) return false;
        if (!winningBets.equals(user.winningBets)) return false;
        return userMessages.equals(user.userMessages);
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + roleId;
        result = 31 * result + userName.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + (access ? 1 : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + role.hashCode();
        result = 31 * result + bets.hashCode();
        result = 31 * result + winningBets.hashCode();
        result = 31 * result + userMessages.hashCode();
        result = 31 * result + (unreadMessages ? 1 : 0);
        return result;
    }
}
