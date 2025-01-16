<template>
  <main>
  <form class="login-form">
      <div>
        <div>
          <h3 class="text-center">Prihlásenie</h3>
          <div v-if="error" class="error-message-container">
            <span class="mdi mdi-close-circle-outline alert-icon"></span>
              <span>
                {{ error }}
              </span>
          </div>
          <div class="divider">
            <hr>
          </div>
          <form @submit.prevent="submitLogin">
            <div class="input-group">
              <input
                  type="text"
                  v-model="email"
                  placeholder="Email"
                  required
              />
            </div>
            <div class="input-group">
              <input
                  type="password"
                  v-model="password"
                  placeholder="Heslo"
                  required
              />
            </div>
            <button type="submit" class="login-button">
              <span style="font-size: 0.875rem" class="text-center">Prihlásiť</span>
            </button>
          </form>
          <span class="register-prompt">
          Nemáte účet?
          <router-link class="register" to="/register">Zaregistruj sa!</router-link>
        </span>
        </div>
      </div>
    </form>
  </main>
</template>

<script>
import axios from 'axios';
export default {
  name: "LoginView",
  data() {
    return {
      email: '',
      password: '',
      error: null,
    };
  },
  methods: {
    async submitLogin() {
      try {
        const response = await axios.post('http://localhost:8080/api/v1/auth/login', {
          email: this.email,
          password: this.password,
        });
        console.log(response);
        localStorage.setItem('token', response.data.access_token);
        this.$router.push(response.data.redirect_url);
      } catch (err) {
        this.error = "Neznámy používateľ alebo nesprávne heslo. Skúste to znova.";
      }
    }
  }
};
</script>

<style scoped>

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
}

.alert-icon {
  color: red;
  font-size: 1.25rem;
  margin-top: 0.25rem;
}

.error-message-container span {
  display: block;
}

.login-form {
  margin: auto;
  max-width: 20rem;
  padding: 2rem 0;
  width: 100%;
}

main {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

h3 {
  font-size: 1.625rem;
  font-family: "Axiforma", sans-serif;
  font-weight: 600;
  line-height: 1.2;
}

.divider {
  padding-right: 1rem;
  padding-left: 1rem
}


.register-prompt {
  display: block;
  text-align: center;
  margin-top: 20px;
  font-size: 1rem;
  font-weight: 500;
  color: #333;
}

.register {
  text-decoration: none;
  color: darkgreen;
  font-weight: bold;
  transition: color 0.3s, text-decoration 0.3s;
}

.register:hover {
  color: #3c8d40;
  text-decoration: underline;
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

.login-button {
  width: 100%;
  padding: 4px;
  background-color: darkgreen;
  border: none;
  border-radius: 5px;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}
</style>
