package vn.edu.stu.library.activities;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.stu.library.R;

public class OptionsMenuActivity extends AppCompatActivity {
    Context context;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent =new Intent(
                        this,
                        AboutActivity.class
                );
                startActivity(intent);
                return true;
            case R.id.action_exit:
                ExitActivity.exit(context);
        }
        return super.onOptionsItemSelected(item);
    }

}
