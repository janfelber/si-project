<template>
  <div>
    <h1>Detail konferencie</h1>
    <h1>{{this.conference_data.name}}</h1>
    <h1>{{this.conference_data.status}}</h1>

    <v-btn v-if="userInConference === false"
        class="text-none font-weight-regular"
        prepend-icon="mdi-account-group"
        text="Pirhlasenie"
        variant="tonal"
        @click="addUserToConference()"
    ></v-btn>
    <v-btn v-if="userInConference === true"
           class="text-none font-weight-regular"
           prepend-icon="mdi-upload"
           text="Odovdzať"
           variant="tonal"
           @click="uploadArticle()"
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
      active: "",
      userInConference: null,
    }

  },
  methods: {
    uploadArticle() {
      this.$router.push({ name: 'upload', params: { id: this.id }});
    },
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
    async checkIfUserInConference() {
      try {
        const token = localStorage.getItem("token");
        const conferenceId = this.id;

        const response = await axios.get(
            `http://localhost:8080/api/v1/conference/isUserInConference?conferenceId=${conferenceId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        if (response.data === "User is in the conference") {
          this.userInConference = true;
          console.log("User is already in the conference");
        } else {
          this.userInConference = false;
          console.log("User is not in the conference");
        }
      } catch (error) {
        console.error("Error checking user in conference:", error);
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
        this.userInConference = true;
      } catch (error) {
        console.error("Failed to fetch conference details:", error);
      }
    },
  },
  mounted() {
    this.checkIfUserInConference();
    this.getConferenceDetails();
  },
};
</script>