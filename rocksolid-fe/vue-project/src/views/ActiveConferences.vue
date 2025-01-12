<template>
  <v-app>
    <v-main>
      <div class="filter-header">
        <h4 style="font-size: 20px">Filter</h4>
      </div>
      <div class="filter-container">
        <div class="form-group">
          <label>Vyhladavacia fraza</label>
          <div class="form-input">
            <input v-model="searchQuery" placeholder="Zadajte vyhľadávací výraz" />
          </div>
        </div>
        <div class="form-group filter-button">
          <button class="btn btn-primary filter" @click="filter" style="color: white">Filter</button>
          <button class="btn btn-primary reset" @click="reset" style="color: #1EB386">Resetovat</button>
        </div>
      </div>

      <div class="conferences" style="padding: 0 1.5rem">


      <v-row class="pa-4" align="stretch" justify="start">
        <v-col
            v-for="conference in filteredConferences"
            :key="conference.id"
            cols="12"
            sm="6"
            md="4"
            lg="3"
        >
          <v-card class="elevation-3">
            <v-card-title class="text-h6 font-weight-bold">{{ conference.name }}</v-card-title>
            <v-card-subtitle class="text-body-2 grey--text">
              <v-icon left>mdi-calendar</v-icon>
              {{ formatDate(conference.datefrom) }} - {{ formatDate(conference.dateuntil) }}
            </v-card-subtitle>
            <v-divider></v-divider>
            <v-card-text class="py-3">
              <div class="text-truncate">
                {{ conference.description || 'No description available' }}
              </div>
            </v-card-text>
            <v-card-actions>
              <v-row class="w-100" justify="space-between">
                <v-col class="d-flex justify-start" style="padding-left: 1rem">
                  <button class="btn btn-primary conference-details" v-if="!userConferenceStatuses[conference.id]" @click="addUserToConference(conference.id)" style="color: white">Prihlasit</button>
                  <button class="btn btn-primary conference-details" v-if="userConferenceStatuses[conference.id]" @click="uploadArticle(conference.id)" style="color: white">Odovzdat</button>
                </v-col>
              </v-row>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
      </div>
    </v-main>
  </v-app>
</template>

<script>
import axios from 'axios';
export default {
  name: 'ActiveConferences',
  data() {
    return {
      activeConferences: [],
      searchQuery: '',
      filteredConferences: [],
      userConferenceStatuses: {}
    };
  },
  methods: {
    formatDate(date) {
      try {
        const formatter = new Intl.DateTimeFormat('en-GB');
        return formatter.format(new Date(date)).replace(/\//g, '.');
      } catch (error) {
        console.log('Invalid date format for date : ' + date + ' ' + error);
      }
    },
    uploadArticle(conferenceId) {
      this.$router.push({ name: 'upload', params: { id: conferenceId }});
    },
    async getConferences() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:8080/api/v1/conference/active', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.activeConferences = response.data;
        this.filteredConferences = this.activeConferences;
        console.log(this.activeConferences);

        await this.getUserStatusesForAllConferences();
      } catch (error) {
        console.error(error);
      }
    },
    async checkIfUserInConference(conferenceId) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(
            `http://localhost:8080/api/v1/conference/isUserInConference?conferenceId=${conferenceId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );
        return response.data === 'User is in the conference';
      } catch (error) {
        console.error(`Error checking user in conference ${conferenceId}:`, error);
        return false;
      }
    },
    async getUserStatusesForAllConferences() {
      const statusPromises = this.activeConferences.map(async (conference) => {
        const isInConference = await this.checkIfUserInConference(conference.id);
        return { id: conference.id, isInConference };
      });

      const statuses = await Promise.all(statusPromises);

      statuses.forEach(({ id, isInConference }) => {
        this.userConferenceStatuses[id] = isInConference;
      });
    },
    async addUserToConference(conferenceId) {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.post(
            `http://localhost:8080/api/v1/conference/addUserToConference?conferenceId=${conferenceId}`,
            null,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
        );

        console.log(`User added to conference ${conferenceId}:`, response.data);

        this.userConferenceStatuses = {
          ...this.userConferenceStatuses,
          [conferenceId]: true,
        };
      } catch (error) {
        console.error(`Failed to add user to conference ${conferenceId}:`, error);
      }
    },
    filter() {
      this.filteredConferences = this.activeConferences.filter((conference) => {
        return conference.name.toLowerCase().includes(this.searchQuery.toLowerCase());
      });
    },
    reset() {
      this.searchQuery = '';
      this.filteredConferences = this.activeConferences;
    }
  },
  mounted() {
    this.getConferences();
  },
};
</script>

<style scoped>
.filter-header {
  align-items: center;
  border-bottom: 1px solid #d8d8f0;
  background-color: #f7f7fc;
  display: flex;
  padding: .75rem 2.5rem;
}

.filter-container {
  border-bottom: 1px solid #d8d8f0;
  padding: .75rem 2.5rem;
}

.form-group {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: .75rem;
}

.form-input input {
  border: 1px solid #d8d8f0;
  padding: 8px;
  width: 40%;
  box-sizing: border-box;
  font-size: 0.75rem;
}

label {
  width: 11rem;
  align-self: center;
  font-size: 14px;
}

.form-input {
  flex: 1;
}

.filter-button {
  display: flex;
  gap: 10px;
  justify-content: flex-start;
  margin-top: 4rem;
}

.filter-button .filter {
  background-color: #1EB386;
  border-color: #1EB386;
}

.filter-button .reset {
  background-color: transparent;
  border-color: #1EB386;
}

.filter-button .filter,
.filter-button .reset {
  font-weight: bold;
  font-size: 14px;
  padding: 6px 12px;
}

.form-input input:focus {
  outline: none;
}

.v-card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.v-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
}

.v-card-title {
  font-size: 1.6rem;
  font-weight: bold;
}

.v-btn {
  border-radius: 50px;
  padding: 0.8rem 1.5rem;
}

.v-btn:hover {
  transform: scale(1.05);
}

.conference-details {
  background-color: #1EB386;
  border-color: #1EB386;
}
</style>