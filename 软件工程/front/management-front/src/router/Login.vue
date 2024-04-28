<template>
    <div id="login">
        <el-row type="flex" justify="center" align="middle" style=" height:100%;">
            <el-col style="width:585px;">
                <el-card id="logincard" class="um-loginbox">		
                    <h2 style="text-align: center;color: lightblue;">用 户 登 录</h2>
                    <br>
                    <el-input style="margin-bottom: 3%; width: 60%;margin-left: 20%;margin-top: -2%;"
                        placeholder="请输入用户名"
                        v-model="phone"
                        clearable>
                    </el-input><br>
                    <el-input style="margin-bottom: 3%; width: 60%;margin-left: 20%;margin-top: -2%;"
                    placeholder="请输入密码" v-model="password" show-password></el-input>
                    <br>
                    <br>
                    <el-button type="primary" style="width:29%;margin-left: 20%;" @click="register">注册账户</el-button>
                    <el-button type="danger" style="width:29%;" @click="go">确认</el-button>
                    <br>
                    <el-link type="primary" style="margin-left: 80%;" @click="admin_go">管理员登录-></el-link>
                </el-card>
                
            </el-col>
        </el-row>        
        <el-dialog title="注册新用户" :visible.sync="register_ctrl" :append-to-body="true" style="width: 80%;margin-left: 10%;">
            <el-form :model="form">
                <el-form-item label="用户名" style="width: 50%;margin-top: -2%;">
                    <el-input v-model="regUsername" autocomplete="off" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="密码" style="width: 50%;margin-top: -2%;">
                    <el-input type="password" show-password v-model="regpwd" autocomplete="off" placeholder="请输入密码"></el-input>
                </el-form-item>
                <el-form-item label="请重复密码" style="width: 50%;margin-top: -2%;">
                    <el-input type="password" show-password  v-model="regpwdagain" autocomplete="off" placeholder="请重复密码"></el-input>
                </el-form-item>
            </el-form>
                <div slot="footer" class="dialog-footer" style="margin-top: -10%;">
                    <el-button @click="register_ctrl = false">取 消</el-button>
                    <el-button type="primary" @click="submitReg">确 定</el-button>
                </div>
        </el-dialog>
    </div>
  </template>
  
  <script>
import router from '.';
import axios from 'axios';
    export default {
      name: "Login",
      data() {
        return {
                phone: '',
                password: '',
                register_ctrl:false,
                regUsername:'',
                regpwd:'',
                regpwdagain:'',
            }
        },
        methods:{
            go(){
                axios.post("http://localhost:8082/api/login", 
                {
                    "username":this.phone,
                    "password":this.password
                }).then(res => {
                    if(res.data.code===401){
                        window.alert("登录错误")
                        return
                    }
                    //请求成功执行
                    const jwtToken = res.data.data.token;
                    // 将JWT保存到localStorage
                    sessionStorage.setItem('jwtToken', jwtToken);
                    console.log(res.data)
                    router.push({path:"/main_page",query:{username:this.phone}})
                }).catch(error => {
                    error
                    window.alert("登录错误");
                })
                
            },
            admin_go(){
                router.push("/admin_login")
            },
            register(){
                this.register_ctrl = true
                this.regUsername = ''
                this.regpwd = ''
                this.regpwdagain=''
            },
            submitReg(){
                if (this.regpwd != this.regpwdagain){
                    alert("密码不一致")
                    return 
                }
                axios.post("http://localhost:8082/api/login/register", 
                {
                    "username":this.regUsername,
                    "password":this.regpwd
                }).then(res => {
                    if(res.data.code===401){
                        window.alert("注册错误")
                        return
                    }
                    alert("注册成功")
                    this.register_ctrl=false
                }).catch(error => {
                    error
                    window.alert("注册错误");
                })
            }
        }
    
    }
  </script>
  
  <style>
    #login{
        height: 100%;
        background-image: url('../assets/img/login_back.png');
        background-repeat: no-repeat;/*图片不重复*/
        overflow: hidden;/*溢出隐藏*/
        background-size: cover;/*背景覆盖窗口*/
    }
    #logincard{
        margin-top: 0%;
        background-color: rgba(255,255,255,0.6);

    }
  </style>