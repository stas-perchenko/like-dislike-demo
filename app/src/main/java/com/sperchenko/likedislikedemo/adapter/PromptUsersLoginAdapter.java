package com.sperchenko.likedislikedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sperchenko.likedislikedemo.R;
import com.sperchenko.likedislikedemo.images.IconLoader;
import com.sperchenko.likedislikedemo.model.Person;
import com.sperchenko.likedislikedemo.utils.FileUtils;

import java.util.List;

/**
 * Created by stanislav.perchenko on 11/30/2015.
 */
public class PromptUsersLoginAdapter extends ArrayAdapter<Person> {

    LayoutInflater inflater;
    public PromptUsersLoginAdapter(Context context, List<Person> data) {
        super(context, R.layout.user_list_item, 0, data);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            row = inflater.inflate(R.layout.user_list_item, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Person p = getItem(position);
        IconLoader.loadIcon(FileUtils.getIconFileById(getContext(), p.getPhotoId()), holder.getIcon());
        holder.getText().setText(p.getDisplayedName());
        holder.getText().setText(p.getUserName());
        return row;
    }

    private class ViewHolder {
        private View base;
        private ImageView icon;
        private TextView text;
        private TextView subtext;

        public ViewHolder(View v) {
            base = v;
        }

        public ImageView getIcon() {
            if (icon == null) {
                icon = (ImageView) base.findViewById(R.id.icon);
            }
            return icon;
        }

        public TextView getText() {
            if (text == null) {
                text = (TextView) base.findViewById(R.id.text);
            }
            return text;
        }

        public TextView getSubtext() {
            if (subtext == null) {
                subtext = (TextView) base.findViewById(R.id.subtext);
            }
            return subtext;
        }
    }
}
