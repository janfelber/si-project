<template>
  <div class="card-class" style="background-color: white">
    <div class="categories-table" style="border: 1px solid #d8d8f0; border-radius: .5rem">
      <v-card-title class="table-head">
        <div class="title-with-count">
          <h5>Kategórie</h5>
          <span class="categories-count" style="font-size: 13px">Showing {{ recordRange }} of {{ filterCategories.length }} record(s)</span>
        </div><div class="pagination-container">
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

        <div class="dialog-window">
          <v-dialog
              v-model="dialog"
              max-width="600"
          >
            <template v-slot:activator="{ props: activatorProps }">
              <v-btn
                  class="text-none font-weight-regular"
                  prepend-icon="mdi-view-list"
                  text="Pridanie kategorie"
                  variant="tonal"
                  v-bind="activatorProps"
                  @click="clearVariables(); this.edit = false;"
              ></v-btn>
            </template>

            <v-card
                prepend-icon="mdi-view-list"
                :title="edit ? 'Editovanie kategorie' : 'Pridanie kategorie'"
            >
              <v-card-text class="form-group">

                <div class="form-input">
                  <input
                      v-model="sectionName"
                      placeholder="Názov kategórie"
                  >
                </div>



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
      </div>
        <table class="categories-table">
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
            <td class="cell" style="width: 5.452637%;">{{ category.sectionName }}</td>
            <td class="cell" style="width: 5.452637%;">
              <v-btn @click="updateDialog(category.id,category.sectionName)" class="mr-2" prepend-icon="mdi-pen">Editovať</v-btn>
            </td>
          </tr>
          </tbody>
        </table>
    </div>
  </div>

</template>
<script>
import axios from 'axios';
export default {
  name : 'CategoriesView',
  data() {
    return {
      headers: ["Názov", "Edit"],
      categories : [],
      dialog: false,
      edit: false,
      category_id: 0,
      sectionName: "",
      currentPage: 1,
      rowsPerPage: 10,
      currentPageInput: 1,
      pageSizes: [10, 20, 50],
      search: ''
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
    goToPage(page) {
      if (page > 0 && page <= this.totalPages) {
        this.currentPage = page;
        this.currentPageInput = page;
      }
    },
    jumpToPage() {
      if (this.currentPageInput > 0 && this.currentPageInput <= this.totalPages) {
        this.goToPage(this.currentPageInput);
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
  computed: {
    filterCategories() {
      return this.categories.filter((category) => {
        if (category.sectionName && category.sectionName.toLowerCase().includes(this.search.toLowerCase())) {
          return true;
        }
        if (String(category.id).includes(this.search)) {
          return true;
        }
        return false;
      });
    },
    totalPages() {
      return Math.ceil(this.filterCategories.length / this.rowsPerPage);
    },
    recordRange() {
      const startRecord = (this.currentPage - 1) * this.rowsPerPage + 1;
      const endRecord = Math.min(this.currentPage * this.rowsPerPage, this.filterCategories.length);
      return `${startRecord}-${endRecord}`;
    }
  }
}
</script>
<style scoped>


.form-group {
  display:       flex;
  align-items:   center;
  margin-bottom: .75rem;
  align-content: center;
}

.form-input input {
  border:     1px solid #d8d8f0;
  padding:    8px;
  width:      100%;
  box-sizing: border-box;
}

.form-input {
  flex: 1;
}

.mr-2 {
  margin-right: 8px; /* Alebo hodnotu podľa potreby */
}

td {
  padding: 24px;
  text-align: center;
  border-bottom: 1px solid rgb(224, 242, 237);
}

.dialog-window{
  display: flex;
  margin-left: auto;
}

.card-class {
  padding: 2.5rem 2.5rem .75rem;
}

.search-icon {
  font-size: 1rem;
  color: #888;
  pointer-events: none;
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

.table-search {
  display: flex;
  padding: .5em 1.5em;
  border-bottom: 1px solid rgb(216, 216, 240);
}

.search-container {
  position: relative;
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

.icon-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: .25rem;
  font-size: 1.125rem;
}

.page-size-select {
  border: none;
  flex: 1;
  padding: 0 8px;
  font-size: 0.75rem;
  font-weight: 500;
  outline: none;
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

.categories-table .cell {
  border-left: 1px solid #d8d8f0;
  border-right: 1px solid #d8d8f0;
  padding: .5em;
  background: white;
}

.categories-table .cell:first-child {
  border-left: none;
}

.table-head {
  padding: .5em 1.5em;
  align-items: center;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid rgb(216, 216, 240);
}

.categories-count::before {
  content: "•";
  padding: 0 .5em;
}

.categories-table .header-item {
  padding: 30px 20px;
  font-size: 12px;
  text-transform: uppercase;
  border-left: 1px solid #d8d8f0;
  border-right: 1px solid #d8d8f0;
  background: #f7f7fc;
  border-bottom: 1px solid #d8d8f0;
}

.categories-table .header-item:first-child {
  border-left: none;
}

.categories-table .header-item:last-child {
  border-right: none;
}
.pagination-container {
  display: flex;
  align-items: center;
}
.title-with-count {
  display: flex;
  align-items: center;
}

.pagination-input {
  text-align: center;
  border: 1px solid #ccc;
  padding: 4px;
}

.pagination-input::-webkit-inner-spin-button,
.pagination-input::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

</style>