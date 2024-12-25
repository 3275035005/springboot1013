package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.util.List;

@Data
@TableName("t_order")
public class Order extends Model<Order> {
    /**
      * 主键
      */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
      * 订单编号
      */
    private String orderNo;

    /**
      * 总价
      */
    private BigDecimal totalPrice;

    /**
      * 下单人id
      */
    private Long userId;

    /**
      * 联系人
      */
    private String linkUser;

    /**
      * 联系电话
      */
    private String linkPhone;

    /**
      * 维修地址
      */
    private String linkAddress;

    /**
      * 状态 0待付款 1派遣中 2维修中  3维修完成 4待评价 5订单完成 6已取消
      */
    private String state;

    /**
      * 创建时间
      */
    private String createTime;


    /**
     * 维修工id
     */
    private Long maintainerId;

    /**
     * 维修项目id
     */
    private Long goodsId;

    /**
     * 维修项目信息
     */
    @TableField(exist = false)
    private Goods goods;

    /**
     * 维修项目名称
     */
    @TableField(exist = false)
    private String goodsName;

    /**
     * 下单用户昵称
     */
    @TableField(exist = false)
    private String userNickName;

    /**
     * 维修工昵称
     */
    @TableField(exist = false)
    private String maintainerNickName;


    /**
     * 评价类型 1好评 2中评 3差评
     */
    @TableField(exist = false)
    private String messageState;
}
