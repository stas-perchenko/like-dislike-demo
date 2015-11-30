package com.sperchenko.likedislikedemo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sperchenko.likedislikedemo.R;
import com.sperchenko.likedislikedemo.adapter.PromptUsersLoginAdapter;
import com.sperchenko.likedislikedemo.model.Person;
import com.sperchenko.likedislikedemo.tasks.ProgressAsyncTaskCompat;

import java.util.List;

/**
 * Created by Stas on 28.11.2015.
 */
public class SelectUserLoginActivity extends BaseActivity {
    public static final String RESULT_LOGGED_IN_USER_ID = "user_id";

    private EditText vEdtUsername;
    private EditText vEdtPassword;
    private Button vBtnUsers;
    private Button vBtnLogin;

    private ProgressAsyncTaskCompat<Void, Void, List<Person>> mSelectUsersTask;

    private List<Person> mAllowedPeople;
    private Person mSelectedPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        vEdtPassword = (EditText) findViewById(R.id.edt_user);
        vEdtPassword = (EditText) findViewById(R.id.edt_password);
        vBtnUsers = (Button) findViewById(R.id.btn_users);
        vBtnLogin = (Button) findViewById(R.id.btn_login);

        vBtnUsers.setEnabled(false);
        vBtnLogin.setEnabled(false);

        initForLayoutAdjust();

        vEdtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vEdtPassword.setVisibility(View.VISIBLE);
                vEdtUsername.setText("");
                mSelectedPerson = null;
                vBtnLogin.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        vBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vEdtUsername.getText().length() > 0) {
                    tryLogin();
                } else {
                    Toast.makeText(SelectUserLoginActivity.this, R.string.login_no_username, Toast.LENGTH_SHORT).show();
                }
            }
        });

        vBtnUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectUserDialog();
            }
        });

        mSelectUsersTask = new ProgressAsyncTaskCompat<Void, Void, List<Person>>(this, false, false){
            @Override
            protected List<Person> doInBackground(Void... params) {
                //TODO Select users
                return null;
            }

            @Override
            protected void onPostExecute(List<Person> result) {
                super.onPostExecute(result);
                mSelectUsersTask = null;
                mAllowedPeople = result;
                if (result.size() > 0) {
                    vBtnLogin.setEnabled(true);
                }
            }

            @Override
            protected void onCancelled(List<Person> persons) {
                super.onCancelled(persons);
                mSelectUsersTask = null;
            }
        };
        mSelectUsersTask.safeExecute();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mSelectUsersTask != null) {
            mSelectUsersTask.cancel(true);
            mSelectUsersTask = null;
        }
        setResult(RESULT_CANCELED);
    }

    private void initForLayoutAdjust() {
        ((ViewGroup) vEdtPassword.getParent()).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup parent = ((ViewGroup) vEdtPassword.getParent());
                int sidePad = getResources().getDimensionPixelSize(R.dimen.login_side_paddings);
                int btnMarg = getResources().getDimensionPixelSize(R.dimen.login_btn_sers_left_margin);

                int finalPad = btnMarg + vBtnUsers.getMeasuredWidth() * sidePad;
                int childrenW = parent.getMeasuredWidth() - 2 * (finalPad);

                vEdtPassword.getLayoutParams().width = childrenW;
                vEdtUsername.getLayoutParams().width = childrenW;
                vBtnUsers.getLayoutParams().width = childrenW;

                if (Build.VERSION.SDK_INT >= 16) {
                    parent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void startSelectUserDialog() {
        //TODO Implement this

        new AlertDialog.Builder(this).setAdapter(new PromptUsersLoginAdapter(this, mAllowedPeople), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedPerson = mAllowedPeople.get(which);
                vEdtPassword.setVisibility((TextUtils.isEmpty(mSelectedPerson.getPassword()) ? View.GONE : View.VISIBLE));
                vEdtPassword.setText("");
                vBtnLogin.setEnabled(true);
            }
        }).setTitle(R.string.login_user_prompt_dialog_title).setCancelable(true).create().show();

    }

    private void tryLogin() {
        //TODO Implement this

        if (mSelectedPerson != null) {
            if (!TextUtils.isEmpty(mSelectedPerson.getPassword()) && !mSelectedPerson.getPassword().equals(vEdtPassword.getText().toString())) {
                showLoginFailedDialog(R.string.login_worng_password);
                return;
            }
            finishWithId(mSelectedPerson.getId());
        } else {
            String uname = vEdtUsername.getText().toString();
            String upass = vEdtPassword.getText().toString();
            //--- Try select user by name ---
            List<Person> options = getHelper().getPreparedQueries().getPersonsByNameIfHaveRatings(uname);
            if (options.size() == 0) {
                showLoginFailedDialog(R.string.login_no_such_user);
                return;
            }
            Person p = options.get(0);

            //--- Check password ---
            if (!TextUtils.isEmpty(p.getPassword()) && !p.getPassword().equals(upass)) {
                showLoginFailedDialog(R.string.login_worng_password);
                return;
            }
            finishWithId(mSelectedPerson.getId());
        }

    }

    private void finishWithId(int userId) {
        setResult(RESULT_OK, new Intent().putExtra(RESULT_LOGGED_IN_USER_ID, userId));
        finish();
    }

    private void showLoginFailedDialog(int msgResId) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.login_failed_dialog_title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(msgResId)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
    }
}
