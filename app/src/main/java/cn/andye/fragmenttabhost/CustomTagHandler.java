package cn.andye.fragmenttabhost;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import cn.andye.fragmenttabhost.model.Comment;


import org.xml.sax.XMLReader;

import java.util.HashMap;



/**
 * 显示评论的自定义Html标签解析器（专业）
 * 给评论添加样式，通过继承spanded函数使文本能够被点击
 * 返回函数接口的作用
 */
public class CustomTagHandler implements Html.TagHandler{
    //自定义标签
    public static final String TAG_COMMENTATOR = "commentator"; // 评论者
    public static final String TAG_RECEIVER = "receiver"; // 评论接收者，即对谁评论
    public static final String TAG_CONTENT = "content"; // 评论内容
    public static final int KEY_CONTENT = -2016;
    public static final int KEY_COMMENTATOR_START = 1;
    public static final int KEY_RECEIVER_START = 11;
    public static final int KEY_CONTENT_START = 21;

    private HashMap<Integer, Integer> mMaps = new HashMap<Integer, Integer>();
    private TextView Input;

    private ClickableSpan mCommentatorSpan, mReceiverSpan, mContentSpan;//局部文字可点击方法

    private Context mContext;

    public CustomTagHandler(final Context context,
                            final OnCommentClickListener listener){
        mContext = context;
        //评论内容
        mContentSpan = new BaseClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (listener != null) {
                    Comment comment=(Comment)widget.getTag(KEY_CONTENT);
                    listener.onContentClick(widget,comment);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(0xff000000);//黑色
                ds.setUnderlineText(false);
            }
        };
    }

    /**
     * 解析自定义标签
     */
    @Override
    public void handleTag(boolean opening, String tag, final Editable output,
     XMLReader xmlReader) {
        if (!tag.toLowerCase().equals(TAG_COMMENTATOR) && !tag.toLowerCase()
            .equals(TAG_RECEIVER)&& !tag.toLowerCase().equals(TAG_CONTENT)) {
            return;
        }
        if (opening) {  //开始标签
            // 记录标签内容的起始索引
            int mStart = output.length();
            if (tag.toLowerCase().equals(TAG_COMMENTATOR)) {
                mMaps.put(KEY_COMMENTATOR_START, mStart);
            } else if (tag.toLowerCase().equals(TAG_RECEIVER)) {
                mMaps.put(KEY_RECEIVER_START, mStart);
            } else if (tag.toLowerCase().equals(TAG_CONTENT)) {
                mMaps.put(KEY_CONTENT_START, mStart);
            }
        } else { // 结束标签
            int mEnd = output.length(); //标签内容的结束索引
            if (tag.toLowerCase().equals(TAG_COMMENTATOR)) {
                int mStart = mMaps.get(KEY_COMMENTATOR_START);
                output.setSpan(new TextAppearanceSpan(mContext, R.style.Comment),
                        mStart, mEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                output.setSpan(mCommentatorSpan, mStart, mEnd, 
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else if (tag.toLowerCase().equals(TAG_RECEIVER)) {
                int mStart = mMaps.get(KEY_RECEIVER_START);
                output.setSpan(new TextAppearanceSpan(mContext, R.style.Comment),
                        mStart, mEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                output.setSpan(mReceiverSpan, mStart, mEnd,
                 Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else if (tag.toLowerCase().equals(TAG_CONTENT)) {
                int mStart = mMaps.get(KEY_CONTENT_START);
                output.setSpan(new TextAppearanceSpan(mContext, R.style.Comment),
                        mStart, mEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                output.setSpan(mContentSpan, mStart, mEnd, 
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    //引入文字点击函数
    abstract class BaseClickableSpan extends ClickableSpan {
        public BaseClickableSpan() {}

        @Override
        public abstract void onClick(View widget);

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor); //默认颜色
            ds.setUnderlineText(false);
        }
    }

    //返回函数接口（评论点击事件）

    public interface OnCommentClickListener {
        //　点击评论内容
        void onContentClick(View view,Comment comment);
    }


}

