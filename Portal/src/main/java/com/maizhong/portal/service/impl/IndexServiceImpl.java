package com.maizhong.portal.service.impl;


import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.maizhong.common.result.JsonResult;
import com.maizhong.common.utils.HttpClientUtil;
import com.maizhong.common.utils.JsonUtils;
import com.maizhong.pojo.TbAdvert;
import com.maizhong.portal.service.IndexService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页服务接口实现
 * Created by Xushd on 2017/3/13.
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Value("${REST_URL}")
    private String REST_URL;

    @Value("${AD_INTERFACE}")
    private String AD_INTERFACE;

    @Value("${CAR_BRAND}")
    private String CAR_BRAND;

    @Value("${CAR_TYPE}")
    private String CAR_TYPE;

    /**
     * 获取
     * @param advertType
     * @return
     */
    @Override
    public String getAdvert(int advertType) {

        //调用服务层服务
        String result = HttpClientUtil.doGet(REST_URL+AD_INTERFACE+advertType);
        List<Map<String, String>> resultList = JsonUtils.jsonResultToList(result);
        List<Map> list = new ArrayList<>();
        for (Map<String, String> resultMap : resultList) {
            Map<String,String> map = new HashMap<>();
            map.put("img",resultMap.get("advertImg"));
            map.put("url",resultMap.get("advertUrl"));
            list.add(map);
        }
        return JsonUtils.objectToJson(list);

    }


    /**
     * 获取汽车品牌
     * @return
     */
    @Override
    public List<Map> getCarBrand() {

        //调用服务层服务
        String result = HttpClientUtil.doGet(REST_URL+CAR_BRAND);

        List<Map<String, String>> resultList = JsonUtils.jsonResultToList(result);
        List<Map> list = new ArrayList<>();
        for (Map<String, String> resultMap : resultList) {
            Map<String,String> map = new HashMap<>();
            map.put("img",resultMap.get("brandImg"));
            map.put("id",resultMap.get("brandId"));
            map.put("name",resultMap.get("brandName"));
            list.add(map);
        }
        return list;
    }

    /**
     * 获取车型
     * @return
     */
    @Override
    public List<Map> getCarType() {

        //调用服务层服务
        String result = HttpClientUtil.doGet(REST_URL+CAR_TYPE);

        List<Map<String, String>> resultList = JsonUtils.jsonResultToList(result);
        List<Map> list = new ArrayList<>();
        for (Map<String, String> resultMap : resultList) {
            Map<String,String> map = new HashMap<>();
            map.put("img",resultMap.get("typeImg"));
            map.put("id",resultMap.get("typeId"));
            map.put("name",resultMap.get("typeName"));
            list.add(map);
        }
        return list;
    }
}