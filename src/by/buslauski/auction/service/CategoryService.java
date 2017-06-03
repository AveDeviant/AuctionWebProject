package by.buslauski.auction.service;

import by.buslauski.auction.entity.Category;
import by.buslauski.auction.service.exception.ServiceException;

import java.util.ArrayList;

/**
 * @author Buslauski Mikita
 */
public interface CategoryService {
    ArrayList<Category> getAllCategories() throws ServiceException;

    void addCategory(String name) throws ServiceException;

    boolean categoryExists(String category) throws ServiceException;
}
