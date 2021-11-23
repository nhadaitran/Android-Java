package vn.edu.stu.library.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
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

public class EditBookActivity extends AppCompatActivity {
    AppDatabase db;
    EditText txtTitle, txtAuthor;
    Button btnEditBook, btnAddImage, btnDeleteBook;
    Spinner spCategory;
    ImageView imgView;
    bookDTO book;
    int id;
    categoryAdapter adapter;
    categoryDAO categoryDAO;
    String category;
    Bitmap bitmap = null;
    List<categoryDTO> categoryList = new ArrayList<>();
    int request = 113, result = 115;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        addControls();
        addEvents();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        book = (bookDTO) intent.getExtras().getSerializable("SELECTED");
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
        if (book.getImage() != null) {
            imgView.setImageBitmap(BitmapUtil.getBitmapFromString(book.getImage()));
        }
        id = book.getId();
    }

    private void addControls() {
        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthor);
        btnEditBook = findViewById(R.id.btnEditBook);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnDeleteBook = findViewById(R.id.btnDeleteBook);
        spCategory = findViewById(R.id.spCategory);
        imgView = findViewById(R.id.imgView);
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

        btnEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditBookActivity.this);
                builder.setTitle("Bạn có chắc muốn sửa sách này không?").setIcon(R.drawable.ic_edit);
                builder.setCancelable(false);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editBook();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        btnDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditBookActivity.this);
                builder.setTitle("Bạn có chắc muốn xoá sách này không?").setIcon(R.drawable.ic_delete);
                builder.setCancelable(false);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBook();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void deleteBook() {
        bookEntity bookEntity = covertAtBookDTOForBookEntity(book);
        db.bookDAO().delete(bookEntity);
        Intent intent = getIntent();
        intent.putExtra("DELETED", book);
        setResult(result, intent);
        finish();
    }

    private void editBook() {
        String title = txtTitle.getText().toString().trim();
        String author = txtAuthor.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(category)) {
            return;
        }
        bookDTO book = new bookDTO(id, title, author, category, BitmapUtil.getStringFromBitmap(bitmap));
        bookEntity bookEntity = covertAtBookDTOForBookEntity(book);
        db.bookDAO().update(bookEntity);
        Intent intent = getIntent();
        intent.putExtra("EDITED", book);
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
                Toast.makeText(EditBookActivity.this, "Quyền truy cập bộ nhớ bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(EditBookActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(EditBookActivity.this, new String[]{permission}, requestCode);
        }else{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(intent, request);
        }
    }
}