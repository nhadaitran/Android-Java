package vn.edu.stu.library.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class bookDTO implements Serializable {
    private Integer id;
    private String title;
    private String author;
    private String category;
    private byte[] image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public bookDTO(Integer id, String title, String author, String category, byte[] image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.image = image;
    }

    public bookDTO() {
    }
}
