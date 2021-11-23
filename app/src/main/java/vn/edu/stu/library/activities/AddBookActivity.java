package vn.edu.stu.library.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.library.R;
import vn.edu.stu.library.adapters.categoryAdapter;
import vn.edu.stu.library.dao.categoryDAO;
import vn.edu.stu.library.entity.bookEntity;
import vn.edu.stu.library.entity.categoryEntity;
import vn.edu.stu.library.model.bookDTO;
import vn.edu.stu.library.model.categoryDTO;
import vn.edu.stu.library.util.AppDatabase;
import vn.edu.stu.library.util.DBConfigUtil;
import vn.edu.stu.library.util.BitmapUtil;

public class AddBookActivity extends AppCompatActivity {
    AppDatabase db;
    EditText txtTitle, txtAuthor;
    Button btnAddBook, btnAddImage;
    Spinner spCategory;
    ImageView imgView;
    categoryAdapter adapter;
    categoryDAO categoryDAO;
    String category;
    Bitmap bitmap = null;
    List<categoryDTO> categoryList = new ArrayList<>();
    int request = 113, result = 115;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthor);
        btnAddBook = findViewById(R.id.btnAddBook);
        btnAddImage = findViewById(R.id.btnAddImage);
        spCategory = findViewById(R.id.spCategory);
        imgView = (ImageView) findViewById(R.id.imgView);
        loadDatabase();
        adapter = new categoryAdapter(this, R.layout.item_selected, categoryList);
        spCategory.setAdapter(adapter);
    }

    protected void loadDatabase() {
        DBConfigUtil.copyDatabaseFromAssets(this);
        db = AppDatabase.getAppDatabase(this);
        categoryDAO = db.categoryDAO();
        getListCategory();
    }

    private void getListCategory() {
        List<categoryEntity> categoryEntityList = categoryDAO.getAll();
        for (categoryEntity categoryEntity : categoryEntityList) {
            categoryList.add(new categoryDTO(categoryEntity.getTitle()));
        }
    }

    protected bookEntity covertAtBookDTOForBookEntity(bookDTO book) {
        return new bookEntity(book.getId(), book.getTitle(), book.getAuthor(), book.getCategory(), book.getImage());
    }

    private void addEvents() {
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, request);
            }
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = adapter.getItem(position).getTitle_cat();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
    }

    private void addBook() {
        String title = txtTitle.getText().toString().trim();
        String author = txtAuthor.getText().toString().trim();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(category)) {
            return;
        }
        bookDTO book = new bookDTO(null, title, author, category, BitmapUtil.getStringFromBitmap(bitmap));
        bookEntity bookEntity = covertAtBookDTOForBookEntity(book);
        db.bookDAO().insert(bookEntity);
        Intent intent = getIntent();
        intent.putExtra("ADDED", book);
        setResult(result, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == request && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream input = getContentResolver().openInputStream(selectedImageUri);
                bitmap = BitmapFactory.decodeStream(input);
                bitmap = BitmapUtil.resizeBitmap(bitmap,500);
                imgView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == request) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, request);
            } else {
                Toast.makeText(AddBookActivity.this, "Quyền truy cập bộ nhớ bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(AddBookActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AddBookActivity.this, new String[]{permission}, requestCode);
        }else{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(intent, request);
        }
    }
}