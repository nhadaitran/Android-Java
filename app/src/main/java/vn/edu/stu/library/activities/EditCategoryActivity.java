package vn.edu.stu.library.activities;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.library.R;
import vn.edu.stu.library.adapters.categoryAdapter;
import vn.edu.stu.library.dao.categoryDAO;
import vn.edu.stu.library.entity.bookEntity;
import vn.edu.stu.library.entity.categoryEntity;
import vn.edu.stu.library.model.categoryDTO;
import vn.edu.stu.library.util.AppDatabase;
import vn.edu.stu.library.util.DBConfigUtil;

public class EditCategoryActivity extends OptionsMenuActivity {
    Button btnAddCategory, btnEditCategory;
    ListView lvCategory;
    EditText txtCategory = null;
    List<categoryDTO> categoryList = new ArrayList<>();
    AppDatabase db;
    categoryAdapter adapter;
    categoryDAO categoryDAO;
    categoryDTO selected;
    int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        addControls();
        addEvents();
    }

    private void addControls() {
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnEditCategory = findViewById(R.id.btnEditCategory);
        lvCategory = findViewById(R.id.lvCategory);
        txtCategory = findViewById(R.id.txtCategory);
        loadDatabase();
        adapter = new categoryAdapter(this, R.layout.activity_item_category, categoryList);
        lvCategory.setAdapter(adapter);
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

    protected categoryEntity covertAtCategoryDTOForCategoryEntity(categoryDTO categoryDTO) {
        return new categoryEntity(categoryDTO.getTitle_cat().trim());
    }

    private void addEvents() {
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = txtCategory.getText().toString().trim();
                int flag = 0;
                if (newTitle.length() == 0) {
                    Toast.makeText(
                            EditCategoryActivity.this,
                            getResources().getString(R.string.error_empty),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    for (categoryDTO item : categoryList) {
                        if (newTitle.compareToIgnoreCase(item.getTitle_cat()) == 0) {
                            Toast.makeText(
                                    EditCategoryActivity.this,
                                    getResources().getString(R.string.error_exists),
                                    Toast.LENGTH_SHORT
                            ).show();
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        categoryEntity categoryEntity = new categoryEntity(newTitle);
                        db.categoryDAO().insert(categoryEntity);
                        categoryDTO newCat = new categoryDTO(newTitle);
                        Toast.makeText(
                                EditCategoryActivity.this,
                                getResources().getString(R.string.added),
                                Toast.LENGTH_SHORT
                        ).show();
                        categoryList.add(newCat);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        btnEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = txtCategory.getText().toString().trim();
                int flag = 0;
                if (pos == -1) {
                    Toast.makeText(
                            EditCategoryActivity.this,
                            getResources().getString(R.string.error_notselect),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    for (categoryDTO item : categoryList) {
                        if (newTitle.compareToIgnoreCase(item.getTitle_cat()) == 0) {
                            Toast.makeText(
                                    EditCategoryActivity.this,
                                    getResources().getString(R.string.error_exists),
                                    Toast.LENGTH_SHORT
                            ).show();
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0) {
                        categoryDAO.update(selected.getTitle_cat(), newTitle);
                        selected.setTitle_cat(newTitle);
                        Toast.makeText(
                                EditCategoryActivity.this,
                                getResources().getString(R.string.changed),
                                Toast.LENGTH_SHORT
                        ).show();
                        categoryList.set(pos, selected);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryAdapter adapter = (categoryAdapter) lvCategory.getAdapter();
                if (position >= 0 && position < adapter.getCount()) {
                    selected = adapter.getItem(position);
                    txtCategory.setText(selected.getTitle_cat());
                    pos = position;
                }
            }
        });

        lvCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                categoryAdapter adapter = (categoryAdapter) lvCategory.getAdapter();
                if (position >= 0 && position < adapter.getCount()) {
                    selected = adapter.getItem(position);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(EditCategoryActivity.this);
                builder.setTitle(getResources().getString(R.string.ask_delete)).setIcon(R.drawable.ic_delete);
                builder.setCancelable(false);
                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<bookEntity> listBook = db.bookDAO().getCategory(selected.getTitle_cat());
                        if(listBook.isEmpty()) {
                            deleteCategory();
                            Toast.makeText(
                                    EditCategoryActivity.this,
                                    getResources().getString(R.string.deleted),
                                    Toast.LENGTH_SHORT
                            ).show();
                            adapter.notifyDataSetChanged();
                        }else  {
                            Toast.makeText(
                                    EditCategoryActivity.this,
                                    getResources().getString(R.string.cannot_delete),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        listBook.clear();
                    }
                });
                builder.setNegativeButton(

                        getResources().

                                getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    private void deleteCategory() {
        categoryEntity categoryEntity = covertAtCategoryDTOForCategoryEntity(selected);
        db.categoryDAO().delete(categoryEntity);
        adapter.remove(selected);
        categoryList.remove(selected);
        pos = -1;
        txtCategory.setText("");
    }
}