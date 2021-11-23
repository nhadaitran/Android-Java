package vn.edu.stu.library.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "category")
public class categoryEntity {
    @ColumnInfo(name = "c_title")
    @PrimaryKey
    @NonNull
    private String title;

    public categoryEntity() {
    }

    public categoryEntity(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }
}