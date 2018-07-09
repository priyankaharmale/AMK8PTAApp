package com.hnweb.amk8ptaapp.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hnweb.amk8ptaapp.R;
import com.hnweb.amk8ptaapp.bo.Messages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessagesAdaptor extends RecyclerView.Adapter<MessagesAdaptor.ViewHolder> {

    private Context context;
    private List<Messages> messagesList;
    private LayoutInflater inflater;


    public MessagesAdaptor(Context context, List<Messages> data) {
        this.context = context;
        this.messagesList = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MessagesAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = inflater.inflate(R.layout.adaptor_messages, parent, false);
        MessagesAdaptor.ViewHolder vh = new MessagesAdaptor.ViewHolder(rowView);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MessagesAdaptor.ViewHolder holder, final int i) {
        final Messages details = messagesList.get(i);

        Log.e("Data", details.toString());
        String messageId = messagesList.get(i).getMid();
        String messageTitle = messagesList.get(i).getMsg_title();
        String messageDesc = messagesList.get(i).getMsg_descp();
        String messageDateTime = messagesList.get(i).getUpdated_dt();

        if (messageTitle.equals("")) {
            holder.textViewMessageTitle.setText("No Name");
        } else {
            holder.textViewMessageTitle.setText(messagesList.get(i).getMsg_title());
        }

        if (messageDesc.equals("")) {
            holder.textViewMessageDescp.setText("No Description ");
        } else {
            holder.textViewMessageDescp.setText(messagesList.get(i).getMsg_descp());
        }
       /* SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            Date dtStartOK = format.parse(messageDateTime);

        } catch (ParseException e) {
            //Handle exception here, most of the time you will just log it.
            e.printStackTrace();
        }*/
        @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        Date date_format = null;
        try {
            date_format = inputFormat.parse(messageDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateFormat = outputFormat.format(date_format);
        Log.d("DateFormat", outputDateFormat);

        if (messageDateTime.equals("")) {
            holder.textViewMessageDate.setText(" - ");
        } else {
            holder.textViewMessageDate.setText(outputDateFormat);
        }


        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String time1 = sdf.format(date_format);
        if (messageDateTime.equals("")) {
            holder.textViewMessageTime.setText(" - ");
        } else {
            holder.textViewMessageTime.setText(time1);
        }

    }


    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMessageTitle, textViewMessageDescp, textViewMessageDate, textViewMessageTime;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewMessageTitle = itemView.findViewById(R.id.tv_message_title);
            textViewMessageDescp = itemView.findViewById(R.id.tv_full_message);

            textViewMessageDate = itemView.findViewById(R.id.tv_date);
            textViewMessageTime = itemView.findViewById(R.id.tv_time);

        }
    }
}
