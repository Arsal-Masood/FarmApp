package com.example.farmapp.ui.view;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.farmapp.R;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    public ChatAdapter(@NonNull Context context, @NonNull List<ChatMessage> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item, parent, false);
        }

        ChatMessage chatMessage = getItem(position);

        TextView senderName = convertView.findViewById(R.id.senderName);
        TextView messageText = convertView.findViewById(R.id.messageText);

        senderName.setText(chatMessage.getSender());
        messageText.setText(chatMessage.getMessage());

        return convertView;
    }
}

