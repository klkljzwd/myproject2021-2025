<template>
  <div>
    <el-button type="primary" @click="addBook=true">新增</el-button>
    <el-input
                style="width: 40%;margin-left: 45%; "
                placeholder="输入书名"
                prefix-icon="el-icon-search"
                v-model="input"
                @input="handleInput"
                >
    </el-input>
    <el-button style="margin-left: 1%;" type="primary" @click="search">搜索</el-button>
    <el-table :data="tableData" border stripe style="width: 100%; margin-top: 1%;">
          <el-table-column prop="id" label="id">
          </el-table-column>
          <el-table-column prop="name" label="书名">
            <template slot-scope="scope">
              <div>
                <div v-html="scope.row.name"></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="简介" >
          </el-table-column>
          <el-table-column prop="type" label="分类">
          </el-table-column>
          <el-table-column prop="isVip" label="是否vip专属">
          </el-table-column>
         
          <el-table-column>
              <template slot-scope="scope">
                  <el-button type="success" style="padding:6px;" icon="el-icon-edit" @click="open(scope.row)">编辑</el-button>
                  <el-button type="danger" style="padding:6px;" icon="el-icon-delete" @click="deleteBook(scope.row)">删除</el-button>
              </template>
          </el-table-column>
    </el-table>
    <el-dialog title="新增" :visible.sync="addBook" :append-to-body="true">
              <el-form :model="form">
                  <el-form-item label="书名" :label-width="formLabelWidth">
                      <el-input v-model="name" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="作者" :label-width="formLabelWidth">
                      <el-input v-model="author" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="是否vip专享(1是/0不是)" :label-width="formLabelWidth">
                      <el-input v-model="isVip" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="描述" :label-width="formLabelWidth">
                      <el-input v-model="description" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="出版商" :label-width="formLabelWidth">
                      <el-input v-model="publish" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="类别" :label-width="formLabelWidth">
                      <el-input v-model="type" autocomplete="off"></el-input>
                  </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                  <el-button @click="addBook = false">取 消</el-button>
                  <el-button type="primary" @click="submitAdd">确 定</el-button>
              </div>
        </el-dialog>

        <el-dialog title="修改" :visible.sync="updateBook" :append-to-body="true">
              <el-form :model="form">
                  <el-form-item label="书名" :label-width="formLabelWidth">
                      <el-input v-model="form.name" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="作者" :label-width="formLabelWidth">
                      <el-input v-model="form.author" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="是否vip专享(1是/0不是)" :label-width="formLabelWidth">
                      <el-input v-model="form.isVip" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="描述" :label-width="formLabelWidth">
                      <el-input v-model="form.description" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="出版商" :label-width="formLabelWidth">
                      <el-input v-model="form.publish" autocomplete="off"></el-input>
                  </el-form-item>
                  <el-form-item label="类别" :label-width="formLabelWidth">
                      <el-input v-model="form.type" autocomplete="off"></el-input>
                  </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                  <el-button @click="updateBook = false">取 消</el-button>
                  <el-button type="primary" @click="submitUpdate">确 定</el-button>
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
    data(){
        return{
            tableData:'',
            input:'',
            current:1,
            total:'',
            pageSize:10,
            addBook:false,
            name:'',
            author:'',
            isVip:'',
            description:'',
            publish:'',
            type:'',
            updateBook:false,
            form:{
                id:'',
                name:'',
                author:'',
                isVip:'',
                description:'',
                publish:'',
                type:'',
            }
        }
    },
    methods:{
        open(row){
            this.updateBook = true
            this.form.id = row.id
            this.form.name = row.name
            this.form.author = row.author
            this.form.isVip = row.isVip === '是' ? 1:0
            this.form.description = row.description
            this.form.publish = row.publish
            this.form.type = row.type
        },
        deleteBook(row){
            let jwt = sessionStorage.getItem("jwtToken")
            this.$confirm('此操作将删这本书，是否确定?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                    axios.get("http://localhost:8082/api/book/deleteBook",
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
        submitUpdate(){
            let jwt = sessionStorage.getItem("jwtToken")
            axios.post("http://localhost:8082/api/book/updateBook",
              {
                id:this.form.id,
                name:this.form.name,
                author:this.form.author,
                isVip:this.form.isVip,
                description:this.form.description,
                publish:this.form.publish,
                type:this.form.type
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
              if(res.data.code===200){
                 window.alert("修改成功")
                 this.updateBook=false
                 location.reload()
              }
          }).catch(error=>{
           
              error
              
          })
        },
        submitAdd(){
            let jwt = sessionStorage.getItem("jwtToken")
            axios.post("http://localhost:8082/api/book/addBook",
              {
                name:this.name,
                author:this.author,
                isVip:this.isVip,
                description:this.description,
                publish:this.publish,
                type:this.type
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
              if(res.data.code===200){
                 window.alert("添加成功")
                 this.addBook=false
                 location.reload()
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