<template>
    <Header v-if="!isAuthPage && !userIdPage" />
    <SideNav v-if="!isAuthPage" />
    <div class="body" :class="{ 'body-no-padding': userIdPage }">
      <router-view />
    </div>

</template>

<script setup>
import Header from '@/components/Header.vue';
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import SideNav from '@/components/Sidenav.vue';

const route = useRoute();

const isAuthPage = computed(() => {
  return route.path === '/login' || route.path === '/register';
});

const userIdPage = computed(() => {
  return /^\/admin\/users\/[^/]+$/.test(route.path);
});
</script>

<style scoped>
.body {
  padding-left: 12rem; /* Základný padding pre väčšinu stránok */
}

.body-no-padding {
  padding-left: 0;
}
</style>