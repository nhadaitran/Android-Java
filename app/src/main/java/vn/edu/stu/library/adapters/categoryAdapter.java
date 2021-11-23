package vn.edu.stu.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.edu.stu.library.R;
import vn.edu.stu.library.model.categoryDTO;

public class categoryAdapter extends ArrayAdapter<categoryDTO> {

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected, parent, false);
        TextView txtSelected = convertView.findViewById(R.id.txtSelected);
        categoryDTO category = this.getItem(position);
        if (category != null){
            txtSelected.setText(category.getTitle_cat());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        TextView txtCategory = convertView.findViewById(R.id.txtCategory);
        categoryDTO category = this.getItem(position);
        if (category != null){
            txtCategory.setText(category.getTitle_cat());
        }
            return convertView;
    }

    public categoryAdapter(@NonNull Context context, int resource, @NonNull List<categoryDTO> objects) {
        super(context, resource, objects);
    }
}
