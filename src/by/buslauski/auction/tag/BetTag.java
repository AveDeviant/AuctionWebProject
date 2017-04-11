package by.buslauski.auction.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Created by Acer on 26.03.2017.
 */
public class BetTag extends TagSupport {
    private static final Logger LOGGER = LogManager.getLogger();
    private String header;
    private String lotTitle;
    private String price;
    private String date;
    private long rows;

    public void setHeader(String header) {
        this.header = header;
    }

    public void setRows(long rows) {
        this.rows = rows;
    }

    public void setLotTitle(String lotTitle) {
        this.lotTitle = lotTitle;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int doStartTag() {
        try {
            JspWriter writer = pageContext.getOut();
            writer.write("<table border='1'><colgroup span='2' title='title' />");
            writer.write("<thead><tr><th scope='col'>" + header + "</th></tr>");
            writer.write("<tr><td>" + lotTitle + "</td><td>" + price + "</td><td>" + date + "</td></tr></thead>");
            writer.write("<tbody><tr><td>");
        } catch (IOException e) {
            LOGGER.error("Exception during tag writing by JspWriter");
            e.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doAfterBody() {
        if (rows-- > 1) {
            try {
                pageContext.getOut().write("</td></tr><tr><td>");
            } catch (IOException e) {
                LOGGER.error("Exception during tag writing by JspWriter");
                e.printStackTrace();
            }
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }

    @Override
    public int doEndTag() {
        try {
            pageContext.getOut().write("</td></tr></tbody></table>");
        } catch (IOException e) {
            LOGGER.error("Exception during tag writing by JspWriter");
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
}
