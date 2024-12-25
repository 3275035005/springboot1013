<template>
  <div>
    <div style="padding: 5px 0">
      <el-input v-model="text" @keyup.enter.native="load" style="width: 200px"> <i slot="prefix" class="el-input__icon el-icon-search"></i></el-input>
      <el-button @click="add" type="primary" size="mini" style="margin: 10px">新增</el-button>
    </div>
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="100" sortable> </el-table-column>
      <el-table-column prop="name" label="维修项目名称" width="150"></el-table-column>
      <el-table-column label="图片" width="200px">
        <template slot-scope="scope">
          <el-image style="width: 100px; height: 100px" :src="scope.row.imgs[0]"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="categoryName" label="分类"></el-table-column>
      <el-table-column prop="description" label="维修项目描述" width="300"></el-table-column>
      <el-table-column prop="price" label="价格"></el-table-column>
      <el-table-column prop="praise" label="点赞数"></el-table-column>
      <el-table-column prop="sales" label="销量"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>

      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" circle  @click="edit(scope.row)"></el-button>
          <el-popconfirm
              @confirm="del(scope.row.id)"
              title="确定删除？"
          >
            <el-button type="danger" icon="el-icon-delete" circle slot="reference" style="margin-left: 10px"></el-button>
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
    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="60%"
               :close-on-click-modal="false">
      <el-form :model="entity" label-position="left" label-width="150px">
        <el-form-item label="维修项目名称">
          <el-input v-model="entity.name" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="维修项目描述">
          <el-input type="textarea"    :rows="10" v-model="entity.description" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>

        <el-form-item label="价格">
          <el-input v-model="entity.price" autocomplete="off" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="维修分类">
          <el-select v-model="entity.categoryId" placeholder="请选择维修分类" style="width: 80%">
            <el-option label="家具" value="1"></el-option>
            <el-option label="家电" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="封面">
          <el-upload action="http://localhost:9999/files/upload" multiple :on-success="handleSuccess" ref="upload">
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>

        <el-form-item label="服务图片">
          <div v-for="(item, idx) in entity.imageLists" :key="item + idx">
            <img class="img" :src="item" style="  width: 178px;height: 178px;display: block;"/>
            <i class="el-icon-circle-close img-close" @click="handleRemove(idx)"></i>
          </div>

          <el-upload
              v-if="entity.imageLists.length < 9"
              action="http://localhost:9999/files/upload"
              ref="imgUpload"
              accept="image/png, image/jpeg, image/gif"
              :on-success="handleUploadSuccess"
              :before-upload="beforeAvatarUpload"
              multiple
              :show-file-list="false"
              :on-exceed="handleExceed"
              :limit="9 - entity.imageLists.length"
          >
            <i class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <div class="upload-tip">支持9张内，不超过10M的jpg、png、gif文件</div>
        </el-form-item>
        <el-form-item label="服务描述">
          <tinymce v-if="dialogFormVisible" :height="300" v-model="entity.content"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import API from '../../utils/request'
const url = "/api/goods/"
import Tinymce from '@/components/Tinymce'
export default {
  name: "Goods",
  components: {Tinymce},
  data() {
    return {
      components: {Tinymce}, // 注册组件
      fileList: [],
      text: '',
      user: {},
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      entity: {
        imageLists:[]
      },
      total: 0,
      picList:[],
      dialogFormVisible: false
    };
  },
  created() {
    this.user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    this.load()
  },
  methods: {
    handleSuccess(res) {
      let url =  res.data
      this.fileList.push(url)
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    load() {
       API.get(url + "/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: this.text
          }
       }).then(res => {
          this.tableData = res.data.records || []
          this.total = res.data.total

         this.tableData.forEach(item => {
           // 处理下表格的图片显示
           if (!item.imgs) {
             item.imgs = ['']
           } else {
             item.imgs = JSON.parse(item.imgs)
           }
         })
       })

    },
    add() {
      this.entity = {
        imageLists:[]
      }
      this.fileList = []
      if(this.$refs['upload']) {
        this.$refs['upload'].clearFiles()
      }
      this.dialogFormVisible = true
    },
    edit(obj) {
      console.log(obj)
      this.entity = obj
      if(!this.entity.imageLists){
        this.entity.imageLists = []
      }
      this.fileList = []
      this.dialogFormVisible = true
      this.$nextTick(() => {
        if(this.$refs['upload']) {
          this.$refs['upload'].clearFiles()
        }
      })
    },
    save() {
      if (this.fileList.length) {
        this.entity.imgs = JSON.stringify(this.fileList)
      }
      if (typeof this.entity.imgs === 'object') {
        this.entity.imgs = JSON.stringify(this.entity.imgs)
      }
      if (!this.entity.id) {
        API.post(url, this.entity).then(res => {
           if (res.code === '0') {
             this.$message({
               type: "success",
               message: "操作成功"
             })
           } else {
             this.$message({
               type: "error",
               message: res.msg
             })
           }
           this.load()
           this.dialogFormVisible = false
        })
      } else {
        API.put(url, this.entity).then(res => {
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "操作成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load()
          this.dialogFormVisible = false
        })
      }
    },
    del(id) {
      API.delete(url + id).then(res => {
        this.$message({
          type: "success",
          message: "操作成功"
        })
        this.load()
      })
    },
    beforeAvatarUpload(file) {
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        this.$message.error("上传图片大小不能超过 10MB!");
      }
      return isLt10M
    },
    // 文件超出限制
    handleExceed() {
      this.$message.error("你最多能发布9张照片");
      // 清空已上传的文件列表，不然数量限制会有问题
      this.$refs.imgUpload.clearFiles();
    },
    // 上传成功回调
    handleUploadSuccess(res, file) {
      console.log(res)
      if (res.code === '0') {
        this.entity.imageLists.push(res.data);
      } else {
        this.$message.error(res.msg || "上传失败");
        this.$refs.imgUpload.clearFiles();
      }
    },
    handleRemove(idx) {
      this.entity.imageLists.splice(idx, 1);
    },
  },
};
</script>

<style scoped>
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border-style: dotted;
  margin: 10px 0;
}
</style>
