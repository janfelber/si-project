<template>
  <div class="default-layout">
    <header class="header">
    </header>
    <div class="content">
      <side-nav layout="admin"/>
      <main class="main">
        <router-view/>
      </main>
    </div>
  </div>
</template>

<script>
import {RouterView} from "vue-router";
import SideNav from "@/components/Sidenav.vue";

export default {
  name: "AdminLayout" ,
  components: {SideNav, RouterView}
}

</script>

<style scoped>
.main {
  flex-grow: 1;
  background: #f8f9fa;
  height: 100%;
  padding-left: 11.5rem;
}
</style>
