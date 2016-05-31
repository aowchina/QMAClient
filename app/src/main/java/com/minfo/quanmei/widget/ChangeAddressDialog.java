package com.minfo.quanmei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.City;
import com.minfo.quanmei.entity.Province;
import com.minfo.quanmei.widget.pickview.adapter.AbstractWheelTextAdapter;
import com.minfo.quanmei.widget.pickview.view.OnWheelChangedListener;
import com.minfo.quanmei.widget.pickview.view.OnWheelScrollListener;
import com.minfo.quanmei.widget.pickview.view.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 更改封面对话框
 *
 * @author ywl
 *
 */
public class ChangeAddressDialog extends Dialog implements View.OnClickListener {

    private WheelView wvProvince;
    private WheelView wvCitys;
    private View lyChangeAddress;
    private View lyChangeAddressChild;
    private TextView btnSure;
    private TextView btnCancel;

    private Context context;
    private JSONObject mJsonObj;
    private String[] mProvinceDatas;
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    private List<Province> arrProvinces = new ArrayList<>();
    private List<City> arrCitys = new ArrayList<>();
    private ProvinceTextAdapter provinceAdapter;
    private CityTextAdapter cityAdapter;

    private Province province;
    private City city;

    private String strProvince = "四川";
    private String strCity = "成都";
    private OnAddressCListener onAddressCListener;

    private int maxsize = 24;
    private int minsize = 14;

    public ChangeAddressDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_myinfo_changeaddress);

        readProvinces();
        wvProvince = (WheelView) findViewById(R.id.wv_address_province);
        wvCitys = (WheelView) findViewById(R.id.wv_address_city);
        lyChangeAddress = findViewById(R.id.ly_myinfo_changeaddress);
        lyChangeAddressChild = findViewById(R.id.ly_myinfo_changeaddress_child);
        btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

        lyChangeAddress.setOnClickListener(this);
        lyChangeAddressChild.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        province = arrProvinces.get(0);
        city = province.getCities().get(0);
        provinceAdapter = new ProvinceTextAdapter(context, arrProvinces, 0, maxsize, minsize);
        wvProvince.setVisibleItems(5);
        wvProvince.setViewAdapter(provinceAdapter);
        wvProvince.setCurrentItem(0);

        arrCitys = arrProvinces.get(0).getCities();
        cityAdapter = new CityTextAdapter(context, arrCitys, 0, maxsize, minsize);
        wvCitys.setVisibleItems(5);
        wvCitys.setViewAdapter(cityAdapter);
        wvCitys.setCurrentItem(0);

        wvProvince.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());

                province = arrProvinces.get(wheel.getCurrentItem());
                strProvince = currentText;
                setTextviewSize(currentText, provinceAdapter);
                String[] citys = mCitisDatasMap.get(currentText);
                arrCitys = province.getCities();
                city = arrCitys.get(0);
                cityAdapter = new CityTextAdapter(context, arrCitys, 0, maxsize, minsize);
                wvCitys.setVisibleItems(5);
                wvCitys.setViewAdapter(cityAdapter);
                wvCitys.setCurrentItem(0);
            }
        });

        wvProvince.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, provinceAdapter);
            }
        });

        wvCitys.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                strCity = currentText;
                city = arrCitys.get(wheel.getCurrentItem());
                setTextviewSize(currentText, cityAdapter);
            }
        });

        wvCitys.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, cityAdapter);
            }
        });
    }

    private class ProvinceTextAdapter extends AbstractWheelTextAdapter {
        List<Province> list;
        protected ProvinceTextAdapter(Context context, List<Province> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index).getName() + "";
        }
    }
    private class CityTextAdapter extends AbstractWheelTextAdapter {
        List<City> list;

        protected CityTextAdapter(Context context, List<City> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index).getName() + "";
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, AbstractWheelTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(24);
            } else {
                textvew.setTextSize(14);
            }
        }
    }

    public void setAddresskListener(OnAddressCListener onAddressCListener) {
        this.onAddressCListener = onAddressCListener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnSure) {
            if (onAddressCListener != null) {
                onAddressCListener.onClick(province, city);
            }
        } else if (v == btnCancel) {

        } else if (v == lyChangeAddressChild) {
            return;
        } else {
            dismiss();
        }
        dismiss();
    }

    /**
     * 回调接口
     *
     * @author Administrator
     *
     */
    public interface OnAddressCListener {
        public void onClick(Province province, City city);
    }

    /**
     * 初始化地点
     *
     */
    public void setAddress(int provinceIndex, int cityIndex) {
        if(arrProvinces.size()==0){
            readProvinces();
            province = arrProvinces.get(provinceIndex);
            city = province.getCities().get(cityIndex);
        }
    }

    /**
     * 读取省市数据
     */
    private void readProvinces() {
        StringBuffer sb = new StringBuffer();
        try {
            InputStream inputStream = context.getAssets().open("pc.txt");
            byte[] buf = new byte[1024];

            while ((inputStream.read(buf)) != -1) {
                sb.append(new String(buf));
                buf = new byte[1024];//重新生成，避免和上次读取的数据重复
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                Province province = new Province();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONArray cityArray = jsonObject.getJSONArray("city");
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                List<City> cityList = new ArrayList<>();
                for(int j = 0;j<cityArray.length();j++){
                    City city = new City();
                    JSONObject cityObject = cityArray.getJSONObject(j);
                    int cid = cityObject.getInt("id");
                    String cname = cityObject.getString("name");
                    city.setId(cid);
                    city.setName(cname);
                    cityList.add(city);
                }
                province.setId(id);
                province.setName(name);
                province.setCities(cityList);
                arrProvinces.add(province);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}