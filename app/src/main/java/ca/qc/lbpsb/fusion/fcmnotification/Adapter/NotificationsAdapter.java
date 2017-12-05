package ca.qc.lbpsb.fusion.fcmnotification.Adapter;


        import android.content.Context;
        import android.graphics.Color;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import com.bumptech.glide.Glide;
        import java.util.List;

        import ca.qc.lbpsb.fusion.fcmnotification.Model.Notifications;
        import ca.qc.lbpsb.fusion.fcmnotification.R;

/**
 * Created by Administrator on 12/3/2017.
 */


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<Notifications> itemList;

    public NotificationsAdapter(Context mCtx, List<Notifications> itemList) {
        this.mCtx = mCtx;
        this.itemList = itemList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.message_item, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Notifications notification = itemList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(R.drawable.board_logo)
                .into(holder.imageView);

        holder.textViewTitle.setText(notification.getTitle());
        holder.textViewMessage.setText(notification.getMessage());
       String priority =  String.valueOf(notification.getPriority());

       switch (priority){
           case "1":
               holder.textViewPriority.setText(priority);
               holder.textViewPriority.setBackgroundColor(Color.RED);
               // icon view
               holder.imageView.setImageResource(R.drawable.ic_event);
               break;
           case "2":
               holder.textViewPriority.setText(priority);
               holder.textViewPriority.setBackgroundColor(Color.BLUE);
               break;
           case "3":
               holder.textViewPriority.setText(priority);
               holder.textViewPriority.setBackgroundColor(Color.rgb(94, 168, 6));
               break;
           case "4":
               holder.textViewPriority.setText(priority);
               holder.textViewPriority.setBackgroundColor(Color.MAGENTA);
               break;
           case "5":
               holder.textViewPriority.setText(priority);
               holder.textViewPriority.setBackgroundColor(Color.rgb(66, 176, 244));
               break;
           case "6":
               holder.textViewPriority.setText(priority);
               holder.textViewPriority.setBackgroundColor(Color.rgb(216, 129, 6));
               break;

               default:
                   holder.textViewPriority.setText(priority);
                   holder.textViewPriority.setBackgroundColor(Color.BLUE);
       }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewMessage, textViewPriority;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewPriority = itemView.findViewById(R.id.textViewPriority);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}