<template>
  <div>

    <div style="margin-top: 10px">
      <!--      商品-->
      <el-row :gutter="10">
        <!--    图片-->
        <el-col :span="8">
          <el-card>
            <div>
              <el-image :src="mainImg"></el-image>
            </div>
            <div
                style="margin-top: 20px; display: flex;  flex-wrap: wrap; flex-direction: row; justify-content: flex-start">
              <div style="flex: 1; padding: 5px" v-for="(item, index) in goods.imgs" :key="item">
                <el-image style="width: 60px; height: 60px;" fit="contain" :src="item"
                          @mouseover="show(item)"></el-image>
              </div>
            </div>
          </el-card>
        </el-col>

        <!--    商品细节-->
        <el-col :span="16">
          <el-card>
            <div style="text-align: right">
              <div style="display: inline-block; cursor: pointer;" @click="collect">
                <img :src="collectActiveIcon" alt="收藏" style="width: 15px; ">
                <span style="position: relative; bottom: 3px; font-size: 12px; left: 5px; color: #666">收藏</span>
              </div>
              <div style="display: inline-block; cursor: pointer; margin-left: 20px" @click="praise">
                <img :src="praiseActiveIcon" alt="点赞" style="width: 15px; ">
                <span style="position: relative; bottom: 2px; color: #666; margin-left: 5px">{{ goods.praise }}</span>
              </div>
            </div>
            <div style="padding: 10px 0; font-size: 20px; font-weight: bold">{{ goods.name }}</div>
            <div style="padding: 10px 0; color: #666; font-size: 14px">{{ goods.description }}</div>
            <div style="padding: 10px 0; color: #999;">
              <span>上架时间</span>
              <span style="margin-left: 20px; color: #666">{{ goods.createTime }}</span>
            </div>

            <div style="padding: 10px 0">
              <span style="color: #999">抢购价</span>
              <span style="margin-left: 10px; color: orangered; font-size: 20px">￥ {{ goods.price }}</span>
            </div>

            <div v-if="goods.discount < 1" style="padding: 10px 0; text-decoration:line-through; color: #999">
              原价：{{ goods.price }}
            </div>

            <div style="padding: 10px 0; color: #999;">
              <span>服务</span>
              <span style="margin-left: 20px; color: #666">
                24小时在线为您并提供维修服务。
              </span>
            </div>

            <div style="margin-top: 20px; padding: 10px 0">
              <el-button style="margin-left: 10px; background-color: orangered; color: white" @click="buyNow">立即预约
              </el-button>
            </div>

          </el-card>
        </el-col>

      </el-row>
      <!--      介绍-->
      <el-row>
        <el-col :span="24">
          <div style="margin-top: 10px; margin-bottom: 80px">
            <el-card>
              <div slot="header" class="clearfix">详情描述</div>
              <div style="margin: 20px auto;clear: both;line-height: 200%; padding: 0 30px 30px;">
                <div style="float: left;width: 72px;text-align: center;">
                  <span style="background: url(https://img.58cdn.com.cn/ds/detail/sheshimiaoshu.png) no-repeat;width: 72px;height: 72px;display: inline-block;background-size: 72px auto;"></span>
                  <span style="width: 72px;height: 72px;display: inline-block;">设施服务</span>
                </div>
                <div style="width: 770px;margin-left: 20px;display: inline-block;position: relative;">
                  <h3 style=" color: #333;font-size: 14px;font-weight: 700; font-family: PingFangSC-Semibold;">服务特色</h3>
                  <div class="labels">
                    <span style="color: #0e8ed8;font-size: 12px;padding: 3px 4px;background-color: #ebf5fd;margin-right: 10px;">24小时</span>
                    <span style="color: #0e8ed8;font-size: 12px;padding: 3px 4px;background-color: #ebf5fd;margin-right: 10px;">上门服务</span>
                  </div>
                </div>
              </div>
              <div style="width: 100%;border-top: 1px solid #f3f3f3;margin-top: 30px;"></div>
              <div style="margin: 20px auto;clear: both;line-height: 200%; padding: 0 30px 30px;">
                <div style="float: left;width: 72px;text-align: center;">
                  <span style="background: url(https://img.58cdn.com.cn/ds/detail/fuwumiaoshu.png) no-repeat;width: 72px;height: 72px;display: inline-block;background-size: 72px auto;"></span>
                  <span style="width: 72px;height: 72px;display: inline-block;">服务描述</span>
                </div>
                <div style="width: 770px;margin-left: 20px;position: relative;" v-html="goods.content">
                </div>
              </div>
              <div style="width: 100%;border-top: 1px solid #f3f3f3;margin-top: 30px;"></div>

              <div style="margin: 20px auto;clear: both;line-height: 200%; padding: 0 30px 30px;">
                <div style="float: left;width: 72px;text-align: center;">
                  <span style="background: url(https://img.58cdn.com.cn/ds/detail/fwtp.png) no-repeat;width: 72px;height: 72px;display: inline-block;background-size: 72px auto;"></span>
                  <span style="width: 72px;height: 72px;display: inline-block;">服务图片</span>
                </div>
                <div style="width: 770px;margin-left: 20px;display: inline-block;position: relative;">
                  <ul style="width: 790px;float: left;overflow: hidden;zoom: 1;display: inline;list-style: none;margin: 0;padding: 0;">
                    <li style="width: 378px;height: 283px;margin: 0 10px 10px 0;line-height: 285px;overflow: hidden; position: relative;float: left;"
                    v-for="item in goods.imageLists">
                      <span><img :src="item" style="width: 380px;height: 285px"></span>
                    </li>
                  </ul>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>

      <!--      评论-->
      <el-row>
        <el-col :span="24">
          <div style="margin-top: 10px; margin-bottom: 80px">
            <el-card>
              <div
                  style="padding-bottom: 10px; margin-bottom: 20px; border-bottom: 2px solid orangered; font-size: 20px">
                服务评分
              </div>

              <div style="display: flex; padding: 20px" v-for="item in messages">

                <div style="text-align: center; flex: 1">
                  <el-image :src="item.avatar" style="width: 60px; height: 60px; border-radius: 50%"></el-image>
                </div>
                <div style="padding: 0 10px; flex: 5">
                  <div><b style="font-size: 14px">{{ item.nickName }}</b></div>
                  <div style="padding: 10px 0; color: #888">
                    {{ item.content }}
                    <el-button type="text" size="mini" @click="del(item.id)" v-if="item.userId === user.id">删除
                    </el-button>
                  </div>
                  <div style="padding-bottom: 10px">
                    <div v-if="item.state === '1'" style="color: #888; font-size: 12px">
                      <span>好评</span>
                    </div>
                    <div v-else-if="item.state === '2'" style="color: #888; font-size: 12px">
                      <span>中评</span>
                    </div>
                    <div v-else-if="item.state === '3'" style="color: #888; font-size: 12px">
                      <span>差评</span>
                    </div>
                  </div>
                  <div style="color: #888; font-size: 12px">
                    <span>{{ item.time }}</span>
                  </div>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>

    </div>
  </div>
</template>

<script>
import API from "@/utils/request";

const url = "/api/video/"

export default {
  name: "Goods",
  data() {
    return {
      messages: [],
      dialogFormVisible: false,
      entity: {},
      address: '',
      mainImg: '',
      cartIcon: require('../../assets/购物车-23.png'),
      praiseActiveIcon: require("../../assets/点赞-激活.png"),
      collectActiveIcon: require("../../assets/收藏-激活.png"),
      num: 1,
      id: 1,
      user: {},
      goods: {imgs: []},
      praiseFlag: false
    };
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    this.id = this.$route.query.id
    this.load()
  },
  methods: {
    collect() {
      API.post("/api/collect", {
        goodsName: this.goods.name,
        goodsImg: this.goods.imgs[0],
        goodsId: this.goods.id,
        userId: this.user.id
      }).then(res => {
        if (res.code === '0') {
          this.$message({
            message: "收藏成功",
            type: "success"
          });
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
        this.load();
      })
    },
    praise() {
      if (this.praiseFlag) {
        this.$message({
          message: "您已点过赞",
          type: "warning"
        });
        return
      }
      this.praiseFlag = true
      this.entity = JSON.parse(JSON.stringify(this.goods))
      this.entity.praise += 1
      this.entity.imgs = null
      API.put("/api/goods", this.entity).then(res => {
        if (res.code === '0') {
          this.$message({
            message: "点赞成功",
            type: "success"
          });
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
        this.load();
      })
    },
    loadMessage() {
      API.get("/api/message/foreign/" + this.goods.id).then(res => {
        this.messages = res.data;
      })
    },
    buyNow() {
      this.$router.replace("/front/preOrder/" + this.goods.id)
    },
    save() {  // 新增评论
      if (!this.user.username) {
        this.$message({
          message: "请登录",
          type: "warning"
        });
        return;
      }
      if (!this.entity.content) {
        this.$message({
          message: "请填写内容",
          type: "warning"
        });
        return;
      }
      API.post("/api/message", this.entity).then(res => {
        if (res.code === '0') {
          this.$message({
            message: "评论成功",
            type: "success"
          });
        } else {
          this.$message({
            message: res.msg,
            type: "error"
          });
        }
        this.entity = {}
        this.loadMessage();
        this.dialogFormVisible = false;
      })
    },
    cancel() {
      this.dialogFormVisible = false;
      this.entity = {};
    },
    reReply(id) {
      this.dialogFormVisible = true;
      this.entity.parentId = id;
    },
    reply() {
      this.entity.content = this.entity.reply;
      this.save();
    },
    del(id) {
      API.delete("/api/message/" + id).then(res => {
        this.$message({
          message: "删除成功",
          type: "success"
        });
        this.loadMessage()
      })
    },
    addCart() {
      if (!this.user.id) {
        this.$message({
          type: 'warning',
          message: '请登录！'
        })
        return
      }
      if ((this.goods.store - this.num) < 0) {
        this.$message({
          type: 'warning',
          message: '商品库存不足！'
        })
        return
      }
      API.post("/api/cart", {goodsId: this.goods.id, count: this.num, userId: this.user.id}).then(res => {
        if (res.code === '0') {
          this.$message({
            type: 'success',
            message: '加入成功！'
          })
        } else {
          this.$message({
            type: 'error',
            message: res.msg
          })
        }
      })
    },
    show(img) {
      this.mainImg = img
      console.log(img)
    },
    load() {
      API.get("/api/goods/" + this.id).then(res => {
        this.goods = res.data

        this.loadMessage()

        // 处理图片
        if (!this.goods.imgs) {
          this.goods.imgs = ['']
        } else {
          this.goods.imgs = JSON.parse(this.goods.imgs)
        }
        this.mainImg = this.goods.imgs[0]
      })
    },
  },
};
</script>

<style scoped>
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>
