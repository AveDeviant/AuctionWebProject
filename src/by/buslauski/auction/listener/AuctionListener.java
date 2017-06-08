package by.buslauski.auction.listener;

import by.buslauski.auction.service.AuctionService;
import by.buslauski.auction.service.exception.ServiceException;
import by.buslauski.auction.service.impl.AuctionServiceImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalTime;

/**
 * @author Mikita Buslauski
 */
@WebListener
public class AuctionListener implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ResultDeterminer resultDeterminer = new ResultDeterminer();
        resultDeterminer.start();

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private class ResultDeterminer extends Thread {

        /**
         * Determination of the auction results every day at 00:00.
         */
        @Override
        public void run() {
            AuctionService auctionService = new AuctionServiceImpl();
            try {
                while (true) {
                    int hours = LocalTime.now().getHour();
                    int minutes = LocalTime.now().getMinute();
                    LocalTime currentTime = LocalTime.of(hours, minutes);
                    if (currentTime.compareTo(LocalTime.MIDNIGHT) == 0) {
                        auctionService.setWinner();
                    }
                    Thread.sleep(60000);
                }
            } catch (ServiceException e) {
                LOGGER.log(Level.ERROR, e + " Exception during determining auction results.");
            } catch (InterruptedException e) {
                LOGGER.log(Level.ERROR, e + " Thread has been incorrectly terminated.");
            }
        }
    }
}