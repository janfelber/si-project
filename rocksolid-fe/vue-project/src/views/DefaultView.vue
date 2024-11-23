<template>
  <div class="default-layout">
    <header class="header">
    </header>
    <div class="content">
      <side-nav/>
      <main class="main">
        <router-view/>
      </main>
    </div>
  </div>
</template>

<script>
import SideNav from "@/components/Sidenav.vue";
import {RouterView} from "vue-router";

export default {
  name: "DefaultLayout",
  components: {RouterView, SideNav},
};
</script>

<style scoped>
.main {
  margin-left: 250px;
  padding: 20px;
}

</style>