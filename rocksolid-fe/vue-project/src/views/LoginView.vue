<template>
  <div class="container">
    <div class="row">
      <div class="column">
        <div class="login-container">
          <h2>Login</h2>
          <form @submit.prevent="submitLogin">
            <div class="input-group">
              <label for="username">Email:</label>
              <input
                  type="text"
                  v-model="email"
                  placeholder="Enter email"
                  required
              />
            </div>
            <div class="input-group">
              <label for="password">Password:</label>
              <input
                  type="password"
                  v-model="password"
                  placeholder="Enter password"
                  required
              />
            </div>
            <button type="submit" class="login-button">Login</button>
          </form>
          <router-link class="register" to="/register">Registrovať</router-link>

          <div v-if="error" class="error-message">
            {{ error }}
          </div>
        </div>
      </div>

      <div class="column" style="background-color: green">
      </div>
    </div>
  </div>

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
        this.error = "Login failed. Please check your credentials.";
      }
    }
  }
};
</script>

<style scoped>


.container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.row {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
}

.column {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 0 0 50%;
}


.register{
  text-align: center;
  margin-top: 10px;
  color: black;
}

.register:hover{
  color: green;
}



.login-container {
  padding: 40px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 20px grey;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: Arial, sans-serif;
  width: 100%;
  max-width: 400px;
}

.login-container h2 {
  font-size: 26px;
  margin-bottom: 20px;
  color: grey;
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
  padding: 12px;
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
  padding: 12px;
  background-color: darkgreen;
  border: none;
  border-radius: 5px;
  color: white;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-button:hover {
  background-color: lawngreen;
}

.error-message {
  color: red;
  margin-top: 15px;
  text-align: center;
  font-size: 14px;
}
</style>
