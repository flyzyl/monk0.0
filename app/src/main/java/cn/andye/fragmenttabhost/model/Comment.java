package cn.andye.fragmenttabhost.model;

/**
 * 评论对象
 */
public class Comment {
    public long mId;
    public String mContent; // 评论内容


    public Comment(long mId, String mContent) {
        this.mId = mId;
        this.mContent = mContent;
    }
}
