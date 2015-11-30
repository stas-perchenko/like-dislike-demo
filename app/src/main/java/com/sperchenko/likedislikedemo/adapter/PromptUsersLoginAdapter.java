package com.sperchenko.likedislikedemo.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.sperchenko.likedislikedemo.R;
import com.sperchenko.likedislikedemo.model.Person;

import java.util.List;

/**
 * Created by stanislav.perchenko on 11/30/2015.
 */
public class PromptUsersLoginAdapter extends ArrayAdapter<Person> {

    public PromptUsersLoginAdapter(Context context, List<Person> data) {
        super(context, R.layout.user_list_item, 0, data);
    }
}
