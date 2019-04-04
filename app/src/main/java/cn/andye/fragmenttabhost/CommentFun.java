package cn.andye.fragmenttabhost;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.andye.fragmenttabhost.fra.Home1Fra;
import cn.andye.fragmenttabhost.model.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;


//该函数的主要功能是往layout里填充数据,实现界面中“添加”的功能
//还有弹出对话框，添加数据对话框，隐藏对话框等等
public class CommentFun {
    public static final int KEY_COMMENT_SOURCE_COMMENT_LIST = -200162;
    //以上表示定义并初始化一个静态常量 public是权限修饰符,final static 表示静态常量
    //在页面中显示传入的数据
    public static void parseCommentList(Context context, ArrayList<Comment> mCommentList,
         LinearLayout commentList, View btnComment,TextView btnContent,Html.TagHandler tagHandler) {
        //传入所有评论
        TextView textView;
        Comment comment,headlist;
        String content,head;

        //为“评论Textview”设置标签
        if (btnComment != null) {
            btnComment.setTag(KEY_COMMENT_SOURCE_COMMENT_LIST, mCommentList);
        }

        int i;
        //为一个moment填充评论
        for (i = 1; i < mCommentList.size(); i++) {
            Log.d("hhh", "parseCommentList: "+i);
            comment = mCommentList.get(i);
            textView = (TextView) commentList.getChildAt(i-1);// 获得某行视图
            //如果某行视图为空
            if (textView == null) {
                textView = (TextView) View.inflate(context, R.layout.view_comment_list_item, null);
                commentList.addView(textView);
                Log.d("hhh", "添加空行");
            }
            textView.setVisibility(View.VISIBLE);//让该textview可见
            content = String.format("<%s>%s</%s></html>",
                    CustomTagHandler.TAG_CONTENT, comment.mContent,
                    CustomTagHandler.TAG_CONTENT);

            // 解析标签,狗给textview填充内容
            textView.setText(Html.fromHtml(content, null, tagHandler));
            textView.setClickable(true);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setTag(KEY_COMMENT_SOURCE_COMMENT_LIST, mCommentList);
            textView.setTag(CustomTagHandler.KEY_CONTENT, comment);
        }
        //给每一个组加一个标题
        headlist=mCommentList.get(0);
        head = String.format("<%s>%s</%s></html>",
                CustomTagHandler.TAG_CONTENT, headlist.mContent,
                CustomTagHandler.TAG_CONTENT);
        btnContent.setText(Html.fromHtml(head, null, tagHandler));
//        for (; i <commentList.getChildCount(); i++) {
//            commentList.getChildAt(i).setVisibility(View.GONE);
//        }

        //如果评论数大于0，则评论列表可见，评论数小于0则不可见
        if (mCommentList.size() > 0) {
            commentList.setVisibility(View.VISIBLE);
        } else {
            commentList.setVisibility(View.GONE);
        }
    }


    //弹出评论对话框
    public static void inputComment(final Activity activity, final ListView listView,
                                    final View btnComment,final int position,
                                    final InputCommentListener listener) {
        final ArrayList<Comment> commentList = (ArrayList) btnComment.getTag(KEY_COMMENT_SOURCE_COMMENT_LIST);
        String hint = "添加";
        // 获取评论的位置,不要在CommentDialogListener.onShow()中获取，
        // 因为onShow在输入法弹出后才调用，此时btnComment所属的父布局可能已经被ListView回收
        final int[] coord = new int[2];//定义一个含有两个元素的数组
        if (listView != null) {
            btnComment.getLocationOnScreen(coord);// 获取评论在屏幕上的位置x,y
        }

        //“add”按钮监听
        showInputComment(activity, hint, new CommentDialogListener() {
            @Override
            //传入对话框，Editext里的文本，
            public void onClickPublish(final Dialog dialog, EditText input,
                                                final TextView btn) {
                final String content = input.getText().toString();
                //获得文本框中输入的文本
                if (content.trim().equals("")) {
                    Toast.makeText(activity, "添加内容不能为空", Toast.LENGTH_SHORT).show();
                    return;//评论为空时点击操作无效
                }
                SimpleDateFormat format=new SimpleDateFormat("HH:mm");//实例化
                Date curDate=new Date();
                String str=format.format(curDate)+"  ";
                Home1Fra.getHome1Fra().writeTxtToFile(str+content,"/sdcard/Aha/","log.txt");
                Home1Fra.getHome1Fra().addDB(content,position-1);
                String retu=Home1Fra.getHome1Fra().transform(content);
                StringTokenizer st=new StringTokenizer(retu,":");
                String shour=st.nextToken();
                String smin=st.nextToken();
                String dur=st.nextToken();
                String thin=st.nextToken();
                int alahour=0,alamin=0,aladur=0;
                if (!shour.equals("non")){
                    alahour=Integer.valueOf(shour);
                    if(!smin.equals("non")){
                       alamin=Integer.valueOf(smin);
                    }
                    if(!dur.equals("non")){
                        aladur=Integer.valueOf(dur);
                    }

                    Home1Fra.getHome1Fra().onTimeSet(alahour,alamin,aladur,thin);
                }
                btn.setClickable(false);
                Comment comment = new Comment(position*100, content);
                commentList.add(comment);
                if (listener != null) {
                    listener.onCommitComment();
                }
                dialog.dismiss();//对话框隐藏
                Toast.makeText(activity, "添加成功", Toast.LENGTH_SHORT).show();

            }

            /**
             * @param inputViewCoordinatesInScreen [left,top]
             */
            @Override
            public void onShow(int[] inputViewCoordinatesInScreen) {
                if (listView != null) {
                    // 点击某条评论则这条评论刚好在输入框上面，
                    // 点击评论按钮则输入框刚好挡住按钮
                    int span = btnComment.getId() == R.id.btn_input_comment ? 0 :
                            btnComment.getHeight();
                    listView.smoothScrollBy(coord[1] + span -
                            inputViewCoordinatesInScreen[1], 1000);
                }
            }

            @Override
            public void onDismiss() {
            }
        });

    }

    public static class InputCommentListener {
        //　评论成功时调用
        public void onCommitComment() {
        }
    }


    //定义对话框
    private static Dialog showInputComment(Activity activity, CharSequence hint,
                                           final CommentDialogListener listener) {
        final Dialog dialog = new Dialog(activity,
                android.R.style.Theme_Translucent_NoTitleBar);//对话框样式（内置）
        dialog.setContentView(R.layout.view_input_comment);//对话框界面
        dialog.findViewById(R.id.input_comment_dialog_container).setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();//点击其他部分则对话框退出
                        if (listener != null) {
                            listener.onDismiss();
                        }
                    }
                });

        final EditText input = (EditText) dialog.findViewById(R.id.input_content);
        input.setHint(hint);
        final TextView btn = (TextView) dialog.findViewById(R.id.btn_publish_comment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickPublish(dialog, input, btn);
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    int[] coord = new int[2];
                    dialog.findViewById(R.id.input_comment_container).
                            getLocationOnScreen(coord);
                    // 传入输入框距离屏幕顶部（不包括状态栏）的位置
                    listener.onShow(coord);
                }
            }
        }, 300);
        return dialog;
    }



    //回调接口
    public interface CommentDialogListener {
        //onShow在输入法弹出后才调用
        void onClickPublish(Dialog dialog, EditText input, TextView btn);
        //输入框距离屏幕顶部（不包括状态栏）的位置[left,top]
        void onShow(int[] inputViewCoordinatesOnScreen);
        void onDismiss();
    }

}
