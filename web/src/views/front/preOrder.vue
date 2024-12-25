<template>
  <div style="margin-top: 10px">
    <div style="background-color: white; padding: 10px">
      <!--维修地址-->
      <div style="padding: 10px; margin-bottom: 20px; border-bottom: 1px solid #eee">
        <div style="font-size: 20px; border-bottom: 2px solid dodgerblue; padding-bottom: 10px; margin-bottom: 20px">
          维修地址
        </div>
        <div style="padding: 10px 0; color: #555" v-for="item in addressData" :key="item.id">
          <el-radio v-model="chooseAddress" :label="item" border>{{ item.linkUser }}</el-radio>
          <span>{{ item.linkUser }}</span>
          <span style="margin-left: 20px">{{ item.linkPhone }}</span>
          <span style="margin-left: 20px">{{ item.linkAddress }}</span>
        </div>
      </div>
      <!--维修工-->
      <div style="padding: 10px; margin-bottom: 20px; border-bottom: 1px solid #eee">
        <div style="font-size: 20px; border-bottom: 2px solid dodgerblue; padding-bottom: 10px; margin-bottom: 20px">
          维修工
        </div>
        <div style="padding: 10px 0; color: #555" >
          <el-radio v-model="maintainerId" :label="item.id" v-for="item in maintainerFind" :key="item.id" border>{{ item.nickName }}</el-radio>
        </div>
      </div>



      <!--        商品确认-->
      <el-table :data="carts" stripe s tyle="width: 100%" :show-header="false">
        <el-table-column label="商品图片" width="150">
          <template slot-scope="scope">
            <el-image :src="scope.row.imgs[0]" style="width: 100px; height: 100px;" fit="contain"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称"></el-table-column>

      </el-table>

      <div style="margin-top: 10px">
        <div style="background-color: white; padding: 10px">
          <div style="color: red; text-align: right">
            <div>
              <span>总价：</span>
              <span>￥ {{ totalPrice }}</span>
            </div>
            <div style="padding: 10px 0">
              <el-button style="background-color: red; color: white; width: 100px" @click="submitOrder">提交订单</el-button>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import API from "@/utils/request";

export default {
  name: "cart",
  data() {
    return {
      chooseAddress: null,// 选择的地址
      maintainerId:'', // 维修工id
      user: {},
      addressData: [],
      maintainerFind:[],
      carts: [],
      totalPrice: 0,
      discount: 0
    }
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    if (!this.user) {
      this.$message({
        type: "warning",
        message: '请登录！'
      })
      return
    }
    this.load()
  },
  methods: {
    submitOrder() {
      if (!this.maintainerId) {
        this.$message({
          type: 'warning',
          message: '请选择维修工！'
        })
        return
      }
      if (!this.chooseAddress) {
        this.$message({
          type: 'warning',
          message: '请选择维修地址！'
        })
        return
      }
      // 提交订单
      API.post("/api/order", {
        goodsId: this.carts[0].id,
        totalPrice: this.totalPrice,
        linkUser: this.chooseAddress.linkUser,
        linkPhone: this.chooseAddress.linkPhone,
        linkAddress: this.chooseAddress.linkAddress,
        maintainerId: this.maintainerId,
        state: '0'
      }).then(res => {
        if (res.code === '0') {
          this.$message({
            type: 'success',
            message: '提交成功！'
          })
          this.$router.replace("/front/order")
        } else {
          this.$message({
            type: 'error',
            message: res.msg
          })
        }
      })
    },
    load() {
      let id = this.$route.params.id
      console.log(id)
      API.post("/api/order/pre/"+id).then(res => {
        this.carts = res.data.list
        this.totalPrice = res.data.totalPrice

        // 处理图片
        this.carts.forEach(item => {
          if (!item.imgs) {
            item.imgs = ['']
          } else {
            item.imgs = JSON.parse(item.imgs)
          }
        })

      })

      // 获取维修地址
      API.get("/api/address").then(res => {
        this.addressData = res.data
      })

      // 获取维修工信息
      API.get("/api/user/getMaintainerFindAll").then(res => {
        this.maintainerFind = res.data
      })
    },
  }
}
</script>

<style scoped>

</style>
