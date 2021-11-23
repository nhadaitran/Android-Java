package vn.edu.stu.library.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

public class categoryDTO {
    private String title_cat;

    public String getTitle_cat() {
        return title_cat;
    }

    public void setTitle_cat(String title_cat) {
        this.title_cat = title_cat;
    }

    public categoryDTO(String title_cat) {
        this.title_cat = title_cat;
    }

    public categoryDTO() {
    }
}
