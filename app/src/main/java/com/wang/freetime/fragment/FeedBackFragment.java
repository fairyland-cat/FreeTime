package com.wang.freetime.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wang.freetime.R;
import com.wang.freetime.model.FeedBack;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedBackFragment extends Fragment {

    private EditText mFeedBack,mEmail;
    private Button mCommit;
    private Context context;

    public FeedBackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_feed_back, container, false);
        mEmail= (EditText) view.findViewById(R.id.contact);
        mFeedBack= (EditText) view.findViewById(R.id.content);
        mCommit= (Button) view.findViewById(R.id.commit);
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFeedBack!=null&&mEmail!=null){
                    String content=mFeedBack.getText().toString();
                    String email=mEmail.getText().toString();
                    FeedBack feedback=new FeedBack();
                    feedback.setFeedback(content);
                    feedback.setEmail(email);
                    feedback.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null){
                                Toast.makeText(context, "已提交反馈", Toast.LENGTH_LONG).show();
                                mEmail.setText("");
                                mFeedBack.setText("");
                            }
                        }
                    });
                }else {
                    Toast.makeText(context, "请完善信息", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
