package com.ytj.ytjijkplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ytj.ytjijkplayer.R;
import com.ytj.ytjijkplayer.bean.VideoijkBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class ResolutionSelectedAdapter extends BaseAdapter {

    private Context mContext;
    private List<VideoijkBean> mVideoList;

    public ResolutionSelectedAdapter(Context context, List<VideoijkBean> videoList) {
        mContext = context;
        mVideoList = videoList;
    }

    @Override
    public int getCount() {
        return mVideoList.size();
    }

    @Override
    public VideoijkBean getItem(int position) {
        return mVideoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.ytj_player_item_resolution_layout, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.ytj_player_resolution_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        VideoijkBean videoijkBean = getItem(position);
        if (videoijkBean != null) {
            viewHolder.textView.setText(videoijkBean.getName());
            if (videoijkBean.isSelected()) {
                viewHolder.textView.setBackgroundDrawable(mContext.getResources()
                        .getDrawable(R.drawable.ytj_player_textview_shape_bg));
                viewHolder.textView.setTextColor(mContext.getResources()
                        .getColor(R.color.player_controller_text_view_btn));
            } else {
                viewHolder.textView.setBackgroundDrawable(null);
                viewHolder.textView.setTextColor(mContext.getResources()
                        .getColor(R.color.white));
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
    }
}
