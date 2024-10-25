<template>
  <body>
    <div class="login-container">
      <h2>Login</h2>
      <form @submit.prevent="submitLogin">
        <div class="input-group">
          <label for="username">Username:</label>
          <input
              type="text"
              v-model="username"
              placeholder="Enter username"
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
        <button @click="submitLogin()" type="submit" class="login-button">Login</button>
      </form>

      <div v-if="error" class="error-message">
        {{ error }}
      </div>
    </div>
  </body>
</template>

<script>
export default {
  name: "LoginView",
  data() {
    return {
      username: '',
      password: '',
      error: null,
    };
  },
  methods: {
    submitLogin() {
      if (this.username === "admin" && this.password === "admin") {
        this.error = null;
        console.log("spravne cred")
        this.$router.push("/main");
      }
      else this.error = "Zadaná kombinácia mena a hesla sa nezhoduje";
    },
  },
};
</script>

<style scoped>
body {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
  background-color: ghostwhite;
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
