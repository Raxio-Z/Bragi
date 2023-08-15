<template>
    <div>
        <div style="height:calc(100vh - 50px);">
            <RelationGraph
                    ref="seeksRelationGraph"
                    :options="graphOptions"
                    :on-node-click="onNodeClick"
                    :on-line-click="onLineClick"/>
        </div>
    </div>
</template>

<script>
// 如果您没有在main.js文件中使用Vue.use(RelationGraph); 就需要使用下面这一行代码来引入relation-graph
import RelationGraph from 'relation-graph';
import global from "@/global";
// import axios from "axios";
import {reqNoteRefGraph, reqNoteRefJump} from "@/api";

export default {

    name: 'Demo',
    components: {RelationGraph},

    inject: ['fa_doSwitchNote'],

    data() {
        return {
            config: {
                headers: {
                    token: this.$store.getters.getToken,
                    username: this.$store.getters.getUsername
                }
            },
            isShowCodePanel: false,
            graphOptions: {
                debug: true,
                allowSwitchLineShape: true,
                allowSwitchJunctionPoint: true,
                allowShowDownloadButton: true,
                defaultJunctionPoint: 'border',
                // 这里可以参考"Graph 图谱"中的参数进行设置
                layoutName: 'center',

            }
        };
    },
    mounted() {
        reqNoteRefGraph(this.config).then(res => {
            // axios.get(global.HOST_URL+"/note/refgraph",this.config).then(res=>{
            res = res.data
            if (res.code === 0)
                console.log("res", res)
            var __graph_json_data = {
                rootId: 'a',
                nodes: [
                    {id: 'a', text: 'A', borderColor: 'yellow'},
                    {id: 'b', text: 'B', color: '#43a2f1', fontColor: 'yellow'},
                    {id: 'c', text: 'C', nodeShape: 1, width: 80, height: 60},
                    {id: 'e', text: 'E', nodeShape: 0, width: 150, height: 150}
                ],
                lines: [
                    {from: 'a', to: 'b', text: '关系1', color: '#43a2f1'},
                    {from: 'a', to: 'c', text: '关系2'},
                    {from: 'a', to: 'e', text: '关系3'},
                    {from: 'b', to: 'e', text: '', color: '#67C23A'}
                ]
            };

            if (res.data.lines.length !== 0) {
                __graph_json_data.rootId = res.data.lines[0].from
            }

            for (var i = 0; i < res.data.lines.length; i++) {
                res.data.lines[i].color = '#43a2f1'
            }
            for (var j = 0; j < res.data.nodes.length; j++) {
                res.data.nodes[j].width = 60
                res.data.nodes[j].height = 60
                res.data.nodes[j].borderWidth = 3
            }


            __graph_json_data.nodes = res.data.nodes
            __graph_json_data.lines = res.data.lines

            console.log("__graph_json_data", __graph_json_data)

            this.$refs.seeksRelationGraph.setJsonData(__graph_json_data, (graphInstance) => {
                // 这些写上当图谱初始化完成后需要执行的代码
            });

        })

    },
    methods: {
        onNodeClick(nodeObject, $event) {
            console.log('onNodeClick:', nodeObject);

            var value = nodeObject.id
            // console.log(value[1])
            let url = global.HOST_URL + "/note/path/" + value
            reqNoteRefJump(value, this.config).then(res => {
                // axios.get(url, this.config).then(res => {
                res = res.data
                if (res.code == 0) {
                    // console.log(res)
                    let strList = res.data.split('/')
                    let notebookName = strList[1]
                    let noteName = strList[2]

                    this.fa_doSwitchNote(noteName, notebookName)
                }
            })

        },
        onLineClick(lineObject, linkObject, $event) {
            console.log('onLineClick:', lineObject);
        }
    }
};
</script>

<style lang="scss">

</style>

<style lang="scss" scoped>

</style>
