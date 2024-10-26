<template>
  <header style="padding-bottom: 66px">
    <Navigation/>
  </header>
  <RouterView />
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router'
import Navigation from "@/components/Navigation.vue";
</script>




<style scoped>


</style>
