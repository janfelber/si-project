<template>
  <div class="card-class" style="background-color: white">
    <div class="articles-table" style="border: 1px solid #d8d8f0; border-radius: .5rem">
      <v-card-title class="table-head">
        <div class="title-with-count">
          <h5>Clanky</h5>
          <span class="articles-count" style="font-size: 13px">Showing {{ recordRange }} of {{ filterArticles.length }} record(s)</span>
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
      </div>
      <table class="articles-table">
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
        <tr v-for="article in paginatedArticles" :key="article.id" class="table-rows">
          <td class="cell" style="width: 15.924607%;">
            <RouterLink :to="{ name: 'AdminArticleDetailView', params: { id: article.id } }" class="article-link">
              {{ article.articleName }}
            </RouterLink>
          </td>
          <td class="cell" style="width: 15.924607%;" >{{ article.conferenceName }}</td>
          <td class="cell" style="width: 15.924607%;" >{{ article.section }}</td>
          <td class="cell" style="width: 15.924607%;" >{{ article.firstName }} {{ article.lastName }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>


<script>
import axios from "axios";

export default {
  name: "AdminView",
  data(){
    return {
      id_article: null,
      headers: ["Nazov clanku", "Konferencia", "Sekcia", "Uzivatel"],
      articles: [],
      currentPage: 1,
      rowsPerPage: 10,
      currentPageInput: 1,
      pageSizes: [10, 20, 50],
      search: ''
    }
  },
  methods:{
    async getArticles() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/article/all",
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        this.articles = response.data;
        console.log(this.articles)
      } catch (error) {
        console.error("Failed to fetch articles:", error);
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
    }
  },
  mounted() {
    this.getArticles()
  },
  computed: {
    filterArticles() {
      return this.articles.filter((article) => {
        if (article.first_name && article.first_name.toLowerCase().includes(this.search.toLowerCase())) {
          return true;
        }
        if (article.last_name && article.last_name.toLowerCase().includes(this.search.toLowerCase())) {
          return true;
        }
        if (article.email && article.email.toLowerCase().includes(this.search.toLowerCase())) {
          return true;
        }
        if (String(article.id).includes(this.search)) {
          return true;
        }
        return false;
      });
    },
    paginatedArticles() {
      const start = (this.currentPage - 1) * this.rowsPerPage;
      const end = start + this.rowsPerPage;
      return this.filterArticles.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filterArticles.length / this.rowsPerPage);
    },
    recordRange() {
      const startRecord = (this.currentPage - 1) * this.rowsPerPage + 1;
      const endRecord = Math.min(this.currentPage * this.rowsPerPage, this.filterArticles.length);
      return `${startRecord}-${endRecord}`;
    }
  }


}
</script>


<style scoped>

.article-link {
  background-color: #0000;
  color: #6895fa;
  text-decoration: none;
}

.articles-table .cell {
  border-left: 1px solid #d8d8f0;
  border-right: 1px solid #d8d8f0;
  padding: .5em;
  background: white;
}

.articles-table .cell:first-child {
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

.articles-count::before {
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

.articles-table .header-item {
  padding: 30px 20px;
  font-size: 12px;
  text-transform: uppercase;
  border-left: 1px solid #d8d8f0;
  border-right: 1px solid #d8d8f0;
  background: #f7f7fc;
  border-bottom: 1px solid #d8d8f0;
}

.articles-table .header-item:first-child {
  border-left: none;
}

.articles-table .header-item:last-child {
  border-right: none;
}

.table-rows:nth-child(odd) {
  background-color: rgb(250, 250, 250);
}

.table-rows:nth-child(n):hover {
  background-color: rgb(244, 246, 245);
}
</style>