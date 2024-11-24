<template>
  <h1>Admin Rozhranie</h1>
  <input type="number" v-model:="id_user">

  <table>
    <thead>
    <tr>
      <th
          v-for="(header, i) in headers"
          :key="`header-${i}`"
          class="header-item"
      >
        {{ header }}
      </th>
    </tr>
    </thead>
    <tbody>
    <tr
        v-for="user in users"
        :key="user.id"
        class="table-rows"
    >
      <td>{{ user.id }}</td>
      <td>{{ user.first_name }}</td>
      <td>{{ user.last_name }}</td>
      <td>{{ user.email }}</td>
      <td><button @click="deleteUser(user.id)">Vymazať</button></td>
    </tr>
    </tbody>
  </table>


  <button @click="deleteUser()">
    Delete User
  </button>
</template>


<script>
import user from "@/services/Users/user.ts";
import axios from "axios";

export default {
  name: "AdminView",
  data(){
    return {
      id_user: null,
      headers: ["ID","Meno", "Priezvisko", "E-mail", ""],
      users: []
    }
  },
  methods:{
    deleteUser() {
      user.deleteUser(this.id_user)
          .then(() => {
            setTimeout(() => {
              console.log("Vymazane")
            }, 3000);
          })
          .catch((error) => {
            this.error = 'Používateľa sa nepodarilo odstrániť';
            console.log(error)
          });
    },
    async getUsers() {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/user/all");
        this.users = response.data; // Update users array with fetched data
      } catch (error) {
        console.error("Failed to fetch users:", error);
      }
    },
  },
  mounted() {
    this.getUsers()
  }


}
</script>


<style scoped>

table {
  border-collapse: collapse;
  width: 100%;
  table-layout: auto !important;
  word-wrap: break-word;
}

td {
  padding: 24px;
  text-align: center;
  border-bottom: 1px solid rgb(224, 242, 237);
}

.header-item {
  padding: 30px 20px;
  font-size: 12px;
  background-color: rgb(224, 242, 237);
  text-transform: uppercase;
}

.table-rows:nth-child(odd) {
  background-color: rgb(250, 250, 250);
}

.table-rows:nth-child(n):hover {
  background-color: rgb(244, 246, 245);
}
</style>