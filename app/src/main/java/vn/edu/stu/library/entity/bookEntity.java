package vn.edu.stu.library.entity;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "book")
public class bookEntity {
    public bookEntity(@NonNull Integer id, String title, String author, String category, byte[] image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.image = image;
    }
    @Ignore
    public bookEntity() {
    }

    @ColumnInfo(name = "b_id")
    @PrimaryKey
    @NonNull
    private Integer id;

    @ColumnInfo(name = "b_title")
    private String title;


    @ColumnInfo(name = "b_author")
    private String author;


    @ColumnInfo(name = "b_category")
    private String category;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB , name = "b_image")
    private byte[] image;


    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
