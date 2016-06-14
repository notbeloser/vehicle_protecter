package notbeloser.vehicle_protecter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView vehicle_photo,vehicle_lock_state;
        private TextView vehicle_name,vehicle_lock_state_txt;
        private View container;


        public ViewHolder(View view) {
            super(view);
            vehicle_photo = (ImageView) view.findViewById(R.id.vehicle_photo);
            vehicle_lock_state = (ImageView)view.findViewById(R.id.vehicle_lock_state);
            vehicle_name = (TextView) view.findViewById(R.id.vehicle_name);
            vehicle_lock_state_txt = (TextView)view.findViewById(R.id.vehicle_lock_state_txt);
            container = view.findViewById(R.id.card_view);
        }
    }

    final private List<vehicle> vehicles;
    final private Activity activity;

    public RecyclerAdapter(Activity activity, List<vehicle> vehicles) {
        this.vehicles = vehicles;
        this.activity = activity;
    }

    private View.OnClickListener onClickListener(final int i)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("name",vehicles.get(i).vehicle_name);
                bundle.putString("id",vehicles.get(i).vehicle_id);
                bundle.putString("type",vehicles.get(i).vehicle_type);
                bundle.putString("path",vehicles.get(i).vehicle_image);
                intent.putExtras(bundle);
                intent.setClass(activity , vehicle_info.class);
                //切換Activity
                activity.startActivityForResult(intent,0);
            }
        };
    }
    public View.OnClickListener onLockListener(final TextView t,final int i)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView)v;

                vehicle car = vehicles.get(i);
                if(car.get_lock_state())
                {
                    imageView.setImageResource(R.drawable.ic_unlock);
                    car.set_lock_state(false);
                    vehicles.set(i,car);
                    t.setText("unlock");
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_locked);
                    car.set_lock_state(true);
                    vehicles.set(i,car);
                    t.setText("lock");
                }
            }
        };
    }
    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    @Override //得到cardview的大架構
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override//新增cardview時 抓arraylist的資料新增
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.vehicle_name.setText(vehicles.get(i).vehicle_name);

        setImage(viewHolder.vehicle_photo,vehicles.get(i).vehicle_image);

        viewHolder.container.setOnClickListener(onClickListener(i));
        viewHolder.vehicle_lock_state.setOnClickListener(onLockListener(viewHolder.vehicle_lock_state_txt,i));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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
