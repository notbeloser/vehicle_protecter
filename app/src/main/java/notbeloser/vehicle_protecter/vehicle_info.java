package notbeloser.vehicle_protecter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class vehicle_info extends AppCompatActivity {
    TextView name,type,id;
    ImageView photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("車輛資訊");
        setSupportActionBar(toolbar);
        name = (TextView)findViewById(R.id.vehicle_info_name);
        type = (TextView)findViewById(R.id.vehicle_info_type);
        id = (TextView)findViewById(R.id.vehicle_info_id);
        photo = (ImageView)findViewById(R.id.vehicle_info_photo);

        Bundle bundle =this.getIntent().getExtras();
        name.setText(bundle.getString("name"));
        type.setText(type.getText().toString()+bundle.getString("type"));
        id.setText(id.getText().toString()+bundle.getString("id"));
        setImage(photo,bundle.getString("path"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton map = (FloatingActionButton) findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(vehicle_info.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }
    private void setImage(ImageView v,String selectedImagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false; // 不顯示照片
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
  /* 圖片縮小2倍 */
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false; // 顯示照片
        Bitmap bm = BitmapFactory.decodeFile(selectedImagePath, options);
        Log.i("selectedImagePath", selectedImagePath + "");
        v.setImageBitmap(bm);// 將圖片顯示
    }

}
