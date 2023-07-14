package chats.com.chats.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import chats.com.chats.ModelClass.Messages;
import chats.com.chats.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static chats.com.chats.Activity.ChatActivity.rImage;
import static chats.com.chats.Activity.ChatActivity.sImage;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVED=2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        if(viewType==ITEM_SEND){

            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
            return new SenderViewHolder(view);

        }
        else {

            View view= LayoutInflater.from(context).inflate(R.layout.receiver_layout_item,parent,false);
            return new ReceiverViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {

        Messages messages=messagesArrayList.get(position);

        if(holder.getClass()==SenderViewHolder.class){

            SenderViewHolder viewHolder=(SenderViewHolder) holder;
            viewHolder.txtMessage.setText(messages.getMessage());

            Picasso.get().load(sImage).into(viewHolder.circleImageView);
        }
        else {

            ReceiverViewHolder viewHolder=(ReceiverViewHolder ) holder;
            viewHolder.txtMessage.setText(messages.getMessage());

            Picasso.get().load(rImage).into(viewHolder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return  ITEM_SEND;
        }
        else {
            return ITEM_RECEIVED;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtMessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.profile_image);
            txtMessage=itemView.findViewById(R.id.txtMessages);

        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtMessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.profile_image);
            txtMessage=itemView.findViewById(R.id.txtMessages);

        }
    }
}
