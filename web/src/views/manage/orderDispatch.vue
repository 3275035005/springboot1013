<template>
  <div>
    <div style="padding: 5px 0">
      <el-input v-model="text" placeholder="请输入订单编号" @keyup.enter.native="load" style="width: 200px"><i slot="prefix"
                                                                                                        class="el-input__icon el-icon-search"></i>
      </el-input>
    </div>
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="100" sortable></el-table-column>
      <el-table-column prop="orderNo" label="订单编号"></el-table-column>
      <el-table-column prop="goodsName" label="维修项目名称"></el-table-column>
      <el-table-column prop="totalPrice" label="总价"></el-table-column>
      <el-table-column prop="userNickName" label="下单人昵称"></el-table-column>
      <el-table-column prop="maintainerNickName" label="维修工姓名"></el-table-column>
      <el-table-column prop="linkUser" label="联系人"></el-table-column>
      <el-table-column prop="linkPhone" label="联系电话"></el-table-column>
      <el-table-column prop="linkAddress" label="维修地址"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>

      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button type="primary" v-if="scope.row.state === '1'" @click="edit(scope.row)">派遣</el-button>
          <el-popconfirm
              @confirm="del(scope.row.id)"
              title="确定删除？"
          >
            <el-button type="danger" icon="el-icon-delete" circle slot="reference"
                       style="margin-left: 10px"></el-button>
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
    <!-- 弹窗   -->
    <el-dialog title="派遣维修任务" :visible.sync="dialogFormVisible" width="30%"
               :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <el-form :model="entity">
        <el-form-item label="维修工" label-width="150px">
          <el-select v-model="entity.maintainerId"  placeholder="请选择">

            <el-option
                v-for="item in maintainerFind"
                :key="item.id"
                :label="item.nickName"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="comment">派遣</el-button>
      </div>
    </el-dialog>
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
      entity: {},
      maintainerFind: [],
      dialogFormVisible: false
    };
  },
  created() {
    // 获取维修工信息
    API.get("/api/user/getMaintainerFindAll").then(res => {
      this.maintainerFind = res.data;
      console.log(this.maintainerFind)
    })
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

    edit(obj) {
      this.entity = obj
      this.dialogFormVisible = true
    },
    load() {
      API.post(url + `/page/${this.pageNum}/${this.pageSize}`, {
        orderNo: this.text,
        state: '1'
      }).then(res => {
        this.tableData = res.data.records || []
        this.total = res.data.total
      })
    },
    // 提交派遣
    comment() {
      this.entity.state = '2'
      API.put(url, this.entity).then(res => {
        this.$message({
          type: "success",
          message: "派遣成功"
        })
        this.dialogFormVisible = false
        this.load()
      })
    },
    del(id) {
      API.delete(url + id).then(res => {
        this.$message({
          type: "success",
          message: "操作成功"
        })
        this.load()
      })
    }
  },
};
</script>

<style scoped>
</style>
