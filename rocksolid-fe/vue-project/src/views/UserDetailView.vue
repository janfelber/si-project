<template>
  <header>
    <nav class="header-nav">
      <div class="active-router">
        <span class="router-label">Users</span>
      </div>
      <div class="user-item">
        <span> {{ this.user_data.first_name }} </span>
      </div>
    </nav>
    <div class="vertical-divider"></div>
    <v-icon @click="deleteUser(this.userID)">
      mdi-trash-can-outline
    </v-icon>
  </header>
  <div class="v-col-7">
    <div class="card">
      <v-card-title class="card-title"> Basic info</v-card-title>
      <div class="card-body">
        <div class="form-group">
          <label>Meno</label>
          <div class="form-input">
            <input v-model="first_name">
          </div>
        </div>
        <div class="form-group">
          <label>Priezvisko</label>
          <div class="form-input">
            <input v-model="last_name">
          </div>
        </div>
        <hr>
        <div class="form-group">
          <label>Email</label>
          <div class="form-input">
            <input v-model="email">
          </div>
        </div>
        <div class="form-group">
          <label>Recenzent</label>
          <div class="form-check form-switch">
            <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault" v-model="isReviewer">
          </div>
          <div class="form-text">
            Upozornenie: Aktivácia používateľa ako recenzenta mu umožní prístup k hodnoteniu a správe recenzií.
            Zmena roly neovplyvní jeho existujúce dáta alebo záznamy.
          </div>
        </div>
      </div>
    </div>
    <div class="button-section">
      <button type="button" class="btn btn-primary" @click="updateUser()" style="color: white">Uložiť</button>
    </div>
  </div>

  <div>

  </div>


</template>

<script>
import axios from "axios";
import user from "@/services/Users/user.ts";

export default {
  name: "UserDetailView",
  props: ['userID'],
  data() {
    return {
      user_data: [],
      first_name: "",
      last_name: "",
      email: "",
      isReviewer: false,
    }
  },
  methods: {
    deleteUser(id) {
      user.deleteUser(id)
          .then(() => {
            console.log("User deleted");
            window.location.href = '/admin/users';
          })
          .catch((error) => {
            this.error = 'Používateľa sa nepodarilo odstrániť';
            console.log(error)
          });
    },
    async getUserData() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/user/" + this.userID,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        this.user_data = response.data;
        this.first_name = this.user_data.first_name;
        this.last_name = this.user_data.last_name;
        this.email = this.user_data.email;
        this.isReviewer = this.user_data.role === "REVIEWER";
      } catch (error) {
        console.error("Failed to fetch user:", error);
      }
    },
    async updateUser() {
      try {
        const token = localStorage.getItem("token");
        const updatedRole = this.isReviewer ? "REVIEWER" : "STUDENT";
        const response = await axios.patch("http://localhost:8080/api/v1/user/" + this.userID,
            {
              first_name: this.first_name,
              last_name: this.last_name,
              email: this.email,
              role: updatedRole,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        console.log(response)
        await this.getUserData();
      } catch (error) {
        console.error("Failed to update user:", error);
      }
    }
  },
  mounted() {
    this.getUserData();
  }
}
</script>

<style scoped>

.button-section {
  display: flex;
  margin-top: 1rem;
}

.form-input input {
  border: 1px solid #d8d8f0;
  padding: 8px;
  width: 100%;
  box-sizing: border-box;
}

.form-group .form-text {
  margin-left: 10rem;
  font-size: 14px;
  color: #9aa0a3;
}

.form-text {
  align-items: flex-start;
  color: #9aa0a3;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  font-size: 14px;
}


.form-input {
  flex: 1;
}

hr {
  border: 0;
  border-top: 1px solid darkgrey;
  margin-bottom: .75rem;
  margin-top: .75rem;
}

.card {
  display: flex;
  flex-direction: column;
  min-width: 0;
  position: relative;
  word-wrap: break-word;
  background-clip: initial;
  background-color: #fefefe;
  border: 1px solid #d8d8f0;
  border-radius: .5rem;
}

.card-body {
  min-height: 1px;
  padding: .75rem;
}

.form-group {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: .75rem;
}

.card-title {
  font-weight: 500;
  font-size: 17px;
  border-bottom: 1px solid #d8d8f0;
}

.user-item ::before {
  color: inherit;
  content: "›";
  float: left;
  padding-right: .375rem;

}

.user-item {
  padding-left: .375rem;
}

label {
  width: 10rem;
  align-self: center;
}

.header-nav {
  margin-left: 1.75rem;
  display: flex;
}

.vertical-divider {
  width: 1px;
  height: 35px;
  background-color: #d8d8f0;
  margin: 0 1rem;
}

header {
  background-color: white;
  display: flex;
  height: 60px;
  align-items: center;
  border-bottom: 1px solid #d8d8f0;
}

</style>