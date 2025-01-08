<template>
  <div class="card-class" style="background-color: white">
    <div class="conferences-table" style="border: 1px solid #d8d8f0; border-radius: .5rem">
      <v-card-title class="table-head">
  <table class="conferences-table">
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
    <tr v-for="conference in categories" :key="conference.id" class="table-rows">
      <td class="cell" style="width: 5.452637%;">{{ conference.id }}</td>
      <td class="cell" style="width: 5.452637%;">{{ conference.sectionName }}</td>
      <td class="cell" style="width: 5.452637%;"><v-btn>Edit</v-btn></td>
    </tr>
    </tbody>
  </table>
</v-card-title>
</div>
</div>
</template>
<script>
import axios from 'axios';
export default {
  name : 'CategoriesView',
  data() {
    return {
      headers: ["ID", "Názov", "Operacia"],
      categories : []
    }
  },
  methods : {
    async getCategories() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/article/sections",
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        this.categories = response.data;
        console.log(this.categories)
      } catch (error) {
        console.error(error);
      }
    },
  },
  mounted() {
    this.getCategories();
  },
}
</script>
<style>
.conferences-table .cell {
  border-left: 1px solid #d8d8f0;
  border-right: 1px solid #d8d8f0;
  padding: .5em;
  background: white;
}

.conferences-table .cell:first-child {
  border-left: none;
}

.conferences-table .header-item {
  padding: 30px 20px;
  font-size: 12px;
  text-transform: uppercase;
  border-left: 1px solid #d8d8f0;
  border-right: 1px solid #d8d8f0;
  background: #f7f7fc;
  border-bottom: 1px solid #d8d8f0;
}

.conferences-table .header-item:first-child {
  border-left: none;
}

.conferences-table .header-item:last-child {
  border-right: none;
}
</style>