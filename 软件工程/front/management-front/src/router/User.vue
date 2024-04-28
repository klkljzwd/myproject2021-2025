<template>
    <div>
      <div>
          <el-input placeholder="请输入用户名" style="width: 250px;margin-left: 10px;" v-model="input" prefix-icon="el-icon-search"></el-input>
          <el-button style="margin: 5px; height: 39px;" type="primary" @click="searchRole" >搜索</el-button>
      </div>
      <div style="margin: 10px;">
          <el-button type="primary" icon="el-icon-plus" style="padding: 8px;width: 90px;" @click="addRoleBool=true">新增</el-button>
          
      </div>
      <el-table :data="tableData" border stripe>
          <el-table-column prop="id" label="用户id" width="200">
          </el-table-column>
          <el-table-column prop="username" label="用户名" width="200">
          </el-table-column>
          <el-table-column prop="phone" label="手机号" width="200">
          </el-table-column>
          <el-table-column prop="status" label="是否黑名单(1否/0是)" width="100">
          </el-table-column>
          <el-table-column>
              <template slot-scope="scope">
                  <el-button type="success" style="padding:6px;" icon="el-icon-edit" @click="open(scope.row)">编辑</el-button>
                  <el-button type="danger" style="padding:6px;" icon="el-icon-delete" @click="deleteRole(scope.row)">删除</el-button>
              </template>
          </el-table-column>
  
          <el-dialog title="修改" :visible.sync="dialogFormVisible" :append-to-body="true">
              <el-form :model="form">
                  <el-form-item label="用户名称" :label-width="formLabelWidth">
                      <el-input v-model="form.username" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="手机号" :label-width="formLabelWidth">
                      <el-input v-model="form.phone" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="是否黑名单" :label-width="formLabelWidth">
                      <el-input v-model="form.status" autocomplete="off"></el-input>
                  </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                  <el-button @click="dialogFormVisible = false">取 消</el-button>
                  <el-button type="primary" @click="submitUpdate">确 定</el-button>
              </div>
          </el-dialog>
  
          <el-dialog title="新增" :visible.sync="addRoleBool" :append-to-body="true">
              <el-form :model="form">
                  <el-form-item label="用户名称" :label-width="formLabelWidth">
                      <el-input v-model="roleNameAdd" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="手机号" :label-width="formLabelWidth">
                      <el-input v-model="descriptionAdd" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="是否黑名单" :label-width="formLabelWidth">
                      <el-input v-model="statusAdd" autocomplete="off"></el-input>
                  </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                  <el-button @click="addRoleBool = false">取 消</el-button>
                  <el-button type="primary" @click="submitAdd">确 定</el-button>
              </div>
          </el-dialog>
      </el-table>

      <el-pagination
            @current-change="handleChange"
            :current-page="current"
            style="text-align: center;"
            background
            layout="prev, pager, next"
            :total=total
            :page-size=pageSize>
        </el-pagination>

    </div>
  </template>
  
  <script>
  import axios from 'axios';

 
  export default {
      data() {
        return {
          tableData: '',
          collapseBtnClass:'el-icon-s-fold',
          isCollapse: true,
          input:"",
          dialogFormVisible: false,
          form: {
            username:'',
            phone:'',
            status:'',
              id:''
          },
          formLabelWidth: '120px',
          addRoleBool: false,
          roleNameAdd: '',
          descriptionAdd:'',
          statusAdd:'',
          total:'',
          pageSize:10,
          current:1,
          bindAuthBool:false
  
        }
      },
      methods:{
          open(row){
              this.dialogFormVisible = true
              this.form.username= row.username
              this.form.phone = row.phone
              this.form.status=row.status
              this.form.id = row.id
          },
          submitAdd(){
              let jwt = sessionStorage.getItem("jwtToken")
              axios.post("http://localhost:8082/api/user/add",
                  {
                      "username":this.roleNameAdd,
                      "phone":this.descriptionAdd,
                      "status":this.statusAdd
                  },
                  {
                      headers: {
                          "authorization":jwt
                      }
                  }
              ).then(res=>{
                if(res.data.code===402){
                        window.alert("权限不足")
                        return
                    }
                  if(res.data.code===401){
                      window.alert(res.data.msg)
                  }
                  location.reload()
              }).catch(error=>{
                  error
          
              })
          },
          submitUpdate(){
              let jwt = sessionStorage.getItem("jwtToken")
  
              axios.post("http://localhost:8082/api/user/update",
                  {
                        "id":this.form.id,
                      "username":this.form.username,
                      "phone":this.form.phone,
                      "status":this.form.status
                  },
                  {
                      headers: {
                          "authorization":jwt
                      }
                  }
              ).then(res=>{
                if(res.data.code===402){
                        window.alert("权限不足")
                        return
                    }
                  location.reload()
                  if(res.data.code===401){
                      window.alert(res.data.msg)
                  }
              }).catch(error=>{
                  error
                  
              })
          },
          deleteRole(row){
              let jwt = sessionStorage.getItem("jwtToken")
              const id = row.id.toString()
              this.$confirm('此操作将永久删除该用户，是否确定?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                      axios.get("http://localhost:8082/api/user/delete",
                      {
                          headers: {
                              "authorization":jwt
                          },
                          params:{
                              "id":id
                          }
                      }
                  ).then(res=>{
                    if(res.data.code===402){
                        window.alert("权限不足")
                        return
                    }
                      location.reload()
                      if(res.data.code===401){
                          window.alert(res.data.msg)
                      }
                  }).catch(error=>{
                      error
                  })
              this.$message({
                  type: 'success',
                  message: '删除成功!'
              });
              }).catch(() => {
              this.$message({
                  type: 'info',
                  message: '已取消删除'
              });          
              });
  
              
          },
          handleChange(currentPage){
            this.current = currentPage
            let jwt = sessionStorage.getItem("jwtToken")
            axios.get("http://localhost:8082/api/user/page",
                  {
                      headers: {
                          "authorization":jwt
                      },
                      params:{
                        "currentPage":this.current,
                        "pageSize":this.pageSize
                      }
                  }
              ).then(res=>{
                    if(res.data.code===402){
                        window.alert(res.data.msg)
                        return
                    }

                  if(res.data.code===401){
                      window.alert(res.data.msg)
                      return
                  }
                  if(res.data.responseResult.code===200){
                      this.tableData = res.data.responseResult.data
                      this.total = res.data.total
                  }
              }).catch(error=>{
                  error
                  
              })
          }
      },
      created(){
        let jwt = sessionStorage.getItem("jwtToken")
        axios.get("http://localhost:8082/api/user/page",
                  {
                      headers: {
                          "authorization":jwt
                      },
                      params:{
                        "currentPage":this.current,
                        "pageSize":this.pageSize
                      }
                  }
              ).then(res=>{
                    if(res.data.code===402){
                        window.alert(res.data.msg)
                        return
                    }

                  if(res.data.code===401){
                      window.alert(res.data.msg)
                      return
                  }
                  if(res.data.responseResult.code===200){
                      this.tableData = res.data.responseResult.data
                      this.total = res.data.total
                  }
              }).catch(error=>{
                  error
                  
              })
          
      }
  }
  </script>
  
  <style>
  
  </style>