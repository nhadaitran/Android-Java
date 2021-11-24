package vn.edu.stu.library.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.stu.library.R;
import vn.edu.stu.library.model.bookDTO;
import vn.edu.stu.library.util.BitmapUtil;

public class BookInfoActivity extends OptionsMenuActivity {
    TextView txtTitle, txtAuthor, txtCategory;
    Button btnEdit;
    ImageView imgView;
    bookDTO book;
    int id, request = 113, result = 115;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        addControls();
        addEvents();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        book = (bookDTO) intent.getExtras().getSerializable("SELECTED");
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
        txtCategory.setText(book.getCategory());
        if (book.getImage() != null) {
            imgView.setImageBitmap(BitmapUtil.getBitmapFromString(book.getImage()));
        }
        id = book.getId();
    }

    private void addControls() {
        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtCategory = findViewById(R.id.txtCategory);
        btnEdit = findViewById(R.id.btnEdit);
        imgView = (ImageView) findViewById(R.id.imgView);
    }

    private void addEvents() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        BookInfoActivity.this,
                        EditBookActivity.class
                );
                intent.putExtra("SELECTED", book);
                startActivityForResult(intent, request);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.request) {
            if (resultCode == this.result) {
                if (data.hasExtra("EDITED")) {
                    book = (bookDTO) data.getSerializableExtra("EDITED");
                    Intent intent = getIntent();
                    intent.putExtra("EDITED", book);
                    setResult(result, intent);
                    txtTitle.setText(book.getTitle());
                    txtAuthor.setText(book.getAuthor());
                    txtCategory.setText(book.getCategory());
                    if(book.getImage()!=null){
                        imgView.setImageBitmap(BitmapUtil.getBitmapFromString(book.getImage()));
                    }
                    Toast.makeText(
                            this,
                            getResources().getString(R.string.changed),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                if (data.hasExtra("DELETED")) {
                    book = (bookDTO) data.getSerializableExtra("DELETED");
                    Intent intent = getIntent();
                    intent.putExtra("DELETED", book);
                    setResult(result, intent);
                    finish();
                }
            }
        }
    }
}