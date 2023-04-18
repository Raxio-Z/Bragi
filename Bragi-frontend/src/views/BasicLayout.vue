<template>
  <el-container style="height: 100%;">
    <el-aside>
      <el-row class="tac">
        <el-col :span="12">
          <div>
            <h1 style="font-size: 1.6rem;">📕Bragi</h1>
          </div>

          <el-menu
              default-active="2"
              class="el-menu-vertical-demo"
              @open="handleOpen"
              @close="handleClose">
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-location"></i>
                <span>文件空间</span>

              </template>
              <!--              <router-link-->
              <!--                  :to="{name:'content'}"-->
              <!--                  style="text-decoration: none;">-->
              <div>
                <el-menu-item-group>
                  <div v-for="(item,index) of this.notebookList" :key="item.notebookName">
                    <el-submenu :index="'1-' + (index + 1) ">
                      <template slot="title">{{ item.notebookName }}</template>
                      <div v-for="subitem in item.noteList"
                           :key="subitem.title">
                        <el-menu-item
                            :index="'1-' + (index + 1) + '-' + (subitem.index + 1)"
                            @click="selectNote(subitem.title, item.notebookName)">
                          {{ subitem.title }}
                        </el-menu-item>

                      </div>
                    </el-submenu>
                  </div>
                </el-menu-item-group>
              </div>
              <!--              </router-link>-->

            </el-submenu>
            <el-menu-item index="2">
              <i class="el-icon-menu"></i>
              <span slot="title">回收站</span>
            </el-menu-item>
            <el-menu-item index="3">
              <i class="el-icon-document"></i>
              <span slot="title">思维导图</span>
            </el-menu-item>
            <el-menu-item index="4">

              <router-link :to="{name:'setting'}" style="text-decoration: none;">
                <div>
                  <i class="el-icon-setting"></i>
                  <span slot="title">设置</span>
                </div>
              </router-link>

            </el-menu-item>
            <el-menu-item index="5">
              <div @click="handleLogout()">
                <i class="el-icon-switch-button"></i>
                <span slot="title">退出登录</span>
              </div>

            </el-menu-item>
          </el-menu>
        </el-col>
      </el-row>
    </el-aside>

    <el-main>
      <div class="mid-content">
        <router-view></router-view>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import axios from "axios";
import global from "@/global";
//import content from "@/views/Content";
// import Editor from "@/components/Editor";

export default {
  name: "navbar",
  components: {
    // Editor
  },
  data() {
    return {
      config: {
        headers: {
          token: this.$store.getters.getToken,
          username: this.$store.getters.getUsername
        }
      },
      // 被删除笔记列表
      delNoteList: [],
      // 用于存放右键菜单选中的笔记信息
      noteRightMenuValues: {},
      // 是否展示关于
      showAboutPage: false,

      newNoteTitle: "",
      curNotebook: {
        notebookName: null,
        noteList: []
      },
      curNote: {
        notebookName: null,
        noteTitle: null,
        content: ""
      },
      curNoteVersion: [],
      curRef: null,

      notebookList: [],
      noteList: []

    }

  },
  mounted() {
    this.refreshNotebookList()
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    refreshNotebookList() {
      // this.clearRenameInfo();
      axios.get(global.HOST_URL + "/note", this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          this.notebookList = res.data;

          console.log(this.notebookList)

          // if (notebookName) {
          //   this.doSwitchNotebook(notebookName);
          //   return;
          // }
          // this.updateCurNotebookInfo()
        } else {
          console.log('note error')
        }
      })
      axios.get(global.HOST_URL + "/delnote", this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          this.delNoteList = res.data;
        }
      })
    },
    doSwitchNotebook(notebookName) {
      for (const notebook of this.notebookList) {
        if (notebook.notebookName === notebookName) {
          this.curNotebook = notebook;
          if (notebook.noteList && notebook.noteList.length > 0) {
            this.doSwitchNote(notebook.noteList[0].title, notebookName, true);
          } else {
            this.clearCurNoteInfo();
          }
          return;
        }
      }
    },
    updateCurNotebookInfo() {
      for (const notebook of this.notebookList) {
        if (notebook.notebookName === this.curNotebook.notebookName) {
          this.curNotebook = notebook;
        }
      }
    },
    clearCurNoteInfo() {
      this.curNote = {
        content: ""
      };
      this.$refs.editor.setContent(null, "", null);

    },
    clearCurNotebookInfo() {
      this.curNotebook = {};
    },
    selectNote(noteTitle, notebookName) {

      // 同一个笔记  不用动
      if (noteTitle === this.curNote.noteTitle && notebookName === this.curNote.notebookName) {
        return;
      }

      // // 判断是否有未保存的内容
      // if (this.isModifUnsaved()) {
      //   this.$confirm('修改尚未保存', 'Confirm', {
      //     distinguishCancelAndClose: true,
      //     confirmButtonText: '保存',
      //     cancelButtonText: '丢弃'
      //   }).then(() => {
      //
      //     this.saveContentAndSwitchNote(this.$refs.editor.getContent(), notebookName, noteTitle);
      //   }).catch(
      //       action => {
      //         this.$notify({
      //           type: action === 'cancel' ? 'warning' : 'info',
      //           message: action === 'cancel'
      //               ? '丢弃修改'
      //               : '停留在当前页',
      //           duration: 1500
      //         });
      //         if (action === 'cancel') {
      //           this.handleDelTmpNote();
      //
      //           this.doSwitchNote(noteTitle, notebookName);
      //         }
      //       }
      //   )
      // } else {
      this.doSwitchNote(noteTitle, notebookName);

      console.log('2',this.curNote)




      //}
    },
    doSwitchNote(noteTitle, notebookName) {

      let url = global.HOST_URL + "/note/" + notebookName + "/" + noteTitle;
      axios.get(url, this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          //this.showHistory = false;
          let newCurNote = {
            noteTitle: noteTitle,
            content: res.data,
            notebookName: notebookName
          }
          this.curNoteVersion = [];
          this.curRef = null
          this.curNote = newCurNote;

          console.log('1',this.curNote)

          const notebookName_temp = this.curNote.notebookName
          const noteTitle_temp = this.curNote.noteTitle
          // const content_temp = this.curNote.content

          this.$router.push({
            name: 'content',
            params: {
              notebookName:notebookName_temp,
              noteTitle:noteTitle_temp
            },
          })

          //this.$refs.editor.setContent(noteTitle, res.data, notebookName);
          // 如果是移动端
          // if (this.isMobile && !auto) {
          //   this.showAside = false;
          // }
        }
      })

    },
    handleSaveContentAndSwitchNotebook(content, notebookName) {
      let request = {
        content: content
      }
      let url = global.HOST_URL + "/note/" + this.curNote.notebookName + "/" + this.curNote.noteTitle;
      axios.post(url, request, this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          this.curNote.content = content;
          this.refreshNotebookList(notebookName);
        }


      })
    },
    saveContentAndSwitchNote(content, targetNotebookName, targetNoteTitle) {
      let request = {
        content: content
      }
      let url = global.HOST_URL + "/note/" + this.curNote.notebookName + "/" + this.curNote.noteTitle;
      axios.post(url, request, this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          this.doSwitchNote(targetNoteTitle, targetNotebookName);
          this.refreshNotebookList();

        }

      })
    },
    // 注销登录
    handleLogout() {

      console.log('想要退出登录')

      let url = global.HOST_URL + "/user/logout";
      axios.post(url, null, this.config);
      this.$store.commit('setLocalInfo', {});

      this.$notify({
        type: "success",
        message: "退出登录成功",
      });

      setTimeout(() => {
        this.$router.push('/login');
      }, 500);
    },
  }
}
</script>

<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>