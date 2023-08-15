<template>
    <el-container style="height: 100%;">
        <el-aside style="background: rgb(245, 245, 245);height: 100vh; width: 250px;overflow-x: hidden;position: fixed">
            <el-row class="tac">
                <el-col :span="24">
                    <div style="width: 100%;margin-left: 20px;cursor: pointer;display: flex;align-items: center;"
                         @click="goHome">
                        <img src="../assets/logo.png" style="width: 40px;height: 40px"/>
                        <h1 style="font-size: 1.8rem;margin-left: 10px">Bragi</h1>
                    </div>

                    <div class="search-icon" @click="openSearch">
                        <i class="el-icon-search" style="margin-left: 15px; font-size: 15px;"></i>

                        <div
                                style="flex: 1 1 auto; white-space: nowrap; min-width: 0px; overflow: hidden; text-overflow: ellipsis;margin-left: 10px;color: rgb(48,49,51)">
                            Search
                        </div>
                    </div>

                    <el-menu default-active="2" ref="menu" class="el-menu-vertical-demo" @open="handleOpen"
                             @close="handleClose">
                        <el-submenu index="1" class="submenu-area"
                                    @mouseover.native="setShowAddIcon(filespcaceShowAddIcon, true)"
                                    @mouseout.native="setShowAddIcon(filespcaceShowAddIcon, false)">
                            <template slot="title">
                                <!-- <i class="el-icon-location"></i> -->
                                <i class="iconfont icon-wenjianjia file-space-icon"></i>
                                <span>文件空间</span>
                                <!-- 右侧的加号按钮 -->
                                <i v-show="filespcaceShowAddIcon.showAddIcon" @click.stop="addNewNotebook"
                                   class="iconfont icon-jiajianzujianjiahao add_icon"></i>
                            </template>
                            <!--              <router-link-->
                            <!--                  :to="{name:'content'}"-->
                            <!--                  style="text-decoration: none;">-->
                            <div>
                                <el-menu ref="notebook_menu">
                                    <!-- 显示所有的笔记本 -->
                                    <div v-for="(item, index) of this.notebookList" :key="item.notebookName"
                                         v-contextmenu:notebookRightMenu>
                                        <el-submenu :index="'1-' + item.notebookName" class="notebook-submenu"
                                                    @mouseover.stop.native="setShowAddIcon(item, true)"
                                                    @mouseout.stop.native="setShowAddIcon(item, false)">
                                            <template slot="title">
                                                <!-- 重命名状态或新创建状态下为输入框，否则直接显示笔记本的名字 -->
                                                <div class="set-right-margin" v-if="toRenameNotebookName && toRenameNotebookName.length > 0 && toRenameNotebookName === item.notebookName
                          || item.notebookName.length === 0">
                                                    <el-input ref="notebook_input" size="mini"
                                                              @blur="handleNotebookInputBlur" @click.native.stop
                                                              @keyup.enter.native="handleNotebookInputEnter"
                                                              v-model="notebookInputModelValue"/>
                                                </div>
                                                <div class="set-right-margin" v-else>
                                                    <!-- <i class="el-icon-document"></i> -->
                                                    <i class="iconfont icon-biji notobook-icon"></i>
                                                    {{ item.notebookName }}
                                                    <!-- 删除图标 -->
                                                    <i class="el-icon-delete notebook-delete-icon"
                                                       @click.stop="notebookDeleteIconClick(item.notebookName)"
                                                       v-show="item.showAddIcon"></i>
                                                    <!-- 笔记本右侧的加号按钮 -->
                                                    <i v-show="item.showAddIcon" @click.stop="addNewNote(index)"
                                                       class="iconfont icon-jiajianzujianjiahao add_icon"></i>
                                                </div>
                                            </template>
                                            <!-- 显示笔记本中所有的笔记 -->
                                            <div v-for="subitem in item.noteList" :key="subitem.title"
                                                 style="background: rgb(245,245,245);">
                                                <el-menu-item :index="'1-' + item.notebookName + '-' + subitem.title"
                                                              @click="selectNote(subitem.title, item.notebookName)"
                                                              @contextmenu.stop.native="setNoteRightMenu(item.notebookName, subitem.title)"
                                                              v-contextmenu:noteRightMenu class="notebook_item"
                                                              :class="{ active: curNote.noteTitle === subitem.title && curNote.notebookName === subitem.notebookName }"
                                                              @mouseover.native.stop="setShowMoreIcon(subitem, true)"
                                                              @mouseout.native.stop="setShowMoreIcon(subitem, false)">
                                                    <div class="menuitem-content"
                                                         style="display: flex; align-items: center; width: 100%;">
                                                        <!-- 重命名状态或新创建状态下为输入框，否则直接显示笔记的名字 -->
                                                        <div v-if="toRenameNoteName && toRenameNoteName.length > 0 && toRenameNoteName === subitem.title && noteRightMenuValues.notebookName === item.notebookName
                              || subitem.title.length === 0">
                                                            <el-input ref="note_input" size="mini"
                                                                      @blur="handleNoteInputBlur(index)"
                                                                      @keyup.enter.native="handleNoteInputEnter(index)"
                                                                      v-model="noteInputModelValue"/>
                                                        </div>
                                                        <div v-else>
                                                            <i class="iconfont icon-bijijilu note-icon"></i>
                                                            {{ subitem.title }}
                                                            <!-- 删除图标 -->
                                                            <i class="el-icon-delete note_delete_icon"
                                                               @click="NoteDeleteIconClick(item.notebookName, subitem.title)"
                                                               v-show="subitem.showMoreIcon"></i>
                                                            <!-- 更多图标 -->
                                                            <i class="el-icon-more more_button"
                                                               v-show="subitem.showMoreIcon"></i>
                                                        </div>
                                                    </div>
                                                </el-menu-item>
                                            </div>
                                        </el-submenu>
                                    </div>
                                </el-menu>
                            </div>

                        </el-submenu>
                        <el-menu-item index="3" style="height: 40px;line-height: 40px" @click="mind">
                            <div>
                                <i class="el-icon-connection"></i>
                                <span style="margin-top: auto;margin-bottom: auto;" slot="title">引用关系</span>
                            </div>

                        </el-menu-item>
                        <el-menu-item index="4" style="height: 40px;line-height: 40px;">
                            <div @click="jumpSetting">
                                <i class="el-icon-setting"></i>
                                <span slot="title">设置</span>
                            </div>
                        </el-menu-item>
                        <el-menu-item index="5" style="height: 40px;line-height: 40px;">
                            <div @click="handleLogout()">
                                <i class="el-icon-switch-button"></i>
                                <span slot="title">退出登录</span>
                            </div>

                        </el-menu-item>
                    </el-menu>
                </el-col>
            </el-row>

            <div class="bottom_two" @click="openTrash">
                <div class="trash_div">
                    <i class="el-icon-delete"></i>
                    <span style="font-size: 14px;margin-left: 4px;">垃圾桶</span>
                </div>
            </div>
        </el-aside>

        <el-main style="padding:0; margin-left: 250px;">
            <div class="mid-content">
                <router-view @updateNotebookList="refreshNotebookList"></router-view>
            </div>
        </el-main>

        <!------------------------------------------垃圾桶------------------------------------------------->

        <el-dialog :visible.sync="trashVisible">
            <div style="display: flex;">
                <el-input v-model="trash_search_input" prefix-icon="el-icon-search"
                          placeholder="搜索被删除的页面"></el-input>
                <el-tooltip class="item" effect="dark" content="清空垃圾桶" placement="right">
                    <i @click="clearAllDelNotes" class="el-icon-delete"
                       style="alignment: right; font-size:20px; line-height:40px; margin-left: 10px; cursor: pointer;"></i>
                </el-tooltip>
            </div>
            <el-divider></el-divider>

            <div v-loading="trashLoading" style="height: 350px;overflow-y: scroll;">
                <div v-for="item of delNoteList_show" :key="item.id" class="trash_content">
                    <div class="trash_item" style="display: flex; align-items: center; height: 30px">
                        <i class="el-icon-document" style="display: flex; align-items: center; font-size:18px;"></i>

                        <span style="font-weight: bold;margin-left: 10px;flex-grow: 1;color: rgb(76,76,76)">{{
                            item.notebook
                            }}/{{
                            item.title
                            }}
              <span style="margin-left: 15px;font-weight: normal;color: rgb(179,179,179);font-size: 10px">
              {{ item.deleteTime }}
            </span>
              <span style="margin-left: 15px;font-weight: normal;color: rgb(179,179,179);font-size: 10px">
              {{ item.fileSize }}B
            </span>

            </span>


                        <div style="display: flex; align-items: center; justify-content: flex-end;">
                            <i @click="recoverDelNote(item.id)" class="el-icon-refresh-left"
                               style="font-size: 18px; margin-right:10px; cursor: pointer;"></i>
                            <i @click="clearDelNote(item.id)" class="el-icon-delete"
                               style="font-size: 18px; margin-right:10px; cursor: pointer;"></i>
                        </div>

                    </div>

                </div>
            </div>
            <!--            <div v-show="trashLoading">-->
            <!--              <el-skeleton :rows="6" animated />-->
            <!--            </div>-->

        </el-dialog>

        <!------------------------------------------搜索框------------------------------------------------->

        <el-dialog :visible.sync="searchVisible">
            <el-tabs v-model="activeName">
                <el-tab-pane label="笔记名搜索" name="noteNameSearch">
                    <div style="display: flex;">
                        <el-input v-model="searchnote_search_input" prefix-icon="el-icon-search"
                                  placeholder="输入笔记名搜索，点击快速跳转"></el-input>
                    </div>
                    <el-divider></el-divider>

                    <div style="height: 350px; overflow-y: auto;">
                        <div v-if="searchNoteList_show.length === 0" class="search_prompt">
                            没有找到笔记名包含“{{ searchnote_search_input }}”的笔记!
                        </div>
                        <div v-for="item of searchNoteList_show" :key="item.articleId" class="trash_content">
                            <div class="trash_item" style="display: flex; align-items: center; height: 30px"
                                 @click="openAnother(item.title, item.notebookName)">
                                <i class="el-icon-document" style="font-size:18px;"></i>

                                <span style="font-weight: bold;margin-left: 10px;flex-grow: 1;color: rgb(76,76,76)">
                  {{ item.notebookName }}/{{ item.title }}
                </span>

                            </div>

                        </div>
                    </div>
                </el-tab-pane>
                <!--            <div v-show="trashLoading">-->
                <!--              <el-skeleton :rows="6" animated />-->
                <!--            </div>-->
                <el-tab-pane label="笔记标签搜索" name="tagSearch">
                    <div style="display: flex;">
                        <el-input v-model="searchTag_search_input" prefix-icon="el-icon-search"
                                  placeholder="输入笔记标签搜索，点击快速跳转"></el-input>
                    </div>
                    <el-divider></el-divider>

                    <div style="height: 350px; overflow-y: auto;">
                        <div v-if="searchNoteListTag_show.length === 0" class="search_prompt">
                            没有找到包含“{{ searchTag_search_input }}”标签的笔记!
                        </div>
                        <div v-for="item of searchNoteListTag_show" :key="item.articleId">
                            <div class="trash_item" style="display:flex; align-items: center; height:30px"
                                 @click="openAnother(item.title, item.notebookName)">
                                <i class="el-icon-document" style="font-size:18px;"></i>

                                <span style="flex-grow: 1; flex-shrink:0;" class="search-item-line-style">
                  {{ item.notebookName }}/{{ item.title }}
                </span>

                                <div style="flex-shrink:1; height:20px; overflow:hidden">
                  <span v-for="tag of item.tags" :key="tag" class="search-item-tag-style">
                    {{ tag }}
                  </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </el-dialog>

        <!------------------------------------------左键菜单------------------------------------------------->

        <v-contextmenu theme="bright" ref="moreMenu">
            <v-contextmenu-item>菜单</v-contextmenu-item>

            <v-contextmenu-group title="按钮组">
                <v-contextmenu-item>菜单1</v-contextmenu-item>
                <v-contextmenu-item>菜单2</v-contextmenu-item>
                <v-contextmenu-item disabled>菜单3</v-contextmenu-item>
            </v-contextmenu-group>
        </v-contextmenu>


        <!------------------------------------------右键菜单------------------------------------------------->
        <!-- 笔记本右键菜单 -->
        <input @change="handleFileUpload($event)" style="display : none" ref="fileUpload" type="file" accept=".md"/>
        <v-contextmenu theme="bright" class="rightMenu" ref="notebookRightMenu" @contextmenu="setNotebookRightMenu">
            <v-contextmenu-item @click="doRenameNotebook">重命名</v-contextmenu-item>
            <v-contextmenu-item @click="handleDelNotebook">删除</v-contextmenu-item>
            <v-contextmenu-item @click="uploadNote">导入笔记</v-contextmenu-item>
        </v-contextmenu>

        <!-- 笔记右键菜单 -->
        <v-contextmenu theme="bright" class="rightMenu" ref="noteRightMenu">
            <v-contextmenu-item @click="doRenameNote">重命名</v-contextmenu-item>
            <v-contextmenu-item @click="handleDelNote">删除</v-contextmenu-item>
            <v-contextmenu-submenu title="移动至">
                <div v-for="otherNotebook of notebookList" :key="otherNotebook.notebookName">
                    <v-contextmenu-item @click="handleMoveNote(otherNotebook.notebookName)"
                                        v-if="curNotebook.notebookName !== otherNotebook.notebookName">
                        📙 {{ otherNotebook.notebookName }}
                    </v-contextmenu-item>
                </div>
            </v-contextmenu-submenu>
            <v-contextmenu-submenu title="复制到">
                <div v-for="otherNotebook of notebookList" :key="otherNotebook.notebookName">
                    <v-contextmenu-item @click="handleCopyNote(otherNotebook.notebookName)"
                                        v-if="curNotebook.notebookName !== otherNotebook.notebookName">
                        📙 {{ otherNotebook.notebookName }}
                    </v-contextmenu-item>
                </div>
            </v-contextmenu-submenu>
        </v-contextmenu>

        <!--------------------------------------------------------------------------------------------------->

    </el-container>
</template>

<script>
// import axios from "axios";
import global from "@/global";
import Vue from "vue";

import {
    reqDelnoteDeletePurgeDelnotes, reqDelnoteDeleteWithdrawDelnote,
    reqDelnoteGetDelnotes, reqDelnoteRecover, reqNoteCopy,
    reqNoteDeleteNote, reqNoteDeleteNotebook,
    reqNoteGetNotebookList, reqNoteGetNoteText, reqNoteMove,
    reqNotePostSave,
    reqNotePutNotebook, reqNoteUpload, reqRenameNote, reqRenameNotebook, reqUserPostLogout
} from "@/api"

export default {
    name: "navbar",
    components: {},

    provide() {
        return {
            fa_handleSaveContent: this.handleSaveContent,
            fa_doSwitchNote: this.doSwitchNote
        }
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
            // 是否展示关于
            showAboutPage: false,

            // 文件空间下拉菜单是否显示添加按钮
            filespcaceShowAddIcon: {
                showAddIcon: false,
            },

            // 是否正在创建新的笔记本
            creatingNewNotebook: false,
            // 新创建的笔记本的名字
            newNotebookName: "",
            // 是否正在创建新的笔记
            creatingNewNote: false,
            // 新创建的笔记的名字
            newNoteName: "",

            // 笔记本右键跳出菜单时存的值
            notebookRightMenuValues: {},
            // 待重命名的笔记本的名字
            toRenameNotebookName: null,
            // 希望修改成的笔记本的名字
            destNotebookName: null,

            // 笔记右键跳出菜单时存的值
            noteRightMenuValues: {},
            // 待重命名的笔记的名字
            toRenameNoteName: null,
            //希望修改成的笔记的名字
            destNoteName: null,

            // 搜索框当前tab的name
            activeName: "noteNameSearch",

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

            // 所有的笔记本
            notebookList: [],
            noteList: [],

            dots: false,
            backgroundColor: "lightgrey",
            trashVisible: false,
            trashLoading: true,
            trash_search_input: '',

            searchVisible: false,

            // 根据笔记搜索输入框中的内容
            searchnote_search_input: '',
            // 根据tag搜索输入框中的内容
            searchTag_search_input: '',

            searchNoteList_show_temp: [],
            //searchnoteLoading: true
        }

    },
    computed: {
        // 笔记本输入框绑定的值
        notebookInputModelValue: {
            get() {
                return this.creatingNewNotebook ? this.newNotebookName : this.destNotebookName;
            },
            set(value) {
                if (this.creatingNewNotebook) {
                    this.newNotebookName = value;
                } else {
                    this.destNotebookName = value;
                }
            }
        },
        // 笔记输入框绑定的值
        noteInputModelValue: {
            get() {
                return this.creatingNewNote ? this.newNoteName : this.destNoteName;
            },
            set(value) {
                if (this.creatingNewNote) {
                    this.newNoteName = value;
                } else {
                    this.destNoteName = value;
                }
            }
        },

        delNoteList_show: function () {
            if (this.trash_search_input === "") return this.delNoteList;
            let ret = [];
            this.delNoteList.forEach((item) => {
                if (item.title.toLowerCase().indexOf(this.trash_search_input.toLowerCase()) !==
                    -1) ret.push(item);
            });
            return ret;
        },

        // 笔记名搜索的结果
        searchNoteList_show: function () {
            if (this.searchnote_search_input === "") {
                return this.searchNoteList_show_temp;
            }
            let ret = [];
            this.searchNoteList_show_temp.forEach((item) => {
                if (item.title.toLowerCase().indexOf(this.searchnote_search_input.toLowerCase()) !== -1) {
                    ret.push(item);
                }
            });
            return ret;
        },
        // 笔记tag搜索的结果
        searchNoteListTag_show: function () {
            let ret = [];
            if (this.searchTag_search_input === "") {
                this.searchNoteList_show_temp.forEach((item) => {
                    if (item.tags && item.tags.length > 0) {
                        ret.push(item);
                    }
                })
                return ret;
            }
            for (let item of this.searchNoteList_show_temp) {
                if (item.tags) {
                    for (let tag of item.tags) {
                        if (tag.toLowerCase().indexOf(this.searchTag_search_input.toLowerCase()) !== -1) {
                            ret.push(item);
                            break;
                        }
                    }
                }
            }
            return ret;
        }

    },
    mounted() {
        this.refreshNotebookList()
        this.computeTemp(this.notebookList)
    },
    watch: {

        notebookList: {
            handler(newValue) {
                this.computeTemp(newValue)
            },
            deep: true,
        },
    },
    methods: {
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        },
        refreshNotebookList(notebookName) {
            // this.clearRenameInfo();
            reqNoteGetNotebookList(this.config).then(res => {
                //axios.get(global.HOST_URL + "/note", this.config).then(res => {
                res = res.data;
                if (res.code === 0) {
                    this.notebookList = res.data;

                    console.log('this.notebookList', this.notebookList);

                    if (notebookName) {
                        this.doSwitchNotebook(notebookName);
                        return;

                    }
                    this.updateCurNotebookInfo()
                }
            })
            reqDelnoteGetDelnotes(this.config).then(res => {
                // axios.get(global.HOST_URL + "/delnote", this.config).then(res => {
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
                    // if (notebook.noteList && notebook.noteList.length > 0) {
                    //   this.doSwitchNote(notebook.noteList[0].title, notebookName, true);
                    // } else {
                    //   this.clearCurNoteInfo();
                    // }
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

        // 设置某个对象显示添加按钮
        setShowAddIcon(obj, value) {
            if (!Object.prototype.hasOwnProperty.call(obj, 'showAddIcon')) {
                Vue.set(obj, "showAddIcon", false);
            }
            obj.showAddIcon = value;
        },

        // notebook位置的输入框blur时调用的函数
        handleNotebookInputBlur() {
            // 正在创建一个新的笔记本
            if (this.creatingNewNotebook) {
                // 如果没有输入，则取消创建新的文件夹
                if (this.newNotebookName === "") {
                    // 弹出笔记本列表首部的空元素
                    this.notebookList.shift();

                    this.creatingNewNotebook = false;
                } else {
                    this.handleCreateNoteBook();
                }
            } else { // 正在重命名
                if (this.destNotebookName === "") {
                    this.$notify({
                        type: "error",
                        message: "笔记本的名字不能为空",
                        duration: 1000,
                    });
                } else {
                    if (this.destNotebookName === this.toRenameNotebookName) {
                        this.clearRenameNotebookName();
                    } else {
                        if (this.destNotebookName) {
                            this.handleRenameNotebook();
                        }
                    }
                }
            }
        },
        // notebook位置的输入框在键盘enter的时候调用的函数
        handleNotebookInputEnter() {
            // 正在创建一个新的笔记本
            if (this.creatingNewNotebook) {
                if (this.newNotebookName === "") {
                    this.$notify({
                        type: "error",
                        message: "笔记本的名字不能为空",
                        duration: 1000,
                    });
                } else {
                    this.handleCreateNoteBook();
                }
            } else { // 正在重命名
                if (this.destNotebookName === "") {
                    this.$notify({
                        type: "error",
                        message: "笔记本的名字不能为空",
                        duration: 1000,
                    });
                } else {
                    if (this.destNotebookName === this.toRenameNotebookName) {
                        this.clearRenameNotebookName();
                    } else {
                        this.handleRenameNotebook();
                    }
                }
            }
        },

        // 添加一个新的笔记本
        addNewNotebook() {
            // 展开文件菜单的下拉菜单
            this.$refs.menu.open(1);

            // 在笔记本列表的首部添加一个空的元素
            this.notebookList.unshift({notebookName: "", noteList: []});

            this.creatingNewNotebook = true;

            this.$nextTick(function () {
                // 界面上只会有一个输入框，所以可以用数组的第0个元素取
                this.$refs.notebook_input[0].focus();
            });
        },
        // 向后端发送一个请求，创建一个新的笔记本
        handleCreateNoteBook() {
            let url = global.HOST_URL + "/note/" + this.newNotebookName;
            reqNotePutNotebook(this.newNotebookName, {}, this.config).then((res) => {
                // axios.put(url, {}, this.config).then((res) => {
                res = res.data;
                if (res.code === 0) {
                    // 弹出笔记本列表首部的元素
                    this.notebookList.shift();

                    this.creatingNewNotebook = false;
                    this.newNotebookName = "";

                    this.refreshNotebookList();

                    this.$message.success('新建笔记本成功~')
                } else {
                    this.$refs.notebook_input[0].focus();
                    this.$notify({
                        type: "error",
                        message: res.msg,
                        duration: 1000,
                    });
                }
            })
        },

        // note位置的输入框blur时调用的函数
        handleNoteInputBlur(index) {
            // 正在创建一个笔记
            if (this.creatingNewNote) {
                // 如果没有输入，则取消创建新的文件
                if (this.newNoteName === "") {
                    // 弹出笔记列表首部的空元素
                    this.notebookList[index].noteList.shift();

                    this.creatingNewNote = false;
                } else {
                    let name_exist = false; // 标志是否存在同名的笔记
                    this.notebookList[index].noteList.forEach((item, index) => {
                        if (index !== 0 && item.title === this.newNoteName) {
                            name_exist = true;
                        }
                    });
                    if (name_exist) {
                        this.$notify({
                            type: "error",
                            message: "笔记名已存在",
                            duration: 1000,
                        });
                        this.$refs.note_input[0].focus();
                    } else {
                        this.handleCreateNote(index);
                    }
                }
            } else { // 正在重命名一个笔记
                if (this.destNoteName === "") {
                    this.$notify({
                        type: "error",
                        message: "笔记的名字不能为空",
                        duration: 1000,
                    });
                } else {
                    if (this.destNoteName === this.toRenameNoteName) {
                        this.clearRenameNoteName();
                    } else {
                        if (this.destNoteName) {
                            this.handleRenameNote();
                        }
                    }
                }
            }
        },
        // note位置的输入框在键盘enter的时候调用的函数
        handleNoteInputEnter(index) {
            // 正在创建一个笔记
            if (this.creatingNewNote) {
                if (this.newNoteName === "") {
                    this.$notify({
                        type: "error",
                        message: "笔记的名字不能为空",
                        duration: 1000,
                    });
                } else {
                    let name_exist = false; // 标志是否存在同名的笔记
                    this.notebookList[index].noteList.forEach((item, index) => {
                        if (index !== 0 && item.title === this.newNoteName) {
                            name_exist = true;
                        }
                    });
                    if (name_exist) {
                        this.$notify({
                            type: "error",
                            message: "笔记名已存在",
                            duration: 1000,
                        });
                        this.$refs.note_input[0].focus();
                    } else {
                        this.handleCreateNote(index);
                    }
                }
            } else { // 正在重命名
                if (this.destNoteName === "") {
                    this.$notify({
                        type: "error",
                        message: "笔记的名字不能为空",
                        duration: 1000,
                    });
                } else {
                    if (this.destNoteName === this.toRenameNoteName) {
                        this.clearRenameNoteName();
                    } else {
                        this.handleRenameNote();
                    }
                }
            }
        },

        // 添加一个新的笔记
        addNewNote(index) {
            let notebookName = this.notebookList[index].notebookName;
            // 展开当前笔记本的下拉菜单
            this.$refs.notebook_menu.open("1-" + notebookName);

            // 当前笔记本的笔记列表的首部添加一个空的元素
            this.notebookList[index].noteList.unshift({title: ""});

            this.creatingNewNote = true;

            this.$nextTick(function () {
                this.$refs.note_input[0].focus();
            });
        },
        // 向后端发送请求，创建一个新的笔记
        handleCreateNote(index) {
            let notebookName = this.notebookList[index].notebookName;
            let noteName = this.newNoteName;
            let url = global.HOST_URL + "/note/" + notebookName + "/" + this.newNoteName;
            let request = {
                content: "",
            };
            reqNotePostSave(notebookName, this.newNoteName, request, this.config).then((res) => {
                // axios.post(url, request, this.config).then((res) => {
                res = res.data;
                if (res.code === 0) {
                    // 弹出笔记列表首部的元素
                    this.notebookList[index].noteList.shift();

                    this.creatingNewNote = false;
                    this.newNoteName = "";

                    this.refreshNotebookList();
                    this.selectNote(noteName, notebookName);

                    this.$message.success('笔记创建成功~')
                } else {
                    this.$refs.note_input[0].focus();
                    this.$notify({
                        type: "error",
                        message: res.msg,
                        duration: 1000,
                    });
                }
            });
        },

        // 设置笔记本右键跳出菜单项时存放的值
        setNotebookRightMenu(vnode) {
            this.notebookRightMenuValues = {
                notebookName: vnode.data.key,
            };
        },

        // 将待重命名的笔记本的名字清为null
        clearRenameNotebookName() {
            this.toRenameNotebookName = null;
            this.destNotebookName = null;
        },
        // 设置待重命名的笔记本的名字为当前选中的笔记本的名字，随后当前笔记本名字的位置变为输入框
        doRenameNotebook() {
            // 设置待重命名的笔记本的名字为当前选中的笔记本的名字
            this.toRenameNotebookName = this.notebookRightMenuValues.notebookName;
            // 希望修改成的笔记本的名字默认为原来的名字
            this.destNotebookName = this.toRenameNotebookName;
            this.$nextTick(function () {
                // 界面上只会有一个输入框，所以可以用数组的第0个元素取
                this.$refs.notebook_input[0].select();
            });
        },
        // 重命名笔记本
        handleRenameNotebook() {
            let url = global.HOST_URL + "/note/" + this.destNotebookName;
            let request = {
                move: true,
                srcNotebook: this.toRenameNotebookName,
            };
            // this.clearRenameNotebookName();
            // this.showHistory = false;
            reqRenameNotebook(this.destNotebookName, request, this.config).then((res) => {
                // axios.put(url, request, this.config).then((res) => {
                res = res.data;
                if (res.code === 0) {
                    res = res.data;
                    // if (request.srcNotebook === this.curNote.notebookName) {
                    //   this.$refs.editor.setNotebookName(this.destNotebookName);
                    // }
                    this.notebookList.forEach((notebook) => {
                        if (notebook.notebookName === request.srcNotebook) {
                            notebook.notebookName = this.destNotebookName;
                            notebook.noteList = res;
                        }
                    });
                    this.clearRenameNotebookName();

                    this.$message.success("重命名成功~")

                } else {
                    this.$notify({
                        type: "error",
                        message: res.msg,
                        duration: 1000,
                    });
                }
            });

        },

        //设置笔记右键跳出菜单项时存放的值
        setNoteRightMenu(notebookName, noteName) {
            this.noteRightMenuValues.noteName = noteName;
            this.noteRightMenuValues.notebookName = notebookName;

            // 改变当前的notebook
            this.doSwitchNotebook(notebookName);
        },

        // 将待重命名的笔记的名字清为null
        clearRenameNoteName() {
            this.toRenameNoteName = null;
            this.destNoteName = null;
        },
        // 设置待重命名的笔记的名字为当前选中的笔记的名字,随后当前笔记名字的位置变为输入框
        doRenameNote() {
            // 设置待重命名的笔记的名字为当前选中的笔记的名字
            this.toRenameNoteName = this.noteRightMenuValues.noteName;
            // 希望修改成的笔记的名字默认为原来的名字
            this.destNoteName = this.toRenameNoteName;
            this.$nextTick(function () {
                // 界面上只会有一个输入框，所以可以用数组的第0个元素取
                this.$refs.note_input[0].select();
            });
        },
        // 重命名笔记
        handleRenameNote() {
            let url = global.HOST_URL + "/note/" + this.noteRightMenuValues.notebookName + "/" + this.destNoteName;
            let param = {
                srcNotebook: this.noteRightMenuValues.notebookName,
                srcTitle: this.toRenameNoteName,
                move: true,
            };
            reqRenameNote(this.noteRightMenuValues.notebookName, this.destNoteName, param, this.config).then((res) => {
                // axios.put(url, param, this.config).then((res) => {
                res = res.data;
                if (res.code === 0) {
                    this.refreshNotebookList();
                    this.clearRenameNoteName()

                    this.$message.success("重命名成功~")

                } else {
                    this.$notify({
                        type: "warning",
                        message: res.msg,
                    });
                }
            });
        },

        // 将笔记从一个文件夹移动到另一个文件夹
        handleMoveNote(targetNotebookName) {
            let target_notebook_exist_note = false;
            for (let i = 0; i < this.notebookList.length; i++) {
                if (this.notebookList[i]['notebookName'] === targetNotebookName) {
                    for (let j = 0; j < this.notebookList[i].noteList.length; j++) {
                        if (this.notebookList[i].noteList[j]['title'] === this.noteRightMenuValues.noteName) {
                            target_notebook_exist_note = true;
                        }
                    }
                }
            }
            if (target_notebook_exist_note) {
                this.$notify({
                    type: "warning",
                    message: "目标笔记本存在同名的笔记",
                });
            } else {
                let url = global.HOST_URL + "/note/" + targetNotebookName + "/" + this.noteRightMenuValues.noteName;
                let param = {
                    srcNotebook: this.noteRightMenuValues.notebookName,
                    srcTitle: this.noteRightMenuValues.noteName,
                    move: true,
                };
                reqNoteMove(targetNotebookName, this.noteRightMenuValues.noteName, param, this.config).then((res) => {
                    // axios.put(url, param, this.config).then((res) => {
                    res = res.data;
                    if (res.code === 0) {
                        this.refreshNotebookList();

                        // 展开目标笔记本的下拉菜单
                        this.$refs.notebook_menu.open("1-" + targetNotebookName);
                        this.$message.success('笔记移动成功~')
                    } else {
                        this.$notify({
                            type: "warning",
                            message: res.msg,
                        });
                    }
                });
            }
        },
        // 将笔记从一个文件夹复制到另一个文件夹
        handleCopyNote(targetNotebookName) {
            let target_notebook_exist_note = false;
            for (let i = 0; i < this.notebookList.length; i++) {
                if (this.notebookList[i]['notebookName'] === targetNotebookName) {
                    for (let j = 0; j < this.notebookList[i].noteList.length; j++) {
                        if (this.notebookList[i].noteList[j]['title'] === this.noteRightMenuValues.noteName) {
                            target_notebook_exist_note = true;
                        }
                    }
                }
            }
            if (target_notebook_exist_note) {
                this.$notify({
                    type: "warning",
                    message: "目标笔记本存在同名的笔记",
                });
            } else {
                let url = global.HOST_URL + "/note/" + targetNotebookName + "/" + this.noteRightMenuValues.noteName;
                let param = {
                    srcNotebook: this.noteRightMenuValues.notebookName,
                    srcTitle: this.noteRightMenuValues.noteName,
                };
                reqNoteCopy(targetNotebookName, this.noteRightMenuValues.noteName, param, this.config).then((res) => {
                    // axios.put(url, param, this.config).then((res) => {
                    res = res.data;
                    if (res.code === 0) {
                        this.refreshNotebookList();

                        // 展开目标笔记本的下拉菜单
                        this.$refs.notebook_menu.open("1-" + targetNotebookName);
                        this.$message.success('笔记复制成功~')
                    } else {
                        this.$notify({
                            type: "warning",
                            message: res.msg,
                        });
                    }
                });
            }
        },

        selectNote(noteTitle, notebookName) {

            // 展开文件菜单的下拉菜单
            this.$refs.menu.open(1);
            // 展开笔记所在的笔记本的下拉菜单
            this.$refs.notebook_menu.open("1-" + notebookName);

            // 同一个笔记  不用动
            // if (noteTitle === this.curNote.noteTitle && notebookName === this.curNote.notebookName) {
            //   return;
            // }

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

            //
            this.doSwitchNote(noteTitle, notebookName);

            console.log('2', this.curNote)


            //}
        },
        uploadNote() {
            this.$refs.fileUpload.click();
        },
        handleFileUpload() {
            let file = event.target.files[0];
            const upfilename = file.name.lastIndexOf(".");
            let fileExtension = file.name.split('.').pop();
            if (fileExtension !== 'md') {
                alert('Please upload a .md file');
                return
            }
            console.log("test1:" + "file");
            console.log(upfilename);
            let formData = new FormData();
            formData.append("file", file);
            formData.append("directory", this.notebookRightMenuValues.notebookName);
            let url = global.HOST_URL + "/note/upload";
            console.log(formData)
            reqNoteUpload(formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        token: this.config.headers.token,
                        username: this.config.headers.username
                    },
                }).then((res) => {
                // axios.post(url, formData,
                //   {
                //     headers: {
                //       'Content-Type': 'multipart/form-data',
                //       token: this.config.headers.token,
                //       username: this.config.headers.username
                //     },
                //   }
                // ).then((res) => {
                res = res.data;
                if (res.code === 0) {
                    this.refreshNotebookList();
                    this.selectNote(upfilename, this.curNotebook.notebookName);
                    this.$message.success("笔记上传成功");
                } else {
                    this.$notify({
                        type: "error",
                        message: res.msg,
                        duration: 1000,
                    });
                }
            });
            // const fileHandle = await window.showOpenFilePicker({
            //   types: [
            //     {
            //       description: "markdown",
            //       accept: { "text/*": [".md"] },
            //     },
            //   ],
            //   mutiple: false,
            // });
            // const upfilename = await fileHandle[0].name.split(".")[0];
            // const file = await fileHandle[0].getFile();
            // console.log(typeof mdfile)

            //       const reader = new FileReader();
            //       // let content = "";
            //       reader.onload = () => {
            //         console.log(file);
            //         // content = reader.result;
            //         // console.log(content);
            //         console.log(upfilename);
            //         let formData = new FormData();
            //         // formData['file']=content
            //         // formData['file']=file;
            //         formData.append("file",file);
            //         formData.append("directory",this.notebookRightMenuValues.notebookName);
            //         // formData['file']=file
            //         // formData['name']=upfilename
            //         // formData['directory']=this.notebookRightMenuValues.notebookName
            //         let url = global.HOST_URL + "/note/upload";
            //         // let request = {
            //         //   content: formData,
            //         // };
            //         console.log(formData)
            //         axios.post(url, formData,
            //             {
            //                 headers:{'Content-Type':'multipart/form-data',
            //                 token:this.config.headers.token,
            //                     username:this.config.headers.username
            //                 },
            //             }
            // ).then((res) => {
            //           res = res.data;
            //           if (res.code === 0) {
            //             this.refreshNotebookList();
            //             this.selectNote(upfilename, this.curNotebook.notebookName);
            //             this.$message.success("笔记上传成功");
            //           } else {
            //             this.$notify({
            //               type: "error",
            //               message: res.msg,
            //               duration: 1000,
            //             });
            //           }
            //         });
            //       };
            // reader.readAsText(file);
            // console.log(fileHandle);
        },
        doSwitchNote(noteTitle, notebookName) {
            let url = global.HOST_URL + "/note/" + notebookName + "/" + noteTitle;
            reqNoteGetNoteText(notebookName, noteTitle, this.config).then(res => {
                res = res.data;
                if (res.code === 0) {
                    let newCurNote = {
                        noteTitle: noteTitle,
                        content: res.data,
                        notebookName: notebookName
                    };
                    this.curNoteVersion = [];
                    this.curRef = null;
                    this.curNote = newCurNote;

                    const notebookName_temp = this.curNote.notebookName;
                    const noteTitle_temp = this.curNote.noteTitle;

                    // 展开文件菜单的下拉菜单
                    this.$refs.menu.open(1);
                    // 展开笔记所在的笔记本的下拉菜单
                    this.$refs.notebook_menu.open("1-" + notebookName);

                    this.$router.push({
                        name: 'content',
                        params: {
                            notebookName: notebookName_temp,
                            noteTitle: noteTitle_temp
                        },
                    })
                }
            })

        },
        handleSaveContentAndSwitchNotebook(content, notebookName) {
            let request = {
                content: content
            }
            let url = global.HOST_URL + "/note/" + this.curNote.notebookName + "/" + this.curNote.noteTitle;
            reqNotePostSave(this.curNote.notebookName, this.curNote.noteTitle, request, this.config).then(res => {
                // axios.post(url, request, this.config).then(res => {
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
            reqNotePostSave(this.curNote.notebookName, this.curNote.noteTitle, request, this.config).then(res => {
                // axios.post(url, request, this.config).then(res => {
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
            reqUserPostLogout(null, this.config);
            // axios.post(url, null, this.config);
            this.$store.commit('setLocalInfo', {});

            this.$notify({
                type: "success",
                message: "退出登录成功",
            });

            setTimeout(() => {
                this.$router.push('/login');
            }, 500);
        },

        handleDelNotebook() {
            let url = global.HOST_URL + "/note/" + this.notebookRightMenuValues.notebookName;
            reqNoteDeleteNotebook(this.notebookRightMenuValues.notebookName, this.config).then(res => {
                // axios.delete(url, this.config).then(res => {
                res = res.data;
                if (res.code === 0) {
                    //this.clearCurNotebookInfo();
                    //this.clearCurNoteInfo();

                    console.log('this.curNote: ', this.curNote)
                    console.log('this.notebookRightMenuValues.notebookName:', this.notebookRightMenuValues.notebookName)

                    if (this.curNote.notebookName === this.notebookRightMenuValues.notebookName) {
                        this.curNotebook = {};

                        let curNote_temp = {
                            notebookName: null,
                            noteTitle: null,
                            content: ""
                        }

                        this.curNote = curNote_temp

                        this.$router.push({
                            name: 'basic'
                        });
                    }

                    this.refreshNotebookList();
                    this.$message.success('笔记本删除成功~')
                }
            })
        },

        handleDelNote() {
            let url = global.HOST_URL + "/note/" + this.noteRightMenuValues.notebookName + "/" + this.noteRightMenuValues.noteName;

            console.log("test test")

            reqNoteDeleteNote(this.noteRightMenuValues.notebookName, this.noteRightMenuValues.noteName, this.config).then(res => {
                // axios.delete(url, this.config).then(res => {
                res = res.data;

                console.log("res = ", res)

                if (res.code === 0) {
                    this.$message.success('笔记删除成功~')
                    this.refreshNotebookList(this.noteRightMenuValues.notebookName);
                    // if(this.rightChosenNote.notebookName == this.chosenNote.notebookName
                    // && this.rightChosenNote.title == this.chosenNote.title){
                    //   this.resetChosenNoteAndOriginNote();
                    // }
                }
            })
                .catch((err) => {
                    console.log("in reqSaveStructure error:", err);
                })

        },

        NoteDeleteIconClick(notebookName, noteName) {
            this.setNoteRightMenu(notebookName, noteName);
            this.handleDelNote();
        },

        notebookDeleteIconClick(notebookName) {
            this.notebookRightMenuValues = {
                notebookName: notebookName,
            };
            this.handleDelNotebook();
        },


        clearAllDelNotes() {
            this.$confirm('确定要清空垃圾桶吗', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let url = global.HOST_URL + "/delnote";
                reqDelnoteDeletePurgeDelnotes(this.config).then(res => {
                        // axios.delete(url, this.config).then(
                        // res => {
                        res = res.data;
                        if (res.code === 0) {
                            this.delNoteList = [];
                        }
                    }
                )
            }).catch(() => {

            });
        },

        clearDelNote(id) {
            this.$confirm('确定要删除该笔记吗', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let url = global.HOST_URL + "/delnote/" + id;
                reqDelnoteDeleteWithdrawDelnote(id, this.config).then(res => {
                        // axios.delete(url, this.config).then(
                        //   res => {
                        res = res.data;
                        if (res.code !== 0) {
                            this.refreshNotebookList();
                            this.$message.error(res.msg)
                            return;
                        }
                        reqDelnoteGetDelnotes(this.config).then(res => {
                            // axios.get(global.HOST_URL + "/delnote", this.config).then(res => {
                            res = res.data;
                            if (res.code === 0) {
                                this.delNoteList = res.data;
                            }
                        })
                        this.$message.success('笔记删除成功~')
                    }
                )
            }).catch(() => {

            });
        },

        recoverDelNote(id) {

            this.$confirm('确定要恢复该笔记吗', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let url = global.HOST_URL + "/delnote/" + id + "?recover=true";
                reqDelnoteRecover(id, this.config).then(res => {
                        // axios.delete(url, this.config).then(
                        //   res => {
                        res = res.data;
                        if (res.code !== 0) {
                            this.refreshNotebookList();
                            this.$message.error(res.msg)
                            return;
                        }
                        this.refreshNotebookList();
                        this.$message.success('笔记恢复成功~')
                    }
                )
            }).catch(() => {

            });
        },

        setShowMoreIcon(subitem, value) {
            if (!Object.prototype.hasOwnProperty.call(subitem, 'showMoreIcon')) {
                Vue.set(subitem, "showMoreIcon", false);
            }
            subitem.showMoreIcon = value;
        },
        openTrash() {
            this.trashVisible = true
            this.trashLoading = true
            reqDelnoteGetDelnotes(this.config).then(res => {
                // axios.get(global.HOST_URL + "/delnote", this.config).then(res => {
                res = res.data;
                if (res.code === 0) {
                    this.trashLoading = false
                    this.delNoteList = res.data;

                    console.log(res.data)

                }
            })
        },
        openSearch() {
            this.searchVisible = true
        },
        computeTemp(value) {
            this.searchNoteList_show_temp = [];
            for (let i = 0; i < value.length; i++) {
                for (let j = 0; j < value[i].noteList.length; j++) {
                    this.searchNoteList_show_temp.push(value[i].noteList[j]);
                }
            }
        },
        openAnother(noteTitle, notebookName) {
            this.selectNote(noteTitle, notebookName)
            this.searchVisible = false
        },
        jumpSetting() {
            this.$router.push({
                name: 'setting'
            })
        },
        goHome() {
            this.$router.push('/')
        },
        // 保存笔记
        handleSaveContent(content, noteTitle, notebookName) {
            this.showHistory = false;
            let request = {
                content: content
            }
            let url = global.HOST_URL + "/note/" + notebookName + "/" + noteTitle;
            reqNotePostSave(notebookName, noteTitle, request, this.config).then(res => {
                // axios.post(url, request, this.config).then(res => {
                res = res.data;
                if (res.code === 0) {
                    this.curNote.content = content;
                    this.refreshNotebookList();
                }
            })
        },

        mind() {
            // this.$message.warning('该功能还未开发哦，请稍等~')
            this.$router.push({
                name: 'reference'
            })
        }
    }
}
</script>

<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 100%;
    min-height: 400px;
    /*max-height: 400px;*/
    margin-bottom: 45px;
    background: rgb(245, 245, 245);

}

.rightMenu {
    min-width: 100px;
}

.set-right-margin {
    margin-right: 20px;
    overflow: hidden;
}

.el-submenu__title {
    height: 40px;
    line-height: 40px;
}

.notebook-submenu {
    padding-left: 15px;
    background: rgb(245, 245, 245);
}

/* 下拉菜单的箭头 */
.el-submenu__icon-arrow {
    right: 230px;
    margin-top: -5px;
    width: 10px;
    height: 10px;
    font-weight: 900;
}

.notebook-submenu .el-submenu__icon-arrow {
    right: 218px;
}

/* 菜单关闭 */
.el-submenu > .el-submenu__title .el-submenu__icon-arrow {
    -webkit-transform: rotateZ(-90deg);
    -ms-transform: rotate(-90deg);
    transform: rotateZ(-90deg);
}

/* 菜单展开 */
.el-submenu.is-opened > .el-submenu__title .el-submenu__icon-arrow {
    -webkit-transform: rotateZ(0deg);
    -ms-transform: rotate(0deg);
    transform: rotateZ(0deg);
}

/* 加号图标 */
.add_icon {
    position: absolute;
    right: 10px;
    top: 50%;
    margin-top: -10px;
    color: rgb(200, 200, 200) !important;
    font-size: 5px;
    line-height: 20px;
    text-align: center;
    height: 20px;
    width: 20px;
}

.add_icon:hover {
    background-color: rgb(230, 230, 230);
}

.add_icon:active {
    background-color: rgb(225, 225, 225);
}

.notebook-delete-icon {
    position: absolute;
    right: 30px;
    top: 50%;
    margin-top: -10px;
    color: rgb(170, 170, 170) !important;
    font-size: 5px;
    line-height: 20px;
    text-align: center;
    height: 20px;
    width: 20px;
}

.notebook-delete-icon:hover {
    background-color: rgb(230, 230, 230);
}

.notebook-delete-icon:active {
    background-color: rgb(225, 225, 225);
}

.trash_item {
    background: white;
}

.trash_item:hover {
    background: rgb(245, 245, 245);
}

.more_button {
    position: absolute;
    top: 10px;
    right: 10px;
    width: 25px;
    border-radius: 4px;
}

.more_button:hover {
    background: rgb(220, 220, 220);
}

.more_button:active {
    background: rgb(210, 210, 210);
}

.note_delete_icon {
    position: absolute;
    top: 10px;
    right: 40px;
}

.note_delete_icon:hover {
    background: rgb(220, 220, 220);
}

.note_delete_icon:active {
    background: rgb(210, 210, 210);
}

.trash_div {
    /*margin-top: 5px;*/
    display: flex;
    align-items: center;
    cursor: pointer;
}

.notebook_item {
    height: 40px !important;
    line-height: 40px !important;
    background: rgb(245, 245, 245);
}

.notebook_item:hover {
    background: rgb(231, 231, 231);
    border-radius: 8px;
}

.notebook_item.active {
    background: rgb(245, 228, 227);
    border-radius: 8px;
}

.search-icon {
    display: flex;
    align-items: center;
    width: 240px;
    height: 40px;
    font-size: 14px;
    min-height: 27px;
    /*padding: 2px 10px;*/
    margin-left: 10px;
    margin-top: 1px;
    margin-bottom: 1px;
    cursor: pointer;
}

.search-icon:hover {
    background: rgb(235, 235, 235);
}

.bottom_two {
    z-index: 2;
    height: 44px;
    width: 250px;
    bottom: 0px;
    position: fixed;
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    border-top: 1px solid rgb(220, 220, 220);
    background: rgb(245, 245, 245);
}

.bottom_two:hover {
    background-color: rgb(220, 220, 220);
}

/*.two_option{*/
/*  position: absolute;*/
/*  display: flex;*/
/*  justify-content: space-evenly;*/
/*  align-items: center;*/
/*}*/

/* 左侧侧边栏图标 */
.file-space-icon {
    margin: 0 5px;
    font-size: 18px;
}

.notobook-icon {
    margin: 0 4px;
    font-size: 18px;
}

.note-icon {
    margin: 0 2px;
    font-size: 18px;
}

.mindmap-icon {
    margin-left: 5px;
    margin-right: 7px;
    font-size: 16px;
    font-weight: 700;
}

/* 搜索框中没有搜索到内容时的提示 */
.search_prompt {
    color: #6b778c;
    font-size: 20px;
    font-weight: 600;
}

/* 搜索结果列表中的文字区域的样式 */
.search-item-line-style {
    height: 30px;
    line-height: 30px;
    overflow: hidden;
    font-weight: bold;
    margin-left: 10px;
    margin-right: 10px;
    color: rgb(76, 76, 76);
}

/* 按照tag搜索列表中tag的样式 */
.search-item-tag-style {
    height: 20px;
    line-height: 20px;
    background-color: rgb(150, 150, 150);
    color: white;
    margin-right: 10px;
    padding: 2px 5px;
    font-size: 10px;
    border-radius: 5px;
}
</style>
