<template>
  <h1>Admin Rozhranie</h1>
  <transition name="fade">
    <v-alert
        v-if="alert_show"
        class="alerts"
        :color="alert_color"
        variant="elevated"
        :icon="alert_icon"
        :text="alert_text"
    ></v-alert>
  </transition>

  <div class="card-class" style="background-color: white">
    <div class="conferences-table" style="border: 1px solid #d8d8f0; border-radius: .5rem">
      <v-card-title class="table-head">
        <div class="title-with-count">
          <h5>Conferences</h5>
          <span class="conferences-count" style="font-size: 13px">Showing {{ recordRange }} of {{ filterconferences.length }} record(s)</span>
        </div>
        <div class="pagination-container">
          <div class="page-number" style="margin-right: 1.5rem">
            <label for="page size" style="font-size: 13px">
              Page {{ currentPage }} / {{ totalPages }}
            </label>
            <button
                class="pagination-button"
                :disabled="currentPage === 1"
                @click="goToPage(currentPage - 1)"
            >
              <span class="mdi mdi-menu-left"></span>
            </button>
            <input
                type="number"
                class="pagination-input"
                v-model.number="currentPageInput"
                @keyup.enter="jumpToPage"
                :min="1"
                :max="totalPages"
                style="width: 5rem; height: 2rem; font-size: .75rem;  font-weight: 500;"
            />
            <button
                class="pagination-button"
                :disabled="currentPage === totalPages"
                @click="goToPage(currentPage + 1)"
            >
              <span class="mdi mdi-menu-right"></span>
            </button>
          </div>
          <label for="page-size" style="margin: 0 10px; font-size: 13px;">
            Page Size
          </label>
          <div class="page-size-selection">
            <div class="page-size-container">
              <select
                  id="page-size"
                  class="page-size-select"
                  v-model.number="rowsPerPage"
                  @change="goToPage(1)"
              >
                <option v-for="size in pageSizes" :key="size" :value="size">{{ size }}</option>
              </select>
              <span class="icon-container">
                <span class="mdi mdi-menu-down"></span>
    </span>
            </div>
          </div>
        </div>
      </v-card-title>
      <div class="table-search">
        <div class="search-container">
          <input
              type="text"
              v-model="search"
              placeholder="Hľadať..."
              class="search-input"
          />
          <span class="mdi mdi-magnify search-icon"></span>
        </div>

        <!-- dialog na vytvorenie konference-->
        <div class="dialog-window">
          <v-dialog
              v-model="dialog"
              max-width="600"
          >
            <template v-slot:activator="{ props: activatorProps }">
              <v-btn
                  class="text-none font-weight-regular"
                  prepend-icon="mdi-account-group"
                  text="Create Conference"
                  variant="tonal"
                  v-bind="activatorProps"
                  @click="clearVariables(); this.edit = false;"
              ></v-btn>
            </template>

            <v-card
                prepend-icon="mdi-account-group"
                :title="edit ? 'Edit Conference' : 'Create Conference'"
            >
              <v-card-text>
                <v-row dense>
                  <v-col
                      cols="12"
                      md="6"
                      sm="6"
                  >
                    <v-text-field
                        label="Conference Name"
                        v-model="name"
                    ></v-text-field>
                  </v-col>

                  <v-col
                      cols="12"
                      md="6"
                      sm="6"
                  >
                    <v-text-field
                        label="Date From"
                        type="date"
                        v-model="dateFrom"
                    ></v-text-field>
                  </v-col>

                  <v-col
                      cols="12"
                      md="6"
                      sm="6"
                  >
                    <v-text-field
                        label="Date To"
                        type="date"
                        v-model="dateTo"
                    ></v-text-field>
                  </v-col>

                  <v-col
                      cols="12"
                      md="6"
                      sm="6"
                  >
                    <v-text-field
                        label="Form ID"
                        type="number"
                        v-model="formID"
                    ></v-text-field>
                  </v-col>

                  <v-col
                      cols="12"
                      md="6"
                      sm="6"
                  >
                    <v-text-field
                        label="Article ID"
                        type="number"
                        v-model="articleID"
                    ></v-text-field>
                  </v-col>

                  <v-col
                      cols="12"
                      md="6"
                      sm="6"
                  >
                    <v-text-field
                        label="Year"
                        type="number"
                        :min="2000"
                        :max="2100"
                        v-model="year"
                    ></v-text-field>
                  </v-col>
                  <v-switch
                      v-if="edit"
                      label="Active"
                      color="primary"
                      v-model="status">
                  </v-switch>
                </v-row>
              </v-card-text>

              <v-divider></v-divider>

              <v-card-actions>
                <v-spacer></v-spacer>

                <v-btn
                    text="Close"
                    variant="plain"
                    @click="dialog = false; clearVariables()"
                ></v-btn>

                <v-btn
                    v-if="!edit"
                    color="primary"
                    text="Save"
                    variant="tonal"
                    @click="dialog = false; createConference()"
                ></v-btn>
                <v-btn
                    v-if="edit"
                    color="primary"
                    text="Edit"
                    variant="tonal"
                    @click="dialog = false; editConference()"
                ></v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </div>



      </div>
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
        <tr v-for="conference in paginatedconferences" :key="conference.id" class="table-rows">
          <td class="cell" style="width: 5.452637%;">{{ conference.id }}</td>
          <td class="cell" style="width: 5.452637%;">{{ conference.name }}</td>
          <td class="cell" style="width: 5.452637%;">{{this.formatDate(conference.date_from) }}</td>
          <td class="cell" style="width: 5.452637%;">{{ this.formatDate(conference.date_to) }}</td>
          <td class="cell" style="width: 5.452637%;">{{ conference.status}}</td>
          <td class="cell" style="width: 5.452637%;"><v-btn @click="updateDialog(conference.id, conference.name, conference.date_from, conference.date_to, conference.status, conference.article_id, conference.form_id, conference.year)">Edit</v-btn></td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ConferenceView",
  data(){
    return {
      headers: ["ID", "Názov", "Od", "Do", "Status", ""],
      conferences: [],
      currentPage: 1,
      rowsPerPage: 10,
      currentPageInput: 1,
      pageSizes: [10, 20, 50],
      search: '',
      dialog: false,
      edit: false,
      //nova konferencia
      name: "",
      dateFrom: "",
      dateTo: "",
      formID: 0,
      year: 0,
      articleID: 0,
      conference_id: 0,
      status: false,
      alert_show: false,
      alert_text: "",
      alert_icon: "",
      alert_color: ""
    }
  },
  methods:{
    goToPage (page) {
      if (page > 0 && page <= this.totalPages) {
        this.currentPage = page;
        this.currentPageInput = page;
      }
    },
    formatDate(date) {
      try {
        const formatter = new Intl.DateTimeFormat('en-GB');
        return formatter.format(new Date(date)).replace(/\//g, '.');
      }
      catch (error) {
        console.log("Invalid date format for date : " + date + " " + error);
      }
    },
    async getConferences(){
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/conference/all",
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        this.conferences = response.data;
        console.log(this.conferences)
      } catch (error) {
        console.error(error);
      }
    },
    async createConference() {
      if(!(this.articleID && this.dateFrom && this.dateTo && this.formID && this.year && this.name)){
        alert("Please, fill all the fields");
        this.dialog = true;
      }
        else {
          try {
            const token = localStorage.getItem("token");
            const response = await axios.post("http://localhost:8080/api/v1/conference/create",
                {
                  article_id: this.articleID,
                  date_from: this.dateFrom,
                  date_to: this.dateTo,
                  form_id: this.formID,
                  year: this.year,
                  name: this.name
                },
                {
                  headers: {
                    Authorization: `Bearer ${token}`
                  }
                });
            this.clearVariables();
            await this.getConferences();
            await this.showAlert("success", "conference");
          } catch (error) {
            await this.showAlert("error", "conference");
            console.error("Failed to create conference:", error);
          }
        }
    },
    async showAlert(status, object){
      if(status === "success"){
        if(object === "conference"){
          this.alert_show = true;
          this.alert_text = "Conference created";
          this.alert_icon = "$success";
          this.alert_color = "success";
          await new Promise(resolve => setTimeout(resolve, 4000));
          this.alert_show = false;
        }
        else if (object === "edit"){
          this.alert_show = true;
          this.alert_text = "Conference edited";
          this.alert_icon = "$success";
          this.alert_color = "success";
          await new Promise(resolve => setTimeout(resolve, 4000));
          this.alert_show = false;
        }
      }
      else if (status === "error"){
        if(object === "conference"){
          this.alert_show = true;
          this.alert_text = "Conference could not be created";
          this.alert_icon = "$error";
          this.alert_color = "error";
          await new Promise(resolve => setTimeout(resolve, 4000));
          this.alert_show = false;
        }
        else if (object === "edit"){
          this.alert_show = true;
          this.alert_text = "Conference could not be edited";
          this.alert_icon = "$error";
          this.alert_color = "error";
          await new Promise(resolve => setTimeout(resolve, 4000));
          this.alert_show = false;
        }
      }
    },
    updateDialog(id, name, date_from, date_to, status, article_id, form_id, year){
      this.dialog = true;
      this.edit = true;
      this.articleID = article_id;
      this.dateFrom = date_from;
      this.dateTo = date_to;
      this.formID = form_id;
      this.year = year;
      this.name = name;
      this.conference_id = id;
      this.status = status;
    },
    clearVariables(){
      this.articleID = 0;
      this.dateFrom = "";
      this.dateTo = "";
      this.formID = 0;
      this.year = 0;
      this.name = "";
      this.edit = false;
    },
    async editConference(){
      if(!(this.articleID && this.dateFrom && this.dateTo && this.formID && this.year && this.name)){
        alert("Please, fill all the fields");
        this.dialog = true;
      }
      else {
        try {
          const token = localStorage.getItem("token");
          const response = await axios.patch(`http://localhost:8080/api/v1/conference/${this.conference_id}`,
              {
                article_id: this.articleID,
                datefrom: this.dateFrom,
                dateuntil: this.dateTo,
                form_id: this.formID,
                year: this.year,
                name: this.name,
                status: this.status
              },
              {
                headers: {
                  Authorization: `Bearer ${token}`
                }
              });
          this.clearVariables();
          await this.showAlert("success", "edit");
          await this.getConferences();
        } catch (error) {
          await this.showAlert("error", "edit");
          console.error("Failed to update conference:", error);
        }
      }
    }
  },
  mounted() {
    this.getConferences();
  },
  computed:{
    filterconferences() {
      return this.conferences.filter((conference) => {
        if (conference.name && conference.name.toLowerCase().includes(this.search.toLowerCase())) {
          return true;
        }
        if (conference.date_from && conference.date_from.toLowerCase().includes(this.search.toLowerCase())) {
          return true;
        }
        if (conference.date_to && conference.date_to.toLowerCase().includes(this.search.toLowerCase())) {
          return true;
        }
        return false;
      });
    }    ,
    paginatedconferences() {
      const start = (this.currentPage - 1) * this.rowsPerPage;
      const end = start + this.rowsPerPage;
      return this.filterconferences.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filterconferences.length / this.rowsPerPage);
    },
    recordRange() {
      const startRecord = (this.currentPage - 1) * this.rowsPerPage + 1;
      const endRecord = Math.min(this.currentPage * this.rowsPerPage, this.filterconferences.length);
      return `${startRecord}-${endRecord}`;
    }
  }
}
</script>


<style scoped>

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.alerts{
  position: fixed;
  top: 20px;
  right: 20px;
  height: fit-content;
  width: fit-content;
  font-size: large;
  z-index: 9999;
}

.dialog-window{
  display: flex;
  margin-left: auto;
}

.conferences-table .cell {
  border-left: 1px solid #d8d8f0;
  border-right: 1px solid #d8d8f0;
  padding: .5em;
  background: white;
}

.conferences-table .cell:first-child {
  border-left: none;
}


.search-container {
  position: relative;
}

.search-input {
  width: 20rem;
  padding: .375rem .625rem;
  border: 1px solid #d8d8f0;
  font-size: .75rem;
  box-sizing: border-box;
  transition: border-color 0.3s ease, border-width 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #a0c4ff;
}

.search-icon {
  font-size: 1rem;
  color: #888;
  pointer-events: none;
}

.table-search {
  display: flex;
  padding: .5em 1.5em;
  border-bottom: 1px solid rgb(216, 216, 240);
}

.page-size-container {
  display: flex;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 0;
  overflow: hidden;
  width: 4rem;
  height: 2rem;
}

.page-size-select {
  border: none;
  flex: 1;
  padding: 0 8px;
  font-size: 0.75rem;
  font-weight: 500;
  outline: none;
}

.icon-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: .25rem;
  font-size: 1.125rem;
}

.pagination-input::-webkit-inner-spin-button,
.pagination-input::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.pagination-container {
  display: flex;
  align-items: center;
}

.pagination-button {
  border: none;
  background: none;
  font-size: 16px;
  cursor: pointer;
}

.pagination-button:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.pagination-input {
  text-align: center;
  border: 1px solid #ccc;
  padding: 4px;
}

.card-class {
  padding: 2.5rem 2.5rem .75rem;
}

.conferences-count::before {
  content: "•";
  padding: 0 .5em;
}

.table-head {
  padding: .5em 1.5em;
  align-items: center;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid rgb(216, 216, 240);
}

.title-with-count {
  display: flex;
  align-items: center;
}

h5 {
  font-size: 1.125rem;
}

.router {
  width: 30px;
  height: 30px;
  display: inline-block;
  margin: 5px;
}

.router img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.buttons {
  width: 30px;
  height: 30px;
  padding: 0;
  border: none;
  background: none;
  cursor: pointer;
  margin: 5px;
}

.buttons img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

table {
  border-collapse: collapse;
  width: 100%;
  table-layout: auto !important;
  word-wrap: break-word;
  border-radius: 0 0 0.5em 0.5em;
  overflow: hidden;
}

td {
  padding: 24px;
  text-align: center;
  border-bottom: 1px solid rgb(224, 242, 237);
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

.table-rows:nth-child(odd) {
  background-color: rgb(250, 250, 250);
}

.table-rows:nth-child(n):hover {
  background-color: rgb(244, 246, 245);
}
</style>