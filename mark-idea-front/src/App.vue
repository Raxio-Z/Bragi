<template>
  <div id="app">
<!--    <navbar></navbar>-->
    <router-view/>
  </div>
</template>
<script>

// import Navbar from "@/components/navbar";

export default {
  // components: {Navbar},
  created() {
    this.$store.commit('setWebsiteTitle', document.title);
  }
}
</script>>

