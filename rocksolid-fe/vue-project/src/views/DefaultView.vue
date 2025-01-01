<template>
  <div class="default-layout">
    <header class="header">
      <nav class="header-nav">
        <div class="active-router">
          <span class="router-label">Users</span>
        </div>
        <div class="user-item">
          <span>{{ routeName }} </span>
        </div>
      </nav>
      <div class="vertical-divider"></div>
    </header>
    <div class="content">
      <side-nav layout="default"/>
      <main class="main">
        <router-view/>
      </main>
    </div>
  </div>
</template>

<script>
import SideNav from "@/components/Sidenav.vue";
import {RouterView} from "vue-router";
import router from '../router/index.js';

export default {
  name: "DefaultLayout",
  methods: {
    router() {
      return router
    }
  },
  computed: {
    routeName() {
      return this.$route.meta.title || '';
    },
  },
  components: {RouterView, SideNav},
};
</script>

<style scoped>
.main {
  margin-left: 250px;
  padding: 20px;
}

hr {
  border: 0;
  border-top: 1px solid darkgrey;
  margin-bottom: .75rem;
  margin-top: .75rem;
}

.user-item {
  padding-left : 9rem;
}

label {
  width: 10rem;
  align-self: center;
}

.header-nav {
  margin-left: 1.75rem;
  display: flex;
}

.vertical-divider {
  width: 1px;
  height: 35px;
  background-color: #d8d8f0;
  margin: 0 1rem;
}

header {
  background-color: white;
  display: flex;
  height: 60px;
  align-items: center;
  border-bottom: 1px solid #d8d8f0;
}

</style>