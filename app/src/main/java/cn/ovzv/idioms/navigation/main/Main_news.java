package cn.ovzv.idioms.navigation.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.LCCloud;
import cn.leancloud.LCUser;
import cn.ovzv.idioms.Login;
import cn.ovzv.idioms.R;
import cn.ovzv.idioms.adapter.CommentExpandAdapter;
import cn.ovzv.idioms.api.LeancloudApi;
import cn.ovzv.idioms.bean.CommentBean;
import cn.ovzv.idioms.bean.CommentDetailBean;
import cn.ovzv.idioms.bean.ReplyDetailBean;
import cn.ovzv.idioms.help.CommentExpandableListView;
import cn.ovzv.idioms.help.GetHttpBitmap;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Main_news extends AppCompatActivity {
    private ImageView mImageView;
    private TextView mTextView;

    private TextView News,Text,Time;
    private ImageView Image;


    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;
    LCUser currentUser = LCUser.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_news);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        initView();
        News = (TextView) findViewById(R.id.news);
        Time = (TextView) findViewById(R.id.time);
        Image = (ImageView) findViewById(R.id.image);
        Text = (TextView) findViewById(R.id.text);

        // 构建传递给服务端的参数字典
        Map<String, Object> NewsData = new HashMap<String, Object>();

        // 调用指定名称的云函数 averageStars，并且传递参数（默认不使用缓存）
        LCCloud.callFunctionInBackground("News_Get", NewsData).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
                System.out.println(object);
                JSONObject json = (JSONObject) JSONObject.toJSON(object);

                JSONArray DataJSONArray = json.getJSONArray("data");
                System.out.println(DataJSONArray.getJSONObject(0));
                News.setText(DataJSONArray.getJSONObject(0).getString("Title"));
                Text.setText(DataJSONArray.getJSONObject(0).getString("Text"));
                Time.setText(DataJSONArray.getJSONObject(0).getString("Time"));
                Image.setImageBitmap(GetHttpBitmap.getHttpBitmap(DataJSONArray.getJSONObject(0).getString("Image")));
                Map<String, Object> CommentData = new HashMap<String, Object>();
                try {
                    CommentData.put("NewsID", DataJSONArray.getJSONObject(0).getString("NewsID"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                // 调用指定名称的云函数 Comment_Get，并且传递参数（默认不使用缓存）
                LCCloud.callFunctionInBackground("Comment_Get", CommentData).subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onNext(Object object) {
                        // succeed.
                        System.out.println(object);
                        JSONObject Commentjson = (JSONObject) JSONObject.toJSON(object);
                        String jsonStr = JSONObject.toJSONString(Commentjson);
                        commentsList = generateTestData(jsonStr);
                        initExpandableListView(commentsList);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        // failed.
                    }

                    @Override
                    public void onComplete() {




                    }
                });
            }
            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {

            }
        });


    }

    /**
     * 设置view
     * */
    public void initView(){
        mTextView = (TextView) findViewById(R.id.title);
        mTextView.setText("文章详情");

        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCommentDialog();

            }
        });


        mImageView = (ImageView)findViewById(R.id.back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(Main_news.this,"点击了回复", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }

    /**
     * func:生成测试数据
     * @return 评论数据
     */
    private List<CommentDetailBean> generateTestData(String Json){
        Gson gson = new Gson();
        commentBean = gson.fromJson(Json, CommentBean.class);
        List<CommentDetailBean> commentList = commentBean.getData().getList();
        return commentList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * func:弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.fragment_main_news_comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();

                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();


                    CommentDetailBean detailBean = new CommentDetailBean(currentUser.getUsername(), commentContent,"刚刚");

                    LeancloudApi.Comment_save(commentsList.get(0).getNewsId(),currentUser.getUsername(),commentContent);

                    adapter.addTheCommentData(detailBean);


                    Toast.makeText(Main_news.this,"评论成功",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(Main_news.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.fragment_main_news_comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");

        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean(currentUser.getUsername(),replyContent);

                    LeancloudApi.Commentreply_save(commentsList.get(position).getId(),currentUser.getUsername(),replyContent);

                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    Toast.makeText(Main_news.this,"回复成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Main_news.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }
}