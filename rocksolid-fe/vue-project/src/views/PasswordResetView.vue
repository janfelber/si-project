<template>
  <main>
  <form class="email-form">
    <div>
      <div>
        <div v-if="error && !success_reset" class="error-message-container">
          <span class="mdi mdi-close-circle-outline alert-icon"></span>
          <span>
                {{ error }}
              </span>
        </div>
        <div v-if="!error && !success_reset" class="info-message-container">
          <span class="mdi mdi-information-outline info-icon"></span>
          <span>
                Prosím zadajte svoje nové heslo
              </span>
        </div>
        <div v-if="success_reset" class="success-message-container">
          <span class="mdi mdi-check-circle success-icon"></span>
          <span>
                Zmena hesla prešla úspešne. Späť na prihlásenie <router-link to="/login" style="text-decoration: none">login</router-link>
              </span>
        </div>
        <form @submit.prevent="resetPassword">
          <div class="input-group">
            <input
                type="password"
                v-model="newPassword"
                placeholder="Nové heslo"
                required
            />
          </div>
          <div class="input-group">
            <input
                type="password"
                v-model="confirmPassword"
                placeholder="Potvrďte heslo"
                required
            />
          </div>
          <div class="button-container">
            <button type="submit" class="continue-button">
              <span style="font-size: 0.875rem" class="text-center">Uložiť</span>
            </button>
            <button type="button" class="back-button">
              <span style="font-size: 0.875rem" class="text-center" @click="backToLogin()">Späť</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </form>
  </main>
</template>

<script>

import axios from 'axios';

export default {
  name: 'PasswordResetView',
  props: {
    token: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      newPassword: '',
      confirmPassword: '',
      error: null,
      success_reset: false
    };
  },
  methods: {
    backToLogin() {
      this.$router.push({ name: 'login' });
    },
    isPasswordValid(password) {
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
      return passwordRegex.test(password);
    },
    async resetPassword() {
      if (this.newPassword !== this.confirmPassword) {
        this.error = 'Heslá sa nezhodujú!';
        return;
      }

      if (!this.isPasswordValid(this.newPassword)) {
        this.error = 'Heslo musí mať aspoň 8 znakov, obsahovať veľké písmeno, malé písmeno, číslo a špeciálny znak.';
        return;
      }



      try {
        const response = await axios.post('http://localhost:8080/api/v1/auth/reset-password', {
          token: this.token,
          password: this.newPassword,
        });

        console.log(response);
        this.success_reset = true;
        setTimeout(() => {
          this.$router.push({ name: 'login' });
        }, 5000);
      } catch (error) {
        this.error = 'Nepodarilo sa zmeniť heslo. Skúste znova.';
      }
    },
  }
};
</script>

<style scoped>
.button-container {
  display: flex;
}

.error-message-container {
  display: flex;
  align-items: flex-start;
  gap: .5rem;
  border-radius: .5rem;
  border: 1px solid #fd6f6f;
  background-color: snow;
  color: #333638;
  font-size: .875rem;
  font-weight: 500;
  padding: .5625rem .75rem;
  line-height: 1.25rem;
  margin-bottom: 1rem;
}

.info-message-container {
  display:          flex;
  align-items:      flex-start;
  gap:              .5rem;
  border-radius:    .5rem;
  border:           1px solid #67a6e4;
  background-color: #f5fafe;
  color:            #333638;
  font-size:        .875rem;
  font-weight:      500;
  padding:          .5625rem .75rem;
  line-height:      1.25rem;
  margin-bottom: 1rem;
}

.info-icon {
  color: #67a6e4;
  font-size: 1.25rem;
}

.alert-icon {
  color: red;
  font-size: 1.25rem;
}

.error-message-container span {
  display: block;
}

.email-form {
  margin: auto;
  max-width: 20rem;
  padding: 2rem 0;
  width: 100%;
}

main {
  display: flex;
  flex-direction: column;
  min-height: 80vh;
}

.input-group {
  width: 100%;
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: black;
}

.input-group input {
  width: 100%;
  padding: 7px;
  border: 1px solid grey;
  border-radius: 5px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.input-group input:focus {
  outline: none;
  border-color: darkgreen;
}

.continue-button {
  width: 22%;
  padding: 6px;
  background-color: darkgreen;
  border: none;
  border-radius: 5px;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.success-message-container {
  display:          flex;
  align-items:      flex-start;
  gap:              .5rem;
  border-radius:    .5rem;
  border:           1px solid green;
  background-color: rgba(0, 255, 0, 0.09);
  color:            #333638;
  font-size:        .875rem;
  font-weight:      500;
  padding:          .5625rem .75rem;
  line-height:      1.25rem;
  margin-bottom: 1rem;
}

.success-icon {
  color: green;
  font-size: 1.25rem;
  margin-top: 0.25rem;
}

.back-button {
  width: 20%;
  padding: 6px;
  border: none;
  border-radius: 5px;
  color: black;
  font-weight: bold;
  cursor: pointer;
}
</style>