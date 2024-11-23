<template>
  <div class="container">
    <div class="row">
      <div class="column">
        <div v-if="!hide_form" class="login-container">
          <h2>Registrácia</h2>
          <form @submit.prevent="registerUser">
            <div class="input-group">
              <label>Meno:</label>
              <input
                  type="text"
                  v-model="name"
                  placeholder="Meno"
                  required
              />
            </div>
            <div class="input-group">
              <label>Priezvisko:</label>
              <input
                  type="text"
                  v-model="surname"
                  placeholder="Priezvisko"
                  required
              />
            </div>
            <div class="input-group">
              <label>E-mail:</label>
              <input
                  type="email"
                  v-model="email"
                  placeholder="E-mail"
                  required
              />
            </div>
            <div class="input-group">
              <label>Heslo:</label>
              <input
                  type="password"
                  v-model="password"
                  placeholder="Heslo"
                  required
              />
            </div>
            <button type="submit" class="login-button">Registrovať</button>
          </form>

          <div v-if="error" class="error-message">
            {{ error }}
          </div>
        </div>
        <div class="register-success" v-if="hide_form">
          <h1>Registrácia bola úspešná</h1>
          <router-link to="/home">Späť na domovskú stránku</router-link>
        </div>
      </div>

      <div class="column" style="background-color: green">
        <img src="../assets/ukflogo.png">
      </div>
    </div>
  </div>

</template>

<script>
import authentication from "@/services/auth/authentication.ts";

export default {
  name: "RegisterView",
  data() {
    return {
      name: '',
      surname: '',
      username: '',
      password: '',
      email: '',
      message: '',
      error: null,
      hide_form: false
    };
  },
  methods: {
    registerUser() {
      const registerRequest = {
        fistName: this.name,
        lastName: this.surname,
        email: this.email,
        password: this.password
      };

      authentication.register(registerRequest)
          .then(() => {
            this.message = 'Účet bol úspešne vytvorený. Budete presmerovaní na prihlasovaciu stránku za 3 sekundy.';
            this.hide_form = true;
            setTimeout(() => {
              this.$router.push('/login');
            }, 3000);
          })
          .catch(() => {
            this.error = 'Registrácia zlyhala. Skúste to prosím znova.';
          });
    }
  }
};
</script>

<style scoped>
.register-success {
  display: table-cell;
  justify-content: center;
  text-align: center;
  align-items: center;
}

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

.login-button:disabled {
  background-color: #222222;
  cursor: default;
}

.error-message {
  color: red;
  margin-top: 15px;
  text-align: center;
  font-size: 14px;
}
</style>