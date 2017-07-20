package by.buslauski.auction.servlet;

import by.buslauski.auction.action.Command;
import by.buslauski.auction.action.CommandFactory;
import by.buslauski.auction.connection.ConnectionPool;
import by.buslauski.auction.response.ResponseType;
import by.buslauski.auction.response.PageResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Buslauski Mikita
 */

@WebServlet("/Controller")
@MultipartConfig
public class Controller extends HttpServlet {
    private static CommandFactory clientCommand = new CommandFactory();

    public void init() throws ServletException {
        super.init();
        ConnectionPool.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    public void destroy() {
        ConnectionPool.getInstance().closeConnections();
        super.destroy();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = clientCommand.getCurrentCommand(request);
        PageResponse pageResponse = command.execute(request);
        if (pageResponse != null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(pageResponse.getPage());
            if (pageResponse.getResponseType() == ResponseType.REDIRECT) {
                response.sendRedirect(pageResponse.getPage());
            } else {
                dispatcher.forward(request, response);
            }
        }
    }
}
