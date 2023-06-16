<template>
  <div>
    <svg id="svg_id" style="width: 100%;height: 90%" className="flex-1" ref="svgRef"/>
    <div>
      <el-button type="primary" @click="exportToSvg">导出为SVG</el-button>
      <el-button type="primary" @click="exportToHTML">导出为可交互HTML</el-button>
    </div>

  </div>
</template>

<script>
/* eslint-disable */
import {ref, onMounted, onUpdated} from 'vue';
import {Transformer} from 'markmap-lib';
import {Markmap} from 'markmap-view/dist/index.esm';

import FileSaver from 'file-saver'

const transformer = new Transformer();
export default {
  name: 'Mindmap',
  data() {
    return {
      root:Object,
    };
  },
  props:{
    mindValue:{
      type:String,
      require:true,
    }
  },
  watch: {
    mindValue: 'update',
  },
  methods: {
    update() {
      const {root} = transformer.transform(this.mindValue);

      console.log("root",root)
      this.root=root

      this.mm.setData(root);
      this.mm.fit();
    },
    // setMindContent(value){
    //   const {root} = transformer.transform(value);
    //   this.mm.setData(root);
    //   this.mm.fit();
    // }


    exportToSvg() {
      let contentHtml = document.getElementById("svg_id").innerHTML
      let content = `<svg xmlns="http://www.w3.org/2000/svg" class="w-screen h-screen leading-none markmap mm-ojrsbz-1" style="">
    <style>.markmap{font:300 16px/20px sans-serif}.markmap-link{fill:none}.markmap-node&gt;circle{cursor:pointer}.markmap-foreign{display:inline-block}.markmap-foreign
        a{color:#0097e6}.markmap-foreign a:hover{color:#00a8ff}.markmap-foreign
        code{background-color:#f0f0f0;border-radius:2px;color:#555;font-size:calc(1em - 2px)}.markmap-foreign :not(pre)&gt;code{padding:.2em
        .4em}.markmap-foreign del{text-decoration:line-through}.markmap-foreign em{font-style:italic}.markmap-foreign
        strong{font-weight:bolder}.markmap-foreign mark{background:#ffeaa7}.markmap-foreign pre,.markmap-foreign
        pre[class*=language-]{margin:0;padding:.2em .4em}
    </style>
${contentHtml}
</svg>`;
      const blob = new Blob([content])		// fileStream 是文件流，一般从后台获取
      FileSaver.saveAs(blob, 'Mindmap.svg')					// fileName 保存文件的名称，需要带后缀

    },

    exportToHTML() {
      let contentHtml = JSON.stringify(this.root)
      let front = `<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Markmap</title>
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        #mindmap {
            display: block;
            width: 100vw;
            height: 100vh;
        }
    </style>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/markmap-toolbar@0.14.4/dist/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.16.0/dist/katex.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/prismjs@1.28.0/themes/prism.css">
</head>
<body>
<svg id="mindmap"></svg>
<script src="https://cdn.jsdelivr.net/npm/d3@6.7.0"><\/script>
<script src="https://cdn.jsdelivr.net/npm/markmap-view@0.14.4"><\/script>

<script>(e => {
  window.WebFontConfig = {
    custom: {families: ["KaTeX_AMS", "KaTeX_Caligraphic:n4,n7", "KaTeX_Fraktur:n4,n7", "KaTeX_Main:n4,n7,i4,i7", "KaTeX_Math:i4,i7", "KaTeX_Script", "KaTeX_SansSerif:n4,n7,i4", "KaTeX_Size1", "KaTeX_Size2", "KaTeX_Size3", "KaTeX_Size4", "KaTeX_Typewriter"]},
    active: () => {
      e().refreshHook.call()
    }
  }
})(() => window.markmap)<\/script>
<script src="https://cdn.jsdelivr.net/npm/webfontloader@1.6.28/webfontloader.js" defer><\/script>
<script>(() => {
  setTimeout(() => {
    const {markmap: Fe, mm: ur} = window, Fr = new Fe.Toolbar;
    Fr.attach(ur);
    const Dr = Fr.render();
    Dr.setAttribute("style", "position:absolute;bottom:20px;right:20px"), document.body.append(Dr)
  })
})()<\/script>
<script>((B, S, M, _) => {
  const I = B();
  window.mm = I.Markmap.create("svg#mindmap", (S || I.deriveOptions)(_), M)
})(() => window.markmap, null,`;

      let end = `, {"colorFreezeLevel": 2})<\/script>
</body>
</html>`

      let content = front+contentHtml+end

      const blob = new Blob([content])		// fileStream 是文件流，一般从后台获取
      FileSaver.saveAs(blob, 'Mindmap.html')					// fileName 保存文件的名称，需要带后缀

    },

  },
  mounted() {
    this.mm = Markmap.create(this.$refs.svgRef);
    this.update();
  },
};
</script>
