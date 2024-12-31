<template>

  <table class="conferences-table" v-if="activeConferences.length > 0">
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
  <tr v-for= "conference in activeConferences" :key="conference.id" class="table-rows">
    <td class="cell" style="width: 5.452637%;">{{ conference.id }}</td>
    <td class="cell" style="width: 5.452637%;">{{ conference.name }}</td>
    <td class="cell" style="width: 5.452637%;">{{this.formatDate(conference.datefrom) }}</td>
    <td class="cell" style="width: 5.452637%;">{{ this.formatDate(conference.dateuntil) }}</td>
    <td class="cell" style="width: 5.452637%;">
      <v-btn @click="viewConference(conference.id)">Náhľad</v-btn>
    </td>
  </tr>
  </tbody>
  </table>
  <p v-else class="empty-message">Žiadne aktívne konferencie neboli nájdené.</p>
</template>
<script>
import axios from 'axios';
export default {
  name : 'ActiveConferences',
  data() {
    return {
      headers: ["ID", "Názov", "Od", "Do"],
      activeConferences : []
    }
  },
  methods : {
    viewConference(id){
      // presmerovanie na stránku detailu konferencie
      this.$router.push({ name: 'conferenceDetail', params: { id: id }});
    },
    formatDate(date) {
      try {
        const formatter = new Intl.DateTimeFormat('en-GB');
        return formatter.format(new Date(date)).replace(/\//g, '.');
      } catch (error) {
        console.log("Invalid date format for date : " + date + " " + error);
      }
    },

    async getConferences() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/conference/active",
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        this.activeConferences = response.data;
        console.log(this.activeConferences)
      } catch (error) {
        console.error(error);
      }
    },
  },
  mounted() {
    this.getConferences();
  },
}
</script>
