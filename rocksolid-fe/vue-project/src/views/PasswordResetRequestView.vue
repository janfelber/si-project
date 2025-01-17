<template>
  <main>
    <form class="email-form">
      <div>
        <div>
          <div v-if="error" class="error-message-container">
            <span class="mdi mdi-close-circle-outline alert-icon"></span>
            <span>
                {{ error }}
              </span>
          </div>
          <div v-if="!error" class="info-message-container">
            <span class="mdi mdi-information-outline info-icon"></span>
            <span>
                Prosím zadajte svoj e-mail
              </span>
          </div>
          <form @submit.prevent="requestPasswordReset">
            <div class="input-group">
              <input
                  type="email"
                  v-model="email"
                  placeholder="E-mail"
                  required
              />
            </div>
            <div class="button-container">
              <button type="submit" class="continue-button">
                <span style="font-size: 0.875rem" class="text-center" @click="confirm()">Pokračovať</span>
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
import router from '@/router/index.js';
import axios from 'axios';

export default {
  name: 'PasswordResetRequestView',
  data() {
    return {
      email: '',
      error: null,
    };
  },
  methods: {
    confirm() {
      router.push({ name: 'confirmationPasswordRequest' });
    },
    backToLogin() {
      router.push({ name: 'login' });
    },
    async requestPasswordReset() {
      try {
        await axios.post('http://localhost:8080/api/v1/auth/request-reset-password',
            {
              email: this.email
            }
        );
      } catch (error) {
        if (error.response && error.response.status === 404) {
          this.error = 'Nemožno nájsť používateľa s týmto e-mailom. Prosím skúste znova.';
        }
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
  min-height: 70vh;
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
  width: 32%;
  padding: 6px;
  background-color: darkgreen;
  border: none;
  border-radius: 5px;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
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