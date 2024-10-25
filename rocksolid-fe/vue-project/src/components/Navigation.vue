<template>
  <header class="header">
    <nav class="nav">
      <h1>RockSolid</h1>
      <ul class="router-links">
        <li>
          <router-link to="/home" class="router-link">Home</router-link>
        </li>
        <li>
          <router-link to="/login" class="router-link">Login</router-link>
        </li>
      </ul>
    </nav>
  </header>
</template>

<script>
export default {
  name: "Navigation",
};
</script>

<style scoped>

.header {
  width: 100%;
  background-color: darkgreen;
  color: white;
  padding: 1em;
  position: fixed;
  top: 0;
  left: 0;
}

.nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}



.router-links {
  list-style: none;
  display: flex;
  gap: 1em;
}

.router-link {
  color: white;
  font-size: 15px;
}

.router-link:hover {
  text-decoration: underline;
  font-size: 17px;
}

main {
  padding-top: 70px;
}
</style>
