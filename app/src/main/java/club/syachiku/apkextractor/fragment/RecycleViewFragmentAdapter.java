package club.syachiku.apkextractor.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import club.syachiku.apkextractor.R;

public class RecycleViewFragmentAdapter extends RecyclerView.Adapter{
    private List<PackageInfo> dataSource;
    private PackageManager packageManager;

    public RecycleViewFragmentAdapter(List<PackageInfo> dataSource, PackageManager packageManager) {
        this.dataSource = dataSource;
        this.packageManager = packageManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card, parent, false)) {};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView title = holder.itemView.findViewById(R.id.itemTitle);
        TextView packageId = holder.itemView.findViewById(R.id.itemPackageId);
        ImageView icon = holder.itemView.findViewById(R.id.itemIcon);
        PackageInfo packageInfo = dataSource.get(position);
        title.setText(packageInfo.applicationInfo.loadLabel(packageManager).toString());
        packageId.setText(packageInfo.packageName);
        icon.setImageDrawable(packageInfo.applicationInfo.loadIcon(packageManager));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}
