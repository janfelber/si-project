<template>
  <div>
    <h1>Detail konferencie</h1>
    <h1>{{this.conference_data.name}}</h1>
    <h1>{{this.conference_data.status}}</h1>

    <v-btn
        class="text-none font-weight-regular"
        prepend-icon="mdi-account-group"
        text="Pirhlasenie"
        variant="tonal"
        @click="addUserToConference()"
    ></v-btn>
  </div>
</template>

<script>
import axios from 'axios';
import { th } from 'vuetify/locale';

export default {
  name: 'ConferenceDetail',
  props: ['id'],
  data() {
    return {
      conference_data: [],
      name: "",
      active: ""
    }

  },
  methods: {
    async getConferenceDetails() {
      try {
        const token = localStorage.getItem("token")
        const response = await axios.get("http://localhost:8080/api/v1/conference/" + this.id, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        this.conference_data = response.data;
        this.name = this.conference_data.name;
        this.active = this.conference_data.status
      } catch (error) {
        console.error("Failed to fetch conference details:", error);
      }
    },
    async addUserToConference() {
      try {
        const token = localStorage.getItem("token")
        const conferenceId = this.id
        const response = await axios.post(`http://localhost:8080/api/v1/conference/addUserToConference?conferenceId=${conferenceId}`,null, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
      } catch (error) {
        console.error("Failed to fetch conference details:", error);
      }
    },
  },
  mounted() {
    this.getConferenceDetails();
  },
};
</script>