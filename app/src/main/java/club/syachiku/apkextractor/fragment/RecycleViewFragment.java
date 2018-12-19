package club.syachiku.apkextractor.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.ArrayList;
import java.util.List;

import club.syachiku.apkextractor.R;

public class RecycleViewFragment extends Fragment{
    private RecyclerView recyclerView;
    private int type;
    private final int TYPE_USER = 0;
    private final int TYPE_SYSTEM = 1;
    private final int TYPE_ALL = 2;
    private List<PackageInfo> appList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appList = getContext().getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

        Bundle bundle = getArguments();
        type = bundle.getInt("type");
        List<PackageInfo> dataSource = initDataSource(type);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        recyclerView.setAdapter(new RecycleViewFragmentAdapter(
                dataSource,
                getContext().getPackageManager(),
                type
        ));
    }

    private List<PackageInfo> initDataSource(int type) {
        switch (type) {
            case TYPE_USER:
                List<PackageInfo> userApp = new ArrayList<>();
                for (PackageInfo packageInfo : appList) {
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        userApp.add(packageInfo);
                    }
                }
                return userApp;
            case TYPE_SYSTEM:
                List<PackageInfo> systemApp = new ArrayList<>();
                for (PackageInfo packageInfo : appList) {
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        systemApp.add(packageInfo);
                    }
                }
                return systemApp;
            case TYPE_ALL:
                return appList;
        }
        return appList;
    }
}
