package notbeloser.vehicle_protecter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class edit_vehicle extends AppCompatActivity {
    public Spinner edit_spinner_vehicle_type;
    public ArrayAdapter<String> vehicle_type;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String selectedImagePath; // 圖片檔案位置
    public ImageView edit_vehicle_image;
    public EditText edit_vehicle_name,edit_vehicle_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("車輛編輯");
        setSupportActionBar(toolbar);
        edit_spinner_vehicle_type = (Spinner)findViewById(R.id.edit_spinner_vehicle_type);
        edit_vehicle_image = (ImageView)findViewById(R.id.edit_vehicle_image);
        final String[] type={"汽車","機車","單車","垃圾車","公車","計程車"};
        vehicle_type = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type);
        vehicle_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_spinner_vehicle_type.setAdapter(vehicle_type);
        edit_vehicle_name = (EditText)findViewById(R.id.edit_vehicle_name);
        edit_vehicle_id = (EditText)findViewById(R.id.edit_vehicle_id);


        try {
            SharedPreferences preferencesGet = getApplicationContext().
                    getSharedPreferences("image",android.content.Context.MODE_PRIVATE);
            selectedImagePath = preferencesGet.getString("selectedImagePath","");
            // 圖片檔案位置，預設為空
            Log.i("selectedImagePath", selectedImagePath + "");
            setImage();
        }
        catch (Exception e) {
        }


    }

    public void ButtonOnClick(View v)
    {
        switch (((Button)v).getId())
        {
            case R.id.edit_vehicle_image_button:
                selectImage();

                break;
            case R.id.edit_vehicle_cancel:
                this.onBackPressed();
                break;
            case R.id.edit_vehicle_summit:

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("id",edit_vehicle_id.getText().toString());
                bundle.putString("name",edit_vehicle_name.getText().toString());
                bundle.putString("image_path",selectedImagePath);
                bundle.putString("type",edit_spinner_vehicle_type.getSelectedItem().toString());
                intent.putExtras(bundle);
                //切換Activity
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
    //-------------------------------------------------------------------------------------
    /* 設定圖片 */
    private void setImage() {
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
        edit_vehicle_image.setImageBitmap(bm);// 將圖片顯示
    }

    private void selectImage() {
        final String item1, item2, item3;
        item1 = "拍一張照";
        item2 = "從圖庫選取";
        item3 = "取消";
        final CharSequence[] items = { item1, item2, item3 };

        AlertDialog.Builder builder = new AlertDialog.Builder(edit_vehicle.this);
        builder.setTitle("選擇照片來源");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0: // 拍一張照
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra("crop", "true");// crop=true 有這句才能叫出裁剪頁面.
                        intent.putExtra("aspectX", 16);// 这兩項為裁剪框的比例.
                        intent.putExtra("aspectY", 9);
                        startActivityForResult(intent, REQUEST_CAMERA);
                        break;
                    case 1: // 從圖庫選取
                        Intent intent1 = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent1.setType("image/*");
                        intent1.putExtra("crop", "true");// crop=true 有這句才能叫出裁剪頁面.
                        intent1.putExtra("aspectX", 16);// 这兩項為裁剪框的比例.
                        intent1.putExtra("aspectY", 9);
                        startActivityForResult(Intent.createChooser(intent1, "選擇開啟圖庫"),
                                SELECT_FILE);
                        break;
                    default: // 取消
                        dialog.dismiss(); // 關閉對話框
                        break;
                }
            }
        });
        builder.show();
    }

    /* 啟動選擇方式 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) // 從圖庫開啟
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA) // 拍照
                onCaptureImageResult(data);
        }
    }

    /* 拍照 */
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory()+"/VehicleImage",
                System.currentTimeMillis() + ".jpg"); // 輸出檔案名稱
        selectedImagePath = destination + ""; // 輸出檔案位置
        FileOutputStream fo;
        try {
            destination.createNewFile(); // 建立檔案
            fo = new FileOutputStream(destination); // 輸出
            fo.write(bytes.toByteArray());
            fo.close(); }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        edit_vehicle_image.setImageBitmap(thumbnail); // 將圖片顯示
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        selectedImagePath = cursor.getString(column_index); // 選擇的照片位置
        setImage(); // 設定圖片
    }

    /* 結束時 */
    @Override
    protected void onDestroy() {
        super.onDestroy();
  /* 紀錄圖片檔案位置 */
        SharedPreferences preferencesSave = getApplicationContext().
                getSharedPreferences("image",android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesSave.edit();
        editor.putString("selectedImagePath", selectedImagePath); // 紀錄最後圖片位置
        editor.commit();
        Log.i("onDestroy", "onDestroy");
    }

}
