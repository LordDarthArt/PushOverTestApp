package tk.lorddarthart.pushovertestapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<PushMessage> pushlist;
    View view;
    ViewHolder viewHolder;

    public RecyclerViewAdapter(Context context, ArrayList<PushMessage> pushlist) {
        this.context = context;
        this.pushlist = pushlist;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.singleitem, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        PushMessage msg = pushlist.get(position);
        holder.tvPushUK.setText("Отправлено: " + msg.getPushkey());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date(msg.getPushtime());
        holder.tvPushTime.setText(sdf.format(date));
        holder.tvPushTitle.setText(String.valueOf(msg.getPushtitle()));
        holder.tvPushText.setText(String.valueOf(msg.getPushtext()));
    }

    @Override
    public int getItemCount() {
        return pushlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvPushTime, tvPushTitle, tvPushText, tvPushUK;

        public ViewHolder(View itemView) {
            super(itemView);

            tvPushTime = itemView.findViewById(R.id.tvPushTime);
            tvPushTitle = itemView.findViewById(R.id.tvPushTitle);
            tvPushText = itemView.findViewById(R.id.tvPushText);
            tvPushUK = itemView.findViewById(R.id.tvPushUK);
        }
    }
}