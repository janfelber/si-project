<template>
  <div class="no-permission-container">
    <h5>Na toto nemáte oprávnenie</h5>
    <span>K tejto stránke má prístup iba <b>Recenzent</b>
      , ak si myslíte, že niekde nastala chyba, kontaktujte nás na rocksolid.supp@gmail.com.
    <br>
    </span>
    <router-link to="/web/active-conferences" class="link">Späť</router-link>
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