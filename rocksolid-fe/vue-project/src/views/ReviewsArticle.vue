<template>
  <v-app>
    <v-main>
      <div class="filter-header">
        <h4 style="font-size: 20px"> Filter </h4>
      </div>
      <div class="filter-container">
        <div class="form-group">
          <label>Vyhladavacia fraza</label>
          <div class="form-input">
            <input v-model="searchQuery" placeholder="Zadajte vyhľadávací výraz">
          </div>
        </div>
        <div class="form-group filter-button">
          <button class="btn btn-primary filter" @click="filter" style="color: white">Filter</button>
          <button class="btn btn-primary reset" @click="reset" style="color: #1EB386">Resetovať</button>
        </div>
      </div>

      <div v-if="emptyArticleList">
        <div style="display: flex; justify-content: center; align-items: center; height: 30vh; flex-direction: row; text-align: center; color: black">
          <span style="margin-right: 5px; font-size: 30px">Je nám ľúto, momentálne nemáte žiadne pridelené práce!</span>
        </div>
      </div>



      <div class="articles" style="padding: 0 1.5rem">
      <v-row class="pa-4" align="stretch" justify="start">
        <v-col
            cols="12"
            sm="6"
            md="4"
            lg="3"
            v-for="article in filteredArticles"
            :key="article.id"
        >
          <v-card class="elevation-3">
            <div class="card-header-green"></div>

            <v-card-title class="text-h6 font-weight-bold">
              {{ article.articleName }}
            </v-card-title>
            <v-card-subtitle class="text-body-2 grey--text">
              Nahrané: {{ dayjs(article.createdAt).format('D.M.YYYY') }}
            </v-card-subtitle>
            <v-divider></v-divider>
            <v-card-text class="py-3">
              <div class="text-truncate">
                <strong> Popis: </strong>{{ article.description }}
              </div>
              <v-divider></v-divider>
              <div class="text-truncate">
                <strong>Klúčové slová: </strong> {{ article.keywords}}
              </div>
              <v-divider></v-divider>
              <div class="text-truncate">
                <strong>Kategória: </strong> {{ article.section }}
              </div>
            </v-card-text>
            <v-card-actions>
            <v-row class="w-100" justify="space-between">
              <v-col class="d-flex justify-start " style="padding-left: 1rem">
                <button class="btn btn-primary review" @click="reviewArticle(article.id)" style="color: white">Recenzovať</button>
              </v-col>

              <v-col class="d-flex justify-end" >
                <v-menu offset-y>
                  <template #activator="{ props }">
                    <v-btn
                        class="blue-darken-1 white--text"
                        v-bind="props"
                    >
                      <i class="fa fa-download" style="cursor: pointer;"></i>
                    </v-btn>
                  </template>
                  <v-list>
                    <v-list-item @click="downloadArticle(article.id, 'pdf')">
                      <v-list-item-title style="font-size: 14px">Stiahnuť PDF</v-list-item-title>
                    </v-list-item>
                    <v-list-item @click="downloadArticle(article.id, 'word')">
                      <v-list-item-title style="font-size: 14px">Stiahnuť Word</v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-menu>
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
import dayjs from 'dayjs';

export default {
  computed: {
    dayjs() {
      return dayjs
    }
  },
  data() {
    return {
      searchQuery: '',
      articles: [],
      filteredArticles: [],
      fileName: null,
      emptyArticleList: null
    };
  },
  mounted() {
    this.filteredArticles = this.articles;
    this.fetchAvailableArticles();
  },
  methods: {
    filter() {
      this.filteredArticles = this.articles.filter(article => {
        return article.articleName.toLowerCase().includes(this.searchQuery.toLowerCase());
      });
    },
    async fetchAvailableArticles() {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/api/v1/article/reviewer/assigned", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data.length === 0) {
          this.emptyArticleList = true;
        } else {
          this.emptyArticleList = false;
        }

        this.articles = response.data;
        this.filteredArticles = this.articles;
        console.log(response.data)
      } catch (error) {
        console.error("Failed to verify user role:", error);
        this.articles = [];
      }
    },

    reset() {
      this.searchQuery = '';
      this.filteredArticles = this.articles;
    },

    reviewArticle(id) {
      this.$router.push({ name: 'reviewArticle', params: { id: id }});
    },
    async downloadArticle(id, fileType) {
      await this.getFileName(id, fileType);
      if (this.fileName != null) {
        const extension = this.fileName.substring(this.fileName.lastIndexOf('.'));
        switch (extension.toLowerCase()) {
          case '.pdf':
            fileType = 'pdf';
            break;
          case '.docx':
          case '.doc':
            fileType = 'word';
            break;
          default:
            console.error('Unsupported file type extension:', extension);
            return;
        }
      }
      const token = localStorage.getItem('token');
      const url = `http://localhost:8080/api/v1/file/download/${id}/${fileType}`;
      axios
          .get(url, {
            responseType: 'blob',
            headers: {
              Authorization: `Bearer ${token}`
            }
          })
          .then((response) => {
            let type;
            switch (fileType) {
              case 'pdf':
                type = 'application/pdf';
                break;
              case 'docx':
                type = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
                break;
              case 'doc':
                type = 'application/msword';
                break;
            }
            const blob = new Blob([response.data], { type: type });
            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = this.fileName;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
          })
          .catch((error) => {
            console.error('File download failed:', error);
          });
    },
    async getFileName(id, fileType) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`http://localhost:8080/api/v1/file/fileName/${id}/${fileType}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            });
        this.fileName = response.data;
      } catch (error) {
        console.error('Failed to fetch article name', error);
      }
    },
  }
};
</script>

<style scoped>
.card-header-green {
  height: 5px;
  background-color: #4caf50;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
}
.v-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.v-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.15);
}
.v-btn {
  border-radius: 20px;
  text-transform: none;
}
.v-card-title {
  color: #1b5e20;
}

.v-card-text {
  margin-bottom: 1rem;
}

.v-card-text .text-truncate {
  font-size: 0.875rem;
  color: #555;
}

.filter-header {
  align-items:      center;
  border-bottom:    1px solid #d8d8f0;
  background-color: #f7f7fc;
  display:          flex;
  padding:          .75rem 2.5rem;

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

.review {
  font-size: 15px;
  padding: 4px 8px;
  height: auto;
  width: auto;
  font-weight: bold;
  background-color: #1EB386;
  border-color: #1EB386;
}

.form-input input:focus {
  outline: none;
}
</style>