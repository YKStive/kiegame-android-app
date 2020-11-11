package com.kiegame.mobile.ui.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.DialogChangeUserBinding;

import java.util.Arrays;

/**
 * 改变店员
 */
public class ChangeUserDialog extends BaseBottomDialogFragment {

    private DialogChangeUserBinding binding;
    private OnSelectedCallback onSelectedCallback;

    public static ChangeUserDialog getInstance(OnSelectedCallback onSelectedCallback){
        ChangeUserDialog dialog = new ChangeUserDialog();
        dialog.onSelectedCallback = onSelectedCallback;
        return dialog;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_change_user;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        binding = (DialogChangeUserBinding) rootBinding;
        String[] userList = {
                "张三","李四","王二麻子"
        };
        binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvData.setAdapter(new BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_change_user, Arrays.asList(userList)) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_name,item);
                helper.itemView.setOnClickListener(v -> {
                    if (onSelectedCallback!=null){
                        onSelectedCallback.onSelected(item);
                    }
                    dismiss();
                });
            }
        });
    }

    public interface OnSelectedCallback{
        void onSelected(String user);
    }

}
