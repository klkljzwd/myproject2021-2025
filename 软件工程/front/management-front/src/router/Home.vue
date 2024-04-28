<template>
  <div style="height: 100%;">
    <el-container style="height: 100%;">
  <el-aside width="200px" style="background-color: rgb(238, 241, 246); height: 100%; overflow-x: hidden;">
    <el-menu :default-openeds="['1']" style="height: 100%;" text-color="#fff"
    background-color="rgb(62, 59, 58)"
    :collapse-transition="false"
    active-text-color="#ffd04b"
    class="el-menu-vertical-demo"
    :isCollapse="isCollapse"

    >
      <div style="height: 60px;line-height: 60px;text-align: center;">
        <b style="color: white;">用户管理系统</b>
      </div>
      <el-submenu index="1">
        <template slot="title"><i class="el-icon-setting"></i>管理员功能</template>
        <el-menu-item-group >
          <el-menu-item index="3-1" @click="gotoRole">角色管理</el-menu-item>
          <el-menu-item index="3-2" @click="gotoAuth">权限管理</el-menu-item>
          <el-menu-item index="3-3" @click="gotoUser">用户管理</el-menu-item>
          <el-menu-item index="3-4" @click="gotoBook">图书管理</el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
  </el-aside>
  
  <el-container>
    <el-header style="font-size: 12px;background-color: white; display: flex;border-bottom: 1px solid;border-color: gray;">
        <div style="flex: 1;font-size: 20px; float: left;">
            <span :class="collapseBtnClass" style="cursor: pointer;width: 20px;height: 20px;" @click="collapse"></span>
        </div>
      <el-dropdown>
        <span style="font-size: 15px;font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;">管理员</span>
        <i class="el-icon-arrow-down" style="margin-right: 15px"></i>

      </el-dropdown>
      
    </el-header>
    
    <el-main>
        <router-view></router-view>
    </el-main>
    
  </el-container>
</el-container>
  </div>
</template>

<script>
import axios from 'axios'
import router from '.';
export default {
    data() {
      const item = {
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄',
        
      };
      return {
        tableData: Array(20).fill(item),
        collapseBtnClass:'el-icon-s-fold',
        isCollapse: true,
        input:""
      }
    },
    methods: {
        collapse() {
          this.isCollapse = !this.isCollapse
        },
        gotoRole(){
          router.push("/role")
        },
        gotoUser(){
          router.push("/user")
        },
        gotoAuth(){
          router.push("/auth")
        },
        gotoBook(){
          router.push("/bookm")
        }
    },
    mounted(){
      axios
    }
}
</script>

<style>
.el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }
  
  .el-aside {
    color: #333;
  }
</style>