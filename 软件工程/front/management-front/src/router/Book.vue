<template>
  <div>
        <el-link style="margin-left: 6%;color: red;" @click="reg=true">点击注册成为会员</el-link>
        <el-input
                style="width: 40%;margin-left: 28%; "
                placeholder="输入书名搜索想要的图书"
                prefix-icon="el-icon-search"
                v-model="input"
                @input="handleInput"
                >
        </el-input>
        <el-button style="margin-left: 1%;height: 38px;" type="primary" @click="search">搜索</el-button>
        
        <el-table :data="tableData" border stripe style="width: 90%;margin-left: 5%;margin-top: 1%;">
          <el-table-column prop="name" label="书名">
            <template slot-scope="scope">
              <div>
                <div v-html="scope.row.name"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="简介">
          </el-table-column>
          <el-table-column prop="type" label="分类">
          </el-table-column>
          <el-table-column prop="isVip" label="是否vip专属" >
          </el-table-column>
          <el-table-column>
              <template slot-scope="scope">
                  <el-button type="success" style="padding: 6px;" icon="el-icon-edit" @click="checkInfo(scope.row)">借阅</el-button>
              </template>
          </el-table-column>
        </el-table>

        <el-dialog title="会员注册" :visible.sync="reg" :append-to-body="true" style="width: 80%;margin-left: 10%;">
            <el-form :model="form">
                <el-form-item label="用户名" style="width: 80%;margin-top: -2%;">
                    <el-input v-model="username" autocomplete="off" disabled></el-input>
                </el-form-item>
                <el-form-item label="手机号" style="width: 80%;margin-top: -2%;">
                    <el-input v-model="phone" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="是否成为vip(可享受vip增值借阅服务)" style="width: 80%;margin-top: -2%;">
                  <br>
                  <el-checkbox v-model="isVip">成为vip</el-checkbox>
                </el-form-item>
            </el-form>
                <div slot="footer" class="dialog-footer" style="margin-top: -10%;">
                    <el-button @click="reg = false">取 消</el-button>
                    <el-button type="primary" @click="submitReg">确 定</el-button>
                </div>
        </el-dialog>
       

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
        tableData: [],
        input:'',
        total:'',
        pageSize:10,
        current:1,
        bookInfo:false,
        reg:false,
        username:'',
        phone:'',
        isVip:false
      };
    },
    methods: {
      submitReg(){
        let jwt = sessionStorage.getItem("jwtToken")
        axios.post("http://localhost:8082/api/user/register",
        {
            'username':this.username,
            'phone':this.phone,
            'isVip':this.isVip
        },
        {
          headers: {
            "authorization":jwt
          }
        }
        ).then(res=>{
          if(res.data.code==200){
            alert("注册成功，请重新登录")
            this.reg=false
          }
        }).catch(error=>{
          error    
        })
      },
      checkInfo(row){
        let jwt = sessionStorage.getItem("jwtToken")
        axios.get("http://localhost:8082/api/book/read",
                  {
                      headers: {
                          "authorization":jwt
                      },
                      params:{
                        "id":row.id
                      }
                  }
              ).then(res=>{
                  if(res.data.code===402){
                    window.alert(res.data.msg)
                    return
                  }
                  if(res.data.code===200){
                    window.alert("借阅成功")
                  }
              }).catch(error=>{
                  error
                  
              })
      },
      search(){
        let jwt = sessionStorage.getItem("jwtToken")
              axios.post("http://localhost:8082/api/book",
                  {
                        "currentPage":this.current,
                        "pageSize":this.pageSize,
                        "inputText":this.input
                  },
                  {
                      headers: {
                          "authorization":jwt
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
                    
                    let data = res.data.responseResult.data
                    data.forEach(item => {
                      item.isVip = item.isVip === 1 ? '是' : '否';
                    });
                    this.total = res.data.total
                    this.tableData = data
                  }
              }).catch(error=>{
                  error
                  
              })
      },
      handleInput(){
        let jwt = sessionStorage.getItem("jwtToken")
  
          axios.post("http://localhost:8082/api/book",
              {
                    "currentPage":this.current,
                    "pageSize":this.pageSize,
                    "inputText":this.input
              },
              {
                  headers: {
                      "authorization":jwt
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
                  let data = res.data.responseResult.data
                data.forEach(item => {
                  item.isVip = item.isVip === 1 ? '是' : '否';
                });
                this.total = res.data.total
                this.tableData = data
              }
          }).catch(error=>{
           
              error
              
          })
      },
      handleChange(currentPage){
        let jwt = sessionStorage.getItem("jwtToken")
  
        axios.post("http://localhost:8082/api/book",
            {
                  "currentPage":currentPage,
                  "pageSize":this.pageSize,
                  "inputText":this.input
            },
            {
                headers: {
                    "authorization":jwt
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
                let data = res.data.responseResult.data
              data.forEach(item => {
                item.isVip = item.isVip === 1 ? '是' : '否';
              });
              this.total = res.data.total
              this.tableData = data
            }
        }).catch(error=>{
        
            error
            
        })
      }
    },
    created(){
      this.username = this.$route.query.username
      let jwt = sessionStorage.getItem("jwtToken")
  
              axios.post("http://localhost:8082/api/book",
                  {
                        "currentPage":this.current,
                        "pageSize":this.pageSize,
                        "inputText":''
                  },
                  {
                      headers: {
                          "authorization":jwt
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
                    
                    let data = res.data.responseResult.data
                    data.forEach(item => {
                      item.isVip = item.isVip === 1 ? '是' : '否';
                    });
                    this.tableData = data
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