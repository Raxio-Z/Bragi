<template>
  <div>
    <el-container>

      <el-container>
        <el-main class="editor" style="overflow: unset">
          <Editor ref="editor"
                  @showHistory="handleShowHistory"
                  @saveContent="handleSaveContent" @renameTitle="doHandleRenameTitle"/>
        </el-main>
      </el-container>
    </el-container>

    <!-- 笔记右键菜单 -->
  </div>


</template>

<script>
import Editor from '@/components/Editor.vue'
// import HistoryPreview from '@/components/HistoryPreview.vue'
import global from "@/global";
import axios from "axios";
// import global from '@/global'
// import util from '@/js/util'

export default {
  name: 'Home',
  components: {
    Editor,
    // HistoryPreview
  },
  // props: {
  //   noteTitle: {
  //     required: true,
  //   },
  //   content:{
  //     required: true,
  //   },
  //   notebookName:{
  //     required: true,
  //   }
  // },
  data() {
    return {
      pageTitle: 'Bragi',

      config: {
        headers: {
          token: this.$store.getters.getToken,
          username: this.$store.getters.getUsername
        }
      },

    }
  },
  methods: {
    doHandleRenameTitle(newTitle) {
      let targetNotebook = this.curNotebook.notebookName;
      let url = global.HOST_URL + "/note/" + targetNotebook + "/" + newTitle;
      let param = {
        srcNotebook: targetNotebook,
        srcTitle: this.curNote.noteTitle,
        move: true
      }
      axios.put(url, param, this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          this.refreshNotebookList(this.curNotebook.notebookName);
        } else {
          this.$notify({
            type: 'warning',
            message: res.msg
          });
        }
      })

    },
// 保存笔记
    handleSaveContent(content, noteTitle, notebookName) {
      this.showHistory = false;
      let request = {
        content: content
      }
      let url = global.HOST_URL + "/note/" + notebookName + "/" + noteTitle;
      axios.post(url, request, this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          this.curNote.content = content;
          this.refreshNotebookList();
        }


      })
    },
    handleShowHistory() {
      this.showHistory = !this.showHistory;
      let url = global.HOST_URL + "/note/" + this.curNotebook.notebookName + "/" + this.curNote.noteTitle + "/history";
      axios.get(url, this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          this.curNoteVersion = res.data;
          this.curRef = res.data[0].ref
        }
      })
    },
  },
  mounted() {
    // document.title = this.$store.getters.getWebsiteTitle;
    // this.validateUserAndInit();

    let url = global.HOST_URL + "/note/" + this.$route.params.notebookName + "/" + this.$route.params.noteTitle;

    console.log('预备！')

    axios.get(url, this.config).then(res => {
      res = res.data;
      if (res.code === 0) {

        console.log('准备给内容了！')
        console.log(this.$route.params.noteTitle,res.data)

        this.$refs.editor.setContent(this.$route.params.noteTitle,res.data,this.$route.params.notebookName)
      }
      else{
        console.log('出错了')
      }
    })
  },
  beforeRouteUpdate(to, from, next) {
    // 获取新的路由参数
    const { notebookName, noteTitle } = to.params
    // 如果新的参数与原来的不同，更新页面内容
    if (notebookName !== this.notebookName || noteTitle !== this.noteTitle) {
      // 根据新的路由参数更新页面内容
      let url = global.HOST_URL + "/note/" + notebookName + "/" + noteTitle;

      console.log('预备！')

      axios.get(url, this.config).then(res => {
        res = res.data;
        if (res.code === 0) {

          console.log('准备给内容了！')

          console.log(this.$route.params.noteTitle,res.data)


          this.$refs.editor.setContent(this.$route.params.noteTitle,res.data,this.$route.params.notebookName)
        }
        else{
          console.log('出错了')
        }
      })
    }
    // 继续路由跳转
    next()
  },
  beforeRouteLeave(to, from, next) {
    clearInterval(this.timerId)
    next()
  }
}
</script>
<style>
.notebook {
  /* margin: 15px; */
  color: rgb(41, 38, 38);
  font-size: 18px;
  margin-top: 1px;
  margin-left: 10px;
  margin-right: 10px;
  padding: 10px;
  padding-top: 15px;
  padding-bottom: 15px;
  /* background-color: rgb(247, 244, 242); */

  /* border-bottom:1px solid rgb(240, 237, 237); */
}

.createNotebook {
  text-align: center;
  border: 1px solid rgb(240, 237, 237);
  border-radius: 7px;
  margin-top: 5px;
  padding-top: 10px;
  padding-bottom: 10px;
  background-color: rgb(247, 245, 240);


}

.createNote {
  text-align: left;
  padding: 5px;

  font-weight: lighter;
  font-size: 16px;
  background-color: rgb(247, 245, 240);
  border: 1px solid rgb(240, 237, 237);


}

.notebookInfo {
  margin-top: 5px;
  /* background-color: rgb(255, 253, 246); */
  padding: 10px;
  font-size: 26px;
  /* font-weight: bold; */
}

.notebooklist {
  /* height:200px; */
  background-color: rgb(248, 248, 248);

  /* border: 1px solid rgb(240, 237, 237); */

}

.chosenNotebook {
  background-color: rgb(252, 251, 251);
  border-left: 3px solid rgb(199, 199, 199);
}

.note {
  padding: 10px;

}

.notetitle {
  /* font-weight: bold; */
  margin-bottom: 5px;
}

.header {
  /* margin-top: 10px; */
  /* padding-top: 10px; */
  color: rgb(24, 21, 17);
  /* line-height: 52px; */
  font-size: 25px;
  display: table-cell;
  vertical-align: bottom;
  /* border-top-left-radius:5px;
    border-top-right-radius:5px; */
  /* border: 1px solid rgb(240, 237, 237); */


  /* background-color: rgb(252, 250, 250); */

}

.editor {
  /* border: 1px solid rgb(240, 237, 237); */

}

.noteList {
  /* border: 1px solid rgb(240, 237, 237); */
  background-color: rgb(251, 250, 250);

}

.rightMenu {
  min-width: 100px;
}

.delnote {
  /* margin: 5px 15px 5px 15px; */
  padding: 6px 16px 6px 20px;
  color: gray;
  font-size: 15px;
  background-color: rgb(251, 250, 250);

  /* border-bottom:1px solid rgb(240, 237, 237); */
  /* border-top:1px solid rgb(240, 237, 237); */

}

.lightGreyBackground {
  background-color: lightgray;
}

.markidea-dropdown-item {
  min-width: 45px;
  text-align: center;
}

.chosenNote {
  background-color: white;
  /* border-left: 3px solid rgb(199, 199, 199); */
}

.notePreview {
  font-size: 14px;
  color: rgb(87, 87, 87);
}

</style>
