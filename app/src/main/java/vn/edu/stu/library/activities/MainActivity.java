package vn.edu.stu.library.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.library.R;
import vn.edu.stu.library.adapters.bookAdapter;
import vn.edu.stu.library.adapters.categoryAdapter;
import vn.edu.stu.library.dao.bookDAO;
import vn.edu.stu.library.dao.categoryDAO;
import vn.edu.stu.library.entity.bookEntity;
import vn.edu.stu.library.entity.categoryEntity;
import vn.edu.stu.library.model.bookDTO;
import vn.edu.stu.library.model.categoryDTO;
import vn.edu.stu.library.util.AppDatabase;
import vn.edu.stu.library.util.DBConfigUtil;

public class MainActivity extends OptionsMenuActivity {
    AppDatabase db;
    Button btnCancel, btnEditCat;
    List<bookDTO> bookList = new ArrayList<>();
    ListView lvBooklist;
    bookDTO selected;
    FloatingActionButton fbtnAddBook;
    bookDAO bookDAO;
    Spinner spCategory;
    categoryAdapter adapter;
    categoryDAO categoryDAO;
    List<categoryDTO> categoryList = new ArrayList<>();
    List<bookEntity> bookEntityList = new ArrayList<>();
    int bookPosition;
    int request = 113, result = 115;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        showListView();
        addEvents();
    }

    private void addEvents() {
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookEntityList = bookDAO.getCategory(adapter.getItem(position).getTitle_cat());
                bookList.clear();
                for (bookEntity bookEntity : bookEntityList) {
                    bookList.add(covertAtBookEntityForBookDTO(bookEntity));
                }
                showListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookList.clear();
                List<bookEntity> bookEntityList = bookDAO.getAll();
                for (bookEntity bookEntity : bookEntityList) {
                    bookList.add(covertAtBookEntityForBookDTO(bookEntity));
                }
                showListView();
            }
        });

        btnEditCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,
                        EditCategoryActivity.class
                );
                startActivity(intent);
            }
        });

        fbtnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,
                        AddBookActivity.class
                );
                startActivityForResult(intent, request);
            }
        });

        lvBooklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bookAdapter adapter = (bookAdapter) lvBooklist.getAdapter();
                if (position >= 0 && position < adapter.getCount()) {
                    Intent intent = new Intent(
                            MainActivity.this,
                            BookInfoActivity.class
                    );
                    selected = adapter.getItem(position);
                    bookPosition = position;
                    intent.putExtra("SELECTED", selected);
                    startActivityForResult(intent, request);
                }
            }
        });

        lvBooklist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                bookAdapter adapter = (bookAdapter) lvBooklist.getAdapter();
                if (position >= 0 && position < adapter.getCount()) {
                    Intent intent = new Intent(
                            MainActivity.this,
                            EditBookActivity.class
                    );
                    selected = adapter.getItem(position);
                    bookPosition = position;
                    intent.putExtra("SELECTED", selected);
                    startActivityForResult(intent, request);
                }
                return true;
            }
        });
    }

    protected void init() {
        lvBooklist = findViewById(R.id.lvBookList);
        fbtnAddBook = findViewById(R.id.fbtnAddBook);
        spCategory = findViewById(R.id.spCategory);
        btnCancel = findViewById(R.id.btnCancel);
        btnEditCat = findViewById(R.id.btnEditCat);
        loadDatabase();
        adapter = new categoryAdapter(this, R.layout.item_selected, categoryList);
        spCategory.setAdapter(adapter);
    }

    protected void showListView() {
        bookAdapter adapter = new bookAdapter(this, R.layout.activity_item_book_adapter, bookList);
        lvBooklist.setAdapter(adapter);
    }

    protected void loadDatabase() {
        DBConfigUtil.copyDatabaseFromAssets(this);
        db = AppDatabase.getAppDatabase(this);
        bookDAO = db.bookDAO();
        categoryDAO = db.categoryDAO();
        getList();
    }

    private void getList() {
        bookList.clear();
        categoryList.clear();
        List<bookEntity> bookEntityList = bookDAO.getAll();
        for (bookEntity bookEntity : bookEntityList) {
            bookList.add(covertAtBookEntityForBookDTO(bookEntity));
        }
        List<categoryEntity> categoryEntityList = categoryDAO.getAll();
        for (categoryEntity categoryEntity : categoryEntityList) {
            categoryList.add(new categoryDTO(categoryEntity.getTitle()));
        }
    }

    protected bookDTO covertAtBookEntityForBookDTO(bookEntity bookEntity) {
        return new bookDTO(bookEntity.getId(), bookEntity.getTitle(),
                bookEntity.getAuthor(), bookEntity.getCategory(),
                bookEntity.getImage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDatabase.destroyInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.request) {
            if (resultCode == this.result) {
                if (data.hasExtra("ADDED")) {
                    bookDTO book = (bookDTO) data.getSerializableExtra("ADDED");
                    bookList.add(book);
                    showListView();
                    Toast.makeText(
                            this,
                            "Thêm sách thành công!",
                            Toast.LENGTH_SHORT
                    ).show();
                    loadDatabase();
                }
                if (data.hasExtra("EDITED")) {
                    bookDTO book = (bookDTO) data.getSerializableExtra("EDITED");
                    bookList.set(bookPosition, book);
                    showListView();
                    Toast.makeText(
                            this,
                            "Chỉnh sừa sách thành công!",
                            Toast.LENGTH_SHORT
                    ).show();
                    loadDatabase();
                }
                if (data.hasExtra("DELETED")) {
                    bookDTO book = (bookDTO) data.getSerializableExtra("DELETED");
                    bookList.remove(bookPosition);
                    showListView();
                    Toast.makeText(
                            this,
                            "Xoá sách thành công!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }
    }
}