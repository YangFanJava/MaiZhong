package com.maizhong.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.maizhong.common.dto.CarBrandDTO;
import com.maizhong.common.dto.PageSearchParam;
import com.maizhong.common.enums.OperateEnum;
import com.maizhong.common.result.JsonResult;
import com.maizhong.common.result.PageResult;
import com.maizhong.common.utils.JsonUtils;
import com.maizhong.common.utils.SqlUtils;
import com.maizhong.dao.JedisClient;
import com.maizhong.mapper.TbCarBrandLineMapper;
import com.maizhong.pojo.TbCarBrand;
import com.maizhong.pojo.TbCarBrandLine;
import com.maizhong.pojo.TbCarBrandLineExample;
import com.maizhong.service.BrandLineService;
import com.maizhong.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by YangF on 2017/3/16.
 */
@Service
public class BrandLineServiceImpl implements BrandLineService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private TbCarBrandLineMapper tbCarBrandLineMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${CAR_SERIES}")
    private String CAR_SERIES;
    @Value("${CAR_SERIES_ID}")
    private String CAR_SERIES_ID;
    @Value("${CAR_SERIES_HOT}")
    private String CAR_SERIES_HOT;
    @Value("${CAR_BRAND_HOT}")
    private String CAR_BRAND_HOT;


    @Override
    public TbCarBrandLine getCarBrandLineByid(Long id) {
        return id == null ? null : tbCarBrandLineMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据品牌查询车型
     *
     * @param brandId
     * @return
     */
    @Override
    public JsonResult getCarBrandLineList(Long brandId) {
        if (brandId == null) return JsonResult.Error("请刷新后重试");
        TbCarBrandLineExample example = new TbCarBrandLineExample();
        example.createCriteria().andBrandIdEqualTo(brandId);
        List<TbCarBrandLine> list = tbCarBrandLineMapper.selectByExample(example);
        return JsonResult.OK(list);
    }


    @Override
    public JsonResult insertCarBrandLine(TbCarBrandLine tbCarBrandLine) {
        if (tbCarBrandLine == null || tbCarBrandLine.getBrandId() == null) {
            return JsonResult.Error("请刷新后重试");
        }
        if (tbCarBrandLine.getStatus() != null) {
            if (tbCarBrandLine.getStatus() == 1) {
                tbCarBrandLine.setDelflag(0);
            } else {
                tbCarBrandLine.setDelflag(1);
            }
        } else {
            tbCarBrandLine.setStatus(1);
            tbCarBrandLine.setDelflag(0);
        }

        //TODO 缓存处理 预留
        if (tbCarBrandLine.getShowFlag() != null && tbCarBrandLine.getShowFlag() == 1) {

        }

        tbCarBrandLineMapper.insertSelective(tbCarBrandLine);
        return JsonResult.OK("添加成功");
    }

    /**
     *
     * @param tbCarBrandLine
     * @return
     */
    @Override
    public JsonResult updateCarBrandLine(TbCarBrandLine tbCarBrandLine) {

        if (tbCarBrandLine == null || tbCarBrandLine.getId() == null || tbCarBrandLine.getBrandId() == null) {
            return JsonResult.Error("错误的数据");
        }
        if (tbCarBrandLine.getStatus() != null) {
            if (tbCarBrandLine.getStatus() == 1) {
                tbCarBrandLine.setDelflag(0);
            } else {
                tbCarBrandLine.setDelflag(1);
            }
        } else {
            tbCarBrandLine.setStatus(1);
            tbCarBrandLine.setDelflag(0);
        }

        //TODO 缓存处理 预留
        if (tbCarBrandLine.getShowFlag() != null && tbCarBrandLine.getShowFlag() == 1) {

        }
        tbCarBrandLineMapper.updateByPrimaryKeySelective(tbCarBrandLine);
        return JsonResult.OK("修改成功");

    }

    @Override
    public JsonResult deleteById(Long id) {
        if (id == null)
            return JsonResult.Error("请刷新后重试");
       int res= tbCarBrandLineMapper.deleteByPrimaryKey(id);
        if (res>0){
            return JsonResult.OK("删除成功");
        }
        return JsonResult.Error("请刷新后重试");

    }

    /**
     * 根据id获取车系
     *
     * @param id
     * @return
     */
    @Override
    public TbCarBrandLine getSeriesById(Long id) {
        TbCarBrandLine tbCarBrandLine = tbCarBrandLineMapper.selectByPrimaryKey(id);
        return tbCarBrandLine;
    }

    @Override
    public PageResult getSeriesList(PageSearchParam param, Long id) {
        PageHelper.startPage(param.getPageIndex(), param.getPageSize());
        TbCarBrandLineExample example = new TbCarBrandLineExample();
        TbCarBrandLineExample.Criteria criteria = example.createCriteria();
        if (param.getFiled("lineName") != null) {
            criteria.andLineNameLike(SqlUtils.getLikeSql(param.getFiled("lineName")));
        }
        criteria.andBrandIdEqualTo(id);
        criteria.andDelflagEqualTo(0);
        example.setOrderByClause("line_sequence ASC");
        List<TbCarBrandLine> list = tbCarBrandLineMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        return new PageResult(pageInfo);
    }

    @Override
    public OperateEnum updateSeries(TbCarBrandLine tbCarBrandLine) {
        int res = tbCarBrandLineMapper.updateByPrimaryKeySelective(tbCarBrandLine);
        if (res > 0) {
            return OperateEnum.SUCCESS;
        } else {
            return OperateEnum.FAILE;
        }
    }

    @Override
    public OperateEnum insertSeries(TbCarBrandLine tbCarBrandLine) {

        TbCarBrandLineExample tbCarBrandLineExample = new TbCarBrandLineExample();
        TbCarBrandLineExample.Criteria criteria = tbCarBrandLineExample.createCriteria();
        criteria.andLineNameEqualTo(tbCarBrandLine.getLineName());
        criteria.andDelflagEqualTo(0);
        List<TbCarBrandLine> tbCarBrandLines = tbCarBrandLineMapper.selectByExample(tbCarBrandLineExample);
        if (tbCarBrandLines.size() > 0) {
            return OperateEnum.NAME_REPEAT;
        }

        int res = tbCarBrandLineMapper.insertSelective(tbCarBrandLine);
        if (res > 0) {
            return OperateEnum.SUCCESS;
        } else {
            return OperateEnum.FAILE;
        }
    }

    @Override
    public OperateEnum updateRedis() {
        //先删除缓存
        try {
            jedisClient.del(CAR_SERIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jedisClient.del(CAR_SERIES_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jedisClient.del(CAR_SERIES_HOT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TbCarBrand> list = brandService.getCarBrandList();

        for (TbCarBrand brand : list) {
            TbCarBrandLineExample example = new TbCarBrandLineExample();
            TbCarBrandLineExample.Criteria criteria = example.createCriteria();
            criteria.andDelflagEqualTo(0);
            criteria.andBrandIdEqualTo(brand.getId());
            List<TbCarBrandLine> list2 = tbCarBrandLineMapper.selectByExample(example);
            //写入车系ID对应名称
            for (TbCarBrandLine brandLine : list2) {
                try {
                    jedisClient.hset(CAR_SERIES_ID, brandLine.getId() + "", brandLine.getLineName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //写入缓存
            try {
                String jsonStr = JsonUtils.objectToJson(list2);
                jedisClient.hset(CAR_SERIES, brand.getId() + "", jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //写入热门车系
        String jsonBrand = jedisClient.get(CAR_BRAND_HOT);
        List<CarBrandDTO> carBrandList = JsonUtils.jsonToList(jsonBrand, CarBrandDTO.class);
        //获取每个热门品牌的首个车系
        List<TbCarBrandLine> resList = Lists.newArrayList();
        for (CarBrandDTO tbCarBrand : carBrandList) {
            TbCarBrandLineExample example = new TbCarBrandLineExample();
            TbCarBrandLineExample.Criteria criteria = example.createCriteria();
            criteria.andBrandIdEqualTo(tbCarBrand.getId());
            criteria.andDelflagEqualTo(0);
            example.setOrderByClause("line_sequence ASC");
            List<TbCarBrandLine> listHot = tbCarBrandLineMapper.selectByExample(example);
            if (listHot != null && listHot.size() > 0) {
                TbCarBrandLine tbCarBrandLine = listHot.get(0);
                resList.add(tbCarBrandLine);
            }
        }
        //写入缓存
        try {
            String carSeriesHot = JsonUtils.objectToJson(resList);
            jedisClient.set(CAR_SERIES_HOT, carSeriesHot);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return OperateEnum.SUCCESS;
    }

    @Override
    public Long getBaseUrl(String id) {
        TbCarBrandLine tbCarBrandLine = getCarBrandLineByid(Long.valueOf(id));
        String baseUrl = null;
        Long brandId = null;
        if (tbCarBrandLine != null && tbCarBrandLine.getBrandId() != null) {
            brandId = tbCarBrandLine.getBrandId();
        }
        return brandId;
    }
}
