package club.syachiku.apkextractor.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import club.syachiku.apkextractor.R;

public class RecycleViewFragmentAdapter extends RecyclerView.Adapter{
    private List<PackageInfo> dataSource;
    private PackageManager packageManager;
    private int type;
    private Context context;
//    private final String BACKUP_PATH = "/sdcard/Download";
    private final String BACKUP_PATH = Environment.getExternalStorageDirectory().getPath() + "/Download/";
    private final int TYPE_USER = 0;
    private final int TYPE_SYSTEM = 1;
    private final int TYPE_ALL = 2;

    public RecycleViewFragmentAdapter(
            Context context,
            List<PackageInfo> dataSource,
            int type
    ) {
        this.context = context;
        this.dataSource = dataSource;
        this.packageManager = context.getPackageManager();
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card, parent, false)) {};
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        TextView titleView = holder.itemView.findViewById(R.id.itemTitle);
        TextView packageIdView = holder.itemView.findViewById(R.id.itemPackageId);
        ImageView iconView = holder.itemView.findViewById(R.id.itemIcon);


        final PackageInfo packageInfo = dataSource.get(position);
        final String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
        final String packageName = packageInfo.packageName;
        Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);

        titleView.setText(appName);
        packageIdView.setText(packageName);
        iconView.setImageDrawable(icon);
        CardView cardView = holder.itemView.findViewById(R.id.card_view);

        switch (type) {
            case TYPE_USER:
                cardView.setForeground(context.getResources().getDrawable(R.drawable.card_foreground_cyan));
                break;
            case TYPE_SYSTEM:
                cardView.setForeground(context.getResources().getDrawable(R.drawable.card_foreground));
                break;
            case TYPE_ALL:
                cardView.setForeground(context.getResources().getDrawable(R.drawable.card_foreground_primary));
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
                builder.setTitle(R.string.dialog_title)
                        .setMessage(appName + "\n" + packageName)
                        .setPositiveButton(R.string.positive_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String dest = doCopy(packageInfo, appName);
                                showSnackBar(
                                        context.getResources().getString(R.string.snackbar_msg) + dest,
                                        holder.itemView,
                                        new SnackBarClickListener() {
                                            @Override
                                            public void onClick(@Nullable View v) {
                                                share(dest, context.getResources().getString(R.string.share_msg));
                                            }
                                        }
                                );
                            }
                        })
                        .setNegativeButton(R.string.negative_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    private String doCopy(PackageInfo packageInfo, String appName) {
        String source = packageInfo.applicationInfo.sourceDir;
        String dest = BACKUP_PATH + appName + ".apk";
        try {
            if (!new File(BACKUP_PATH).exists()) {
                new File(BACKUP_PATH).mkdir();
            }
            File destFile = new File(dest);
            if (destFile.exists()) {
                destFile.delete();
            }
            destFile.createNewFile();

            FileInputStream in = new FileInputStream(new File(source));
            FileOutputStream out = new FileOutputStream(destFile);
            FileChannel inC = in.getChannel();
            FileChannel outC = out.getChannel();
            int length;
            while (true) {
                if (inC.position() == inC.size()) {
                    inC.close();
                    outC.close();
                    break;
                }
                if ((inC.size() - inC.position()) < 1024 * 1024) {
                    length = (int) (inC.size() - inC.position());
                } else {
                    length = 1024 * 1024;
                }
                inC.transferTo(inC.position(), length, outC);
                inC.position(inC.position() + length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }

    interface SnackBarClickListener {
        void onClick(@Nullable View v);
    }

    private void showSnackBar(String message, View holderView, final SnackBarClickListener listener) {
        Snackbar.make(holderView, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(v);
                    }
                }).show();
    }

    private void share(String filePath, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        intent.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                        context,
                        context.getApplicationContext().getPackageName() + ".club.syachiku.apkextractor.provider",
                        new File(filePath)
                )
        );
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("application/octet-stream");
        Intent chooser = Intent.createChooser(intent, title);
        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooser);
    }
}
