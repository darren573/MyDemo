package com.darren.mydemo.utils;

import com.alibaba.fastjson.JSON;
import com.darren.mydemo.bean.ProvinceBean;
import com.darren.mydemo.common.BaseApplication;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/5/19.
 */

public class CityDataUtil {
    private static ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private static ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private static ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    public static ArrayList<ProvinceBean> getProvinceData() {
        if (options1Items.size() <= 0){
            initJsonData();
        }
        return options1Items;
    }

    public static ArrayList<ArrayList<String>> getCityData() {
        if (options2Items.size() <= 0){
            initJsonData();
        }
        return options2Items;
    }

    public static ArrayList<ArrayList<ArrayList<String>>> getAreData() {
        if (options3Items.size() <= 0){
            initJsonData();
        }
        return options3Items;
    }


    private static void initJsonData() {
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String jsonData = FileUtils.getJson(BaseApplication.getInstance(), "province.json");//获取assets目录下的json文件数据

        ArrayList<ProvinceBean> jsonBean = (ArrayList<ProvinceBean>) JSON.parseArray(jsonData, ProvinceBean.class);

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCity().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCity().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCity().get(c).getArea() == null
                        || jsonBean.get(i).getCity().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCity().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCity().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }
}
