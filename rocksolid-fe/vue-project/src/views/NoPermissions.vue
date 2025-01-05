<template>
  <div class="no-permission-container">
    <h5>Sorry</h5>
    <span>You are lacking the permission <b>Review articles</b>
      to access this object, please let us know on support@rocksolid.com if you think this is incorrect.
    <br>
    </span>
    <router-link to="/web/home" class="link">Home</router-link>
  </div>
</template>

<script>
export default {
  name: 'NoPermissions'
};
</script>

<style scoped>
.no-permission-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 50vh;
  text-align: center;
  flex-direction: column;
  font-size: .875rem;
}

h5 {
  font-weight: bold;
}

.link {
  color:           #32c8b4;
  text-decoration: none;
  font-size:       .875rem;
  font-weight:     bold;
}

.link:hover {
  text-decoration: underline;
}
</style>