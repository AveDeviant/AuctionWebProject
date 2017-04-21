package by.buslauski.auction.tag;

import by.buslauski.auction.entity.Lot;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Acer on 07.04.2017.
 */
public class DateTag extends TagSupport {
    private static Logger LOGGER = LogManager.getLogger();
    private static final String LOT = "lot";
    private static final String LOCALIZATION = "locale";
    private static final String ENGLISH_LOCALIZATION = "en_US";
    private static final String RUSSIAN_LOCALIZATION = "default";
    private static final String RUSSIAN_FORMAT = "dd MMMM yyyy";
    private static final String ENGLISH_FORMAT = "MMMM dd, yyyy";

    @Override
    public int doStartTag() throws JspTagException {
        try {
            String locale = (String) pageContext.getSession().getAttribute(LOCALIZATION);
            Lot lot = (Lot) pageContext.getRequest().getAttribute(LOT);
            SimpleDateFormat dateFormat = null;
            if (locale == null || RUSSIAN_LOCALIZATION.equalsIgnoreCase(locale)) {
                dateFormat = new SimpleDateFormat(RUSSIAN_FORMAT, Locale.getDefault());
            }
            if (ENGLISH_LOCALIZATION.equalsIgnoreCase(locale)) {
                dateFormat = new SimpleDateFormat(ENGLISH_FORMAT, Locale.ENGLISH);
            }

            Date date = java.sql.Date.valueOf(lot.getDateAvailable());
            String dateAsString = dateFormat.format(date);
            JspWriter writer = pageContext.getOut();
            writer.write(dateAsString);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new JspTagException();
        }
        return SKIP_BODY;
    }
}
