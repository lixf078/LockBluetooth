package in.srain.cube.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import in.srain.cube.views.scrolllayout.ScrollAbleFragment;

/**
 * Created by lixufeng on 16/5/26.
 */
public class CustomFragmentPagerAdapter <T extends Fragment> extends FragmentStatePagerAdapter {

    private List<T> fragmentList;
    private List<String> titleList;

    public CustomFragmentPagerAdapter(FragmentManager fm, List<T> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public T getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
