<!--Home页面部件-->
<template>
  <div>
    <!-- <div v-show="notebookName && title && title.length > 0" class="title">
            <span v-if="!showEditTitle" class="noselect">✏️ </span>
      <span v-if="!showEditTitle">{{ title }}</span>
      <span v-if="contentModfied"
            style="color:lightgrey;font-size:14px;padding-left:5px;font-weight:lighter">- 已编辑</span>

      <el-input @blur="setTitleEditable(false)" v-model="newTitle" v-if="showEditTitle"
                @keyup.enter.native="renameTitle"/>

    </div> -->

    <div v-show="notebookName && title && title.length > 0" id="vditor" class="vditor"></div>

    <el-dialog :visible.sync="searchVisible">
      <div style="display: flex;">
        <el-input v-model="searchnote_search_input" prefix-icon="el-icon-search" placeholder="选择页面，创建快速跳转"></el-input>
      </div>
      <el-divider></el-divider>

      <div style="height: 350px;overflow-y: scroll;">
        <div v-for="item of searchNoteList_show" :key="item.noteId" class="trash_content">
          <div class="trash_item" style="display: flex; align-items: center; height: 30px"
               @click="jump_swift(item.noteId,item.title)">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="24" fill="none" viewBox="0 0 24 24">
              <path fill="#2C2C2C" fill-rule="evenodd"
                    d="M18 3.2a2.8 2.8 0 012.795 2.63L20.8 6v12a2.8 2.8 0 01-2.63 2.795L18 20.8H6a2.8 2.8 0 01-2.795-2.63L3.2 18V6a2.8 2.8 0 012.63-2.795L6 3.2h12zm0 1.6H6a1.2 1.2 0 00-1.194 1.078L4.8 6v12a1.2 1.2 0 001.078 1.194L6 19.2h12a1.2 1.2 0 001.194-1.077L19.2 18V6a1.2 1.2 0 00-1.077-1.194L18 4.8z"
                    clip-rule="evenodd"></path>
              <path fill="#2C2C2C" fill-rule="evenodd"
                    d="M16.627 14.841a.3.3 0 01-.3.3H7.21a.3.3 0 01-.3-.3v-1.2a.3.3 0 01.3-.3h9.116a.3.3 0 01.3.3v1.2zm-2.756-4.883a.3.3 0 01-.3.3h-6.36a.3.3 0 01-.3-.3v-1.2a.3.3 0 01.3-.3h6.36a.3.3 0 01.3.3v1.2z"
                    clip-rule="evenodd"></path>
            </svg>

            <span style="font-weight: bold;margin-left: 10px;flex-grow: 1;color: rgb(76,76,76)">{{
                item.notebookName
              }}/{{
                item.title
              }}</span>

            <div style="display: flex; align-items: center; justify-content: flex-end;">
              <svg viewBox="0 0 16 16" class="enter"
                   style="width: 14px; height: 14px; display: block; fill: rgba(55, 53, 47, 0.45); flex-shrink: 0; backface-visibility: hidden; opacity: 1;">
                <path
                    d="M5.38965 14.1667C5.81812 14.1667 6.10156 13.8767 6.10156 13.468C6.10156 13.2571 6.01587 13.0989 5.89062 12.967L4.18994 11.3125L3.02979 10.3369L4.55908 10.4028H12.7922C14.4402 10.4028 15.1389 9.65796 15.1389 8.04297V4.13403C15.1389 2.48608 14.4402 1.78735 12.7922 1.78735H9.13379C8.70532 1.78735 8.4021 2.11035 8.4021 2.50586C8.4021 2.90137 8.69873 3.22437 9.13379 3.22437H12.7593C13.4316 3.22437 13.7151 3.50781 13.7151 4.17358V7.99683C13.7151 8.67578 13.425 8.95923 12.7593 8.95923H4.55908L3.02979 9.03174L4.18994 8.04956L5.89062 6.39502C6.01587 6.26978 6.10156 6.11157 6.10156 5.89404C6.10156 5.48535 5.81812 5.19531 5.38965 5.19531C5.21167 5.19531 5.01392 5.27441 4.8689 5.41943L1.08521 9.1438C0.933594 9.28882 0.854492 9.48657 0.854492 9.68433C0.854492 9.87549 0.933594 10.0732 1.08521 10.2183L4.8689 13.9492C5.01392 14.0876 5.21167 14.1667 5.38965 14.1667Z"></path>
              </svg>
              <!--                    </el-tooltip>-->
            </div>

          </div>

        </div>
      </div>

    </el-dialog>

    <el-dialog :visible.sync="deljumpVisible">
      <div style="display: flex;">
        <el-input v-model="deljump_search_input" prefix-icon="el-icon-search" placeholder="选择引用，进行删除"></el-input>
      </div>
      <el-divider></el-divider>

      <div style="height: 350px;overflow-y: scroll;">
        <div v-for="item of delRefList_show" :key="item.articleId" class="trash_content">
          <div class="trash_item" style="display: flex; align-items: center; height: 30px" @click="delRef_swift(item.noteId)">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="24" fill="none" viewBox="0 0 24 24">
              <path fill="#2C2C2C" fill-rule="evenodd"
                    d="M18 3.2a2.8 2.8 0 012.795 2.63L20.8 6v12a2.8 2.8 0 01-2.63 2.795L18 20.8H6a2.8 2.8 0 01-2.795-2.63L3.2 18V6a2.8 2.8 0 012.63-2.795L6 3.2h12zm0 1.6H6a1.2 1.2 0 00-1.194 1.078L4.8 6v12a1.2 1.2 0 001.078 1.194L6 19.2h12a1.2 1.2 0 001.194-1.077L19.2 18V6a1.2 1.2 0 00-1.077-1.194L18 4.8z"
                    clip-rule="evenodd"></path>
              <path fill="#2C2C2C" fill-rule="evenodd"
                    d="M16.627 14.841a.3.3 0 01-.3.3H7.21a.3.3 0 01-.3-.3v-1.2a.3.3 0 01.3-.3h9.116a.3.3 0 01.3.3v1.2zm-2.756-4.883a.3.3 0 01-.3.3h-6.36a.3.3 0 01-.3-.3v-1.2a.3.3 0 01.3-.3h6.36a.3.3 0 01.3.3v1.2z"
                    clip-rule="evenodd"></path>
            </svg>

            <span style="font-weight: bold;margin-left: 10px;flex-grow: 1;color: rgb(76,76,76)">{{ item.notebookName }}/{{ item.title
              }}</span>

            <div style="display: flex; align-items: center; justify-content: flex-end;">
              <svg viewBox="0 0 16 16" class="enter" style="width: 14px; height: 14px; display: block; fill: rgba(55, 53, 47, 0.45); flex-shrink: 0; backface-visibility: hidden; opacity: 1;">
                <path d="M5.38965 14.1667C5.81812 14.1667 6.10156 13.8767 6.10156 13.468C6.10156 13.2571 6.01587 13.0989 5.89062 12.967L4.18994 11.3125L3.02979 10.3369L4.55908 10.4028H12.7922C14.4402 10.4028 15.1389 9.65796 15.1389 8.04297V4.13403C15.1389 2.48608 14.4402 1.78735 12.7922 1.78735H9.13379C8.70532 1.78735 8.4021 2.11035 8.4021 2.50586C8.4021 2.90137 8.69873 3.22437 9.13379 3.22437H12.7593C13.4316 3.22437 13.7151 3.50781 13.7151 4.17358V7.99683C13.7151 8.67578 13.425 8.95923 12.7593 8.95923H4.55908L3.02979 9.03174L4.18994 8.04956L5.89062 6.39502C6.01587 6.26978 6.10156 6.11157 6.10156 5.89404C6.10156 5.48535 5.81812 5.19531 5.38965 5.19531C5.21167 5.19531 5.01392 5.27441 4.8689 5.41943L1.08521 9.1438C0.933594 9.28882 0.854492 9.48657 0.854492 9.68433C0.854492 9.87549 0.933594 10.0732 1.08521 10.2183L4.8689 13.9492C5.01392 14.0876 5.21167 14.1667 5.38965 14.1667Z"></path>
              </svg>
              <!--                    </el-tooltip>-->
            </div>

          </div>

        </div>
      </div>

    </el-dialog>


  </div>
</template>

<script>
import Vditor from 'vditor'
import global from '@/global'
import axios from 'axios'
// import Lute from "vditor";


export default {
  name: 'Editor',
  data() {
    return {
      showEditTitle: false,
      config: {
        headers: {
          token: this.$store.getters.getToken,
          username: this.$store.getters.getUsername
        }
      },
      editorConfig: this.$store.getters.getEditorConfig,
      contentModfied: false,
      notebookName: null,
      originContent: "",
      title: "",

      searchVisible: false,
      searchnote_search_input: '',
      searchNoteList_show_temp: [],
      notebookList: [],

      deljumpVisible: false,
      deljump_search_input: '',
      deljumpList_show_temp:[],
      deljump_List:[],

      last_one:"",

      newTitle: null,
      vditor: null,
      toolbar: [
        {
          hotkey: "⌘H",
          icon: "<i  class='fa fa-header fa-lg'></i>",
          name: "headings",
          tipPosition: "ne",
        },
        {
          hotkey: "⌘B",
          name: "bold",
          prefix: "**",
          suffix: "**",
          tipPosition: "ne",
          icon: "<i class='fa fa-bold fa-lg'></i>"
        },
        {
          hotkey: "⌘I",
          icon: "<i class='fa fa-italic fa-lg'></i>",
          name: "italic",
          prefix: "*",
          suffix: "*",
          tipPosition: "ne",
        },
        {
          hotkey: "⌘L",
          icon: "<i class='fa fa-strikethrough fa-lg'></i>",
          name: "strike",
          prefix: "~~",
          suffix: "~~",
          tipPosition: "ne",
        },
        {
          hotkey: "⌘K",
          icon: "<i class='fa fa-link fa-lg'></i>",
          name: "link",
          prefix: "[",
          suffix: "](https://)",
          tipPosition: "n",
        }, "table",
        "|",
        {
          // hotkey: "⌘L",
          icon: "<i class='fa fa-list-ul fa-lg'></i>",
          name: "list",
          prefix: "* ",
          tipPosition: "n",
        },
        {
          hotkey: "⌘O",
          icon: "<i class='fa fa-list-ol fa-lg'></i>",
          name: "ordered-list",
          prefix: "1. ",
          tipPosition: "n",
        },
        {
          hotkey: "⌘J",
          icon: "<i class='fa fa-check-square-o fa-lg'></i>",
          name: "check",
          prefix: "* [ ] ",
          tipPosition: "n",
        },
        {
          hotkey: "⌘⇧I",
          icon: "<i class='fa fa-outdent fa-lg'></i>",
          name: "outdent",
          tipPosition: "n",
        }, {
          hotkey: "⌘⇧O",
          icon: "<i class='fa fa-indent fa-lg'></i>",
          name: "indent",
          tipPosition: "n",
        },

        {
          hotkey: "⌘Q",
          icon: "<i class='el-icon-top-right'></i>",
          name: "jump",
          prefix: "[",
          suffix: "](https://)",
          tipPosition: "n",
          tip: '笔记跳转',
          click: () => {
            this.searchVisible = true;
            this.get_allNote()
          }
        },
        {
          hotkey: "⌘D",
          icon: "<i class='el-icon-delete'></i>",
          name: "deljump",
          // prefix: "[",
          // suffix: "](https://)",
          tipPosition: "n",
          tip: '删除笔记跳转',
          click: () => {
            this.deljumpVisible=true;
            this.get_allReference()
          }
        },
        "|",
        {
          hotkey: "⌘;",
          icon: "<i class='fa fa-quote-left fa-lg'></i>",
          name: "quote",
          prefix: "> ",
          tipPosition: "n",
        },

        "line",
        "code",
        "inline-code",
        "|",
        "upload",
        "undo",
        "redo",
        //  {
        //   name: "enter",
        //   tip: "换行",
        //   icon: '<svg version="1.1" id="layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 16 16" style="enable-background:new 0 0 16 16;" xml:space="preserve"><style type="text/css">.st0{fill:#2C2C2C;}</style><path class="st0" d="M0.7,0.6c0-0.3,0.2-0.5,0.5-0.5c0,0,0,0,0,0h14.1c0.3,0,0.5,0.2,0.5,0.5c0,0,0,0,0,0v0.1c0,0.3-0.2,0.5-0.5,0.5l0,0H1.2C0.9,1.1,0.7,0.9,0.7,0.6C0.7,0.7,0.7,0.6,0.7,0.6z M0.7,4.6c0-0.3,0.2-0.5,0.5-0.5h14.1c0.3,0,0.5,0.2,0.5,0.5v0v0.1c0,0.3-0.2,0.5-0.5,0.5l0,0H1.2C0.9,5.1,0.7,4.9,0.7,4.6C0.7,4.7,0.7,4.6,0.7,4.6z M0.7,8.6c0-0.3,0.2-0.5,0.5-0.5c0,0,0,0,0,0h12c0.3,0,0.5,0.2,0.5,0.5v0v0.1c0,0.3-0.2,0.5-0.5,0.5h0h-12C0.9,9.2,0.7,8.9,0.7,8.6L0.7,8.6C0.7,8.7,0.7,8.6,0.7,8.6z M0.7,12.6c0-0.3,0.2-0.5,0.5-0.5c0,0,0,0,0,0h5.5c0.3,0,0.5,0.2,0.5,0.5l0,0v0.1c0,0.3-0.2,0.5-0.5,0.5h0H1.2C0.9,13.2,0.7,13,0.7,12.6L0.7,12.6C0.7,12.7,0.7,12.6,0.7,12.6z"/><path class="st0" d="M15.2,8.1c-0.3,0-0.5,0.2-0.5,0.5v0v3.1c0,0.3-0.2,0.5-0.5,0.5h0h-2.8v-2l-2.8,2.1c-0.2,0.2-0.2,0.5-0.1,0.7c0,0,0.1,0.1,0.1,0.1l2.8,2.1v-2h3.8c0.3,0,0.5-0.2,0.5-0.5v0V8.6c0-0.3-0.2-0.5-0.5-0.5h0C15.3,8.1,15.2,8.1,15.2,8.1z"/></svg>',
        //   click: () => {
        //     this.vditor.insertValue('\n<br/>\n');
        //   }
        // },
        "|",
        "edit-mode",
        "outline",
        {
          hotkey: '⌘s',
          name: "save",
          tip: "保存",
          icon: '<i class="fa fa-save fa-lg"/>',
          click: () => {
            this.child_saveContent(this.vditor.getValue());
          }
        },
        {
          name: "more",
          toolbar: [
            // "content-theme",
            "export",
            "preview",
            {
              name: "share",
              tip: "公开",
              icon: '公开',
              click: () => {
                this.publishNote();
              }
            }
          ],
        }
      ],
      refPath: '',
      link_svg: '<svg style="width: 15px;height: auto" t="1683352845036" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2394" width="200" height="200"><path d="M607.934444 417.856853c-6.179746-6.1777-12.766768-11.746532-19.554358-16.910135l-0.01228 0.011256c-6.986111-6.719028-16.47216-10.857279-26.930349-10.857279-21.464871 0-38.864146 17.400299-38.864146 38.864146 0 9.497305 3.411703 18.196431 9.071609 24.947182l-0.001023 0c0.001023 0.001023 0.00307 0.00307 0.005117 0.004093 2.718925 3.242857 5.953595 6.03853 9.585309 8.251941 3.664459 3.021823 7.261381 5.997598 10.624988 9.361205l3.203972 3.204995c40.279379 40.229237 28.254507 109.539812-12.024871 149.820214L371.157763 796.383956c-40.278355 40.229237-105.761766 40.229237-146.042167 0l-3.229554-3.231601c-40.281425-40.278355-40.281425-105.809861 0-145.991002l75.93546-75.909877c9.742898-7.733125 15.997346-19.668968 15.997346-33.072233 0-23.312962-18.898419-42.211381-42.211381-42.211381-8.797363 0-16.963347 2.693342-23.725354 7.297197-0.021489-0.045025-0.044002-0.088004-0.066515-0.134053l-0.809435 0.757247c-2.989077 2.148943-5.691629 4.669346-8.025791 7.510044l-78.913281 73.841775c-74.178443 74.229608-74.178443 195.632609 0 269.758863l3.203972 3.202948c74.178443 74.127278 195.529255 74.127278 269.707698 0l171.829484-171.880649c74.076112-74.17435 80.357166-191.184297 6.282077-265.311575L607.934444 417.856853z" fill="#5D5D5D" p-id="2395"></path><path d="M855.61957 165.804257l-3.203972-3.203972c-74.17742-74.178443-195.528232-74.178443-269.706675 0L410.87944 334.479911c-74.178443 74.178443-78.263481 181.296089-4.085038 255.522628l3.152806 3.104711c3.368724 3.367701 6.865361 6.54302 10.434653 9.588379 2.583848 2.885723 5.618974 5.355985 8.992815 7.309476 0.025583 0.020466 0.052189 0.041956 0.077771 0.062422l0.011256-0.010233c5.377474 3.092431 11.608386 4.870938 18.257829 4.870938 20.263509 0 36.68962-16.428158 36.68962-36.68962 0-5.719258-1.309832-11.132548-3.645017-15.95846l0 0c-4.850471-10.891048-13.930267-17.521049-20.210297-23.802102l-3.15383-3.102664c-40.278355-40.278355-24.982998-98.79612 15.295358-139.074476l171.930791-171.830507c40.179095-40.280402 105.685018-40.280402 145.965419 0l3.206018 3.152806c40.279379 40.281425 40.279379 105.838513 0 146.06775l-75.686796 75.737962c-10.296507 7.628748-16.97358 19.865443-16.97358 33.662681 0 23.12365 18.745946 41.87062 41.87062 41.87062 8.048303 0 15.563464-2.275833 21.944801-6.211469 0.048095 0.081864 0.093121 0.157589 0.141216 0.240477l1.173732-1.083681c3.616364-2.421142 6.828522-5.393847 9.529027-8.792247l79.766718-73.603345C929.798013 361.334535 929.798013 239.981676 855.61957 165.804257z" fill="#5D5D5D" p-id="2396"></path></svg>',
      // inline_fun:"alert('success')"
    }
  },

  computed: {

    searchNoteList_show: function () {
      if (this.searchnote_search_input === "") {
        return this.searchNoteList_show_temp
      }
      let ret = [];
      this.searchNoteList_show_temp.forEach((item) => {
        if (item.title.toLowerCase().indexOf(this.searchnote_search_input.toLowerCase()) !==
            -1) ret.push(item);
      });
      return ret;
    },
    delRefList_show: function () {
      if (this.deljump_search_input === "")
      {
        return this.deljumpList_show_temp
      }
      let ret = [];
      this.deljumpList_show_temp.forEach((item) => {
        if (item.title.toLowerCase().indexOf(this.deljump_search_input.toLowerCase()) !==
            -1) ret.push(item);
      });
      return ret;
    }
  },


  mounted() {
    this.init();
    this.vditor.clearCache();
  },
  created() {
    window.jumptoRef = this.jumptoRef;

    window.jumptoLink = this.jumptoLink
    // this.$nextTick(() => {
    //   window.addEventListener('keyup', (e) => {
    //     if (e.ctrlKey && event.keyCode === 83) { // 监听ctrl+s组合键
    //       e.preventDefault(); // 阻止浏览器默认行为
    //       console.log('--------------')
    //       this.child_saveContent()
    //     }
    //   });
    //
    // })
    let element = document.querySelector('#customEditorStyle');

    if (this.editorConfig.enableCustomStyle && this.editorConfig.customStylePath !== null && this.editorConfig.customStylePath.length > 5) {
      if (element) {
        element.href = this.editorConfig.customStylePath;
        return;
      }

      const link = document.createElement("link");
      link.href = this.editorConfig.customStylePath;
      link.rel = "stylesheet";
      link.id = "customEditorStyle"
      document.querySelector("head").appendChild(link)
    } else {
      if (element) {
        document.querySelector("head").removeChild(element)
      }

      import ("@/assets/vditor.css");
    }
  },
  methods: {
    jumptoRef(data) {
      // console.log(data)
      // console.log(data.getAttribute('value'))

      let value = data.getAttribute('value')
      value = value.split('&&')

      // console.log(value[1])
      let url = global.HOST_URL + "/note/path/" + value[1]
      axios.get(url, this.config).then(res => {
        res = res.data
        if (res.code == 0) {
          // console.log(res)
          let strList =  res.data.split('/')
          let notebookName = strList[1]
          let noteName = strList[2]
          this.handleJump(noteName,notebookName);
        }
      })


      // this.$router.push({
      //   name: 'content',
      //   params: {
      //     notebookName: notebookName,
      //     noteTitle: noteName
      //   },
      // })

      // this.handleJump(noteName,notebookName);
      // alert('success')
    },
    jumptoLink(data){
      console.log("data",data)
      let value = data.getAttribute('value')
      window.open(value, '_blank');
    },

    get_allNote() {
      axios.get(global.HOST_URL + "/note", this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          this.notebookList = res.data;
          this.compute_temp(this.notebookList)
        }
      })
    },
    get_allReference(){
      let req = {
        notebookName: this.notebookName,
        noteTitle: this.title
      }
      console.log(req)
      axios.post(global.HOST_URL + "/note/getnoteref", req, this.config).then(res => {
        res = res.data;

        if (res.code === 0) {
          this.deljump_List = res.data;
          this.compute_temp_del(this.deljump_List)
        }
      })

    },

    compute_temp(value) {
      //console.log('this.notebookList',value)
      this.searchNoteList_show_temp = []
      for (var i = 0; i < value.length; i++) {
        for (var j = 0; j < value[i].noteList.length; j++) {
          //console.log('j',value[0].noteList[j])
          if (!(value[i].noteList[j].title === this.$route.params.noteTitle && value[i].noteList[j].notebookName === this.$route.params.notebookName))
            this.searchNoteList_show_temp.push(value[i].noteList[j])
        }
      }

      //console.log('this.searchNoteList_show_temp',this.searchNoteList_show_temp)

    },
    jump_swift(noteId, noteTitle) {
      this.vditor.insertValue("[" + noteTitle + "](&&" + noteId + ")")
      // this.title
      // this.notebookTitle

      let req = {
        curNoteBookName: this.notebookName,
        curNoteTitle: this.title,
        refNoteId: noteId
      }

      axios.put(global.HOST_URL + "/note/refinsert",req,this.config).then(res=>{
        res = res.data
        if(res.code===0) {
          this.$notify({
            type: "success",
            message: "引用插入成功",
            duration: 10000
          })
        }
        else{
          this.$notify({
            type: "fail",
            message: "引用插入失败",
            duration: 10000
          })
        }
      })
      console.log(req)
      this.searchVisible = false
      this.child_saveContent(this.vditor.getValue());
    },
    compute_temp_del(value){
      //console.log('this.notebookList',value)
      this.deljumpList_show_temp=[]
      for(var i=0;i<value.length;i++)
      {
        for(var j=0;j<value[i].noteList.length;j++)
        {
          //console.log('j',value[0].noteList[j])
          if(!(value[i].noteList[j].title===this.$route.params.noteTitle &&  value[i].noteList[j].notebookName===this.$route.params.notebookName))
            this.deljumpList_show_temp.push(value[i].noteList[j])
        }
      }

      //console.log('this.searchNoteList_show_temp',this.searchNoteList_show_temp)
    },

    delRef_swift(noteId){

      let req = {
        notebookName: this.notebookName,
        noteTitle: this.title,
        delRefNoteId: noteId
      }
      console.log(req)
      axios.post(global.HOST_URL + "/note/delnoteref", req, this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          this.deljumpVisible=false
          //this.child_saveContent(this.vditor.getValue());
          let url = global.HOST_URL + "/note/" + this.notebookName + "/" + this.title;
          axios.get(url, this.config).then(res => {
            res = res.data;
            if (res.code === 0) {
              this.$notify({
                type: "success",
                message: "删除引用成功~",
                duration: 10000
              })
              console.log(res.data)
              //this.curNote.content = res.data
              //this.vditor.insertValue("")

              this.setContent(this.title, res.data, this.notebookName)
            } else {
              console.log('出错了')
            }
          })

        }
      })
    },

    init() {
      const options = {
        counter: {
          enable: this.editorConfig.enableCounter,
          type: 'text'
        },
        after: () => {
          // 设置大纲固定
          // let lute = new Lute();
          document.querySelector('.vditor-outline__content').classList.add("vditor-toolbar--pin")
          document.querySelector('.vditor-outline').classList.add("vditor-toolbar--pin")

          // console.log('在头上')
          // console.log(this.vditor)

          this.vditor.vditor.lute.SetJSRenderers({
            renderers: {
              Md2VditorIRDOM: {   // 请根据不同的模式选择不同的渲染对象
                renderLink: (node, entering) => {
                  if (entering) {
                    // console.log('enter renderLink')
                    return [``, 2]
                  } else {
                    // console.log('leave renderLink')
                    console.log(node.TokensStr())

                    console.log("this.refPath.substring(0,1)",this.refPath.substring(0,1))

                    if(this.refPath.substring(0,2)==="&&")
                      return [`<a style='color: magenta;font-weight: bold;text-decoration: none;cursor: pointer;' href="${this.refPath}" value="${this.refPath}" onclick="jumptoRef(this)">${node.Text()}</a>`, 2]
                    else
                      return [`<a style='color: dodgerblue;cursor: pointer' href="${this.refPath}" value="${this.refPath}" onclick="jumptoLink(this)">${node.Text()}</a>`, 2]
                  }

                },
                renderLinkDest: (node, entering) => {
                  if (entering) {
                    // console.log('enter renderLinkDest')
                    this.refPath = node.TokensStr()
                    return ['', 2]
                  } else {
                    // console.log('leave renderLinkDest')
                    return ['', 2]
                  }
                },
                renderLinkText: (node, entering) => {
                  if (entering) {
                    // console.log('enter renderLinkText')
                    return ['', 2]
                  } else {
                    // console.log('leave renderLinkText')
                    return ['', 2]
                  }
                },
                renderOpenBracket: (node, entering) => {
                  if (entering) {
                    // console.log('enter renderOpenBracket')
                    return ['', 2]
                  } else {
                    // console.log('leave renderOpenBracket')
                    return ['', 2]
                  }
                },
                renderCloseBracket: (node, entering) => {
                  if (entering) {
                    // console.log('enter renderCloseBracket')
                    return ['', 2]
                  } else {
                    // console.log('leave renderCloseBracket')
                    return ['', 2]
                  }
                },
                renderOpenParen: (node, entering) => {
                  if (entering) {
                    // console.log('enter renderOpenParen')
                    return ['', 2]
                  } else {
                    // console.log('leave renderOpenParen')
                    return ['', 2]
                  }
                },
                renderCloseParen: (node, entering) => {
                  if (entering) {
                    // console.log('enter renderCloseParen')
                    return ['', 2]
                  } else {
                    // console.log('leave renderCloseParen')
                    return ['', 2]
                  }
                },
              },
            }
          })

        },
        input: () => {
          this.contentModfied = (this.vditor.getValue() !== this.originContent);
        },
        mode: this.editorConfig.editMode,
        toolbar: this.toolbar,
        toolbarConfig: {
          hide: false,
          pin: true
        },
        outline: {
          position: this.editorConfig.outlinePosition,
          enable: this.editorConfig.enableOutline
        },
        value: "",
        preview: {
          hljs: {
            enable: this.editorConfig.enableHighLight,
            style: this.editorConfig.codeStyle,
            lineNumber: this.editorConfig.enableLineNumber
          },
          maxWidth: 4000,

        },
        upload: {
          url: global.HOST_URL + "/file/vditor",
          headers: this.config.headers,
          format(files, responseText) {
            const res = JSON.parse(responseText);
            const name = files[0].name;
            const url = res.data.succMap[name];

            return JSON.stringify({
              code: 0,
              data: {
                errFiles: '',
                succMap: {
                  [name]: global.HOST_URL_WITHOUT_API + url
                }
              }
            });
          }
        },


      }
      this.vditor = new Vditor('vditor', options)
    },
    setContent(title, value, notebookName) {
      this.contentModfied = false;
      this.notebookName = notebookName
      this.showEditTitle = false;
      this.vditor.setValue(value);
      this.originContent = this.vditor.getValue();
      this.title = title;
      this.newTitle = title;

      console.log('this.notebookName', this.notebookName)

    },
    clear() {
      this.vditor.setValue("");
      this.notebookName = null
      this.showEditTitle = false;
      this.title = null;

    },
    setTitleEditable(editable) {
      this.showEditTitle = editable;
    },
    /**
     * raw 是否直接获取值  不进行去空行
     */
    getContent(raw) {
      if (raw) {
        return this.vditor.getValue();
      }
      this.vditor.setValue(this.vditor.getValue());
      return this.vditor.getValue();
    },
    getTitle() {
      return this.title;
    },
    setNotebookName(notebookName) {
      this.notebookName = notebookName
    },
    publishNote() {
      let req = {
        notebookName: this.notebookName,
        noteTitle: this.title
      }
      axios.post(global.HOST_URL + "/article", req, this.config).then(res => {
        res = res.data;
        if (res.code === 0) {
          res = res.data;
          this.$notify({
            type: "success",
            message: "公开成功，地址为: " + window.location.host + "/#/article/" + this.config.headers.username + "/" + res.articleId,
            duration: 10000
          })
        }
      })
    },
    child_saveContent() {
      if (!this.title) {
        this.$notify({
          type: "warning",
          message: "标题为空",
          duration: 1000
        })
        return;
      }
      if (!this.notebookName) {
        this.$notify({
          type: "warning",
          message: "请选择笔记本",
          duration: 1000
        })
        return;
      }

      console.log("this.vditor.getValue()",this.vditor.getValue())
      //this.vditor.setValue(this.vditor.getValue());
      let content = this.vditor.getValue();
      this.contentModfied = false;
      this.originContent = content;

      console.log("content",content)

      this.$emit('saveContent', content, this.title, this.notebookName);

      this.vditor.setValue(content);

      this.$message.success('保存成功~')
      console.log(this.title)

    },
    renameTitle() {
      this.$emit('renameTitle', this.newTitle);
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(() => {
            done();
          })
          .catch(() => {
          });
    },
    handleJump(noteTitle, notebookName) {
      this.$emit('jumpSelect', noteTitle, notebookName)
    }
  }
}
</script>

<style>
.title {
  font-size: 24px;
  /* font-weight: bold; */
  margin-bottom: 5px;
}

.vidtor {
  width: 100%;
  min-height: 200px;
}
</style>