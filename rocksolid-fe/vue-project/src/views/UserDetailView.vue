<template>
  <header style="padding-left: 11.5rem">
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
    <div class="vertical-divider"></div>
    <div class="spacer"></div>
    <div class="vertical-divider"></div>
    <div class="user-container">
      <div class="user-menu">
        <button class="user-menu-button" @click="toggleDropdown">
          <div class="user-avatar"></div>
          <div class="user-info">
            <div class="user-details">
              <span class="name">{{ fullName }}</span>
              <span class="email">{{ admin_email }}</span>
            </div>
          </div>
        </button>
        <div class="dropdown-content" v-if="isDropdownOpen">
          <div class="wrapper">
            <router-link to="/user" class="user-detail" active-class="active" exact>
              <span style="font-size: 12px">Profil</span>
              <v-icon size="small">mdi-account</v-icon>
            </router-link>
          </div>
          <hr class="devider">
          <div class="wrapper">
            <router-link to="/login" class="logout" @click.native="logout">
              <span style="font-size: 12px">Odhlásiť sa</span>
              <v-icon size="small">mdi-exit-to-app</v-icon>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </header>
  <div class="v-col-7" style="padding-left: 12.5rem">
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
      isDropdownOpen: false,
      fullName: "",
      admin_email: ""
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
    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen;
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
    async fetchCurrentUser() {
      try {
        const userResponse = await axios.get('http://localhost:8080/api/v1/user/me', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
          },
        });
        this.fullName = `${userResponse.data.first_name} ${userResponse.data.last_name}`;
        this.admin_email = userResponse.data.email;
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
    this.fetchCurrentUser();
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

.spacer {
  flex-grow: 1;
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

.university-logo {
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  padding-left: 5rem;
  margin-left: auto;
}

.user-menu-button {
  align-items: center;
  border-radius: .25rem;
  display: flex;
  padding: .25rem;
  width: 16rem;
  border-color: transparent;
  background-color: white;
  box-sizing: border-box;

  &:hover {
    background-color: rgba(228, 226, 226, 0.918);
  }
}

.dropdown-content {
  position: absolute;
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 1;
  padding: .25rem 0;
  gap: .25rem;
  border-radius: .25rem;
  width: 16rem;
}

.wrapper {
  padding: 0 .25rem;
}

.user-detail{
  border-radius: .25rem;
  align-items: center;
  color: #333638;
  display: flex;
  justify-content: space-between;
  padding: .25rem;
  text-decoration: none;

  &:hover {
    background-color: rgba(228, 226, 226, 0.918);
  }
}

.logout {
  border-radius: .25rem;
  align-items: center;
  color: #333638;
  display: flex;
  justify-content: space-between;
  padding: .25rem;
  text-decoration: none;

  &:hover {
    color: #f44336;
    background-color: rgba(244, 67, 54, 0.1);
  }
}

.user-menu {
  margin-right: 2rem;
  position: relative;
}

.devider {
  margin: 0.25rem 0;
  border-top: 1px solid #d8d8f0;
  width: 100%;
}

.user-info {
  display: flex;
  flex: 1;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  max-width: 12rem;
  text-align: start;
}

.user-details {
  display: flex;
  flex-direction: column;
}


.email {
  font-size: 10px;
}

.name {
  font-size: 12px;
  font-weight: bold;
}

</style>