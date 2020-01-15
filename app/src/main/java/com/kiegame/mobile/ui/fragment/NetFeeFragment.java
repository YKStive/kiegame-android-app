package com.kiegame.mobile.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProviders;

import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentNetFeeBinding;
import com.kiegame.mobile.model.NetFeeModel;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/7.
 * Description: 网费充值
 */
public class NetFeeFragment extends BaseFragment<FragmentNetFeeBinding> {

    private NetFeeModel model;

    @Override
    protected int onLayout() {
        return R.layout.fragment_net_fee;
    }

    @Override
    protected void onObject() {
        model = ViewModelProviders.of(this).get(NetFeeModel.class);
        binding.setModel(model);
    }

    @Override
    protected void onView() {
        List<Drawable> drawables = new ArrayList<>();
        drawables.add(getResources().getDrawable(R.drawable.ic_vip_box));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher_background));
        drawables.add(getResources().getDrawable(R.drawable.ic_confirm_order_background));
        binding.bnBanner.setImages(drawables);
        binding.bnBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setImageDrawable((Drawable) path);
            }
        });
        binding.bnBanner.start();
    }

    @Override
    protected void onData() {

    }
}
