package com.kiegame.mobile.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.FragmentNetFeeBinding;
import com.kiegame.mobile.model.NetFeeModel;
import com.kiegame.mobile.repository.entity.receive.BannerEntity;
import com.kiegame.mobile.repository.entity.receive.UserInfoEntity;
import com.kiegame.mobile.ui.base.BaseFragment;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.utils.Toast;
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
    private PopupWindow pw;
    private LinearLayout list;
    private LayoutInflater inflater;

    @Override
    protected int onLayout() {
        return R.layout.fragment_net_fee;
    }

    @Override
    protected void onObject() {
        model = ViewModelProviders.of(this).get(NetFeeModel.class);
        binding.setModel(model);
        binding.setFragment(this);
        model.userSearch.observe(this, this::searchUserInfoList);
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
     * 搜索用户信息列表
     *
     * @param keywords 关键字
     */
    private void searchUserInfoList(String keywords) {
        if (Text.empty(keywords) && pw != null && pw.isShowing()) {
            pw.dismiss();
        } else {
            LiveData<List<UserInfoEntity>> data = model.searchUserInfos(keywords);
            if (!data.hasObservers()) {
                data.observe(this, this::searchUserInfosResult);
            }
        }
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
     * 查询用户数据返回处理
     *
     * @param data 数据对象
     */
    @SuppressLint("InflateParams")
    private void searchUserInfosResult(List<UserInfoEntity> data) {
        if (pw == null) {
            inflater = LayoutInflater.from(this.getContext());
            View view = inflater.inflate(R.layout.view_search_auto_fill, null, false);
            list = view.findViewById(R.id.ll_search_list);
            pw = new PopupWindow(this.getContext());
            pw.setContentView(view);
            pw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            pw.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.translucent)));
            pw.setOutsideTouchable(false);
            pw.setSplitTouchEnabled(true);
        }
        if (data != null && !data.isEmpty()) {
            list.removeAllViews();
            for (UserInfoEntity entity : data) {
                View view = inflater.inflate(R.layout.item_search_user_info, null, false);
                TextView tv = view.findViewById(R.id.tv_user_item);
                tv.setText(entity.getCustomerName());
                tv.setOnClickListener(v -> {
                    model.userSearch.setValue("");
                    hideInputMethod();
                    binding.etSearchInput.clearFocus();
                    Toast.show(entity.getCustomerName());
                });
                list.addView(view);
            }
            if (!pw.isShowing()) {
                pw.showAsDropDown(binding.llSearch);
            }
        } else {
            if (pw.isShowing()) {
                pw.dismiss();
            }
        }
    }

    /**
     * 隐藏输入法
     */
    private void hideInputMethod() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                View focus = activity.getCurrentFocus();
                if (focus != null) {
                    inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
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
