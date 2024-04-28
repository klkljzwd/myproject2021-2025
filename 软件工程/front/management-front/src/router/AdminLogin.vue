<template>
    <div id="login">
        <el-row type="flex" justify="center" align="middle" style=" height:100%;">
            <el-col style="width:585px;">
                <el-card id="logincard"  class="um-loginbox">		
                    <h2 style="text-align: center;color: lightblue;">管理员登录</h2>
                    <br>
                    <el-input style="margin-bottom: 3%; width: 60%;margin-left: 20%;"
                        placeholder="请输入用户名"
                        v-model="username"
                        clearable>
                    </el-input><br>
                    <el-input style="margin-bottom: 3%; width: 60%;margin-left: 20%;"
                    placeholder="请输入密码" v-model="password" show-password></el-input>
                    
                    <br>
                    <br>
                    <el-button type="danger" style="width:60%;margin-left: 20%;" @click="go">确认</el-button>
                    <br>
                </el-card>
                    
            </el-col>
        </el-row>        
    </div>
  </template>
  
  <script>
import router from '.';
import axios from 'axios';
    export default {
      data() {
        return {
            username: '',
            password: ''
            }
        },
        methods:{
            go(){
                axios.post("http://localhost:8082/api/login/admin", 
                {
                    "username":this.username,
                    "password":this.password
                }).then(res => {
                    //请求成功执行
                    const jwtToken = res.data.data.token;
                    // 将JWT保存到localStorage
                    sessionStorage.setItem('jwtToken', jwtToken);
                    console.log(res.data)
                    if(res.data.code===401){
                        window.alert("登录错误")
                    }
                    router.push("/admin")

                }).catch(error => {
                    error
                    window.alert("登录错误");
                })
                
            }
        }
    
    }
  </script>
  
  <style lang="scss" scoped>

  </style>