package club.syachiku.apkextractor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import club.syachiku.apkextractor.fragment.RecycleViewFragment;

public class MainActivity extends AppCompatActivity {
    private MaterialViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        setTitle("");
        viewPager = findViewById(R.id.materialViewPager);
        final Toolbar toolbar = viewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        viewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", position);
                RecycleViewFragment recycleViewFragment = new RecycleViewFragment();
                recycleViewFragment.setArguments(bundle);
                return recycleViewFragment;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "User APP";
                    case 1:
                        return "System APP";
                    case 2:
                        return "All APP";
                }
                return "";
            }
        });
        viewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.colorCyan,
                                ""
                        );
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.colorSkyBlue,
                                ""
                        );
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.colorPrimary,
                                ""
                        );
                    default:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.colorCyan,
                                ""
                        );

                }
            }
        });
        viewPager.getViewPager().setOffscreenPageLimit(viewPager.getViewPager().getAdapter().getCount());
        viewPager.getPagerTitleStrip().setViewPager(viewPager.getViewPager());
    }
}
