package vn.edu.stu.library.activities;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
//                ExitActivity.exit(context);
                Toast.makeText(this,getResources().getString(R.string.in_progess),Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
