package com.kiegame.mobile.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentNetFeeBinding;
import com.kiegame.mobile.model.NetFeeModel;
import com.kiegame.mobile.repository.entity.receive.BannerEntity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

/**
 * Created by: var_rain.
 * Created date: 2020/1/7.
 * Description: 网费充值
 */
public class NetFeeFragment extends BaseFragment<FragmentNetFeeBinding> {

    private NetFeeModel model;
    private TextView moneyBtn;

    @Override
    protected int onLayout() {
        return R.layout.fragment_net_fee;
    }

    @Override
    protected void onObject() {
        model = ViewModelProviders.of(this).get(NetFeeModel.class);
        binding.setModel(model);
        binding.setFragment(this);
    }

    @Override
    protected void onView() {
        binding.bnBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(imageView).load(((BannerEntity) path).getBannerUrl()).into(imageView);
            }
        });
    }

    @Override
    protected void onData() {
        model.queryBanner().observe(this, this::queryBannerResult);
    }

    /**
     * banner数据处理
     *
     * @param data 数据对象
     */
    private void queryBannerResult(List<BannerEntity> data) {
        binding.bnBanner.setImages(data);
        binding.bnBanner.start();
    }

    /**
     * 删除会员
     */
    public void deleteVipInfo() {

    }

    /**
     * 充值金额
     *
     * @param view  选择金额按钮
     * @param money 金额数量
     */
    public void recharge(View view, int money) {
        if (this.moneyBtn != null) {
            this.moneyBtn.setBackgroundResource(R.drawable.shape_net_fee_none_border);
        }
        this.moneyBtn = (TextView) view;
        this.moneyBtn.setBackgroundResource(R.drawable.shape_net_fee_press_border);
        this.model.recharge.setValue(String.format("%s.00", money));
    }
}
