<template>
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
          <v-switch label="Recenzent" color="primary"></v-switch>
          <label>Rola:</label>
          <v-combobox
              label="Recenzent"
              :items="['Recenzent', 'Študent', 'Admin']"
          ></v-combobox>
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

export default {
  name: "UserDetailView",
  props: ['userID'],
  data(){
    return {
      user_data: [],
      first_name: "",
      last_name: "",
      email: ""
    }
  },
  methods: {
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
      } catch (error) {
        console.error("Failed to fetch user:", error);
      }
    },
    async updateUser(){
      try {
        const token = localStorage.getItem("token");
        const response = await axios.patch("http://localhost:8080/api/v1/user/"+this.userID,
            {
              first_name: this.first_name,
              last_name: this.last_name,
              email: this.email
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

label {
  margin-bottom: 4px;
}

</style>