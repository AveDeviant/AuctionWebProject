package by.buslauski.auction.entity;

/**
 * Created by Acer on 17.03.2017.
 */
public class Category {
    private int categoryId;
    private String value;

    public Category(int categoryId, String value) {
        this.categoryId = categoryId;
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (categoryId != category.categoryId) return false;
        return value.equals(category.value);
    }

    @Override
    public int hashCode() {
        int result = categoryId;
        result = 31 * result + value.hashCode();
        return result;
    }
}
