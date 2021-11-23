package vn.edu.stu.library.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import vn.edu.stu.library.entity.categoryEntity;

@Dao
public interface categoryDAO {
    @Query("SELECT * FROM category")
    List<categoryEntity> getAll();

    @Query("UPDATE category SET c_title = :newTitle WHERE c_title = :oldTitle")
    void update(String newTitle, String oldTitle);

    @Insert
    void insert (categoryEntity category);

    @Delete
    int delete (categoryEntity category);

}
