package by.buslauski.auction.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Acer on 28.02.2017.
 */
public class UserValidator {
    private static final String PASSWORD_REGEXP = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[\\d])[\\w_-]{8,}$";
    private static final String Pass = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[\\d])[\\w_-]{8,}$";
    private static final String MAIL_REGEXP = "[\\w]{3,}@[a-z]{4,}.[a-z]{2,3}";
    private static final String USERNAME_REGEXP = "[\\w_-]{6,}";
    private static final String NAME_REGEXP = "[A-Za-z А-Яа-я ]{2,}";
    private static final String CITY_REGEXP = "[\\w,-.А-Яа-я ]{5,}";
    private static final String ADDRESS_REGEXP = "[\\w\\d,-.А-Яа-я ]{5,}";

    public static boolean checkLoginPassword(String username, String password) {
        Pattern patternPwd = Pattern.compile(PASSWORD_REGEXP);
        Pattern patternUnm = Pattern.compile(USERNAME_REGEXP);
        Matcher matcherPwd = patternPwd.matcher(password);
        Matcher matcherUnm = patternUnm.matcher(username);
        return (matcherPwd.matches() && matcherUnm.matches());
    }

    public static boolean checkEmail(String email){
        Pattern pattern = Pattern.compile(MAIL_REGEXP);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean checkUserInfo(String realName, String city, String address, String phone) {
        Pattern patternName = Pattern.compile(NAME_REGEXP);
        Pattern patternCity = Pattern.compile(CITY_REGEXP);
        Pattern patternAddress = Pattern.compile(ADDRESS_REGEXP);

        Matcher matcherName = patternName.matcher(realName);
        Matcher matcherCity = patternCity.matcher(city);
        Matcher matcherAddress = patternAddress.matcher(address);
        return matcherName.matches() && matcherCity.matches() && matcherAddress.matches();
    }
}
