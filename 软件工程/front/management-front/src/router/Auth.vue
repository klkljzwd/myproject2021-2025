<template>
    <div>
      <div>
          <el-input placeholder="请输入权限名" style="width: 250px;margin-left: 10px;" v-model="input" prefix-icon="el-icon-search"></el-input>
          <el-button style="margin: 5px; height: 39px;" type="primary" @click="searchRole" >搜索</el-button>
      </div>
      <div style="margin: 10px;">
          <el-button type="primary" icon="el-icon-plus" style="padding: 8px;width: 90px;" @click="addRoleBool=true">新增</el-button>
          
      </div>
      <el-table :data="tableData" border stripe>
          <el-table-column prop="id" label="权限id" width="200">
          </el-table-column>
          <el-table-column prop="resource" label="权限名" width="400">
          </el-table-column>

          <el-table-column prop="status" label="权限状态(1有效/0无效)" width="200">
          </el-table-column>
          <el-table-column prop="level" label="权限等级" width="100">
          </el-table-column>
          <el-table-column>
              <template slot-scope="scope">
                  <el-button type="success" style="padding:6px;" icon="el-icon-edit" @click="open(scope.row)">编辑</el-button>
                  <el-button type="danger" style="padding:6px;" icon="el-icon-delete" @click="deleteRole(scope.row)">删除</el-button>
              </template>
          </el-table-column>
  
          <el-dialog title="修改" :visible.sync="dialogFormVisible" :append-to-body="true">
              <el-form :model="form">
                  <el-form-item label="权限名" :label-width="formLabelWidth">
                      <el-input v-model="form.resource" autocomplete="off"></el-input>
                  </el-form-item>
    
                  <el-form-item label="权限状态(1有效/0无效)" :label-width="formLabelWidth">
                      <el-input v-model="form.status" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="权限等级" :label-width="formLabelWidth">
                      <el-input v-model="form.level" autocomplete="off"></el-input>
                  </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                  <el-button @click="dialogFormVisible = false">取 消</el-button>
                  <el-button type="primary" @click="submitUpdate">确 定</el-button>
              </div>
          </el-dialog>
  
          <el-dialog title="新增" :visible.sync="addRoleBool" :append-to-body="true">
              <el-form :model="form">
                  <el-form-item label="权限名" :label-width="formLabelWidth">
                      <el-input v-model="authNameAdd" autocomplete="off"></el-input>
                  </el-form-item>

                  <el-form-item label="权限状态(1有效/0无效)" :label-width="formLabelWidth">
                      <el-input v-model="statusAdd" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="权限等级" :label-width="formLabelWidth">
                      <el-input v-model="levelAdd" autocomplete="off"></el-input>
                  </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                  <el-button @click="addRoleBool = false">取 消</el-button>
                  <el-button type="primary" @click="submitAdd">确 定</el-button>
              </div>
          </el-dialog>
  
  
         
      </el-table>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
  import router from '.';
  export default {
      data() {
        return {
          tableData: '',
          collapseBtnClass:'el-icon-s-fold',
          isCollapse: true,
          input:"",
          dialogFormVisible: false,
          form: {
            id:'',
            resource:'',
            operation:'',
            status:'',
            level:''
          },
          formLabelWidth: '120px',
          addRoleBool: false,
          authNameAdd: '',
          operationAdd:'',
          statusAdd:'',
          levelAdd:''
  
        }
      },
      methods:{
          open(row){
              this.dialogFormVisible = true
              this.form.id = row.id
              this.form.resource = row.resource
              this.form.status = row.status
              this.form.level = row.level
          },
          submitAdd(){
              let jwt = sessionStorage.getItem("jwtToken")
              axios.post("http://localhost:8082/api/auth/add",
                  {
                      "resource":this.authNameAdd,
                      "status":this.statusAdd,
                      "level":this.levelAdd
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
  
              axios.post("http://localhost:8082/api/auth/update",
                  {
                        "id":this.form.id,
                      "resource":this.form.resource,
                      "status":this.form.status,
                      "level":this.form.level
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
              this.$confirm('此操作将永久删除该权限，是否确定?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                      axios.get("http://localhost:8082/api/auth/delete",
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
          searchRole(){
              
          }
      },
      created(){
          const instance = axios.create({
              baseURL:'',
              timeout:5000
          })
          let jwt = sessionStorage.getItem("jwtToken")
          
          instance.get("http://localhost:8082/api/auth",{
              headers: {
                  "authorization":jwt
              }
          }).then(
              res=>{
                  if(res.data.code===200){
                      this.tableData = res.data.data
                      console.log(res.data.data)
                  }else{
                      if(res.data.code===402){
                          window.alert("权限不足")
                          return
                      }
                      if(res.data.code===401){
                          window.alert("未知错误")
                      }
                      if(res.data===403){
                        router.push('/');
                      }
                  }
                  
              }
          ).catch(error=>{
              error
          })
      }
  }
  </script>
  
  <style>
  
  </style>