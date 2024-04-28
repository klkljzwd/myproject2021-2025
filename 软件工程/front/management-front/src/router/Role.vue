<template>
  <div>
    <div>
        <el-input placeholder="请输入角色名" style="width: 250px;margin-left: 10px;" v-model="input" prefix-icon="el-icon-search"></el-input>
        <el-button style="margin: 5px; height: 39px;" type="primary" @click="searchRole" >搜索</el-button>
    </div>
    <div style="margin: 10px;">
        <el-button type="primary" icon="el-icon-plus" style="padding: 8px;width: 90px;" @click="addRoleBool=true">新增</el-button>
        
    </div>
    <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="角色id" width="150">
        </el-table-column>
        <el-table-column prop="roleName" label="角色名" width="140">
        </el-table-column>
        <el-table-column prop="description" label="角色描述" width="500">
        </el-table-column>
        <el-table-column>
            <template slot-scope="scope">
                <el-button type="success" style="padding:6px;" icon="el-icon-edit" @click="open(scope.row)">编辑</el-button>
                <el-button type="danger" style="padding:6px;" icon="el-icon-delete" @click="deleteRole(scope.row)">删除</el-button>
                <el-button type="primary" style="padding:6px;" icon="el-icon-edit" @click="bindAuth(scope.row)">修改绑定的权限</el-button>
            </template>
        </el-table-column>

        <el-dialog title="修改" :visible.sync="dialogFormVisible" :append-to-body="true">
            <el-form :model="form">
                <el-form-item label="角色名称" :label-width="formLabelWidth">
                    <el-input v-model="form.roleName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="角色详情" :label-width="formLabelWidth">
                    <el-input v-model="form.description" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitUpdate">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="新增" :visible.sync="addRoleBool" :append-to-body="true">
            <el-form :model="form">
                <el-form-item label="角色名称" :label-width="formLabelWidth">
                    <el-input v-model="roleNameAdd" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="角色详情" :label-width="formLabelWidth">
                    <el-input v-model="descriptionAdd" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addRoleBool = false">取 消</el-button>
                <el-button type="primary" @click="submitAdd">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="绑定权限" :visible.sync="bindAuthBool" :append-to-body="true">
            <h4>拥有的权限：</h4>
            <el-table :data="roleAuthData">
                <el-table-column prop="id" label="权限id" width="100">
                </el-table-column>
                <el-table-column prop="resource" label="权限名" width="200">
                </el-table-column>
                <el-table-column prop="status" label="权限状态(1有效/0无效)" width="200">
                </el-table-column>
                <el-table-column prop="level" label="权限等级" width="100">
                </el-table-column>
                <el-table-column>
                    <template slot-scope="scope">
                        <el-button type="danger" style="padding:6px;" icon="el-icon-delete" @click="removeAuth(scope.row)">移除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <h4>未拥有的权限：</h4>
            <el-table :data="roleAuthDataNot">
                <el-table-column prop="id" label="权限id" width="100">
                </el-table-column>
                <el-table-column prop="resource" label="权限名" width="200">
                </el-table-column>
                <el-table-column prop="status" label="权限状态(1有效/0无效)" width="200">
                </el-table-column>
                <el-table-column prop="level" label="权限等级" width="100">
                </el-table-column>
                <el-table-column>
                    <template slot-scope="scope">
                        <el-button type="primary" style="padding:6px;" icon="el-icon-edit" @click="addAuth(scope.row)">添加</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div slot="footer" class="dialog-footer">
                <el-button @click="bindAuthBool = false">取 消</el-button>
                <el-button type="primary" @click="submitBind">确 定</el-button>
            </div>
        </el-dialog>
    </el-table>
  </div>
</template>

<script>
import axios from 'axios';
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
            roleName:'',
            description:'',
            id:''
        },
        formLabelWidth: '120px',

        addRoleBool: false,
        roleNameAdd: '',
        descriptionAdd:'',

        bindAuthBool:false,
        roleAuthData:'',
        roleAuthDataNot:'',
        roleId:''
      }
    },
    methods:{
        open(row){
            this.dialogFormVisible = true
            this.form.roleName = row.roleName
            this.form.description = row.description
            this.form.id=row.id
        },

        bindAuth(row){
            let jwt = sessionStorage.getItem("jwtToken")
            this.roleId = row.id
            axios.get("http://localhost:8082/api/role/bind",
                {
                    headers: {
                        "authorization":jwt
                    },
                    params:{
                        "id":row.id
                    },
                }
            ).then(res=>{
                if(res.data.code===402){
                        window.alert("权限不足")
                        return
                    }
                if(res.data.code===401){
                    window.alert(res.data.msg)
                }
                if(res.data.code===405){
                    window.alert(res.data.msg)
                }
                if(res.data.code===200){
                    this.roleAuthData = res.data.data.authorities
                    this.roleAuthDataNot = res.data.data.authoritiesNot
                    this.bindAuthBool=true
                }
                
            }).catch(error=>{
                error
                
            })
        },
        removeAuth(row){

            this.$confirm('此操作将移除该角色的权限，是否确定?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let jwt = sessionStorage.getItem("jwtToken")
            axios.get("http://localhost:8082/api/role/deleteAuth",
                {
                    headers: {
                        "authorization":jwt
                    },
                    params:{
                        "roleId":this.roleId,
                        "authId":row.id
                    },
                }
            ).then(res=>{
                if(res.data.code===402){
                        window.alert("权限不足")
                        return
                    }
                if(res.data.code===401){
                    window.alert(res.data.msg)
                    return
                }
                if(res.data.code===405){
                    window.alert(res.data.msg)
                    return
                }
                window.alert("修改成功")
                location.reload()
            }).catch(error=>{
                error
                
            })
            
            }).catch(() => {
                    
            });
        },

        addAuth(row){
            this.$confirm('此操作将该权限添加到该角色，是否确定?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let jwt = sessionStorage.getItem("jwtToken")
            axios.get("http://localhost:8082/api/role/addAuth",
                {
                    headers: {
                        "authorization":jwt
                    },
                    params:{
                        "roleId":this.roleId,
                        "authId":row.id
                    },
                }
            ).then(res=>{
                if(res.data.code===402){
                        window.alert("权限不足")
                        return
                    }
                if(res.data.code===401){
                    window.alert(res.data.msg)
                    return
                }
                if(res.data.code===405){
                    window.alert(res.data.msg)
                    return
                }
                window.alert("添加成功")
                location.reload()
            }).catch(error=>{
                error
                
            })
            this.$message({
                type: 'success',
                message: '添加成功!'
            });
            }).catch(() => {
               
            });
        },
        submitAdd(){
            let jwt = sessionStorage.getItem("jwtToken")
            axios.post("http://localhost:8082/api/role/add",
                {
                    "roleName":this.roleNameAdd,
                    "description":this.descriptionAdd
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
            const newName = this.form.roleName
            const newDes = this.form.description
            const id = this.form.id
            let jwt = sessionStorage.getItem("jwtToken")

            axios.post("http://localhost:8082/api/role/update",
                {
                    "id":id,
                    "roleName":newName,
                    "description":newDes
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
            const id = row.id
            this.$confirm('此操作将永久删除该角色，是否确定?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                    axios.get("http://localhost:8082/api/role/delete",
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
            
        },
        
    },
    created(){
        const instance = axios.create({
            baseURL:'',
            timeout:5000
        })
        let jwt = sessionStorage.getItem("jwtToken")
        
        instance.get("http://localhost:8082/api/role",{
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