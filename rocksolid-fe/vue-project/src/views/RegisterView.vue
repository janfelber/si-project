<template>
  <main>
  <form class="register-form">
    <div>
      <div>
          <h3 class="text-center">Registrácia</h3>
          <div v-if="error && !success_register" class="error-message-container">
            <span class="mdi mdi-close-circle-outline alert-icon"></span>
            <span>
                {{ error }}
              </span>
          </div>
          <div v-if="success_register" class="success-message-container">
            <span class="mdi mdi-check-circle success-icon"></span>
            <span>
                Registrácia bola úspešná. Späť na prihlásenie <router-link to="/login" style="text-decoration: none">login</router-link>
              </span>
          </div>
          <div class="divider">
            <hr>
          </div>
          <form @submit.prevent="registerUser">
            <div class="input-group">
              <input
                  type="text"
                  v-model="name"
                  placeholder="Meno"
                  required
              />
            </div>
            <div class="input-group">
              <input
                  type="text"
                  v-model="surname"
                  placeholder="Priezvisko"
                  required
              />
            </div>
            <div class="input-group">
            <div class="form-input">
                <select v-model="university" required>
                  <option value="" disabled selected>Univerzita</option>
                  <option v-for="item in items" :key="item" :value="item">
                    {{ item }}
                  </option>
                </select>
            </div>
            </div>
            <div class="input-group">
              <input
                  type="email"
                  v-model="email"
                  placeholder="E-mail"
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
              <span style="font-size: 0.875rem" class="text-center">Registrovať</span>

            </button>
          </form>
        </div>
      </div>

      <div class="column" style="background-color: green">
      </div>
  </form>
  </main>
</template>

<script>
import authentication from "@/services/auth/authentication.js";

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
      university: '',
      items: ["UKF", "UCM", "UMB"],
      error: null,
      success_register: false
    };
  },
  methods: {
    isPasswordValid(password) {
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
      return passwordRegex.test(password);
    },
    registerUser() {
      const registerRequest = {
        firstName: this.name,
        lastName: this.surname,
        email: this.email,
        password: this.password,
        university: this.university
      };

      if (!this.isPasswordValid(this.password)) {
        this.error = 'Heslo musí mať aspoň 8 znakov, obsahovať veľké písmeno, malé písmeno, číslo a špeciálny znak.';
        return;
      }

      authentication.register(registerRequest)
          .then(() => {
            this.success_register = true;
            setTimeout(() => {
              this.$router.push('/login');
            }, 5000);
          })
          .catch((err) => {
            if (err.response) {
              if (err.response.status === 409) {
                this.error = 'Tento e-mail už je zaregistrovaný.';
              }
            } else {
              this.error = 'Registrácia zlyhala. Skúste to prosím znova.';
            }
          });
    }
  }
};
</script>

<style scoped>

main {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.register-form {
  margin: auto;
  max-width: 20rem;
  padding: 2rem 0;
  width: 100%;
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
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
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
}

.alert-icon {
  color: red;
  font-size: 1.25rem;
  margin-top: 0.25rem;
}

.error-message-container span {
  display: block;
}

.success-icon {
  color: green;
  font-size: 1.25rem;
  margin-top: 0.25rem;
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
}

.success-message-container span {
  display: block;
}

.form-input select {
  border: 1px solid grey;
  padding:    8px;
  width:      100%;
  box-sizing: border-box;
  appearance: none;
  background: url("data:image/svg+xml,%3Csvg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 16 16\"%3E%3Cpath d=\"M4.293 5.293a1 1 0 0 1 1.414 0L8 7.586l2.293-2.293a1 1 0 0 1 1.414 1.414L8 10.414l-3.707-3.707a1 1 0 0 1 0-1.414z\"%3E%3C/path%3E%3C/svg%3E") no-repeat right 0.75rem center;
  background-size: 8px 8px;
  cursor: pointer;
}

.form-input {
  flex: 1;
}
</style>