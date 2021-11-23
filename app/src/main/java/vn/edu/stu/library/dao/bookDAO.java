package vn.edu.stu.library.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import vn.edu.stu.library.entity.bookEntity;

@Dao
 public interface bookDAO {
    @Query("SELECT * FROM book")
    List<bookEntity> getAll();

   @Query("SELECT * FROM book WHERE b_category = :category")
   List<bookEntity> getCategory(String category);

    @Insert
    void insert (bookEntity book);

    @Update
    int update (bookEntity book);

    @Delete
    int delete (bookEntity book);
}
