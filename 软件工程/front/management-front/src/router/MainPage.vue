<template>
  <el-container>
    <el-header>
        <div class="line"></div>
        <el-menu
        :default-active="activeIndex2"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        >
            <el-menu-item index="1" style="margin-left: 5%;" @click="book">图书借阅</el-menu-item>
            <el-menu-item index="3">我的借阅</el-menu-item>
        </el-menu>
    </el-header>
    <el-main>
        <router-view></router-view>
    </el-main>
  </el-container>
</template>

<script>
import router from '.';

export default {
    data() {
      return {
        activeIndex: '1',
        activeIndex2: '1',
        search_items: '',
        username:''
      };
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },
      book(){
        router.push({path:"/book",query:{username:this.username}})
      },
    },
    created(){
      this.username = this.$route.query.username
      router.push({path:"/book",query:{username:this.$route.query.username}})
    },

}
</script>

<style>

</style>