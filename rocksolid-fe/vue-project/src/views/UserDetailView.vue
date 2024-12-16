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
  <v-container>
    <v-card>
      <v-card-title class="card-title">{{ this.user_data.first_name }}</v-card-title>
      <hr>
      <v-card-text>
        <div class="text-fields">
          <label>Meno:</label>
          <v-text-field v-model="first_name"></v-text-field>
          <label>Priezvisko:</label>
          <v-text-field v-model="last_name"></v-text-field>
          <label>Email:</label>
          <v-text-field v-model="email"></v-text-field>
        </div>
        <div>
          <v-switch
              label="Recenzent"
              color="primary"
              v-model="isReviewer">
          </v-switch>
          <label>Rola:</label>
        </div>
      </v-card-text>

      <v-card-actions>
        <v-btn color="primary" @click="updateUser()">Uložiť</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import axios from "axios";
import user from "@/services/Users/user.ts";

export default {
  name: "UserDetailView",
  props: ['userID'],
  data(){
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
        const response = await axios.get("http://localhost:8080/api/v1/user/"+this.userID,
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
    async updateUser(){
      try {
        const token = localStorage.getItem("token");
        const updatedRole = this.isReviewer ? "REVIEWER" : "STUDENT";
        const response = await axios.patch("http://localhost:8080/api/v1/user/"+this.userID,
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
.text-fields {
  display: flex;
  flex-direction: column;
  gap: 16px;
}



.card-title{
  font-size: 30px;
  font-weight: bold;
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
  margin-bottom: 4px;
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