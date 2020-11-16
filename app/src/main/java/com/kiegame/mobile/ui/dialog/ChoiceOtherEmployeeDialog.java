package com.kiegame.mobile.ui.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.DialogChangeUserBinding;
import com.kiegame.mobile.repository.entity.receive.Employee;

import java.util.Arrays;
import java.util.List;

/**
 * Description: 选择其员工
 */
public class ChoiceOtherEmployeeDialog extends BaseBottomDialogFragment {

    private OnSelectedCallback onSelectedCallback;
    private List<Employee> data;

    public static ChoiceOtherEmployeeDialog getInstance(List<Employee> employees,OnSelectedCallback onSelectedCallback) {
        ChoiceOtherEmployeeDialog dialog = new ChoiceOtherEmployeeDialog();
        dialog.data = employees;
        dialog.onSelectedCallback = onSelectedCallback;
        return dialog;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_change_user;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        DialogChangeUserBinding binding = (DialogChangeUserBinding) rootBinding;

        binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvData.setAdapter(new BaseQuickAdapter<Employee, BaseViewHolder>(R.layout.item_change_user,data) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, Employee item) {
                helper.setText(R.id.tv_name, item.getEmpName());
                helper.itemView.setOnClickListener(v -> {
                    if (onSelectedCallback != null) {
                        onSelectedCallback.onSelected(item.getEmpId());
                    }
                    dismiss();
                });
            }
        });
    }

    public interface OnSelectedCallback {
        void onSelected(String employeeId);
    }
}
