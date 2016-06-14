package notbeloser.vehicle_protecter;

/**
 * Created by notbeloser on 2016/5/7.
 */
public class vehicle {
    String vehicle_name;
    String vehicle_id;
    String vehicle_image;
    String vehicle_type;
    boolean lock_state=false;
    public  vehicle(String vehicle_name , String vehicle_id,String vehicle_image,String vehicle_type){
        this.vehicle_name = vehicle_name;
        this.vehicle_id = vehicle_id;
        this.vehicle_image = vehicle_image;
        this.vehicle_type = vehicle_type;
    }
    public void set_lock_state(boolean s)
    {
        lock_state = s;
    }
    public boolean get_lock_state()
    {
        return lock_state;
    }
}
