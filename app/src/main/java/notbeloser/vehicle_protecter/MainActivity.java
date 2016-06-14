package notbeloser.vehicle_protecter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public List<vehicle> vehicles;
    public RecyclerAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("車輛管理");
        setSupportActionBar(toolbar);
        vehicles = new ArrayList<vehicle>();
        //--------------新增cardview 用---------------------------------
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerAdapter = new RecyclerAdapter(this,vehicles);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        //--------------------------------------------------------------

        //----------------------------按下新增車輛的floating button--------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, edit_vehicle.class);
                startActivityForResult(intent,0);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------------新增卡片-------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(resultCode){
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                vehicles.add(new vehicle(bundle.getString("name"),
                        bundle.getString("id"), bundle.getString("image_path"), bundle.getString("type")));

                recyclerAdapter.notifyDataSetChanged();
                break;
            default:
                break;

        }
    }
}
