<template>
  <div class="not-found">
    <h5>Not found.Sorry
      <br>
      Return to
      <router-link to="/web/home">home page</router-link>
      .
    </h5>
    <p></p>
  </div>
</template>

<script>
export default {
  name: 'NotFoundView'
}
</script>

<style scoped>

h5 {
  font-weight: bold;
}
.not-found {
  text-align: center;
  margin-top: 50px;
}
</style>