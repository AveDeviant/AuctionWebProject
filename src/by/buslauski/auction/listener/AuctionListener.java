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
         * This method every hour checks the results of the auction.
         */
        @Override
        public void run() {
            AuctionService auctionService = new AuctionServiceImpl();
            try {
                while (true) {
                    auctionService.setWinner();
                    Thread.sleep(3600000);
                }
            } catch (ServiceException e) {
                LOGGER.log(Level.ERROR, e + " Exception during determining auction results.");
            } catch (InterruptedException e) {
                LOGGER.log(Level.ERROR, e + " Thread has been incorrectly terminated.");
            }
        }
    }
}