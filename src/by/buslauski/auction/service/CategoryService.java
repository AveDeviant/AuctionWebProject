package by.buslauski.auction.service;

import by.buslauski.auction.entity.Category;
import by.buslauski.auction.exception.ServiceException;

import java.util.ArrayList;

/**
 * Created by Acer on 14.04.2017.
 */
public interface CategoryService {
    ArrayList<Category> getAllCategories() throws ServiceException;

    void addCategory(String name) throws ServiceException;
}
