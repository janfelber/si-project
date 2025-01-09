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
          <tr v-for="category in categories" :key="category.id" class="table-rows">
            <td class="cell" style="width: 5.452637%;">{{ category.id }}</td>
            <td class="cell" style="width: 5.452637%;">{{ category.sectionName }}</td>
            <td class="cell" style="width: 5.452637%;">
              <v-btn @click="updateDialog(category.id,category.sectionName)">Editovat</v-btn>
              <v-btn>Vymazat</v-btn>
            </td>
          </tr>
          </tbody>
        </table>
      </v-card-title>
    </div>
  </div>

  <div class="dialog-window">
    <v-dialog
        v-model="dialog"
        max-width="600"
    >
      <template v-slot:activator="{ props: activatorProps }">
        <v-btn
            class="text-none font-weight-regular"
            prepend-icon="mdi-account-group"
            text="Pridanie kategorie"
            variant="tonal"
            v-bind="activatorProps"
            @click="clearVariables(); this.edit = false;"
        ></v-btn>
      </template>

      <v-card
          prepend-icon="mdi-account-group"
          :title="edit ? 'Editovanie kategorie' : 'Pridanie kategorie'"
      >
        <v-card-text>
          <v-row dense>
            <v-col
                cols="12"
                md="6"
                sm="6"
            >
              <v-text-field
                  label="Nazov kategorie"
                  v-model="sectionName"
              ></v-text-field>
            </v-col>
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
              @click="dialog = false; createCategory()"
          ></v-btn>
          <v-btn
              v-if="edit"
              color="primary"
              text="Edit"
              variant="tonal"
              @click="dialog = false; editCategory()"
          ></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>


</template>
<script>
import axios from 'axios';
export default {
  name : 'CategoriesView',
  data() {
    return {
      headers: ["ID", "Názov", "Operacia"],
      categories : [],
      dialog: false,
      edit: false,
      category_id: 0,
      sectionName: ""
    }
  },
  methods : {
    async getCategories() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/category/categories",
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
    async createCategory() {
      if(!this.sectionName){
        alert("Please, fill all the fields");
        this.dialog = true;
      } else {
        try {
          const token = localStorage.getItem("token");
          const response = await axios.post("http://localhost:8080/api/v1/category/create",
              {
                sectionName: this.sectionName
              },
              {
                headers: {
                  Authorization: `Bearer ${token}`
                }
              });
          this.clearVariables();
          await this.getCategories();
        }catch (error) {
          console.log("Failed to create new category", error)
        }
      }
    },
    async editCategory() {
      if(!this.sectionName){
        alert("Please, fill all the fields");
        this.dialog = true;
      } else {
        try {
          const token = localStorage.getItem("token");
          const response = await axios.put(`http://localhost:8080/api/v1/category/update/${this.category_id}`,
              {
                name: this.sectionName
              },
              {
                headers: {
                  Authorization: `Bearer ${token}`
                }
              });
          console.log(this.category_id)
          console.log(response)
          this.clearVariables();
          await this.getCategories();
        }catch (error) {
          console.log("Failed to update the category")
        }
      }
    },
    updateDialog(id, name){
      this.dialog = true;
      this.edit = true;
      this.sectionName = name;
      this.category_id = id;
    },
    clearVariables(){
      this.sectionName = "";
      this.edit = false;
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