<template>
  <div>
    <div style="padding: 5px 0">
      <el-input v-model="text" placeholder="请输入订单编号" @keyup.enter.native="load" style="width: 200px"> <i slot="prefix" class="el-input__icon el-icon-search"></i></el-input>
    </div>
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="100" sortable> </el-table-column>
      <el-table-column prop="orderNo" label="订单编号"></el-table-column>
      <el-table-column prop="goodsName" label="维修项目名称"></el-table-column>
      <el-table-column prop="totalPrice" label="总价"></el-table-column>
      <el-table-column prop="userNickName" label="下单人昵称"></el-table-column>
      <el-table-column prop="maintainerNickName" label="维修工姓名"></el-table-column>
      <el-table-column prop="linkUser" label="联系人"></el-table-column>
      <el-table-column prop="linkPhone" label="联系电话"></el-table-column>
      <el-table-column prop="linkAddress" label="维修地址"></el-table-column>
      <el-table-column label="订单状态" prop="state">
        <template slot-scope="scope">
          <span v-if="scope.row.state ==='0'">待付款</span>
          <span v-if="scope.row.state ==='1'">派遣中</span>
          <span v-if="scope.row.state ==='2'">维修中</span>
          <span v-if="scope.row.state ==='3'">维修完成</span>
          <span v-if="scope.row.state ==='4'">待评价</span>
          <span v-if="scope.row.state ==='5'">订单完成</span>
          <span v-if="scope.row.state ==='6'">已取消</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>

      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-popconfirm v-if="scope.row.state === '2'"
                         @confirm="comment(scope.row)"
              title="确定提交维修完成？"
          >
            <el-button type="primary" slot="reference" >完成</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 10px">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-size="pageSize"
          :page-sizes="[2, 5, 10]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
      >
      </el-pagination>
    </div>

  </div>
</template>

<script>
import API from '../../utils/request'
const url = "/api/order/"

export default {
  name: "Order",
  data() {
    return {
      fileList: [],
      options: [],
      text: '',
      user: {},
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      entity:{}
    };
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    this.load()
  },
  methods: {
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    load() {
      API.post(url + `/page/${this.pageNum}/${this.pageSize}`, {
        orderNo: this.text,
        maintainerId: this.user.id
      }).then(res => {
        this.tableData = res.data.records || []
        this.total = res.data.total
      })
    },
    edit(obj) {
      this.entity = obj
      this.dialogFormVisible = true
    },
    // 提交上报
    comment(row) {
      row.state = '3'
      API.put(url, row).then(res => {
        this.$message({
          type: "success",
          message: "订单进程上报成功"
        })
        this.load()
      })
    },
  },
};
</script>

<style scoped>
</style>
