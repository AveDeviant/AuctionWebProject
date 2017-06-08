package by.buslauski.auction.entity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents info about entity "user".
 *
 * @author Mikita Buslauski
 */
public class User {
    /**
     * Unique identifier of user.
     */
    private long userId;

    /**
     * Identifier of user role.
     */
    private int roleId;

    /**
     * User login.
     */
    private String userName;

    /**
     * User alias in the system.
     */
    private String alias;

    /**
     * User e-mail.
     */
    private String email;

    /**
     * User's city.
     */
    private String city;

    /**
     * User's address.
     */
    private String address;

    /**
     * User's contact number;
     */
    private String phoneNumber;

    /**
     * User's access to the auction.
     */
    private boolean access;

    /**
     * User's real name, surname etc.
     */
    private String name;

    /**
     * User role in the system.
     *
     * @see Role
     */
    private Role role;

    /**
     * All bets which made by user.
     *
     * @see Bet
     */
    private ArrayList<Bet> bets;

    /**
     * Bets which made by user and  were the highest among the rest.
     *
     * @see Bet
     */
    private ArrayList<Bet> winningBets;

    /**
     * All messages that user were sent or received.
     *
     * @see UserMessage
     */
    private ArrayList<UserMessage> userMessages;

    /**
     * Indicator of new messages to user.
     */
    private boolean unreadMessages;

    /**
     * User rating.
     */
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
        Bet[] bets1 = (Bet[]) bets.toArray();
        Bet[] bets2 = (Bet[]) user.bets.toArray();
        if (!Arrays.equals(bets1, bets2)) return false;
        Bet[] winning1 = (Bet[]) winningBets.toArray();
        Bet[] winning2 = (Bet[]) user.winningBets.toArray();
        if (!Arrays.equals(winning1, winning2)) return false;
        UserMessage[] messages1 = (UserMessage[]) userMessages.toArray();
        UserMessage[] messages2 = (UserMessage[]) user.userMessages.toArray();
        return Arrays.equals(messages1, messages2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId, access, unreadMessages, userName, email, city, address, phoneNumber,
                name, alias, role, bets, winningBets, userMessages, userRating);
    }
}
