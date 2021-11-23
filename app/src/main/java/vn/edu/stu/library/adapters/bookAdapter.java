package vn.edu.stu.library.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.edu.stu.library.R;
import vn.edu.stu.library.model.bookDTO;
import vn.edu.stu.library.util.BitmapUtil;

public class bookAdapter extends ArrayAdapter<bookDTO> {
    private List<bookDTO> book_List;

    public bookAdapter(@NonNull Context context, int resource, @NonNull List<bookDTO> book_List) {
        super(context, resource, book_List);
        this.book_List = book_List;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_book_adapter, parent, false);
        bookDTO book = this.book_List.get(position);
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtAuthor = convertView.findViewById(R.id.txtAuthor);
        TextView txtCategory = convertView.findViewById(R.id.txtCategory);
        ImageView imageView = convertView.findViewById(R.id.imgView);
        txtTitle.setText(String.valueOf(book.getTitle()));
        txtAuthor.setText(String.valueOf(book.getAuthor()));
        txtCategory.setText(String.valueOf(book.getCategory()));
        if (book.getImage() != null) {
            imageView.setImageBitmap(BitmapUtil.getBitmapFromString(book.getImage()));
        }
        return convertView;
    }
}