package com.gitxpresso.launcherhijack;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class AppAdapter extends BaseAdapter {
    private List<ResolveInfo> mListAppInfo;
    private LayoutInflater inflater;
    private PackageManager pm;

    public AppAdapter(Context context, List<ResolveInfo> apps, PackageManager pm) {
        this.mListAppInfo = apps;
        this.inflater = LayoutInflater.from(context);
        this.pm = pm;
    }

    @Override
    public int getCount() { return mListAppInfo.size(); }
    @Override
    public Object getItem(int position) { return mListAppInfo.get(position); }
    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.layout_appinfo, parent, false);
        }
        ResolveInfo info = mListAppInfo.get(position);
        ImageView ivAppIcon = v.findViewById(R.id.ivIcon);
        TextView tvAppName = v.findViewById(R.id.tvName);
        TextView tvPkgName = v.findViewById(R.id.tvPack);

        ivAppIcon.setImageDrawable(info.loadIcon(pm));
        tvAppName.setText(info.loadLabel(pm));
        tvPkgName.setText(info.activityInfo.packageName);

        return v;
    }
}