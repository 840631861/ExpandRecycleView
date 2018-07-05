package com.example.xiebin.expandrecycleview;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author xiebin
 */
public class MainActivity extends AppCompatActivity implements HomeCategoriesAdapter.AllCategoriesOnClickListener {

    private RecyclerView rvExpand;
    private MyDataBean myDataBean;
    private List<MyDataBean.DataBean.CategoriesBean> categories;
    private List<MyDataBean.DataBean.CategoriesBean.SubCategoriesBean> subCategoriesBeansListShow = new ArrayList<>();
    private HomeCategoriesAdapter homeCategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    private void initView() {
        rvExpand = (RecyclerView) findViewById(R.id.rv_expand);
    }

    /**
     * 整理数据
     */
    private void initData() {
        String json = new String(getAssertsFile(this, "data.json"));
        Gson gson = new Gson();
        myDataBean = gson.fromJson(json, MyDataBean.class);
        categories = myDataBean.getData().getCategories();
        if (subCategoriesBeansListShow.size() == 0) {
            int size = categories.size();
            for (int i = 0; i < size; i++) {
                MyDataBean.DataBean.CategoriesBean categoriesBean = categories.get(i);
                //先把每一个数据都放到一个大集合里面
                MyDataBean.DataBean.CategoriesBean.SubCategoriesBean subCategoriesBean = new MyDataBean.DataBean.CategoriesBean.SubCategoriesBean();
                subCategoriesBean.setName(categoriesBean.getName());
                subCategoriesBean.setTitle(true);
                subCategoriesBean.setTitleOldListPos(i);

                MyDataBean.DataBean.CategoriesBean.SubCategoriesBean subCategoriesBean2 = new MyDataBean.DataBean.CategoriesBean.SubCategoriesBean();
                subCategoriesBean2.setName(categoriesBean.getIntroduce());
                subCategoriesBean2.setChildTitle(true);

                categoriesBean.getSub_categories().add(0, subCategoriesBean2);
                subCategoriesBeansListShow.add(subCategoriesBean);
            }
            rvExpand.setLayoutManager(new LinearLayoutManager(this));
            homeCategoriesAdapter = new HomeCategoriesAdapter(this, categories, subCategoriesBeansListShow, this);
        }
        rvExpand.setAdapter(homeCategoriesAdapter);
        //以下是对布局进行控制
        final GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (homeCategoriesAdapter.getItemViewType(position) == 1) {
                    return 3;
                } else if (homeCategoriesAdapter.getItemViewType(position) == 2) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        rvExpand.setLayoutManager(manager);
    }


    public byte[] getAssertsFile(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            if (inputStream == null) {
                return null;
            }

            BufferedInputStream bis = null;
            int length;
            try {
                bis = new BufferedInputStream(inputStream);
                length = bis.available();
                byte[] data = new byte[length];
                bis.read(data);

                return data;
            } catch (IOException e) {

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {

                    }
                }
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void clickItem(MyDataBean.DataBean.CategoriesBean.SubCategoriesBean subCategoriesBean) {

    }
}
