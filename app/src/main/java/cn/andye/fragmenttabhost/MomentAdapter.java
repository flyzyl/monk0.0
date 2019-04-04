package cn.andye.fragmenttabhost;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.andye.fragmenttabhost.CustomTagHandler;

import cn.andye.fragmenttabhost.fra.Home1Fra;
import cn.andye.fragmenttabhost.model.Moment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

//数据与界面的连接器
public class MomentAdapter extends BaseAdapter {

    public static final int VIEW_HEADER = 0;
    public static final int VIEW_MOMENT = 1;

    float start;
    String[] co={"#FF77CCAA","#FF11AA33","#0000ff","#FACC17","#d48a71","#7af090", "#0eaf52",
            "#007ca5","#ff0000","#f0d794","#a0ee1e","#8d92a6","#ffda44","#795547","#c4bfb9",
            "#79c1da","#f3ccfa","#4b223d","#dee3e4","#e1c2fc","#3E3F3C","#ffe9e2","#C34F9A","#ffced2","#FFB6C1"};//如果颜色无法识别将画不出图形
    private ArrayList<Moment> mList;
    private Context mContext;
    private int mg,mto,mye,mbe,mnum;
    private View mView;
    private CustomTagHandler mTagHandler;
    private Home1Fra home;
    private String[] mwords;
    int n=0,i;

    public MomentAdapter(Context context,ArrayList<Moment> list,String[] wor,int num ,CustomTagHandler tagHandler) {
        mList = list;
        mContext = context;
        mTagHandler = tagHandler;
        mwords=wor;
        mnum=num;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_HEADER : VIEW_MOMENT;
    }

    @Override
    public int getCount() {
        // headerView
        return mList.size() + 1;
    }

    @Override
    public Object getItem(int position) {

        return mList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
    ViewHolder holder;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder=null;

            if (position == 0) {
                convertView = View.inflate(mContext, R.layout.item_header, null);
//                TextView word=(TextView)convertView.findViewById(R.id.headw);
//                word.setText(mwords[mnum]);//展示一句话
                PieChartView pieChartView = (PieChartView)convertView.findViewById(R.id.pie_ch);
                List<PieChartView.PieceDataHolder> pieceDataHolders = new ArrayList<>();
                pieceDataHolders.add(new PieChartView.PieceDataHolder(-90,1440,"#c4bfb9", ""));
                String[] arr=new String[50];
                n=0;
                for(i=0;i<3;i++){
                    for(int j=1;j<mList.get(i).mComment.size();j++){
                        String a=mList.get(i).mComment.get(j).mContent;
                        arr[n]=Home1Fra.getHome1Fra().transform(a);
                        n++;
                    }
                }
                for(i=0;i<25;i++){
                    if(arr[i]!=null) {
                        Log.d("kk", "onStart: " + arr[i]);
                        StringTokenizer st = new StringTokenizer(arr[i], ":");
                        String shour = st.nextToken();
                        String smin = st.nextToken();
                        String dur = st.nextToken();
                        String thing = st.nextToken();
                        int alahour = 0, alamin = 0,aladur=0;
                        if (!shour.equals("non")&!dur.equals("non")) {
                            alahour = Integer.valueOf(shour);
                            aladur=Integer.valueOf(dur);
                            if (!smin.equals("non")) {
                                alamin = Integer.valueOf(smin);
                            } else {
                                alamin = 0;
                            }
                            start=alahour*60*360/1440+alamin*360/1440-90;//java里面的运算，在运算过程中不能出现除不尽的情况，否则直接等于0
                            pieceDataHolders.add(new PieChartView.PieceDataHolder(start,aladur,co[i], thing));
                        }
                    }
                }
                Calendar c=Calendar.getInstance();
                int hour=c.get(Calendar.HOUR_OF_DAY);
                int min=c.get(Calendar.MINUTE);
                float now=hour*60*360/1440+min*360/1440-90;
                pieceDataHolders.add(new PieChartView.PieceDataHolder(now,3,"#ffffff", "now"));
                pieChartView.setData(pieceDataHolders);

            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_moment, null);
                holder = new ViewHolder();
                Log.d("hhh", "初始化="+position);
                holder.mCommentList = (LinearLayout) convertView.findViewById(R.id.comment_list);
                holder.mBtnInput = (TextView) convertView.findViewById(R.id.btn_input_comment);
                holder.mContent = (TextView) convertView.findViewById(R.id.content);
                Log.d("hhh", "我在执行 adapter.getview");
                convertView.setTag(holder);
            }
        if(position!=0){
        holder.mBtnInput.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View View) {
                mItemOnClickListener.itemOnClickListener(View,position);
                Log.d("hhh", "传入的position="+position);
            }
        });}

        //防止ListView的OnItemClick与item里面子view的点击发生冲突
        ((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        if (position == 0) {
        } else {
            Log.d("hhh", "传入的position="+position);
            int index = position - 1;
            ViewHolder holder = (ViewHolder) convertView.getTag();
//            holder.mContent.setText(mList.get(index).mContent);
                CommentFun.parseCommentList(mContext, mList.get(index).mComment, holder.mCommentList, holder.mBtnInput, holder.mContent, mTagHandler);
                //往commentFun中传入“上下文”，“某段评论数据”，“评论列表layout”，
                // “评论输入框textview”，taghandler返回数据
        }
        return convertView;
    }
    public interface ItemOnClickListener{
        //传递点击的view
        void itemOnClickListener(View view,int position);
    }
    private ItemOnClickListener mItemOnClickListener;

    public void setmItemOnClickListener(ItemOnClickListener listener){
        this.mItemOnClickListener = listener;
    }



    private static class ViewHolder {
        LinearLayout mCommentList;
        TextView mBtnInput;
        TextView mContent;
    }


}

