package club.syachiku.apkextractor;

import android.util.Log;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        viewPager = findViewById(R.id.materialViewPager);
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
                String pageTitle;
                switch (position) {
                    case 0:
                        pageTitle = getResources().getString(R.string.user_app);
                        break;
                    case 1:
                        pageTitle = getResources().getString(R.string.system_app);
                        break;
                    case 2:
                        pageTitle = getResources().getString(R.string.all_app);
                        break;
                    default:
                        pageTitle = "";
                }
                return pageTitle;
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
