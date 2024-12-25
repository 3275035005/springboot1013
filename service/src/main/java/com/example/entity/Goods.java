package com.example.entity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.example.common.handler.ListHandler;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName(value = "goods", autoResultMap = true)
public class Goods extends Model<Goods> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 商品名称
      */
    private String name;

    /**
      * 商品描述
      */
    private String description;


    /**
      * 原价
      */
    private BigDecimal price;


    /**
      * 点赞数
      */
    private Integer praise;

    /**
      * 销量
      */
    private Integer sales;

    /**
     * 分类(1家具 2家电)
     */
    private String categoryId;


    @TableField(exist = false)
    private String categoryName;

    /**
      * 商品图片
      */
    private String imgs;

    /**
     * 服务描述
     */
    private String content;

    /**
     * 服务图片
     */
    private String imageList;

    @TableField(exist = false)
    private List<String> imageLists;

    /**
      * 创建时间
      */
    private String createTime;

    public void setImageList(String imageList) {

        if(StringUtils.isEmpty(imageList)){
            this.imageLists =  new ArrayList<>();
        }else{
            this.imageLists =  JSONArray.parseArray(imageList, String.class);
        }

    }


    public void setImageLists(List<String> imageLists) {
        this.imageList = JSON.toJSONString(imageLists);
    }
}
