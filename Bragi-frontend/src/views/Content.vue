<template>
    <div>
        <el-container>

            <el-header class="header-style">
                <div>
                    <el-popover placement="right" width="50" title="笔记标签" trigger="click">
                        <i class="iconfont icon-a-yijianfankuijibiji path-icon" slot="reference"></i>
                        <div v-if="this.curNoteTag.length === 0">
                            当前笔记还未添加标签
                        </div>
                        <el-tag v-for="(item, index) in this.curNoteTag" :key="index" class="tag-style"
                                v-contextmenu:tagRightMenu
                                @contextmenu.native.prevent="setTagRightMenu(item.tagName)"
                                v-click-outside="hideTagRightMenu">
                            {{ item.tagName }}
                        </el-tag>
                    </el-popover>
                    <el-breadcrumb separator-class="el-icon-arrow-right"
                                   style="line-height: 40px; font-size: 15px; float:left;">
                        <el-breadcrumb-item>{{ notebookName }}</el-breadcrumb-item>
                        <el-breadcrumb-item>{{ title }}</el-breadcrumb-item>
                    </el-breadcrumb>
                </div>
                <div class="icon-area">

                    <el-tooltip class="item" effect="dark" content="添加笔记标签" placement="bottom">
                        <i class="el-icon-plus" @click="addNoteTag"></i>
                    </el-tooltip>

                    <el-tooltip class="item" effect="dark" content="思维导图" placement="bottom">

                        <i @click="handleShowMindMap">
                            <svg t="1683895875499" style="width: 20px;height: 30px" viewBox="0 0 1024 1024"
                                 version="1.1"
                                 xmlns="http://www.w3.org/2000/svg" p-id="1810">
                                <path
                                        d="M341.333 456.533a55.467 55.467 0 0 1 0 110.934H170.667a55.467 55.467 0 1 1 0-110.934h170.666zM170.667 384a128 128 0 0 0 0 256h170.666a128 128 0 1 0 0-256H170.667z m618.666-140.8a34.133 34.133 0 0 1 0 68.267h-128a34.133 34.133 0 0 1 0-68.267h128z m-128-72.533a106.667 106.667 0 0 0 0 213.333h128a106.667 106.667 0 0 0 0-213.333h-128z"
                                        fill="#333333" p-id="1811"></path>
                                <path
                                        d="M554.667 277.333c0-17.45 4.181-33.92 11.605-48.426-77.44 12.416-147.456 37.077-203.947 70.997-38.613 23.083-72.106 51.499-96.981 84.096h75.99c7.978 0 15.786 0.725 23.338 2.133 10.496-8.32 22.187-16.384 34.987-24.064 43.093-25.898 97.194-46.165 158.378-58.026a106.667 106.667 0 0 1-3.37-26.71zM364.672 637.867A127.657 127.657 0 0 1 341.333 640h-75.946c24.789 32.597 58.325 60.97 96.938 84.139 56.491 33.877 126.507 58.538 203.947 70.997a106.24 106.24 0 0 1-8.235-75.093c-61.184-11.947-115.242-32.214-158.421-58.112-12.8-7.68-24.448-15.702-34.987-24.064z"
                                        fill="#333333" p-id="1812"></path>
                                <path
                                        d="M789.333 712.533a34.133 34.133 0 0 1 0 68.267h-128a34.133 34.133 0 1 1 0-68.267h128z m-128-72.533a106.667 106.667 0 0 0 0 213.333h128a106.667 106.667 0 0 0 0-213.333h-128z"
                                        fill="#333333" p-id="1813"></path>
                            </svg>
                        </i>

                    </el-tooltip>

                    <el-tooltip class="item" effect="dark" content="历史记录" placement="bottom">
                        <i class="iconfont icon-history-full" @click="handleShowHistory"></i>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="设置" placement="bottom">
                        <i class="el-icon-s-tools" @click="handleShowSetting"></i>
                    </el-tooltip>

                </div>
            </el-header>

            <el-main class="editor" style="overflow: unset">
                <Editor ref="editor" @saveContent="ch_handleSaveContent" @renameTitle="doHandleRenameTitle"
                        @jumpSelect="ch_doSwitchNote"/>
            </el-main>

            <!-- 添加笔记tag弹窗 -->
            <el-dialog :visible.sync="showAddTagDialog" width="20%" custom-class="add-tag-dialog">
                <template slot="title">
                    <div class="add-tag-title">
                        添加笔记tag
                    </div>
                </template>
                <span class="add-tag-prompt">请输入tag的名称：</span>
                <el-input v-model="addTagName" placeholder="tag名称" ref="tagNameInput"></el-input>
                <span slot="footer">
          <el-button @click="showAddTagDialog = false">取 消</el-button>
          <el-button type="primary" @click="handleAddTag">确 定</el-button>
        </span>
            </el-dialog>


            <el-dialog :title="title" :visible.sync="showMindMap" width="60%">
                <span slot="title" style="font-size: 17px;margin-left: 20px">思维导图</span>
                <Mindmap ref="mindMap" :mindValue="mindMapValue" style="height: 450px"></Mindmap>
            </el-dialog>
            <!-- 预览历史版本弹框 -->
            <el-dialog :visible.sync="showHistoryPreview" width="60%" @opened="handleOpenHistoryPrev">
                <history-preview ref="historyPreview"></history-preview>
            </el-dialog>
            <el-aside class="history-aside" width="220px" v-if="showHistory">
                <div style="padding-left: 10px;padding-top: 15px;">
                    <div style="font-size:18px;padding-bottom: 5px; border">
                        <i class="iconfont icon-jurassic_version history-icon"></i>
                        <strong>历史版本</strong>
                    </div>
                    <div v-for="version of curNoteVersion" :key="version.ref">
                        <span style="font-size:13px;margin-right:5px">{{ version.date }}</span>
                        <el-button v-show="version.ref !== curRef" size="mini" type="text" style="font-size:13px"
                                   @click="handleRecover(version.ref)">恢复
                        </el-button>
                        <el-button v-show="version.ref !== curRef" size="mini" type="text" style="font-size:13px;"
                                   @click="handlePreviewHistory(version.ref)">预览
                        </el-button>
                    </div>
                </div>
            </el-aside>

            <v-contextmenu theme="bright" ref="tagRightMenu" v-show="showTagRightMenu">
                <v-contextmenu-item @click="handleDelTag">删除</v-contextmenu-item>
            </v-contextmenu>

        </el-container>
    </div>
</template>

<script>
import Editor from '@/components/Editor.vue'
import HistoryPreview from '@/components/HistoryPreview.vue'
import global from "@/global";
import axios from "axios";
import Mindmap from "@/components/Mindmap.vue";
import {
    reqHistoryPostQuery,
    reqHistoryRecover,
    reqNoteAddTag, reqNoteDelTag,
    reqNoteGetNoteHistory,
    reqNoteGetNoteText, reqNoteGetTag,
    reqRenameNote
} from "@/api";

export default {
    name: 'Home',
    components: {
        Mindmap,
        Editor,
        HistoryPreview
    },
    data() {
        return {
            pageTitle: 'Bragi',

            // 是否显示笔记的历史记录
            showHistory: false,
            // 是否展示历史预览
            showHistoryPreview: false,
            //是否显示设置窗口
            showSetting: false,
            // 是否显示添加tag的弹框
            showAddTagDialog: false,

            config: {
                headers: {
                    token: this.$store.getters.getToken,
                    username: this.$store.getters.getUsername
                }
            },
            curNote: {
                content: ''
            },
            notebookName: null,
            title: "",

            // 当前笔记的历史版本
            curNoteVersion: [],

            // 当前笔记的所有tag
            curNoteTag: [],

            // 是否显示tag右键菜单项
            showTagRightMenu: false,

            // 待添加的tag的名称
            addTagName: "",

            // 待删除的tag的名称
            tagToDelete: "",

            curRef: null,

            previewRef: null,

            showMindMap: false,
            mindMapValue: ''
        }
    },

    inject: ['fa_handleSaveContent', 'fa_doSwitchNote'],

    methods: {
        handleShowSetting() {
            this.$router.push({
                    name: "setting"
                }
            )
        },
        doHandleRenameTitle(newTitle) {
            let targetNotebook = this.curNotebook.notebookName;
            let url = global.HOST_URL + "/note/" + targetNotebook + "/" + newTitle;
            let param = {
                srcNotebook: targetNotebook,
                srcTitle: this.curNote.noteTitle,
                move: true
            }
            reqRenameNote(targetNotebook, newTitle, param, this.config).then(res => {
                // axios.put(url, param, this.config).then(res => {
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

        handleShowMindMap() {
            console.log('点击了思维导图')
            this.showMindMap = true

            let url = global.HOST_URL + "/note/" + this.$route.params.notebookName + "/" + this.$route.params.noteTitle;
            reqNoteGetNoteText(this.$route.params.notebookName, this.$route.params.noteTitle, this.config)(res => {
                // axios.get(url, this.config).then(res => {
                res = res.data;
                if (res.code === 0) {
                    this.mindMapValue = res.data
                } else {
                    console.log('出错了')
                }
            })
        },

        ch_handleSaveContent(content, noteTitle, notebookName) {
            this.fa_handleSaveContent(content, noteTitle, notebookName);

            // 保存笔记时关闭历史记录
            this.showHistory = false;
        },
        ch_doSwitchNote(noteTitle, notebookName) {
            this.fa_doSwitchNote(noteTitle, notebookName)
        },
        handleShowHistory() {
            this.showHistory = !this.showHistory;

            let url = global.HOST_URL + "/note/" + this.notebookName + "/" + this.title + "/history";
            reqNoteGetNoteHistory(this.notebookName, this.title, this.config).then(res => {
                // axios.get(url, this.config).then(res => {
                res = res.data;
                if (res.code === 0) {
                    console.log(res)
                    this.curNoteVersion = res.data;
                    this.curRef = res.data[0].ref
                }
            })
        },

        // 显示历史记录预览窗口
        handlePreviewHistory(ref) {
            this.showHistoryPreview = true;
            this.previewRef = ref;
        },

        // 获取某个历史版本的内容
        handleOpenHistoryPrev() {
            let url = global.HOST_URL + "/history/queryHistoryContent";
            let req = {
                notebookName: this.notebookName,
                noteTitle: this.title,
                versionRef: this.previewRef,
            };
            reqHistoryPostQuery(req, this.config).then((res) => {
                // axios.post(url, req, this.config).then((res) => {
                res = res.data;
                if (res.code !== 0) {
                    this.$notify({
                        type: "error",
                        message: "获取历史版本内容失败",
                        duration: 1000,
                    });
                    return;
                }
                this.$refs.historyPreview.setText(res.data);
            });
        },

        // 恢复笔记至某一版本
        handleRecover(ref) {
            let url = global.HOST_URL + "/note/" + this.notebookName + "/" + this.title;
            let request = {
                versionRef: ref,
            };
            reqHistoryRecover(this.notebookName, this.title, request, this.config).then((res) => {
                // axios.post(url, request, this.config).then((res) => {
                res = res.data;
                if (res.code === 0) {
                    // 更新当前vditor中显示的内容
                    this.curNote.content = res.data;
                    this.$refs.editor.setContent(this.title, res.data, this.notebookName);

                    // 展示新的历史记录
                    this.handleShowHistory();
                }
            })

        },

        // 跳出添加笔记tag弹框
        addNoteTag() {
            this.showAddTagDialog = true;
        },

        // 给笔记添加tag
        handleAddTag() {
            if (this.addTagName === "") {
                this.$notify({
                    type: "warning",
                    message: "标签名称不能为空",
                    duration: 1000,
                });
            } else {
                let url = global.HOST_URL + "/note/tag";
                let request = {
                    notebookName: this.notebookName,
                    noteName: this.title,
                    tagName: this.addTagName
                };
                reqNoteAddTag(request, this.config).then((res) => {
                    // axios.post(url, request, this.config).then((res) => {
                    res = res.data;
                    if (res.code === 0) {
                        this.$message.success("笔记标签添加成功");
                        this.showAddTagDialog = false;
                        this.addTagName = "";

                        // 更新当前笔记的tag列表
                        this.getNoteTag();
                        // 更新主界面中的notebookList
                        this.$emit('updateNotebookList');
                    } else {
                        this.$notify({
                            type: "error",
                            message: res.msg,
                            duration: 1000
                        });
                        this.$refs.tagNameInput.focus();
                    }
                })
            }
        },

        // 获取笔记的所有tag
        getNoteTag() {
            let url = global.HOST_URL + "/note/tag";
            // reqNoteGetTag(this.notebookName,this.title,this.config)((res) => {
            axios.get(url + "?notebookName=" + this.notebookName + "&noteName=" + this.title, this.config).then((res) => {
                res = res.data;
                if (res.code === 0) {
                    this.curNoteTag = res.data;
                }
            });
        },

        setTagRightMenu(tagName) {
            this.tagToDelete = tagName;
            this.showTagRightMenu = true;
        },

        hideTagRightMenu() {
            this.showTagRightMenu = false;
        },

        // 删除当前笔记的某个tag
        handleDelTag() {
            let url = global.HOST_URL + "/note/tag";
            reqNoteDelTag(this.notebookName, this.title, this.tagToDelete, this.config).then((res) => {
                // axios.delete(url + "?notebookName=" + this.notebookName + "&noteName=" + this.title + "&tagName=" + this.tagToDelete, this.config).then((res) => {
                res = res.data;
                if (res.code === 0) {
                    this.$message.success("标签删除成功");
                    // 更新笔记的tag
                    this.getNoteTag();
                    this.tagToDelete = "";

                    // 更新主界面中的notebookList
                    this.$emit('updateNotebookList');
                } else {
                    this.$notify({
                        type: "error",
                        message: res.msg,
                        duration: 1000
                    });
                }
            });
        },

        handleCommand(command) {
            if (command === "a") {
                this.handleShowMindMap()
            }
        }


    },
    mounted() {
        // document.title = this.$store.getters.getWebsiteTitle;
        // this.validateUserAndInit();

        this.notebookName = this.$route.params.notebookName
        this.title = this.$route.params.noteTitle

        // 更新笔记的tag
        this.getNoteTag();

        let url = global.HOST_URL + "/note/" + this.$route.params.notebookName + "/" + this.$route.params.noteTitle;

        console.log('预备！')

        reqNoteGetNoteText(this.$route.params.notebookName, this.$route.params.noteTitle, this.config).then(res => {
            // axios.get(url, this.config).then(res => {
            res = res.data;
            if (res.code === 0) {

                console.log('准备给内容了！')
                console.log(this.$route.params.noteTitle, res.data)

                this.curNote.content = res.data
                // this.$refs.mindMap.setMindContent(res.data)
                setTimeout(() => {
                    this.$refs.editor.setContent(this.$route.params.noteTitle, res.data, this.$route.params.notebookName)
                }, 200)
                // this.$refs.editor.setContent(this.$route.params.noteTitle, res.data, this.$route.params.notebookName)
            } else {
                console.log('出错了')
            }
        })
    },
    beforeRouteUpdate(to, from, next) {
        // 获取新的路由参数
        const {notebookName, noteTitle} = to.params
        // 如果新的参数与原来的不同，更新页面内容
        if (notebookName !== this.notebookName || noteTitle !== this.noteTitle) {
            this.notebookName = notebookName;
            this.title = noteTitle;

            // 更新笔记的tag
            this.getNoteTag();

            // 更新笔记的历史记录
            this.handleShowHistory();
            // 跳转后不显示历史记录
            this.showHistory = false;

            // 根据新的路由参数更新页面内容
            let url = global.HOST_URL + "/note/" + notebookName + "/" + noteTitle;

            console.log('预备！')
            reqNoteGetNoteText(notebookName, noteTitle, this.config).then(res => {
                // axios.get(url, this.config).then(res => {
                res = res.data;
                if (res.code === 0) {

                    console.log('准备给内容了！')

                    console.log(this.$route.params.noteTitle, res.data)
                    this.curNote.content = res.data

                    // this.$refs.mindMap.setMindContent(res.data)

                    this.$refs.editor.setContent(this.$route.params.noteTitle, res.data, this.$route.params.notebookName)
                    this.$refs.editor.setContent(this.$route.params.noteTitle, res.data, this.$route.params.notebookName)
                } else {
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
.header-style {
    height: 40px !important;
    line-height: 40px;
}

.history-aside {
    position: absolute;
    right: 0;
    background: white;
    z-index: 3;
    margin-top: 40px;
    width: 250px !important;
    height: calc(100% - 40px);
    /* border:1px solid black; */
    box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.3);
}

/* 设置历史记录框中滚动条的样式 */
.history-aside::-webkit-scrollbar {
    width: 10px;
}

.history-aside::-webkit-scrollbar-thumb {
    background: #ccc;
    border-radius: 5px;
}

.history-aside::-webkit-scrollbar-thumb:hover {
    background: rgb(180, 180, 180);
}

/* 标题栏右侧图标区域 */
.icon-area {
    float: right;
}

.icon-area i {
    display: block;
    float: left;
    width: 30px;
    height: 30px;
    margin-top: 5px;
    margin-right: 5px;
    font-size: 20px;
    line-height: 30px;
    text-align: center;
    color: rgb(50, 50, 50);
}

.icon-area i:hover {
    background-color: rgb(240, 240, 240);
}

.icon-area i:active {
    background-color: rgb(230, 230, 230);
}

.icon-area i:active {
    background-color: rgb(230, 230, 230);
}


.path-icon {
    display: block;
    float: left;
    font-size: 20px;
    line-height: 42px;
    margin-right: 8px;
}

.path-divider {
    color: rgb(200, 200, 200);
    font-weight: 500;
}

.editor {
    /* border: 1px solid rgb(240, 237, 237); */
    padding-top: 15px;
}

.history-icon {
    font-size: 25px;
    margin-right: 5px;
}

/* 标题栏更多按钮弹框中的各个项 */
.header-more-item {
    height: 30px;
    line-height: 30px;
    padding-left: 10px;
}

.header-more-item:hover {
    background-color: rgb(240, 240, 240);
}

.header-more-item:active {
    background-color: rgb(230, 230, 230);
}

.add-tag-dialog {
    border-radius: 20px;
}

.add-tag-title {
    margin-left: 20px;
    color: rgb(100, 100, 100);
    font-size: 18px;
}

.add-tag-prompt {
    display: block;
    margin-bottom: 15px;
    font-size: 15px;
}

.tag-style {
    display: block;
    background-color: rgb(200, 200, 200);
    height: 25px;
    line-height: 25px;
    margin: 5px 0;
    border-radius: 5px;
    padding-left: 10px;
    color: white;
    overflow: hidden;
}
</style>
