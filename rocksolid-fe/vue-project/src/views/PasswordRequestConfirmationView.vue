

<template>
<main>
<div class="confirmation">
<h3 class="text-center"> Pozri si svoj e-mail.</h3>
<span class="text-center"> Poslali sme ti email. Klikni na link v e-maile pre zmenu hesla.</span>
<div class="text-center" style="padding-top: 1rem">
  <button type="button" class="back-button">
    <span class="text-center back-button" @click="backToLogin()">Späť</span>
  </button>
</div>
</div>
</main>
</template>

<script>
export default {
  name: 'PasswordRequestConfirmationView',
  methods: {
    backToLogin() {
      this.$router.push({ name: 'login' });
    },
  },
};
</script>

<style scoped>
main {
  display: flex;
  flex-direction: column;
  min-height: 70vh;
}

.confirmation {
  margin: auto;
  max-width: 20rem;
  padding: 2rem 0;
  width: 100%;
}

.back-button {
  font-size: 0.875rem;
  color: darkgreen;
  font-weight: 600;
}

h3 {
  font-family: Axiforma,sans-serif;
  font-weight: 600;
  line-height: 1.2;
  margin-bottom: .375rem;
}
</style>